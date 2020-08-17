package model.spatialView.media;

import java.io.Serializable;


public class CropProperty implements Serializable{

	private static final long serialVersionUID = 5622323641709542335L;
	
	private String left = "0px";
	private String right = "0px";
	private String top = "0px";
	private String bottom = "0px";
	
	public CropProperty(){

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
	
}
