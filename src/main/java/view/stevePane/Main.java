package view.stevePane;

import controller.ApplicationController;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application{

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage stage) throws Exception {

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
							ApplicationController.getInstance().createView(stage);
							ApplicationController.getInstance().createMainTemporalView();
						} catch (Exception ex) {
							System.out.println(ex.getMessage());
						}

					}
				});

			}
		});

	}

}
