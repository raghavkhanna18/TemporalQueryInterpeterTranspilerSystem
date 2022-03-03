parser grammar TSQLParser;

options {
  tokenVocab=TSQLLexer;
}


//parse: (sql_stmt_list)* EOF
//;

//sql_stmt_list:
//    SCOL* sql_stmt (SCOL+ sql_stmt)* SCOL*
//;
//
//sql_stmt: select_stmt
//;
//
//
//type_name:
//    name+? (
//        OPEN_PAR signed_number CLOSE_PAR
//        | OPEN_PAR signed_number COMMA signed_number CLOSE_PAR
//    )?
//;
//
//
//signed_number: (PLUS | MINUS)? NUMERIC_LITERAL
//;
//
//common_table_expression:
//    table_name (OPEN_PAR column_name ( COMMA column_name)* CLOSE_PAR)? AS_ OPEN_PAR select_stmt CLOSE_PAR
//;


///*
// TQLite understands the following binary operators, in order from highest to lowest precedence:
//    ||
//    * / %
//    + -
//    << >> & |
//    < <= > >=
//    = == != <> IS IS NOT IN LIKE GLOB MATCH REGEXP
//    AND
//    OR
// */
//expr:
//    literal_value
//    | BIND_PARAMETER
//    | ((schema_name DOT)? table_name DOT)? column_name
//    | unary_operator expr
//    | expr PIPE2 expr
//    | expr ( STAR | DIV | MOD) expr
//    | expr ( PLUS | MINUS) expr
//    | expr ( LT2 | GT2 | AMP | PIPE) expr
//    | expr ( LT | LT_EQ | GT | GT_EQ) expr
//    | expr (
//        ASSIGN
//        | EQ
//        | NOT_EQ1
//        | NOT_EQ2
//        | IS_
//        | IS_ NOT_
//        | IN_
//        | LIKE_
//    ) expr
//    | expr AND_ expr
//    | expr OR_ expr
//    | OPEN_PAR expr (COMMA expr)* CLOSE_PAR
//    | expr NOT_? LIKE_ expr
//    | expr ( ISNULL_ | NOTNULL_ | NOT_ NULL_)
//    | expr IS_ NOT_? expr
//    | expr NOT_? BETWEEN_ expr AND_ expr
//    | expr NOT_? IN_ (
//        OPEN_PAR (select_stmt | expr ( COMMA expr)*)? CLOSE_PAR
//        | ( schema_name DOT)? table_name
//        | (schema_name DOT)? table_function_name OPEN_PAR (expr (COMMA expr)*)? CLOSE_PAR
//    )
//    | ((NOT_)? EXISTS_)? OPEN_PAR select_stmt CLOSE_PAR
//;


literal_value:
    NUMERIC_LITERAL                         # lit_num
    | STRING_LITERAL                        # lit_string
    | BLOB_LITERAL                          # lit_blob
    | NULL_                                 # lit_null
    | TRUE_                                 # lit_true
    | FALSE_                                # lit_false
    | CURRENT_TIME_                         # lit_current_time
    | CURRENT_DATE_                         # lit_current_date
    | CURRENT_TIMESTAMP_                    # lit_current_timestamp
;
//
//
//select_stmt:
//    select_core (compound_operator select_core)* order_by_stmt? limit_stmt?
//;
//
//join_clause:
//    table_or_subquery (join_operator table_or_subquery join_constraint?)*
//;
//
//select_core:
//    (
//        SELECT_ (DISTINCT_ | ALL_)? result_column (COMMA result_column)* (
//            FROM_ (table_or_subquery (COMMA table_or_subquery)* | join_clause)
//        )? (WHERE_ expr)? (GROUP_ BY_ expr (COMMA expr)* (HAVING_ expr)?)?
//    )
//;
//
//factored_select_stmt:
//    select_stmt
//;
//
//simple_select_stmt:
//    select_core order_by_stmt? limit_stmt?
//;
//
//compound_select_stmt:
//    common_table_stmt? select_core (
//        (UNION_ ALL_? | INTERSECT_ | EXCEPT_) select_core
//    )+ order_by_stmt? limit_stmt?
//;
//
//table_or_subquery: (
//        table_name (AS_? table_alias)?
//    )
//    |   table_function_name OPEN_PAR expr (COMMA expr)* CLOSE_PAR (
//        AS_? table_alias
//    )?
//    | OPEN_PAR (table_or_subquery (COMMA table_or_subquery)* | join_clause) CLOSE_PAR
//    | OPEN_PAR select_stmt CLOSE_PAR (AS_? table_alias)?
//;
//
//result_column:
//    STAR
//    | table_name DOT STAR
//    | expr ( AS_? column_alias)?
//;
//
join_operator:
    COMMA
    | NATURAL_? (LEFT_ OUTER_? | INNER_ | CROSS_ | SINCE_ | UNTIL_)? JOIN_
