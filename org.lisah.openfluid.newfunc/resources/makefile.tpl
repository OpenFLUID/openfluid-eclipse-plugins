# makefile for mhydas function

CPP = g++
WXLIBS = $(shell wx-config --libs base)
WXFLAGS = $(shell wx-config --cxxflags base)
MHYDASDKLIBS = $(shell ofelib-config --libs) -L/usr/lib
MHYDASDKFLAGS = $(shell ofelib-config --cflags)
BINFILE = $$FUNCTIONID$$
SRCFILESROOT = $$ROOTFILENAME$$

INSTALLPATH = $$INSTALLDIR$$
OBJPATH = .
BINPATH = .

LDFLAGS =
PLUGEXT = sompi
ifeq ($(OSTYPE),msys)
  LDFLAGS=-Wl,--enable-runtime-pseudo-reloc
  PLUGEXT = dllmpi
endif

ifdef FORCEINSTALLPATH
  INSTALLPATH = $(FORCEINSTALLPATH)
endif

ifdef FORCEOBJPATH
  OBJPATH = $(FORCEOBJPATH)
endif

ifdef FORCEBINPATH
  BINPATH = $(FORCEBINPATH)
endif


all:
	$(CPP) -c $(SRCFILESROOT).cpp -o $(OBJPATH)/$(SRCFILESROOT).o -fPIC $(WXFLAGS) $(MHYDASDKFLAGS)
	$(CPP) $(OBJPATH)/$(SRCFILESROOT).o $(WXLIBS) $(MHYDASDKLIBS) -o $(BINPATH)/$(BINFILE).$(PLUGEXT) -shared $(LDFLAGS)


clean:
	rm -f $(BINPATH)/$(BINFILE).$(PLUGEXT)
	rm -f $(OBJPATH)/$(SRCFILESROOT).o

install: all
	 @mkdir -p $(INSTALLPATH)
	 @cp ./$(BINFILE).$(PLUGEXT) $(INSTALLPATH)

