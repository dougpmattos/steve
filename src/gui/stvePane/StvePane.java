
package gui.stvePane;

import gui.repositoryPane.RepositoryPane;
import gui.spatialViewPane.SpatialViewPane;
import gui.temporalViewPane.TemporalViewPane;

import java.io.IOException;

import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import br.uff.midiacom.ana.util.exception.XMLException;

/**
 *
 * @author Douglas
 */
public class StvePane extends Scene {
    
	private static final int STVE_HEIGHT = 768;
	private static final int STVE_WITDH = 1366;

    private StveMenuBar stveMenuBar;
    private RepositoryPane repositoryPane;
    private SpatialViewPane spatialViewPane;
    private TemporalViewPane temporalViewPane;
    private SplitPane repositorySpatialViewSplitPane;
    private SplitPane containerSplitPane;
    private static BorderPane containerBorderPane = new BorderPane();
    
    public StvePane() throws XMLException, IOException  {
    	
    	super(containerBorderPane);
    	containerBorderPane.setId("container-border-pane");
    	containerBorderPane.setPrefSize(STVE_WITDH, STVE_HEIGHT);
    	containerBorderPane.getStylesheets().add("gui/stvePane/styles/stvePane.css");
    	getStylesheets().add("gui/stvePane/styles/stvePane.css");
	   
    	stveMenuBar = new StveMenuBar();
    	repositoryPane = new RepositoryPane();
    	spatialViewPane = new SpatialViewPane();
    	temporalViewPane = new TemporalViewPane();
	   
    	repositorySpatialViewSplitPane = new SplitPane();
    	repositorySpatialViewSplitPane.getStylesheets().add("gui/stvePane/styles/stvePane.css");
    	repositorySpatialViewSplitPane.setId("repo-spatial-spli-pane");
    	repositorySpatialViewSplitPane.setId("splitRepoSpatial");
    	repositorySpatialViewSplitPane.setOrientation(Orientation.HORIZONTAL);
    	repositorySpatialViewSplitPane.setDividerPositions(0.6);
    	repositorySpatialViewSplitPane.getItems().addAll(repositoryPane, spatialViewPane);
	   
    	containerSplitPane = new SplitPane();
    	containerSplitPane.getStylesheets().add("gui/stvePane/styles/stvePane.css");
    	containerSplitPane.setId("container-split-pane");
    	containerSplitPane.setId("splitPane");
    	containerSplitPane.setOrientation(Orientation.VERTICAL);
    	containerSplitPane.getItems().addAll(repositorySpatialViewSplitPane, temporalViewPane);

    	containerBorderPane.setId("containerBorderPane");
    	containerBorderPane.setTop(stveMenuBar);
    	containerBorderPane.setCenter(containerSplitPane);
       
   }
   
}
