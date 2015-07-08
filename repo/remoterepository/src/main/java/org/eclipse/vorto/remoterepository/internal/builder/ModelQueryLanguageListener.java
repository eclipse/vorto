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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.eclipse.vorto.remoterepository.internal.builder.generated.QueryBaseListener;
import org.eclipse.vorto.remoterepository.internal.builder.generated.QueryParser.Attr_exprContext;
import org.eclipse.vorto.remoterepository.internal.builder.generated.QueryParser.Combination_exprContext;
import org.eclipse.vorto.remoterepository.internal.builder.generated.QueryParser.Negation_exprContext;

/**
 * This is an implemention of the QueryBaseListener that produces a generic
 * expression object based on the ExpressionFactory passed.
 * 
 *
 * @param <ExprObject>
 */
public class ModelQueryLanguageListener<ExprObject> extends QueryBaseListener {
	private Stack<ExprElement<ExprObject>> scratchPad = new Stack<ExprElement<ExprObject>>();
	private ExpressionFactory<ExprObject> exprFactory;

	public ModelQueryLanguageListener(
			ExpressionFactory<ExprObject> expressionFactory) {
		this.exprFactory = expressionFactory;
	}

	/*
	 * When we see an operator, we push it to the stack as an operator. We need
	 * this as a marker for when the operand for this operator ends.
	 */
	public void enterCombination_expr(Combination_exprContext ctx) {
		scratchPad.push(new ExprElement<ExprObject>(ElementType.OPERATOR));
		super.enterCombination_expr(ctx);
	}

	/*
	 * When we exit a combination operator, get all the operands from the stack
	 * and combine them. Push back the result as an operand.
	 */
	public void exitCombination_expr(Combination_exprContext ctx) {
		String type = ctx.getChild(0).getText();

		Collection<ExprObject> operands = new ArrayList<ExprObject>();
		ExprElement<ExprObject> element = scratchPad.pop();
		while (element.getType() != ElementType.OPERATOR) {
			operands.add(element.getPayload());
			element = scratchPad.pop();
		}

		scratchPad.push(new ExprElement<ExprObject>(ElementType.OPERAND,
				exprFactory.getExpression(type, operands)));
		super.exitCombination_expr(ctx);
	}

	/*
	 * When we get an attribute (name, nameLike, namespace, namespaceLike,
	 * version, versionLike), we just push it to the stack as operands.
	 */
	public void enterAttr_expr(Attr_exprContext ctx) {
		String attributeType = ctx.getChild(0).getText();
		String attributeValue = ctx.getChild(2).getText();

		scratchPad.push(new ExprElement<ExprObject>(ElementType.OPERAND,
				exprFactory.getExpression(attributeType, attributeValue)));

		super.enterAttr_expr(ctx);
	}

	/*
	 * When we exit a negation operator, we pop the last operand and we negate
	 * it and we push it again as an operand.
	 */
	public void exitNegation_expr(Negation_exprContext ctx) {
		ExprElement<ExprObject> element = scratchPad.pop();

		String type = ctx.getChild(0).getText();

		scratchPad.push(new ExprElement<ExprObject>(ElementType.OPERAND,
				exprFactory.getExpression(type, element.getPayload())));
		super.exitNegation_expr(ctx);
	}

	/*
	 * This handles parser error.
	 */
	public void visitErrorNode(ErrorNode node) {
		throw new RuntimeException("Query Parsing Error");
	}

	/*
	 * The last expression in the stack is the value we want.
	 */
	public ExprObject getFinalExpression() {
		return scratchPad.pop().getPayload();
	}

	private class ExprElement<K> {
		private ElementType type;
		private K payload;

		private ExprElement(ElementType type) {
			this.type = type;
		}

		private ExprElement(ElementType type, K payload) {
			this.type = type;
			this.payload = payload;
		}

		public ElementType getType() {
			return type;
		}

		public K getPayload() {
			return payload;
		}
	}

	private enum ElementType {
		OPERATOR, OPERAND
	}
}
