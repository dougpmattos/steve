package view.stevePane;
	
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.NCLSupport.NCLExportEventHandler;
import model.common.SpatialTemporalView;
import model.repository.RepositoryMediaList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.Controller;


public class Main extends Application{

	final Logger logger = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage stage) throws Exception {
		
		RepositoryMediaList repositoryMediaList = new RepositoryMediaList();
		SpatialTemporalView temporalView = new SpatialTemporalView();
		
		LoadingWindow loadingWindow = new LoadingWindow();
		loadingWindow.show();
		
		FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.3), loadingWindow.getScene().getRoot());
		fadeIn.setFromValue(0.0);
		fadeIn.setToValue(1.0);
		fadeIn.play();
		fadeIn.setOnFinished(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				FadeTransition fadeOut = new FadeTransition(Duration.seconds(1.3), loadingWindow.getScene().getRoot());
				fadeOut.setFromValue(1.0);
				fadeOut.setToValue(0.0);
				fadeOut.setDelay(Duration.seconds(1));
				fadeOut.play();
				fadeOut.setOnFinished(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						
						loadingWindow.close();
						try {
							new Controller(repositoryMediaList, temporalView, stage);
						} catch (Exception ex) {
							logger.error(ex.getMessage());
						}
						
					}
				});
				
			}
		});

	}

}
