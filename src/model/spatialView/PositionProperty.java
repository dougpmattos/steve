package model.spatialView;

import java.io.Serializable;

import model.temporalView.TemporalChain;

public class PositionProperty implements Serializable{

	private static final long serialVersionUID = 7275587760424836463L;
	
	private String left = "0px";
	private String right = "0px";
	private String top = "0px";
	private String bottom = "0px";
	private String rotation = "0";
	private int orderZ;
	
	public PositionProperty(){

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
