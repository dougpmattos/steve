package model.spatialView;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import model.common.Media;
import javafx.scene.image.ImageView;
import model.common.SpatialTemporalView;
import model.spatialView.enums.AspectRatio;

import javax.imageio.ImageIO;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;


public class HiddenStyle implements setImageInterface {

    SpatialTemporalView spatialTemporalView;

    double fullHeight = 1080;
    double fullWidth = 1920;

    public void setSpatialTemporalView(SpatialTemporalView spatialTemporalView) {
        this.spatialTemporalView = spatialTemporalView;
    }

    @Override
    public Point2D spaceAvailable(ImageView mediaContent, double left, double right, double top, double bottom, String w, String h, StackPane screen) {
        return null;
    }

    public Point2D spaceAvailable(ImageView mediaContent, Media media, double left, double right, double top, double bottom, String w, String h, StackPane screen) {
        Point2D space = null;

        double x=fullWidth, y=fullHeight;
        if(left>100) left=100;
        if(top>100) top=100;
        if(right>100) right=100;
        if(bottom>100) bottom=100;

        SizeProperty sizeProperty = media.getPresentationProperty().getSizeProperty();

        double inputWidth = Double.parseDouble(sizeProperty.getWidth().replace("%",""));
        double absolutWidth = inputWidth/100 * (new Image(media.getFile().toURI().toString()).getWidth());

        double inputHeight = Double.parseDouble(sizeProperty.getHeight().replace("%",""));
        double absolutHeight = inputHeight/100 * (new Image(media.getFile().toURI().toString()).getHeight());

        //atualizar width e height
        //testar se left + width formam uma combinacao valida
        if(inputWidth!=100){
            x = absolutWidth;
        }
        else if(absolutWidth==screen.getWidth()) {

            if (left != 0) {
                x = screen.getWidth() - ((left / 100) * screen.getWidth());

            } else { //testar se right + width formam uma combinacao valida
                if (right != 0) {
                    x = (right / 100) * screen.getWidth();
                }
            }
        }
            else x=absolutWidth;

        if(inputHeight!=100){
            y = absolutHeight;
        }
        else if(absolutHeight==screen.getHeight()) {

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
//        if(!media.getPresentationProperty().getSizeProperty().getWidth().contains("100")){
//            x=media.getPresentationProperty().getSizeProperty().getRealSize().getX();
//        }
        //else
        if((fullWidth>=image.getWidth())&&(image.getWidth()==space.getX())){ //if width available bigger than original width then width will be original width
            x=image.getWidth();
        }
        else if((fullWidth>=image.getWidth())&&(media.getPresentationProperty().getSizeProperty().getWidth()!="100")){
            x=space.getX();
        }
        else if(space.getX()==0){ //if 0 width available then fix it
            x=image.getWidth();
        }

        else { // if width available is smaller than original image width then width will be available width
            x=fullWidth;
        }

        if((fullHeight>=image.getHeight()&&(image.getHeight()==space.getY()))){//&&(media.getPresentationProperty().getSizeProperty().getHeight().contains("100"))){
            y=image.getHeight();
        }
        else if((fullHeight>=image.getHeight())&&(media.getPresentationProperty().getSizeProperty().getHeight()!="100")){
            y=space.getY();
        }

        else if(space.getY()==0){
            y=screen.getHeight();
        }
        else {
            y=fullHeight;
        }

//	        WritableImage(PixelReader reader, int x, int y, int width, int height)
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);

        WritableImage writableImage=null;

        if((x!=0)&&(y!=0)) {

            if((image.getWidth()<x)||(image.getHeight()<y)) {

//                writableImage = isImageSmallerThanDimensions(imageView, image, reader, writableImage, x, y, media, screen);
//                setImageToSmallerDimensions(imageView, image, reader, writableImage, x, y, media, screen);
                writableImage = setImageToSmallerDimensions(imageView, image, reader, writableImage, x, y, media, screen);
                image = writableImage;
                media.getPresentationProperty().getSizeProperty().setRealSize(new Point2D.Double(writableImage.getWidth(), writableImage.getHeight()));
            } else {

                writableImage = new WritableImage(reader, 0, 0, (int) x, (int) y);
//                SnapshotParameters parameters = new SnapshotParameters();
//                parameters.setFill(Color.TRANSPARENT);
                image = scaleSmaller(writableImage, writableImage.getWidth() / (fullWidth / screen.getWidth()), writableImage.getHeight() / (fullHeight / screen.getHeight()), true, parameters);
                media.getPresentationProperty().getSizeProperty().setRealSize(new Point2D.Double(writableImage.getWidth()/(fullWidth/screen.getWidth()), writableImage.getHeight()/(fullHeight/screen.getHeight())));
            }

            imageView.setImage(image);
        }

//        media.getPresentationProperty().getSizeProperty().setRealSize(new Point2D.Double(writableImage.getWidth(), writableImage.getHeight()));


        System.out.println("Compute area in screen: "+imageView.computeAreaInScreen());
        return imageView;
    }

