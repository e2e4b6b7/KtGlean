parser grammar AngleParser;

options {
	tokenVocab=AngleLexer;
}

schemas : (schema | evolves)*;

evolves : SCHEMA QVIDENT EVOLVES QVIDENT;

schema : SCHEMA QVIDENT inherit? BRACE_OPEN schemadecl* BRACE_CLOSE;

inherit : COLON QVIDENT (COMMA QVIDENT)* COMMA?;

schemadecl : typedef | predicate | import_ | derivedecl;

derivedecl : DERIVE ident deriving;

typedef : TYPE ident EQ type;

import_ : IMPORT QVIDENT;

predicate : PREDICATE ident COLON key value? deriving?;

key : type;

value : ARROW type;

deriving : derivewhen query;

derivewhen : STORED | DEFAULT |;


type : record | sum | array | maybe | enum_ | reference;

record : BRACE_OPEN (fielddef (COMMA fielddef)* COMMA?)? BRACE_CLOSE;

sum : BRACE_OPEN (fielddef (PIPE fielddef)* PIPE?)? BRACE_CLOSE;

fielddef : ident COLON type;

array : SQBR_OPEN type SQBR_CLOSE;

maybe : MAYBE type;

enum_ : ENUM BRACE_OPEN (ident (PIPE ident)* PIPE?) BRACE_CLOSE;

reference : QIDENT | ident;


ident : PREIDENT | ENUM | MAYBE | TYPE | IMPORT | PREDICATE | SCHEMA;


query : pattern WHERE (statement (SEMICOLON statement)* SEMICOLON?)
      | statement (SEMICOLON statement)* SEMICOLON?;

statement : pattern EQ pattern | pattern;

pattern : gen PLUSPLUS pattern
        | gen PIPE pattern
        | NOT gen
        | IF pattern THEN pattern ELSE pattern
        | gen;

gen : plus
    | plus NEQ plus
    | plus NEQEQ plus
    | plus GT plus
    | plus GE plus
    | plus LT plus
    | plus LE plus
    | kv SQBR_OPEN DOTDOT SQBR_CLOSE
    | plus COLON type;

plus : plus PLUS app | app;

app : kv+;

kv : apat ARROW apat | apat;

apat : NAT
     | STRING
     | STRING DOTDOT
     | DOLLAR NAT
     | DOLLAR PREIDENT NAT
     | SQBR_OPEN (fielddef (COMMA fielddef)*)? SQBR_CLOSE
     | SQBR_OPEN (fielddef (COMMA fielddef)* COMMA) DOTDOT SQBR_CLOSE
     | BRACE_OPEN (pattern COMMA pattern (COMMA pattern)*) BRACE_CLOSE
     | BRACE_OPEN (field (COMMA field)* COMMA?)? BRACE_CLOSE
     | WILDCARD
     | QVIDENT | QIDENT | PREIDENT
     | NEVER
     | PAR_OPEN query PAR_CLOSE;

field : ident EQ pattern;
