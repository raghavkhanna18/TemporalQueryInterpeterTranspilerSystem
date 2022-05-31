parser grammar TSQLParser;

options {
  tokenVocab=TSQLLexer;
}

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

join_operator:
LEFT_ OUTER_? JOIN_                     # left_join
| RIGHT_ OUTER_? JOIN_                  # right_join
| INNER_ JOIN_                          # inner_join
| CROSS_ JOIN_                          # cross_join
| SINCE_ JOIN_                          # since_join
| UNTIL_ JOIN_                          # until_join
;

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

table_name:
    IDENTIFIER
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
      where_expresion AND_ where_operation            # where_op_and
    | where_expresion OR_  where_operation            # where_op_or
    | where_expresion                                 # where_single
    ;

at_operation:
    AT_ literal_value;

join_operation:
    table join_operator table ON_ attribute ASSIGN attribute;

binary_operator:
      TIMES_                                             # binop_times
    | SINCE_                                             # binop_since
    | UNTIL_                                             # binop_until
    ;


binary_statement_operator:
      UNION_                                             # binop_stat_union
    | MINUS                                              # binop_stat_minus
    ;

table:
    table_name as_operation                             # table_as
    | table_name                                        # table_not_renamed
    ;

//binary_operation:
//    OPEN_PAR statement CLOSE_PAR binary_operator (OPEN_PAR statement CLOSE_PAR | table) | table binary_operator (table | OPEN_PAR statement CLOSE_PAR) ;
binary_operation:
        table binary_operator table                                     # binopn_table_with_table
        | table binary_operator binary_operation                        # binopn_table_with_binopn
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
    select_operator FROM_ table (WHERE_ where_operation)? (modal_operation)? (at_operation)? SCOL                          # select_from_table
    | select_operator FROM_ binary_operation (WHERE_ where_operation)? (modal_operation)? (at_operation)? SCOL             # select_from_bin_opn
    | select_operator FROM_ join_operation ( WHERE_ where_operation)? (modal_operation)? (at_operation)? SCOL               # select_from_join

;

program:
    coalesce_statement+ EOF;
