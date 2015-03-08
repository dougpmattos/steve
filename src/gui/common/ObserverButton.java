
package gui.common;

import java.util.Observable;
import java.util.Observer;

import controller.repository.MediaController;
import model.repository.MediaList;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;

/**
 *
 * @author Douglas
 */
public class ObserverButton extends Button implements Observer	{
	
	private MediaController mediaController = MediaController.getMediaController();
	
	private String id;
	
	public ObserverButton(String id, String toolTip, double scaleX, double scaleY) {
		
		setId(id);
        setTooltip(new Tooltip(toolTip));
        setScaleX(scaleX);
        setScaleY(scaleY); 
        
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
