
package gui.stvePane;

import gui.repositoryPane.RepositoryPane;
import gui.spatialViewPane.SpatialViewPane;
import gui.temporalViewPane.TemporalViewPane;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import model.common.Operation;
import model.common.Operator;
import model.temporalView.TemporalChain;
import br.uff.midiacom.ana.util.exception.XMLException;
import controller.TemporalViewController;

/**
 *
 * @author Douglas
 */
public class StvePane extends Scene implements Observer{
    
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
    	containerSplitPane.setDividerPositions(0.4);
    	containerSplitPane.getItems().addAll(repositorySpatialViewSplitPane, temporalViewPane);

    	containerBorderPane.setTop(stveMenuBar);
    	containerBorderPane.setCenter(containerSplitPane);
    	
    	temporalViewController.createTemporalView();
    	
    	temporalViewController.getTemporalView().addObserver(this);
    	
    	temporalViewController.addTemporalChain(new TemporalChain());
   }

	@Override
	public void update(Observable o, Object arg) {
		
		Operation operation = (Operation) arg;
		TemporalChain temporalChain = (TemporalChain) operation.getOperating();
		
		if(operation.getOperator().equals(Operator.ADD)){
			temporalViewPane.createTemporalChain(temporalChain.getId());
		}
		
	}

   
}
