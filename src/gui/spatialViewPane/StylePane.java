package gui.spatialViewPane;

import gui.common.Language;
import gui.common.SliderButton;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.common.Media;
import model.common.enums.MediaType;
import model.spatialView.StyleProperty;
import model.spatialView.TextStyleProperty;
import model.spatialView.enums.FontFamily;
import model.spatialView.enums.FontStyle;
import model.spatialView.enums.FontWeight;
import controller.Controller;

public class StylePane extends GridPane {

	private Controller controller;
	private Media media;
	
	private SliderButton transparency;
	private ChoiceBox<FontFamily> fontFamily;
	private TextField fontSize;
	private ChoiceBox<FontStyle> fontStyle;
	private ChoiceBox<FontWeight> fontWeight;
	private ColorPicker fontColor;
	private ImageView styleIcon;
	
	public StylePane(Controller controller, Media media){
		
		setId("style-grid-pane");
		
		this.controller = controller;
		this.media = media;
		
		Text title = new Text(Language.translate("style"));
		title.setId("style-title");
		
		styleIcon = new ImageView(new Image(getClass().getResourceAsStream("/gui/spatialViewPane/images/style.png")));
		
		Label transparencyLabel = new Label(Language.translate("transparency"));
		
		transparency = new SliderButton(0.0, 100.0, 0.0, 200.0, null, true);
		transparency.setSliderValue(0.0);
		
		add(title, 0, 0);
		add(styleIcon, 3, 0);
		add(transparencyLabel, 0, 3);
		add(transparency, 0, 4, 2, 1);
		
		createFieldsTextMedia(media);
		
		populateStylePane();
		
	}

	private void createFieldsTextMedia(Media media) {
		
		if(media.getMediaType().equals(MediaType.TEXT)){
			
			Label fontFamilyLabel = new Label(Language.translate("font.family"));
			Label fontSizeLabel = new Label(Language.translate("font.size"));
			Label fontStyleLabel = new Label(Language.translate("font.style"));
			Label fontWeightLabel = new Label(Language.translate("font.weight"));
			Label fontColorLabel = new Label(Language.translate("font.color"));
			
			fontFamily = new ChoiceBox<FontFamily>(FXCollections.observableArrayList(FontFamily.TIMES_NEW_ROMAN));
			fontSize = new TextField();
			fontStyle = new ChoiceBox<FontStyle>(FXCollections.observableArrayList(FontStyle.NORMAL, FontStyle.ITALIC));
			fontWeight = new ChoiceBox<FontWeight>(FXCollections.observableArrayList(FontWeight.NORMAL, FontWeight.BOLD));
			fontColor = new ColorPicker(Color.WHITE);
			
			add(fontFamilyLabel, 0, 6);
			add(fontFamily, 0, 7);
			add(fontSizeLabel, 1, 6);
			add(fontSize, 1, 7);
			add(fontStyleLabel, 0, 9);
			add(fontStyle, 0, 10);
			add(fontWeightLabel, 1, 9);
			add(fontWeight, 1, 10);
			add(fontColorLabel, 0, 12);
			add(fontColor, 0, 13);
			
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
		
		if(media.getMediaType().equals(MediaType.TEXT)){
			
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
		
		if(media.getMediaType().equals(MediaType.TEXT)){
			
			controller.populateTextStylePropertyJavaBean(this, media);
			
		} else{
			
			controller.populateStylePropertyJavaBean(this, media);
			
		}
		
	}
	
}
