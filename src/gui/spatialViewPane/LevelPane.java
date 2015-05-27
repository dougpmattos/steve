package gui.spatialViewPane;

import gui.common.Language;
import gui.common.SliderButton;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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
	private ImageView levelIcon;
	
	public LevelPane(Controller controller, Media media){
		
		setId("level-grid-pane");
		
		this.controller = controller;
		this.media = media;
		
		Text title = new Text(Language.translate("levels"));
		title.setId("level-title");
		levelIcon = new ImageView(new Image(getClass().getResourceAsStream("/gui/spatialViewPane/images/level.png")));
		HBox titleIconContainer = new HBox();
		titleIconContainer.setId("title-icon-container");
		titleIconContainer.getChildren().addAll(title, levelIcon);
		
		Label volumeLabel = new Label(Language.translate("volume"));
		Label balanceLabel = new Label(Language.translate("balance"));
		Label trebleLabel = new Label(Language.translate("treble"));
		Label bassLabel = new Label(Language.translate("bass"));
		
		volume = new SliderButton(0.0, 100.0, 50.0, 120.0, null, true);
		volume.setSliderValue(50.0);
		balance = new SliderButton(-1.0, 1.0, 0.5, 120.0, null, true);
		balance.setSliderValue(0.5);
		treble = new SliderButton(0.0, 100.0, 0.0, 120.0, null, true);
		treble.setSliderValue(0.0);
		bass = new SliderButton(0.0, 100.0, 0.0, 120.0, null, true);
		bass.setSliderValue(0.0);

		add(titleIconContainer, 0, 0, 2, 1);
		add(volumeLabel, 0, 3);
		add(volume, 0, 4);
		add(balanceLabel, 1, 3);
		add(balance, 1, 4);
		add(trebleLabel, 0, 6);
		add(treble, 0, 7);
		add(bassLabel, 1, 6);
		add(bass, 1, 7);
		
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
