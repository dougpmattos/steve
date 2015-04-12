package gui.repositoryPane;

import javafx.geometry.Orientation;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;

public class RepositoryPane extends BorderPane {
	
	private ScrollPane scrollPaneTree;
	private SplitPane splitPane;
	private MediaListPane mediaListPane;
	private MediaTreePane mediaTreePane;
    private RepositoryButtonPane buttonPane;
		
	public RepositoryPane(){
		
		setId("repository-pane");
		getStylesheets().add("gui/repositoryPane/styles/repositoryPane.css");
		
		mediaListPane = new MediaListPane();
        mediaTreePane = new MediaTreePane(mediaListPane);
        buttonPane = new RepositoryButtonPane();
        
		scrollPaneTree = new ScrollPane();
	    scrollPaneTree.setContent(mediaTreePane);
	    scrollPaneTree.setFitToHeight(true);
	    scrollPaneTree.setFitToWidth(true);
	    
	    splitPane = new SplitPane();
	    splitPane.setOrientation(Orientation.HORIZONTAL);
	    splitPane.setDividerPositions(0.3);
	    splitPane.getItems().addAll(scrollPaneTree,mediaListPane);
	            
	    setCenter(splitPane);
	    setBottom(buttonPane);
	    
	}

}
