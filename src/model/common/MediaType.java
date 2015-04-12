package model.common;

import gui.common.Language;

public enum MediaType {
	
	TEXT,
    IMAGE,
    AUDIO,
    VIDEO,
    PROCEDURAL,
    OTHER;
	    
	    
    public static MediaType getEnumType(String ext) throws Exception {
    	
        if(ext == null){
        	throw new Exception(Language.translate("null.extension"));
        }
        
        boolean status = false;

        status |= ext.contentEquals(".html");
        status |= ext.contentEquals(".html");
        status |= ext.contentEquals(".xhtml");
        status |= ext.contentEquals(".css");
        status |= ext.contentEquals(".xml");
        status |= ext.contentEquals(".txt");
        if(status){
            return TEXT;
        }

        status |= ext.contentEquals(".bmp");
        status |= ext.contentEquals(".png");
        status |= ext.contentEquals(".gif");
        status |= ext.contentEquals(".jpg");
        status |= ext.contentEquals(".jpeg");
        status |= ext.contentEquals(".jpe");
        if(status){
            return IMAGE;
        }

        status |= ext.contentEquals(".ua");
        status |= ext.contentEquals(".wav");
        status |= ext.contentEquals(".mp1");
        status |= ext.contentEquals(".mp2");
        status |= ext.contentEquals(".mp3");
        if(status){
            return AUDIO;
        }

        status |= ext.contentEquals(".mpeg");
        status |= ext.contentEquals(".mpg");
        status |= ext.contentEquals(".mpe");
        status |= ext.contentEquals(".mng");
        status |= ext.contentEquals(".qt");
        status |= ext.contentEquals(".mov");
        status |= ext.contentEquals(".avi");
        status |= ext.contentEquals(".mp4");
        status |= ext.contentEquals(".mpg4");
        if(status){
            return VIDEO;
        }
        
        status |= ext.contentEquals(".class");
        status |= ext.contentEquals(".xlet");
        status |= ext.contentEquals(".xlt");
        status |= ext.contentEquals(".lua");
        if(status){
            return PROCEDURAL;
        }
        
        return OTHER;
    }
	
}
