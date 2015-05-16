package model.common;


public class Operation {
	
	Object operating;
	Operator operator;
	
	public Operation(Operator operator, Object operating){
		
		this.operating = operating;
		this.operator = operator;
		
	}
	
	public Operation(Operator operator){
		
		this.operator = operator;
		
	}

	public Object getOperating() {
		return operating;
	}

	public void setOperating(Object operating) {
		this.operating = operating;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

}