    public Image scaleSmaller(Image source, double targetWidth, double targetHeight, boolean preserveRatio, SnapshotParameters parameters) {
        ImageView imageView = new ImageView(source);
        imageView.setPreserveRatio(preserveRatio);
        imageView.setFitWidth(targetWidth);
        imageView.setFitHeight(targetHeight);
        return imageView.snapshot(parameters, null);
    }

    public Media getMasterMedia(Media slaveMedia){
        for(SpatialRelation spatialRelation : spatialTemporalView.getSpatialRelationList()){
            if(spatialRelation instanceof Alignment){
                for(int i=0;i<((Alignment) spatialRelation).getSlaveMediaList().size();i++){
                    if(((Alignment) spatialRelation).getSlaveMediaList().get(i).equals(slaveMedia)) return ((Alignment) spatialRelation).getMasterMedia();
                }
            }
        } return null;
    }

    public WritableImage setImageToSmallerDimensions(ImageView imageView, Image image, PixelReader reader, WritableImage writableImage, double width, double height, Media media, StackPane screen){
        Media masterMedia = getMasterMedia(media);
        Point2D masterRealSize = masterMedia.getPresentationProperty().getSizeProperty().getRealSize();
//        ImageView iv = imageView;
//        SnapshotParameters parameters = new SnapshotParameters();
//        parameters.setFill(Color.BLACK);
//        WritableImage wi = iv.snapshot(parameters, null);
////                saveToFile(wi,"snapshot-out-of-bounds");
//        reader = wi.getPixelReader();
//
//        writableImage = iv.snapshot(parameters, new WritableImage(reader, 0,0,(int) masterRealSize.getX(),(int) masterRealSize.getY()));
        writableImage = new WritableImage((int) masterRealSize.getX(), (int) masterRealSize.getY());
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.BLACK);

