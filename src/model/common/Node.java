package model.common;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.scene.image.ImageView;

public class Node<T> implements Serializable {

	private static final long serialVersionUID = 3268239784445533812L;

	public String name;
    private Double begin;
    private Double end;
    public Double duration = 5.0;
    private Boolean interactive = false;
    private ArrayList<Property> propertyList = new ArrayList<Property>();
    public T type;
    private Fade fade;
    private Boolean isPLayingInPreview = false;
    public transient ImageView icon;
    private transient Object executionObject;

	public Node(){
    	
    }
    
    public void setName(String name){
		this.name = name;
	}
    
	public String getName() {
		   return name;
	}
	
	public void setBegin(Double begin) {
		   this.begin = begin;
	}
	
	public Double getBegin() {
		return begin;
	}

	public void setEnd(Double end) {
		this.end = end;
	}
   
	public Double getEnd() {
		return end;
	}
	   
	public void setDuration(Double duration) {
			this.duration = duration;
	}
		
	public Double getDuration() {
			return duration;
	}
	
	public void setInteractive(Boolean interactive) {
		   this.interactive = interactive;  
	}
	   
	public Boolean isInteractive() {
		return interactive;
	}
	
	public ArrayList<Property> getPropertyList() {
		return propertyList;
	}

	public void setPropertyList(ArrayList<Property> propertyList) {
		this.propertyList = propertyList;
	}

	public T getType() {
		return type;
	}

	public void setType(T type) {
		this.type = type;
	}
	
	public Fade getFade() {
		return fade;
	}

	public void setFade(Fade fade) {
		this.fade = fade;
	}
	
    public void setIsPLayingInPreview(Boolean value){
		this.isPLayingInPreview = value;
	}
	
	public Boolean getIsPLayingInPreview(){
		return this.isPLayingInPreview;
	}
	   
	public Object getExecutionObject(){
		return this.executionObject;
	}
	
	public void setExecutionObject(Object executionObject){
		this.executionObject = executionObject;
	}
}
