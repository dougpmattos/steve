package gui.repositoryPane;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;

public class RepositoryPane extends BorderPane {
	
	private ScrollPane scrollPaneTree;
	private MediaListPane mediaListPane;
	private MediaTreePane mediaTreePane;
    private RepositoryButtonPane buttonPane;
		
	public RepositoryPane(){

		getStylesheets().add("gui/repositoryPane/styles/repositoryPane.css");
		
		mediaListPane = new MediaListPane();
        mediaTreePane = new MediaTreePane(mediaListPane);
        
		scrollPaneTree = new ScrollPane();
	    scrollPaneTree.setContent(mediaTreePane);
	    scrollPaneTree.setFitToHeight(true);
	    scrollPaneTree.setFitToWidth(true);
	    
	    buttonPane = new RepositoryButtonPane(scrollPaneTree, mediaListPane, this);
	            
	    setCenter(mediaListPane);
	    setBottom(buttonPane);
	    
	}

}
