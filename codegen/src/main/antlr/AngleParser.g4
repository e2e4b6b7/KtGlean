parser grammar AngleParser;

options {
	tokenVocab=AngleLexer;
}

schemas : (schema | evolves)*;

evolves : SCHEMA QVIDENT EVOLVES QVIDENT;

schema : SCHEMA QVIDENT inherit? BRACE_OPEN schemadecl* BRACE_CLOSE;

inherit : COLON QVIDENT (COMMA QVIDENT)* COMMA?;

schemadecl : typedef | predicate | import_ /*| derivedecl*/;

import_ : IMPORT QVIDENT;

predicate : PREDICATE IDENT COLON key value? /*deriving*/;

key : type;

value : ARROW type;

type : reference | record | array | maybe | enum_ | sum;

sum : BRACE_OPEN (fielddef (PIPE fielddef)* PIPE?)? BRACE_CLOSE;

enum_ : ENUM BRACE_OPEN (IDENT (PIPE IDENT)* PIPE?)? BRACE_CLOSE;

maybe : MAYBE type;

array : SQBR_OPEN type SQBR_CLOSE;

reference : QIDENT | IDENT;

record : BRACE_OPEN (fielddef (COMMA fielddef)* COMMA?)? BRACE_CLOSE;

fielddef : IDENT COLON type;

typedef : TYPE IDENT EQ type;
