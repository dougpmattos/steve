package controller;
	
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application{
	
	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage stage) throws Exception {
		
		stage.setScene(new Stve());
		stage.setTitle("STVE-HD");
		stage.setFullScreen(true);
		stage.show();

	}

}
