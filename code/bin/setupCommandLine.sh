#
# These settings are valid for a command-line environment.
#
export EDITOR=vim
export COLUMNS=5000;
export PATH=$PATH:$HOME/projects/code/bin;
export OPENSSL_CONF=/home/brandon/projects/ca/conf/openssl.cnf;
export DVBAR=`perl -e 'print "\x{253C}"' 2>/dev/null`;
export VBAR=`perl -e 'print "\x{2502}"' 2>/dev/null`;
export TLDCORNER=`perl -e 'print "\x{2552}"' 2>/dev/null`;
export BLDCORNER=`perl -e 'print "\x{2558}"' 2>/dev/null`;
export HHBAR=`perl -e 'print "\x{2550}"' 2>/dev/null`;
export LVBAR=`perl -e 'print "\x{255E}"' 2>/dev/null`;
export RVBAR=`perl -e 'print "\x{2561}"' 2>/dev/null`;
export PROMPT_COMMAND='PS1="\n\[\e[1;30m\]$TLDCORNER$HHBAR$RVBAR\[\e[m\]\[\e[1;34m\]\u@\h\[\e[m\]\[\e[1;30m\]$LVBAR$HHBAR$RVBAR\[\e[m\]\[\e[1;35m\]\d \@\[\e[m\]\[\e[1;30m\]$LVBAR$HHBAR$RVBAR\[\e[m\]\[\e[1;33m\]uptime: `uptime_txt`\[\e[1;30m\]$LVBAR$HHBAR$RVBAR\[\e[m\]\[\e[1;36m\]load averages: `load_avg`\[\e[m\]\[\e[1;30m\]$VBAR\[\e[m\]\[\e[m\]\n\[\e[1;30m\]$VBAR\[\e[m\][\[\e[0;31m\]\w\[\e[m\]]\n\[\e[1;30m\]$BLDCORNER$HHBAR$RVBAR\[\e[m\]\[\e[1;32m\]\$\[\e[m\] "';
export CSCOPE_DB=/home/brandon/projects/code/java/cscope.out;

alias cl='clear';
alias l='ls -l --color=always';
alias ll='ls -lart --color=always';
alias llh='ls -lhart --color=always';
alias lll='ls -lartR --color=always';
alias la='ls -lrt --color=always';
alias l.='ls -d .* --color=auto'
alias ..='cd ..';
alias r='fc -e -';
alias f='find . -name ';
alias less='less -R';
alias god='cd $HOME/dist';
alias gocc='cd $HOME/projects/code/cc';
alias goj='cd $HOME/projects/code/java';
alias gob='cd $HOME/projects/code/bin';
alias goc='cd $HOME/projects/code/java/common/src/common';
alias godb='cd $HOME/projects/code/java/database/src/database';
alias gof='cd $HOME/projects/code/java/fuzzy/src/fuzzy';
alias gog='cd $HOME/projects/code/java/genetic/src/genetic';
alias gom='cd $HOME/projects/code/java/math/src/math';
alias gos='cd $HOME/projects/code/java/stock/src/stock';
alias gop='cd $HOME/projects/code/java/poc/src/proofofconcept';
alias gol='cd $HOME/projects/code/java/linkparser/src/linkparser';
alias gon='cd $HOME/projects/code/java/functionalNetwork/src/functional/network';
alias gox='cd $HOME/projects/code/java/grafix/src/grafix';
alias goi='cd $HOME/projects/code/java/ifs/src/ifs';
alias gok='cd $HOME/projects/code/java/kinect/src/openkinect';
alias gow='cd $HOME/projects/code/java/wiki/src/wikimedia';
alias gor='cd $HOME/projects/code/ruby/poc';
alias d='hexdump -C';
alias ,f='pushd .';
alias ,a='popd;,f';
alias ,e='cd -';
alias rot13="tr '[A-Za-z]' '[N-ZA-Mn-za-m]'";
alias neth='telnet nethack.alt.org';
alias cron='crontab -l | less';

function f ()
{
   find . -name "*$@*";
}

function fj ()
{
   JARS=`find . -name "*.jar"`;
   for jar in $JARS
   do
      echo "searching jar: $jar";
      jar tvf $jar | grep ${1};
   done
}

function g ()
{
   grep "${1}" `find . -name "*.java"`;
}

function gc ()
{
   grep --color $*;
}

function gx ()
{
   grep "${1}" `find . -name "*.xml"`;
}

function gp ()
{
   grep "${1}" `find . -name "*.properties"`;
}

function v ()
{
   vim `find ~/projects/code/java -name "*${1}*.java"`;
}

function vv ()
{
   vim `find . -name "*${1}*.java"`;
}

function psef ()
{
   if [[ -z "$1" ]]; then
      ps -feww
   else
      ps -feww | grep $1
   fi;
}

function pst ()
{
   ps -O pcpu,lwp -L $1 | sort -nk 2,2;
}

function d2h ()
{
   echo "obase=16;$1" | bc;
}

function h2d ()
{
   echo "ibase=16;obase=10;$1" | bc;
}

function load_avg ()
{
   cat /proc/loadavg;
}

function uptime_txt ()
{
   TEMP=`uptime | awk '{print $3" "$4}' | sed s/","//g`;
   TEMP=$TEMP" "$DVBAR" ";
   TEMP=$TEMP`uptime | awk '{print $6" "$7}' | sed s/","//g`;
   echo -n $TEMP;
}

function c ()
{
   echo "$1" | bc -l;
}

set -o vi;
