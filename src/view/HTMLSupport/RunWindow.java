package view.HTMLSupport;

import java.io.File;

import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RunWindow extends Stage {
	
	private static final int HEIGHT = 768;
	private static final int WIDTH = 1024;
	
    private Scene scene;

    public RunWindow(File htmlFile) {

        initModality(Modality.APPLICATION_MODAL);

        Browser browser = new Browser(htmlFile);

        scene = new Scene(browser, WIDTH, HEIGHT);
        scene.setFill(Color.BLACK);
        setScene(scene);
        
        
    }
}