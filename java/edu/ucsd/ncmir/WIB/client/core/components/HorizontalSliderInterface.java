package edu.ucsd.ncmir.WIB.client.core.components;

import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author spl
 */
public interface HorizontalSliderInterface

{

    void setSliderParameters( int min_value, int max_value, int default_value );
    
    double getSliderValue();

    void setSliderValue( double value );

    /**
     * Updates the value of the slider without firing the handler.
     * @param value The value to be set.
     */
    void setSliderValueOnly( int value );

    void setWidth( String size );
    
    void setHeight( String size );
    
    void updateHandler( double value );
    
    /**
     * 
     * Returns the <code>Widget</code> we created.
     * 
     * Yes, this is ugly and probably shouldn't be here but I'm being lazy
     * at the moment and don't want to spend the time to do it right.
     * 
     * Maybe I'll fix it before I evaporate.
     * @return The slider widget.
     */
    Widget widget();
    
}
