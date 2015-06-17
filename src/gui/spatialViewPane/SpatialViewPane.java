
package gui.spatialViewPane;

import gui.common.Language;
import gui.temporalViewPane.TemporalViewPane;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import model.common.Media;
import model.temporalView.TemporalView;
import controller.Controller;


/**
 *
 * @author Douglas
 */
public class SpatialViewPane extends SplitPane implements gui.common.Observer{

	private Controller controller;
	
	private TemporalView temporalViewModel;
	
	private DisplayPane displayPane;
	private PropertyPane propertyPane;
	private InfoPane infoPane;
	private Tab propertyTab;
	private Tab infoTab;
	private TabPane propertyInfoTabPane;
	
    public SpatialViewPane(Controller controller, TemporalView temporalViewModel, TemporalViewPane temporalViewPane) {
  
    	setOrientation(Orientation.HORIZONTAL);
    	setDividerPositions(0.5);
    	
    	getStylesheets().add("gui/spatialViewPane/styles/spatialViewPane.css");
    	
    	this.temporalViewModel = temporalViewModel;
    	
    	displayPane = new DisplayPane();
    	
    	HBox labelContainer = new HBox();
		labelContainer.setId("label-container");
		Label label = new Label(Language.translate("no.selected.media"));
		labelContainer.getChildren().add(label);
    	
    	getItems().addAll(labelContainer, displayPane);
    	
    	temporalViewPane.addObserver(this);
    	
    	this.controller = controller;
    	
    }

    @Override
	public void update(gui.common.Observable o, Object obj) {
		
    	if(obj instanceof Media){
    		Media selectedMedia = (Media) obj;
    		createPropertyInfoTabPane(controller, selectedMedia);
    	}
		
	}
    
	private void createPropertyInfoTabPane(Controller controller, Media media) {
		
		propertyPane = new PropertyPane(controller, media);
    	infoPane = new InfoPane(controller, media);
    	
    	propertyTab = new Tab();
    	propertyTab.setText(Language.translate("PROPERTIES"));
    	propertyTab.setClosable(false); 
    	propertyTab.setContent(propertyPane);
    	infoTab = new Tab();
    	infoTab.setText(Language.translate("INFO"));
    	infoTab.setClosable(false); 
    	infoTab.setContent(infoPane);

    	propertyInfoTabPane = new TabPane();
    	
    	propertyInfoTabPane.getTabs().add(propertyTab);
    	propertyInfoTabPane.getTabs().add(infoTab);
    	
    	getItems().clear();
    	getItems().addAll(propertyInfoTabPane, displayPane);
    	
	}
    
}