;
//
//join_constraint:
//    ON_ expr
//    | USING_ OPEN_PAR column_name ( COMMA column_name)* CLOSE_PAR
//;
//
//compound_operator:
//    UNION_ ALL_?
//    | INTERSECT_
//    | EXCEPT_
//;
//
//column_name_list:
//    OPEN_PAR column_name (COMMA column_name)* CLOSE_PAR
//;
//
//
//qualified_table_name: (schema_name DOT)? table_name (AS_ alias)?
//;
//
//common_table_stmt: //additional structures
//    common_table_expression (COMMA common_table_expression)*
//;
//
//order_by_stmt:
//    ORDER_ BY_ ordering_term (COMMA ordering_term)*
//;
//
//limit_stmt:
//    LIMIT_ expr (COMMA expr)?
//;
//
//ordering_term:
//    expr (COLLATE_ collation_name)? asc_desc? (NULLS_ (FIRST_ | LAST_))?
//;
//
//asc_desc:
//    ASC_
//    | DESC_
//;
//
//
//// unknown
//
//
//offset:
//    COMMA signed_number
//;

//default_value:
//    COMMA signed_number
//;


//order_by_expr:
//    ORDER_ BY_ expr+
//;

//order_by_expr_asc_desc:
//    ORDER_ BY_ expr_asc_desc
//;
//
//expr_asc_desc:
//    expr asc_desc? (COMMA expr asc_desc?)*
//;

//TODO BOTH OF THESE HAVE TO BE REWORKED TO FOLLOW THE SPEC
//initial_select:
//    select_stmt
//;
//
//recursive_select:
//    select_stmt
//;

//unary_operator:
//    MINUS
//    | PLUS
//    | TILDE
//    | NOT_
//;

//error_message:
//    STRING_LITERAL
//;


column_alias:
    IDENTIFIER
    | STRING_LITERAL
;

keyword:
    ADD_
    | ALL_
    | AND_
    | AS_
    | ASC_
    | BETWEEN_
    | BY_
    | COLLATE_
    | COLUMN_
    | CROSS_
    | CURRENT_DATE_
    | CURRENT_TIME_
    | CURRENT_TIMESTAMP_
    | DESC_
    | DISTINCT_
    | EXCEPT_
    | EXISTS_
    | FROM_
    | GROUP_
    | HAVING_
    | IN_
    | INNER_
    | INTERSECT_
    | INTO_
    | IS_
    | ISNULL_
    | JOIN_
    | LEFT_
    | LIKE_
    | LIMIT_
    | NATURAL_
    | NOT_
    | NOTNULL_
    | NULL_
    | ON_
    | OR_
    | ORDER_
    | OUTER_
    | RIGHT_
    | SELECT_
    | THEN_
    | TO_
    | UNION_
    | UNIQUE_
    | USING_
    | WHEN_
    | WHERE_
    | WITH_
    | CURRENT_
    | FOLLOWING_
    | TRUE_
    | FALSE_
    | NULLS_
    | FIRST_
    | LAST_
;

// TODO: check all names below

name:
    any_name
;

//function_name:
//    any_name
//;
//
//schema_name:
//    any_name
//;

table_name:
    IDENTIFIER
;

