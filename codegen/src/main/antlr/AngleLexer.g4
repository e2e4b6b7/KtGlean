lexer grammar AngleLexer;

fragment DECIMAL : [0-9]+;

COMMENT : '#' ~'\n'* -> skip;
WHITESPACE : [ \t\r\n] -> skip;

SCHEMA : 'schema';
EVOLVES : 'evolves';
PREDICATE : 'predicate';
TYPE : 'type';
IMPORT : 'import';
MAYBE : 'maybe';
ENUM : 'enum';

BRACE_OPEN : '{';
BRACE_CLOSE : '}';

SQBR_OPEN : '[';
SQBR_CLOSE : ']';

PIPE : '|';
EQ : '=';
ARROW : '->';
COLON: ':';
COMMA : ',';

PREIDENT : [a-zA-Z_] [a-zA-Z0-9_]*;
QIDENT : PREIDENT ('.' PREIDENT)*;
QVIDENT : QIDENT '.' DECIMAL;

