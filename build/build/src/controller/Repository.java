package controller;

import gui.repositoryPanel.MediaListPanel;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import model.repository.MediaList;
import model.repository.MediaTree;
import model.repository.RepositoryPanel;

/**
 *
 * @author Douglas
 */
public class Repository {
    
    public MediaListPanel mediaListPanel;
    public MediaList mediaList;
    public MediaTree mediaTree;
    private ButtonPane buttonPane;
    private RepositoryPanel repositoryPanel;

    public Repository() {
        
        mediaTree = new MediaTree();
        mediaList = new MediaList();
        mediaListPanel = new MediaListPanel(mediaList);
        buttonPane = new ButtonPane(mediaList, mediaTree);
        
        repositoryPanel = new RepositoryPanel(mediaListPanel, mediaTree, buttonPane);

    }

	public RepositoryPanel getRepositoryPanel() {
		return repositoryPanel;
	}

}

                