package view.spatialViewPane;

import view.common.Language;
import view.common.SliderButton;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.common.Media;
import model.common.enums.MediaType;
import model.spatialView.media.StyleProperty;
import model.spatialView.media.TextStyleProperty;
import model.spatialView.media.enums.FontFamily;
import model.spatialView.media.enums.FontStyle;
import model.spatialView.media.enums.FontWeight;
import controller.Controller;

public class StylePane extends VBox {

	private Controller controller;
	private Media media;
	
	private SliderButton transparency;
	private ChoiceBox<FontFamily> fontFamily;
	private TextField fontSize;
	private ChoiceBox<FontStyle> fontStyle;
	private ChoiceBox<FontWeight> fontWeight;
	private ColorPicker fontColor;
	private ImageView styleIcon;
	
	private BorderPane titleImageBorderPane;
	private GridPane stylePropertyGridPane;
	
	public StylePane(Controller controller, Media media){
		
		setId("style-vbox");
		
		this.controller = controller;
		this.media = media;
		
		Text title = new Text(Language.translate("style"));
		title.setId("style-title");
		
		styleIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/spatialViewPane/style.png")));
		
		Label transparencyLabel = new Label(Language.translate("transparency"));
		transparencyLabel.setId("spatial-view-label");
		
		transparency = new SliderButton(0.0, 100.0, 0.0, 200.0, null, true);
		transparency.setSliderValue(0.0);
		
		titleImageBorderPane = new BorderPane();
		titleImageBorderPane.setId("title-button-hbox");
		titleImageBorderPane.setLeft(title);
		titleImageBorderPane.setRight(styleIcon);
		
		stylePropertyGridPane = new GridPane();
		stylePropertyGridPane.setId("style-property-grid-pane");
		stylePropertyGridPane.add(transparencyLabel, 0, 0);
		stylePropertyGridPane.add(transparency, 1, 0, 2, 1);
	
		createFieldsTextMedia(media);
		
		getChildren().add(titleImageBorderPane);
		getChildren().add(stylePropertyGridPane);
		
		populateStylePane();
		
	}

	private void createFieldsTextMedia(Media media) {
		
		if(media.getType().equals(MediaType.TEXT)){
			
			Label fontFamilyLabel = new Label(Language.translate("font.family"));
			Label fontSizeLabel = new Label(Language.translate("font.size"));
			Label fontStyleLabel = new Label(Language.translate("font.style"));
			Label fontWeightLabel = new Label(Language.translate("font.weight"));
			Label fontColorLabel = new Label(Language.translate("font.color"));
			fontFamilyLabel.setId("spatial-view-label");
			fontSizeLabel.setId("spatial-view-label");
			fontStyleLabel.setId("spatial-view-label");
			fontWeightLabel.setId("spatial-view-label");
			fontColorLabel.setId("spatial-view-label");
			
			fontFamily = new ChoiceBox<FontFamily>(FXCollections.observableArrayList(FontFamily.TIMES_NEW_ROMAN));
			fontSize = new TextField();
			fontStyle = new ChoiceBox<FontStyle>(FXCollections.observableArrayList(FontStyle.NORMAL, FontStyle.ITALIC));
			fontWeight = new ChoiceBox<FontWeight>(FXCollections.observableArrayList(FontWeight.NORMAL, FontWeight.BOLD));
			fontColor = new ColorPicker(Color.WHITE);
			fontFamily.setId("font-style-field");
			fontSize.setId("font-style-field");
			fontStyle.setId("font-style-field");
			fontWeight.setId("font-style-field");
			
			stylePropertyGridPane.add(fontFamilyLabel, 0, 2);
			stylePropertyGridPane.add(fontFamily, 1, 2);
			stylePropertyGridPane.add(fontSizeLabel, 2, 2);
			stylePropertyGridPane.add(fontSize, 3, 2);
			stylePropertyGridPane.add(fontStyleLabel, 0, 4);
			stylePropertyGridPane.add(fontStyle, 1, 4);
			stylePropertyGridPane.add(fontWeightLabel, 2, 4);
			stylePropertyGridPane.add(fontWeight, 3, 4);
			stylePropertyGridPane.add(fontColorLabel, 0, 6);
			stylePropertyGridPane.add(fontColor, 1, 6, 1, 2);
			
		}
		
	}
	
	public void setTransparency(Double value){
		transparency.setSliderValue(value);
	}
	
	public Double getTransparency(){
		return transparency.getSliderValue();
	}
	
	public void setFontFamilyValue(FontFamily value){
		this.fontFamily.setValue(value);
	}
	
	public FontFamily getFontFamilyValue(){
		return fontFamily.getValue();
	}
	
	public void setFontSizeValue(String value){
		this.fontSize.setText(value);
	}
	
	public String getFontSizeValue(){
		return fontSize.getText();
	}
	
	public void setFontStyleValue(FontStyle value){
		this.fontStyle.setValue(value);
	}
	
	public FontStyle getFontStyleValue(){
		return fontStyle.getValue();
	}
	
	public void setFontWeightValue(FontWeight value){
		this.fontWeight.setValue(value);
	}
	
	public FontWeight getFontWeightValue(){
		return fontWeight.getValue();
	}
	
	public void setFontColorValue(Color value){
		this.fontColor.setValue(value);
	}
	
	public Color getFontColorValue(){
		return fontColor.getValue();
	}
	
	private void populateStylePane(){
		
		if(media.getType().equals(MediaType.TEXT)){
			
			TextStyleProperty textStyleProperty = media.getPresentationProperty().getTextStyleProperty();
			
			setTransparency(textStyleProperty.getTransparency());
			setFontFamilyValue(textStyleProperty.getFontFamily());
			setFontSizeValue(String.valueOf(textStyleProperty.getFontSize()));
			setFontStyleValue(textStyleProperty.getFontStyle());
			setFontWeightValue(textStyleProperty.getFontWeight());
			setFontColorValue(new Color(textStyleProperty.getFontColor().getRed(), textStyleProperty.getFontColor().getGreen(), textStyleProperty.getFontColor().getBlue(), 1));
			
		} else{
			
			StyleProperty styleProperty = media.getPresentationProperty().getStyleProperty();
			setTransparency(styleProperty.getTransparency());
			
		}
		
	}

	public void populateStylePropertyJavaBean() {
		
		if(media.getType().equals(MediaType.TEXT)){
			
			controller.populateTextStylePropertyJavaBean(this, media);
			
		} else{
			
			controller.populateStylePropertyJavaBean(this, media);
			
		}
		
	}
	
}
