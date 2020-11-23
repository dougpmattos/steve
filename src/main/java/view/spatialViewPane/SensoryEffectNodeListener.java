package view.spatialViewPane;

import model.common.SensoryEffectNode;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SensoryEffectNodeListener implements PropertyChangeListener {

    EffectPropertyPaneContainer effectPropertyPaneContainer;

    public SensoryEffectNodeListener(EffectPropertyPaneContainer effectPropertyPaneContainer) {
        this.effectPropertyPaneContainer = effectPropertyPaneContainer;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        effectPropertyPaneContainer.getSensoryEffectPropertyPane().populatePropertyPane();
        System.out.println("Chamou o listner");
    }

}
