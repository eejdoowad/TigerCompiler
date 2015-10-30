# Makefile

JCC=javac
JFLAGS = -g

# Modify these if you change the project's directory structure
ROOTDIR=../../
OUTDIR=$(ROOTDIR)out/
SOURCEDIR=$(ROOTDIR)src/
TESTDIR=$(ROOTDIR)src/
RESOURCEDIR=$(ROOTDIR)src/

# Gets file paths of headers, sources, and generated objects
SOURCES:=$(wildcard $(SOURCEDIR)*.java)
CLASSES=$(patsubst $(SOURCEDIR)%.java,$(OUTDIR)%.class,$(SOURCES))
RESOURCES=$(wildcard $(RESOURCEDIR)*)
OUTRESOURCES=$(patsubst $(SOURCEDIR)%,$(OUTDIR)%,$(SOURCES))
TESTS=$(wildcard $(TESTDIR)*)

all: $(CLASSES)
# $(TESTDIR) $(RESOURCEDIR)

#$(OUTDIR)%.class: $(SOURCEDIR)%.java
#	$(JCC) -d $(OUTDIR) $< $(JFLAGS)


$(CLASSES): $(SOURCES)
	$(JCC) -d $(OUTDIR) $(JFLAGS) $(SOURCES)

clean:
	-rm $(OUTDIR)*

# The starting - suppresses output value from rm (which fails if files don't exist)

.PHONY: all clean

# End Makefile