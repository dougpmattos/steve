package gui.spatialViewPane;

import gui.common.Language;
import gui.common.SliderButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import model.common.Media;
import controller.Controller;

public class LevelPane extends GridPane {

	private Controller controller;
	private Media media;
	
	private SliderButton volume;
	private SliderButton balance;
	private SliderButton treble;
	private SliderButton bass;
	
	public LevelPane(Controller controller, Media media){
		
		this.controller = controller;
		this.media = media;
		
		Text title = new Text(Language.translate("levels"));
		title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		Label volumeLabel = new Label(Language.translate("volume"));
		Label balanceLabel = new Label(Language.translate("balance"));
		Label trebleLabel = new Label(Language.translate("treble"));
		Label bassLabel = new Label(Language.translate("bass"));
		
		volume = new SliderButton();
		balance = new SliderButton();
		treble = new SliderButton();
		bass = new SliderButton();

		add(title, 0, 0);
		add(volumeLabel, 0, 1);
		add(balanceLabel, 1, 1);
		add(trebleLabel, 0, 3);
		add(bassLabel, 1, 3);
		add(volume, 0, 2);
		add(balance, 1, 2);
		add(treble, 0, 4);
		add(bass, 1, 4);
		
		setAlignment(Pos.CENTER);
		setHgap(5);
		setVgap(5);
		setStyle("-fx-background-color: #263238");
		setPadding(new Insets(10, 10, 10, 10));
		
	}
	
	public void setVolume(Double value){
		volume.setSliderValue(value);
	}
	
	public Double getVolume(){
		return volume.getSliderValue();
	}
	
	public void setBalance(Double value){
		balance.setSliderValue(value);
	}
	
	public Double getBalance(){
		return balance.getSliderValue();
	}
	
	public void setTreble(Double value){
		treble.setSliderValue(value);
	}
	
	public Double getTreble(){
		return treble.getSliderValue();
	}
	
	public void setBass(Double value){
		bass.setSliderValue(value);
	}
	
	public Double getBass(){
		return bass.getSliderValue();
	}
	
}
