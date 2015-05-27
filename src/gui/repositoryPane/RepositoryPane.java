package gui.repositoryPane;

import gui.common.Language;

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
import controller.Controller;

@SuppressWarnings("unchecked")
public class RepositoryPane extends BorderPane implements Observer {
	
	private ScrollPane scrollPaneTree;
	private MediaListPane mediaListPane;
	private MediaTreePane mediaTreePane;
    private RepositoryButtonPane buttonPane;
    private HBox labelContainer;
		
	public RepositoryPane(Controller controller, RepositoryMediaList repositoryMediaList){

		getStylesheets().add("gui/repositoryPane/styles/repositoryPane.css");
		
		mediaTreePane = new MediaTreePane();
		mediaListPane = new MediaListPane();
        
		scrollPaneTree = new ScrollPane();
	    scrollPaneTree.setContent(mediaTreePane);
	    scrollPaneTree.setFitToHeight(true);
	    scrollPaneTree.setFitToWidth(true);
	    
	    buttonPane = new RepositoryButtonPane(controller, scrollPaneTree, mediaTreePane, mediaListPane, this);
	            
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
		        	
		        	mediaListPane.add(media);
		        	mediaTreePane.add(media);
		        	
		        	buttonPane.getDeleteButton().setDisable(false);
		        	buttonPane.getClearButton().setDisable(false);
		        	
		        	if(!getChildren().contains(mediaListPane)){
		        		getChildren().remove(labelContainer);
			        	setCenter(mediaListPane);
		        	}
		        	
		            break;
		            
		        case REMOVE_REPOSITORY_MEDIA:
		        	
		        	mediaListPane.remove(media);
		        	mediaTreePane.remove(media);
		        	
		        	if(mediaListPane.getAllTypes().isEmpty()){
		        		getChildren().remove(mediaListPane);
			        	setCenter(labelContainer);
		        	}
		        	
		            break;
				
		        case CLEAR_REPOSITORY_MEDIA_LIST:
		        	
		        	mediaListPane.getAllTypes().clear();
		        	mediaListPane.getImages().clear();
		        	mediaListPane.getVideos().clear();
		        	mediaListPane.getAudios().clear();
		        	mediaListPane.getTexts().clear();
		        	mediaListPane.getOthers().clear();
		        	mediaTreePane.clear();
		        	
		        	buttonPane.getDeleteButton().setDisable(true);
		        	buttonPane.getClearButton().setDisable(true);
		        	
		        	getChildren().remove(mediaListPane);
		        	setCenter(labelContainer);
		        	
		        	break;
		        	
		        default:
		        	
		        	break;
		        	
			}
			
		}
		
	}

}
