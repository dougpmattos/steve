
package gui.spatialViewPane;

import javafx.scene.layout.BorderPane;


/**
 *
 * @author Douglas
 */
public class SpatialViewPane extends BorderPane{
	
	PropertyButtonPane propertyButtonPane;
	ControlButtonPane controlButtonPane;
	
    public SpatialViewPane() {
  
    	setId("spatial-view-pane");
    	getStylesheets().add("gui/spatialViewPane/styles/spatialViewPane.css");
    	
    	propertyButtonPane = new PropertyButtonPane();
    	controlButtonPane = new ControlButtonPane();
    	
    	setTop(propertyButtonPane);
    	setBottom(controlButtonPane);
    	
    }
    
}
