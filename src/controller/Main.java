package controller;
	
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import br.uff.midiacom.ana.util.exception.XMLException;


public class Main {
	
	public static void main(String[] args)  {
		
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
					@SuppressWarnings("unused")
					StartPlugin startPlugin = new StartPlugin();
				} catch (XMLException | IOException e) {
					JOptionPane.showMessageDialog(null, "Error opening NCL document.");
				}
            }
        });
        
    }

}
