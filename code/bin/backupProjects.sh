#!/bin/bash
#
# Simple script to backup our code base and documents.
# We also backup shell environment and vim settings.
# Should be run in the crontab as:
# 00 00 * * * $HOME/projects/code/bin/backupProjects.sh > /dev/null 2>&1;
#

cp $HOME/.bashrc     $HOME/projects/code/bin
cp $HOME/.dir_colors $HOME/projects/code/bin
cp $HOME/.vimrc      $HOME/projects/code/bin

tar cvfz $HOME/lib/projects.tar.gz projects/
