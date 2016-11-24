AR=ar
CC=g++
OBJECT_DIR=../../../build/Targa
LIBRARY_DIR=../../../dist/Targa
COMPILE_OPTIONS=-c -I../Logger -I../Matc -I../Miscellaneous
BUILD_OPTIONS=-o Unit1 -I../Logger -I../Matc -L../Logger -L../Matc -L../Miscellaneous
LINK_OPTIONS=rcs

compile: Targa.o

Targa.o : Targa.cpp Targa.hpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/Targa.o Targa.cpp

test: 
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/Unit1.o Unit1.cpp

link: 
	$(CC) $(BUILD_OPTIONS) $(OBJECT_DIR)/Unit1.o $(OBJECT_DIR)/Targa.o

lib: compile
	$(AR) $(LINK_OPTIONS) $(LIBRARY_DIR)/libTarga.a $(OBJECT_DIR)/Targa.o 

clean:
	rm $(OBJECT_DIR)/*.o
	rm $(LIBRARY_DIR)/*.a
