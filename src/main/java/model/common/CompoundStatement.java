package model.common;

import model.common.enums.Operator;

import java.util.ArrayList;

public class CompoundStatement extends ConditionalStatement {
	
	private ArrayList<ConditionalStatement> conditionalStatementList = new ArrayList<ConditionalStatement>();
	private Operator operator;
	private Boolean isNegated;
	
	public CompoundStatement(){
		
	}

	public ArrayList<ConditionalStatement> getConditionalStatementList() {
		return conditionalStatementList;
	}

	public void setConditionalStatementList(
			ArrayList<ConditionalStatement> conditionalStatementList) {
		this.conditionalStatementList = conditionalStatementList;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public Boolean getIsNegated() {
		return isNegated;
	}

	public void setIsNegated(Boolean isNegated) {
		this.isNegated = isNegated;
	}
	
}
