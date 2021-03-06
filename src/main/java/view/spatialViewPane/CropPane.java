package view.spatialViewPane;

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
import model.common.MediaNode;
import model.spatialView.media.CropProperty;
import model.spatialView.media.enums.Size;
import controller.ApplicationController;

public class CropPane extends VBox {

	private ApplicationController applicationController;
	private MediaNode mediaNode;
	
	private TextField left;
	private TextField right;
	private TextField top;
	private TextField bottom;
	private ChoiceBox<Size> leftUnit;
	private ChoiceBox<Size> rightUnit;
	private ChoiceBox<Size> topUnit;
	private ChoiceBox<Size> bottomUnit;
	private Button cropButton;
	
	private BorderPane titleButtonBorderPane;
	private GridPane cropPropertyGridPane;
	
	public CropPane(ApplicationController applicationController, MediaNode mediaNode){
		
		setId("crop-vbox");
		
		this.applicationController = applicationController;
		this.mediaNode = mediaNode;
		
		Text title = new Text(Language.translate("crop"));
		title.setId("crop-title");
		
		cropButton = new Button();
		cropButton.setId("crop-button");
		cropButton.setTooltip(new Tooltip(Language.translate("crop.graphically")));
		
		Label leftLabel = new Label(Language.translate("left"));
		Label rightLabel = new Label(Language.translate("right"));
		Label topLabel = new Label(Language.translate("top"));
		Label bottomLabel = new Label(Language.translate("bottom"));
		leftLabel.setId("spatial-view-label");
		rightLabel.setId("spatial-view-label");
		topLabel.setId("spatial-view-label");
		bottomLabel.setId("spatial-view-label");
		
		left = new TextField();
		leftUnit = new ChoiceBox<Size>(FXCollections.observableArrayList(Size.PX, Size.PERCENTAGE));
		right = new TextField();
		rightUnit = new ChoiceBox<Size>(FXCollections.observableArrayList(Size.PX, Size.PERCENTAGE));
		top = new TextField();
		topUnit = new ChoiceBox<Size>(FXCollections.observableArrayList(Size.PX, Size.PERCENTAGE));
		bottom = new TextField();
		bottomUnit = new ChoiceBox<Size>(FXCollections.observableArrayList(Size.PX, Size.PERCENTAGE));
		
		titleButtonBorderPane = new BorderPane();
		titleButtonBorderPane.setId("title-button-hbox");
		titleButtonBorderPane.setLeft(title);
		titleButtonBorderPane.setRight(cropButton);
		
		cropPropertyGridPane = new GridPane();
		cropPropertyGridPane.setId("crop-property-grid-pane");
		cropPropertyGridPane.add(leftLabel, 0, 0);
		cropPropertyGridPane.add(left, 1, 0);
		cropPropertyGridPane.add(leftUnit, 2, 0);
		cropPropertyGridPane.add(rightLabel, 7, 0);
		cropPropertyGridPane.add(right, 8, 0);
		cropPropertyGridPane.add(rightUnit, 9, 0);
		cropPropertyGridPane.add(topLabel, 0, 2);
		cropPropertyGridPane.add(top, 1, 2);
		cropPropertyGridPane.add(topUnit, 2, 2);
		cropPropertyGridPane.add(bottomLabel, 7, 2);
		cropPropertyGridPane.add(bottom, 8, 2);
		cropPropertyGridPane.add(bottomUnit, 9, 2);
		
		getChildren().add(titleButtonBorderPane);
		getChildren().add(cropPropertyGridPane);
		
		populateCropPane();
		
	}
	
	public void setLeftValue(String value){
		
		if(value.contains(Size.PX.toString())){
			this.left.setText(value.substring(0, value.indexOf('p')));
			this.leftUnit.setValue(Size.PX);
		}else {
			this.left.setText(value.substring(0, value.indexOf('%')));
			this.leftUnit.setValue(Size.PERCENTAGE);
		}
		
	}
	
	public String getLeftValue(){
		return left.getText() + leftUnit.getValue().toString();
	}
	
	public void setRightValue(String value){
		
		if(value.contains(Size.PX.toString())){
			this.right.setText(value.substring(0, value.indexOf('p')));
			this.rightUnit.setValue(Size.PX);
		}else {
			this.right.setText(value.substring(0, value.indexOf('%')));
			this.rightUnit.setValue(Size.PERCENTAGE);
		}
		
	}
	
	public String getRightValue(){
		return right.getText() + rightUnit.getValue().toString();
	}
	
	public void setTopValue(String value){
		
		if(value.contains(Size.PX.toString())){
			this.top.setText(value.substring(0, value.indexOf('p')));
			this.topUnit.setValue(Size.PX);
		}else {
			this.top.setText(value.substring(0, value.indexOf('%')));
			this.topUnit.setValue(Size.PERCENTAGE);
		}
		
	}
	
	public String getTopValue(){
		return top.getText() + topUnit.getValue().toString();
	}
	
	public void setBottomValue(String value){
		
		if(value.contains(Size.PX.toString())){
			this.bottom.setText(value.substring(0, value.indexOf('p')));
			this.bottomUnit.setValue(Size.PX);
		}else {
			this.bottom.setText(value.substring(0, value.indexOf('%')));
			this.bottomUnit.setValue(Size.PERCENTAGE);
		}
		
	}
	
	public String getBottomValue(){
		return bottom.getText() + bottomUnit.getValue().toString();
	}
	
	private void populateCropPane(){
		
		CropProperty cropProperty = mediaNode.getPresentationProperty().getCropProperty();
		
		setLeftValue(cropProperty.getLeft());
		setRightValue(cropProperty.getRight());
		setTopValue(cropProperty.getTop());
		setBottomValue(cropProperty.getBottom());
		
	}

	public void populateCropPropertyJavaBean() {
		
		applicationController.populateCropPropertyJavaBean(this, mediaNode);
		
	}
	
}
