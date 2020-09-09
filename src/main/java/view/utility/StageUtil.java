package view.utility;

import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class StageUtil {

    public static void centerStage(Stage stage, double width, double height) {

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - width) / 2);
        stage.setY((screenBounds.getHeight() - height) / 2);

    }

    public static void makeWindowDraggable(BorderPane root, Stage windowStage) {

        final Delta dragDelta = new Delta();
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                // record a delta distance for the drag and drop operation.
                dragDelta.x = windowStage.getX() - mouseEvent.getScreenX();
                dragDelta.y = windowStage.getY() - mouseEvent.getScreenY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                windowStage.setX(mouseEvent.getScreenX() + dragDelta.x);
                windowStage.setY(mouseEvent.getScreenY() + dragDelta.y);
            }
        });
    }

}
