package view.spatialViewPane;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.common.Media;
import model.spatialView.SizeProperty;
import model.spatialView.enums.AspectRatio;
import model.spatialView.enums.Size;
import view.common.Language;
import controller.Controller;

public class SizePane extends VBox {

	private Controller controller;
	private Media media;	
	
	private TextField width;
	private TextField height;
	private ChoiceBox<AspectRatio> aspectRatio;
	private ChoiceBox<Size> widthUnit;
	private ChoiceBox<Size> heightUnit;
	private Button sizeButton;
	
	private BorderPane titleButtonBorderPane;
	private GridPane sizePropertyGridPane;
	
	private StackPane screen;
	private ControlButtonPane controlButtonPane;
	
	public SizePane(Controller controller, Media media){
		
		setId("size-vbox");
		
		this.controller = controller;
		this.media = media;
		
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
		aspectRatio = new ChoiceBox<AspectRatio>(FXCollections.observableArrayList(AspectRatio.HIDDEN, AspectRatio.SLICE, AspectRatio.FILL));
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
		aspectRatio.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
			media.getPresentationProperty().getSizeProperty().setAspectRatio(aspectRatio.getItems().get((int) newValue));
			ImageView mediaContent = new ImageView(new Image(media.getFile().toURI().toString()));
			this.controlButtonPane.setImagePresentationProperties(mediaContent, media);
			this.screen.getChildren().clear();
			this.screen.getChildren().add((ImageView) mediaContent);
//			System.out.println("ASPECT RATIO - Mudou para "+aspectRatio.getItems().get((int) newValue));

		});
		this.controlButtonPane = controller.getStevePane().getSpatialViewPane().getDisplayPane().getControlButtonPane();
		this.screen = controlButtonPane.getScreen();
		
		getChildren().add(titleButtonBorderPane);
		getChildren().add(sizePropertyGridPane);
		
		width.textProperty().addListener((observable, oldValue, newValue) -> {
			
			if (Integer.parseInt(newValue.replace("%","")) < 1) newValue = "100";
			media.getPresentationProperty().getSizeProperty().setWidth(newValue);
			Object mediaContentObject = controlButtonPane.getMediaContent(media);
			controlButtonPane.setImagePresentationProperties((ImageView) mediaContentObject, media);
//			mediaContent.setPreserveRatio(false);
//			mediaContent.setFitWidth((Double.parseDouble(newValue)/100)*screen.getWidth());
			this.screen.getChildren().clear();
			this.screen.getChildren().add((ImageView) mediaContentObject);
		});
		
		height.textProperty().addListener((observable, oldValue, newValue) -> {

			if (Integer.parseInt(newValue.replace("%","")) < 1) newValue = "100";
			media.getPresentationProperty().getSizeProperty().setHeight(newValue);
			Object mediaContentObject = controlButtonPane.getMediaContent(media);
			controlButtonPane.setImagePresentationProperties((ImageView) mediaContentObject, media);
//			mediaContent.setPreserveRatio(false);
//			mediaContent.setFitHeight((Double.parseDouble(newValue)/100)*screen.getHeight());
			
			this.screen.getChildren().clear();
			this.screen.getChildren().add((ImageView) mediaContentObject);
		});

		populateSizePane();
		
		/*ChangeListener heightChanged = new ChangeListener<String>(){
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				ImageView mediaContent = new ImageView();
				Image image = new Image(media.getFile().toURI().toString());
				mediaContent.setImage(image);
				media.getPresentationProperty().getSizeProperty().setWidth(newValue);				
				
				if(aspectRatio.getValue()==aspectRatio.getValue().SLICE){
					width.setText(newValue);
					media.getPresentationProperty().getSizeProperty().setWidth(newValue);				
				}
				controlButtonPane.setImagePresentationProperties(mediaContent, media);
				screen.getChildren().clear();
				screen.getChildren().add((ImageView) mediaContent);
			}
		};
		height.textProperty().addListener(heightChanged);
		
		ChangeListener widthChanged = new ChangeListener<String>(){
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				ImageView mediaContent = new ImageView();
				Image image = new Image(media.getFile().toURI().toString());
				mediaContent.setImage(image);
				media.getPresentationProperty().getSizeProperty().setHeight(newValue);				
				
				if(aspectRatio.getValue()==aspectRatio.getValue().SLICE){
					height.textProperty().removeListener(heightChanged);
					height.setText(newValue);
					media.getPresentationProperty().getSizeProperty().setHeight(newValue);
					height.textProperty().addListener(heightChanged);
				}
				controlButtonPane.setImagePresentationProperties(mediaContent, media);
				screen.getChildren().clear();
				screen.getChildren().add((ImageView) mediaContent);
			}			
		};
		width.textProperty().addListener(widthChanged);*/
		
	}
	
	public void setWidthValue(String value){
		
		if(value.contains(Size.PX.toString())){
			this.width.setText(value.replace("%",""));
			this.widthUnit.setValue(Size.PX);
		}else {
			this.width.setText(value.replace("%",""));
			this.widthUnit.setValue(Size.PERCENTAGE);
		}
		

	}
	
	public String getWidthValue(){
		return width.getText() + widthUnit.getValue().toString();
	}
	
	public void setHeightValue(String value){
		System.out.println("SETTANDO VALOR " +value);
		System.out.println("Size.PX.toString() "+Size.PX.toString());
		if(value.contains(Size.PX.toString())){

			this.height.setText(value.substring(0, value.indexOf('p')));
			this.heightUnit.setValue(Size.PX);
		}else {
			this.height.setText(value.replace("%",""));
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
		
		SizeProperty sizeProperty = media.getPresentationProperty().getSizeProperty();

		setWidthValue(sizeProperty.getWidth());
		System.out.println("Is sizeProperty.getWidth() null? "+ sizeProperty.getWidth());
		setHeightValue(sizeProperty.getHeight());
		System.out.println("Is sizeProperty.getHeight() null? "+ sizeProperty.getHeight());
		setAspectRatioValue(sizeProperty.getAspectRatio());
		
	}
	
	public void populateSizePropertyJavaBean(){
		
		controller.populateSizePropertyJavaBean(this, media);
		
	}
}	
