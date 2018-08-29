package model.spatialView;

import java.awt.geom.Point2D;
import java.io.Serializable;

import model.spatialView.enums.AspectRatio;

public class SizeProperty implements Serializable{

	private static final long serialVersionUID = 2285635562719779375L;
	
	private String width = "100%";
	private String height = "100%";
	private AspectRatio aspectRatio = AspectRatio.HIDDEN;
	private Point2D realSize;

	public Point2D getRealSize() {
		return realSize;
	}

	public void setRealSize(Point2D realSize) {
		this.realSize = realSize;
	}

	public SizeProperty(){

	}
	
	public void setWidth(String width){
		this.width = width;
	}
	
	public String getWidth(){
		return width;
	}
	
	public void setHeight(String height){
		this.height = height;
	}
	
	public String getHeight(){
		return height;
	}
	
	public void setAspectRatio(AspectRatio aspectRatio){
		this.aspectRatio = aspectRatio;
	}
	
	public AspectRatio getAspectRatio(){
		return aspectRatio;
	}
	
}
