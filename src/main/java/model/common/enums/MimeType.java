package model.common.enums;

public enum MimeType {

    TEXT_HTML("text/html"),
    TEXT_PLAIN("text/plain"),
    TEXT_CSS("text/css"),
    TEXT_XML("text/xml"),
    IMAGE_BMP("image/bmp"),
    IMAGE_PNG("image/png"),
    IMAGE_GIF("image/gif"),
    IMAGE_JPEG("image/jpeg"),
    AUDIO_BASIC("audio/basic"),
    AUDIO_MP3("audio/mp3"),
    AUDIO_MP2("audio/mp2"),
    AUDIO_MPEG("audio/mpeg"),
    AUDIO_MPEG4("audio/mpeg4"),
    VIDEO_MPEG("video/mpeg"),
    VIDEO_MP4("video/mp4"),
    APPLICATION_X_GINGA_NCL("application/x-ginga-NCL"),
    APPLICATION_X_GINGA_NCLUA("application/x-ginga-NCLua"),
    APPLICATION_X_GINGA_NCLET("application/x-ginga-NCLet"),
    APPLICATION_X_GINGA_SETTINGS("application/x-ginga-settings"),
    APPLICATION_X_GINGA_TIME("application/x-ginga-time");


    private String name;

    private MimeType(String name) {
        this.name = name;
    }

    public static MimeType getEnumType(String name){
        for(MimeType opt : values()){
            if(name.equals(opt.name))
                return opt;
        }
        return null;
    }

    @Override
    public String toString() {
        return name;
    }
    
}
