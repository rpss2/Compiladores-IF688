package atividade1;

%%

/* Não altere as configurações a seguir */

%line
%column
%unicode
//%debug
%public
%standalone
%class Minijava
%eofclose

/* Insira as regras léxicas abaixo */

/* Declaracoes */
whitespace 		= [\n\t\r\f ]
comment1Line	= (\/\/(.)*)
commentNLines	= (\/\*(.|\n)*\*\/)
comment			= {comment1Line}|{commentNLines}
letter			= [A-Za-z]
digit			= [0-9]
integer			= [1-9]{digit}*|0
alphanumeric	= {letter}|{digit}
identifier		= ({letter}|[_])({alphanumeric}|[_])*

%%

/* Regras Lexicas */
//palavras reservadas
"boolean"				{ System.out.println("Token boolean"); }
"class"					{ System.out.println("Token class"); }
"public"				{ System.out.println("Token public"); }
"extends"				{ System.out.println("Token extends"); }
"static"				{ System.out.println("Token static"); }
"void"					{ System.out.println("Token void"); }
"main"					{ System.out.println("Token main"); }
"String"				{ System.out.println("Token String"); }
"int"					{ System.out.println("Token int"); }
"while"					{ System.out.println("Token while"); }
"if"					{ System.out.println("Token if"); }
"else"					{ System.out.println("Token else"); }
"return"				{ System.out.println("Token return"); }
"length"				{ System.out.println("Token length"); }
"true"					{ System.out.println("Token true"); }
"false"					{ System.out.println("Token false"); }
"this"					{ System.out.println("Token this"); }
"new"					{ System.out.println("Token new"); }
"System.out.println"	{ System.out.println("Token System.out.println"); }

//operadores
"&&"	{ System.out.println("Token &&"); }
"<"		{ System.out.println("Token <"); }
"=="	{ System.out.println("Token =="); }
"!="	{ System.out.println("Token !="); }
"+"		{ System.out.println("Token +"); }
"-"		{ System.out.println("Token -"); }
"*"		{ System.out.println("Token *"); }
"!"		{ System.out.println("Token !"); }

//delimitadores e pontuacao
";"		{ System.out.println("Token ;"); }
"."		{ System.out.println("Token ."); }
","		{ System.out.println("Token ,"); }
"="		{ System.out.println("Token ="); }
"("		{ System.out.println("Token ("); }
")"		{ System.out.println("Token )"); }
"{"		{ System.out.println("Token {"); }
"}"		{ System.out.println("Token }"); }
"["		{ System.out.println("Token ["); }
"]"		{ System.out.println("Token ]"); }

{whitespace}	{ /* Ignore whitespace */ }	
{comment}		{ /* Ignore comment */ }
{identifier}	{ System.out.println("Token ID (" + yytext() + ")"); }
{integer}		{ System.out.println("Token INT (" + yytext() + ")"); }

    
/* Insira as regras léxicas no espaço acima */     
     
. { throw new RuntimeException("Caractere ilegal! '" + yytext() + "' na linha: " + yyline + ", coluna: " + yycolumn); }