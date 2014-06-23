CMAKE_MINIMUM_REQUIRED (VERSION 2.8)

PROJECT(${BUILDEREXT_ID} CXX)

INCLUDE(CMake.in.config)



# =========================================================================
#   checking libraries and tools
# =========================================================================

FIND_PACKAGE(PkgConfig REQUIRED)

IF(NOT openfluid_FOUND)
  PKG_CHECK_MODULES(openfluid REQUIRED openfluid)
ENDIF(NOT openfluid_FOUND)

PKG_CHECK_MODULES(GTKMM REQUIRED gtkmm-2.4)

# =========================================================================
#   include and link directories
# =========================================================================

INCLUDE_DIRECTORIES(${openfluid_INCLUDE_DIRS} ${Boost_INCLUDE_DIRS} ${GTKMM_INCLUDE_DIRS})
INCLUDE_DIRECTORIES(${BUILDEREXT_INCLUDE_DIRS})

LINK_DIRECTORIES(${openfluid_LIBRARY_DIRS} ${Boost_LIBRARY_DIRS} ${GTKMM_LIBRARY_DIRS})
LINK_DIRECTORIES(${BUILDEREXT_LIBRARY_DIRS})


# =========================================================================   
#   process fortran source files if present
# ========================================================================= 

IF(BUILDEREXT_FORTRAN)
  ENABLE_LANGUAGE(Fortran)
  SET(CMAKE_Fortran_FLAGS_RELEASE "-funroll-all-loops -fno-f2c -O3")
  SET(FORTRAN_LINK_LIBS "gfortran")
ENDIF(BUILDEREXT_FORTRAN)


# =========================================================================
#   definitions
# =========================================================================

ADD_DEFINITIONS(-DOPENFLUID_VERSION=${openfluid_VERSION})
ADD_DEFINITIONS(${openfluid_CFLAGS})
ADD_DEFINITIONS(${BUILDEREXT_DEFINITIONS})


# =========================================================================
#   function build
# =========================================================================

# workaround for CMake bug with MinGW
IF(MINGW)
  ADD_LIBRARY(${BUILDEREXT_ID} SHARED ${BUILDEREXT_CPP} ${BUILDEREXT_FORTRAN})
ELSE(MINGW)
  ADD_LIBRARY(${BUILDEREXT_ID} MODULE ${BUILDEREXT_CPP} ${BUILDEREXT_FORTRAN})
ENDIF(MINGW)

SET_TARGET_PROPERTIES(${BUILDEREXT_ID} PROPERTIES PREFIX "" SUFFIX "${CMAKE_SHARED_LIBRARY_SUFFIX}bepi")
  
TARGET_LINK_LIBRARIES(${BUILDEREXT_ID} ${openfluid_LIBRARIES} ${Boost_LIBRARIES} ${FORTRAN_LINK_LIBS} ${BUILDEREXT_LINK_LIBS})


# =========================================================================
#   install directives
# =========================================================================

IF(NOT USER_FUNCTIONS_INSTALL_PATH)
 SET(USER_FUNCTIONS_INSTALL_PATH "$ENV{HOME}/.openfluid/builder-extensions")
 IF(WIN32)
   SET(USER_FUNCTIONS_INSTALL_PATH "$ENV{USERPROFILE}/openfluid/builder-extensions") 
 ENDIF(WIN32)
ENDIF(NOT USER_FUNCTIONS_INSTALL_PATH)

IF(REPOS_INSTALL_COMPONENT)
  INSTALL(TARGETS ${BUILDEREXT_ID} DESTINATION ${REPOS_BUILDEREXTENSIONS_INSTALL_PATH} COMPONENT ${REPOS_INSTALL_COMPONENT})
ELSE(REPOS_INSTALL_COMPONENT)
  INSTALL(TARGETS ${BUILDEREXT_ID} DESTINATION "${USER_FUNCTIONS_INSTALL_PATH}")
ENDIF(REPOS_INSTALL_COMPONENT) 

