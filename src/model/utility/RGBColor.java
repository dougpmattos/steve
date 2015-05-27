package model.utility;

import java.io.Serializable;

public class RGBColor implements Serializable{

	private static final long serialVersionUID = 4870206102750406680L;
	
	private Double red;
	private Double green;
	private Double blue;
	
	public RGBColor(Double red, Double green, Double blue){
		
		this.red = red;
		this.green = green;
		this.blue = blue;
		
	}
	
	public Double getRed() {
		return red;
	}

	public void setRed(Double red) {
		this.red = red;
	}

	public Double getGreen() {
		return green;
	}

	public void setGreen(Double green) {
		this.green = green;
	}

	public Double getBlue() {
		return blue;
	}

	public void setBlue(Double blue) {
		this.blue = blue;
	}
	
}
