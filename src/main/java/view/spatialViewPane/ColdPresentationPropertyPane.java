package view.spatialViewPane;

import controller.ApplicationController;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import model.common.SensoryEffectNode;
import model.spatialView.sensoryEffect.temperature.ColdPresentationProperty;
import view.common.Language;

import java.util.function.UnaryOperator;

public class ColdPresentationPropertyPane extends SensoryEffectPropertyPane {

    private TextField intensityValueTextField;
    private TextField rangeFromTextField;
    private TextField rangeToTextField;

    private ApplicationController applicationController;
    private SensoryEffectNode sensoryEffectNode;

    public ColdPresentationPropertyPane(ApplicationController applicationController, SensoryEffectNode sensoryEffectNode){

        setId("sensory-effect-vbox");

        this.applicationController = applicationController;
        this.sensoryEffectNode = sensoryEffectNode;

        Label intensityValueLabel = new Label(Language.translate("intensity.value"));
        Label intensityRangeLabel = new Label(Language.translate("intensity.range"));
        Label unitLabel = new Label(Language.translate("celsius"));

        intensityValueLabel.setId("spatial-view-label-intensity");
        intensityRangeLabel.setId("spatial-view-label-intensity");
        unitLabel.setId("spatial-view-label-intensity");

        intensityValueTextField = new TextField();
        rangeFromTextField = new TextField();
        rangeFromTextField.setDisable(true);
        rangeToTextField = new TextField();
        rangeToTextField.setDisable(true);

        rangeFromTextField.setTooltip(new Tooltip(Language.translate("from")));
        rangeToTextField.setTooltip(new Tooltip(Language.translate("to")));

        BorderPane titleButtonBorderPane = new BorderPane();
        titleButtonBorderPane.setId("title-button-hbox");
        Text title = new Text(Language.translate("cold.properties"));
        title.setId("sensory-effect-title");
        titleButtonBorderPane.setLeft(title);

        GridPane propertyGridPane = new GridPane();
        propertyGridPane.setId("sensory-effect-property-grid-pane");
        propertyGridPane.add(intensityValueLabel, 0, 0);
        propertyGridPane.add(intensityValueTextField, 1, 0);
        propertyGridPane.add(unitLabel, 2, 0);
        propertyGridPane.add(intensityRangeLabel, 0, 1);
        propertyGridPane.add(rangeFromTextField, 1, 1);
        propertyGridPane.add(rangeToTextField, 2, 1);

        getChildren().add(titleButtonBorderPane);
        getChildren().add(propertyGridPane);

        populatePropertyPane();

        createListeners();

        createValidatorListeners();

    }

    private void createValidatorListeners() {

        UnaryOperator<TextFormatter.Change> intensityValueTextFieldFilter = change -> {

            String newTextInput = "";
            int changeStartIndex = change.getRangeStart();

            if (change.isAdded()) {
                String stringChange = change.getText();
                StringBuilder sb = new StringBuilder(intensityValueTextField.getText());
                sb.insert(changeStartIndex, stringChange);
                newTextInput = sb.toString();
            } else if (change.isDeleted()) {
                String stringChange = intensityValueTextField.getText().substring(changeStartIndex, changeStartIndex + 1);
                newTextInput = intensityValueTextField.getText().replace(stringChange, "");
            }

            String regex = "^[0-9]*(\\.)?(\\d{1,2})?$";
            if (newTextInput.matches(regex)) {
                return change;
            } else {
                change.setText("");
            }
            return change;
        };

        TextFormatter<String> intensityValueTextFormatter = new TextFormatter<>(intensityValueTextFieldFilter);
        intensityValueTextField.setTextFormatter(intensityValueTextFormatter);

    }

    @Override
    public void populatePropertyPane(){

        ColdPresentationProperty coldPresentationProperty =
                (ColdPresentationProperty) sensoryEffectNode.getPresentationProperty();

        intensityValueTextField.setText(String.valueOf(coldPresentationProperty.getIntensityValue().getValue()));
        rangeFromTextField.setText(String.valueOf(coldPresentationProperty.getIntensityRange().getFromValue()));
        rangeToTextField.setText(String.valueOf(coldPresentationProperty.getIntensityRange().getToValue()));

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

                }
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

}
