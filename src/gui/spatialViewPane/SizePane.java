package gui.spatialViewPane;

import gui.common.Language;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import model.common.Media;
import model.spatialView.AspectRatio;
import controller.Controller;

public class SizePane extends GridPane {

	private Controller controller;
	private Media media;
	
	private TextField width;
	private TextField height;
	private ChoiceBox<AspectRatio> aspectRatio;
	
	public SizePane(Controller controller, Media media){
		
		this.controller = controller;
		this.media = media;
		
		Text title = new Text(Language.translate("size"));
		title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		Label widthLabel = new Label(Language.translate("width"));
		Label heightLabel = new Label(Language.translate("height"));
		Label aspectRatioLabel = new Label(Language.translate("aspect.ratio"));

		width = new TextField();
		height = new TextField();
		aspectRatio = new ChoiceBox<AspectRatio>(FXCollections.observableArrayList(AspectRatio.SLICE, AspectRatio.FILL, AspectRatio.NONE));
		
		add(title, 0, 0);
		add(widthLabel, 0, 1);
		add(heightLabel, 1, 1);
		add(aspectRatioLabel, 0, 3);
		add(width, 0, 2);
		add(height, 1, 2);
		add(aspectRatio, 0, 4);
		
		setAlignment(Pos.CENTER);
		setHgap(5);
		setVgap(5);
		setStyle("-fx-background-color: #263238");
		setPadding(new Insets(10, 10, 10, 10));
		
	}
	
	public void setWidthValue(String value){
		this.width.setText(value);
	}
	
	public String getWidthValue(){
		return width.getText();
	}
	
	public void setHeightValue(String value){
		this.height.setText(value);
	}
	
	public String getHeightValue(){
		return height.getText();
	}
	
	public void setAspectRatioValue(AspectRatio value){
		this.aspectRatio.setValue(value);
	}
	
	public AspectRatio getAspectRatioValue(){
		return aspectRatio.getValue();
	}
	
}
