package gui.repositoryPanel;
    
import gui.common.ObserverButton;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import controller.MediaController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import model.NCLSupport.extendedAna.Media;
import model.repository.MediaList;

/**
 *
 * @author Douglas
 */
public class ButtonPane extends HBox{
    
	private MediaController mediaController = MediaController.getMediaController();
	
    ObserverButton addButton, deleteButton, clearButton;
    FileChooser fileChooser;
    Media media;
    Boolean contains;
    @SuppressWarnings("rawtypes")
	Iterator mediaListIterator;
    String selectedMediaName, listMediaName;
    List <File> fileList;
    
    public ButtonPane(){
        
        setId("button-pane");
        getStylesheets().add("gui/styles/buttonPane.css");
        setSpacing(-20);
        
        addButton = new ObserverButton("add-button", "Add media", 0.5, 0.5);
        deleteButton = new ObserverButton("delete-button", "Delete media", 0.5, 0.5);
        deleteButton.setDisable(true);
        clearButton = new ObserverButton("clear-button", "Clear repository.", 0.5, 0.5);
        clearButton.setDisable(true);

        getChildren().addAll(addButton, deleteButton, clearButton);

        setButtonActions();
        
    }

    private void setButtonActions() {
    	
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                fileChooser = new FileChooser();
                fileChooser.setTitle("Select Media");
                fileList = fileChooser.showOpenMultipleDialog(null);
                
                if(fileList != null){
                	for (File file : fileList) {
                		mediaController.addMedia(file);
                    }
                }                      
            }
        });
        
        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	mediaController.deleteMedia();
            }
        });
        
        clearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	mediaController.clearMediaList();
                deleteButton.setDisable(true);
                clearButton.setDisable(true);
            }
        });
    }
}
