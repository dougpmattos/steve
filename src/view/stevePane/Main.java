package view.stevePane;
	
import javafx.application.Application;
import javafx.stage.Stage;
import model.repository.RepositoryMediaList;
import model.temporalView.TemporalView;
import controller.Controller;


public class Main extends Application{

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage stage) throws Exception {
		
		RepositoryMediaList repositoryMediaList = new RepositoryMediaList();
		TemporalView temporalView = new TemporalView();
		
		new Controller(repositoryMediaList, temporalView, stage);

	}

}
