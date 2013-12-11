package edu.ucsd.ncmir.WIB.client.core.components;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Button;

         
    
/**
 *
 * @author spl
 */
public class ColorButton
    extends Button

{
    
    public ColorButton( String label )
        
    {
        
        super( label );
        
    }
    
    public void setColor( int red, int green, int blue )
        
    {
        
	String color =
	    "rgb(" + red + "," + green + "," + blue + ")";
	double intensity =
	    ( 0.299 * red ) + 
	    ( 0.587 * green ) + 
	    ( 0.115 * blue );
	
	int font_intensity = ( int ) ( ( intensity + 128 ) % 256 );

	Style s = this.getElement().getStyle();

	s.setProperty( "background", color );

	s.setColor( "rgb(" +
		    font_intensity + "," +
		    font_intensity + "," +
		    font_intensity + ")" );

    }
    
}
