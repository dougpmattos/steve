package view.stevePane;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Douglas
 */
public class LoadingWindow extends Stage {

    private Scene scene;
    private BorderPane containerBorderPane;

    public LoadingWindow() {

        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.TRANSPARENT);

        setLayout();

        scene = new Scene(containerBorderPane, 500, 300);
        scene.setFill(Color.TRANSPARENT);
        setScene(scene);

    }

    public void setLayout(){
    	
    	containerBorderPane = new BorderPane();
        containerBorderPane.setId("container-border-pane");
        
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/view/stevePane/images/compoundLogo.png")));
		icon.setPreserveRatio(true);
        icon.setSmooth(true);
        
        containerBorderPane.setCenter(icon);
        containerBorderPane.getStylesheets().add("view/stevePane/styles/loadingWindow.css");
        
    }
 
}
