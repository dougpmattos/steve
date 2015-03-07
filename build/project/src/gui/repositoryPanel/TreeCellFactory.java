package gui.repositoryPanel;

import javafx.scene.control.Label;
import javafx.scene.control.TreeCell;
import model.NCLSupport.extendedAna.Media;

public class TreeCellFactory extends TreeCell<Object> {
	
	  Media media;
	    Label mediaCell;
	    
	    @Override
	    public void updateItem(Object item, boolean empty) {
	        super.updateItem(item, empty);
	        
	        if (media != null) {
	            mediaCell = new Label(media.getName());
	            setGraphic(mediaCell);
	        }
	    }
	
}
