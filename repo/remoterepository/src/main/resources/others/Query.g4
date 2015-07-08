grammar Query;

r:  (attr_expr | combination_expr | negation_expr);

attr_expr : attr_op '("' PARAM_VALUE '")';

attr_op : 'name' | 'nameLike' | 'namespace' | 'namespaceLike' | 'version' | 'versionLike' | 'modelType';

combination_expr : combination_op '(' r ',' r (',' r)* ')';

combination_op : 'and' | 'or';

negation_expr : negation_op '(' r ')';

negation_op : 'not';

PARAM_VALUE : [a-zA-Z0-9.-_]+;

WS : [ \t\r\n]+ -> skip; 