package controller;
    
import gui.repositoryPanel.MessageDialog;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import model.NCLSupport.extendedAna.Media;
import model.repository.MediaList;
import model.repository.MediaTree;

/**
 *
 * @author Douglas
 */
public class ButtonPane extends HBox{
    
    Button addButton, deleteButton, clearButton;
    FileChooser fileChooser;
    Media media;
    Boolean contains;
    @SuppressWarnings("rawtypes")
	Iterator mediaListIterator;
    String selectedMediaName, listMediaName;
    List <File> fileList;
    
    public ButtonPane(MediaList mediaList, MediaTree mediaTree){
        
        setId("button-pane");
        getStylesheets().add("gui/styles/repositoryButtonPane.css");
        setSpacing(-20);
        
        addButton = new Button();
        addButton.setId("add-button");
        addButton.setTooltip(new Tooltip("Add media"));
        addButton.setScaleX(0.5);
        addButton.setScaleY(0.5);
        deleteButton = new Button();
        deleteButton.setDisable(true);
        deleteButton.setId("delete-button");
        deleteButton.setTooltip(new Tooltip("Delete media"));
        deleteButton.setScaleX(0.5);
        deleteButton.setScaleY(0.5);
        clearButton = new Button();
        clearButton.setDisable(true);
        clearButton.setId("clear-button");
        clearButton.setTooltip(new Tooltip("Clear repository."));
        clearButton.setScaleX(0.5);
        clearButton.setScaleY(0.5);

        getChildren().addAll(addButton, deleteButton, clearButton);

        setButtonActions(mediaList, mediaTree);
        
    }

    private void setButtonActions(final MediaList mediaList, final MediaTree mediaTree) {
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                fileChooser = new FileChooser();
                fileChooser.setTitle("Select Media");
                fileList = fileChooser.showOpenMultipleDialog(null);
                
                if(fileList != null){
                	for (File file : fileList) {
                        media = new Media(file);
                        selectedMediaName = media.getName();
                        contains = false;
                        mediaListIterator = mediaList.getAllTypesList().iterator();
                        while(mediaListIterator.hasNext() && contains==false) {
                            listMediaName = ((Media) mediaListIterator.next()).getName();
                            if(selectedMediaName.equalsIgnoreCase(listMediaName)){
                                new MessageDialog("Media's already added.", MessageDialog.ICON_INFO).showAndWait();
                                contains = true;
                            }
                        }
                        if(!contains) {
                            mediaList.add(media);
                            mediaTree.add(media);
                        }
                        if(deleteButton.isDisabled()){ //testar se esta condição vai ser usada mais tarde.
                            deleteButton.setDisable(false);
                            clearButton.setDisable(false);
                        }
                                    
     
                    }
                }
                
//                arvore.getTree().setSelectionRow(0);
//
//                }
//
//                lista.getList().setVisibleRowCount(-1);
//                lista.getList().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//                lista.getList().setLayoutOrientation(JList.HORIZONTAL_WRAP);
//
//                reposit.revalidate();
//                reposit.repaint();
                                       
      
            }
        });
        
        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
//            	mediaList.delete(mediaList.getSelectionModel().getSelectedItem());
//            	mediaTree.delete(mediaList.getSelectionModel().getSelectedItem());
            }
        });
        
        clearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                mediaList.clear();
                mediaTree.clear();
                deleteButton.setDisable(true);
                clearButton.setDisable(true);
            }
        });
    }
}
