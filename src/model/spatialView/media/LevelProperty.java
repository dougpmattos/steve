package model.spatialView.media;

import java.io.Serializable;

public class LevelProperty implements Serializable {

	private static final long serialVersionUID = 7579424707735451518L;
	
	private Double volume = 50.0;
	private Double balance = 0.0;
	private Double treble = 0.0;
	private Double bass = 0.0;
	
	public LevelProperty(){

	}
	
	public void setVolume(Double volume){
		this.volume = volume;
	}
	
	public Double getVolume(){
		return volume;
	}
	
	public void setBalance(Double balance){
		this.balance = balance;
	}
	
	public Double getBalance(){
		return balance;
	}
	
	public void setTreble(Double treble){
		this.treble = treble;
	}
	
	public Double getTreble(){
		return treble;
	}
	
	public void setBass(Double bass){
		this.bass = bass;
	}
	
	public Double getBass(){
		return bass;
	}
	
}
