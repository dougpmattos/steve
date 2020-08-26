package model.common;

import java.io.Serializable;

public class InteractivityKeyMapping implements Serializable{
	
	char redKey;
	char greenKey;
	char blueKey;
	char yellowKey;
	
	public InteractivityKeyMapping() {
		
	}
	
	public void setInteractivityKeyMapping(String rk, String gk, String bk, String yk){
		this.redKey = rk.charAt(0);
		this.greenKey = gk.charAt(0);
		this.blueKey = bk.charAt(0);
		this.yellowKey = yk.charAt(0);				
	}
	
	public void setInteractivityKeyMapping(String color, char key){
		if(color.toLowerCase().equals("red"))
			this.redKey = key;
		if(color.toLowerCase().equals("green"))
			this.greenKey = key;
		if(color.toLowerCase().equals("blue"))
			this.blueKey = key;
		if(color.toLowerCase().equals("yellow"))
			this.yellowKey = key;		
		
	}
	
	public String getInteractivityKeyMapping(String color){
		if(color.toLowerCase().equals("red"))
			return new StringBuilder().append("").append(this.redKey).toString();
		if(color.toLowerCase().equals("green"))
			return new StringBuilder().append("").append(this.greenKey).toString();
		if(color.toLowerCase().equals("blue"))
			return new StringBuilder().append("").append(this.blueKey).toString();
		if(color.toLowerCase().equals("yellow"))
			return new StringBuilder().append("").append(this.yellowKey).toString();
		else 
			return "0";
		
	}

}
