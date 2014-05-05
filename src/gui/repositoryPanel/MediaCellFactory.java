package gui.repositoryPanel;

import model.nclDocument.extendedAna.Media;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

/**
 *
 * @author Douglas
 */
public class MediaCellFactory extends ListCell<Object> {
    
    Media media;
    Label mediaCell;
    
    @Override
    public void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty);
        media = (Media) item;
        if (media != null) {
            mediaCell = new Label(media.getName(), media.getMediaIcon());
            setGraphic(mediaCell);
        }
    }
}
