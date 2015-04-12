package gui.spatialViewPane;

import gui.common.Language;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;

public class PropertyButtonPane extends HBox{
	
	private Button propertyButton;
	private Button animationButton;
	
	public PropertyButtonPane(){
		
		setId("property-button-pane");
	    
	    createButtons();
	  
	    getChildren().add(propertyButton);
		getChildren().add(animationButton);
		
		createButtonActions();

	}
	
	public void createButtons(){
		
		propertyButton = new Button(Language.translate("PROPERTIES"));
		propertyButton.setId("property-button");
		propertyButton.setTooltip(new Tooltip(Language.translate("media.properties")));
		
		animationButton = new Button(Language.translate("ANIMATIONS"));
		animationButton.setId("animation-button");
		animationButton.setTooltip(new Tooltip(Language.translate("animations")));
		
	}
	
	public void createButtonActions(){
		
	}
	
}
