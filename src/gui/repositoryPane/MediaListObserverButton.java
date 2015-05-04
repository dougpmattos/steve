
package gui.repositoryPane;

import gui.common.Language;

import java.util.Observable;
import java.util.Observer;

import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import controller.RepositoryController;

/**
 *
 * @author Douglas
 */
public class MediaListObserverButton extends Button implements Observer	{
	
	private RepositoryController mediaController = RepositoryController.getMediaController();
	
	private String id;
	
	public MediaListObserverButton(String id, String toolTip) {
		
		setId(id);
        setTooltip(new Tooltip(Language.translate(toolTip))); 
        
        this.id = id;
        
        mediaController.getMediaList().addObserver(this);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		
		if(this.id.contains("delete") || this.id.contains("clear")){
			if(mediaController.getMediaList().isEmpty()){
				this.setDisable(true);
	            this.setDisable(true);
			} else{
				this.setDisable(false);
	            this.setDisable(false);
			}
		}
		
	}
    
}
