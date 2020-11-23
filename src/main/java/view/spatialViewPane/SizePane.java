package view.spatialViewPane;

import model.common.MediaNode;
import view.common.Language;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.spatialView.media.SizeProperty;
import model.spatialView.media.enums.AspectRatio;
import model.spatialView.media.enums.Size;
import controller.ApplicationController;

public class SizePane extends VBox {

	private ApplicationController applicationController;
	private MediaNode mediaNode;
	
	private TextField width;
	private TextField height;
	private ChoiceBox<AspectRatio> aspectRatio;
	private ChoiceBox<Size> widthUnit;
	private ChoiceBox<Size> heightUnit;
	private Button sizeButton;
	
	private BorderPane titleButtonBorderPane;
	private GridPane sizePropertyGridPane;
	
	public SizePane(ApplicationController applicationController, MediaNode mediaNode){
		
		setId("size-vbox");
		
		this.applicationController = applicationController;
		this.mediaNode = mediaNode;
		
		Text title = new Text(Language.translate("size"));
		title.setId("size-title");
		
		sizeButton = new Button();
		sizeButton.setId("size-button");
		sizeButton.setTooltip(new Tooltip(Language.translate("edit.size.graphically")));
		
		Label widthLabel = new Label(Language.translate("width"));
		Label heightLabel = new Label(Language.translate("height"));
		Label aspectRatioLabel = new Label(Language.translate("aspect.ratio"));
		widthLabel.setId("spatial-view-label");
		heightLabel.setId("spatial-view-label");

		width = new TextField();
		widthUnit = new ChoiceBox<Size>(FXCollections.observableArrayList(Size.PX, Size.PERCENTAGE));
		height = new TextField();
		heightUnit = new ChoiceBox<Size>(FXCollections.observableArrayList(Size.PX, Size.PERCENTAGE));
		aspectRatio = new ChoiceBox<AspectRatio>(FXCollections.observableArrayList(AspectRatio.SLICE, AspectRatio.FILL));
		aspectRatio.setId("size-aspect-ratio");
		
		titleButtonBorderPane = new BorderPane();
		titleButtonBorderPane.setId("title-button-hbox");
		titleButtonBorderPane.setLeft(title);
		titleButtonBorderPane.setRight(sizeButton);
		
		sizePropertyGridPane = new GridPane();
		sizePropertyGridPane.setId("size-property-grid-pane");
		sizePropertyGridPane.add(widthLabel, 0, 0);
		sizePropertyGridPane.add(width, 1, 0);
		sizePropertyGridPane.add(widthUnit, 2, 0);
		sizePropertyGridPane.add(heightLabel, 7, 0);
		sizePropertyGridPane.add(height, 8, 0);
		sizePropertyGridPane.add(heightUnit, 9, 0);
		sizePropertyGridPane.add(aspectRatioLabel, 0, 2);
		sizePropertyGridPane.add(aspectRatio, 1, 2);
		
		getChildren().add(titleButtonBorderPane);
		getChildren().add(sizePropertyGridPane);
		
		populateSizePane();
		
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
	
	private void populateSizePane(){
		
		SizeProperty sizeProperty = mediaNode.getPresentationProperty().getSizeProperty();

		setWidthValue(sizeProperty.getWidth());
		setHeightValue(sizeProperty.getHeight());
		setAspectRatioValue(sizeProperty.getAspectRatio());
		
	}
	
	public void populateSizePropertyJavaBean(){
		
		applicationController.populateSizePropertyJavaBean(this, mediaNode);
		
	}
}	
