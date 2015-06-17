package gui.spatialViewPane;

import gui.common.Language;
import gui.common.SliderButton;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.common.Media;
import model.spatialView.LevelProperty;
import controller.Controller;

public class LevelPane extends VBox {

	private Controller controller;
	private Media media;
	
	private SliderButton volume;
	private SliderButton balance;
	private SliderButton treble;
	private SliderButton bass;
	private ImageView levelIcon;
	
	private BorderPane titleImageBorderPane;
	private GridPane levelPropertyGridPane;
	
	public LevelPane(Controller controller, Media media){
		
		setId("level-vbox");
		
		this.controller = controller;
		this.media = media;
		
		Text title = new Text(Language.translate("levels"));
		title.setId("level-title");
		levelIcon = new ImageView(new Image(getClass().getResourceAsStream("/gui/spatialViewPane/images/level.png")));
		
		Label volumeLabel = new Label(Language.translate("volume"));
		Label balanceLabel = new Label(Language.translate("balance"));
		Label trebleLabel = new Label(Language.translate("treble"));
		Label bassLabel = new Label(Language.translate("bass"));
		volumeLabel.setId("spatial-view-label");
		balanceLabel.setId("spatial-view-label");
		trebleLabel.setId("spatial-view-label");
		bassLabel.setId("spatial-view-label");
		
		volume = new SliderButton(0.0, 100.0, 50.0, 120.0, null, true);
		volume.setSliderValue(50.0);
		balance = new SliderButton(-1.0, 1.0, 0.5, 120.0, null, true);
		balance.setSliderValue(0.5);
		treble = new SliderButton(0.0, 100.0, 0.0, 120.0, null, true);
		treble.setSliderValue(0.0);
		bass = new SliderButton(0.0, 100.0, 0.0, 120.0, null, true);
		bass.setSliderValue(0.0);

		titleImageBorderPane = new BorderPane();
		titleImageBorderPane.setId("title-button-hbox");
		titleImageBorderPane.setLeft(title);
		titleImageBorderPane.setRight(levelIcon);
		
		levelPropertyGridPane = new GridPane();
		levelPropertyGridPane.setId("level-property-grid-pane");
		levelPropertyGridPane.add(volumeLabel, 0, 0);
		levelPropertyGridPane.add(volume, 0, 1);
		levelPropertyGridPane.add(balanceLabel, 2, 0);
		levelPropertyGridPane.add(balance, 2, 1);
		levelPropertyGridPane.add(trebleLabel, 0, 3);
		levelPropertyGridPane.add(treble, 0, 4);
		levelPropertyGridPane.add(bassLabel, 2, 3);
		levelPropertyGridPane.add(bass, 2, 4);
		
		getChildren().add(titleImageBorderPane);
		getChildren().add(levelPropertyGridPane);
		
		populateLevelPane();
		
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

	private void populateLevelPane() {
		
		LevelProperty levelProperty = media.getPresentationProperty().getLevelProperty();

		setVolume(levelProperty.getVolume());
		setBalance(levelProperty.getBalance());
		setTreble(levelProperty.getTreble());
		setBass(levelProperty.getBass());
		
	}
	
	public void populateLevelPropertyJavaBean() {
		
		controller.populateLevelPropertyJavaBean(this, media);
		
	}
	
}
