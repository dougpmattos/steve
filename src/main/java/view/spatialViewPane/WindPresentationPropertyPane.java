package view.spatialViewPane;

import controller.ApplicationController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import model.common.SensoryEffectNode;
import model.spatialView.sensoryEffect.WindPresentationProperty;
import view.common.Language;
import view.enums.WindUnit;

public class WindPresentationPropertyPane extends SensoryEffectPropertyPane {

    private TextField intensityValueTextField;
    private TextField rangeFromTextField;
    private TextField rangeToTextField;
    private ChoiceBox<WindUnit> intensityUnit;

    private ApplicationController applicationController;
    private SensoryEffectNode sensoryEffectNode;

    public WindPresentationPropertyPane(ApplicationController applicationController, SensoryEffectNode sensoryEffectNode){

        setId("sensory-effect-vbox");

        this.applicationController = applicationController;
        this.sensoryEffectNode = sensoryEffectNode;

        Label intensityValueLabel = new Label(Language.translate("intensity.value"));
        Label intensityRangeLabel = new Label(Language.translate("intensity.range"));

        intensityValueLabel.setId("spatial-view-label-intensity");
        intensityRangeLabel.setId("spatial-view-label-intensity");

        intensityValueTextField = new TextField();
        intensityUnit = new ChoiceBox<WindUnit>(FXCollections.observableArrayList(WindUnit.BEAUFORT, WindUnit.CUSTOM));
        intensityUnit.setValue(WindUnit.BEAUFORT);
        rangeFromTextField = new TextField();
        rangeFromTextField.setDisable(true);
        rangeToTextField = new TextField();
        rangeToTextField.setDisable(true);

        rangeFromTextField.setTooltip(new Tooltip(Language.translate("from")));
        rangeToTextField.setTooltip(new Tooltip(Language.translate("to")));

        BorderPane titleButtonBorderPane = new BorderPane();
        titleButtonBorderPane.setId("title-button-hbox");
        Text title = new Text(Language.translate("wind.properties"));
        title.setId("sensory-effect-title");
        titleButtonBorderPane.setLeft(title);

        GridPane propertyGridPane = new GridPane();
        propertyGridPane.setId("sensory-effect-property-grid-pane");
        propertyGridPane.add(intensityValueLabel, 0, 0);
        propertyGridPane.add(intensityValueTextField, 1, 0);
        propertyGridPane.add(intensityUnit, 2, 0);
        propertyGridPane.add(intensityRangeLabel, 0, 1);
        propertyGridPane.add(rangeFromTextField, 1, 1);
        propertyGridPane.add(rangeToTextField, 2, 1);

        getChildren().add(titleButtonBorderPane);
        getChildren().add(propertyGridPane);

        populatePropertyPane();

        createListeners();

    }

    @Override
    public void populatePropertyPane(){

        WindPresentationProperty windPresentationProperty =
                (WindPresentationProperty) sensoryEffectNode.getPresentationProperty();

        intensityValueTextField.setText(String.valueOf(windPresentationProperty.getIntensityValue().getValue()));
        rangeFromTextField.setText(String.valueOf(windPresentationProperty.getIntensityRange().getFromValue()));
        rangeToTextField.setText(String.valueOf(windPresentationProperty.getIntensityRange().getToValue()));
        intensityUnit.setValue(windPresentationProperty.getUnit());

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

                    intensityUnit.requestFocus();

                }
            }

        });

        intensityUnit.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {

                if(intensityUnit.getItems().get((Integer) number2) == WindUnit.CUSTOM){
                    rangeFromTextField.setDisable(false);
                    rangeToTextField.setDisable(false);
                }else if(intensityUnit.getItems().get((Integer) number2) == WindUnit.BEAUFORT){
                    rangeFromTextField.setText("0.0");
                    rangeToTextField.setText("12.0");
                    rangeFromTextField.setDisable(true);
                    rangeToTextField.setDisable(true);
                }
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

    public ChoiceBox<WindUnit> getIntensityUnit() {
        return intensityUnit;
    }

}
