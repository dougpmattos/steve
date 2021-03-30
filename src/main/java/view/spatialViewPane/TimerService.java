package view.spatialViewPane;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

public class TimerService extends ScheduledService<Double> {

    // all instance variables accessed only on FX Application Thread:
    private DoubleProperty currentTimeProperty = new SimpleDoubleProperty();

    @Override
    protected Task<Double> createTask() {

        // invoked on FX Application Thread
        return new Task<Double>() {
            protected Double call() {

                // invoked on background thread:

                currentTimeProperty.set(currentTimeProperty.get() + 0.102);

                if(currentTimeProperty.get() < 0){
                    currentTimeProperty.set(0.0);
                }

                return getCurrentTime();

            }
        };
    }

    public void setCurrentTime(Double atomicDouble) {
        this.currentTimeProperty.set(atomicDouble);
    }

    public Double getCurrentTime() {
        return currentTimeProperty.get();
    }

}