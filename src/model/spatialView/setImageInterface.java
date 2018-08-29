package model.spatialView;

import javafx.scene.layout.StackPane;
import model.common.Media;
import javafx.scene.image.ImageView;
import java.awt.geom.Point2D;

public interface setImageInterface {

    Point2D spaceAvailable(ImageView mediaContent, double left, double right, double top, double bottom, String w, String h, StackPane screen);

    ImageView setImageProperties(ImageView mediaContent, Media media, boolean hLock, boolean vLock, double top, double bottom, double left, double right, StackPane screen, boolean widthLock, boolean heightLock, String w, String h);

    void moveMediaBottom(ImageView mediaContent, Media media, double bottom, StackPane screen);

    void moveMediaRight(ImageView mediaContent, Media media, double right,StackPane screen);

    void moveMediaLeft(ImageView mediaContent, Media media, double left, StackPane screen);

    void moveMediaTop(ImageView mediaContent, Media media, double top, StackPane screen);


}
