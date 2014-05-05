
package controller;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;

/**
 *
 * @author Douglas
 */
public class PluginInfo {
    
    public static List<Object[]> getAlias(){
        @SuppressWarnings({ "rawtypes", "unchecked" })
		List<Object[]> alias = new ArrayList();
        
        Object[] enOpt = new Object[2];
        Object[] ptOpt = new Object[2];
        
        //enOpt[0] = MultiLanguage.Language.EN;
        enOpt[1] = "Temporal view";
        
        //ptOpt[0] = MultiLanguage.Language.PT;
        ptOpt[1] = "Visão temporal";
        
        alias.add(enOpt);
        alias.add(ptOpt);
        
        return alias;
    }
     
    public static List<Element> getNCLInterest(){
        // TODO Elementos direcionados ao NEXT.
    	return null;
    }
    
}
