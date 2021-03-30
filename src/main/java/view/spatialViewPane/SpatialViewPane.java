
package view.spatialViewPane;

import java.util.Observable;
import java.util.Observer;

import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import model.common.MediaNode;
import model.common.SensoryEffectNode;
import model.common.SpatialTemporalApplication;
import model.repository.RepositoryMediaList;
import model.repository.enums.RepositoryOperator;
import model.temporalView.enums.TemporalViewOperator;
import model.utility.Operation;
import view.common.Language;
import view.repositoryPane.RepositoryPane;
import view.stevePane.SteveMenuBar;
import view.temporalViewPane.TemporalViewPane;
import controller.ApplicationController;


/**
 *
 * @author Douglas
 */
public class SpatialViewPane extends SplitPane implements view.common.Observer, Observer {

	private ApplicationController applicationController;
	
	private DisplayPane displayPane;
	private MediaPropertyPane mediaPropertyPane;
	private EffectPropertyPaneContainer effectPropertyPaneContainer;
	private TemporalMediaInfoPane temporalMediaInfoPane;
	private RepositoryMediaInfoPane repositoryMediaInfoPane;
	private Tab propertyTab;
	private Tab infoTab;
	private TabPane propertyInfoTabPane;
	private HBox labelContainer;
	
    public SpatialViewPane(ApplicationController applicationController, SpatialTemporalApplication temporalViewModel, TemporalViewPane temporalViewPane, RepositoryPane repositoryPane, RepositoryMediaList repositoryMediaList, SteveMenuBar steveMenuBar) {
  
    	setOrientation(Orientation.HORIZONTAL);
    	setDividerPositions(0.5);
    	
    	getStylesheets().add("styles/spatialViewPane/spatialViewPane.css");

    	displayPane = new DisplayPane(temporalViewPane, steveMenuBar, temporalViewModel);
    	
    	labelContainer = new HBox();
		labelContainer.setId("label-container");
		Label label = new Label(Language.translate("no.selected.media"));
		labelContainer.getChildren().add(label);
    	
		getItems().addAll(labelContainer, displayPane);
//    	getItems().addAll(labelContainer);
    	
    	temporalViewPane.addObserver(this);
    	repositoryPane.getRepositoryMediaItemContainerListPane().addObserver(this);
    	repositoryMediaList.addObserver(this);
    	
    	this.applicationController = applicationController;
    	
    }

    @Override
	public void update(view.common.Observable o, Object obj, Object operator) {
	
    	if(operator instanceof RepositoryOperator){
    		
    		RepositoryOperator repositoryOperator = (RepositoryOperator) operator;
    		
    		switch (repositoryOperator) {
    		
	    		case SELECT_REPOSITORY_MEDIA:
	    			
	    			if(obj instanceof MediaNode){
	    				MediaNode selectedMediaNode = (MediaNode) obj;
	    				createMediaInfoTabPane(applicationController, selectedMediaNode);
	    			}
	    			
	    			break;

	    		default:
	    			break;
	    			
    		}
    		
    	}else if(operator instanceof TemporalViewOperator){
    		
    		TemporalViewOperator temporalViewOperator = (TemporalViewOperator) operator;
    		
    		switch (temporalViewOperator) {
    		
				case SELECT_TEMPORAL_MEDIA:
					
					if(obj instanceof MediaNode){
						MediaNode selectedMediaNode = (MediaNode) obj;
						createMediaPropertyInfoTabPane(applicationController, selectedMediaNode);
					}else if(obj instanceof SensoryEffectNode){
						SensoryEffectNode selectedEffect = (SensoryEffectNode) obj;
						createEffectPropertyInfoTabPane(applicationController, selectedEffect);
					}
					
					break;
				
				case CLEAR_SELECTION_TEMPORAL_NODE:
					
					getItems().clear();
			    	getItems().addAll(labelContainer, displayPane);
//			    	getItems().addAll(labelContainer);
			    	
					break;
					
				default:
					break;
					
			}
    		
    	}
	
	}

	private void createEffectPropertyInfoTabPane(ApplicationController applicationController, SensoryEffectNode sensoryEffectNode) {

		effectPropertyPaneContainer = new EffectPropertyPaneContainer(applicationController, sensoryEffectNode);
		temporalMediaInfoPane = new TemporalMediaInfoPane(applicationController, sensoryEffectNode);

		propertyTab = new Tab();
		propertyTab.setText(Language.translate("PROPERTIES"));
		propertyTab.setClosable(false);
		propertyTab.setContent(effectPropertyPaneContainer);
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

	private void createMediaPropertyInfoTabPane(ApplicationController applicationController, MediaNode mediaNode) {
		
		mediaPropertyPane = new MediaPropertyPane(applicationController, mediaNode);
    	temporalMediaInfoPane = new TemporalMediaInfoPane(applicationController, mediaNode);
    	
    	propertyTab = new Tab();
    	propertyTab.setText(Language.translate("PROPERTIES"));
    	propertyTab.setClosable(false); 
    	propertyTab.setContent(mediaPropertyPane);
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
	
	private void createMediaInfoTabPane(ApplicationController applicationController, MediaNode mediaNode) {
		
		repositoryMediaInfoPane = new RepositoryMediaInfoPane(applicationController, mediaNode);

    	infoTab = new Tab();
    	infoTab.setText(Language.translate("INFO"));
    	infoTab.setClosable(false); 
    	infoTab.setContent(repositoryMediaInfoPane);

    	propertyInfoTabPane = new TabPane();
    	propertyInfoTabPane.getTabs().add(infoTab);
    	
    	getItems().clear();
    	getItems().addAll(propertyInfoTabPane, displayPane);
//    	getItems().addAll(propertyInfoTabPane);
    	
	}

	@Override
	public void update(Observable o, Object arg) {
		
		if(o instanceof RepositoryMediaList){
			
			Operation<RepositoryOperator> operation = (Operation<RepositoryOperator>) arg;
			MediaNode mediaNode = (MediaNode) operation.getOperating();

			switch(operation.getOperator()){
			
				case CLEAR_SELECTION_REPOSITORY_MEDIA:
					
					getItems().clear();
			    	getItems().addAll(labelContainer, displayPane);
//			    	getItems().addAll(labelContainer);
			    	
					break;
					
	    		case REMOVE_REPOSITORY_MEDIA:
					
					getItems().clear();
			    	getItems().addAll(labelContainer, displayPane);
//			    	getItems().addAll(labelContainer);
			    	
					break;
		        	
		        default:
		        	
		        	break;
		        	
			}
			
		}
		
	}
	
	public DisplayPane getDisplayPane(){
		return displayPane;
	}

	public void removeNodeOfSpatialView(model.common.Node node) {

		getItems().clear();

		if(!displayPane.getScreen().getChildren().isEmpty()){
			if(node instanceof SensoryEffectNode){
				displayPane.getControlButtonPane().getEffectIconsContainer().getChildren().remove(node.getExecutionObject());
			}else{
				displayPane.getScreen().getChildren().remove(node.getExecutionObject());
			}
			node.setIsShownInPreview(false);
			node.setIsContinuousMediaPlaying(false);
		}

		getItems().addAll(labelContainer, displayPane);

	}
}
