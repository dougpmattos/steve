
package view.stevePane;

import java.io.IOException;
import java.util.Locale;

import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.common.SpatialTemporalView;
import model.repository.RepositoryMediaList;
import view.common.Language;
import view.repositoryPane.RepositoryPane;
import view.spatialViewPane.SpatialViewPane;
import view.temporalViewPane.TemporalViewPane;
import br.uff.midiacom.ana.util.exception.XMLException;
import controller.Controller;

/**
 *
 * @author Douglas
 */
public class StevePane extends Scene{
	
	private static final int STEVE_HEIGHT = 768;
	private static final int STEVE_WITDH = 1366;
	private static final String SEPARATOR = "   -   ";

	private RepositoryMediaList repositoryMediaList;
	private SpatialTemporalView temporalView;
	
	private Controller controller;
	
    private SteveMenuBar stveMenuBar;
    private RepositoryPane repositoryPane;
    private SpatialViewPane spatialViewPane;
    private TemporalViewPane temporalViewPane;
    private SplitPane repositorySpatialViewSplitPane;
    private SplitPane containerSplitPane;
    private Locale defaultLocale;
    private Boolean isMetaDown = false;
    
    private static BorderPane containerBorderPane = new BorderPane();
    
    public StevePane(Controller controller, RepositoryMediaList repositoryMediaList, SpatialTemporalView temporalView) throws XMLException, IOException  {
    	
    	super(containerBorderPane);
    	getStylesheets().add("view/stevePane/styles/stevePane.css");
    	containerBorderPane.setPrefSize(STEVE_WITDH, STEVE_HEIGHT);
    	
    	this.controller = controller;
    	this.repositoryMediaList = repositoryMediaList;
		this.temporalView = temporalView;

   }

	public void createView(Stage stage) {
		
		defaultLocale = new Locale("en","US");
		Language.setLocale(defaultLocale);
		
    	repositoryPane = new RepositoryPane(controller, repositoryMediaList);
    	temporalViewPane = new TemporalViewPane(controller, temporalView, repositoryPane, this, repositoryMediaList);
    	spatialViewPane = new SpatialViewPane(controller, temporalView, temporalViewPane, repositoryPane, repositoryMediaList);
    	stveMenuBar = new SteveMenuBar(controller, temporalView, repositoryMediaList, temporalViewPane);
	   
    	repositoryPane.setTemporalViewPane(temporalViewPane);
    	
    	repositorySpatialViewSplitPane = new SplitPane();
    	repositorySpatialViewSplitPane.setOrientation(Orientation.HORIZONTAL);
    	repositorySpatialViewSplitPane.setDividerPositions(0.3);
    	repositorySpatialViewSplitPane.getItems().addAll(repositoryPane, spatialViewPane);
	   
    	containerSplitPane = new SplitPane();
    	containerSplitPane.setOrientation(Orientation.VERTICAL);
    	containerSplitPane.setDividerPositions(0.44);
    	containerSplitPane.getItems().addAll(repositorySpatialViewSplitPane, temporalViewPane);

    	containerBorderPane.setTop(stveMenuBar);
    	containerBorderPane.setCenter(containerSplitPane);
		
    	setEventHandlers();
    	
		stage.setScene(this);
		stage.setTitle(Language.translate("untitled.project") + SEPARATOR + Language.translate("steve"));
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/view/stevePane/images/logo.png")));
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
	
	public SpatialViewPane getSpatialViewPane(){
		return spatialViewPane;
	}

}
