package model.repository;

import gui.repositoryPanel.MediaListPanel;
import javafx.geometry.Orientation;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import controller.ButtonPane;

public class RepositoryPanel extends BorderPane {
	
	private ScrollPane scrollPaneList, scrollPaneTree;
	private SplitPane splitPane;
		
	public RepositoryPanel(MediaListPanel mediaListPanel, MediaTree mediaTree, ButtonPane buttonPane){
		
		scrollPaneTree = new ScrollPane();
	    scrollPaneTree.setContent(mediaTree);
	    scrollPaneTree.setFitToHeight(true);
	    scrollPaneTree.setFitToWidth(true);
	    scrollPaneList = new ScrollPane();
	    scrollPaneList.setContent(mediaListPanel);
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
