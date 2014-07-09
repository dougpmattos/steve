package gui.repositoryPanel;

import java.util.Observable;
import java.util.Observer;

import javafx.geometry.Insets;
import javafx.scene.layout.FlowPane;
import model.repository.ListUpdateOperation;
import model.repository.MediaList;

/**
 *
 * @author Douglas
 */
public class MediaListPanel extends FlowPane implements Observer {
    
	private final int ADD = 1;
	private final int REMOVE = 2;
	private final int CLEAR = 3;
	
	public MediaListPanel(MediaList mediaList){
		
		setPadding(new Insets(5, 0, 5, 0));
	    setVgap(4);
	    setHgap(4);
	    setPrefWrapLength(170);
	    //setStyle("-fx-background-color: DAE6F3;"); 
	    //getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	    
	    mediaList.addObserver(this);
		
	}

	@Override
	public void update(Observable observable, Object obj) {
		
		if (obj instanceof ListUpdateOperation) {
			
			ListUpdateOperation listUpdateOperation = (ListUpdateOperation) obj;
			
			switch(listUpdateOperation.getOperationType()){
		        case ADD:
		            getChildren().add(listUpdateOperation.getMedia().getMediaIcon());
		            break;
		            
		        case REMOVE:
		        	getChildren().remove(listUpdateOperation.getMedia().getMediaIcon());
		            break;
				
		        case CLEAR:
		        	getChildren().clear();
		        	break;
			}
	            
		}
		
	}
	
}
