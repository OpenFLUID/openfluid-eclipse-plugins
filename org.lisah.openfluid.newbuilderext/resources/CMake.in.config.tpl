# builder-extension ID
SET(BUILDEREXT_ID $$BUILDEREXTID$$)

# list of CPP files
SET(BUILDEREXT_CPP $$ROOTFILENAME$$.cpp)

# list of Fortran files, if any
SET(BUILDEREXT_FORTRAN )

# packages to find and/or to check (example below with sqlite3)
#FIND_PACKAGE(PkgConfig REQUIRED)
#PKG_CHECK_MODULES(sqlite3 REQUIRED sqlite3)


# includes directories for builder-extension compilation (example below with sqlite3)
#SET(BUILDEREXT_INCLUDE_DIRS ${sqlite3_INCLUDE_DIRS})

# libraries directories for builder-extension build (example below with sqlite3)
#SET(BUILDEREXT_LIBRARY_DIRS ${sqlite3_LIBRARY_DIRS})

# libraries to link for function build (example below with sqlite3)
#SET(BUILDEREXT_LINK_LIBS ${sqlite3_LIBRARIES})

# definitions to add at compile time (example below with a dummy definition)
#SET(BUILDEREXT_DEFINITIONS "-Ddummydef")
 