package view.spatialViewPane;

import controller.ApplicationController;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.common.SensoryEffectNode;

public class EffectPropertyPaneContainer extends ScrollPane {

    private ApplicationController applicationController;
    private SensoryEffectNode sensoryEffectNode;

    private EffectPositionPane effectPositionPane;
    private SensoryEffectPropertyPane sensoryEffectPropertyPane;
    private VBox container;

    public EffectPropertyPaneContainer(ApplicationController applicationController, SensoryEffectNode sensoryEffectNode){

        this.applicationController = applicationController;
        this.sensoryEffectNode = sensoryEffectNode;

        sensoryEffectNode.addPropertyChangeListener(new SensoryEffectNodeListener(this));

        setId("property-pane");

        container = new VBox();
        container.setId("property-container");

        effectPositionPane = new EffectPositionPane(applicationController, sensoryEffectNode);

        sensoryEffectPropertyPane = createEffectPaneInstance();

        container.getChildren().add(effectPositionPane);
        container.getChildren().add(sensoryEffectPropertyPane);

        setContent(container);

    }

    private SensoryEffectPropertyPane createEffectPaneInstance() {

        switch(sensoryEffectNode.getType()) {

            case WIND:
                return new WindPresentationPropertyPane(applicationController, sensoryEffectNode);
            case WATER_SPRAYER:
                return new WaterSprayerPresentationPropertyPane(applicationController, sensoryEffectNode);
            case FLASH:
                return new FlashPresentationPropertyPane(applicationController, sensoryEffectNode);
            case FOG:
                return new FogPresentationPropertyPane(applicationController, sensoryEffectNode);
            case LIGHT:
                return new LightPresentationPropertyPane(applicationController, sensoryEffectNode);
            case SCENT:
                return new ScentPresentationPropertyPane(applicationController, sensoryEffectNode);
            case COLD:
                return new ColdPresentationPropertyPane(applicationController, sensoryEffectNode);
            case HOT:
                return new HotPresentationPropertyPane(applicationController, sensoryEffectNode);
            case VIBRATION:
                return new VibrationPresentationPropertyPane(applicationController, sensoryEffectNode);
//            case BUBBLE:
//                return new BubblePresentationPropertyPane(applicationController, sensoryEffectNode);
            case RAINSTORM:
                return new RainstormPresentationPropertyPane(applicationController, sensoryEffectNode);
//            case SNOW:
//                return new SnowPresentationPropertyPane(applicationController, sensoryEffectNode);
            default:
                return null;

        }

    }

    public SensoryEffectPropertyPane getSensoryEffectPropertyPane() {
        return sensoryEffectPropertyPane;
    }

}
