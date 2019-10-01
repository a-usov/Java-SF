grammar jsf;

@header{
package jsf;
}

program
        :       classdecl* expression EOF
        ;

classdecl
        :   CLASS classlbl=ID (EXTEND extendlbl=ID)? LBRAC
                fielddecl* constructordecl methoddecl*
            RBRAC
        ;

fielddecl
        :       objecttype ID SEMI
        ;

constructordecl
        :       constructorname=ID LPAR (objecttype ID (COMMA objecttype ID)*)? RPAR LBRAC
                    superdecl
                    fieldassignment*
                RBRAC
        ;

superdecl
        :       SUPER LPAR (ID (COMMA ID)*)? RPAR SEMI
        ;

fieldassignment
        :       THIS DOT field=ID EQ parameter=ID SEMI
        ;

methoddecl
        :       objecttype ID LPAR (objecttype ID)? RPAR LBRAC
                    RETURN expression SEMI
                RBRAC
        ;

expression
        :       NUMBER | ID | expression DOT ID | expression DOT ID LPAR expression RPAR | NEW objecttype LPAR (expression (COMMA expression)*)? RPAR
                | expression PLUS expression
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
RETURN  :       'return'                    ;
NEW     :       'new'                       ;

SEMI    :       ';'                         ;
LPAR    :       '('                         ;
RPAR    :       ')'                         ;
LBRAC   :       '{'                         ;
RBRAC   :       '}'                         ;
PLUS    :       '+'                         ;


COMMA   :       ','                         ;

ID      :       LETTER (LETTER | DIGIT)*    ;
NUMBER  :       DIGIT (DIGIT)*              ;
SPACE   :       (' ' | '\t')+   -> skip     ;
EOL     :       '\r'? '\n'      -> skip     ;
EMPTY   :       'EMPTY'                     ;

fragment LETTER :   'a'..'z' | 'A'..'Z'     ;
fragment DIGIT  :   '0'..'9'                ;