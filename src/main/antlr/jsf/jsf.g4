grammar jsf;

@header{
package jsf;
}

program
        :       classDecl* expression EOF
        ;

classDecl
        :   CLASS classlbl=ID (EXTEND extendlbl=ID)? LBRAC
                fieldDecl* constructorDecl methodDecl*
            RBRAC
        ;

fieldDecl
        :       type ID SEMI
        ;

constructorDecl
        :       constructorname=ID LPAR (type ID (COMMA type ID)*)? RPAR LBRAC
                    superDecl
                    fieldAssignment*
                RBRAC
        ;

superDecl
        :       SUPER LPAR (ID (COMMA ID)*)? RPAR SEMI
        ;

fieldAssignment
        :       THIS DOT field=ID EQ parameter=ID SEMI
        ;

methodDecl
        :       returntype=type name=ID LPAR (paramtype=type paramname=ID)? RPAR LBRAC
                    RETURN expression SEMI
                RBRAC
        ;

expression
        :       e1=primExpression
                    (op=(PLUS | MINUS | DIV | MULT) e2=primExpression)?
        ;

primExpression
        :       NUMBER                                                                  # num
        |       ID                                                                      # var
        |       primExpression DOT ID                                                   # field
        |       primExpression DOT ID LPAR expression RPAR                              # method
        |       NEW ID LPAR (expression (COMMA expression)*)? RPAR                      # object
        ;

type
        :       basicType                                           # basic
        |       ID                                                  # class
        |       NOT ID                                              # notClass
        |       LPAR type1=type bool=(AND | OR) type2=type RPAR     # boolean
        ;

basicType
        :       BYTE | INT | LONG | FLOAT | DOUBLE | CHAR | BOOL
        ;

BYTE    :       'byte'                      ;
INT     :       'int'                       ;
LONG    :       'long'                      ;
FLOAT   :       'float'                     ;
DOUBLE  :       'double'                    ;
CHAR    :       'char'                      ;
BOOL    :       'bool'                      ;

AND     :       'and'                       ;
OR      :       'or'                        ;
NOT     :       'not'                       ;

CLASS   :       'class'                     ;
SUPER   :       'super'                     ;
EXTEND  :       'extends'                   ;
THIS    :       'this'                      ;
RETURN  :       'return'                    ;
NEW     :       'new'                       ;

LPAR    :       '('                         ;
RPAR    :       ')'                         ;
LBRAC   :       '{'                         ;
RBRAC   :       '}'                         ;
EQ      :       '='                         ;
PLUS    :       '+'                         ;
MINUS   :       '-'                         ;
DIV     :       '/'                         ;
MULT    :       '*'                         ;
COMMA   :       ','                         ;
DOT     :       '.'                         ;
SEMI    :       ';'                         ;

ID      :       LETTER (LETTER | DIGIT)*    ;
NUMBER  :       DIGIT (DIGIT)*              ;
SPACE   :       (' ' | '\t')+   -> skip     ;
EOL     :       '\r'? '\n'      -> skip     ;
EMPTY   :       'EMPTY'                     ;

fragment LETTER :   'a'..'z' | 'A'..'Z'     ;
fragment DIGIT  :   '0'..'9'                ;