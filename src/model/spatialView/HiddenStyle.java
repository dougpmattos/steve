package model.spatialView;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import model.common.Media;
import javafx.scene.image.ImageView;
import model.spatialView.enums.AspectRatio;

import javax.imageio.ImageIO;
import java.awt.geom.Point2D;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

public class HiddenStyle implements setImageInterface {

    @Override
    public Point2D spaceAvailable(ImageView mediaContent, double left, double right, double top, double bottom, String w, String h, StackPane screen) {
        return null;
    }

    public Point2D spaceAvailable(ImageView mediaContent, Media media, double left, double right, double top, double bottom, String w, String h, StackPane screen) {
        Point2D space = null;

        double x=screen.getWidth(), y=screen.getHeight();
        if(left>100) left=100;
        if(top>100) top=100;
        if(right>100) right=100;
        if(bottom>100) bottom=100;

        SizeProperty sizeProperty = media.getPresentationProperty().getSizeProperty();

        double absolutWidth = Integer.parseInt(sizeProperty.getWidth().replace("%",""));
        absolutWidth = absolutWidth/100 * screen.getWidth();

        double absolutHeight = Integer.parseInt(sizeProperty.getHeight().replace("%",""));
        absolutHeight = absolutHeight/100 * screen.getHeight();

        //atualizar width e height
        //testar se left + width formam uma combinacao valida
        if(absolutWidth==screen.getWidth()) {

            if (left != 0) {
                x = screen.getWidth() - ((left / 100) * screen.getWidth());

            } else { //testar se right + width formam uma combinacao valida
                if (right != 0) {
                    x = (right / 100) * screen.getWidth();
                }
            }
        } else
            x=absolutWidth;

        if(absolutHeight==screen.getHeight()) {

            if (top != 0) {
                y = screen.getHeight() - ((top / 100) * screen.getHeight());
            } else { //testar se bottom + height formam uma combinacao valida
                if (bottom != 0) {
                    y = (bottom / 100) * screen.getHeight();
                }
            }
        } else
            y=absolutHeight;

        space = new Point2D.Double(x,y);

        return space;

    }

    @Override
    public ImageView setImageProperties(ImageView mediaContent, Media media, boolean hLock, boolean vLock, double top, double bottom, double left, double right, StackPane screen, boolean widthLock, boolean heightLock, String w, String h) {
        Point2D spaceAvailable = spaceAvailable(mediaContent, media,left,right,top,bottom,w,h, screen);
        mediaContent = hidden(mediaContent,media,spaceAvailable,vLock,hLock,screen);
        if ((top == 0) && (bottom == 0) && (left == 0) && (right == 0)) {
            moveMediaLeft(mediaContent, media, left,screen);
            moveMediaTop(mediaContent, media, top,screen);
        }

        System.out.println("H Lock = " + hLock);
        if ((!hLock) && (!widthLock)) { //widthLock: only move to right if width % of image is not 100 else width has precedence over right
            moveMediaRight(mediaContent, media, right,screen);
        } else {
            moveMediaLeft(mediaContent, media, left, screen);
        }
        if ((!vLock) && (!heightLock) ) { // heightLock: only move to bottom if height % of image is not 100 else height has precedence over bottom
            moveMediaBottom(mediaContent, media, bottom,screen);
        } else {
            moveMediaTop(mediaContent, media, top,screen);
        }
        System.out.println("bottom: " + bottom + " top: " + top + " right: " + right + " left: " + left);
        if ((!vLock) && (bottom == 0)) {
            System.out.println("e zero vertical");
            moveMediaTop(mediaContent, media, top,screen);
        }
        System.out.println(right == 0);
        if ((!hLock) && (right == 0))
            moveMediaLeft(mediaContent, media, left,screen);

        return mediaContent;
    }



