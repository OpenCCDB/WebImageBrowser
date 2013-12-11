package edu.ucsd.ncmir.WIB.client.core.components;

import com.google.gwt.event.shared.EventHandler;

/**
 *
 * @author spl
 */
public interface SliderValueUpdateHandler
    extends EventHandler

{

    /**
     * Called when the slider changes value.
     * @param event
     */
    public void onBarValueChanged( SliderValueUpdateEvent event );

}
