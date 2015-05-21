package gui.repositoryPane;

import java.util.Observable;
import java.util.Observer;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import model.common.Media;
import model.common.Operation;
import model.repository.RepositoryMediaList;
import model.repository.RepositoryOperator;
import controller.Controller;

@SuppressWarnings("unchecked")
public class RepositoryPane extends BorderPane implements Observer {
	
	private ScrollPane scrollPaneTree;
	private MediaListPane mediaListPane;
	private MediaTreePane mediaTreePane;
    private RepositoryButtonPane buttonPane;
		
	public RepositoryPane(Controller controller, RepositoryMediaList repositoryMediaList){

		getStylesheets().add("gui/repositoryPane/styles/repositoryPane.css");
		
		mediaTreePane = new MediaTreePane();
		mediaListPane = new MediaListPane();
        
		scrollPaneTree = new ScrollPane();
	    scrollPaneTree.setContent(mediaTreePane);
	    scrollPaneTree.setFitToHeight(true);
	    scrollPaneTree.setFitToWidth(true);
	    
	    buttonPane = new RepositoryButtonPane(controller, scrollPaneTree, mediaTreePane, mediaListPane, this);
	            
	    setCenter(mediaListPane);
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
		        	
		            break;
		            
		        case REMOVE_REPOSITORY_MEDIA:
		        	
		        	mediaListPane.remove(media);
		        	mediaTreePane.remove(media);
		        	
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
		        	
		        	break;
		        	
		        default:
		        	
		        	break;
		        	
			}
			
		}
		
	}

}