    @Override
    public void moveMediaBottom(ImageView mediaContent, Media media, double bottom, StackPane screen) {
        PositionProperty pp = media.getPresentationProperty().getPositionProperty();
        pp.setBottom(Double.toString(bottom));

        media.getPresentationProperty().setPositionProperty(pp);

        int boundHeight = (int)mediaContent.getLayoutBounds().getHeight();
        int boundHeightParent = (int)mediaContent.getBoundsInParent().getHeight();
        int boundHeightLocal = (int)mediaContent.getBoundsInLocal().getHeight();
        //the x or y variables of a shape, or translateX,
        //translateY should never be bound to boundsInParent for the purpose of positioning the node.
//		System.out.println("esse e o bound height= "+boundHeight);
        double screenHeight = screen.getHeight();

        System.out.println(boundHeight);
        System.out.println(boundHeightLocal);
        System.out.println(boundHeightParent);

        double yZero = (screenHeight)-boundHeight; //Referencial da tela (borda inferior)
        System.out.println("yZero: "+ yZero);

        double dYDown = (bottom/100)*screenHeight;
        //System.out.println("yZero-dYDown = "+(yZero-dYDown));
//		mediaContent.setFitWidth((screen.getWidth()));
        String imageFile = media.getFile().toURI().toString();

        System.out.println("imageFile: "+imageFile);

        Image image = new Image(imageFile);

        double ty=image.getHeight();

        if(ty<screenHeight){

            if(ty < dYDown){
                double x = dYDown-ty;
                System.out.println("Move: "+x);
                mediaContent.setTranslateY(dYDown-ty);

            } else {
                mediaContent.setFitHeight(dYDown);
            }

        }

    }



    @Override
    public void moveMediaRight(ImageView mediaContent, Media media, double right, StackPane screen) {
        PositionProperty pp = media.getPresentationProperty().getPositionProperty();
        pp.setRight(Double.toString(right));

        media.getPresentationProperty().setPositionProperty(pp);

        double screenWidth = screen.getWidth();
        double iw = mediaContent.getImage().getWidth();
        double dXRight = ((right/100)*screenWidth);

        String imageFile = media.getFile().toURI().toString();

        System.out.println("imageFile: "+imageFile);

        Image image = new Image(imageFile);

        double tx=image.getWidth();

        System.out.println("Size of media is: "+mediaContent.getImage().getWidth());
        if(tx<screenWidth){
            if(tx < dXRight){
                mediaContent.setTranslateX(dXRight-tx);
            } else {
                mediaContent.setFitWidth(dXRight);
            }

        }
    }

    @Override
    public void moveMediaLeft(ImageView mediaContent, Media media, double left, StackPane screen) {
        PositionProperty pp = media.getPresentationProperty().getPositionProperty();
        pp.setLeft(Double.toString(left));

        media.getPresentationProperty().setPositionProperty(pp);

        double screenWidth = screen.getWidth();
        double dXLeft = (left/100)*screen.getWidth();
        double xZero = 0; //Referencial da tela (borda esquerda)
        mediaContent.setTranslateX(xZero+dXLeft);
    }

    @Override
    public void moveMediaTop(ImageView mediaContent, Media media, double top, StackPane screen) {
        PresentationProperty presentationProperty = media.getPresentationProperty();
        PositionProperty pp = media.getPresentationProperty().getPositionProperty();
        pp.setTop(Double.toString(top));
        media.getPresentationProperty().setPositionProperty(pp);

        double screenWidth = screen.getWidth();
        double screenHeight = screen.getHeight();

        double yZero = 0; //Referencial da tela (borda superior)
        double dYTop = (top/100)*screenHeight;

        mediaContent.setTranslateY(yZero+dYTop);

    }

    public Point2D spaceAvailable(ImageView mediaContent, double left, double right, double top, double bottom, double w, double h){
        return null;
    }

    private ImageView hidden(ImageView imageView, Media media, Point2D space, boolean hLock, boolean vLock, StackPane screen){
        // TODO Auto-generated method stub

        imageView.setPreserveRatio(false);
        double x=0,y=0;

        String imageFile = media.getFile().toURI().toString();

        Image image = new Image(imageFile);

        PixelReader reader = image.getPixelReader();

        System.out.println("X = "+space.getX()+" >= "+image.getWidth());
        if(space.getX()>=image.getWidth()){
            x=image.getWidth();
        }

        else if(space.getX()==0){
            x=screen.getWidth();
        }

        else {
            x=space.getX();
        }

        if(space.getY()>=image.getHeight()){
            y=image.getHeight();
        }

        else if(space.getY()==0){
            y=screen.getHeight();
        }
        else {
            y=space.getY();
        }

//	        WritableImage(PixelReader reader, int x, int y, int width, int height)
        if((x!=0)||(y!=0)){
            WritableImage newImage = new WritableImage(reader, 0,0,(int) x, (int) y);
//            File file = new File("test.png");
//            RenderedImage renderedImage = SwingFXUtils.fromFXImage(newImage, null);
//            try {
//                ImageIO.write(
//                        renderedImage,
//                        "png",
//                        file);
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
            imageView.setImage(newImage);

            imageView.setFitHeight(y);
            imageView.setFitWidth(x);

        }



        System.out.println("Compute area in screen: "+imageView.computeAreaInScreen());
        return imageView;
    }


}
