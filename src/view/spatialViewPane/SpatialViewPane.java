
package view.spatialViewPane;

import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import model.common.Media;
import model.temporalView.TemporalView;
import view.common.Language;
import view.repositoryPane.RepositoryPane;
import view.temporalViewPane.TemporalViewPane;
import controller.Controller;


/**
 *
 * @author Douglas
 */
public class SpatialViewPane extends SplitPane implements view.common.Observer {

	private Controller controller;
	
	private TemporalView temporalViewModel;
	
	private DisplayPane displayPane;
	private PropertyPane propertyPane;
	private TemporalMediaInfoPane temporalMediaInfoPane;
	private RepositoryMediaInfoPane repositoryMediaInfoPane;
	private Tab propertyTab;
	private Tab infoTab;
	private TabPane propertyInfoTabPane;
	private Media selectedMedia;
	
    public SpatialViewPane(Controller controller, TemporalView temporalViewModel, TemporalViewPane temporalViewPane, RepositoryPane repositoryPane) {
  
    	setOrientation(Orientation.HORIZONTAL);
    	setDividerPositions(0.5);
    	
    	getStylesheets().add("view/spatialViewPane/styles/spatialViewPane.css");
    	
    	this.temporalViewModel = temporalViewModel;
    	
    	displayPane = new DisplayPane();
    	
    	HBox labelContainer = new HBox();
		labelContainer.setId("label-container");
		Label label = new Label(Language.translate("no.selected.media"));
		labelContainer.getChildren().add(label);
    	
    	getItems().addAll(labelContainer, displayPane);
    	
    	temporalViewPane.addObserver(this);
    	repositoryPane.getMediaListPane().addObserver(this);
    	
    	this.controller = controller;
    	
    }

    @Override
	public void update(view.common.Observable o, Object obj) {
		
    	if(obj instanceof Media){
    		
    		Media selectedMedia = (Media) obj;
    		
    		if(selectedMedia.getBegin() != null){
    			createPropertyInfoTabPane(controller, selectedMedia);
    		}else{
    			createMediaInfoTabPane(controller, selectedMedia);
    		}
    		
    	}
		
	}
    
	private void createPropertyInfoTabPane(Controller controller, Media media) {
		
		propertyPane = new PropertyPane(controller, media);
    	temporalMediaInfoPane = new TemporalMediaInfoPane(controller, media);
    	
    	propertyTab = new Tab();
    	propertyTab.setText(Language.translate("PROPERTIES"));
    	propertyTab.setClosable(false); 
    	propertyTab.setContent(propertyPane);
    	infoTab = new Tab();
    	infoTab.setText(Language.translate("INFO"));
    	infoTab.setClosable(false); 
    	infoTab.setContent(temporalMediaInfoPane);

    	propertyInfoTabPane = new TabPane();
    	
    	propertyInfoTabPane.getTabs().add(propertyTab);
    	propertyInfoTabPane.getTabs().add(infoTab);
    	
    	getItems().clear();
    	getItems().addAll(propertyInfoTabPane, displayPane);
    	
	}
	
	private void createMediaInfoTabPane(Controller controller, Media media) {
		
		repositoryMediaInfoPane = new RepositoryMediaInfoPane(controller, media);
    	
    	infoTab = new Tab();
    	infoTab.setText(Language.translate("INFO"));
    	infoTab.setClosable(false); 
    	infoTab.setContent(repositoryMediaInfoPane);

    	propertyInfoTabPane = new TabPane();
    	propertyInfoTabPane.getTabs().add(infoTab);
    	
    	getItems().clear();
    	getItems().addAll(propertyInfoTabPane, displayPane);
    	
	}
    
}
