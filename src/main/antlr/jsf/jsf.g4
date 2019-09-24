grammar jsf;

@header{
package jsf;
}

program
        :       CLASS classlbl=ID (EXTEND extendlbl=ID)? LBRAC
                    fielddecl+ constructordecl
                RBRAC EOF
        ;

fielddecl
        :       objecttype ID SEMI
        ;

constructordecl
        :       ID LPAR objecttype ID (COMMA objecttype ID)* RPAR LBRAC
                    SUPER LPAR (ID (COMMA ID)*)? RPAR SEMI
                    (THIS DOT ID EQ ID SEMI)*
                RBRAC
        ;


objecttype
        :       EMPTY | basictype | ID
        ;

basictype
        :       BYTE | INT | LONG | FLOAT | DOUBLE | CHAR
        ;

BYTE    :       'byte'                      ;
INT     :       'int'                       ;
LONG    :       'long'                      ;
FLOAT   :       'float'                     ;
DOUBLE  :       'double'                    ;
CHAR    :       'char'                      ;

AND     :       'and'                       ;
OR      :       'or'                        ;
EQ      :       '='                         ;
CLASS   :       'class'                     ;
SUPER   :       'super'                     ;
NOT     :       'not'                       ;
EXTEND  :       'extends'                   ;
THIS    :       'this'                      ;
DOT     :       '.'                         ;

SEMI    :       ';'                         ;
LPAR    :       '('                         ;
RPAR    :       ')'                         ;
LBRAC   :       '{'                         ;
RBRAC   :       '}'                         ;


COMMA   :       ','                         ;

ID      :       LETTER (LETTER | DIGIT)*    ;
SPACE   :       (' ' | '\t')+   -> skip     ;
EOL     :       '\r'? '\n'      -> skip     ;
EMPTY   :       'EMPTY'                     ;

fragment LETTER :   'a'..'z' | 'A'..'Z'     ;
fragment DIGIT  :   '0'..'9'                ;