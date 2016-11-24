set nu
set ai
set wrap
set nolist
set textwidth=0
set wrapmargin=0
set noeb vb t_vb=
set tags+=/home/brandon/projects/code/java/tags
set path+=/home/brandon/projects/code/java/**
set nocompatible
"set compatible
set diffexpr=MyDiff()
set expandtab
set shiftwidth=3
set smarttab
set ignorecase smartcase
set selection=inclusive
set backup
set backupdir=~/.vim/backup
set nrformats=
abbr hte the
abbr thier their
abbr adv AAdvantage
abbr aaa American Airlines
abbr tkt iTKTS
abbr qkt qTKTS
abbr qtk qTKTS
abbr ppp public
abbr pp protected
abbr p private
abbr arr ArrayList<
abbr carr private ArrayList< String > _anArray;
abbr narr new ArrayList<
abbr hsh HashMap<
abbr chsh private HashMap< PARAM1, PARAM2 > _aHashMap;
abbr nhsh new HashMap<
abbr str String
abbr cstr private String _aString;
abbr nstr new String( "
abbr strb StringBuffer
abbr cstrb private StringBuffer _aStringBuffer;
abbr nstrb new StringBuffer( "
abbr dbl Double
abbr cdbl private Double _aDouble;
abbr ndbl new Double(
abbr dl double
abbr inte Integer
abbr cinte private Integer _anInteger;
abbr ninte new Integer(
abbr rtn return
abbr antp !ant -projecthelp
set t_Co=256
colorscheme calmar256-dark
"colorscheme elflord
map v <C-F>
map f <C-B>
map g <C-W>
map t <C-Q>
map U o{<CR>}<ESC>k
map ; @
map s 0i// <ESC>j
map S 03x<ESC>j
map C :!ant all<CR>
map W owhile( COND )<CR>{<CR>COMMAND;<CR>}<ESC>kkkww
map F :call Bump()<CR>ofor( int <C-r>z=0; <C-r>z<COND; <C-r>z++ )<CR>{<CR>COMMAND;<CR>}<ESC>kkkwwwwwwwwwmp
map ,F ofor( TYPE obj : COLLECTION )<CR>{<CR>COMMAND;<CR>}<ESC>kkk0wwwmp
map T otry<CR>{<CR>COMMAND;<CR>}<CR>catch( TYPE e )<CR>{<CR>EXCEPTION;<CR>}<ESC>kkkkk0w
map Z o/**<CR><CR>/<CR>private TYPE method( TYPE )<CR>{<CR>COMMAND;<CR>}<CR><ESC>k,>kkw
map ,Z o/**<CR><CR>@param TYPE<CR>@return TYPE<CR>/<CR>public TYPE method( TYPE )<CR>{<CR>COMMAND;<CR>}<CR><ESC>k,>kkw
map ,,Z mb"aywt?\(private\\|public\\|protected\)<CR>w"by'a,Zmb:'a,'b s/TYPE/<C-R>b/g<CR>,givar <ESC>wwwcw<C-R>a = var<ESC>?method<CR>cwset<C-R>a<ESC>bht?\(private\\|public\\|protected\)<CR>wcvoid<ESC>kkdd'a,Zmb:'a,'b s/TYPE/<C-R>b/g<CR>/method<CR>cwget<C-R>a<ESC>wDA()<ESC>/COMMAND<CR>cwreturn <C-R>a<ESC>kkkkkdd'a17jmA:'a,'A s/\([a-z]\) \+/\1 /g<CR>:'a,'A s/\([a-z]\) (/\1(/g<CR><F4>
map Y oif( COND )<CR>{<CR>COMMAND;<CR>}<ESC>kkkww
map ,Y oif [ COND ]; then<CR>COMMAND;<CR><BS>fi;<ESC>?COND<CR>
map K q:k<CR>
map ,u <C-R>
map ,,u ,d/extends<CR>w,j<TAB><CR><F4>
map <TAB> :bn<CR>
map ,k <C-W>+
map ,,k <C-W>-
map ,K <C-W>>
map ,,K <C-W><
map ,<TAB> :bN<CR>
map ,,<TAB> :bla<CR>
map ,v <C-A>
map ,,v <C-X>
map ,,d :e .<CR>
map ,w ma"aywbbea <ESC>bi <ESC>w"byw?[A-Z][a-z]* \+<TAB> *[,;)>=]<CR>bi <ESC>w:!vl <TAB> "\(public\\|protected\\|private\) .*<C-R>a("<CR><CR>uuu<F4>
map ,w <C-]>
map ,e <C-t>
map E ?^ +(public\|protected\|private)<CR><F4>zt
map B /^ +(public\|protected\|private)<CR><F4>zt
map <SPACE> :1match Error '^ \+\(public\\|protected\\|private\) .*'<CR>:2match DiffChange '_[A-Za-z]\+'<CR>
map <SPACE> ,bi <ESC>,ba <ESC>w
map ,<SPACE> :let @/=""<CR>:match<CR>:2mat<CR>:3mat<CR>
map ,a <C-O>
map ,f <C-I>
map ,g $
map ,b %
map <CR> %
map ,c :e _BR_<CR>,,ad:silent read !ant<CR>:g/java:[0-9]\+: warning/d<CR>,s/(java:[0-9]+\|BUILD SUCCESSFUL)<CR>
map C :e _BR_<CR>,,ad:silent read !ant dist<CR>:g/java:[0-9]\+: warning/d<CR>,s/(java:[0-9]+\|BUILD SUCCESSFUL)<CR>
map ,d :1<CR>
map ,s :w<CR>
map <BS> :qall<CR>
map ,<BS> :qall!<CR>
map <F12> :set number!<CR>
map <F4> :let @/=""<CR>:match<CR>:2mat<CR>:3mat<CR>
map ,t :1<CR>ipackage PACKAGE;<CR><CR>import IMPORT;<CR><CR>/**<CR><CR>/<CR>public class CLASS extends EXTEND implements IMPLEMENT<CR>{<CR><CR>// Class Level Variables<CR><BS><BS><BS><CR>/**<CR><CR>/<CR>public CLASS()<CR>{<CR>}<CR><CR>}<ESC>H
map ,P :1<CR>i#!/usr/bin/perl<CR><BS><CR>sub readLogFiles<CR>{<CR>my @_files = glob( "FILES" );<CR>foreach( @_files )<CR>{<CR>my $_fileName = $_;<CR>open( THE_FILE, "<$_fileName" ) or die( "Unable to open file! $!" );<CR>while( <THE_FILE> )<CR>{<CR>handleLogLine( $_ );<CR>}<CR>close( THE_FILE );<CR>}<CR>}<CR><CR>sub handleLogLine<CR>{<CR>my $line = $_;<CR><CR>if( $line =~ m/PATTERN/g )<CR>{<CR>print( $line );<CR>}<CR>}<CR><CR>readLogFiles();<ESC>?FILES<CR>ma/PATTERN<CR>'a,gbbb
map <F5> :%s/^ *//g<CR>:%s/^\*/ \*/g<CR>:%s/\(.\+\){/\1\r{/ge<CR>:%s/{\(.\+\)/{\r\1/ge<CR>:%s/\(.\+\)}/\1\r}/ge<CR>:%s/}\(.\+\)/}\r\1/ge<CR>
map ,l :set nowrapscan<CR>/{<CR>>i{,l
map ,C <F5><F5>,d,l
" Insert spaces inside paranthesis -- normally this won't be an issue:
map ,p "+p
map ,> >i{
map ,< <i{
" Search and replace -- the first is for single file, while the second is for all buffers:
map ,r te"ayq:i%s/<C-R>a//g<ESC>hi
map ,,r te"ayq:iargdo %s/<C-R>a//g \| update<ESC>bbbhi
map ,S oSystem.out.println( "" );<ESC>bbli
" Getters and setters -- assumes that the class variable is pre-pended with an underscore:
map ,G t/_<CR>be"aynlte"byGk,Zcwvoid<ESC>3kma6jmb:'a,'bs/TYPE/<C-R>a/g<CR>,gi<C-R>b <ESC>?method<CR>cwset<C-R>b<ESC>b3l~2kdd/COMMAND<CR>cw_<C-R>b = <C-R>b<ESC>Gk,Z3kma6jmb:'a,'bs/TYPE/<C-R>a/g<CR>/method<CR>cwget<C-R>b<ESC>b3l~2whDA)<ESC>3kdd/COMMAND<CR>cwreturn _<C-R>b<ESC>
map ,G mo:call CreateAccessors()<CR>'o
map ,x te"ay:cs f c <C-R>a<CR><F4>
map ,m :cs f e 
map ,j ,m(class\|interface) 
map ,n :'a,'bs/0/\=INC(1)/g<CR>
map ,N :let g:I=0<CR>:'a,'bs/0/\=INC(1)/g<CR>
map ,q :q!<CR>
map ,z :call MoveCursor( "z" )<CR>
map ,l :s/\([<,]\) \+/\1/g<CR>:'<,'>s/ \+>/>/g<CR>:'<,'>s/ \+/              /g<CR>:'<,'>s/^ \+/   /g<CR>:'<,'>s/<\([A-Za-z]\)/< \1/g<CR>:'<,'>s/\([A-Za-z]\)>/\1 >/g<CR>:'<,'>s/\([>,]\)/\1 /g<CR>:let @/=""<CR>
map ,l 0:call FillInForLoop()<CR>
map ,z z=
map zl ]s
map zh [s
map / /\v
map ? ?\v
map ,Q :vs cal<CR>:read !cal -y<CR>:mat Error "^ \+[0-9]\{4,4} \+$"<CR>:2mat DiffChange "[A-Z][a-z]\{2,}"<CR>:3mat Folded "\([A-Z][a-z]\)"<CR>
map ,X :!tfvi.sh<CR>
map X :call cursor( line( "." ), col( "." ) + ( ( col( "$" ) - col( "." ) ) / 3 ) )<CR>
map ,T :sp new<CR>:read !top -b -n 1<CR>:%s/ \+$//g<CR>,d:3mat Error "\(^ \+PID.*COMMAND$\)"<CR>:mat DiffAdd "^top - .*$"<CR>:2mat Folded "[0-9]\+"<CR>
map ,1 :set spell!<CR>
map ,2 <F2>
map ,3 <F12>
map ,4 :set nrformats=alpha,octal,hex<CR>
map ,,1 :NERDTree<CR>
map ,,a ,dVG
set pastetoggle=<F2>
map ,,s mi:call GetCommentChar()<CR>:g/DEBUG >/.+1,/DEBUG </-1 s/^/<C-R>o/g<CR>:let @/=''<CR>'i
map ,,S mi:call GetCommentChar()<CR>:g/DEBUG >/.+1,/DEBUG </-1 s/^<C-R>o//g<CR>:let @/=''<CR>'i
map ,,b :bd!<CR>
map s :call GetCommentChar()<CR>:s/^/<C-R>o/g<CR>:let @/=''<CR>j
map S :call GetCommentChar()<CR>:s/^<C-R>o//g<CR>:let @/=''<CR>j
map ,i mite"iy:cs f e (class\|interface) <C-R>i<CR>:1<CR>/package<CR>"zyy,e?^import <CR>"zpcwimport<ESC>,gi.<C-R>i<ESC>'i:call GotoMarkInLine()<CR>:let @/=""<CR>
map <CR> :call GotoMarkInLine()<CR>
map ,<CR> :sh<CR>
map ,,<CR> :!
map ,,w ,j<TAB><CR>
map ,,f wwt/:<CR>"nybbbbteey:find <C-R>"<CR>:<C-R>n<CR>
map ,,t 0wt/ <CR>:s/\./\//g<CR>0wt/ <CR>"ay0w/ <CR>bte"by:!mkdir -p src/$(dirname <C-R>a.java)<CR><CR>u:let @/=""<CR>:e src/<C-R>a.java<CR>,twcw<C-R>a<ESC>bbDA;<ESC>:s/\//\./g<CR>:%s/CLASS/<C-R>b/g<CR>
map ,,i 0wmpt/ <CR>"ay,d/import<CR>Oimport <C-R>a;<ESC>'pt/ <CR>bx,<SPACE>
map ,,j tey:fin <C-R>"<CR>
map ,,g te"bybbtey?([^,%*+&\|!= ) ]+) +<C-R>"<CR>tey:fin <C-R>"<CR>Gk,Zwcw<C-R>b<ESC>bb
inoremap <F9> <C-x><C-u>
inoremap <F10> <C-x><C-l>
inoremap <TAB> <C-n>
inoremap ,<TAB> <C-x><C-u>
inoremap ,' <C-x><C-l>
inoremap ,, <ESC>
inoremap ,; <ESC>A;<ESC>
inoremap ,o <ESC>hbh<C-V>0wyA = new <C-R>"();<ESC>
inoremap ,a <ESC>:call SaveCursor()<CR>?for<CR>/=<CR>b"zyw:let g:forindex=@z<CR>:call RestoreCursor()<CR>:let @/=""<CR>a[ <C-r>z ]
inoremap ,A []
inoremap ,g .get( <C-r>z )
inoremap ,s .size()
inoremap ,g <ESC>:call SaveCursor()<CR>?for<CR>/=<CR>b"zyw:let g:forindex=@z<CR>:call RestoreCursor()<CR>:let @/=""<CR>a.get( <C-r>z )
inoremap ,x .size()
inoremap ,l .length()
inoremap ,L .length
inoremap ,t .trace( "" );<ESC>bba
inoremap ,d .debug( "" );<ESC>bba
inoremap ,i .info( "" );<ESC>bba
inoremap ,w .warn( "" );<ESC>bba
inoremap ,e .error( "" );<ESC>bba
inoremap ,f .fatal( "" );<ESC>bba
inoremap ,n = null;<ESC>
inoremap ,r <ESC>la + 
inoremap ,b ( )<ESC>hi 
inoremap ,s ""<ESC>i
inoremap ,p private
inoremap ,P public
inoremap ,z <C-x>s
inoremap ,w Sindi<ESC>?\.<CR>b<C-V>ey?\v[A-Za-z0-9]+ +<C-R>"<CR><C-V>ey/Sindi<CR>:call BuildMethods()<CR>cw<C-R>=DrawPopup()<CR>
inoremap ,m Sindi<ESC>?\.<CR>b<C-V>ey?\v[A-Za-z0-9]+ +<C-R>"<CR><C-V>ey/Sindi<CR>:call BuildMethods()<CR>cw<C-R>=DrawPopup()<CR>
inoremap ,2 <C-O>:set paste<CR>
inoremap ,<CR> <C-E><ESC>
cmap <TAB> <C-r><C-a>
vmap * y/<C-u>\V<C-R>"<CR>
vmap # y?<C-u>\V<C-R>"<CR>
vmap X y:read !echo "<C-R>"" \| bc \-l<CR>
vmap ,r "ayq:i%s/<C-R>a//g<ESC>hi
vmap ,,r "ayq:iargdo %s/<C-R>a//g \| update<ESC>bbbhi
vmap ,n :s/0/\=INC(1)/g<CR>
vmap ,N :let g:I=0<CR>:s/0/\=INC(1)/g<CR>
vmap ,c cSINDI<ESC>:read !echo "<C-R>"" \| bc -l<CR>t,g"addd:%s/SINDI/<C-R>a/g<CR>
vmap ,h :D2h<CR>
vmap ,d :H2d<CR>
" 
" INC
" 
let g:I=0
function! INC(increment)
   let g:I = g:I + a:increment
   return g:I
endfunction
" 
" MyDiff
" 
function MyDiff()
  let opt = '-a --binary '
  if &diffopt =~ 'icase' | let opt = opt . '-i ' | endif
  if &diffopt =~ 'iwhite' | let opt = opt . '-b ' | endif
  let arg1 = v:fname_in
  if arg1 =~ ' ' | let arg1 = '"' . arg1 . '"' | endif
  let arg2 = v:fname_new
  if arg2 =~ ' ' | let arg2 = '"' . arg2 . '"' | endif
  let arg3 = v:fname_out
  if arg3 =~ ' ' | let arg3 = '"' . arg3 . '"' | endif
  let eq = ''
  if $VIMRUNTIME =~ ' '
    if &sh =~ '\<cmd'
      let cmd = '""' . $VIMRUNTIME . '\diff"'
      let eq = '"'
    else
      let cmd = substitute($VIMRUNTIME, ' ', '" ', '') . '\diff"'
    endif
  else
    let cmd = $VIMRUNTIME . '\diff'
  endif
  silent execute '!' . cmd . ' ' . opt . arg1 . ' ' . arg2 . ' > ' . arg3 . eq
endfunction
" 
" D2h
" 
command! -nargs=? -range D2h call s:D2h(<line1>, <line2>, '<args>')
function! s:D2h(line1, line2, arg) range
  if empty(a:arg)
    if histget(':', -1) =~# "^'<,'>" && visualmode() !=# 'V'
      let cmd = 's/\%V\<\d\+\>/\=printf("0x%x",submatch(0)+0)/g'
    else
      let cmd = 's/\<\d\+\>/\=printf("0x%x",submatch(0)+0)/g'
    endif
    try
      execute a:line1 . ',' . a:line2 . cmd
    catch
      echo 'Error: No decimal number found'
    endtry
  else
    echo printf('%x', a:arg + 0)
  endif
endfunction
" 
" H2d
" 
command! -nargs=? -range H2d call s:H2d(<line1>, <line2>, '<args>')
function! s:H2d(line1, line2, arg) range
  if empty(a:arg)
    if histget(':', -1) =~# "^'<,'>" && visualmode() !=# 'V'
      let cmd = 's/\%V0x\x\+/\=submatch(0)+0/g'
    else
      let cmd = 's/0x\x\+/\=submatch(0)+0/g'
    endif
    try
      execute a:line1 . ',' . a:line2 . cmd
    catch
      echo 'Error: No hex number starting "0x" found'
    endtry
  else
    echo (a:arg =~? '^0x') ? a:arg + 0 : ('0x'.a:arg) + 0
  endif
endfunction
" 
" Table test
" 
function! Table( title, ...)
   echo " Title "
   echon a:title
   echo " None "
   echo a:0 . " items:"
   for s in a:000
      echo ' ' . s
   endfor
endfunction
" 
" Bump
" 
let g:forindex = '-'
let @z = g:forindex
function! Bump()
   if g:forindex == '-'
      let g:forindex = 'i'
   elseif g:forindex == 'i'
      let g:forindex = 'j'
   elseif g:forindex == 'j'
      let g:forindex = 'k'
   elseif g:forindex == 'k'
      let g:forindex = 'r'
   elseif g:forindex == 'r'
      let g:forindex = 's'
   elseif g:forindex == 's'
      let g:forindex = 't'
   elseif g:forindex == 't'
      let g:forindex = 'u'
   elseif g:forindex == 'u'
      let g:forindex = 'w'
   elseif g:forindex == 'w'
      let g:forindex = 'x'
   elseif g:forindex == 'x'
      let g:forindex = 'y'
   elseif g:forindex == 'y'
      let g:forindex = 'z'
   elseif g:forindex == 'z'
      let g:forindex = 'v'
   elseif g:forindex == 'v'
      let g:forindex = 'i'
   endif
   let @z = g:forindex
endfunction
" 
" ShiftRightOne
" 
function ShiftRightOne() range
   let _c = col( "'a" ) . "lh"
   let _l = a:firstline
   while _l <= a:lastline
      let _c = _c . "i ,,j"
      let _l += 1
   endwhile
   let _c = _c . "'a"
   execute "normal " . _c
endfunction
" 
" SaveCursor
" 
function SaveCursor()
   let g:tcursor = col( "." )
   let g:tline   = line( "." )
endfunction
" 
" RestoreCursor
" 
function RestoreCursor()
   call cursor( g:tline, g:tcursor )
endfunction
" 
" MoveCursor
" 
function MoveCursor( register )
   let lineNumber   = line( "'" . a:register )
   let columnNumber = col( "'" . a:register )
   call cursor( lineNumber, columnNumber )
endfunction
" 
" GotoMarkInLine
" 
function GotoMarkInLine()
   redir => _marks
   silent execute "marks"
   redir END
   let _lmarks = split( _marks, '\n' )
   let _curr = line( '.' )
   for _mark in _lmarks
      let _words = split( _mark, ' \+' )
      if _curr == _words[ 1 ] && _words[ 0 ] =~ '[a-z]\+'
         call cursor( _words[ 1 ], _words[ 2 ] + 1 )
      endif
   endfor
endfunction
" 
" CreateAccessors
" 
function CreateAccessors()
   let strip = substitute( getline( '.' ), "\\(private\\|protected\\|public\\|final\\|static\\)", "", "g" )
   let strip = substitute( strip, " \\+=.*$", "", "g" )
   let strip = substitute( strip, "^ \\+", "", "g" )
   let strip = substitute( strip, ";", "", "g" )
   let _strings = split( strip, ' \+_' )
   let _cmd = "Gki   ,,,Z"
   "echom "strings 0: " . _strings[ 0 ]
   "echom "strings 1: " . _strings[ 1 ]
   execute "normal " . _cmd
   execute "normal cwvoid"
   execute "normal w"
   let @o = _strings[ 0 ]
   let @p = _strings[ 1 ]
   execute "normal cwset\<C-R>p\<ESC>blll~wwcw\<C-R>o \<C-R>p\<ESC>jj0wcw_\<C-R>p = \<C-R>p\<ESC>kkkkddkwwwcw\<C-R>o"
   execute "normal " . _cmd
   execute "normal cw\<C-R>o\<ESC>wcwget\<C-R>p\<ESC>blll~wwdwhxjj0wcwreturn _\<C-R>p\<ESC>kkkkbcw\<C-R>o\<ESC>kdd"
endfunction
" 
" GetCommentChar
" 
function GetCommentChar()
   if @% =~ '.*pl$'
      let @o = "# "
   elseif @% =~ '.*java$'
      let @o = "\\/\\/ "
   elseif @% =~ '.*sh$'
      let @o = "# "
   elseif @% =~ '.*py$'
      let @o = "# "
   elseif @% =~ '.*jy$'
      let @o = "# "
   elseif @% =~ '.*sql$'
      let @o = "-- "
   elseif @% =~ '.*vimrc$'
      let @o = '" '
   endif
endfunction
" 
" BuildMethods
" 
function BuildMethods()
   "execute 'read !grep "^ \+public" `find /home/brandon/projects/code/java -regex "\(' . GetFileRegex() . '\)"`|sed s/{//g|awk ''BEGIN{state==0;ORS=" ";}{for(i=1;i<=NF;i++){if($i~/\(/)state=1;if(state==1)print $i;if(i==NF){state=0;printf"\n";}}}'''
   execute 'read !find /home/brandon/projects/code/java -name ' . @" . '.java | xargs ctags -x | grep method | tr -s " " | cut -d " " -f 1,5-'
   execute 'normal moNjV''odN'
endfunction
" 
" DrawPopup
" 
function DrawPopup()
   let _methods = split( @1, '\n' )
   call complete(col('.'), _methods )
   return ''
endfunction
" 
" GetFileRegex
" 
function GetFileRegex()
   let localFile = @"
   let regexFile = ''
   while len( localFile ) > 0
      let regexFile = '.*/' . localFile . '.java\|' . regexFile
      let localFile = system( 'find /home/brandon/projects/code/java -name ' . localFile . '.java | xargs grep "public \(abstract \)\?class" | sed s/" "/\\n/g | awk ''{if(FLAG==1){print $1;FLAG=0;}if($1~/extends/)FLAG=1;}''|sed s/\<//g' )
      let localFile = substitute( localFile, '\n', '', 'g' )
   endwhile
   return regexFile
endfunction
"
" FillInForLoop
"
function FillInForLoop()
   let pos = search( 'Map', '', line( '.' ) )
   if pos == 0
"       echom 'In collection/set scenario'
      let pos = search( '<', '', line( '.' ) )
      execute 'normal wte"ay'
      let pos = search( '=\|$', '', line( '.' ) )
      execute 'normal bte"cy'
      execute 'normal ''p'
      let pos = search( ':', '', line( '.' ) )
      if pos == 0
"          echom 'In C for loop'
         let n = line( '.' )
         let l = getline( n )
         let l = substitute( l, 'COND', @c . '.size()', '' )
         call setline( n, l )
         execute 'normal jj'
         let n = line( '.' )
         let l = getline( n )
         let l = substitute( l, 'COMMAND', @a . ' v = ' . @c . '.get( i )', '' )
         call setline( n, l )
      else
"          echom 'In generics loop'
         let n = line( '.' )
         let l = getline( n )
         let l = substitute( l, 'TYPE', @a, '' )
         let l = substitute( l, 'COLLECTION', @c, '' )
         call setline( n, l )
         execute 'normal jj0w'
      endif
   else
"       echom 'In map scenario'
      let pos = search( '<', '', line( '.' ) )
      execute 'normal w"aywwwti>owww"by'
      let pos = search( '=\|$', '', line( '.' ) )
      execute 'normal bte"cy'
      execute 'normal ''p'
      let @b = substitute( @b, ' \+$', '', '' )
      let pos = search( ':', '', line( '.' ) )
      if pos == 0
"          echom 'In C for loop'
         let n = line( '.' )
         let l = getline( n )
         let l = substitute( l, 'COND', @c . '.keySet().size()', '' )
         call setline( n, l )
         execute 'normal jjyyP'
         let n = line( '.' )
         let l = getline( n )
         let l = substitute( l, 'COMMAND', @a . ' key = ' . @c . '.keySet().get( i )', '' )
         call setline( n, l )
         execute 'normal j'
         let n = line( '.' )
         let l = getline( n )
         let l = substitute( l, 'COMMAND', @b . ' v = ' . @c . '.get( key )', '' )
         call setline( n, l )
      else
"          echom 'In generics loop'
         let n = line( '.' )
         let l = getline( n )
         let l = substitute( l, 'TYPE', @a, '' )
         let l = substitute( l, 'COLLECTION', @c . '.keySet()', '' )
         call setline( n, l )
         execute 'normal jj0w'
         let n = line( '.' )
         let l = getline( n )
         let l = substitute( l, 'COMMAND', @b . ' v = ' . @c . '.get( obj )', '' )
         call setline( n, l )
      endif
   endif
endfunction
