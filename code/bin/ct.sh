#!/bin/bash
cd /home/brandon
rm tags
ctags -R /home/brandon/projects/code/java/poc /home/brandon/projects/code/java/fuzzy /home/brandon/projects/code/java/fuzzyNetwork /home/brandon/projects/code/java/etc /home/brandon/projects/code/java/NCAA /home/brandon/projects/code/java/functionalNetwork /home/brandon/projects/code/java/common /home/brandon/projects/code/java/stock /home/brandon/projects/code/java/thread /home/brandon/projects/code/java/linkparser /home/brandon/projects/code/java/ifs /home/brandon/projects/code/java/grafix /home/brandon/projects/code/java/database /home/brandon/projects/code/java/math /home/brandon/projects/code/java/genetic --fields=-afikKlmnsSzt+f
