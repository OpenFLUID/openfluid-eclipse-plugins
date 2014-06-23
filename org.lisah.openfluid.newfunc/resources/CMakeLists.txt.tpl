CMAKE_MINIMUM_REQUIRED (VERSION 2.6)

INCLUDE(CMake.in.config)

PROJECT(${FUNC_ID} CXX)


# =========================================================================
#   checking libraries
# =========================================================================
FIND_PACKAGE(PkgConfig REQUIRED)
PKG_CHECK_MODULES(ofelib REQUIRED ofelib)
FIND_PACKAGE(LATEX)



# =========================================================================
#   headers dirs for wx, ofelib, and other libraries
# =========================================================================
INCLUDE_DIRECTORIES(${ofelib_INCLUDE_DIRS})
INCLUDE_DIRECTORIES(${FUNC_INCLUDE_DIRS})



# =========================================================================   
#   process fortran source files if present
# ========================================================================= 
IF(FUNC_FORTRAN)
  ENABLE_LANGUAGE(Fortran)
  SET(CMAKE_Fortran_FLAGS_RELEASE "-funroll-all-loops -fno-f2c -O3")
  SET(FORTRAN_LINK_LIBS "gfortran")
ENDIF(FUNC_FORTRAN)



# =========================================================================
#   function build
# =========================================================================

ADD_LIBRARY(${FUNC_ID} MODULE ${FUNC_CPP} ${FUNC_FORTRAN})
ADD_DEFINITIONS(${ofelib_CFLAGS})
ADD_DEFINITIONS(${FUNC_DEFINITIONS})
SET_TARGET_PROPERTIES(${FUNC_ID} PROPERTIES PREFIX "" SUFFIX "${CMAKE_SHARED_LIBRARY_SUFFIX}mpi")

IF(MSYS)
  SET_TARGET_PROPERTIES(${FUNC_ID} PROPERTIES LINK_FLAGS "-shared")
ENDIF(MSYS)
  
TARGET_LINK_LIBRARIES(${FUNC_ID} ${ofelib_LIBRARIES} ${wxWidgets_LIBRARIES} ${FORTRAN_LINK_LIBS} ${FUNC_LINK_LIBS})



# =========================================================================
#   function documentation
# =========================================================================
IF(PDFLATEX_COMPILER)

  LIST(GET FUNC_CPP 0 CPP_FOR_DOC)

  IF(NOT DOCS_OUTPUT_PATH)
    SET(DOCS_OUTPUT_PATH "${PROJECT_BINARY_DIR}")
  ENDIF(NOT DOCS_OUTPUT_PATH)

  # latex command for doc build
  ADD_CUSTOM_COMMAND(
    OUTPUT "${DOCS_OUTPUT_PATH}/${FUNC_ID}.pdf"
    DEPENDS "${CMAKE_CURRENT_SOURCE_DIR}/${CPP_FOR_DOC}"
    COMMAND "openfluid-engine"
    ARGS "--buddy" "func2doc" "--buddyopts" "inputcpp=${CMAKE_CURRENT_SOURCE_DIR}/${CPP_FOR_DOC},outpudir=${DOCS_OUTPUT_PATH},tplfile=/usr/share/openfluid/engine/func2doc/template/func2doc_tpl.tex,pdf=1"     
  )

  ADD_CUSTOM_TARGET(${FUNC_ID}-doc ALL DEPENDS "${DOCS_OUTPUT_PATH}/${FUNC_ID}.pdf")

ENDIF(PDFLATEX_COMPILER)



# =========================================================================
#   install directives
# =========================================================================  
IF(REPOS_INSTALL_COMPONENT)
  INSTALL(TARGETS ${FUNC_ID} DESTINATION ${REPOS_FUNCTIONS_INSTALL_PATH} COMPONENT ${REPOS_INSTALL_COMPONENT})
  IF(PDFLATEX_COMPILER)
    INSTALL(FILES "${DOCS_OUTPUT_PATH}/${FUNC_ID}.pdf" DESTINATION ${REPOS_FUNCDOCS_INSTALL_PATH} COMPONENT ${REPOS_INSTALL_COMPONENT})
  ENDIF(PDFLATEX_COMPILER)
ELSE(REPOS_INSTALL_COMPONENT)
  INSTALL(TARGETS ${FUNC_ID} DESTINATION "$ENV{HOME}/.openfluid/engine/functions")
ENDIF(REPOS_INSTALL_COMPONENT) 

