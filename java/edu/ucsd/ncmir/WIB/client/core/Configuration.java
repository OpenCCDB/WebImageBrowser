package edu.ucsd.ncmir.WIB.client.core;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.Window;
import edu.ucsd.ncmir.WIB.client.debug.Debug;
import edu.ucsd.ncmir.WIB.client.core.exception.ConfigurationException;
import edu.ucsd.ncmir.WIB.client.plugins.AbstractPlugin;
import edu.ucsd.ncmir.WIB.client.plugins.AnnotationPlugin.AnnotationPlugin;
import edu.ucsd.ncmir.WIB.client.plugins.DefaultPlugin.DefaultPlugin;
import edu.ucsd.ncmir.WIB.client.plugins.INCFPlugin.INCFPlugin;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.SLASHPlugin;
import edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin.WarpPlugin;
import edu.ucsd.ncmir.spl.Util.MD5;
import java.util.HashMap;

/**
 * Parses the argument string.
 * @author spl
 */
public class Configuration

{

    private Configuration() {}		// No instantiation.

    private static HashMap<String,String> _args = new HashMap<String,String>();

    /**
     * Query a parameter.
     *
     * @param key The parameter name.
     * @return The parameter value, if any.  If no parameter set for
     * the key, return <code>null</code>.
     */

    public static String parameter( String key )

    {

	return Configuration._args.get( key );

    }

    public static int intParameter( String key )

    {

	String p = Configuration._args.get( key );

	return ( p != null ) ? Integer.parseInt( p ) : Integer.MAX_VALUE;

    }

    public static double doubleParameter( String key )

    {

	String p = Configuration._args.get( key );

	return ( p != null ) ? Double.parseDouble( p ) : Double.MAX_VALUE;

    }

    static String _md5_string;

    public static String getCookiePrefix()

    {

	return Configuration._md5_string;

    }

    /**
     * Parses the QUERY_STRING passed from the URL.
     */