        ImageView iv = new ImageView(writableImage);
        iv.snapshot(parameters,writableImage);
        StackPane temp = new StackPane();
        temp.setAlignment(Pos.TOP_LEFT);
        ImageView new_image = new ImageView(image);
        new_image.setPreserveRatio(true);
        new_image.setFitHeight((imageView.getBoundsInLocal().getMaxY()-imageView.getBoundsInLocal().getMinY())/ (fullHeight / screen.getHeight()));
        new_image.setFitWidth((imageView.getBoundsInLocal().getMaxX()-imageView.getBoundsInLocal().getMinX())/ (fullWidth / screen.getWidth()));
        temp.getChildren().add(iv);
        temp.getChildren().add(new_image);
//        WritableImage result = new WritableImage((int) masterRealSize.getX(), (int) masterRealSize.getY());
        temp.snapshot(parameters, writableImage);


//        saveToFile(writableImage, "blackImage");
        return writableImage;


    }

    public Point2D.Double redimensioningToBigggerImageSize(Image image, double width, double height){
        double proportion;
        double targetHeight=0;
        double targetWidth=0;
        if (width > height){
            proportion = width / image.getWidth();
            targetHeight = proportion * image.getHeight();
            return new Point2D.Double(width,targetHeight);
        }
        else {
            proportion = height / image.getHeight();
            targetWidth = proportion * image.getWidth();
            return new Point2D.Double(targetWidth,height);
        }
    }



    public Image scaleBigger(Image source, double targetWidth, double targetHeight, boolean preserveRatio) {
        ImageView imageView = new ImageView(source);
        imageView.setPreserveRatio(preserveRatio);
        imageView.setFitWidth(targetWidth);
        imageView.setFitHeight(targetHeight);
        return imageView.snapshot(null, null);
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

        Image smlImg = scaleSmaller(imageView.getImage(),(int) space.getX(),(int) space.getY(), true, parameters);
        Image bigImg = null;
        if(space.getY()>space.getX()){
            //if (space.getX()<screen.getHeight()) nv.setFitWidth();
            bigImg = scaleBigger(imageView.getImage(),(int) space.getY()/(fullHeight / screen.getHeight()),(int) space.getY()/(fullHeight / screen.getHeight()), true);

        } else    {
//            imageView.setFitHeight(space.getY());
            bigImg = scaleBigger(imageView.getImage(),(int) space.getX()/(fullWidth / screen.getWidth()),(int) space.getX()/(fullWidth / screen.getWidth()), true);
        }


//		saveToFile(smlImg,"-2");

        if((space.getX()<screen.getWidth())&&(space.getY()<screen.getHeight())){

            nv.setImage(smlImg);

            nv.setPreserveRatio(true);

            reader = smlImg.getPixelReader();

//			saveToFile(smlImg,"-2");

            System.out.println("(int) space.getX() "+(int) space.getX()+" space.getY() "+(int) space.getY());

            System.out.println("(img X() "+(int) smlImg.getWidth()+" img Y() "+(int) smlImg.getHeight());

//            if( smlImg.getWidth()<=space.getX()) space.setLocation((int) smlImg.getWidth(), space.getY());
//
//            if(smlImg.getHeight()<space.getY()) space.setLocation(space.getX(), (int) smlImg.getHeight());


        } else {reader = bigImg.getPixelReader();}

        System.out.println("2: (int) space.getX() "+(int) space.getX()+" space.getY() "+(int) space.getY());
        WritableImage wi = nv.snapshot(parameters, new WritableImage(reader, 0,0,(int) space.getX()/(int) (fullWidth/screen.getWidth()),(int) space.getY()/(int)(fullHeight/screen.getHeight())));
        System.out.println("3: (int) space.getX() "+(int) space.getX()+" space.getY() "+(int) space.getY());

        if(ty>tx){
            //if (space.getX()<screen.getHeight()) nv.setFitWidth();
            nv.setFitWidth(space.getX());
            nv.setFitHeight(space.getY());

        } else    {
            nv.setFitHeight(space.getY());
            nv.setFitWidth(space.getX());
        }

//        media.getPresentationProperty().getSizeProperty().setRealSize(new Point2D.Double(nv.getFitWidth(),nv.getFitHeight()));

//		saveToFile(nv.getImage(),"-3");

        System.out.println("Tamanho 1: "+nv.getFitWidth()+"x"+nv.getFitHeight());
        newImage = nv.snapshot(parameters, wi);

//		saveToFile(newImage,"-4");

        imageView.setImage(newImage);
        imageView.setPreserveRatio(true);
        if(space.getY()>space.getX()){
            //if (space.getX()<screen.getHeight()) nv.setFitWidth();
            imageView.setFitHeight(space.getY());

        } else    {
//            imageView.setFitHeight(space.getY());
            imageView.setFitWidth(space.getX());
        }

        System.out.println("Tamanho 2: "+imageView.getFitWidth()+"x"+imageView.getFitHeight());
//		saveToFile(smlImg,"-5");

        return imageView;

    }

    public static void saveToFile(Image image, String n) {
        File outputFile = new File("thisTest"+n+".png");
        BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
        try {
            ImageIO.write(bImage, "png", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
