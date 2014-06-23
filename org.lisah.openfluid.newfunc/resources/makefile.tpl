# makefile for mhydas function

CPP = g++
OFELIBS = $(shell ofelib-config --libs) -L/usr/lib
OFEFLAGS = $(shell ofelib-config --cflags)
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
	$(CPP) -c $(SRCFILESROOT).cpp -o $(OBJPATH)/$(SRCFILESROOT).o -fPIC $(OFEFLAGS)
	$(CPP) $(OBJPATH)/$(SRCFILESROOT).o $(OFELIBS) -o $(BINPATH)/$(BINFILE).$(PLUGEXT) -shared $(LDFLAGS)


clean:
	rm -f $(BINPATH)/$(BINFILE).$(PLUGEXT)
	rm -f $(OBJPATH)/$(SRCFILESROOT).o

install: all
	 @mkdir -p $(INSTALLPATH)
	 @cp ./$(BINFILE).$(PLUGEXT) $(INSTALLPATH)

