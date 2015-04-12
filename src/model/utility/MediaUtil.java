package model.utility;

import java.text.DecimalFormat;

public class MediaUtil {

	public static Double approximateDouble(Double value){
		
        DecimalFormat approximator = new DecimalFormat( " 0.00 " );
        String aprox = approximator.format(value);
        aprox = aprox.replace(",", ".");
        Double d = Double.parseDouble(aprox);
        return d;
        
    }
	
}
