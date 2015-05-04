
package gui.stvePane;

import gui.repositoryPane.RepositoryPane;
import gui.spatialViewPane.SpatialViewPane;
import gui.temporalViewPane.TemporalViewPane;

import java.io.IOException;

import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import br.uff.midiacom.ana.util.exception.XMLException;
import controller.TemporalViewController;

/**
 *
 * @author Douglas
 */
public class StvePane extends Scene {
    
	private TemporalViewController temporalViewController = TemporalViewController.getTemporalViewController();
	
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
    	getStylesheets().add("gui/stvePane/styles/stvePane.css");
    	containerBorderPane.setPrefSize(STVE_WITDH, STVE_HEIGHT);
	   
    	stveMenuBar = new StveMenuBar();
    	repositoryPane = new RepositoryPane();
    	spatialViewPane = new SpatialViewPane();
    	temporalViewPane = new TemporalViewPane();
	   
    	repositorySpatialViewSplitPane = new SplitPane();
    	repositorySpatialViewSplitPane.setOrientation(Orientation.HORIZONTAL);
    	repositorySpatialViewSplitPane.setDividerPositions(0.3);
    	repositorySpatialViewSplitPane.getItems().addAll(repositoryPane, spatialViewPane);
	   
    	containerSplitPane = new SplitPane();
    	containerSplitPane.setOrientation(Orientation.VERTICAL);
    	containerSplitPane.getItems().addAll(repositorySpatialViewSplitPane, temporalViewPane);

    	containerBorderPane.setTop(stveMenuBar);
    	containerBorderPane.setCenter(containerSplitPane);
    	
    	createNewProject();
       
   }
    
    public void createNewProject(){
    	temporalViewController.createTemporalView();
    }
   
}
