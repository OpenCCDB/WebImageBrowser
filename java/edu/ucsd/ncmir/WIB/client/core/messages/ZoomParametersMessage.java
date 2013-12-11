package edu.ucsd.ncmir.WIB.client.core.messages;

import edu.ucsd.ncmir.WIB.client.core.message.Message;

/**
 *
 * @author spl
 */
public class ZoomParametersMessage
    extends Message

{

    private final double _max_zoom_out;
    private final double _default_zoom;
    private final double _max_zoom_in;
    private final double _delta;

    /**
     * Sent when the Zoom Parameters change.
     * @param max_zoom_out This is the zoom out factor.
     * @param default_zoom Default value for resets.
     */
    public ZoomParametersMessage( double max_zoom_out,
                                  double max_zoom_in,
				  double default_zoom,
				  double delta )

    {

        this._max_zoom_out = max_zoom_out;
        this._max_zoom_in = max_zoom_in;
        this._default_zoom = default_zoom;
        this._delta = delta;

    }

    /**
     * @return the max zoom
     */
    public double getMaxZoomOut()
    {

        return this._max_zoom_out;

    }

    /**
     * @return the max zoom
     */
    public double getMaxZoomIn()
    {

        return this._max_zoom_in;

    }

    /**
     * @return the delta
     */
    public double getZoomDelta()
    {

        return this._delta;

    }

    /**
     * @return the default zoom
     */
    public double getDefaultZoom()

    {

        return this._default_zoom;

    }

}
