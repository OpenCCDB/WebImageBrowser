package edu.ucsd.ncmir.WIB.client.plugins.DefaultPlugin;

import com.google.gwt.user.client.Window;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import edu.ucsd.ncmir.WIB.client.core.Configuration;
import edu.ucsd.ncmir.WIB.client.core.image.ImageFactoryInterface;
import edu.ucsd.ncmir.WIB.client.core.menus.AbstractImageMenu;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.message.MessageListener;
import edu.ucsd.ncmir.WIB.client.core.message.MessageManager;
import edu.ucsd.ncmir.WIB.client.core.messages.DisplayInitMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.ImpededModeMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.ResetMessage;
import edu.ucsd.ncmir.WIB.client.core.panel.ControlsPanel;
import edu.ucsd.ncmir.WIB.client.core.request.AbstractXMLRequestCallback;
import edu.ucsd.ncmir.WIB.client.core.request.HTTPRequest;
import edu.ucsd.ncmir.WIB.client.debug.Debug;
import edu.ucsd.ncmir.WIB.client.plugins.AbstractPlugin;
import edu.ucsd.ncmir.spl.LinearAlgebra.Matrix2D;
import edu.ucsd.ncmir.spl.LinearAlgebra.Quaternion;

/**
 *
 * @author spl
 */
public class DefaultPlugin
    extends AbstractPlugin

{

    private final ImageFactoryInterface _image_factory = new DefaultImageFactory();

    /**
     * The Default plug-in.
     */
    public DefaultPlugin()

    {

        this( ( Class ) null );

    }

    public DefaultPlugin( Class... listeners )

    {

        super( listeners );
	MessageManager.registerListener( ImpededModeMessage.class,
					 new ImpededModeHandler( this ) );


    }

    @Override
    public String getTransactionURL()

    {

 	String uri = Configuration.parameter( "uri" );
	String type = Configuration.parameter( "idtype" );

	String url = "";
	if ( ( uri != null ) && ( type != null ) )
	    url = "cgi-bin/Loader.pl?" +
		"type=" + type + "&" +
		"uri=" + uri + "&" +
		"transaction=";

	return url;

    }

    @Override
    public ImageFactoryInterface getImageFactory()

    {

        return this._image_factory;

    }

    @Override
    public ControlsPanel getControlsPanel()

    {

        return new DefaultControlsPanel();

    }

    @Override
    public void setupPlugin()

    {

        this.initializeDimensions( new Quaternion() );

    }

    /**
     * To be called once Transaction URL is properly generated or defined.
     */
    @Override
    public final void initializeDimensions( Quaternion quaternion )

    {

	try {

	    String transact = this.getTransactionURL();

	    String dims = transact + "dimensions&reinit=false" +
		quaternion.getFormatted( "&qr=%g&qi=%g&qj=%g&qk=%g" );
	    HTTPRequest.get( dims, new InitializeHandler() );

	} catch ( Exception e ) {

	    Debug.traceback( e );

	}

    }

    private boolean _impeded_mode = false;

    private void setImpededMode( boolean impeded_mode )

    {

	this._impeded_mode = impeded_mode;

    }

    private static class InitializeHandler
        extends AbstractXMLRequestCallback

    {

        @Override
        public void handleXMLResponse( Element root )

        {

	    int width = 0;
	    int height = 0;
	    int depth = 0;
	    int tilesize = 0;
	    int timesteps = 0;
	    String atlas_name = null;
	    int page = -1;
	    Matrix2D matrix = null;
	    int red_map = 0;
	    int green_map = 0;
	    int blue_map = 0;

	    for ( Node n = root.getFirstChild();
		  n != null;
		  n = n.getNextSibling() )
		if ( n.getNodeType() == Node.ELEMENT_NODE ) {

		    Element e = ( Element ) n;
		    String tagname = e.getTagName();

		    if ( tagname.equals( "dimension" ) ) {

			width =
			    Integer.parseInt( e.getAttribute( "width" ) );
			height =
			    Integer.parseInt( e.getAttribute( "height" ) );
			depth =
			    Integer.parseInt( e.getAttribute( "depth" ) );
			if ( depth == 0 )
			    depth = 1;
			timesteps =
			    Integer.parseInt( e.getAttribute( "timesteps" ) );
			if ( timesteps == 0 )
			    timesteps = 1;
			tilesize =
			    Integer.parseInt( e.getAttribute( "tilesize" ) );

		    } else if ( tagname.equals( "rgborder" ) ) {

			red_map =
			    Integer.parseInt( e.getAttribute( "R" ) );
			green_map =
			    Integer.parseInt( e.getAttribute( "G" ) );
			blue_map =
			    Integer.parseInt( e.getAttribute( "B" ) );

		    }

		}

	    new DisplayInitMessage( width,
				    height,
				    depth,
				    tilesize,
				    timesteps,
				    atlas_name,
				    page,
				    matrix,
				    red_map,
				    green_map,
				    blue_map ).send();

	}

    }

    @Override
    public AbstractImageMenu getImageMenu()

    {

	return new DefaultImageMenu();

    }

    @Override
    public void action( Message m, Object o )

    {

        // Currently no <code>Message</code>s to be listened for.

    }

    @Override
    public double getMaxZoomIn()

    {

        return this._impeded_mode ? 0 : 1;

    }

    @Override
    public double getZoomDelta()

    {

        return this._impeded_mode ? 1 : Double.MAX_VALUE;

    }

    @Override
    public String getState()
    {

        return "Default plugin: no state right now!";

    }

    private static class ImpededModeHandler
	implements MessageListener

    {

	private final DefaultPlugin _plugin;

        public ImpededModeHandler( DefaultPlugin plugin )

        {

	    this._plugin = plugin;

        }

        @Override
        public void action( Message m, Object o )

        {

	    this._plugin.setImpededMode( true );
	    new ResetMessage().send();
	    Window.alert( "Warning\n\n" +
			  "Your browser is incompatible\n" +
			  "with certain features of\n" +
			  "Web Image Browser 3.0,\n\n" +
			  "Recent versions of Firefox,\n" +
			  "Google Chromium, Opera, or\n" +
			  "Safari are recommended instead." );

        }

    }

}
