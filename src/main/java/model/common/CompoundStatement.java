package model.common;

import java.util.ArrayList;

import model.common.enums.Operator;

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
