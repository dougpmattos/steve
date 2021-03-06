package model.spatialView.media;

import java.io.Serializable;

import model.temporalView.TemporalChain;

public class MediaPositionProperty implements Serializable{

	private static final long serialVersionUID = 7275587760424836463L;
	
	private String left = "0%";
	private String right = "0%";
	private String top = "0%";
	private String bottom = "0%";
	private String rotation = "0";
	private int orderZ;
	
	public MediaPositionProperty(){

		orderZ = TemporalChain.getTemporalViewMediaNumber();

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

}
