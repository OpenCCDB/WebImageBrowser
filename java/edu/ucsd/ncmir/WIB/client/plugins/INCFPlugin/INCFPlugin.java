package edu.ucsd.ncmir.WIB.client.plugins.INCFPlugin;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import edu.ucsd.ncmir.WIB.client.WIB;
import edu.ucsd.ncmir.WIB.client.core.Configuration;
import edu.ucsd.ncmir.WIB.client.core.components.InfoDialog;
import edu.ucsd.ncmir.WIB.client.core.components.menu.AbstractRadioMenuItem;
import edu.ucsd.ncmir.WIB.client.core.components.menu.WIBMenuBar;
import edu.ucsd.ncmir.WIB.client.core.drawable.Drawable;
import edu.ucsd.ncmir.WIB.client.core.drawable.Point;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.messages.ClearTransientVectorOverlayMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.GetDisplayListMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.InformationMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.MousePositionMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.ParameterUpdateMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.RenderRequestMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.RenderTransientVectorOverlayMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.RequestPopupMenuMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.SetQuaternionMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.SetTransientVectorOverlayLineWidthMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.StatusMessage;
import edu.ucsd.ncmir.WIB.client.core.request.AbstractXMLRequestCallback;
import edu.ucsd.ncmir.WIB.client.core.request.HTTPRequest;
import edu.ucsd.ncmir.WIB.client.debug.Debug;
import edu.ucsd.ncmir.WIB.client.plugins.DefaultPlugin.DefaultPlugin;
import edu.ucsd.ncmir.WIB.client.plugins.INCFPlugin.messages.QueryMessage;
import edu.ucsd.ncmir.spl.Graphics.Color;
import edu.ucsd.ncmir.spl.LinearAlgebra.AbstractDoubleVector;
import edu.ucsd.ncmir.spl.LinearAlgebra.DoubleVector;
import edu.ucsd.ncmir.spl.LinearAlgebra.Quaternion;
import edu.ucsd.ncmir.spl.XMLUtil.XML;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author spl
 */
public class INCFPlugin
    extends DefaultPlugin

