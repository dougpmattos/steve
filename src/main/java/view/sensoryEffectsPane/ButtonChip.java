package view.sensoryEffectsPane;

import controller.ApplicationController;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import model.common.enums.SensoryEffectType;
import view.temporalViewPane.TemporalChainPane;
import view.temporalViewPane.TemporalViewPane;
import view.temporalViewPane.TimeLineXYChartData;

import java.util.ArrayList;

public class ButtonChip extends Button {

    private static final DataFormat dataFormat = new DataFormat("String");

    public ButtonChip(SensoryEffectType sensoryEffectType){

        setOnDragDetected(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {

                Dragboard dragBoard = startDragAndDrop(TransferMode.COPY);
                ClipboardContent content = new ClipboardContent();

                SnapshotParameters snapshotParameters = new SnapshotParameters();
                snapshotParameters.setFill(Color.TRANSPARENT);

                dragBoard.setDragView(snapshot(snapshotParameters, null));
                dragBoard.setDragViewOffsetX(99);

                content.put(dataFormat, sensoryEffectType);

                dragBoard.setContent(content);

                mouseEvent.consume();

            }

        });

        setOnMousePressed(mouseEvent -> {

            ApplicationController.getInstance().getSteveScene().setCursor(Cursor.CLOSED_HAND);
            mouseEvent.consume();

            TemporalViewPane temporalViewPane = ApplicationController.getInstance().getSteveScene().getTemporalViewPane();
            temporalViewPane.clearSelectedNodeList();

            for(Tab temporalTab : temporalViewPane.getTemporalChainTabPane().getTabs()){

                TemporalChainPane temporalChainPane = (TemporalChainPane) temporalTab.getContent();
                temporalChainPane.getParentTab().setStyle(null);

                for(ArrayList<TimeLineXYChartData> timeLineXYChartDataList : temporalChainPane.getTimeLineXYChartDataLineList()){
                    for(TimeLineXYChartData timeLineXYChartData : timeLineXYChartDataList){
                        boolean styleRemoved = false;
                        if(timeLineXYChartData.getContainerNode().getStylesheets().remove("styles/temporalViewPane/mousePressedSecondaryTemporalMediaNode.css")){
                            styleRemoved = true;
                        }
                        if(timeLineXYChartData.getContainerNode().getStylesheets().remove("styles/temporalViewPane/mousePressedTemporalMediaNode.css")){
                            styleRemoved = true;
                        }
                        if(styleRemoved){
                            timeLineXYChartData.getMediaImageClip().setHeight(timeLineXYChartData.getMediaImageClip().getHeight()+5);
                        }
                    }
                }
            }

        });

        setOnMouseReleased(mouseEvent -> {
            ApplicationController.getInstance().getSteveScene().setCursor(Cursor.OPEN_HAND);
            mouseEvent.consume();
        });

        setOnMouseEntered(mouseEvent -> {
            ApplicationController.getInstance().getSteveScene().setCursor(Cursor.OPEN_HAND);
            mouseEvent.consume();
        });

        setOnMouseExited(mouseEvent -> {

            ApplicationController.getInstance().getSteveScene().setCursor(Cursor.DEFAULT);
            mouseEvent.consume();

        });

        setOnMouseDragged(mouseEvent -> {

            ApplicationController.getInstance().getSteveScene().setCursor(Cursor.CLOSED_HAND);
            mouseEvent.consume();

        });

    }

}
