lexer grammar AngleLexer;

COMMENT : '#' ~'\n'* -> skip;
WHITESPACE : [ \t\r\n] -> skip;

SCHEMA : 'schema';
EVOLVES : 'evolves';
DERIVE : 'derive';
STORED : 'stored';
DEFAULT : 'default';
WHERE : 'where';
PREDICATE : 'predicate';
TYPE : 'type';
IMPORT : 'import';
MAYBE : 'maybe';
ENUM : 'enum';
IF : 'if';
THEN : 'then';
ELSE : 'else';
NEVER : 'never';

BRACE_OPEN : '{';
BRACE_CLOSE : '}';

PAR_OPEN : '(';
PAR_CLOSE : ')';

SQBR_OPEN : '[';
SQBR_CLOSE : ']';

PIPE : '|';
EQ : '=';
NEQ : '!=';
NEQEQ : '!==';
ARROW : '->';
COLON: ':';
SEMICOLON : ';';
COMMA : ',';
PLUS : '+';
PLUSPLUS : '++';
NOT : '!';
GT : '>';
GE : '>=';
LT : '<';
LE : '<=';
DOTDOT : '..';
DOLLAR : '$';
WILDCARD : '_';

NAT : [0-9]+;
STRING : '"' (~'"'* | '\\"') '"';
PREIDENT : [a-zA-Z_] [a-zA-Z0-9_]*;
QIDENT : PREIDENT ('.' PREIDENT)*;
QVIDENT : QIDENT '.' NAT;

