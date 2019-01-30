package view.spatialViewPane;

import java.awt.event.MouseListener;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.common.Media;
import model.spatialView.PositionProperty;
import model.spatialView.enums.AspectRatio;
import model.spatialView.enums.Size;
import view.common.Language;
import view.temporalViewPane.TemporalChainPane;
import controller.Controller;

public class PositionPane extends VBox {

	private Controller controller;
	private Media media;
	
	private TextField left;
	private TextField right;
	private TextField top;
	private TextField bottom;
//	private TextField rotation;
	private TextField zOrder;
	private ChoiceBox<Size> leftUnit;
	private ChoiceBox<Size> rightUnit;
	private ChoiceBox<Size> topUnit;
	private ChoiceBox<Size> bottomUnit;
	private Button positionButton;	 
	
	private BorderPane titleButtonBorderPane;
	private GridPane positionPropertyGridPane;
	
	private StackPane screen;
	private ControlButtonPane controlButtonPane;
	private ImageView imageView;
	
	public PositionPane(Controller controller, Media media){
		
		setId("position-vbox");
				
		this.controller = controller;
		this.media = media;
		this.controlButtonPane = controller.getStevePane().getSpatialViewPane().getDisplayPane().getControlButtonPane();
		this.imageView = new ImageView(new Image(media.getFile().toURI().toString()));
		this.screen = controlButtonPane.getScreen();
		
		Text title = new Text(Language.translate("position"));
		title.setId("position-title");
		
		positionButton = new Button();
		positionButton.setId("position-button");
		positionButton.setTooltip(new Tooltip(Language.translate("edit.position.graphically")));
		
		Label leftLabel = new Label(Language.translate("left"));
		Label rightLabel = new Label(Language.translate("right"));
		Label topLabel = new Label(Language.translate("top"));
		Label bottomLabel = new Label(Language.translate("bottom"));
//		Label rotationLabel = new Label(Language.translate("rotation"));
		Label zOrderLabel = new Label(Language.translate("z.order"));
		leftLabel.setId("spatial-view-label");
		rightLabel.setId("spatial-view-label");
		topLabel.setId("spatial-view-label");
		bottomLabel.setId("spatial-view-label");
//		rotationLabel.setId("spatial-view-label");
		zOrderLabel.setId("spatial-view-label");
				
		//populatePositionPane();
		
		left = new TextField();
		leftUnit = new ChoiceBox<Size>(FXCollections.observableArrayList(Size.PX, Size.PERCENTAGE));
		right = new TextField();
		rightUnit = new ChoiceBox<Size>(FXCollections.observableArrayList(Size.PX, Size.PERCENTAGE));
		top = new TextField();
		topUnit = new ChoiceBox<Size>(FXCollections.observableArrayList(Size.PX, Size.PERCENTAGE));
		bottom = new TextField();
		bottomUnit = new ChoiceBox<Size>(FXCollections.observableArrayList(Size.PX, Size.PERCENTAGE));
//		rotation = new TextField();
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
//		positionPropertyGridPane.add(rotationLabel, 0, 4);
//		positionPropertyGridPane.add(rotation, 1, 4);
		positionPropertyGridPane.add(zOrderLabel, 7, 4);
		positionPropertyGridPane.add(zOrder, 8, 4);
		populatePositionPane();
		getChildren().add(titleButtonBorderPane);
		getChildren().add(positionPropertyGridPane);
												

		left.textProperty().addListener((observable, oldValue, newValue) -> {
			System.out.println("MUDOU O LISTENER OUVINDO CHANGES LEFT: "+left.getText());
			media.getPresentationProperty().getPositionProperty().setLeft(left.getText());
			Object mediaContent = controlButtonPane.getMediaContent(media);
			controlButtonPane.setImagePresentationProperties((ImageView) mediaContent, media);
			screen.getChildren().clear();
			screen.getChildren().add((ImageView) mediaContent);


		});
		right.textProperty().addListener((observable, oldValue, newValue) -> {

			media.getPresentationProperty().getPositionProperty().setRight(right.getText());
			Object mediaContent = controlButtonPane.getMediaContent(media);
			controlButtonPane.setImagePresentationProperties((ImageView) mediaContent, media);
			screen.getChildren().clear();
			screen.getChildren().add((ImageView) mediaContent);

		});
		top.textProperty().addListener((observable, oldValue, newValue) -> {
			media.getPresentationProperty().getPositionProperty().setTop(top.getText());
			Object mediaContent = controlButtonPane.getMediaContent(media);
			controlButtonPane.setImagePresentationProperties((ImageView) mediaContent, media);
			screen.getChildren().clear();
			screen.getChildren().add((ImageView) mediaContent);
		});
		bottom.textProperty().addListener((observable, oldValue, newValue) -> {
			System.out.println("aqui mudou para "+newValue+" e o top é "+top.getText());
			media.getPresentationProperty().getPositionProperty().setBottom(newValue);
			Object mediaContent = controlButtonPane.getMediaContent(media);
			controlButtonPane.setImagePresentationProperties((ImageView) mediaContent, media);
			screen.getChildren().clear();
			screen.getChildren().add((ImageView) mediaContent);

		});

	}
	
	

