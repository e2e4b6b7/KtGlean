parser grammar AngleParser;

options {
	tokenVocab=AngleLexer;
}

schemas : (schema | evolves)*;

evolves : SCHEMA QVIDENT EVOLVES QVIDENT;

schema : SCHEMA QVIDENT inherit? BRACE_OPEN schemadecl* BRACE_CLOSE;

inherit : COLON QVIDENT (COMMA QVIDENT)* COMMA?;

schemadecl : typedef | predicate | import_ /*| derivedecl*/;

typedef : TYPE ident EQ type;

import_ : IMPORT QVIDENT;

predicate : PREDICATE ident COLON key value? /*deriving*/;

key : type;

value : ARROW type;


type : record | sum | array | maybe | enum_ | reference;

record : BRACE_OPEN (fielddef (COMMA fielddef)* COMMA?)? BRACE_CLOSE;

sum : BRACE_OPEN (fielddef (PIPE fielddef)* PIPE?)? BRACE_CLOSE;

fielddef : ident COLON type;

array : SQBR_OPEN type SQBR_CLOSE;

maybe : MAYBE type;

enum_ : ENUM BRACE_OPEN (ident (PIPE ident)* PIPE?)? BRACE_CLOSE;

reference : QIDENT | ident;


ident : PREIDENT | ENUM | MAYBE | TYPE | IMPORT | PREDICATE | SCHEMA;
