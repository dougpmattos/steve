package view.spatialViewPane;

import controller.ApplicationController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import model.common.SensoryEffectNode;
import model.spatialView.sensoryEffect.EffectPositionProperty;
import model.spatialView.sensoryEffect.enums.XPositionType;
import model.spatialView.sensoryEffect.enums.YPositionType;
import model.spatialView.sensoryEffect.enums.ZPositionType;
import view.common.Language;

public class EffectPositionPane extends SensoryEffectPropertyPane {

    private ChoiceBox<XPositionType> xPositionChoiceBox;
    private ChoiceBox<YPositionType> yPositionChoiceBox;
    private ChoiceBox<ZPositionType> zPositionChoiceBox;

    private ApplicationController applicationController;
    private SensoryEffectNode sensoryEffectNode;

    public EffectPositionPane(ApplicationController applicationController, SensoryEffectNode sensoryEffectNode){

        setId("sensory-effect-vbox");

        this.applicationController = applicationController;
        this.sensoryEffectNode = sensoryEffectNode;

        Label xPositionLabel = new Label(Language.translate("x.axis"));
        Label yPositionLabel = new Label(Language.translate("y.axis"));
        Label zPositionLabel = new Label(Language.translate("z.axis"));

        xPositionLabel.setId("spatial-view-label-position");
        yPositionLabel.setId("spatial-view-label-position");
        zPositionLabel.setId("spatial-view-label-position");

        xPositionChoiceBox = new ChoiceBox<XPositionType>(FXCollections.observableArrayList(XPositionType.values()));
        yPositionChoiceBox = new ChoiceBox<YPositionType>(FXCollections.observableArrayList(YPositionType.values()));
        zPositionChoiceBox = new ChoiceBox<ZPositionType>(FXCollections.observableArrayList(ZPositionType.values()));

        xPositionChoiceBox.setId("effect-position-choice-box");
        yPositionChoiceBox.setId("effect-position-choice-box");

        BorderPane titleButtonBorderPane = new BorderPane();
        titleButtonBorderPane.setId("title-button-hbox");
        Text title = new Text(Language.translate("position"));
        title.setId("sensory-effect-title");
        titleButtonBorderPane.setLeft(title);

        GridPane propertyGridPane = new GridPane();
        propertyGridPane.setId("sensory-effect-property-grid-pane");
        propertyGridPane.add(xPositionLabel, 0, 0);
        propertyGridPane.add(xPositionChoiceBox, 1, 0);
        propertyGridPane.add(yPositionLabel, 0, 1);
        propertyGridPane.add(yPositionChoiceBox, 1, 1);
        propertyGridPane.add(zPositionLabel, 2, 1);
        propertyGridPane.add(zPositionChoiceBox, 3, 1);

        getChildren().add(titleButtonBorderPane);
        getChildren().add(propertyGridPane);

        populatePropertyPane();

        createListeners();

    }

    @Override
    public void populatePropertyPane(){

        EffectPositionProperty effectPositionProperty =
                (EffectPositionProperty) sensoryEffectNode.getPresentationProperty().positionProperty;

        xPositionChoiceBox.setValue(effectPositionProperty.getxPosition());
        yPositionChoiceBox.setValue(effectPositionProperty.getyPosition());
        zPositionChoiceBox.setValue(effectPositionProperty.getzPosition());

    }

    private void createListeners(){

        xPositionChoiceBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                updateSensoryEffectPositions();
                yPositionChoiceBox.requestFocus();
            }
        });

        yPositionChoiceBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                updateSensoryEffectPositions();
                zPositionChoiceBox.requestFocus();
            }
        });

        zPositionChoiceBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                updateSensoryEffectPositions();
            }
        });

    }

    private void updateSensoryEffectPositions(){

        applicationController.updateSensoryEffectPositions(this, sensoryEffectNode);

    }

    public ChoiceBox<XPositionType> getxPositionChoiceBox() {
        return xPositionChoiceBox;
    }

    public ChoiceBox<YPositionType> getyPositionChoiceBox() {
        return yPositionChoiceBox;
    }

    public ChoiceBox<ZPositionType> getzPositionChoiceBox() {
        return zPositionChoiceBox;
    }
}
