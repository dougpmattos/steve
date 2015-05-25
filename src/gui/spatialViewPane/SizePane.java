package gui.spatialViewPane;

import gui.common.Language;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import model.common.Media;
import model.spatialView.AspectRatio;
import model.spatialView.Size;
import controller.Controller;

public class SizePane extends GridPane {

	private Controller controller;
	private Media media;
	
	private TextField width;
	private TextField height;
	private ChoiceBox<AspectRatio> aspectRatio;
	private ChoiceBox<Size> widthUnit;
	private ChoiceBox<Size> heightUnit;
	
	public SizePane(Controller controller, Media media){
		
		setId("size-grid-pane");
		
		this.controller = controller;
		this.media = media;
		
		Text title = new Text(Language.translate("size"));
		title.setId("size-title");
		
		Label widthLabel = new Label(Language.translate("width"));
		Label heightLabel = new Label(Language.translate("height"));
		Label aspectRatioLabel = new Label(Language.translate("aspect.ratio"));

		width = new TextField();
		widthUnit = new ChoiceBox<Size>(FXCollections.observableArrayList(Size.PX, Size.PERCENTAGE));
		height = new TextField();
		heightUnit = new ChoiceBox<Size>(FXCollections.observableArrayList(Size.PX, Size.PERCENTAGE));
		aspectRatio = new ChoiceBox<AspectRatio>(FXCollections.observableArrayList(AspectRatio.SLICE, AspectRatio.FILL, AspectRatio.NONE));
		
		add(title, 0, 0);
		add(widthLabel, 0, 3);
		add(width, 0, 4);
		add(widthUnit, 1, 4);
		add(heightLabel, 2, 3);
		add(height, 2, 4);
		add(heightUnit, 3, 4);
		add(aspectRatioLabel, 0, 6);
		add(aspectRatio, 0, 7);
		
	}
	
	public void setWidthValue(String value){
		
		if(value.contains(Size.PX.toString())){
			this.width.setText(value.substring(0, value.indexOf('p')));
			this.widthUnit.setValue(Size.PX);
		}else {
			this.width.setText(value.substring(0, value.indexOf('%')));
			this.widthUnit.setValue(Size.PERCENTAGE);
		}

	}
	
	public String getWidthValue(){
		return width.getText() + widthUnit.getValue().toString();
	}
	
	public void setHeightValue(String value){
		
		if(value.contains(Size.PX.toString())){
			this.height.setText(value.substring(0, value.indexOf('p')));
			this.heightUnit.setValue(Size.PX);
		}else {
			this.height.setText(value.substring(0, value.indexOf('%')));
			this.heightUnit.setValue(Size.PERCENTAGE);
		}

	}
	
	public String getHeightValue(){
		return height.getText() + heightUnit.getValue().toString();
	}
	
	public void setAspectRatioValue(AspectRatio value){
		this.aspectRatio.setValue(value);
	}
	
	public AspectRatio getAspectRatioValue(){
		return aspectRatio.getValue();
	}
	
}