	public void setLeftValue(String value){
		
		if(value.contains(Size.PX.toString())){
			this.left.setText(value.substring(0, value.indexOf('p')));
			this.leftUnit.setValue(Size.PX);
		}else {
			if (value.indexOf('%')!=-1){
				System.out.println("Settando left = "+value+"% ");
				this.left.setText(value.substring(0, value.indexOf('%')));
				this.leftUnit.setValue(Size.PERCENTAGE);
			}
			else {
				System.out.println("Settando left = "+value);
				this.left.setText(value);
				this.leftUnit.setValue(Size.PERCENTAGE);
			}
	
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
			if(value.indexOf('%')!=-1){
				this.right.setText(value.substring(0, value.indexOf('%')));
				this.rightUnit.setValue(Size.PERCENTAGE);
			}
			else{
				this.right.setText(value);
				this.rightUnit.setValue(Size.PERCENTAGE);
			}
		
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
			if(value.indexOf('%')==-1){
				this.top.setText(value);
				this.topUnit.setValue(Size.PERCENTAGE);
			}
			else{
				this.top.setText(value.substring(0, value.indexOf('%')));
				this.topUnit.setValue(Size.PERCENTAGE);
			}			
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
			if(value.indexOf('%')==-1){
				this.bottom.setText(value);
				this.bottomUnit.setValue(Size.PERCENTAGE);
			}
			else{
				this.bottom.setText(value.substring(0, value.indexOf('%')));
				this.bottomUnit.setValue(Size.PERCENTAGE);
			}
			
		}
		
	}
	
	public String getBottomValue(){
		return bottom.getText() + bottomUnit.getValue().toString();
	}
	
//	public void setRotationValue(String value){
//		this.rotation.setText(value);
//	}
//	
//	public String getRotationValue(){
//		return rotation.getText();
//	}
	
	public void setZOrderValue(String value){
		this.zOrder.setText(value);
	}
	
	public String getZOrderValue(){
		return zOrder.getText();
	}
	
	public void populatePositionPane(){
		
		PositionProperty positionProperty = media.getPresentationProperty().getPositionProperty();

    	setLeftValue(positionProperty.getLeft());
		setRightValue(positionProperty.getRight());
		setTopValue(positionProperty.getTop());
		setBottomValue(positionProperty.getBottom());
		//setRotationValue(positionProperty.getRotation());
		setZOrderValue(String.valueOf(positionProperty.getOrderZ()));
		
    
		
	}
	
	public void populatePositionPropertyJavaBean(){
		
		controller.populatePositionPropertyJavaBean(this, media);
		
	}
	
	public String calculateBottomMargin(Media media, ImageView imageView){
		
		double height = this.screen.getHeight();
		imageView.setFitHeight(height);
		double boundHeight = imageView.getBoundsInParent().getHeight();
			
		double dY = boundHeight + imageView.getTranslateY(); //coordenada da borda inferior
		System.out.println((height - dY)/height);
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.CEILING);		
		return df.format(((height - dY)/height)*100);
		
	}
	public String calculateRightMargin(Media media, ImageView imageView){
		double width = this.screen.getWidth();
		imageView.setFitWidth(width);
        
		double boundWidth = imageView.getBoundsInParent().getWidth();
		double dX = boundWidth + imageView.getTranslateX();
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.CEILING);		
		return df.format(((width - dX)/width)*100);
	}
	public String calculateTopMargin(Media media, ImageView imageView){
		
		double height = this.screen.getHeight();
		//double mediaHeight = imageView.getBoundsInParent().getHeight();		
		double dY = imageView.getTranslateY(); //coordenada da borda superior
		System.out.println((dY)/height);
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.CEILING);		
		return df.format(((dY)/height)*100);
		
	}
	
	public String calculateLeftMargin(Media media, ImageView imageView){
		double width = this.screen.getWidth();
		double dX = imageView.getTranslateX();
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.CEILING);		
		return df.format(((dX)/width)*100);
	}
	
}
