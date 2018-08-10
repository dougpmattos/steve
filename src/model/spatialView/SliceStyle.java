package model.spatialView;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import model.common.Media;
import javafx.scene.image.ImageView;
import model.spatialView.enums.AspectRatio;

import javax.imageio.ImageIO;
import java.awt.geom.Point2D;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

public class SliceStyle implements setImageInterface {

    @Override
    public Point2D spaceAvailable(ImageView mediaContent, double left, double right, double top, double bottom, String w, String h, StackPane screen) {
        Point2D space = null;

        double x=screen.getWidth(), y=screen.getHeight();
        if(left>100) left=100;
        if(top>100) top=100;
        if(right>100) right=100;
        if(bottom>100) bottom=100;

        //atualizar width e height
        //testar se left + width formam uma combinacao valida
        if(left!=0){
            x=screen.getWidth()-((left/100)*screen.getWidth());

        } else { //testar se right + width formam uma combinacao valida
            if(right!=0){
                x=(right/100)*screen.getWidth();
            }
        }

        if(top!=0){
            y=screen.getHeight()-((top/100)*screen.getHeight());
        } else { //testar se bottom + height formam uma combinacao valida
            if(bottom!=0){
                y=(bottom/100)*screen.getHeight();
            }
        }

        space = new Point2D.Double(x,y);

        return space;
    }

