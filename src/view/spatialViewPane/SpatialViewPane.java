
package view.spatialViewPane;

import java.util.Observable;
import java.util.Observer;

import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import model.common.Media;
import model.common.SpatialTemporalView;
import model.repository.RepositoryMediaList;
import model.repository.enums.RepositoryOperator;
import model.temporalView.enums.TemporalViewOperator;
import model.utility.Operation;
import view.common.Language;
import view.repositoryPane.RepositoryPane;
import view.temporalViewPane.TemporalViewPane;
import controller.Controller;


/**
 *
 * @author Douglas
 */
public class SpatialViewPane extends SplitPane implements view.common.Observer, Observer {

	private Controller controller;
	
	private SpatialTemporalView temporalViewModel;
	
	private DisplayPane displayPane;
	private PropertyPane propertyPane;
	private TemporalMediaInfoPane temporalMediaInfoPane;
	private RepositoryMediaInfoPane repositoryMediaInfoPane;
	private Tab propertyTab;
	private Tab infoTab;
	private TabPane propertyInfoTabPane;
	private HBox labelContainer;
	
    public SpatialViewPane(Controller controller, SpatialTemporalView temporalViewModel, TemporalViewPane temporalViewPane, RepositoryPane repositoryPane, RepositoryMediaList repositoryMediaList) {
  
    	setOrientation(Orientation.HORIZONTAL);
    	setDividerPositions(0.5);
    	
    	getStylesheets().add("view/spatialViewPane/styles/spatialViewPane.css");
    	
    	this.temporalViewModel = temporalViewModel;
    	
    	displayPane = new DisplayPane(temporalViewPane);
    	
    	labelContainer = new HBox();
		labelContainer.setId("label-container");
		Label label = new Label(Language.translate("no.selected.media"));
		labelContainer.getChildren().add(label);
    	
    	//getItems().addAll(labelContainer, displayPane);
    	getItems().addAll(labelContainer);
    	
    	temporalViewPane.addObserver(this);
    	repositoryPane.getRepositoryMediaItemContainerListPane().addObserver(this);
    	repositoryMediaList.addObserver(this);
    	
    	this.controller = controller;
    	
    }

    @Override
	public void update(view.common.Observable o, Object obj, Object operator) {
	
    	if(operator instanceof RepositoryOperator){
    		
    		RepositoryOperator repositoryOperator = (RepositoryOperator) operator;
    		
    		switch (repositoryOperator) {
    		
	    		case SELECT_REPOSITORY_MEDIA:
	    			
	    			if(obj instanceof Media){
	    				Media selectedMedia = (Media) obj;
	    				createMediaInfoTabPane(controller, selectedMedia);
	    			}
	    			
	    			break;

	    		default:
	    			break;
	    			
    		}
    		
    	}else if(operator instanceof TemporalViewOperator){
    		
    		TemporalViewOperator temporalViewOperator = (TemporalViewOperator) operator;
    		
    		switch (temporalViewOperator) {
    		
				case SELECT_TEMPORAL_MEDIA:
					
					if(obj instanceof Media){
						Media selectedMedia = (Media) obj;
						createPropertyInfoTabPane(controller, selectedMedia);
					}
					
					break;
				
				case CLEAR_SELECTION_TEMPORAL_MEDIA:
					
					getItems().clear();
//			    	getItems().addAll(labelContainer, displayPane);
			    	getItems().addAll(labelContainer);
			    	
					break;
					
				default:
					break;
					
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
    	//getItems().addAll(propertyInfoTabPane, displayPane);
    	getItems().addAll(propertyInfoTabPane);
    	
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
//    	getItems().addAll(propertyInfoTabPane, displayPane);
    	getItems().addAll(propertyInfoTabPane);
    	
	}

	@Override
	public void update(Observable o, Object arg) {
		
		if(o instanceof RepositoryMediaList){
			
			Operation<RepositoryOperator> operation = (Operation<RepositoryOperator>) arg;
			Media media = (Media) operation.getOperating();

			switch(operation.getOperator()){
			
				case CLEAR_SELECTION_REPOSITORY_MEDIA:
					
					getItems().clear();
//			    	getItems().addAll(labelContainer, displayPane);
			    	getItems().addAll(labelContainer);
			    	
					break;
					
	    		case REMOVE_REPOSITORY_MEDIA:
					
					getItems().clear();
//			    	getItems().addAll(labelContainer, displayPane);
			    	getItems().addAll(labelContainer);
			    	
					break;
		        	
		        default:
		        	
		        	break;
		        	
			}
			
		}
		
	}
	
	public DisplayPane getDisplayPane(){
		return displayPane;
	}
    
}
