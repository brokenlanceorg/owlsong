#!/bin/bash
export CP=$HOME/dist/common/common.jar;
export CP=$CP:$HOME/dist/math/math.jar;
export CP=$CP:$HOME/dist/genetic/genetic.jar;
export CP=$CP:$HOME/dist/grafix/grafix.jar;
export CP=$CP:$HOME/dist/functionalNetwork/functionalNetwork.jar;
java -cp $CP functional.network.ManyToOneEnsemble $*;
