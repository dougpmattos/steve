package model.common;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;

import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import model.common.enums.MediaType;
import model.temporalView.TemporalChain;
import model.utility.MediaUtil;

public class Node<T> implements Serializable {

	private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	private static final long serialVersionUID = 3268239784445533812L;

	public String name;
	private ArrayList<Property> propertyList = new ArrayList<Property>();

    private Double begin;
    private Double end;
    public Double duration = 10.0;
    private Boolean interactive = false;
    public T type;

    private Boolean isShownInPreview = false;
	private Boolean isContinuousMediaPlaying = false;
    public transient ImageView icon;
    private transient Object executionObject;
	public transient HBox containerNode;
	private TemporalChain parentTemporalChain;

	public Node(){
    	
    }

    public void setName(String name){
		this.name = name;
	}
    
	public String getName() {
		   return name;
	}
	
	public void setBegin(Double begin) {
		   this.begin = MediaUtil.approximateDouble(begin);
	}
	
	public Double getBegin() {
		return begin;
	}

	public void setEnd(Double end) {
		Double oldValue = this.end;
		this.end = MediaUtil.approximateDouble(end);
		propertyChangeSupport.firePropertyChange("end", oldValue, end);
	}
   
	public Double getEnd() {
		return end;
	}

	public String getNCLName(){
		return name.replaceAll("\\s+", "");
	}

	public void setDuration(Double duration) {
			this.duration = MediaUtil.approximateDouble(duration);
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
	
    public void setIsShownInPreview(Boolean value){
		this.isShownInPreview = value;
	}
	
	public Boolean getIsShownInPreview(){
		return this.isShownInPreview;
	}
	   
	public Object getExecutionObject(){
		return this.executionObject;
	}
	
	public void setExecutionObject(Object executionObject){
		this.executionObject = executionObject;
	}

	public TemporalChain getParentTemporalChain() {
		return parentTemporalChain;
	}

	public void setParentTemporalChain(TemporalChain parentTemporalChain) {
		this.parentTemporalChain = parentTemporalChain;
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	public void setContainerNode(HBox containerNode) {
		this.containerNode = containerNode;
	}

	public HBox getContainerNode() {
		return containerNode;
	}

	public boolean isContinousMedia(){

		if(type == MediaType.VIDEO || type == MediaType.AUDIO){
			return true;
		}else{
			return false;
		}

	}

	public Boolean getIsContinuousMediaPlaying() {
		return isContinuousMediaPlaying;
	}

	public void setIsContinuousMediaPlaying(Boolean isContinuousMediaPlaying) {
		this.isContinuousMediaPlaying = isContinuousMediaPlaying;
	}

}
