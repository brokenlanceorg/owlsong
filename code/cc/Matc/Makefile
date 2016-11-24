AR=ar
CC=g++
OBJECT_DIR=../../../build/Matc
LIBRARY_DIR=../../../dist/Matc
COMPILE_OPTIONS=-c -I../Logger -I../Targa
LINK_OPTIONS=rcs

compile: Func.o Numerr.o Random.o Mathmatc.o Onevar.o Domain.o Interval.o Twovar.o Newton.o

Func.o: Func.cpp Func.hpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/Func.o Func.cpp 

Numerr.o: Numerr.cpp Numerr.hpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/Numerr.o Numerr.cpp 

Random.o: Random.cpp Random.hpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/Random.o Random.cpp 

Mathmatc.o: Mathmatc.cpp Mathmatc.hpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/Mathmatc.o Mathmatc.cpp 

Onevar.o: Onevar.cpp Onevar.hpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/Onevar.o Onevar.cpp 

Domain.o: Domain.cpp Domain.hpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/Domain.o Domain.cpp 

Interval.o: Interval.cpp Interval.hpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/Interval.o Interval.cpp 

Twovar.o: Twovar.cpp Twovar.hpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/Twovar.o Twovar.cpp 

Newton.o: Newton.cpp Newton.hpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/Newton.o Newton.cpp 

lib: compile
	$(AR) $(LINK_OPTIONS) $(LIBRARY_DIR)/libMathmatc.a $(OBJECT_DIR)/Func.o $(OBJECT_DIR)/Numerr.o $(OBJECT_DIR)/Random.o $(OBJECT_DIR)/Mathmatc.o $(OBJECT_DIR)/Onevar.o $(OBJECT_DIR)/Domain.o $(OBJECT_DIR)/Interval.o $(OBJECT_DIR)/Twovar.o $(OBJECT_DIR)/Newton.o

clean:
	rm $(OBJECT_DIR)/*.o
	rm $(LIBRARY_DIR)/*.a

all: lib
