
package view.common;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author Douglas
 */
 public class Language {
    
	private static Locale locale;
	
	public static String translate(String message){
		
		ResourceBundle bundle = ResourceBundle.getBundle("languages.messages", Language.locale);
		return bundle.getString(message);

//		ResourceBundle bundle = ResourceBundle.getBundle("view.common.languages.messages", Language.locale);
//		return bundle.getString(message);
		
	}
	
	public static void setLocale(Locale locale){
		Language.locale = locale;
	}
	
	
}
