package gui.spatialViewPane;

import gui.common.Language;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import model.common.Media;
import model.spatialView.CropProperty;
import model.spatialView.enums.Size;
import controller.Controller;

public class CropPane extends GridPane {

	private Controller controller;
	private Media media;
	
	private TextField left;
	private TextField right;
	private TextField top;
	private TextField bottom;
	private ChoiceBox<Size> leftUnit;
	private ChoiceBox<Size> rightUnit;
	private ChoiceBox<Size> topUnit;
	private ChoiceBox<Size> bottomUnit;
	private Button cropButton;
	
	public CropPane(Controller controller, Media media){
		
		setId("crop-grid-pane");
		
		this.controller = controller;
		this.media = media;
		
		Text title = new Text(Language.translate("crop"));
		title.setId("crop-title");
		
		cropButton = new Button();
		cropButton.setId("crop-button");
		cropButton.setTooltip(new Tooltip(Language.translate("crop.graphically")));
		
		Label leftLabel = new Label(Language.translate("left"));
		Label rightLabel = new Label(Language.translate("right"));
		Label topLabel = new Label(Language.translate("top"));
		Label bottomLabel = new Label(Language.translate("bottom"));

		left = new TextField();
		leftUnit = new ChoiceBox<Size>(FXCollections.observableArrayList(Size.PX, Size.PERCENTAGE));
		right = new TextField();
		rightUnit = new ChoiceBox<Size>(FXCollections.observableArrayList(Size.PX, Size.PERCENTAGE));
		top = new TextField();
		topUnit = new ChoiceBox<Size>(FXCollections.observableArrayList(Size.PX, Size.PERCENTAGE));
		bottom = new TextField();
		bottomUnit = new ChoiceBox<Size>(FXCollections.observableArrayList(Size.PX, Size.PERCENTAGE));
		
		add(title, 0, 0);
		add(cropButton, 3, 0);
		add(leftLabel, 0, 3);
		add(left, 0, 4);
		add(leftUnit, 1, 4);
		add(rightLabel, 2, 3);
		add(right, 2, 4);
		add(rightUnit, 3, 4);
		add(topLabel, 0, 6);
		add(top, 0, 7);
		add(topUnit, 1, 7);
		add(bottomLabel, 2, 6);
		add(bottom, 2, 7);
		add(bottomUnit, 3, 7);
		
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
		
		CropProperty cropProperty = media.getPresentationProperty().getCropProperty();
		
		setLeftValue(cropProperty.getLeft());
		setRightValue(cropProperty.getRight());
		setTopValue(cropProperty.getTop());
		setBottomValue(cropProperty.getBottom());
		
	}

	public void populateCropPropertyJavaBean() {
		
		controller.populateCropPropertyJavaBean(this, media);
		
	}
	
}
