package view.common.dialogs;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import view.utility.StageUtil;

public class ReturnMessage extends Stage {

    private static final int HEIGHT = 50;

    private Scene scene;
    private Label msgLabel;
    private BorderPane containerBorderPane;

    public ReturnMessage(String msg, int width) {

        setResizable(false);
        initModality(Modality.NONE);
        initStyle(StageStyle.TRANSPARENT);

        msgLabel = new Label(msg);
        msgLabel.setId("msg-label");
        msgLabel.setWrapText(true);

        setLayout();

        scene = new Scene(containerBorderPane, width, HEIGHT);
        scene.setFill(Color.TRANSPARENT);
        setScene(scene);
        StageUtil.centerStage(this, width, HEIGHT);

    }

    public void setLayout(){

        containerBorderPane = new BorderPane();
        containerBorderPane.setId("container-border-pane");
        containerBorderPane.getStylesheets().add("styles/common/returnMessage.css");

        containerBorderPane.setCenter(msgLabel);

    }

}
