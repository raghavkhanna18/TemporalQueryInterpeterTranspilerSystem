lexer grammar TSQLLexer;
options { caseInsensitive = true; }

SCOL:      ';';
DOT:       '.';
OPEN_PAR:  '(';
CLOSE_PAR: ')';
COMMA:     ',';
ASSIGN:    '=';
STAR:      '*';
PLUS:      '+';
MINUS:     '-';
TILDE:     '~';
PIPE2:     '||';
DIV:       '/';
MOD:       '%';
LT2:       '<<';
GT2:       '>>';
AMP:       '&';
PIPE:      '|';
LT:        '<';
LT_EQ:     '<=';
GT:        '>';
GT_EQ:     '>=';
EQ:        '==';
NOT_EQ1:   '!=';
NOT_EQ2:   '<>';

ADD_:               'ADD';
ALL_:               'ALL';
AND_:               'AND';
AS_:                'AS';
ASC_:               'ASC';
BETWEEN_:           'BETWEEN';
BY_:                'BY';
COLLATE_:           'COLLATE';
COLUMN_:            'COLUMN';
CROSS_:             'CROSS';
CURRENT_DATE_:      'CURRENT_DATE';
CURRENT_TIME_:      'CURRENT_TIME';
CURRENT_TIMESTAMP_: 'CURRENT_TIMESTAMP';
DESC_:              'DESC';
DISTINCT_:          'DISTINCT';
EXISTS_:            'EXISTS';
EXCEPT_:            'EXCEPT';
FROM_:              'FROM';
GROUP_:             'GROUP';
HAVING_:            'HAVING';
IN_:                'IN';
INNER_:             'INNER';
INTERSECT_:         'INTERSECT';
INTO_:              'INTO';
IS_:                'IS';
ISNULL_:            'ISNULL';
JOIN_:              'JOIN';
LEFT_:              'LEFT';
LIKE_:              'LIKE';
LIMIT_:             'LIMIT';
NATURAL_:           'NATURAL';
NOT_:               'NOT';
NOTNULL_:           'NOTNULL';
NULL_:              'NULL';
ON_:                'ON';
OR_:                'OR';
ORDER_:             'ORDER';
OUTER_:             'OUTER';
RIGHT_:             'RIGHT';
SELECT_:            'SELECT';
THEN_:              'THEN';
TO_:                'TO';
UNION_:             'UNION';
UNIQUE_:            'UNIQUE';
USING_:             'USING';
WHEN_:              'WHEN';
WHERE_:             'WHERE';
WITH_:              'WITH';
CURRENT_:           'CURRENT';
FOLLOWING_:         'FOLLOWING';
TRUE_:              'TRUE';
FALSE_:             'FALSE';
NULLS_:             'NULLS';
FIRST_:             'FIRST';
LAST_:              'LAST';
SINCE_:             'SINCE';
UNTIL_:             'UNTIL';
COALESCE:           'COALESCE';
PAST_: 'PAST';
PREVIOUS_: 'PREVIOUS';
ALWAYS_: 'ALWAYS';
FUTURE_: 'FUTURE';
NEXT_: 'NEXT';
TIMES_: 'TIMES';
AT_: 'AT';

IDENTIFIER:
    '"' (~'"' | '""')* '"'
    | '`' (~'`' | '``')* '`'
    | '[' ~']'* ']'
    | [A-Z_] [A-Z_0-9]*
;

NUMERIC_LITERAL: ((DIGIT+ ('.' DIGIT*)?) | ('.' DIGIT+)) ('E' [-+]? DIGIT+)? | '0x' HEX_DIGIT+;

BIND_PARAMETER: '?' DIGIT* | [:@$] IDENTIFIER;

STRING_LITERAL: '\'' ( ~'\'' | '\'\'')* '\'';

BLOB_LITERAL: 'X' STRING_LITERAL;

SINGLE_LINE_COMMENT: '--' ~[\r\n]* (('\r'? '\n') | EOF) -> channel(HIDDEN);

MULTILINE_COMMENT: '/*' .*? '*/' -> channel(HIDDEN);

SPACES: [ \u000B\t\r\n] -> skip;

UNEXPECTED_CHAR: .;



fragment HEX_DIGIT: [0-9A-F];
fragment DIGIT:     [0-9];
