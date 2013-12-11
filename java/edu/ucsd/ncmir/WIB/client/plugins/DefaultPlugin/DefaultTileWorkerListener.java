package edu.ucsd.ncmir.WIB.client.plugins.DefaultPlugin;

import com.google.gwt.canvas.dom.client.ImageData;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.webworker.client.MessageEvent;
import com.google.gwt.webworker.client.MessageHandler;
import com.google.gwt.webworker.client.Worker;
import edu.ucsd.ncmir.WIB.DefaultWorker.client.CanvasData;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.message.MessageListener;
import edu.ucsd.ncmir.WIB.client.plugins.DefaultPlugin.messages.DefaultTileWorkerMessage;
import java.util.HashMap;

/**
 *
 * @author spl
 */
class DefaultTileWorkerListener
    implements MessageListener,
	       MessageHandler

{

    private final static String DW =
	"edu.ucsd.ncmir.WIB.DefaultWorker.DefaultWorker";

    private final Worker[] _w;
    private int _index = 0;
    private final HashMap<Integer,DefaultTile> _tile_map =
	new HashMap<Integer, DefaultTile>();

    DefaultTileWorkerListener()

    {

        this._w = new Worker[4];

        for ( int i = 0; i < this._w.length; i++ ) {

            this._w[i] = 
		Worker.createNamedWorker( DefaultTileWorkerListener.DW );
            this._w[i].setOnMessage( this );

        }

    }

    @Override
    public void onMessage( MessageEvent event )

    {

        CanvasData canvas_data = ( CanvasData ) event.getDataAsJSO();
        int index = canvas_data.getIndex();
        Integer idx = new Integer( index );
        ImageData image_data = canvas_data.getImageData();

        this._tile_map.get( idx ).updateImageData( image_data );
        this._tile_map.remove( idx );

    }

    @Override
    public void action( Message m, Object o )

    {
        DefaultTileWorkerMessage twm = ( DefaultTileWorkerMessage ) m;
        CanvasData canvas_data = twm.getCanvasData();
        DefaultTile tile = twm.getTile();

        this._tile_map.put( new Integer( this._index ), tile );

        canvas_data.setIndex( this._index );

        int wn = this._index % this._w.length;

        this._w[wn].postMessage( ( JavaScriptObject ) canvas_data );

        this._index++;

    }

}
