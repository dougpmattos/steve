package model.common.enums;

public enum Operator {

	AND("And"),
	OR("Or");
	
	private String name;
	
	private Operator(String name) {
        this.name = name;
    }
	
	@Override
	public String toString() {
		return name;
	}
	
}