    @Override
    public ImageView setImageProperties(ImageView mediaContent, Media media, boolean hLock, boolean vLock, double top, double bottom, double left, double right, StackPane screen, boolean widthLock, boolean heightLock, String w, String h) {
        Point2D spaceAvailable = spaceAvailable(mediaContent,left,right,top,bottom,w,h, screen);
        mediaContent = slice(mediaContent,spaceAvailable,media, screen);
        if ((top == 0) && (bottom == 0) && (left == 0) && (right == 0)) {
            moveMediaLeft(mediaContent, media, left, screen);
            moveMediaTop(mediaContent, media, top, screen);
        }

        System.out.println("H Lock = " + hLock);
        if ((!hLock) && (!widthLock)) { //widthLock: only move to right if width % of image is not 100 else width has precedence over right
            moveMediaRight(mediaContent, media, right, screen);

            ImageView newImageView = new ImageView();
            SnapshotParameters snapParams = new SnapshotParameters();
            snapParams.setFill(Color.TRANSPARENT); // see documentation
            newImageView.setImage(screen.snapshot(snapParams, null));
            File file = new File("imageView-after-move-to-right.png");
            RenderedImage renderedImage = SwingFXUtils.fromFXImage(((ImageView) newImageView).getImage(), null);
            try {
                ImageIO.write(
                        renderedImage,
                        "png",
                        file);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            moveMediaLeft(mediaContent, media, left, screen);
        }
        if ((!vLock) && (!heightLock)) { // heightLock: only move to bottom if height % of image is not 100 else height has precedence over bottom
            moveMediaBottom(mediaContent, media, bottom, screen);
        } else {
            moveMediaTop(mediaContent, media, top, screen);
        }
        System.out.println("bottom: " + bottom + " top: " + top + " right: " + right + " left: " + left);
        if ((!vLock) && (bottom == 0)) {
            System.out.println("e zero vertical");
            moveMediaTop(mediaContent, media, top, screen);
        }
        System.out.println(right == 0);
        if ((!hLock) && (right == 0))
            moveMediaLeft(mediaContent, media, left, screen);

        return mediaContent;
    }



    @Override
    public void moveMediaBottom(ImageView mediaContent, Media media, double bottom, StackPane screen) {

        PositionProperty pp = media.getPresentationProperty().getPositionProperty();
        pp.setBottom(Double.toString(bottom));

        media.getPresentationProperty().setPositionProperty(pp);

        int boundHeight = (int) mediaContent.getLayoutBounds().getHeight();
        int boundHeightParent = (int) mediaContent.getBoundsInParent().getHeight();
        int boundHeightLocal = (int) mediaContent.getBoundsInLocal().getHeight();

        double screenHeight = screen.getHeight();

        System.out.println(boundHeight);
        System.out.println(boundHeightLocal);
        System.out.println(boundHeightParent);

        double yZero = (screenHeight) - boundHeight; //Referencial da tela (borda inferior)
        System.out.println("yZero: " + yZero);

        double dYDown = (bottom / 100) * screenHeight;

        String imageFile = media.getFile().toURI().toString();

        System.out.println("imageFile: " + imageFile);

        Image image = new Image(imageFile);

        double ty = image.getHeight();

        if (ty < screenHeight) {

            mediaContent.setFitHeight(dYDown);

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
//            if(tx < dXRight){
//                mediaContent.setTranslateX(dXRight-tx);
//            } else {
                mediaContent.setFitWidth(dXRight);
//            }
        }


    }



    @Override
    public void moveMediaLeft(ImageView mediaContent, Media media, double left, StackPane screen) {
        PositionProperty pp = media.getPresentationProperty().getPositionProperty();
        pp.setLeft(Double.toString(left));

        media.getPresentationProperty().setPositionProperty(pp);

        double screenWidth = screen.getWidth();
        double dXLeft = (left/100)*screen.getWidth();
//		if(media.getPresentationProperty().getSizeProperty().getAspectRatio().compareTo(AspectRatio.FILL)==0){
        double xZero = 0; //Referencial da tela (borda esquerda)
        mediaContent.setTranslateX(xZero+dXLeft);
    }

    @Override
    public void moveMediaTop(ImageView mediaContent, Media media, double top, StackPane screen) {
        PositionProperty pp = media.getPresentationProperty().getPositionProperty();
        pp.setTop(Double.toString(top));
        media.getPresentationProperty().setPositionProperty(pp);

        double screenHeight = screen.getHeight();

        double yZero = 0; //Referencial da tela (borda superior)
        double dYTop = (top/100)*screenHeight;

        mediaContent.setTranslateY(yZero+dYTop);

    }

    public Image scale(Image source, double targetWidth, double targetHeight, boolean preserveRatio, SnapshotParameters parameters) {
        ImageView imageView = new ImageView(source);
        imageView.setPreserveRatio(preserveRatio);
        imageView.setFitWidth(targetWidth);
        imageView.setFitHeight(targetHeight);
        return imageView.snapshot(parameters, null);
    }

    public ImageView slice(ImageView imageView, Point2D space, Media media, StackPane screen){

        imageView.setPreserveRatio(true);

        String imageFile = media.getFile().toURI().toString();

        System.out.println("imageFile: "+imageFile);

        Image image = new Image(imageFile);

        double tx=image.getWidth();

        double ty=image.getHeight();

        PixelReader reader = image.getPixelReader();

        WritableImage newImage = new WritableImage(reader, (int)  image.getWidth(), (int) image.getHeight());

        SnapshotParameters parameters = new SnapshotParameters();

        parameters.setFill(Color.TRANSPARENT);

        ImageView nv = new ImageView(newImage);

        nv.setPreserveRatio(true);

        imageView.setImage(newImage);

        Image smlImg = scale(imageView.getImage(),(int) space.getX(),(int) space.getY(), true, parameters);

//		saveToFile(smlImg,"-2");

        if((tx<screen.getWidth())&&(ty<screen.getHeight())){

            nv.setImage(smlImg);

            nv.setPreserveRatio(true);

            reader = smlImg.getPixelReader();

//			saveToFile(smlImg,"-2");

            System.out.println("(int) space.getX() "+(int) space.getX()+" space.getY() "+(int) space.getY());

            System.out.println("(img X() "+(int) smlImg.getWidth()+" img Y() "+(int) smlImg.getHeight());

            if(smlImg.getWidth()<space.getX()) space.setLocation((int) smlImg.getWidth(), space.getY());

            if(smlImg.getHeight()<space.getY()) space.setLocation(space.getX(), (int) smlImg.getHeight());


        }


        WritableImage wi = nv.snapshot(parameters, new WritableImage(reader, 0,0,(int) space.getX(),(int) space.getY()));

        if(ty>tx){
            //if (space.getX()<screen.getHeight()) nv.setFitWidth();
            nv.setFitWidth(space.getX());

        }
        else {

            nv.setFitHeight(space.getY());

        }

//		saveToFile(nv.getImage(),"-3");

        System.out.println("Tamanho 1: "+nv.getFitWidth()+"x"+nv.getFitHeight());
        newImage = nv.snapshot(parameters, wi);

//		saveToFile(newImage,"-4");

        imageView.setImage(newImage);
        System.out.println("Tamanho 2: "+imageView.getFitWidth()+"x"+imageView.getFitHeight());
//		saveToFile(smlImg,"-5");

        return imageView;

    }

}
