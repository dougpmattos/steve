module steve {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.media;
    requires javafx.web;
    requires javafx.swing;
    requires javafx.graphics;

    requires java.prefs;
    requires json.simple;
    requires aNa;
    requires core;
    requires jgrapht;
    requires jgraph;
    requires com.jfoenix;
    requires commons.io;
    requires com.google.common;

    exports view.stevePane to javafx.graphics;

    opens view.stevePane to javafx.fxml;

    opens fonts;

    opens images.loadingWindow;
    opens images.common;
    opens images.repositoryPane;
    opens images.sensoryEffectsPane;
    opens images.spatialViewPane;
    opens images.temporalViewPane;

    opens jsFiles;

    opens jsonFiles;

    opens languages;

    opens NCLFiles;

    opens styles.common;
    opens styles.htmlSupport;
    opens styles.repositoryPane;
    opens styles.sensoryEffectsPane;
    opens styles.spatialViewPane;
    opens styles.stevePane;
    opens styles.temporalViewPane;
}