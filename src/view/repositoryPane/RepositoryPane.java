package view.repositoryPane;

import java.util.Observable;
import java.util.Observer;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import model.common.Media;
import model.repository.RepositoryMediaList;
import model.repository.enums.RepositoryOperator;
import model.utility.Operation;
import view.common.Language;
import view.temporalViewPane.TemporalViewPane;
import controller.Controller;

@SuppressWarnings("unchecked")
public class RepositoryPane extends BorderPane implements Observer {
	
	private ScrollPane scrollPaneTree;
	private RepositoryMediaItemContainerListPane repositoryMediaItemContainerListPane;
	private MediaTreePane mediaTreePane;
    private RepositoryButtonPane buttonPane;
    private TemporalViewPane temporalViewPane;
    private HBox labelContainer;
    private RepositoryMediaList repositoryMediaList;
		
	public RepositoryPane(Controller controller, RepositoryMediaList repositoryMediaList){

		getStylesheets().add("view/repositoryPane/styles/repositoryPane.css");
		
		this.repositoryMediaList = repositoryMediaList;
		
		mediaTreePane = new MediaTreePane();
		repositoryMediaItemContainerListPane = new RepositoryMediaItemContainerListPane();
        
		scrollPaneTree = new ScrollPane();
	    scrollPaneTree.setContent(mediaTreePane);
	    scrollPaneTree.setFitToHeight(true);
	    scrollPaneTree.setFitToWidth(true);
	    
	    buttonPane = new RepositoryButtonPane(controller, scrollPaneTree, mediaTreePane, repositoryMediaItemContainerListPane, this);
	            
	    labelContainer = new HBox();
		labelContainer.setId("media-repo-label-container");
		Label label = new Label(Language.translate("media.repository.is.empty"));
		labelContainer.getChildren().add(label);
		
	    setCenter(labelContainer);
	    setBottom(buttonPane);
	    
	    repositoryMediaList.addObserver(this);
	    
	}

	@Override
	public void update(Observable observable, Object obj) {
	
		if(observable instanceof RepositoryMediaList){
		
			Operation<RepositoryOperator> operation = (Operation<RepositoryOperator>) obj;
			Media media = (Media) operation.getOperating();

			switch(operation.getOperator()){
			
		        case ADD_REPOSITORY_MEDIA:
		        	
		        	repositoryMediaItemContainerListPane.add(media);
		        	mediaTreePane.add(media);
		        	
		        	buttonPane.getDeleteButton().setDisable(false);
		        	buttonPane.getClearButton().setDisable(false);
		        	
		        	if(!getChildren().contains(repositoryMediaItemContainerListPane)){
		        		getChildren().remove(labelContainer);
			        	setCenter(repositoryMediaItemContainerListPane);
		        	}
		        	
		            break;
		            
		        case REMOVE_REPOSITORY_MEDIA:
		        	
		        	repositoryMediaItemContainerListPane.remove(media);
		        	mediaTreePane.remove(media);
		        	
		        	if(repositoryMediaItemContainerListPane.getAllTypes().isEmpty()){
		        		getChildren().remove(repositoryMediaItemContainerListPane);
			        	setCenter(labelContainer);
		        	}
		        	
		            break;
				
		        case CLEAR_SELECTION_REPOSITORY_MEDIA:
		        	
		        	repositoryMediaItemContainerListPane.getAllTypes().clear();
		        	repositoryMediaItemContainerListPane.getImages().clear();
		        	repositoryMediaItemContainerListPane.getVideos().clear();
		        	repositoryMediaItemContainerListPane.getAudios().clear();
		        	repositoryMediaItemContainerListPane.getTexts().clear();
		        	repositoryMediaItemContainerListPane.getApplications().clear();
		        	mediaTreePane.clear();
		        	
		        	buttonPane.getDeleteButton().setDisable(true);
		        	buttonPane.getClearButton().setDisable(true);
		        	
		        	getChildren().remove(repositoryMediaItemContainerListPane);
		        	setCenter(labelContainer);
		        	
		        	break;
		        	
		        default:
		        	
		        	break;
		        	
			}
			
		}
		
	}
	
	public RepositoryMediaItemContainerListPane getRepositoryMediaItemContainerListPane(){
		return repositoryMediaItemContainerListPane;
	}

	public void setTemporalViewPane(TemporalViewPane temporalViewPane) {
		
		this.temporalViewPane = temporalViewPane;
		repositoryMediaItemContainerListPane.setTemporalViewPane(temporalViewPane);
		
	}
	
	public RepositoryMediaList getRepositoryMediaList() {
		return repositoryMediaList;
	}

}
