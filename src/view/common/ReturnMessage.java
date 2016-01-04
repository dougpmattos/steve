package view.common;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Douglas
 */
public class ReturnMessage extends Stage {
 
    private static final int HEIGHT = 50;
    
    private Scene scene;
    private Label msgLabel;
    private BorderPane containerBorderPane;

    public ReturnMessage(String msg, int width) {

        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.TRANSPARENT);
        
        msgLabel = new Label(msg);
        msgLabel.setId("msg-label");
        msgLabel.setWrapText(true);
       
        setLayout();

        scene = new Scene(containerBorderPane, width, HEIGHT);
        scene.setFill(Color.TRANSPARENT);
        setScene(scene);

    }

    public void setLayout(){
    	
    	containerBorderPane = new BorderPane();
        containerBorderPane.setId("container-border-pane");
        containerBorderPane.getStylesheets().add("view/common/styles/returnMessage.css"); 

        containerBorderPane.setCenter(msgLabel);

    }
 
}
