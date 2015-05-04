
package gui.spatialViewPane;

import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;


/**
 *
 * @author Douglas
 */
public class SpatialViewPane extends SplitPane{

	private DisplayPane displayPane;
	private PropertyPane propertyPane;
	
    public SpatialViewPane() {
  
    	setOrientation(Orientation.HORIZONTAL);
    	setDividerPositions(0.6);
    	
    	getStylesheets().add("gui/spatialViewPane/styles/spatialViewPane.css");
    	
    	displayPane = new DisplayPane();
    	propertyPane = new PropertyPane();
    	
    	getItems().addAll(displayPane, propertyPane);
    	
    }
    
}
