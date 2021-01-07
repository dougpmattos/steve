package view.spatialViewPane;

import controller.ApplicationController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.common.SensoryEffectNode;
import model.spatialView.sensoryEffect.RainstormPresentationProperty;
import model.spatialView.sensoryEffect.enums.SprayingType;
import view.common.Language;

import java.util.function.UnaryOperator;

public class RainstormPresentationPropertyPane extends SensoryEffectPropertyPane {

    private TextField intensityValueTextField;
    private TextField rangeFromTextField;
    private TextField rangeToTextField;
    private ChoiceBox<SprayingType> sprayingTypeChoiceBox;

    private TextField flashIntensityValueTextField;
    private TextField flashRangeFromTextField;
    private TextField flashRangeToTextField;
    private ColorPicker colorPicker;
    private TextField frequencyTextField;

    private ApplicationController applicationController;
    private SensoryEffectNode sensoryEffectNode;

    public RainstormPresentationPropertyPane(ApplicationController applicationController, SensoryEffectNode sensoryEffectNode){

        setId("sensory-effect-vbox");

        this.applicationController = applicationController;
        this.sensoryEffectNode = sensoryEffectNode;

        Label intensityValueLabel = new Label(Language.translate("intensity.value"));
        Label intensityRangeLabel = new Label(Language.translate("intensity.range"));
        Label sprayingTypeLabel = new Label(Language.translate("spraying.type"));
        Label flashlightIntensityValueLabel = new Label(Language.translate("intensity.value"));
        Label lightIntensityRangeLabel = new Label(Language.translate("intensity.range"));
        Label colorLabel = new Label(Language.translate("color"));
        Label frequencyLabel = new Label(Language.translate("frequency"));

        intensityValueLabel.setId("spatial-view-label-intensity");
        intensityRangeLabel.setId("spatial-view-label-intensity");
        sprayingTypeLabel.setId("spatial-view-label-intensity");
        flashlightIntensityValueLabel.setId("spatial-view-label-intensity");
        lightIntensityRangeLabel.setId("spatial-view-label-intensity");
        colorLabel.setId("spatial-view-label-intensity");
        frequencyLabel.setId("spatial-view-label-intensity-flashlight");

        intensityValueTextField = new TextField();
        rangeFromTextField = new TextField();
        rangeToTextField = new TextField();
        sprayingTypeChoiceBox = new ChoiceBox<SprayingType>(FXCollections.observableArrayList(SprayingType.values()));

        rangeFromTextField.setTooltip(new Tooltip(Language.translate("from")));
        rangeToTextField.setTooltip(new Tooltip(Language.translate("to")));

        flashIntensityValueTextField = new TextField();
        flashRangeFromTextField = new TextField();
        flashRangeToTextField = new TextField();
        colorPicker = new ColorPicker();
        frequencyTextField = new TextField();

        flashRangeFromTextField.setTooltip(new Tooltip(Language.translate("from")));
        flashRangeToTextField.setTooltip(new Tooltip(Language.translate("to")));

        BorderPane titleButtonBorderPane = new BorderPane();
        titleButtonBorderPane.setId("title-button-hbox");
        Text title = new Text(Language.translate("rainstorm.properties"));
        title.setId("sensory-effect-title");
        titleButtonBorderPane.setLeft(title);

        Text waterSubtitle = new Text(Language.translate("water.sprayer"));
        waterSubtitle.setId("sensory-effect-subtitle");
        Text flashSubtitle = new Text(Language.translate("flash"));
        flashSubtitle.setId("sensory-effect-subtitle");

        Separator waterSeparator = new Separator();
        waterSeparator.setId("separator");
        waterSeparator.setPrefSize(200, 1);
        VBox waterSubtitleSeparatorContainer = new VBox();
        waterSubtitleSeparatorContainer.setSpacing(5);
        waterSubtitleSeparatorContainer.getChildren().add(waterSubtitle);
        waterSubtitleSeparatorContainer.getChildren().add(waterSeparator);

        Separator lightSeparator = new Separator();
        lightSeparator.setId("separator");
        lightSeparator.setPrefSize(200, 1);
        VBox flashlightSubtitleSeparatorContainer = new VBox();
        flashlightSubtitleSeparatorContainer.setSpacing(5);
        flashlightSubtitleSeparatorContainer.getChildren().add(flashSubtitle);
        flashlightSubtitleSeparatorContainer.getChildren().add(lightSeparator);

        GridPane propertyGridPane = new GridPane();
        propertyGridPane.setId("sensory-effect-property-grid-pane");
        propertyGridPane.add(waterSubtitleSeparatorContainer, 0, 0);
        propertyGridPane.add(intensityValueLabel, 0, 1);
        propertyGridPane.add(intensityValueTextField, 1, 1);
        propertyGridPane.add(intensityRangeLabel, 0, 2);
        propertyGridPane.add(rangeFromTextField, 1, 2);
        propertyGridPane.add(rangeToTextField, 2, 2);
        propertyGridPane.add(sprayingTypeLabel, 0, 3);
        propertyGridPane.add(sprayingTypeChoiceBox, 1, 3);
        propertyGridPane.add(flashlightSubtitleSeparatorContainer, 0, 4);
        propertyGridPane.add(flashlightIntensityValueLabel, 0, 5);
        propertyGridPane.add(flashIntensityValueTextField, 1, 5);
        propertyGridPane.add(lightIntensityRangeLabel, 0, 6);
        propertyGridPane.add(flashRangeFromTextField, 1, 6);
        propertyGridPane.add(flashRangeToTextField, 2, 6);
        propertyGridPane.add(frequencyLabel, 0, 7);
        propertyGridPane.add(frequencyTextField, 1, 7);
        propertyGridPane.add(colorLabel, 0, 8);
        propertyGridPane.add(colorPicker, 1, 8);

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

        RainstormPresentationProperty rainstormPresentationProperty =
                (RainstormPresentationProperty) sensoryEffectNode.getPresentationProperty();

        intensityValueTextField.setText(String.valueOf(rainstormPresentationProperty.getWaterSprayerPresentationProperty().getIntensityValue().getValue()));
        rangeFromTextField.setText(String.valueOf(rainstormPresentationProperty.getWaterSprayerPresentationProperty().getIntensityRange().getFromValue()));
        rangeToTextField.setText(String.valueOf(rainstormPresentationProperty.getWaterSprayerPresentationProperty().getIntensityRange().getToValue()));
        sprayingTypeChoiceBox.setValue(rainstormPresentationProperty.getWaterSprayerPresentationProperty().getSprayingType());

        flashIntensityValueTextField.setText(String.valueOf(rainstormPresentationProperty.getFlashPresentationProperty().getIntensityValue().getValue()));
        flashRangeFromTextField.setText(String.valueOf(rainstormPresentationProperty.getFlashPresentationProperty().getIntensityRange().getFromValue()));
        flashRangeToTextField.setText(String.valueOf(rainstormPresentationProperty.getFlashPresentationProperty().getIntensityRange().getToValue()));
        colorPicker.setValue(rainstormPresentationProperty.getFlashPresentationProperty().getColor());
        frequencyTextField.setText(String.valueOf(rainstormPresentationProperty.getFlashPresentationProperty().getFrequency()));

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

                    sprayingTypeChoiceBox.requestFocus();

                }
            }

        });

        sprayingTypeChoiceBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                updateSensoryEffectProperties();
                flashIntensityValueTextField.requestFocus();
            }
        });

        flashIntensityValueTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {

                if(event.getCode().equals(KeyCode.ENTER) || event.getCode().equals(KeyCode.TAB)) {

                    updateSensoryEffectProperties();

                    flashRangeFromTextField.requestFocus();

                }
            }

        });

        flashRangeFromTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {

                if(event.getCode().equals(KeyCode.ENTER) || event.getCode().equals(KeyCode.TAB)) {

                    updateSensoryEffectProperties();

                    flashRangeToTextField.requestFocus();

                }
            }

        });

        flashRangeToTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {

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

    public TextField getFlashIntensityValueTextField() {
        return flashIntensityValueTextField;
    }

    public TextField getFlashRangeFromTextField() {
        return flashRangeFromTextField;
    }

    public TextField getFlashRangeToTextField() {
        return flashRangeToTextField;
    }

    public ColorPicker getColorPicker() {
        return colorPicker;
    }

    public ChoiceBox<SprayingType> getSprayingTypeChoiceBox() {
        return sprayingTypeChoiceBox;
    }

    public TextField getFrequencyTextField() {
        return frequencyTextField;
    }

}
