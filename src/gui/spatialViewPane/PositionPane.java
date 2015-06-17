package gui.spatialViewPane;

import gui.common.Language;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.common.Media;
import model.spatialView.PositionProperty;
import model.spatialView.enums.Size;
import controller.Controller;

public class PositionPane extends VBox {

	private Controller controller;
	private Media media;
	
	private TextField left;
	private TextField right;
	private TextField top;
	private TextField bottom;
	private TextField rotation;
	private TextField zOrder;
	private ChoiceBox<Size> leftUnit;
	private ChoiceBox<Size> rightUnit;
	private ChoiceBox<Size> topUnit;
	private ChoiceBox<Size> bottomUnit;
	private Button positionButton;
	
	private BorderPane titleButtonBorderPane;
	private GridPane positionPropertyGridPane;
	
	public PositionPane(Controller controller, Media media){
		
		setId("position-vbox");
		
		this.controller = controller;
		this.media = media;
		
		Text title = new Text(Language.translate("position"));
		title.setId("position-title");
		
		positionButton = new Button();
		positionButton.setId("position-button");
		positionButton.setTooltip(new Tooltip(Language.translate("edit.position.graphically")));
		
		Label leftLabel = new Label(Language.translate("left"));
		Label rightLabel = new Label(Language.translate("right"));
		Label topLabel = new Label(Language.translate("top"));
		Label bottomLabel = new Label(Language.translate("bottom"));
		Label rotationLabel = new Label(Language.translate("rotation"));
		Label zOrderLabel = new Label(Language.translate("z.order"));
		leftLabel.setId("spatial-view-label");
		rightLabel.setId("spatial-view-label");
		topLabel.setId("spatial-view-label");
		bottomLabel.setId("spatial-view-label");
		rotationLabel.setId("spatial-view-label");
		zOrderLabel.setId("spatial-view-label");

		left = new TextField();
		leftUnit = new ChoiceBox<Size>(FXCollections.observableArrayList(Size.PX, Size.PERCENTAGE));
		right = new TextField();
		rightUnit = new ChoiceBox<Size>(FXCollections.observableArrayList(Size.PX, Size.PERCENTAGE));
		top = new TextField();
		topUnit = new ChoiceBox<Size>(FXCollections.observableArrayList(Size.PX, Size.PERCENTAGE));
		bottom = new TextField();
		bottomUnit = new ChoiceBox<Size>(FXCollections.observableArrayList(Size.PX, Size.PERCENTAGE));
		rotation = new TextField();
		zOrder = new TextField();
		
		titleButtonBorderPane = new BorderPane();
		titleButtonBorderPane.setId("title-button-hbox");
		titleButtonBorderPane.setLeft(title);
		titleButtonBorderPane.setRight(positionButton);
		
		positionPropertyGridPane = new GridPane();
		positionPropertyGridPane.setId("position-property-grid-pane");
		positionPropertyGridPane.add(leftLabel, 0, 0);
		positionPropertyGridPane.add(left, 1, 0);
		positionPropertyGridPane.add(leftUnit, 2, 0);
		positionPropertyGridPane.add(rightLabel, 7, 0);
		positionPropertyGridPane.add(right, 8, 0);
		positionPropertyGridPane.add(rightUnit, 9, 0);
		positionPropertyGridPane.add(topLabel, 0, 2);
		positionPropertyGridPane.add(top, 1, 2);
		positionPropertyGridPane.add(topUnit, 2, 2);
		positionPropertyGridPane.add(bottomLabel, 7, 2);
		positionPropertyGridPane.add(bottom, 8, 2);
		positionPropertyGridPane.add(bottomUnit, 9, 2);
		positionPropertyGridPane.add(rotationLabel, 0, 4);
		positionPropertyGridPane.add(rotation, 1, 4);
		positionPropertyGridPane.add(zOrderLabel, 7, 4);
		positionPropertyGridPane.add(zOrder, 8, 4);
		
		getChildren().add(titleButtonBorderPane);
		getChildren().add(positionPropertyGridPane);
		
		populatePositionPane();
		
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
	
	public void setRotationValue(String value){
		this.rotation.setText(value);
	}
	
	public String getRotationValue(){
		return rotation.getText();
	}
	
	public void setZOrderValue(String value){
		this.zOrder.setText(value);
	}
	
	public String getZOrderValue(){
		return zOrder.getText();
	}
	
	private void populatePositionPane(){
		
		PositionProperty positionProperty = media.getPresentationProperty().getPositionProperty();
		
		setLeftValue(positionProperty.getLeft());
		setRightValue(positionProperty.getRight());
		setTopValue(positionProperty.getTop());
		setBottomValue(positionProperty.getBottom());
		setRotationValue(positionProperty.getRotation());
		setZOrderValue(String.valueOf(positionProperty.getOrderZ()));
		
	}
	
	public void populatePositionPropertyJavaBean(){
		
		controller.populatePositionPropertyJavaBean(this, media);
		
	}
	
}
