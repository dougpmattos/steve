package view.spatialViewPane;

import controller.ApplicationController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import model.common.SensoryEffectNode;
import model.spatialView.sensoryEffect.ScentPresentationProperty;
import model.spatialView.sensoryEffect.WaterSprayerPresentationProperty;
import model.spatialView.sensoryEffect.enums.ScentType;
import model.spatialView.sensoryEffect.enums.SprayingType;
import view.common.Language;

public class ScentPresentationPropertyPane extends SensoryEffectPropertyPane {

    private TextField intensityValueTextField;
    private TextField rangeFromTextField;
    private TextField rangeToTextField;
    private ChoiceBox<ScentType> scentTypeChoiceBox;

    private ApplicationController applicationController;
    private SensoryEffectNode sensoryEffectNode;

    public ScentPresentationPropertyPane(ApplicationController applicationController, SensoryEffectNode sensoryEffectNode) {

        setId("sensory-effect-vbox");

        this.applicationController = applicationController;
        this.sensoryEffectNode = sensoryEffectNode;

        Label intensityValueLabel = new Label(Language.translate("intensity.value"));
        Label intensityRangeLabel = new Label(Language.translate("intensity.range"));
        Label scentTypeLabel = new Label(Language.translate("scent.type"));

        intensityValueLabel.setId("spatial-view-label-intensity");
        intensityRangeLabel.setId("spatial-view-label-intensity");
        scentTypeLabel.setId("spatial-view-label-intensity");

        intensityValueTextField = new TextField();
        scentTypeChoiceBox = new ChoiceBox<ScentType>(FXCollections.observableArrayList(ScentType.values()));
        rangeFromTextField = new TextField();
        rangeToTextField = new TextField();

        rangeFromTextField.setTooltip(new Tooltip(Language.translate("from")));
        rangeToTextField.setTooltip(new Tooltip(Language.translate("to")));

        BorderPane titleButtonBorderPane = new BorderPane();
        titleButtonBorderPane.setId("title-button-hbox");
        Text title = new Text(Language.translate("scent.properties"));
        title.setId("sensory-effect-title");
        titleButtonBorderPane.setLeft(title);

        GridPane propertyGridPane = new GridPane();
        propertyGridPane.setId("sensory-effect-property-grid-pane");
        propertyGridPane.add(intensityValueLabel, 0, 0);
        propertyGridPane.add(intensityValueTextField, 1, 0);
        propertyGridPane.add(intensityRangeLabel, 0, 1);
        propertyGridPane.add(rangeFromTextField, 1, 1);
        propertyGridPane.add(rangeToTextField, 2, 1);
        propertyGridPane.add(scentTypeLabel, 0, 2);
        propertyGridPane.add(scentTypeChoiceBox, 1, 2);

        getChildren().add(titleButtonBorderPane);
        getChildren().add(propertyGridPane);

        populatePropertyPane();

        createListeners();

    }

    @Override
    public void populatePropertyPane(){

        ScentPresentationProperty scentPresentationProperty =
                (ScentPresentationProperty) sensoryEffectNode.getPresentationProperty();

        intensityValueTextField.setText(String.valueOf(scentPresentationProperty.getIntensityValue().getValue()));
        rangeFromTextField.setText(String.valueOf(scentPresentationProperty.getIntensityRange().getFromValue()));
        rangeToTextField.setText(String.valueOf(scentPresentationProperty.getIntensityRange().getToValue()));
        scentTypeChoiceBox.setValue(scentPresentationProperty.getScentType());

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

                    scentTypeChoiceBox.requestFocus();

                }
            }

        });

        scentTypeChoiceBox.setOnAction(new EventHandler<ActionEvent>() {
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

    public ChoiceBox<ScentType> getScentTypeChoiceBox() {
        return scentTypeChoiceBox;
    }
}
