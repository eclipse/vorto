/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/
package org.eclipse.vorto.remoterepository.internal.builder;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.eclipse.vorto.remoterepository.builder.IModelQueryBuilder;
import org.eclipse.vorto.remoterepository.internal.builder.generated.QueryLexer;
import org.eclipse.vorto.remoterepository.internal.builder.generated.QueryParser;
import org.eclipse.vorto.remoterepository.service.search.IModelQuery;
import org.springframework.stereotype.Service;

@Service
public class DefaultModelQueryBuilder implements IModelQueryBuilder {

	public IModelQuery buildFromExpression(final IModelQuery query, String expression) {
		if (expression == null || expression.trim().equals("")) {
			return query;
		}
		
		QueryLexer lexer = new QueryLexer(new ANTLRInputStream(expression));
		QueryParser parser = new QueryParser(new CommonTokenStream(lexer));
		ParseTree tree = parser.r();
		
		ExpressionFactory<IModelQuery> exprFactory = new ModelQueryExpressionFactory(query);

		ModelQueryLanguageListener<IModelQuery> parseListener = new ModelQueryLanguageListener<IModelQuery>(exprFactory);
		
		ParseTreeWalker.DEFAULT.walk(parseListener, tree);

		return parseListener.getFinalExpression();
	}
}