//table_or_index_name:
//    any_name
//;
//
//new_table_name:
//    any_name
//;

column_name:
    any_name
;

//collation_name:
//    any_name
//;

//foreign_table:
//    any_name
//;
//
//index_name:
//    any_name
//;

//trigger_name:
//    any_name
//;
//
//view_name:
//    any_name
//;

//module_name:
//    any_name
//;

//pragma_name:
//    any_name
//;

//savepoint_name:
//    any_name
//;
//
//table_alias:
//    any_name
//;

//transaction_name:
//    any_name
//;
//
//window_name:
//    any_name
//;

//alias:
//    any_name
//;
//
//filename:
//    any_name
//;

//base_window_name:
//    any_name
//;

//simple_func:
//    any_name
//;
//
//aggregate_func:
//    any_name
//;
//
//table_function_name:
//    any_name
//;
//
any_name:
    IDENTIFIER
    | keyword
    | STRING_LITERAL
    | OPEN_PAR any_name CLOSE_PAR
;


unary_operatoion:
    select_operator |  where_operation | at_operation | as_operation |  modal_operator;

select_operator:
 SELECT_ (DISTINCT_ | ALL_)? attribute_list;

attribute:
    IDENTIFIER                                      # attri_identifier
    | STAR                                          # attri_star
    | IDENTIFIER as_operation                       # attrie_as
    | table_name DOT IDENTIFIER                     # attri_table_dot_ident
    | table_name DOT STAR                           # attri_table_dot_star
    | table_name DOT IDENTIFIER as_operation        # attri_table_dot_ident_as
    ;

as_operation:
    AS_ STRING_LITERAL;

attribute_list:
     attribute (COMMA attribute)*;

comparator :
    LT                                                  # lt
    | LT_EQ                                             # lt_eq
    | GT                                                # gt
    | GT_EQ                                             # gt_eq
    | EQ                                                # eq
    | NOT_EQ1                                           # not_eq_1
    | NOT_EQ2                                           # not_eq_2
    | ASSIGN                                            # eq_2
    ;
where_expresion:
      attribute  comparator attribute                   # where_aca
    | attribute comparator literal_value                # where_acl
    | literal_value comparator literal_value            # where_lcl
    | literal_value comparator attribute                # where_lca
    ;

where_operation:
      WHERE_ where_expresion ( AND_ where_expresion)*   # where_op_and
    | WHERE_ where_expresion ( OR_ where_expresion)*    # where_op_or
    ;

at_operation:
    AT_ literal_value;

join_operation:
    join_operator ON_ attribute ASSIGN attribute;

binary_operator:
      TIMES_                                             # binop_times
    | SINCE_                                             # binop_since
    | UNTIL_                                             # binop_until
    | join_operation                                     # binop_join
    ;


binary_statement_operator:
      UNION_                                             # binop_stat_union
    | MINUS                                              # binop_stat_minus
    ;

table:
    table_name as_operation | table_name ;

//binary_operation:
//    OPEN_PAR statement CLOSE_PAR binary_operator (OPEN_PAR statement CLOSE_PAR | table) | table binary_operator (table | OPEN_PAR statement CLOSE_PAR) ;
binary_operation:
        table binary_operator table                                  # binopn_table_with_table
      | table binary_operator binary_operation                       # binopn_table_with_binopn
    ;


modal_operation:
    modal_operator;

modal_operator:
      PAST_                                             # modal_past
    | PREVIOUS_                                         # modal_previous
    | ALWAYS_ PAST_                                     # modal_always_past
    | FUTURE_                                           # modal_future
    | NEXT_                                             # modal_next
    | ALWAYS_ FUTURE_                                   # modal_always_future
;
coalesce_statement:
    COALESCE? statement;

statement:
    select_operator FROM_ binary_operation (where_operation)? (modal_operation)? (at_operation)? SCOL           # select_from_bin_opn
    | select_operator FROM_ table (where_operation)? (modal_operation)? (at_operation)? SCOL                    # select_from_table
;

program:
    coalesce_statement (coalesce_statement )? EOF;
