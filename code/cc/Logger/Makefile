CC=g++
OBJECT_DIR=../../../build/Logger
LIBRARY_DIR=../../../dist/Logger
COMPILE_OPTIONS=-c
LINK_OPTIONS=rcs

compile : DebugLogger.o

DebugLogger.o : DebugLogger.cpp DebugLogger.hpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/DebugLogger.o DebugLogger.cpp

lib: compile
	$(AR) $(LINK_OPTIONS) $(LIBRARY_DIR)/libLogger.a $(OBJECT_DIR)/DebugLogger.o

all: lib

clean: 
	rm $(OBJECT_DIR)/*.o
	rm $(LIBRARY_DIR)/*.a
