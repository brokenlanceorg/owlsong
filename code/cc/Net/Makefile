AR=ar
CC=g++
OBJECT_DIR=../../../build/Net
LIBRARY_DIR=../../../dist/Net
LIBS_DIR=../../../dist
RUN_DIR=../../../run
COMPILE_OPTIONS=-c -I../Logger -I../Matc -I../Miscellaneous
BUILD_OPTIONS=-I../Logger -I../Matc -L$(LIBS_DIR)/Logger -L$(LIBS_DIR)/Matc -L$(LIBS_DIR)/Miscellaneous
LINK_OPTIONS=rcs

compile: Neural.o Backprop.o ModBackprop.o NewtonNet.o

Neural.o: Neural.cpp Neural.hpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/Neural.o Neural.cpp

Backprop.o: Backprop.cpp Backprop.hpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/Backprop.o Backprop.cpp

ModBackprop.o: ModBackprop.cpp ModBackprop.hpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/ModBackprop.o ModBackprop.cpp

NewtonNet.o: NewtonNet.cpp NewtonNet.hpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/NewtonNet.o NewtonNet.cpp

test: Unit1.cpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/Unit1.o Unit1.cpp

link: test
	$(CC) -o $(RUN_DIR)/Unit1 $(BUILD_OPTIONS) $(OBJECT_DIR)/Unit1.o $(OBJECT_DIR)/Neural.o $(OBJECT_DIR)/Backprop.o $(OBJECT_DIR)/ModBackprop.o $(OBJECT_DIR)/NewtonNet.o -lMisc -lMathmatc -lLogger

lib: compile
	$(AR) $(LINK_OPTIONS) $(LIBRARY_DIR)/libNeuralNet.a $(OBJECT_DIR)/Neural.o $(OBJECT_DIR)/Backprop.o $(OBJECT_DIR)/ModBackprop.o $(OBJECT_DIR)/NewtonNet.o

clean:
	rm $(OBJECT_DIR)/*.o
	rm $(LIBRARY_DIR)/*.a

all: lib
