package model.utility;

public class Operation<E> {
	
	Object operating;
	E operator;
	Object arg;
	
	public Operation(E operator, Object operating){
		
		this.operating = operating;
		this.operator = operator;
		
	}
	
	public Operation(E operator, Object operating, Object arg){
		
		this.operating = operating;
		this.operator = operator;
		this.arg = arg;
		
	}
	
	public Operation(E operator){
		this.operator = operator;
	}

	public Object getOperating() {
		return operating;
	}

	public void setOperating(Object operating) {
		this.operating = operating;
	}

	public E getOperator() {
		return operator;
	}

	public void setOperator(E operator) {
		this.operator = operator;
	}
	
	public Object getArg() {
		return arg;
	}

	public void setArg(Object arg) {
		this.arg = arg;
	}

}
