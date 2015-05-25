package gui.spatialViewPane;

import gui.common.Language;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import model.common.Media;
import controller.Controller;

public class PositionPane extends GridPane {

	private Controller controller;
	private Media media;
	
	private TextField left;
	private TextField right;
	private TextField top;
	private TextField bottom;
	private TextField rotation;
	private TextField zOrder;
	
	public PositionPane(Controller controller, Media media){
		
		this.controller = controller;
		this.media = media;
		
		Text title = new Text(Language.translate("position"));
		title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		Label leftLabel = new Label(Language.translate("left"));
		Label rightLabel = new Label(Language.translate("right"));
		Label topLabel = new Label(Language.translate("top"));
		Label bottomLabel = new Label(Language.translate("bottom"));
		Label rotationLabel = new Label(Language.translate("rotation"));
		Label zOrderLabel = new Label(Language.translate("z.order"));

		left = new TextField();
		right = new TextField();
		top = new TextField();
		bottom = new TextField();
		rotation = new TextField();
		zOrder = new TextField();
		
		add(title, 0, 0);
		add(leftLabel, 0, 1);
		add(rightLabel, 1, 1);
		add(topLabel, 0, 3);
		add(bottomLabel, 1, 3);
		add(rotationLabel, 0, 5);
		add(zOrderLabel, 1, 5);
		add(left, 0, 2);
		add(right, 1, 2);
		add(top, 0, 4);
		add(bottom, 1, 4);
		add(rotation, 0, 6);
		add(zOrder, 1, 6);
		
		setAlignment(Pos.CENTER);
		setHgap(5);
		setVgap(5);
		setStyle("-fx-background-color: #263238");
		setPadding(new Insets(10, 10, 10, 10));

	}
	
	public void setLeftValue(String value){
		this.left.setText(value);
	}
	
	public String getLeftValue(){
		return left.getText();
	}
	
	public void setRightValue(String value){
		this.right.setText(value);
	}
	
	public String getRightValue(){
		return right.getText();
	}
	
	public void setTopValue(String value){
		this.top.setText(value);
	}
	
	public String getTopValue(){
		return top.getText();
	}
	
	public void setBottomValue(String value){
		this.bottom.setText(value);
	}
	
	public String getBottomValue(){
		return bottom.getText();
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

	
}
