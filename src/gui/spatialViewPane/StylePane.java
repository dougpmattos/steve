package gui.spatialViewPane;

import gui.common.Language;
import gui.common.SliderButton;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.common.Media;
import model.common.MediaType;
import model.spatialView.FontFamily;
import model.spatialView.FontStyle;
import model.spatialView.FontWeight;
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
	
	public StylePane(Controller controller, Media media){
		
		setId("style-grid-pane");
		
		this.controller = controller;
		this.media = media;
		
		Text title = new Text(Language.translate("style"));
		title.setId("style-title");
		
		Label transparencyLabel = new Label(Language.translate("transparency"));
		
		transparency = new SliderButton(0.0, 100.0, 0.0, 200.0, null, true);
		transparency.setSliderValue(0.0);
		
		add(title, 0, 0);
		add(transparencyLabel, 0, 3);
		add(transparency, 0, 4);
		
		createFieldsTextMedia(media);
		
	}

	private void createFieldsTextMedia(Media media) {
		
		if(media.getType().equals(MediaType.TEXT)){
			
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
			
			add(fontFamilyLabel, 0, 3);
			add(fontSizeLabel, 1, 3);
			add(fontStyleLabel, 0, 5);
			add(fontWeightLabel, 1, 5);
			add(fontColorLabel, 0, 7);
			add(fontFamily, 0, 4);
			add(fontSize, 1, 4);
			add(fontStyle, 0, 6);
			add(fontWeight, 1, 6);
			add(fontColor, 0, 7);
			
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
	
}
