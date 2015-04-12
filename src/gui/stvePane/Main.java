package gui.stvePane;
	
import gui.common.Language;

import java.util.Locale;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application{
	
	private static final String SEPARATOR = "   -   ";

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage stage) throws Exception {
		
		Locale defaultLocale = new Locale("en","US");
		Language.setLocale(defaultLocale);
		
		stage.setScene(new StvePane());
		stage.setTitle(Language.translate("untitled.project") + SEPARATOR + Language.translate("stve"));
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/gui/stvePane/images/logo.png")));
		stage.show();

	}

}
