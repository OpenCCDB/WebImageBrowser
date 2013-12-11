package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin;

import com.google.gwt.webworker.client.ErrorEvent;
import com.google.gwt.webworker.client.ErrorHandler;
import com.google.gwt.webworker.client.MessageEvent;
import com.google.gwt.webworker.client.MessageHandler;
import com.google.gwt.webworker.client.Worker;
import edu.ucsd.ncmir.WIB.SLASHWorker.client.SLASHWorkerAnnotation;
import edu.ucsd.ncmir.WIB.SLASHWorker.client.SLASHWorkerPointData;
import edu.ucsd.ncmir.WIB.SLASHWorker.client.SLASHWorkerPointDataList;
import edu.ucsd.ncmir.WIB.SLASHWorker.client.SLASHWorkerReturnData;
import edu.ucsd.ncmir.WIB.SLASHWorker.worker.SLASHWorker;
import edu.ucsd.ncmir.WIB.client.core.Configuration;
import edu.ucsd.ncmir.WIB.client.debug.Debug;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.ProcessContoursMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.mode.SLASHDatabaseMode.ElementMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

/**
 *
 * @author spl
 */
public class SLASHWorkerManager
    implements ErrorHandler,
	       MessageHandler

{

    private final HashMap<String,Worker> _worker_table =
	new HashMap<String,Worker>();

    private final LinkedList<Worker> _worker_queue = new LinkedList<Worker>();

    public SLASHWorkerManager()

    {
    	
    	System.err.println("----------------SLASHWorkerManager----------------");

	for ( int i = 0; i < 4; i++ ) {

	    Worker w = Worker.createNamedWorker( SLASHWorker.NAME );
	    w.setOnError( this );
	    w.setOnMessage( this );
	    this._worker_queue.add( w );

	}
	System.err.println("-------Done---------SLASHWorkerManager----------------");

    }

    private HashMap<String,ElementMap> _element_map_table =
	new HashMap<String,ElementMap>();

    @Override
    public void onMessage( MessageEvent event )

    {

	try {

	    SLASHWorkerReturnData ccird =
		( SLASHWorkerReturnData ) event.getDataAsJSO();

	    if ( ccird.getType() == 1 )
		Debug.message( "Internal SLASH Worker Error:",
			       ccird.getStatus() );
	    else if ( ccird.getType() == 0 ) {

		String url = ccird.getURL();
		ElementMap em = this._element_map_table.get( url );

		SLASHWorkerPointDataList pdl =
		    ccird.getPointDataList();

                int model_id =
                    Integer.parseInt( Configuration.parameter( "modelID" ) );
		for ( int i = 0; i < pdl.length(); i++ ) {

		    SLASHWorkerPointData pd = pdl.get( i );

		    SLASHWorkerAnnotation a = pd.getAnnotation();

		    int geometry_id = a.getGeometryID();

		    a.setModelID( model_id );
		    a.setObjectName( em.getObjectName( geometry_id ) );
		    a.setUserName( em.getUserName( geometry_id ) );
		    a.setRGB( em.getColor( geometry_id ) );
		    if ( em.getGeometryType( geometry_id ).equals( "polygon" ) )
			a.closed();

		}
		this._element_map_table.remove( url );
		new ProcessContoursMessage().send( ccird );

		Worker w = this._worker_table.get( url );
		this._worker_table.remove( url );
		this._worker_queue.add( w );

		this.dispatch();

	    }

	} catch ( ClassCastException cce ) {

	    Debug.message( "Unexpected return from SLASHWorker:",
			   event.getDataAsString() );

	}

    }

    private Stack<String> _url_stack = new Stack<String>();

    public void request( String url, ElementMap element_map )

    {

	this._url_stack.push( url );
	this._element_map_table.put( url, element_map );

	this.dispatch();

    }

    private void dispatch()

    {

	while ( ( this._worker_queue.size() > 0 ) &&
		( this._url_stack.size() > 0 ) ) {

	    Worker w = this._worker_queue.poll();

	    String url = this._url_stack.pop();

	    this._worker_table.put( url, w );
	    w.postMessage( url );

	}

    }

    @Override
    public void onError( ErrorEvent event )

    {

	Debug.message( "Message:", event.getMessage() );
	Debug.message( "LineNumber:", event.getLineNumber() );
	Debug.message( "Filename:", event.getFilename() );
	Debug.message( "Error!" );

    }

}
