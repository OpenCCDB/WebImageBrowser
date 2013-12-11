package edu.ucsd.ncmir.WIB.client.plugins.DefaultPlugin.messages;

import edu.ucsd.ncmir.WIB.DefaultWorker.client.CanvasData;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.plugins.DefaultPlugin.DefaultTile;

/**
 * @author spl
 */
public class DefaultTileWorkerMessage
    extends Message

{

    private final DefaultTile _tile;
    private final CanvasData _canvas_data;

    public DefaultTileWorkerMessage( DefaultTile tile, CanvasData canvas_data )

    {

	this._tile = tile;
	this._canvas_data = canvas_data;

    }

    /**
     * @return The context
     */

    public DefaultTile getTile()

    {

        return this._tile;

    }

    /**
     * @return the _canvas_data
     */

    public CanvasData getCanvasData()

    {

        return this._canvas_data;

    }

}