    static {

	try {

	    String arguments =
		Dictionary.getDictionary( "arguments" ).get( "query_string" );

	    MD5 md5 = new MD5();

	    md5.update( arguments );
	    md5.close();

	    Configuration._md5_string = md5.toHexString();

	    for ( String s : arguments.split( "&" ) ) {

		String[] t = s.split( "=" );

		if ( t.length != 2 )
		    throw new ConfigurationException( "Parameter '" + s +
						      "' is bogus" );
		Configuration._args.put( t[0], t[1] );

	    }

	    // This has grown in a rather ad hoc manner and probably
	    // should be somehow cleaned up.

	    if ( Configuration._args.get( "imageID" ) != null ) {

		Configuration._args.put( "mode", "image" );
		Configuration._args.put( "idtype", "zoomify" );
		Configuration._args.put( "uri",
					 Configuration._args.get( "imageID" ) );

	    } else if ( Configuration._args.get( "imagePath" ) != null ) {

		Configuration._args.put( "mode", "image" );
		Configuration._args.put( "idtype", "zoomify" );
		Configuration._args.put( "uri",
					 Configuration._args.get( "imagePath" ) );

	    } else if ( Configuration._args.get( "session" ) != null ) {

		Configuration._args.put( "mode", "session" );
		Configuration._args.put( "session",
					 Configuration._args.get( "session" ) );
		Configuration._args.put( "idtype", "session" );
		Configuration._args.put( "type",
					 Configuration._args.get( "typeID" ) );

	    } else if ( Configuration._args.get( "imageRegistrationID" ) != null ) {

		Configuration._args.put( "mode", "workflow" );
		Configuration._args.put( "idtype", "image_service" );
		Configuration._args.put( "image_registration_id",
					 Configuration._args.get( "imageRegistrationID" ) );

	    } else if ( Configuration._args.get( "datasetID" ) != null ) {

		Configuration._args.put( "mode", "db" );
		Configuration._args.put( "idtype", "volume" );
		Configuration._args.put( "datasetID",
					 Configuration._args.get( "datasetID" ) );
		String model_id = Configuration._args.get( "modelID" );
		Configuration._args.put( "modelID",
					 model_id == null ? "-1" :
					 model_id );

	    } else if ( Configuration._args.get( "flatID" ) != null ) {

		Configuration._args.put( "mode", "image" );
		Configuration._args.put( "idtype", "flat" );
		Configuration._args.put( "uri",
					 Configuration._args.get( "flatID" ) );
		if ( Configuration._args.get( "sensorbits" ) != null )
		    Configuration._args.put( "sensorbits",
					     Configuration._args.get( "sensorbits" ) );

	    } else if ( Configuration._args.get( "pffID" ) != null ) {

		Configuration._args.put( "mode", "image" );
		Configuration._args.put( "idtype", "pff" );
		Configuration._args.put( "uri",
					 Configuration._args.get( "pffID" ) );

	    } else if ( Configuration._args.get( "pnzID" ) != null ) {

		Configuration._args.put( "idtype", "pnz" );
		Configuration._args.put( "uri",
					 Configuration._args.get( "pnzID" ) );

		String plugin = Configuration._args.get( "plugin" );
		if ( ( plugin != null ) && plugin.equals( "INCF" ) ) {

		    Configuration._args.put( "mode", "incf2d" );

		    if ( Configuration._args.get( "use" ) != null )
			Configuration._args.put( "use_server",
						 Configuration._args.get( "use" ) );

		} else
		    Configuration._args.put( "mode", "image" );

	    } else if ( Configuration._args.get( "volumeID" ) != null ) {

		Configuration._args.put( "mode", "image" );
		Configuration._args.put( "idtype", "volume" );
		Configuration._args.put( "uri",
					 Configuration._args.get( "volumeID" ) );

	    } else if ( Configuration._args.get( "pyramidID" ) != null ) {

		Configuration._args.put( "mode", "image" );
		Configuration._args.put( "idtype", "pyramid" );
		Configuration._args.put( "uri",
					 Configuration._args.get( "pyramidID" ) );

	    } else if ( Configuration._args.get( "atlas" ) != null ) {

		Configuration._args.put( "idtype", "volume" );
		Configuration._args.put( "mode", "incf" );

	    } else if ( Configuration._args.get( "agentID" ) != null ) {

		Configuration._args.put( "idtype", "image_service" );
		Configuration._args.put( "mode", "agent" );

	    } else if ( Configuration._args.get( "omeraID" ) != null ) {

		Configuration._args.put( "idtype", "volume" );
		Configuration._args.put( "mode", "omera" );

	    } else if ( Configuration._args.get( "omeroID" ) != null ) {

		Configuration._args.put( "idtype", "volume" );
		Configuration._args.put( "mode", "omera" );

	    } else if ( Configuration._args.get( "simsID" ) != null ) {

		Configuration._args.put( "idtype", "volume" );
		Configuration._args.put( "mode", "SIMS" );
		Configuration._args.put( "uri",
					 Configuration._args.get( "simsID" ) );

	    } else if ( Configuration._args.get( "tandemID" ) != null ) {

		Configuration._args.put( "idtype", "flat" );
		Configuration._args.put( "mode", "tandem" );

		Configuration._args.put( "uri",
					 Configuration._args.get( "tandemID" ) );

	    }

	    if ( Configuration._args.get( "application" ) == null )
		Configuration._args.put( "application", "CCDB" );

	    String plugin_name = Configuration._args.get( "plugin" );

	    if ( plugin_name != null ) {

		if ( plugin_name.equals( "SLASH" ) )
		    Configuration._plugin = GWT.create( SLASHPlugin.class );
		else if ( plugin_name.equals( "INCF" ) )
		    Configuration._plugin = GWT.create( INCFPlugin.class );
		else if ( plugin_name.equals( "Warp" ) )
		    Configuration._plugin = GWT.create( WarpPlugin.class );
		else if ( plugin_name.equals( "Annotaton" ) )
		    Configuration._plugin = GWT.create( AnnotationPlugin.class );
		else
		    Window.alert( "Plugin name '" + plugin_name +
				  "' is unknown.  Assuming Default Plugin." );

	    } else
		Configuration._plugin = GWT.create( DefaultPlugin.class );

	} catch ( Exception e ) {

	    Debug.traceback( e );

	}

    }

    private static AbstractPlugin _plugin;

    /**
     * Returns a <code>AbstractPlugin</code> object.
     *
     * @return A <code>AbstractPlugin</code> object.
     */

    public static AbstractPlugin getPlugin()

    {

	return Configuration._plugin;

    }

}
