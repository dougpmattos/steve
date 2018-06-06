package model.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

public abstract class Relation extends Observable implements Serializable {

	private static final long serialVersionUID = 4519112300170261880L;

	public static int relationNumber = 0;
	
	private int id;
	private ConditionalStatement conditionalStatement;
	private ArrayList<KeyValue> setActionList = new ArrayList<KeyValue>();
	
	public Relation(){
		
		this.id = relationNumber; 
		relationNumber++;
		
	}
	
	public int getId() {
		return id;
	}
	
	public ConditionalStatement getConditionalStatement() {
		return conditionalStatement;
	}

	public void setConditionalStatement(ConditionalStatement conditionalStatement) {
		this.conditionalStatement = conditionalStatement;
	}

	public ArrayList<KeyValue> getSetActionList() {
		return setActionList;
	}

	public void setSetActionList(ArrayList<KeyValue> setActionList) {
		this.setActionList = setActionList;
	}
	
}
