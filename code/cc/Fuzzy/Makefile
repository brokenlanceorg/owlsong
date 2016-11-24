AR=ar
CC=g++
OBJECT_DIR=../../../build/Fuzzy
LIBRARY_DIR=../../../dist/Fuzzy
COMPILE_OPTIONS=-c -I../Logger -I../Matc -I../Miscellaneous
BUILD_OPTIONS=-o Unit1 -I../Logger -I../Matc -L../Logger -L../Matc -L../Miscellaneous
LINK_OPTIONS=rcs

compile: FuzzySet.o Hedge.o Holder.o Consequent.o FuzzyFactory.o ConcentrateHedge.o DiffuseHedge.o DiluteHedge.o IntenseHedge.o RestrictHedge.o FuzzyBase.o CompoundConsequent.o MembershipFunc.o

FuzzySet.o : FuzzySet.cpp FuzzySet.hpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/FuzzySet.o FuzzySet.cpp

Hedge.o : Hedge.cpp Hedge.hpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/Hedge.o Hedge.cpp

Holder.o : Holder.cpp Holder.hpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/Holder.o Holder.cpp

Consequent.o : Consequent.cpp Consequent.hpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/Consequent.o Consequent.cpp

FuzzyFactory.o : FuzzyFactory.cpp FuzzyFactory.hpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/FuzzyFactory.o FuzzyFactory.cpp

ConcentrateHedge.o : ConcentrateHedge.cpp ConcentrateHedge.hpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/ConcentrateHedge.o ConcentrateHedge.cpp

DiffuseHedge.o : DiffuseHedge.cpp DiffuseHedge.hpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/DiffuseHedge.o DiffuseHedge.cpp

DiluteHedge.o : DiluteHedge.cpp DiluteHedge.hpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/DiluteHedge.o DiluteHedge.cpp

IntenseHedge.o : IntenseHedge.cpp IntenseHedge.hpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/IntenseHedge.o IntenseHedge.cpp

RestrictHedge.o : RestrictHedge.cpp RestrictHedge.hpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/RestrictHedge.o RestrictHedge.cpp

FuzzyBase.o : FuzzyBase.cpp FuzzyBase.hpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/FuzzyBase.o FuzzyBase.cpp

CompoundConsequent.o : CompoundConsequent.cpp CompoundConsequent.hpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/CompoundConsequent.o CompoundConsequent.cpp

MembershipFunc.o : MembershipFunc.cpp MembershipFunc.hpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/MembershipFunc.o MembershipFunc.cpp

test: 
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/Unit1.o Unit1.cpp

link: 
	$(CC) $(BUILD_OPTIONS) $(OBJECT_DIR)/Unit1.o $(OBJECT_DIR)/FuzzySet.o $(OBJECT_DIR)/Hedge.o $(OBJECT_DIR)/Holder.o $(OBJECT_DIR)/Consequent.o $(OBJECT_DIR)/FuzzyFactory.o $(OBJECT_DIR)/ConcentrateHedge.o $(OBJECT_DIR)/DiffuseHedge.o $(OBJECT_DIR)/DiluteHedge.o $(OBJECT_DIR)/IntenseHedge.o $(OBJECT_DIR)/RestrictHedge.o $(OBJECT_DIR)/FuzzyBase.o $(OBJECT_DIR)/CompoundConsequent.o $(OBJECT_DIR)/MembershipFunc.o -lMisc -lMathmatc -lLogger

lib: compile
	$(AR) $(LINK_OPTIONS) $(LIBRARY_DIR)/libFuzzy.a $(OBJECT_DIR)/FuzzySet.o $(OBJECT_DIR)/Hedge.o $(OBJECT_DIR)/Holder.o $(OBJECT_DIR)/Consequent.o $(OBJECT_DIR)/FuzzyFactory.o $(OBJECT_DIR)/ConcentrateHedge.o $(OBJECT_DIR)/DiffuseHedge.o $(OBJECT_DIR)/DiluteHedge.o $(OBJECT_DIR)/IntenseHedge.o $(OBJECT_DIR)/RestrictHedge.o $(OBJECT_DIR)/FuzzyBase.o $(OBJECT_DIR)/CompoundConsequent.o $(OBJECT_DIR)/MembershipFunc.o

all: lib

clean:
	rm $(OBJECT_DIR)/*.o
	rm $(LIBRARY_DIR)/*.a
