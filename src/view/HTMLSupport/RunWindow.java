package view.HTMLSupport;

import java.io.File;

import javafx.scene.Scene;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class RunWindow extends Stage {
	private static final int HEIGHT = 600;
	private static final int WIDTH = 650;
	
    private Scene scene;
        
    private GridPane formGridPane;


    public RunWindow(/*File htmlFile*/) {

        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);

        Browser browser = new Browser();

        scene = new Scene(browser, WIDTH, HEIGHT);
        scene.setFill(Color.TRANSPARENT);
        setScene(scene);

    }
}