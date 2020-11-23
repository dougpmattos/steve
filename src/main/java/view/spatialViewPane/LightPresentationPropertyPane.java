package view.spatialViewPane;

import controller.ApplicationController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import model.common.SensoryEffectNode;
import model.spatialView.sensoryEffect.LightPresentationProperty;
import view.common.Language;

public class LightPresentationPropertyPane extends SensoryEffectPropertyPane {

    private TextField intensityValueTextField;
    private TextField rangeFromTextField;
    private TextField rangeToTextField;
    private ColorPicker colorPicker;

    private ApplicationController applicationController;
    private SensoryEffectNode sensoryEffectNode;

    public LightPresentationPropertyPane(ApplicationController applicationController, SensoryEffectNode sensoryEffectNode) {

        setId("sensory-effect-vbox");

        this.applicationController = applicationController;
        this.sensoryEffectNode = sensoryEffectNode;

        getStylesheets().clear();

        Label intensityValueLabel = new Label(Language.translate("intensity.value"));
        Label intensityRangeLabel = new Label(Language.translate("intensity.range"));
        Label colorLabel = new Label(Language.translate("color"));

        intensityValueLabel.setId("spatial-view-label-intensity-flashlight");
        intensityRangeLabel.setId("spatial-view-label-intensity-flashlight");
        colorLabel.setId("spatial-view-label-intensity");

        intensityValueTextField = new TextField();
        rangeFromTextField = new TextField();
        rangeToTextField = new TextField();
        colorPicker = new ColorPicker();

        rangeFromTextField.setTooltip(new Tooltip(Language.translate("from")));
        rangeToTextField.setTooltip(new Tooltip(Language.translate("to")));

        BorderPane titleButtonBorderPane = new BorderPane();
        titleButtonBorderPane.setId("title-button-hbox");
        Text title = new Text(Language.translate("light.properties"));
        title.setId("sensory-effect-title");
        titleButtonBorderPane.setLeft(title);

        GridPane propertyGridPane = new GridPane();
        propertyGridPane.setId("sensory-effect-property-grid-pane");
        propertyGridPane.add(intensityValueLabel, 0, 0);
        propertyGridPane.add(intensityValueTextField, 1, 0);
        propertyGridPane.add(intensityRangeLabel, 0, 1);
        propertyGridPane.add(rangeFromTextField, 1, 1);
        propertyGridPane.add(rangeToTextField, 2, 1);
        propertyGridPane.add(colorLabel, 0, 2);
        propertyGridPane.add(colorPicker, 1, 2);

        getChildren().add(titleButtonBorderPane);
        getChildren().add(propertyGridPane);

        populatePropertyPane();

        createListeners();

    }

    @Override
    public void populatePropertyPane(){

        LightPresentationProperty LightPresentationProperty =
                (LightPresentationProperty) sensoryEffectNode.getPresentationProperty();

        intensityValueTextField.setText(String.valueOf(LightPresentationProperty.getIntensityValue().getValue()));
        rangeFromTextField.setText(String.valueOf(LightPresentationProperty.getIntensityRange().getFromValue()));
        rangeToTextField.setText(String.valueOf(LightPresentationProperty.getIntensityRange().getToValue()));
        colorPicker.setValue(LightPresentationProperty.getColor());

    }

    private void createListeners(){

        intensityValueTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {

                if(event.getCode().equals(KeyCode.ENTER) || event.getCode().equals(KeyCode.TAB)) {

                    updateSensoryEffectProperties();

                    rangeFromTextField.requestFocus();

                }
            }

        });

        rangeFromTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {

                if(event.getCode().equals(KeyCode.ENTER) || event.getCode().equals(KeyCode.TAB)) {

                    updateSensoryEffectProperties();

                    rangeToTextField.requestFocus();

                }
            }

        });

        rangeToTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {

                if(event.getCode().equals(KeyCode.ENTER) || event.getCode().equals(KeyCode.TAB)) {

                    updateSensoryEffectProperties();

                    colorPicker.requestFocus();

                }
            }

        });

        colorPicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                updateSensoryEffectProperties();
            }
        });

    }

    private void updateSensoryEffectProperties(){

        applicationController.updateSensoryEffectProperties(this, sensoryEffectNode);

    }

    public TextField getIntensityValueTextField() {
        return intensityValueTextField;
    }

    public TextField getRangeFromTextField() {
        return rangeFromTextField;
    }

    public TextField getRangeToTextField() {
        return rangeToTextField;
    }

    public ColorPicker getColorPicker() {
        return colorPicker;
    }
}
