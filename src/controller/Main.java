package controller;
	
import gui.common.Language;

import java.util.Locale;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application{
	
	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage stage) throws Exception {
		
		Locale defaultLocale = new Locale("en","US");
		Language.setLocale(defaultLocale);
		
		stage.setScene(new Stve());
		stage.setTitle(Language.translate("stve"));
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/gui/images/logo.png")));
		stage.show();

	}

}
