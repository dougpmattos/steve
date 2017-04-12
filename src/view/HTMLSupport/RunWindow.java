package view.HTMLSupport;

import java.io.File;

import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
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
/*        
        double scaleFactor =
                browser.getWidth() / browser.getHeight() > browser.get
                    ? newHeight / initHeight
                    : newWidth / initWidth;


        Scale scale = new Scale(browser.getScaleX(), browser.getScaleY());
        scale.setPivotX(0);
        scale.setPivotY(0);
        scene.getRoot().getTransforms().setAll(scale);
*/
        setScene(scene);
        
        
    }
}