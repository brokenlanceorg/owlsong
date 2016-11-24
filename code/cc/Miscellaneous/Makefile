AR=ar
CC=g++
COMPILE_OPTIONS=-c 
OBJECT_DIR=../../../build/Miscellaneous
LIBRARY_DIR=../../../dist/Miscellaneous
BUILD_OPTIONS=-o Unit1 -I../Logger -I../Matc -L../Logger -L../Matc
LINK_OPTIONS=rcs

compile: FileReader.o ObjectVector.o

FileReader.o: FileReader.cpp FileReader.hpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/FileReader.o FileReader.cpp 

ObjectVector.o: ObjectVector.cpp ObjectVector.hpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/ObjectVector.o ObjectVector.cpp 

lib: compile
	$(AR) $(LINK_OPTIONS) $(LIBRARY_DIR)/libMisc.a $(OBJECT_DIR)/FileReader.o $(OBJECT_DIR)/ObjectVector.o

clean:
	rm $(OBJECT_DIR)/*.o
	rm $(LIBRARY_DIR)/*.a

all: clean lib
