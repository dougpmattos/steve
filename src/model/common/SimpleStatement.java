package model.common;

import model.common.enums.AssessmentComparator;

public class SimpleStatement extends ConditionalStatement {

	private AssessmentComparator comparator;
	private KeyValue attributeAssessment;
	private String valueAssessment;
	
	public SimpleStatement(){
		
	}

	public AssessmentComparator getComparator() {
		return comparator;
	}

	public void setComparator(AssessmentComparator comparator) {
		this.comparator = comparator;
	}

	public KeyValue getAttributeAssessment() {
		return attributeAssessment;
	}

	public void setAttributeAssessment(KeyValue attributeAssessment) {
		this.attributeAssessment = attributeAssessment;
	}

	public String getValueAssessment() {
		return valueAssessment;
	}

	public void setValueAssessment(String valueAssessment) {
		this.valueAssessment = valueAssessment;
	}
	
}
