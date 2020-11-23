
package view.stevePane;

import java.io.IOException;
import java.util.Locale;

import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.common.SpatialTemporalApplication;
import model.repository.RepositoryMediaList;
import view.common.Language;
import view.repositoryPane.RepositoryPane;
import view.spatialViewPane.SpatialViewPane;
import view.temporalViewPane.TemporalViewPane;
import br.uff.midiacom.ana.util.exception.XMLException;
import controller.ApplicationController;

/**
 *
 * @author Douglas
 */
public class SteveScene extends Scene{
	
	private static final int STEVE_HEIGHT = 768;
	private static final int STEVE_WITDH = 1366;
	private static final String SEPARATOR = "   -   ";

	private RepositoryMediaList repositoryMediaList;
	private SpatialTemporalApplication spatialTemporalApplication;
	
	private ApplicationController applicationController;
	
    private SteveMenuBar steveMenuBar;
    private RepositoryPane repositoryPane;
    private SpatialViewPane spatialViewPane;
	private TemporalViewPane temporalViewPane;
    private SplitPane repositorySpatialViewSplitPane;
    private SplitPane containerSplitPane;
    private Locale defaultLocale;
    private Boolean isMetaDown = false;
    
    private BorderPane containerBorderPane = new BorderPane();
    
    public SteveScene(ApplicationController applicationController, RepositoryMediaList repositoryMediaList, SpatialTemporalApplication spatialTemporalApplication) throws XMLException, IOException  {
    	
    	super(new BorderPane());
    	setRoot(containerBorderPane);
    	getStylesheets().add("styles/stevePane/stevePane.css");
    	containerBorderPane.setPrefSize(STEVE_WITDH, STEVE_HEIGHT);
    	
    	this.applicationController = applicationController;
    	this.repositoryMediaList = repositoryMediaList;
		this.spatialTemporalApplication = spatialTemporalApplication;

   }

	public void createView(Stage stage) {
		
//		defaultLocale = new Locale("pt","BR");
		defaultLocale = new Locale("en","US");
		Language.setLocale(defaultLocale);
		
    	repositoryPane = new RepositoryPane(applicationController, repositoryMediaList);
    	temporalViewPane = new TemporalViewPane(applicationController, spatialTemporalApplication, repositoryPane, this, repositoryMediaList);
    	spatialViewPane = new SpatialViewPane(applicationController, spatialTemporalApplication, temporalViewPane, repositoryPane, repositoryMediaList, steveMenuBar);
    	steveMenuBar = new SteveMenuBar(applicationController, spatialTemporalApplication, repositoryMediaList, temporalViewPane, stage);
	   
    	repositoryPane.setTemporalViewPane(temporalViewPane);
    	
    	repositorySpatialViewSplitPane = new SplitPane();
    	repositorySpatialViewSplitPane.setOrientation(Orientation.HORIZONTAL);
    	repositorySpatialViewSplitPane.setDividerPositions(0.3); //XXX com o displayPane, esta é a divisão
//    	repositorySpatialViewSplitPane.setDividerPositions(0.5);
    	repositorySpatialViewSplitPane.getItems().addAll(repositoryPane, spatialViewPane);
	   
    	containerSplitPane = new SplitPane();
    	containerSplitPane.setOrientation(Orientation.VERTICAL);
    	containerSplitPane.setDividerPositions(0.44); 
    	containerSplitPane.getItems().addAll(repositorySpatialViewSplitPane, temporalViewPane);

    	containerBorderPane.setTop(steveMenuBar);
    	containerBorderPane.setCenter(containerSplitPane);
		
    	setEventHandlers();
    	
		stage.setScene(this);
		stage.setTitle(Language.translate("untitled.project") + SEPARATOR + Language.translate("steve"));
		stage.show();
		
	}
	
	public void setEventHandlers(){
		
		containerBorderPane.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent keyEvent) {
				
				if(keyEvent.getCode() == KeyCode.COMMAND || keyEvent.getCode() == KeyCode.SHIFT){
					isMetaDown = true;
				}
				
			}
			
		});
		
		containerBorderPane.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent keyEvent) {
				
				if(keyEvent.getCode() == KeyCode.COMMAND || keyEvent.getCode() == KeyCode.SHIFT){
					isMetaDown = false;
				}

			}
			
		});
		
	}
	
	public Boolean isMetaDown(){
		
		return isMetaDown;
		
	}

	public TemporalViewPane getTemporalViewPane() {
		return temporalViewPane;
	}

	public SpatialViewPane getSpatialViewPane(){
		return spatialViewPane;
	}


}
