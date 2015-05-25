package model.spatialView;

import java.io.Serializable;

import model.temporalView.TemporalChain;

public abstract class PresentationProperty implements Serializable{

	private static final long serialVersionUID = 7275587760424836463L;
	
	private String left;
	private String right;
	private String top;
	private String bottom;
	private String rotation;
	private int orderZ;
	private Double transparency;
	
	public PresentationProperty(){
		
		left = "0";
		right = "0";
		top = "0";
		bottom = "0";
		rotation = "0";
		orderZ = TemporalChain.getTemporalViewMediaNumber();
		transparency = 0.0;
		
	}
	
	public void setLeft(String left){
		this.left = left;
	}
	
	public String getLeft(){
		return left;
	}
	
	public void setRight(String right){
		this.right = right;
	}
	
	public String getRight(){
		return right;
	}
	
	public void setTop(String top){
		this.top = top;
	}
	
	public String getTop(){
		return top;
	}
	
	public void setBottom(String bottom){
		this.bottom = bottom;
	}
	
	public String getBottom(){
		return bottom;
	}
	
	public void setRotation(String rotation){
		this.rotation = rotation;
	}
	
	public String getRotation(){
		return rotation;
	}
	
	public void setOrderZ(int orderZ){
		this.orderZ = orderZ;
	}
	
	public int getOrderZ(){
		return orderZ;
	}
	
	public void setTransparency(Double transparency){
		this.transparency = transparency;
	}
	
	public Double getTransparency(){
		return transparency;
	}

}