{

    private String _uri;
    private final DatatipTimer _datatip_timer;
    private Request _request;

    /**
     * Instantiates the plugin.
     */
    public INCFPlugin()
    {

        super( CommandMessage.class,
               GetDisplayListMessage.class,
               MousePositionMessage.class,
               ParameterUpdateMessage.class,
               QueryMessage.class,
               SetViewMessage.class );

        this._datatip_timer = new DatatipTimer( this );

    }

    @Override
    public void action( Message m, Object o )
    {

        if ( m instanceof GetDisplayListMessage )
            this.getDisplayList( ( GetDisplayListMessage ) m );
        else if ( m instanceof SetViewMessage )
            this.setView( ( String ) o );
        else if ( m instanceof ParameterUpdateMessage )
            this.parameterUpdate( ( ParameterUpdateMessage ) m );
        else if ( m instanceof MousePositionMessage )
            this.mousePositionUpdate( ( Point ) o );
        else if ( m instanceof QueryMessage )
            this.handleQuery( ( Point ) o );
        else if ( m instanceof CommandMessage )
            this.issueQuery( ( String ) o );

    }

    @Override
    public void setupPlugin()

    {

        String atlas = Configuration.parameter( "atlas" );
        String mode = Configuration.parameter( "atlas_mode" );

        if ( mode == null )
            mode = "default";

        HTTPRequest.get( "cgi-bin/atlas_info.pl?atlas=" + atlas +
			 "&mode=" + mode,
			 new AtlasInfoHandler( this ) );
        HTTPRequest.get( "cgi-bin/hubagent.pl?action=capabilities",
			 new CapabilitiesHandler( this ) );

    }

    /**
     * Called by the
     * <code>WIB</code> module upon loading.
     *
     * @param menu_bar The top-level <code>MenuBar</code> object to be added to.
     */
    @Override
    protected void configureMenuBar( MenuBar menu_bar )
    {

        super.configureMenuBar( menu_bar ); // Need to add some stuff here.

        menu_bar.addItem( "Actions", new INCFActionsMenu() );
        menu_bar.addItem( "Views", new ViewsMenu(  INCFPlugin._view_option ) );

    }

    private static class AtlasInfoHandler
        extends AbstractXMLRequestCallback

    {

        private INCFPlugin _plugin;

        AtlasInfoHandler( INCFPlugin plugin )
        {

            this._plugin = plugin;

        }

        @Override
        public void handleXMLResponse( Element root )
        {

            if ( root.getAttribute( "status" ).equals( "success" ) ) {

                Element path = XML.find( root, "path" );
                SRSTransform transform =
		    new SRSTransform( XML.find( root, "transform" ) );

                this._plugin.startup( path.getAttribute( "name" ),
                                      transform );

            }

        }

    }

    private static class CapabilitiesHandler
        extends AbstractXMLRequestCallback
    {

        private INCFPlugin _plugin;

        CapabilitiesHandler( INCFPlugin plugin )
        {

            this._plugin = plugin;

        }

        @Override
        public void handleXMLResponse( Element root )
        {

            if ( root.getAttribute( "status" ).equals( "success" ) )
                this._plugin.buildCapabilitiesMenu( root );

        }

    }
    private SRSTransform _transform;

    private void startup( String uri, SRSTransform transform )
    {

        this._uri = uri;
        this._transform = transform;

        try {

            this.initializeView( Configuration.parameter( "view" ) );

        } catch ( Exception e ) {

            Debug.traceback( e );

        }

    }

    private Point _mouse_position = null;

    private void mousePositionUpdate( Point point )
    {

        if ( this._mouse_position == null )
            new InformationMessage().send( "" );

        this._datatip_timer.cancel();
        this._datatip_timer.schedule( 250 );
        this._mouse_position = point;

    }

    private WIBMenuBar _popup_menu;
    private Point _where = null;
    private boolean _in_query = false;
    private boolean _busy = false;

    private void handleQuery( Point point )
    {

        if ( !this._in_query ) {

            this._in_query = true;
            this._where = point;

            new RequestPopupMenuMessage( this._popup_menu, this._where ).send();
            new SetTransientVectorOverlayLineWidthMessage().send( 2 );
            new RenderTransientVectorOverlayMessage().send( new Drawable[] {
                    new Star( this._where.getX(),
                              this._where.getY(),
                              5.0 )
                } );

        } else if ( !this._busy )
	    this.clearQuery();

    }

    private void clearQuery()

    {

	this._busy = false;
	this._in_query = false;

	new RequestPopupMenuMessage( this._popup_menu, null ).send();
	new ClearTransientVectorOverlayMessage().send();

    }

    private int _width;
    private int _height;
    private int _depth;
    private AbstractDoubleVector _query_location;
    private Timeout _timeout;

    private void issueQuery( String query )
    {

	this._busy = true;
        new RequestPopupMenuMessage( this._popup_menu, null ).send();

	this._query_location =
	    this.computeViewTransform( this._where.getX(),
				       this._where.getY(),
				       this._plane  );

        String[] q = query.split( ";" );
        String url =
	    "cgi-bin/hubagent.pl?action=request" +
            "&hub=" + q[1] +
            "&term=" + q[2] +
	    "&srsName=" + this._transform.getSRSName() +
            "&x=" + this._query_location.getComponent( 0 ) +
            "&y=" + this._query_location.getComponent( 1 ) +
            "&z=" + this._query_location.getComponent( 2 );

        this._request = HTTPRequest.get( url, new QueryHandler( this ) );
	this._timeout = new Timeout( this );

    }

    private static class Timeout
	extends Timer

    {

	private final INCFPlugin _plugin;

	Timeout( INCFPlugin plugin )

	{

	    this._plugin = plugin;
            this.schedule( WIB.getTimeoutSeconds() * 1000 );

	}

        @Override
	public void run()

	{

	    boolean ok =
		Window.confirm( "Operation timed out.\n" +
				"Do you want to wait?\n\n" +
				"Click \"OK\" to continue waiting.\n" +
				"Click \"Cancel\" to terminate." );

	    if ( ok )
		this.schedule( WIB.getTimeoutSeconds() * 1000 );
	    else
		this._plugin.cancelQuery();

	}

    }

    private void cancelQuery()

    {

	this._request.cancel();
	this.clearQuery();

    }

    private static class QueryHandler
        extends AbstractXMLRequestCallback
    {

        private INCFPlugin _plugin;

        QueryHandler( INCFPlugin plugin )
        {

            this._plugin = plugin;

        }

        @Override
        protected void handleXMLResponse( Element root )
        {

	    this._plugin.handleQueryResponse( root );

	}

        @Override
	protected void handleError( Throwable exception )

	{

	    this._plugin.handleQueryResponse( null );
	    Debug.traceback( exception );

	}

    }

    private void handleQueryResponse( Element root )

    {

	this._busy = false;
	this._timeout.cancel();

	if ( root != null ) {

	    if ( root.getAttribute( "status" ).equals( "success" ) ) {

		Element data = XML.find( root, "data" );

		if ( data != null ) {

		    String usage = data.getAttribute( "usage" );

		    if ( "link".equals( usage ) )
			this.openLink( data );
		    else if ( "display".equals( usage ) )
			this.display( root );
		    else if ( "WIB".equals( usage ) )
			this.WIB( root );
		    else if ( "view".equals( usage ) )
			this.WIB( root );

		} else {

		    InfoDialog id = new InfoDialog();

		    id.addTitle( "Sorry. . ." );
		    id.addInfoPair( "Error", "Nothing found." );
		    id.displayCenteredMessage();

		    this.clearQuery();

		}

	    } else {

		InfoDialog id = new InfoDialog();

		id.addTitle( "Sorry. . ." );
		id.addInfoPair( "Error", root.getAttribute( "reason" ) );
		id.displayCenteredMessage();

		this.clearQuery();

	    }

	}  else {

	    InfoDialog id = new InfoDialog();

	    id.addTitle( "Sorry. . ." );
	    id.addInfoPair( "Error", "Null response from server!" );
	    id.displayCenteredMessage();

	    this.clearQuery();

	}

    }

    private void openLink( Element data )

    {

        String link = XML.extractTextFromElement( data );

        Window.open( link, "_blank", "" );

    }

    private void display( Element root )

    {

        InfoDialog id = new InfoDialog();

        id.addTitle( "Query Result" );

	String x = this.truncate( this._query_location.getComponent( 0 ), 2 );
	String y = this.truncate( this._query_location.getComponent( 1 ), 2 );
	String z = this.truncate( this._query_location.getComponent( 2 ), 2 );

	id.addInfoPair( "Display Location",
			"x: " + this.truncate( this._where.getX(), 1 ) +
			", y: " + this.truncate( this._where.getY(), 1 ) +
			", plane: " + this._plane );
	id.addInfoPair( "SRS Name",
                        this._transform.getSRSName() );
	id.addInfoPair( "Atlas Location",
                        "X: " + x + ", Y: " + y + ", Z: " + z );

        NodeList nl = root.getElementsByTagName( "data" );
        for ( int i = 0; i < nl.getLength(); i++ ) {

            Element data = ( Element ) nl.item( i );

            String label = data.getAttribute( "label" );
            String value = XML.extractTextFromElement( data );

            id.addInfoPair( label, value );

        }

        id.displayCenteredMessage();

    }

    private String truncate( double value, int digits )

    {

	double factor = Math.pow( 10, digits );
	value = Math.round( value * factor ) / factor;

	String[] v = ( value + "" ).split( "\\." );

	String vout;
	if ( v.length == 2 ) {

	    if ( v[1].length() < digits )
		v[1] = "0" + v[1];
	    else if ( v[1].length() > digits )
		v[1] = v[1].substring( 0, 2 );

	    vout = v[0] + "." + v[1];

	} else if ( Math.abs( value ) < 1 )
	    vout = "0." + v[0];
	else {

	    vout = v[0] + ".";
	    for ( int i = 0; i < digits; i++ )
		vout += "0";

	}

	return vout;

    }

    private void WIB( Element root )

    {

	this._request = HTTPRequest.post( "cgi-bin/wibagent.pl?action=build",
					  root.toString(),
					  new LaunchNewWIBHandler( this ) );
	this._timeout = new Timeout( this );

    }

    private static class LaunchNewWIBHandler
        extends AbstractXMLRequestCallback

    {

        private INCFPlugin _plugin;

        LaunchNewWIBHandler( INCFPlugin plugin )
        {

            this._plugin = plugin;

        }

        @Override
        public void handleXMLResponse( Element root )
        {

	    this._plugin.launchNewWIB( root );

	}

        @Override
	public void handleError( Throwable t )

	{

	    Debug.traceback( t );
	    Debug.message( "Fail!" );

	}

    }

    private void launchNewWIB( Element root )

    {

	String link = XML.extractTextFromElement( root );

        Window.open( link, "_blank", "" );
	this._request.cancel();

    }

    private AbstractDoubleVector computeViewTransform( double x,
						       double y,
						       double z )

    {

        double[] txyz = this.xyzTransform( x, y, z );

        DoubleVector vin =
	    new DoubleVector( new double[]{ txyz[0], txyz[1], txyz[2], 1 } );
        return this._transform.getMatrix().multiply( vin );

    }

    private double[] xyzTransform( double x, double y, double z )
    {

        x -= this._x0t;
        y -= this._y0t;
        z -= this._z0t;

        AbstractDoubleVector vout =
	    this._quaternion.conjugate().transformVector( x, y, z );

        return new double[]{
                vout.getComponent( 0 ) + this._x1t,
                vout.getComponent( 1 ) + this._y1t,
                vout.getComponent( 2 ) + this._z1t
            };

    }

    private void buildCapabilitiesMenu( Element root )
    {

        this._popup_menu = this.buildMenu( "", root );

    }

    private WIBMenuBar buildMenu( String parent, Element root )
    {

        NodeList list = root.getChildNodes();

        String pname = root.getAttribute( "name" );
        if ( pname == null )
            pname = "";
        pname = parent + pname + ";";
        WIBMenuBar menu = new WIBMenuBar( true );
        menu.setAutoOpen( true );
        for ( int i = 0; i < list.getLength(); i++ )
            if ( list.item( i ).getNodeType() == Node.ELEMENT_NODE ) {

                Element e = ( Element ) list.item( i );

                String has_submenu = e.getAttribute( "has_submenu" );
                String name = e.getAttribute( "name" );

                if ( has_submenu.equals( "true" ) )
                    menu.addSubmenu( name,
                                     this.buildMenu( pname, e ) );
                else
                    menu.addCommandItem( name,
                                         new CommandMessage( pname + name ) );

            }

        return menu;

    }

    private static class ViewsMenu
        extends WIBMenuBar

    {

        public ViewsMenu( ViewOption view_option )

        {

            super( true );

	    this.addItem( view_option.get( "Horizontal" ).getRadioMenuItem() );
	    this.addItem( view_option.get( "Coronal" ).getRadioMenuItem() );
	    this.addItem( view_option.get( "Sagittal" ).getRadioMenuItem() );

        }

    }

    private class CommandMessage
        extends Message
        implements Scheduler.ScheduledCommand
    {

        private final String _name;

        private CommandMessage( String name )
        {

            this._name = name;

        }

        @Override
        public void execute()
        {

            super.send( this._name );

        }

    }

    private void datatip()
    {

        String datatip = "";

        ArrayList<Overlay> overlay = this._points.get( this._url );

        if ( overlay != null ) {

            ArrayList<Insider> insiders = new ArrayList<Insider>();

            double x = this._mouse_position.getX();
            double y = this._mouse_position.getY();

            for ( Overlay o : overlay )
                if ( o.pointInPolygon( x, y ) )
                    insiders.add( new Insider( o, o.distanceTo( x, y ) ) );

            if ( insiders.size() > 0 ) {

                Insider[] insider_list = insiders.toArray( new Insider[0] );
                Arrays.sort( insider_list );

                String name = insider_list[0].getName();
                datatip = "Object: " + name;

                HashSet<String> already = new HashSet<String>();

                already.add( name );

                for ( int i = 1; i < insider_list.length; i++ ) {

                    name = insider_list[i].getName();

                    if ( already.add( name ) )
                        datatip += " > " + name;

                }

            }

        }
        this._mouse_position = null;
        new InformationMessage().send( datatip );

    }

    private class Insider
        implements Comparable<Insider>
    {

        private final String _name;
        private final double _d;

        Insider( Overlay overlay, double d )
        {

            this._name = overlay.getObjectName();
            this._d = d;

        }

        String getName()
        {

            return this._name;

        }

        @Override
        public int compareTo( Insider o )
        {

            double d = this._d - o._d;

            return ( d < 0 ) ? -1 : ( ( d > 0 ) ? 1 : 0 );

        }

    }

    private static class DatatipTimer
        extends Timer
    {

        private INCFPlugin _plugin;

        DatatipTimer( INCFPlugin plugin )
        {

            this._plugin = plugin;

        }

        @Override
        public void run()
        {

            this._plugin.datatip();

        }

    }

    private void setView( String v )

    {

        this.initializeView( v );

    }

    private String _view = "Horizontal";
    private int _plane = 0;
    private Quaternion _quaternion = new Quaternion( 1, 0, 0, 0 );
    private String _url;
    private HashMap<String, ArrayList<Overlay>> _points =
	new HashMap<String, ArrayList<Overlay>>();

    private void parameterUpdate( ParameterUpdateMessage pum )
    {

	int plane = pum.getPlane();

	if ( plane != this._plane )
	    this.clearQuery();

        this._plane = plane;

        if ( Cookies.isCookieEnabled() ) {

            String cookie_prefix = Configuration.getCookiePrefix();

            Cookies.setCookie( cookie_prefix + ".plane." + this._view,
                               this._plane + "" );

        }

        this._url = "cgi-bin/niicontour.pl?atlas=" +
            Configuration.parameter( "atlas" ) +
            this._quaternion.getFormatted( "&qr=%g&qi=%g&qj=%g&qk=%g" ) +
            "&z=" + this._plane;

        if ( this._points.get( this._url ) == null ) {

            this._points.put( this._url, new ArrayList<Overlay>() );
            HTTPRequest.get( this._url, new ContourHandler( this, this._url ) );

        }

    }

    private void getDisplayList( GetDisplayListMessage gdlm )
    {

        ArrayList<Overlay> plist = this._points.get( this._url );

        if ( plist != null )
            for ( Overlay overlay : plist )
                gdlm.addPoints( overlay );

    }

    private double _x0t;
    private double _y0t;
    private double _z0t;
    private double _x1t;
    private double _y1t;
    private double _z1t;

    private void contourHandler( String url, Element root )

    {

        if ( root.getAttribute( "status" ).equals( "success" ) ) {

            NodeList objects = root.getElementsByTagName( "object" );
            Element dimensions = XML.find( root, "dimensions" );

            this._width =
                Integer.parseInt( dimensions.getAttribute( "width" ) );
            this._height =
                Integer.parseInt( dimensions.getAttribute( "height" ) );
            this._depth =
                Integer.parseInt( dimensions.getAttribute( "depth" ) );

            this._x1t = ( this._width - 1 ) / 2.0;
            this._y1t = ( this._height - 1 ) / 2.0;
            this._z1t = ( this._depth - 1 ) / 2.0;

            this._x0t = this._y0t = this._z0t = -Double.MAX_VALUE;

            Quaternion iq = this._quaternion;

            for ( double zm = -1; zm <= 1; zm += 2 )
                for ( double ym = -1; ym <= 1; ym += 2 )
                    for ( double xm = -1; xm <= 1; xm += 2 ) {

                        AbstractDoubleVector vout =
                            iq.transformVector( xm * this._x1t,
                                                ym * this._y1t,
                                                zm * this._z1t );

                        double x = vout.getComponent( 0 );
                        double y = vout.getComponent( 1 );
                        double z = vout.getComponent( 2 );

                        if ( this._x0t < x )
                            this._x0t = x;
                        if ( this._y0t < y )
                            this._y0t = y;
                        if ( this._z0t < z )
                            this._z0t = z;

                    }

            ArrayList<Overlay> point_list = new ArrayList<Overlay>();
            for ( int i = 0; i < objects.getLength(); i++ ) {

                Element object = ( Element ) objects.item( i );
                String name =
                    object.getAttribute( "name" ).replaceAll( "_", " " );
                int rgb = Color.parseRGB( object.getAttribute( "color" ) );
                NodeList contours =
                    object.getElementsByTagName( "contour" );

                for ( int c = 0; c < contours.getLength(); c++ ) {

                    Overlay overlay = new Overlay();

                    overlay.setObjectName( name );
                    overlay.setRGB( rgb );
                    Element contour = ( Element ) contours.item( c );
                    overlay.addPointsFromXML( contour );
                    overlay.close();

                    point_list.add( overlay );

                }

            }

            this._points.put( url, point_list );
            if ( url.equals( this._url ) )
                new RenderRequestMessage().send();

        }

    }

    private static class ContourHandler
        extends AbstractXMLRequestCallback

    {

        private final INCFPlugin _incf_plugin;
        private final String _url;

        private ContourHandler( INCFPlugin incf_plugin, String url )

        {

            this._incf_plugin = incf_plugin;
            this._url = url;

        }

        @Override
        public void handleXMLResponse( Element root )

        {

            this._incf_plugin.contourHandler( this._url, root );

        }

    }

    private void initializeView( String view )

    {

	this._view = INCFPlugin._view_term.get( view.toLowerCase() );

	this.clearQuery();
        ViewData data = INCFPlugin._view_option.get( this._view );

	new StatusMessage( "View:", this._view ).send();

        data.getRadioMenuItem().setSelected( true );

        this._quaternion = data.getQuaternion();
	new SetQuaternionMessage().send( this._quaternion );
        if ( Cookies.isCookieEnabled() ) {

            String cookie_prefix = Configuration.getCookiePrefix();

            Cookies.setCookie( cookie_prefix + ".quaternion",
                               this._quaternion.toString() );

            String plane_string =
                Cookies.getCookie( cookie_prefix + ".plane." + this._view );

            if ( plane_string == null ) {

                String parm_view = Configuration.parameter( "view" );

                if ( parm_view.equals( this._view ) )
                    plane_string = Configuration.parameter( "offset" );

            }
            if ( plane_string != null )
                Cookies.setCookie( cookie_prefix + ".plane", plane_string );
            else
                Cookies.removeCookie( cookie_prefix + ".plane" );

        }

        if ( this._uri != null )
            super.initializeDimensions( this._quaternion );

    }

    @Override
    public String getTransactionURL()

    {

        return "cgi-bin/Loader.pl?" +
            "type=volume&" +
            "uri=" + this._uri + "&" +
            "transaction=";

    }

    private static class SetViewMessage extends Message {}

    private static class ViewOptionRadioMenuItem
        extends AbstractRadioMenuItem
    {

        private static class NoOpMessage extends Message {}

        protected ViewOptionRadioMenuItem( String label )

        {

            super( label, new SendMessageFactory( label ) );

        }

        private static class SendMessageFactory
            implements Scheduler.ScheduledCommand

        {

            private final String _name;

            SendMessageFactory( String name )
            {

                this._name = name;

            }

            @Override
            public void execute()
            {

                new SetViewMessage().send( this._name );

            }

        }
        private static AbstractRadioMenuItem _selected;

        @Override
        protected AbstractRadioMenuItem getSelectedItem()
        {

            return ViewOptionRadioMenuItem._selected;

        }

        @Override
        protected void setSelectedItem( AbstractRadioMenuItem selected )
        {

            ViewOptionRadioMenuItem._selected = selected;

        }

    }

    @Override
    public String getState()
    {

        return "No useful state yet.";

    }

    private static ViewTerm _view_term = new ViewTerm();
    private static ViewOption _view_option =
	new ViewOption( INCFPlugin._view_term );

    static {

        INCFPlugin._view_term.define( "xy", "Horizontal" );
        INCFPlugin._view_term.define( "xz", "Coronal" );
        INCFPlugin._view_term.define( "yz", "Sagittal" );

        INCFPlugin._view_option.map( "Horizontal",
                                     new Quaternion( 1,
                                                     0,
                                                     0,
                                                     0 ) );
        INCFPlugin._view_option.map( "Coronal",
                                     new Quaternion( Math.sqrt( 2 ) / 2,
                                                     Math.sqrt( 2 ) / 2,
                                                     0,
                                                     0 ) );
        INCFPlugin._view_option.map( "Sagittal",
                                     new Quaternion( 0.5,
                                                     0.5,
                                                     0.5,
                                                     -0.5 ) );
    }

    private static class ViewTerm
        extends HashMap<String,String>

    {

        void define( String s1, String s2 )

        {

            super.put( s1, s2 );
            super.put( s1.toLowerCase(), s2 );
	    super.put( s2.toLowerCase(), s2 );

        }

    }

    private static class ViewOption
        extends HashMap<String,ViewData>
    {

        private final ViewTerm _view_term;

        ViewOption( ViewTerm view_term )

        {

            this._view_term = view_term;

        }

        void map( String name, Quaternion q )

        {

            ViewData data =
		new ViewData( new ViewOptionRadioMenuItem( name ), q );

            super.put( name, data );
            super.put( this._view_term.get( name ), data );

        }

    }

    private static class ViewData
    {

        private final ViewOptionRadioMenuItem _button;
        private final Quaternion _q;

        ViewData( ViewOptionRadioMenuItem button, Quaternion q )
        {

            this._button = button;
            this._q = q;

        }

        /**
         * @return the button
         */
        public ViewOptionRadioMenuItem getRadioMenuItem()
        {

            return this._button;

        }

        /**
         * @return the _q
         */
        public Quaternion getQuaternion()
        {

            return this._q;

        }

    }

}
