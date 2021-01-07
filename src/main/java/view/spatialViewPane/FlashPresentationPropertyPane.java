package view.spatialViewPane;

import controller.ApplicationController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import model.common.SensoryEffectNode;
import model.spatialView.sensoryEffect.FlashPresentationProperty;
import view.common.Language;

import java.util.function.UnaryOperator;

public class FlashPresentationPropertyPane extends SensoryEffectPropertyPane {

    private TextField intensityValueTextField;
    private TextField rangeFromTextField;
    private TextField rangeToTextField;
    private ColorPicker colorPicker;
    private TextField frequencyTextField;

    private ApplicationController applicationController;
    private SensoryEffectNode sensoryEffectNode;

    public FlashPresentationPropertyPane(ApplicationController applicationController, SensoryEffectNode sensoryEffectNode) {

        setId("sensory-effect-vbox");

        this.applicationController = applicationController;
        this.sensoryEffectNode = sensoryEffectNode;

        getStylesheets().clear();

        Label intensityValueLabel = new Label(Language.translate("intensity.value"));
        Label intensityRangeLabel = new Label(Language.translate("intensity.range"));
        Label colorLabel = new Label(Language.translate("color"));
        Label frequencyLabel = new Label(Language.translate("frequency"));

        intensityValueLabel.setId("spatial-view-label-intensity-flashlight");
        intensityRangeLabel.setId("spatial-view-label-intensity-flashlight");
        colorLabel.setId("spatial-view-label-intensity");
        frequencyLabel.setId("spatial-view-label-intensity-flashlight");

        intensityValueTextField = new TextField();
        rangeFromTextField = new TextField();
        rangeToTextField = new TextField();
        colorPicker = new ColorPicker();
        frequencyTextField = new TextField();

        rangeFromTextField.setTooltip(new Tooltip(Language.translate("from")));
        rangeToTextField.setTooltip(new Tooltip(Language.translate("to")));

        BorderPane titleButtonBorderPane = new BorderPane();
        titleButtonBorderPane.setId("title-button-hbox");
        Text title = new Text(Language.translate("flash.properties"));
        title.setId("sensory-effect-title");
        titleButtonBorderPane.setLeft(title);

        GridPane propertyGridPane = new GridPane();
        propertyGridPane.setId("sensory-effect-property-grid-pane");
        propertyGridPane.add(intensityValueLabel, 0, 0);
        propertyGridPane.add(intensityValueTextField, 1, 0);
        propertyGridPane.add(intensityRangeLabel, 0, 1);
        propertyGridPane.add(rangeFromTextField, 1, 1);
        propertyGridPane.add(rangeToTextField, 2, 1);
        propertyGridPane.add(frequencyLabel, 0, 2);
        propertyGridPane.add(frequencyTextField, 1, 2);
        propertyGridPane.add(colorLabel, 0, 3);
        propertyGridPane.add(colorPicker, 1, 3);

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

        UnaryOperator<TextFormatter.Change> rangeFromTextFieldFilter = change -> {

            String newTextInput = "";
            int changeStartIndex = change.getRangeStart();

            if(change.isAdded()){
                String stringChange = change.getText();
                StringBuilder sb = new StringBuilder(rangeFromTextField.getText());
                sb.insert(changeStartIndex, stringChange);
                newTextInput = sb.toString();
            }else if(change.isDeleted()){
                String stringChange = rangeFromTextField.getText().substring(changeStartIndex, changeStartIndex+1);
                newTextInput = rangeFromTextField.getText().replace(stringChange, "");
            }

            String regex = "^[0-9]*(\\.)?(\\d{1,2})?$";
            if (newTextInput.matches(regex)) {
                return change;
            }else{
                change.setText("");
            }
            return change;
        };

        TextFormatter<String> rangeFromTextFormatter = new TextFormatter<>(rangeFromTextFieldFilter);
        rangeFromTextField.setTextFormatter(rangeFromTextFormatter);

        UnaryOperator<TextFormatter.Change> rangeToTextFieldFilter = change -> {

            String newTextInput = "";
            int changeStartIndex = change.getRangeStart();

            if(change.isAdded()){
                String stringChange = change.getText();
                StringBuilder sb = new StringBuilder(rangeToTextField.getText());
                sb.insert(changeStartIndex, stringChange);
                newTextInput = sb.toString();
            }else if(change.isDeleted()){
                String stringChange = rangeToTextField.getText().substring(changeStartIndex, changeStartIndex+1);
                newTextInput = rangeToTextField.getText().replace(stringChange, "");
            }

            String regex = "^[0-9]*(\\.)?(\\d{1,2})?$";
            if (newTextInput.matches(regex)) {
                return change;
            }else{
                change.setText("");
            }
            return change;
        };

        TextFormatter<String> rangeToTextFormatter = new TextFormatter<>(rangeToTextFieldFilter);
        rangeToTextField.setTextFormatter(rangeToTextFormatter);

        UnaryOperator<TextFormatter.Change>  frequencyTextFieldFilter = change -> {

            String newTextInput = "";
            int changeStartIndex = change.getRangeStart();

            if(change.isAdded()){
                String stringChange = change.getText();
                StringBuilder sb = new StringBuilder(frequencyTextField.getText());
                sb.insert(changeStartIndex, stringChange);
                newTextInput = sb.toString();
            }else if(change.isDeleted()){
                String stringChange = frequencyTextField.getText().substring(changeStartIndex, changeStartIndex+1);
                newTextInput = frequencyTextField.getText().replace(stringChange, "");
            }

            String regex = "^[0-9]*$";
            if (newTextInput.matches(regex)) {
                return change;
            }else{
                change.setText("");
            }
            return change;
        };

        TextFormatter<String> frequencyTextFormatter = new TextFormatter<>(frequencyTextFieldFilter);
        frequencyTextField.setTextFormatter(frequencyTextFormatter);

    }

    @Override
    public void populatePropertyPane(){

        FlashPresentationProperty flashLightPresentationProperty =
                (FlashPresentationProperty) sensoryEffectNode.getPresentationProperty();

        intensityValueTextField.setText(String.valueOf(flashLightPresentationProperty.getIntensityValue().getValue()));
        rangeFromTextField.setText(String.valueOf(flashLightPresentationProperty.getIntensityRange().getFromValue()));
        rangeToTextField.setText(String.valueOf(flashLightPresentationProperty.getIntensityRange().getToValue()));
        colorPicker.setValue(flashLightPresentationProperty.getColor());
        frequencyTextField.setText(String.valueOf(flashLightPresentationProperty.getFrequency()));

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

                    frequencyTextField.requestFocus();

                }
            }

        });

        frequencyTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {

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

    public TextField getFrequencyTextField() {
        return frequencyTextField;
    }
}
