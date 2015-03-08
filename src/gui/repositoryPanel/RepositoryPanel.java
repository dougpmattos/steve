package gui.repositoryPanel;

import javafx.geometry.Orientation;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;

public class RepositoryPanel extends BorderPane {
	
	private ScrollPane scrollPaneList, scrollPaneTree;
	private SplitPane splitPane;
	private MediaListPane mediaListPane;
	private MediaTreePane mediaTreePane;
    private ButtonPane buttonPane;
		
	public RepositoryPanel(){
		
		mediaListPane = new MediaListPane();
        mediaTreePane = new MediaTreePane(mediaListPane);
        buttonPane = new ButtonPane();
        
		scrollPaneTree = new ScrollPane();
	    scrollPaneTree.setContent(mediaTreePane);
	    scrollPaneTree.setFitToHeight(true);
	    scrollPaneTree.setFitToWidth(true);
	    scrollPaneList = new ScrollPane();
	    scrollPaneList.setContent(mediaListPane);
	    scrollPaneList.setFitToHeight(true);
	    scrollPaneList.setFitToWidth(true);
	    
	    splitPane = new SplitPane();
	    splitPane.setOrientation(Orientation.HORIZONTAL);
	    splitPane.setDividerPositions(0.2);
	    splitPane.getItems().addAll(scrollPaneTree,scrollPaneList);
	            
	    setCenter(splitPane);
	    setBottom(buttonPane);
	    
	}

}
