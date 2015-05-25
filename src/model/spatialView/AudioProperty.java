package model.spatialView;

public class AudioProperty extends PresentationProperty {

	private static final long serialVersionUID = 7579424707735451518L;
	
	private Double volume;
	private Double balance;
	private Double treble;
	private Double bass;
	
	public AudioProperty(){
		
		super();
		
		volume = 50.0;
		balance = 0.0;
		treble = 0.0;
		bass = 0.0;
		
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
