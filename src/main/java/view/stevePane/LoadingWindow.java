package view.stevePane;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
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
    private StackPane containerBorderPane;

    public LoadingWindow() {

        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.TRANSPARENT);

        setLayout();

        scene = new Scene(containerBorderPane, 800, 450);
        scene.setFill(Color.TRANSPARENT);
        setScene(scene);

    }

    public void setLayout(){

        containerBorderPane = new StackPane();

        containerBorderPane.setId("container-border-pane");

        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/images/loadingWindow/loadingScreen.png")));
        icon.setPreserveRatio(true);
        icon.setSmooth(true);
        icon.fitWidthProperty().bind(containerBorderPane.widthProperty());
        icon.fitHeightProperty().bind(containerBorderPane.heightProperty());

        ProgressBar progressBar = new ProgressBar();

        containerBorderPane.getChildren().addAll(icon, progressBar);
        containerBorderPane.setAlignment(progressBar, Pos.CENTER_LEFT);
        StackPane.setMargin(progressBar, new Insets(120,0,0,48));
        containerBorderPane.getStylesheets().add("styles/stevePane/loadingWindow.css");
        
    }
 
}
