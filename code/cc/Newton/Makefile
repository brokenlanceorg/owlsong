AR=ar
CC=g++
OBJECT_DIR=../../../build/Newton
LIBRARY_DIR=../../../dist/Newton
LIBS_DIR=../../../dist
COMPILE_OPTIONS=-c -I../Logger -I../Matc -I../Miscellaneous -I../Targa
BUILD_OPTIONS=-o Unit1 -I../Logger -I../Matc -L$(LIBS_DIR)/Logger -L$(LIBS_DIR)/Matc -L$(LIBS_DIR)/Miscellaneous
LINK_OPTIONS=rcs

test: 
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/Unit1.o Unit1.cpp

link: 
	$(CC) $(BUILD_OPTIONS) $(OBJECT_DIR)/Unit1.o -lMisc -lMathmatc -lLogger

clean:
	rm $(OBJECT_DIR)/*.o
	#rm $(LIBRARY_DIR)/*.a NO libs for the project
