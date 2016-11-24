AR=ar
CC=g++
OBJECT_DIR=../../../build/Genetic
LIBRARY_DIR=../../../dist/Genetic
COMPILE_OPTIONS=-c -I../Logger -I../Matc -I../Miscellaneous
BUILD_OPTIONS=-o Unit1 -I../Logger -I../Matc -L../Logger -L../Matc -L../Miscellaneous
LINK_OPTIONS=rcs

compile: Chromosome.o ValueEncodedChromosome.o MutationStrategy.o ValueEncodedMutationStrategy.o SelectionStrategy.o RouletteWheelSelectionStrategy.o CrossoverStrategy.o SinglePointCrossoverStrategy.o Individual.o GeneticAlgorithm.o

Chromosome.o : Chromosome.cpp Chromosome.hpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/Chromosome.o Chromosome.cpp

ValueEncodedChromosome.o : ValueEncodedChromosome.cpp ValueEncodedChromosome.hpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/ValueEncodedChromosome.o ValueEncodedChromosome.cpp

MutationStrategy.o : MutationStrategy.cpp MutationStrategy.hpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/MutationStrategy.o MutationStrategy.cpp

ValueEncodedMutationStrategy.o : ValueEncodedMutationStrategy.cpp ValueEncodedMutationStrategy.hpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/ValueEncodedMutationStrategy.o ValueEncodedMutationStrategy.cpp

SelectionStrategy.o : SelectionStrategy.cpp SelectionStrategy.hpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/SelectionStrategy.o SelectionStrategy.cpp

RouletteWheelSelectionStrategy.o : RouletteWheelSelectionStrategy.cpp RouletteWheelSelectionStrategy.hpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/RouletteWheelSelectionStrategy.o RouletteWheelSelectionStrategy.cpp

CrossoverStrategy.o : CrossoverStrategy.cpp CrossoverStrategy.hpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/CrossoverStrategy.o CrossoverStrategy.cpp

SinglePointCrossoverStrategy.o : SinglePointCrossoverStrategy.cpp SinglePointCrossoverStrategy.hpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/SinglePointCrossoverStrategy.o SinglePointCrossoverStrategy.cpp

Individual.o : Individual.cpp Individual.hpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/Individual.o Individual.cpp

GeneticAlgorithm.o : GeneticAlgorithm.cpp GeneticAlgorithm.hpp
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/GeneticAlgorithm.o GeneticAlgorithm.cpp

test: 
	$(CC) $(COMPILE_OPTIONS) -o $(OBJECT_DIR)/Unit1.o Unit1.cpp

link: 
	$(CC) $(BUILD_OPTIONS) 

lib: compile
	$(AR) $(LINK_OPTIONS) $(LIBRARY_DIR)/libGenetic.a $(OBJECT_DIR)/Chromosome.o $(OBJECT_DIR)/ValueEncodedChromosome.o $(OBJECT_DIR)/MutationStrategy.o $(OBJECT_DIR)/ValueEncodedMutationStrategy.o $(OBJECT_DIR)/SelectionStrategy.o $(OBJECT_DIR)/RouletteWheelSelectionStrategy.o $(OBJECT_DIR)/CrossoverStrategy.o $(OBJECT_DIR)/SinglePointCrossoverStrategy.o $(OBJECT_DIR)/Individual.o $(OBJECT_DIR)/GeneticAlgorithm.o

clean:
	rm $(OBJECT_DIR)/*.o
	rm $(LIBRARY_DIR)/*.a

all: lib
