package gui.repositoryPanel;

import javafx.geometry.Orientation;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import model.repository.MediaList;
import model.repository.MediaTree;
import controller.ButtonPane;

/**
 *
 * @author Douglas
 */
public class Repository extends BorderPane {
    
    public MediaList mediaList;
    public MediaTree mediaTree;
    private ScrollPane scrollPaneList, scrollPaneTree;
    private ButtonPane buttonPane;
    private SplitPane splitPane;

    public Repository() {
        
        mediaTree = new MediaTree();
        mediaList = new MediaList();
        
        buttonPane = new ButtonPane(mediaList,mediaTree);
        
        scrollPaneTree = new ScrollPane();
        scrollPaneTree.setContent(mediaTree);
        scrollPaneTree.setFitToHeight(true);
        scrollPaneTree.setFitToWidth(true);
        scrollPaneList = new ScrollPane();
        scrollPaneList.setContent(mediaList);
        scrollPaneList.setFitToHeight(true);
        scrollPaneList.setFitToWidth(true);
        
        splitPane = new SplitPane();
        splitPane.setOrientation(Orientation.HORIZONTAL);
        splitPane.setDividerPositions(0.1);
        splitPane.getItems().addAll(scrollPaneTree,scrollPaneList);
                
        setTop(buttonPane);
        setCenter(splitPane);

    }

}

                