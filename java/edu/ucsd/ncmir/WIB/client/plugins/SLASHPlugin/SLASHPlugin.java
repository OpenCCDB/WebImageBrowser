package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin;

import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.dialogs.UserNameDialog;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.dialogs.InterpolationDialog;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.dialogs.DeleteDialog;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.dialogs.AnnotationRetryDialog;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.dialogs.AnnotationDialog;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.dialogs.EditDialog;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayNumber;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.webworker.client.MessageEvent;
import com.google.gwt.webworker.client.MessageHandler;
import com.google.gwt.webworker.client.Worker;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Text;
import edu.ucsd.ncmir.WIB.SLASHInterpolator.client.SLASHInterpolatorData;
import edu.ucsd.ncmir.WIB.SLASHInterpolator.worker.SLASHInterpolator;
import edu.ucsd.ncmir.WIB.SLASHWorker.client.SLASHWorkerAnnotation;
import edu.ucsd.ncmir.WIB.SLASHWorker.client.SLASHWorkerPointData;
import edu.ucsd.ncmir.WIB.SLASHWorker.client.SLASHWorkerPointDataList;
import edu.ucsd.ncmir.WIB.SLASHWorker.client.SLASHWorkerReturnData;
import edu.ucsd.ncmir.WIB.client.core.Configuration;
import edu.ucsd.ncmir.WIB.client.core.KeyData;
import edu.ucsd.ncmir.WIB.client.core.PreferencesDialog;
import edu.ucsd.ncmir.WIB.client.core.TraceFactory;
import edu.ucsd.ncmir.WIB.client.core.components.AbstractInteractionMessageFactory;
import edu.ucsd.ncmir.WIB.client.core.components.mobile.MobileButtonBar;
import edu.ucsd.ncmir.WIB.client.core.drawable.Drawable;
import edu.ucsd.ncmir.WIB.client.core.drawable.Point;
import edu.ucsd.ncmir.WIB.client.core.image.ImageFactoryInterface;
import edu.ucsd.ncmir.WIB.client.core.menus.AbstractImageMenu;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.messages.ClearCursorOverlayMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.ClearTransientVectorOverlayMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.ContourDataMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.DisableDrawingMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.DisplayInitMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.EnableCursorOverlayMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.EnableDrawingMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.GetDisplayListMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.InformationMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.KeyPressMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.MousePositionMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.ParameterUpdateMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.RenderRequestMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.RenderTransientVectorOverlayMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.ResetMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.SetActionButtonListenerMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.SetCursorMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.SetDrawColorMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.SetInteractionFactoryMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.SetTraceFactoryMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.SetTransientVectorOverlayLineWidthMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.SetZoomBarDeltaMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.SetZoomBarMaxMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.StatusMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.ToggleToolboxStateMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.UpdateCursorOverlayMessage;
import edu.ucsd.ncmir.WIB.client.core.panel.ControlsPanel;
import edu.ucsd.ncmir.WIB.client.core.request.AbstractRequestCallback;
import edu.ucsd.ncmir.WIB.client.core.request.AbstractXMLRequestCallback;
import edu.ucsd.ncmir.WIB.client.core.request.HTTPRequest;
import edu.ucsd.ncmir.WIB.client.debug.Debug;
import edu.ucsd.ncmir.WIB.client.plugins.AbstractPlugin;
import edu.ucsd.ncmir.WIB.client.plugins.DefaultPlugin.DefaultImageFactory;
import edu.ucsd.ncmir.WIB.client.plugins.INCFPlugin.messages.AnnotationUpdateCompleteMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.dialogs.LivewireDialog;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.message_factory.AddPolylineMessageFactory;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.message_factory.EditMessageFactory;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.AddAnnotationMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.AddPolygonSetupMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.AddPolylineSetupMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.AddTraceSelectCompleteMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.AnnotationAcceptedMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.AnnotationDataMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.AnnotationEditRequestMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.AnnotationMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.AnnotationUpdateMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.AnnotationUpdatedMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.BumpNudgeRadiusMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.ChooseObjectMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.ClearEditSelectionMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.ContourEditCompleteMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.ContourEditMotionMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.ContourEditSetupMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.DeleteAnnotationMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.DeleteAnnotationVerifyMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.DeleteContourMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.DeleteObjectCompleteMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.DeleteObjectSetupMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.DeleteTraceCompleteMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.DeleteTraceSetupMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.DisableRedoMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.DisableUndoMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.DrawPolygonSetupMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.DrawPolylineSetupMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.EditModeSelectionMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.EnableRedoMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.EnableUndoMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.IdentifyCurrentObjectMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.InterpolationContourMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.InterpolationStatusMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.LivewireAddPolygonMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.LivewireAddPolylineMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.LivewireDrawPolygonMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.LivewireDrawPolylineMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.ObjectListReadyMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.ProcessContoursMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.RedoMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.RemoveGeometryMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.RevertToImageManagerMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.SLASHModeHandlerReadyMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.SelectAnnotationByIDMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.SelectContourMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.SelectInterpolationContourMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.SetCurrentAnnotationMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.SetEditModeMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.SetEditObjectLineTypeMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.SetLineTypeMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.SetNudgeRadiusMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.SetUserNameMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.SetVisibilityMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.ToggleView3DStateMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.UndoMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.UpdateAnnotationPlanesMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.ValidateEditSelectionMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.mobile.MobileObjectsButton;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.mobile.MobilePlanesButton;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.mobile.MobileSLASHToolsButton;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.mode.AbstractSLASHModeHandler;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.mode.SLASHDatabaseMode.SLASHdbModeHandler;
import edu.ucsd.ncmir.spl.LinearAlgebra.Quaternion;
import edu.ucsd.ncmir.spl.XMLUtil.XML;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

/**
 *
 * @author spl
 */
public class SLASHPlugin
    extends AbstractPlugin

{

    public enum EditMode {
	NONE,
        NUDGE,
        PULL,
        EXTEND
    }

    private static final int DEFAULT_EDIT_DISTANCE = 20;
    private static final int NEARNESS = 20;

    private AbstractSLASHModeHandler _mode_handler = null;
    private DatatipTimer _datatip_timer;
    private String _last_user_name;
    private int _nudge_radius = SLASHPlugin.DEFAULT_EDIT_DISTANCE;


    /**
     * Support for SLASH annotation and contouring.
     */
    public SLASHPlugin()

    {

        super( AddTraceSelectCompleteMessage.class,
               AddPolygonSetupMessage.class,
               AddPolylineSetupMessage.class,
               AnnotationDataMessage.class,
               AnnotationUpdateMessage.class,
               AnnotationEditRequestMessage.class,
	       ChooseObjectMessage.class,
               ClearEditSelectionMessage.class,
               ContourDataMessage.class,
               ContourEditCompleteMessage.class,
               ContourEditMotionMessage.class,
               ContourEditSetupMessage.class,
	       DeleteAnnotationVerifyMessage.class,
               DeleteContourMessage.class,
               DeleteObjectCompleteMessage.class,
               DeleteAnnotationMessage.class,
               DeleteObjectSetupMessage.class,
               DeleteTraceCompleteMessage.class,
               DeleteTraceSetupMessage.class,
               DrawPolygonSetupMessage.class,
               DrawPolylineSetupMessage.class,
               GetDisplayListMessage.class,
	       IdentifyCurrentObjectMessage.class,
               KeyPressMessage.class,
	       LivewireAddPolygonMessage.class,
	       LivewireAddPolylineMessage.class,
	       LivewireDrawPolygonMessage.class,
	       LivewireDrawPolylineMessage.class,
               MousePositionMessage.class,
               ObjectListReadyMessage.class,
               ParameterUpdateMessage.class,
               ProcessContoursMessage.class,
               RedoMessage.class,
               RemoveGeometryMessage.class,
               ResetMessage.class,
               RevertToImageManagerMessage.class,
               SelectContourMessage.class,
	       SelectInterpolationContourMessage.class,
               SetActionButtonListenerMessage.class,
	       SetCurrentAnnotationMessage.class,
	       SetEditModeMessage.class,
               SetInteractionFactoryMessage.class,
               SetLineTypeMessage.class,
               SetNudgeRadiusMessage.class,
	       SetUserNameMessage.class,
	       SetVisibilityMessage.class,
               SLASHModeHandlerReadyMessage.class,
               UndoMessage.class,
               ValidateEditSelectionMessage.class );

        this._datatip_timer = new DatatipTimer( this );
        String mode = Configuration.parameter( "mode" );

        if ( mode.equals( "db" ) )
            this._mode_handler = new SLASHdbModeHandler();

        this._image_factory = new SLASHImageFactory();
	this._last_user_name = Configuration.parameter( "user" );

	new InterpolationDialog( this );

    }

    @Override
    public void action( Message m, Object o )

    {

        if ( m instanceof ParameterUpdateMessage )
            this.setParameters( ( ParameterUpdateMessage ) m );
        else if ( m instanceof MousePositionMessage )
            this.updateDatatipTimer( ( Point ) o );
        else if ( m instanceof ContourDataMessage )
            this.addDrawingData( ( Annotation ) o );
        else if ( m instanceof GetDisplayListMessage )
            this.getDisplayList( ( GetDisplayListMessage ) m );
        else if ( m instanceof SLASHModeHandlerReadyMessage )
            this.getDimensions();
        else if ( m instanceof DrawPolygonSetupMessage )
            this.setupContourOperation( LineType.INITIAL_POLYGON );
        else if ( m instanceof DrawPolylineSetupMessage )
            this.setupContourOperation( LineType.INITIAL_POLYLINE );
        else if ( m instanceof AnnotationDataMessage )
            this.handleAnnotation( ( AnnotationDataMessage ) m );
        else if ( m instanceof AnnotationUpdateMessage )
            this.handleAnnotationUpdate( ( AnnotationUpdateMessage ) m );
        else if ( m instanceof DeleteObjectSetupMessage )
            this.initializeDeleteOperation();
        else if ( m instanceof DeleteObjectCompleteMessage )
            this.completeDeleteObjectOperation( ( Point ) o );
        else if ( m instanceof DeleteTraceCompleteMessage )
            this.completeDeleteTraceOperation( ( Point ) o );
        else if ( m instanceof AddTraceSelectCompleteMessage )
            this.selectTraceObject( ( Point ) o );
        else if ( m instanceof AddPolylineSetupMessage )
            this.initializeAddTraceOperation( ( Point ) o,
					      LineType.ADDED_POLYLINE );
        else if ( m instanceof AddPolygonSetupMessage )
            this.initializeAddTraceOperation( ( Point ) o,
					      LineType.ADDED_POLYGON );
        else if ( m instanceof SelectContourMessage )
            this.selectNewContourForEditing( ( Point ) o );
        else if ( m instanceof ContourEditSetupMessage )
            this.initializeEditOperation( ( Point ) o );
        else if ( m instanceof ContourEditMotionMessage )
            this.editOperation( ( Point ) o );
        else if ( m instanceof ContourEditCompleteMessage )
            this.completeEditOperation( ( Point ) o );
        else if ( m instanceof DeleteContourMessage )
            this.commitContourDeletion( ( ( Integer ) o ).intValue() );
        else if ( m instanceof DeleteAnnotationMessage )
            this.commitObjectDeletion( ( ( Integer ) o ).intValue() );
        else if ( m instanceof UndoMessage )
            this.undo();
        else if ( m instanceof RedoMessage )
            this.redo();
        else if ( m instanceof SetInteractionFactoryMessage )
            this.handleInteractionSetup( ( AbstractInteractionMessageFactory ) o );
        else if ( m instanceof SetNudgeRadiusMessage )
            this._nudge_radius = ( ( Double ) o ).intValue();
        else if ( m instanceof RemoveGeometryMessage )
            this.removeFromAnnotationTable( ( Annotation ) o );
        else if ( m instanceof ClearEditSelectionMessage )
            this.clearEditSelection();
        else if ( m instanceof ValidateEditSelectionMessage )
            this.validateEditSelection();
        else if ( m instanceof SetEditModeMessage )
            this.setEditMode( ( SLASHPlugin.EditMode ) o );
        else if ( m instanceof AnnotationEditRequestMessage )
            this.editAnnotation();
        else if ( m instanceof ResetMessage )
            this.reset();
        else if ( m instanceof RevertToImageManagerMessage )
            this.revertToImageManager();
        else if ( m instanceof ProcessContoursMessage )
            this.processContours( ( SLASHWorkerReturnData ) o );
        else if ( m instanceof KeyPressMessage )
            this.keyHandler( ( KeyData ) o );
	else if ( m instanceof ObjectListReadyMessage )
	    this.setupInteraction();
	else if ( m instanceof DeleteAnnotationVerifyMessage )
	    this.verifyDeleteAnnotation( ( SLASHAnnotationSpinnerInfo ) o );
	else if ( m instanceof SetCurrentAnnotationMessage )
	    this.selectCurrentAnnotation( ( SLASHAnnotationSpinnerInfo ) o );
	else if ( m instanceof SetUserNameMessage )
	    this._last_user_name = ( String ) o;
	else if ( m instanceof IdentifyCurrentObjectMessage )
	    this.identifyCurrentObject( ( SLASHAnnotationSpinnerInfo ) o );
	else if ( m instanceof ChooseObjectMessage )
	    this.chooseObject( ( Point ) o );
	else if ( m instanceof SelectInterpolationContourMessage )
	    this.chooseInterpolationContour( ( Point ) o );
	else if ( m instanceof SetLineTypeMessage )
	    this.setEditObjectLineType( ( Boolean ) o );
	else if ( m instanceof SetVisibilityMessage )
	    this.setVisibility( ( ( Integer ) o ).intValue() );
	else if ( m instanceof LivewireDrawPolygonMessage )
	    this.handleLivewireDrawPolygon( ( Annotation ) o );
	else if ( m instanceof LivewireDrawPolylineMessage )
	    this.handleLivewireDrawPolyline( ( Annotation ) o );
	else if ( m instanceof LivewireAddPolygonMessage )
	    this.handleLivewireAddPolygon( ( Annotation ) o );
	else if ( m instanceof LivewireAddPolylineMessage )
	    this.handleLivewireAddPolyline( ( Annotation ) o );

    }

    private void handleLivewireDrawPolygon( Annotation annotation )

    {

	this._line_type = LineType.INITIAL_POLYGON;
	this.addDrawingData( annotation );

    }

    private void handleLivewireDrawPolyline( Annotation annotation )

    {

	this._line_type = LineType.INITIAL_POLYLINE;
	this.addDrawingData( annotation );

    }

    private void handleLivewireAddPolygon( Annotation annotation )

    {

	this._line_type = LineType.ADDED_POLYGON;
	this.addDrawingData( annotation );

    }

    private void handleLivewireAddPolyline( Annotation annotation )

    {

	this._line_type = LineType.INITIAL_POLYLINE;
	this.addDrawingData( annotation );

    }

    private void setVisibility( int id )

    {

	switch ( id ) {

	case Integer.MIN_VALUE: { // Turn off everything.

	    this.setAllVisibility( false );
	    break;

	}
	case Integer.MAX_VALUE: { // Turn on everything.

	    this.setAllVisibility( true );
	    break;

	}
	default: {

	    boolean set = id < 0;

	    id = Math.abs( id );
	    this.setAllVisibility( !set );
	    this.setObjectVisibility( this._annotation_id_table.get( id ),
				      set );
	    break;

	}

	}

	new RenderRequestMessage().send();

    }

    private void setAllVisibility( boolean set )

    {

	for ( AnnotationList al : this._annotation_id_table.values() )
	    this.setObjectVisibility( al, set );

    }

    private void setObjectVisibility( AnnotationList al, boolean set )

    {

	if ( al != null )
	    for ( Annotation a : al )
		a.setVisible( set );

    }

    private void setupContourOperation( LineType line_type )

    {

        if ( ( line_type == LineType.INITIAL_POLYGON ) ||
	     ( line_type == LineType.INITIAL_POLYLINE ) )
	    new SetDrawColorMessage( 255, 0, 255 ).send();

        this._line_type = line_type;

    }

    @Override
    public void setupPlugin()

    {

        this._mode_handler.start();

        new SetDrawColorMessage( 255, 0, 255 ).send();
	new SetTraceFactoryMessage().send( new AnnotationFactory() );

    }

    private final HashMap<String,Long> _autoincrement_table =
	new HashMap<String,Long>();

    public String autoincrement( String object_name )

    {

	String basename =
	    object_name.replaceAll( "[0-9]+$", "" ).replaceAll( "_$", "" );

	Long last = this._autoincrement_table.get( basename );

	if ( last == null )
	    last = new Long( -1 );

	return basename + "_" + ( last.longValue() + 1 );

    }

    private void updateAutoincrement( String name )

    {

	String base = name.replaceAll( "[0-9]+$", "" ).replaceAll( "_$", "" );

	String[] num =
	    name.replaceAll( "_", " " ).
	    replaceAll( "[^0-9]+$", "" ).split( " " );

	String n = "????";
	if ( num != null )
	    n = num[num.length - 1];
	if ( n.equals( "????" ) || n.equals( "" ) )
	    n = "-1";

	long i;
        try {

	    i = Long.parseLong( n );

	} catch ( Throwable t ) {

	    i = -1;

	}
	Long itable = this._autoincrement_table.get( base );

	long itablevalue = ( itable != null ) ? itable.longValue() : -1;

	if ( i > itablevalue )
	    this._autoincrement_table.put( base,
					   new Long( i ) );

    }

    private final CheckBox _auto_increment_name =
	new CheckBox( "Autoincrement Name" );
    private final CheckBox _enforce_unique_names =
	new CheckBox( "Enforce Unique Names" );

    @Override
    protected void initializePreferences()

    {

        if ( Cookies.isCookieEnabled() ) {

            String cookie_prefix = Configuration.getCookiePrefix();

	    String auto_increment_string =
		Cookies.getCookie( cookie_prefix + ".auto_increment_name" );

	    boolean auto_increment = true;
	    if ( auto_increment_string != null )
		auto_increment =
		    Boolean.valueOf( auto_increment_string ).booleanValue();

	    this._auto_increment_name.setValue( auto_increment );

	    String enforce_string =
		Cookies.getCookie( cookie_prefix + ".enforce_unique_names" );

	    boolean enforce = true;
	    if ( enforce_string != null )
		enforce = Boolean.valueOf( enforce_string ).booleanValue();

	    this._enforce_unique_names.setValue( enforce );

	}

    }

    @Override
    public void pluginPreferences( PreferencesDialog preferences_dialog )

    {


	preferences_dialog.addWidgets( new Label( "SLASH:" ),
				       this._auto_increment_name );
	preferences_dialog.addWidgets( new Label( "" ),
				       this._enforce_unique_names );

    }

    @Override
    public void acceptPluginPreferences()

    {

        if ( Cookies.isCookieEnabled() ) {

            String cookie_prefix = Configuration.getCookiePrefix();

	    Boolean auto_increment = this._auto_increment_name.getValue();

            Cookies.setCookie( cookie_prefix + ".auto_increment_name",
			       auto_increment.toString() );

	    Boolean enforce = this._enforce_unique_names.getValue();

            Cookies.setCookie( cookie_prefix + ".enforce_unique_names",
			       enforce.toString() );

	}

    }

    private void keyHandler( KeyData key_data )

    {

	switch ( key_data.getKeyCode() ) {

	case 'v':
	case 'V': {

	    new ToggleView3DStateMessage().send();
	    break;

	}
	case 't':
	case 'T': {

	    new ToggleToolboxStateMessage().send();
	    break;

	}
	case 'x':
	case 'X': {

	    new BumpNudgeRadiusMessage().send( 1 );
	    if ( this._trace_edit_mode == SLASHPlugin.EditMode.NUDGE )
		new UpdateCursorOverlayMessage().send( this.buildCursor() );
	    break;

	}
	case 'z':
	case 'Z': {

	    new BumpNudgeRadiusMessage().send( -1 );
	    if ( this._trace_edit_mode == SLASHPlugin.EditMode.NUDGE )
		new UpdateCursorOverlayMessage().send( this.buildCursor() );
	    break;

	}

	}

    }

    private void setupInteraction()

    {

	String[] list = this._mode_handler.getObjectList();

	if ( ( list != null ) && ( list.length > 0 ) ) {

	    ArrayList<SLASHAnnotationSpinnerInfo> sasi_list =
		new ArrayList<SLASHAnnotationSpinnerInfo>();

	    for ( String s : list ) {

		try {

		    String[] p = s.split( "," );
		    if ( p.length == 2 ) {

			String name = p[0];
			int id = Integer.parseInt( p[1] );

			SLASHAnnotationSpinnerInfo sasi =
			    new SLASHAnnotationSpinnerInfo( name, id );
			sasi_list.add( sasi );

			this.updateAutoincrement( name );

		    }

		} catch ( Throwable t ) {

		    Debug.message( "String", s , "unparsable!" );
                    Debug.traceback( t );
		    break;

		}

	    }
	    SLASHAnnotationSpinnerInfo[] sl =
		sasi_list.toArray( new SLASHAnnotationSpinnerInfo[0] );

	    new AddAnnotationMessage().send( sl );

	}

    }

    private void selectCurrentAnnotation( SLASHAnnotationSpinnerInfo sasi )

    {

	this._edit_object = this.findAnnotation( sasi );

	if ( this._edit_object == null ) {

	    String url = "cgi-bin/SLASHdb.pl?" +
		"request=get_annotation&" +
		"id=" + sasi.getAnnotationID();

	    HTTPRequest.get( url, new EditObjectHandler( this ) );

	}

    }

    private Annotation findAnnotation( SLASHAnnotationSpinnerInfo sasi )

    {

	int annotation_id = sasi.getAnnotationID();
        HashMap<Integer,AnnotationList> ip = this._qip.get( this._quaternion );

	Annotation annotation = null;

	if ( ( ip != null ) && !ip.isEmpty() )
	    for ( AnnotationList al : ip.values() )
		if ( !al.isEmpty() )
		    for ( Annotation a : al )
			if ( a.getAnnotationID() == annotation_id ) {

			    annotation = a;
			    break;

			}

	return annotation;

    }

    private void handleEditObject( Element root )

    {

	String status = root.getAttribute( "status" );

	if ( status.equals( "success" ) ) {

	    Element annotation = XML.find( root, "annotation" );

	    int annotation_id = XML.getIntAttribute( annotation, "id" );
	    String name = annotation.getAttribute( "object_name" );
	    int color = XML.getIntAttribute( annotation, "color" );

	    this._edit_object = new Annotation();

	    this._edit_object.setAnnotationID( annotation_id );
	    this._edit_object.setObjectName( name );
	    this._edit_object.setUserName( this._last_user_name );
	    int model_id =
		Integer.parseInt( Configuration.parameter( "modelID" ) );
	    this._edit_object.setModelID( model_id );
	    this._edit_object.setRGB( color );
	    if ( annotation.getAttribute( "geometry_type" ).equals( "polygon" ) ) {

		this._edit_object.close();
		this._line_type = LineType.ADDED_POLYGON;

	    } else
		this._line_type = LineType.ADDED_POLYLINE;

	}

    }

    private void identifyCurrentObject( SLASHAnnotationSpinnerInfo sasi )

    {

	int annotation_id = sasi.getAnnotationID();
	HashMap<Integer,AnnotationList> ip = this._qip.get( this._quaternion );

	AnnotationList al = new AnnotationList();

	if ( ip != null ) {

	    AnnotationList p = ip.get( this._plane );

	    if ( p != null )
		for ( Annotation a : p )
		    if ( a.getAnnotationID() == annotation_id )
			al.add( a );


        }

	if ( al.size() > 0 )
	    new Flasher( al ).scheduleRepeating( 250 );
	else
	    Window.alert( "Sorry\n\n" +
			  "The object\n" +
			  sasi.getName() +
			  " (" + sasi.getAnnotationID() + ")\n" +
			  "is not on this plane.\n\n" +
			  "Use plane selection to locate." );


    }

    private void chooseInterpolationContour( Point point )

    {

	Annotation closest = this.findClosest( point );

	if ( closest != null )
	    new InterpolationContourMessage().send( closest );

    }

    private void chooseObject( Point point )

    {

	Annotation closest = this.findClosest( point );

	if ( closest != null )
	    new SelectAnnotationByIDMessage().send( closest.getAnnotationID() );

    }

    public void interpolate( Annotation a, Annotation b )

    {

	if ( a.getPlane() > b.getPlane() ) {

	    Annotation temp = a;

	    a = b;
	    b = temp;

	}

	SLASHInterpolatorData sid =
	    SLASHInterpolatorData.create( a.getAnnotationID(),
					  a.getUserName() );

	this.convertContour( sid, a );
	this.convertContour( sid, b );

	Worker w = Worker.createNamedWorker( SLASHInterpolator.NAME );
	w.setOnMessage( new Trap( a.getAnnotationID() ) );
	w.postMessage( sid );

    }

    private void setEditObjectLineType( Boolean closed )

    {

        if ( this._edit_points != null ) {

            this._edit_points.setClosed( closed );
	    this.renderEditPoints( true );
	    String url = "cgi-bin/SLASHdb.pl?" +
		"request=set_geometry_type&" +
		"id=" + this._edit_points.getGeometryID() + "&" +
		"closed=" + this._edit_points.isClosed();
	    HTTPRequest.get( url );

	}

    }

    private boolean _object_closure;

    private void setEditMode( SLASHPlugin.EditMode mode )

    {

	this._last_trace_edit_mode = this._trace_edit_mode = mode;

        if ( this._edit_points != null ) {

	    if ( mode == SLASHPlugin.EditMode.EXTEND ) {

		this._object_closure = this._edit_points.isClosed();
		this._edit_points.setClosed( false );

	    } else
		this._edit_points.setClosed( this._object_closure );
	    this.renderEditPoints( true );

	}

    }


    private static class Trap
	implements MessageHandler

    {

        private final int _annotation_id;

	Trap( int annotation_id )

	{

	    this._annotation_id = annotation_id;

	}

        @Override
        public void onMessage( MessageEvent event )

        {

	    String s = event.getDataAsString();

	    if ( s.equals( "complete" ) ) {

		new UpdateAnnotationPlanesMessage().send( this._annotation_id );
		new InterpolationStatusMessage().send( -1 );

	    } else if ( s.startsWith( "plane" ) ) {

		String[] sp = s.split( " " );

		int plane = Integer.parseInt( sp[1] );
		new InterpolationStatusMessage().send( plane );

	    } else
		Debug.message( "Error in interpolation: " + s );

        }

    }

    private void convertContour( SLASHInterpolatorData sid,
				 Annotation a )

    {

	JsArrayNumber x = JavaScriptObject.createArray().cast();
	JsArrayNumber y = JavaScriptObject.createArray().cast();

	double[][] data = a.toArray( new double[0][] );

	for ( int i = 0; i < data.length; i++ ) {

	    x.push( data[i][0] );
	    y.push( data[i][1] );

	}
	double[] ac = a.getCentroid();

	sid.addContour( a.getPlane(), ac[0], ac[1], x, y );

    }

    private static class Flasher
	extends Timer

    {

	private final Annotation[] _list;

	Flasher( AnnotationList annotation_list )

	{

	    super();

	    this._list = annotation_list.toArray( new Annotation[0] );
	    new SetTransientVectorOverlayLineWidthMessage().send( 10 );

	}

	private int _times = 15;

        @Override
	public void run()

	{

	    boolean visible = ( this._times % 2 == 0 );

	    new ClearTransientVectorOverlayMessage().send();
	    if ( !visible )
		new RenderTransientVectorOverlayMessage().send( this._list );

            this._times--;

            if ( this._times < 0 )
                this.cancel();

	}

    }

    private static class EditObjectHandler
        extends AbstractXMLRequestCallback

    {

	private final SLASHPlugin _sp;

        private EditObjectHandler( SLASHPlugin sp )

        {

	    this._sp = sp;

        }

        @Override
        protected void handleXMLResponse( Element root )

        {

	    this._sp.handleEditObject( root );

        }

    }

    private class AnnotationFactory
	implements TraceFactory

    {

        @Override
	public Drawable create()

	{

	    return new Annotation();

	}

    }

    /**
     * Called by the
     * <code>WIB</code> module upon loading.
     *
     * @param menu_bar The top-level <code>MenuBar</code> object to be
     * added to.
     */
    @Override
    protected void configureMenuBar( MenuBar menu_bar )
    {

        menu_bar.addItem( "Actions", new SLASHActionsMenu() );

    }

    @Override
    protected void configureMobileButtonBar( MobileButtonBar button_bar )

    {

	button_bar.add( new MobileSLASHToolsButton() );
	button_bar.add( new MobileObjectsButton() );
	button_bar.add( new MobilePlanesButton() );

    }


    private boolean _image_manager_mode = false;

    @Override
    public String getTransactionURL()

    {

	String uri = this._mode_handler.getURI();

	return this._image_manager_mode ?
	    ( "cgi-bin/Loader.pl?type=volume&uri=" + uri + "&" +
	      "transaction=" ) :
	    (  uri + "/" );

    }

    private void revertToImageManager()

    {

	this._image_manager_mode = true;

	this._image_factory = new DefaultImageFactory();

    }

    private void initializeDeleteOperation()

    {

        new SetCursorMessage().send( Cursor.CROSSHAIR );

    }

    private void completeDeleteObjectOperation( Point xy )

    {

        new SetCursorMessage().send( Cursor.DEFAULT );
        this._datatip_timer.cancel();

        Annotation chosen = this.findInside();

	if ( chosen == null )
	    chosen = this.findClosest( xy );

        if ( chosen != null )
	    this.deleteAreYouSure( chosen.getAnnotationID(),
				   DeleteDialog.Mode.OBJECT );

    }

    private void completeDeleteTraceOperation( Point xy )

    {

        new SetCursorMessage().send( Cursor.DEFAULT );
        this._datatip_timer.cancel();

        Annotation inside = this.findInside();

 	if ( inside == null )
	    inside = this.findClosest( xy );

       if ( inside != null )
	   this.deleteAreYouSure( inside.getGeometryID(),
				  DeleteDialog.Mode.TRACE );

    }

    private void verifyDeleteAnnotation( SLASHAnnotationSpinnerInfo sasi )

    {

	this.deleteAreYouSure( sasi.getAnnotationID(),
			       DeleteDialog.Mode.OBJECT );

    }

    private void deleteAreYouSure( int id,
				   DeleteDialog.Mode mode )

    {

	DeleteDialog dd = new DeleteDialog( id, mode );

	dd.display( this._pointery - 25, this._pointery - 25 );

    }

    private void commitContourDeletion( int id )

    {

        this.deletePoints( id, this._plane );

    }

    private void commitObjectDeletion( int id )

    {

        this.deleteAnnotationByID( id );

    }

    private void deleteAnnotationByID( int annotation_id )

    {

        Integer id = new Integer( annotation_id );

        String url =
	    "cgi-bin/SLASHdb.pl?" +
            "request=delete_annotation&" +
            "id=" + annotation_id + "&" +
            "model_id" + Configuration.parameter( "modelID" );

        HTTPRequest.get( url, new DeletionCompleteHandler() );

        AnnotationList list = this._annotation_id_table.get( id );
	if ( list != null ) {

	    for ( Drawable p : list )
		this.deletePoints( ( ( Annotation ) p ).getGeometryID(),
				   ( ( Annotation ) p ).getPlane() );
	    this._annotation_id_table.remove( id );

	}

    }

    private static class DeletionCompleteHandler
	extends AbstractRequestCallback

    {


        @Override
        protected void handleResponse( String data )
        {

	     new AnnotationUpdatedMessage().send();

        }

    }

    private Annotation findClosest( Point xy )

    {

        double mindistance = SLASHPlugin.NEARNESS * xy.getScale();
        Annotation closest = null;

        for ( Drawable p : this._visible ) {

            double distance = p.distanceTo( xy.getX(), xy.getY() );

            if ( mindistance >= distance ) {

                mindistance = distance;
                closest = ( Annotation ) p;

            }

        }

        return closest;

    }

    private Annotation _edit_points = null;

    private void selectNewContourForEditing( Point xy )

    {

	new ClearTransientVectorOverlayMessage().send();
	new EditModeSelectionMessage().send( true );
	if ( this._edit_points != null ) {

	    if ( this._trace_edit_mode == SLASHPlugin.EditMode.EXTEND )
		this._edit_points.setClosed( this._object_closure );
	    this._edit_points.setVisible( true );

        }
	this._edit_points = null;
        this.renderEditPoints( true );
	this.contourSelection( xy );

	if ( ( this._edit_points != null ) &&
	     ( this._trace_edit_mode == SLASHPlugin.EditMode.EXTEND ) ) {

	    this._object_closure = this._edit_points.isClosed();
	    this._edit_points.setClosed( false );
	    this.renderEditPoints( true );

	}

    }

    private void selectContourForEditing( Point xy )

    {

        Annotation closest = this.findClosest( xy );

        if ( closest != null ) {

            if ( this._edit_points != null )
                this._edit_points.setVisible( true );

            this._edit_points = closest;
            this._object_closure = closest.isClosed();
            this._edit_points.setVisible( false );
            new SetEditObjectLineTypeMessage().send( this._object_closure );
            this.renderEditPoints( true );
            new StatusMessage( "Object: ",
                               ( closest.getObjectName().equals( "" ) ?
				 "UNASSIGNED" : closest.getObjectName() ),
                               "GeomID: ",
                               closest.getGeometryID(),
                               "AnnotationID:",
                               closest.getAnnotationID() ).send();
	    AnnotationEditRequestMessage aerm =
		new AnnotationEditRequestMessage();
            new SetActionButtonListenerMessage( "Edit", aerm, null ).send();

        }

    }

    private Annotation _edit_object = null;

    private void selectTraceObject( Point xy )
    {

        Annotation closest = this.findClosest( xy );

        if ( closest != null )
            this.enableAddTracesToObject( closest );

    }

    private void enableAddTracesToObject( Annotation points )

    {

        new EnableDrawingMessage().send();
        this._edit_object = points;

        new SetDrawColorMessage( points.getRed(),
                                 points.getGreen(),
                                 points.getBlue() ).send();
        new StatusMessage( "Current Edit Object: ",
                           ( points.getObjectName().equals( "" ) ?
                             "UNASSIGNED" : points.getObjectName() ),
                           " AnnotationID:",
                           points.getAnnotationID() ).send();

    }

    private void enableAddTracesToObject( Annotation points,
					  LineType line_type )
    {

        this._line_type = line_type;
	this.enableAddTracesToObject( points );

    }

    private void initializeAddTraceOperation( Point xy, LineType line_type )

    {

        if ( this._edit_object == null )
            Window.alert( "You must select an object to add to first.\n" +
			  "\n" +
			  "Hold down the Control (CTRL) key " +
			  "and click a line\n" +
			  "to select." );
	else if ( this._edit_object.getUserName() == null ) {

	    UserNameDialog und = new UserNameDialog();

	    und.displayCenteredMessage();

	} else
	    this.enableAddTracesToObject( this._edit_object, line_type );

    }

    private static final String BLANK_CURSOR =
        "url('resources/cursors/blank.png'),none";

    private void initializeEditOperation( Point xy )

    {

        if ( this._edit_points != null ) {

            switch ( this._trace_edit_mode ) {

            case NUDGE: {

                new EnableCursorOverlayMessage().send( this.buildCursor() );
                new SetCursorMessage().send( SLASHPlugin.BLANK_CURSOR );
                break;

            }
            case EXTEND:
            case PULL: {

                new SetCursorMessage().send( Cursor.CROSSHAIR );
                break;

            }

            }

            this.editTrace( xy );

        }

    }

    private int[][] buildCursor()
    {

        int[][] c = new int[72][2];

        for ( int i = 0; i < c.length; i++ ) {

            double ang = ( ( double ) i / ( double ) c.length ) * Math.PI * 2;

            c[i][0] = ( int ) ( Math.cos( ang ) * this._nudge_radius );
            c[i][1] = ( int ) ( Math.sin( ang ) * this._nudge_radius );

        }

        return c;

    }

    private void editOperation( Point xy )
    {

        if ( this._edit_points != null )
            this.editTrace( xy );

    }

    private void editAnnotation()

    {

        AnnotationDialog ad =
            new AnnotationDialog( this._edit_points,
                                  this._plane,
                                  new AnnotationUpdateMessage() );
        ad.displayCenteredMessage();

    }

    private void handleAnnotationUpdate( AnnotationUpdateMessage aum )
    {

        if ( aum.keep() ) {

            int id = this._edit_points.getAnnotationID();

            HTTPRequest.get( "cgi-bin/SLASHdb.pl?" +
			     "request=update_annotation" +
			     "&color=" +
			     this._edit_points.getRGB() +
			     "&note=" +
			     this._edit_points.getSanitizedDescription()
			     +"&annotation_id=" + id +
			     "&model_id=" +
			     Configuration.parameter( "modelID" ) +
			     "&linetype=" +
			     this._edit_points.getLineType() +
			     "&name=" +
			     this._edit_points.getObjectName() +
			     "&onotology_name=NIF" +
			     "&name_ontology_uri=" +
			     this._edit_points.getURI(),
                             new AnnotationUpdateCompleteHandler( id ) );

            this.renderEditPoints( true );

	    String objname = this._edit_points.getObjectName().equals( "" ) ?
		"UNASSIGNED" : this._edit_points.getObjectName();
	    this.updateAutoincrement( objname );

            new StatusMessage( "Object: ",
			       objname,
                               "GeomID: ",
                               this._edit_points.getGeometryID(),
                               "AnnotationID:",
                               this._edit_points.getAnnotationID() ).send();

        }

    }

    private static class AnnotationUpdateCompleteHandler
	extends AbstractRequestCallback

    {

	private int _id;

        public AnnotationUpdateCompleteHandler( int id )
        {

	    this._id = id;

        }

        @Override
        protected void handleResponse( String data )

        {

	    new AnnotationUpdateCompleteMessage().send( this._id );

        }

    }

    @Override
    public double getMaxZoomIn()
    {

        return 0;		// Only allow zoom to 1:1.

    }

    @Override
    public double getZoomDelta()
    {

        return 1;		// Only allow zoom to exact powers of two.

    }

    @Override
    public String getState()
    {

        return "SLASH Plugin:\nState: " +
	    ( ( this._pum != null ) ? this._pum.toString() : "Uninitialized" );

    }

    @Override
    public void initializeDimensions( Quaternion quaternion )

    {

        // Currently does nothing.

    }

    private EditMode _last_trace_edit_mode = SLASHPlugin.EditMode.NUDGE;
    private EditMode _trace_edit_mode = SLASHPlugin.EditMode.NONE;

    private void editTrace( Point xy )

    {

	int radius =
	    ( int ) ( this._nudge_radius / this._pum.getZoom().getScale() );
        switch ( this._trace_edit_mode ) {

        case NUDGE: {

            this._edit_points.nudge( radius,
				     xy.getX(),
				     xy.getY() );
            break;

        }
        case PULL: {

            this._edit_points.bezier( radius,
				      xy.getX(),
				      xy.getY() );
            break;

        }
        case EXTEND: {

            this._edit_points.extend( xy.getX(),
				      xy.getY() );
            break;

        }

        }

        this.renderEditPoints( false );

    }

    private void renderEditPoints( boolean render_all )

    {

        if ( render_all )
            new RenderRequestMessage().send();

        if ( this._edit_points != null ) {

            Annotation[] p = new Annotation[]{ this._edit_points };
            new SetTransientVectorOverlayLineWidthMessage().send( 10 );
            new RenderTransientVectorOverlayMessage().send( p );

        }

    }

    private void completeEditOperation( Point xy )

    {

        if ( this._edit_points != null ) {

            new ClearCursorOverlayMessage().send();
            new SetCursorMessage().send( Style.Cursor.DEFAULT );

	    boolean mode = this._edit_points.isClosed();
	    this._edit_points.setClosed( this._object_closure );

	    if ( this._trace_edit_mode != SLASHPlugin.EditMode.EXTEND )
		this._edit_points.resample();

            HTTPRequest.post( "cgi-bin/SLASHdb.pl?" +
			      "request=update_contour&" +
			      "id=" + this._edit_points.getGeometryID(),
			      this._edit_points.toString(),
			      new EditCompleteHandler() );

	    this._edit_points.setClosed( mode );

        } else
	    this.contourSelection( xy );

        this.renderEditPoints( false );

    }

    private static class EditCompleteHandler
	extends AbstractRequestCallback

    {

        @Override
        protected void handleResponse( String data )
        {

	    new AnnotationUpdatedMessage().send();

	}

    }

    private void contourSelection( Point xy )

    {

	this.selectContourForEditing( xy );
	if ( this._edit_points == null )
	    this.showSelectErrorMessage();
	else
	    new EditModeSelectionMessage().send( false );

    }

    private LivewireDialog _livewire = new LivewireDialog();

    private EditDialog _edit_action_controls =
	new EditDialog( this._nudge_radius );

    private void handleInteractionSetup( AbstractInteractionMessageFactory amf )

    {

        new StatusMessage( amf.toString() ).send();

        if ( amf instanceof EditMessageFactory )
	    this._trace_edit_mode = this._last_trace_edit_mode;
	else if ( this._edit_action_controls != null ) {

            this.clearEditSelection();
	    this._trace_edit_mode = SLASHPlugin.EditMode.NONE;

        }

        new SetDrawColorMessage( 255, 0, 255 ).send();

        if ( amf instanceof AddPolylineMessageFactory )
            new DisableDrawingMessage().send();

    }

    private void clearEditSelection()

    {

        if ( this._edit_points != null ) {

	    if ( this._trace_edit_mode == SLASHPlugin.EditMode.EXTEND )
		this._edit_points.setClosed( this._object_closure );
            this._edit_points.setVisible( true );
            new ClearTransientVectorOverlayMessage().send();
            new RenderRequestMessage().send();
            new SetActionButtonListenerMessage().send();
	    new EditModeSelectionMessage().send( true );

        }
        this._edit_points = null;

    }

    private void showSelectErrorMessage()
    {

        Window.alert( "You must select an object to edit first.\n" +
		      "\n" +
		      "Click on a line to select." );

    }

    private void validateEditSelection()
    {

        if ( this._edit_points == null ) {

            new EditModeSelectionMessage().send( true );
            this.showSelectErrorMessage();

        }

    }

    private enum LineType
    {
        INITIAL_POLYGON,
        ADDED_POLYGON,
        INITIAL_POLYLINE,
        ADDED_POLYLINE;
    }

    private LineType _line_type = LineType.INITIAL_POLYGON;
    private int _plane;
    private Quaternion _quaternion = new Quaternion();
    private HashMap<Quaternion,HashMap<Integer,AnnotationList>> _qip =
	new HashMap<Quaternion,HashMap<Integer,AnnotationList>>();
    private String _last_name = "";

    private void addDrawingData( Annotation points )
    {

        this._datatip_timer.cancel();

        switch ( this._line_type ) {

        case INITIAL_POLYGON:
        case ADDED_POLYGON: {

            points.close();
            break;

        }

        }
        points.validate();

        if ( points.size() > 2 ) {

	    double red;
	    double green;
	    double blue;

	    do {

		red = Math.random();
		green = Math.random();
		blue = Math.random();

	    } while ( ( ( red * 0.2989 ) +
			( green * 0.5870 ) +
			( blue * 0.1140 ) ) < 0.75 );
            points.setRGB( ( int ) ( red * 256.0 ),
                           ( int ) ( green * 256.0 ),
                           ( int ) ( blue * 256.0 ) );
            this.processDrawingData( points, this._plane );

            switch ( this._line_type ) {

            case INITIAL_POLYGON:
            case INITIAL_POLYLINE: {

		String name = this._last_name;

		if ( name.equals( "" ) ) {

		    if ( this._edit_object != null )
			name = this._edit_object.getObjectName();
		    else
			name = "";

		}

		if ( !name.equals( "" ) &&
                     this._auto_increment_name.getValue() )
		    name = this.autoincrement( name );

                points.setObjectName( name );
                points.setUserName( this._last_user_name );

                AnnotationDialog ad =
                    new AnnotationDialog( points,
                                          this._plane,
                                          new AnnotationDataMessage() );

                ad.displayMessage( this._pointerx - 25, this._pointery - 25 );
                break;

            }
            case ADDED_POLYGON:
            case ADDED_POLYLINE: {

                this.addContourToObject( points );
                break;

            }

            }

        }

    }

    private void addContourToObject( Annotation points )
    {

        points.setURI( this._edit_object.getURI() );
        points.setUserName( this._edit_object.getUserName() );
        points.setRGB( this._edit_object.getRGB() );
        points.setObjectName( this._edit_object.getObjectName() );
        points.setAnnotationID( this._edit_object.getAnnotationID() );
        this.handleAnnotation( points, this._plane );
        new RenderRequestMessage().send();

    }

    private Stack<AnnotationMessage> _undo = new Stack<AnnotationMessage>();
    private Stack<AnnotationMessage> _redo = new Stack<AnnotationMessage>();

    private void undo()

    {

        AnnotationMessage adm = this._undo.pop();

        this.deletePoints( adm.getPoints().getGeometryID(), adm.getPlane() );

        if ( this._redo.size() == 0 )
            new EnableRedoMessage().send();
        this._redo.push( adm );
        if ( this._undo.size() == 0 )
            new DisableUndoMessage().send();

    }

    private void redo()

    {

        AnnotationMessage adm = this._redo.pop();

        this.processDrawingData( adm.getPoints(), adm.getPlane() );

        if ( this._redo.size() == 0 )
            new DisableRedoMessage().send();

        this.insertAnnotation( adm );

    }

    private void handleAnnotation( AnnotationDataMessage adm )

    {

        if ( !adm.keep() )
	    this.cancelAnnotation( adm );
        else {

            if ( this._redo.size() > 0 ) {

                this._redo.clear();
                new DisableRedoMessage().send();

            }
            this.insertAnnotation( adm );

        }

    }

    public void cancelAnnotation( AnnotationMessage adm )

    {

	this.deletePoints( adm.getPoints().getGeometryID(),
			   adm.getPlane() );
	this._edit_object = null;

    }

    private void insertAnnotation( AnnotationMessage adm )

    {

	if ( this._enforce_unique_names.getValue() ) {

	    Annotation points = adm.getPoints();

	    String url =
		"cgi-bin/SLASHdb.pl?" +
		"request=validate_name&" +
		"name=" + points.getObjectName() + "&" +
		"id=" + Configuration.parameter( "datasetID" ) + "&" +
		"model_id=" + Configuration.parameter( "modelID" );

	    HTTPRequest.get( url,
			     new NameValidationHandler( this, adm ) );

	} else
	    this.annotationInsertion( adm );

    }

    private void annotationRetry( AnnotationMessage adm )

    {

	new AnnotationRetryDialog( adm, this ).display();

    }

    private static class NameValidationHandler
        extends AbstractXMLRequestCallback

    {

	private final AnnotationMessage _am;
	private final SLASHPlugin _sp;

	NameValidationHandler( SLASHPlugin sp, AnnotationMessage am )

        {

	    this._am = am;
	    this._sp = sp;

	}

        @Override
        public void handleXMLResponse( Element root )
        {

	    Element validated = XML.find( root, "validated" );

	    if ( validated.getAttribute( "unique" ).equals( "true" ) )
		this._sp.annotationInsertion( this._am );
	    else
		this._sp.annotationRetry( this._am );

	}

    }

    public void annotationInsertion( AnnotationMessage adm )

    {

        new RenderRequestMessage().send();
        this._last_name = adm.getPoints().getObjectName();
        this._last_user_name = adm.getPoints().getUserName();

        if ( this._undo.size() == 0 )
            new EnableUndoMessage().send();
        this._undo.push( adm );

        if ( ( this._last_name != null ) && !this._last_name.equals( "" ) )
            HTTPRequest.get( "cgi-bin/db.pl?" +
			     "querytype=get_ontology_reference&" +
			     "name=" + this._last_name,
			     new InsertHandler( adm.getPoints(),
						adm.getPlane(),
						this ) );

    }

    private void handleInsert( Annotation points, int plane )

    {

        String url =
	    "cgi-bin/SLASHdb.pl?" +
            "request=add_annotation&" +
            "color=" + points.getRGB() + "&" +
            "note=" + points.getSanitizedDescription() + "&" +
            "dataset_id=" + Configuration.parameter( "datasetID" ) + "&" +
            "model_id=" + Configuration.parameter( "modelID" ) + "&" +
            "linetype=" + points.getLineType() + "&" +
            "name=" + points.getObjectName() + "&" +
            "onotology_name=NIF&" +
            "name_ontology_uri=" + points.getURI();
	this.updateAutoincrement( points.getObjectName() );

        new StatusMessage( "Current Edit Object: ",
			   points.getObjectName(),
                           " AnnotationID:",
                           points.getAnnotationID() ).send();

        HTTPRequest.get( url,
			 new AnnotationHandler( points, plane, this ) );

   }

    private class GeometryTable extends HashMap<Integer,Annotation> {}

    private final GeometryTable _geometry_table = new GeometryTable();

    private void handleAnnotation( Annotation points, int plane )

    {

        String url =
	    "cgi-bin/SLASHdb.pl?" +
            "request=add_contour&" +
            "user_name=" + points.getUserName() + "&" +
            "annotation_id=" + points.getAnnotationID() + "&" +
	    "closed=" + ( points.isClosed() ? "true" : "false" ) + "&" +
	    "z=" + plane;

        HTTPRequest.post( url,
			  points.toString(),
			  new PointUploadHandler( points,
                                                  this._geometry_table ) );

        this.insertTraceIntoAnnotationTable( points );
        if ( this._edit_object != null )
            this.enableAddTracesToObject( this._edit_object );

    }

    private static class PointUploadHandler
        extends AbstractXMLRequestCallback

    {

        private final Annotation _points;
	private final GeometryTable _geometry_table;

        PointUploadHandler( Annotation points, GeometryTable geometry_table )

        {

            this._points = points;
	    this._geometry_table = geometry_table;

        }

        @Override
        public void handleXMLResponse( Element root )

        {

            String status = root.getAttribute( "status" );

            if ( status.equals( "success" ) ) {

                Element geometry = XML.find( root, "geometry" );
                int geometry_id =
                    Integer.parseInt( geometry.getAttribute( "geometry_id" ) );
                this._points.setGeometryID( geometry_id );
		this._geometry_table.put( geometry_id, this._points );

		new AnnotationAcceptedMessage().send( this._points );

		String name = this._points.getObjectName();
		int annotation_id = this._points.getAnnotationID();

		SLASHAnnotationSpinnerInfo sasi =
		    new SLASHAnnotationSpinnerInfo( name, annotation_id );
		new AddAnnotationMessage().send( sasi );
		new SelectAnnotationByIDMessage().send( annotation_id );
		new AnnotationUpdatedMessage().send();

            } else {

                new RemoveGeometryMessage().send( this._points );

                Element error = XML.find( root, "error" );

                Text text = ( Text ) error.getFirstChild();
                String message = text.getData();
                String alert =
                    "SLASH Database Error\n" +
                    "\n" +
                    message;

                if ( message.equals( "NULL result for key 'user_id'" ) )
                    alert += "\n" +
			"\n" +
                        "You entered an invalid user name.\n" +
                        "\n" +
                        "Use a valid user name or\n" +
                        "contact administrator/supervisor\n" +
                        "to obtain one.";

                Window.alert( alert );

            }

        }

    }

    private static class AnnotationHandler
        extends AbstractXMLRequestCallback
    {

        private Annotation _points;
        private int _plane;
        private SLASHPlugin _sp;

        AnnotationHandler( Annotation points, int plane, SLASHPlugin sp )
        {

            this._points = points;
            this._plane = plane;
            this._sp = sp;

        }

        @Override
        public void handleXMLResponse( Element root )
        {

            if ( root.getAttribute( "status" ).equals( "success" ) ) {

                Element annotation = XML.find( root, "annotation" );
                int annotation_id =
                    Integer.parseInt( annotation.getAttribute( "id" ) );
                this._points.setAnnotationID( annotation_id );
                this._sp.handleAnnotation( this._points, this._plane );

            } else
                Debug.showXMLErrorMessage( "SLASH Database Error",
                                           "Unable to register annotation:",
                                           root );

        }

    }

    private static class InsertHandler
        extends AbstractXMLRequestCallback
    {

        private Annotation _points;
        private int _plane;
        private SLASHPlugin _sp;

        InsertHandler( Annotation points, int plane, SLASHPlugin sp )
        {

            this._points = points;
            this._plane = plane;
            this._sp = sp;

        }

        @Override
        public void handleXMLResponse( Element root )
        {

            String uri = XML.extractTextFromElement( root );

            this._points.setURI( uri );
            this._sp.handleInsert( this._points, this._plane );

        }

    }

    private void deletePoints( int id, int plane )

    {

        HashMap<Integer,AnnotationList> ip = this._qip.get( this._quaternion );

	HTTPRequest.get( "cgi-bin/SLASHdb.pl?" +
			 "request=delete_contour" +
			 "&id=" + id,
                         new DeleteHandler() );
        if ( ip != null ) {

            AnnotationList p = ip.get( plane );

            if ( p != null ) {

		for ( Annotation a : p )
		    if ( a.getGeometryID() == id ) {

			p.remove( a );
			break;

		    }

                if ( this._plane == plane )
                    new RenderRequestMessage().send();

            }

        }

    }

    private static class DeleteHandler
        extends AbstractXMLRequestCallback

    {

	DeleteHandler()

	{

	    super();

	}

        @Override
        public void handleXMLResponse( Element root )
        {

	    if ( root.getAttribute( "status" ).equals( "success" ) ) {

		Element deleted = XML.find( root, "deleted" );

		if ( XML.getBooleanAttribute( deleted, "empty" ) ) {

		    int annotation_id =
			XML.getIntAttribute( deleted, "annotation_id" );

		    new DeleteAnnotationMessage().send( annotation_id );

		} else
		    new AnnotationUpdatedMessage().send();

	    }

	}

    }

    private class AnnotationList extends ArrayList<Annotation> {}

    private HashMap<Integer,AnnotationList> _annotation_id_table =
        new HashMap<Integer,AnnotationList>();

    private void processDrawingData( Annotation points, int plane )

    {

	this.addPoints( points, plane );
        if ( this._plane == plane )
            new RenderRequestMessage().send();

    }

    private void addPoints( Annotation points, int plane )

    {

        this.insertTraceIntoAnnotationTable( points );

        points.setPlane( plane );
        points.setQuaternion( this._quaternion );

        HashMap<Integer,AnnotationList> ip = this._qip.get( this._quaternion );

        if ( ip == null )
            this._qip.put( this._quaternion,
                           ip = new HashMap<Integer, AnnotationList>() );

        AnnotationList p = ip.get( plane );

        if ( p == null )
            ip.put( plane, p = new AnnotationList() );

        p.add( points );

    }

    private void insertTraceIntoAnnotationTable( Annotation points )

    {

        Integer id = new Integer( points.getAnnotationID() );

        AnnotationList points_list = this._annotation_id_table.get( id );

        if ( points_list == null )
            this._annotation_id_table.put( id, points_list = new AnnotationList() );

        points_list.add( points );

    }

    private void removeFromAnnotationTable( Annotation points )
    {

        Integer id = new Integer( points.getAnnotationID() );

        AnnotationList points_list = this._annotation_id_table.get( id );

        points_list.remove( points );
        HashMap<Integer, AnnotationList> ip = this._qip.get( this._quaternion );
        AnnotationList pl = ip.get( this._plane );
        pl.remove( points );

        new RenderRequestMessage().send();

    }

    private ParameterUpdateMessage _pum = null;

    private void setParameters( ParameterUpdateMessage pum )
    {

        this._pum = pum;

	int plane = pum.getPlane();
	Quaternion quaternion = pum.getQuaternion();

	if ( ( this._plane != plane ) ||
	     !this._quaternion.equals( quaternion ) )
	    this.clearEditSelection();
        this._plane = plane;
        this._quaternion = quaternion;

    }

    private AnnotationList _visible = new AnnotationList();

    private void getDisplayList( GetDisplayListMessage gdlm )
    {

        double x0 = gdlm.getX0();
        double y0 = gdlm.getY0();
        double x1 = gdlm.getX1();
        double y1 = gdlm.getY1();

        this._mode_handler.getContours( x0, y0, this._plane,
                                        x1, y1, this._plane );
        HashMap<Integer,AnnotationList> ip = this._qip.get( this._quaternion );

        if ( ip != null ) {

            AnnotationList pl = ip.get( this._plane );

            if ( pl != null )
                for ( Drawable p : pl )
                    if ( p.isVisible() &&
			 ( p.contains( x0, y0, x1, y1 ) ||
			   p.containedBy( x0, y0, x1, y1 ) ) )
                        gdlm.addPoints( p );

        }
        this._visible.clear();

	for ( Drawable p : gdlm.getDisplayList() )
	    this._visible.add( ( Annotation ) p );

    }

    private void processContours( SLASHWorkerReturnData ccird )

    {

	SLASHWorkerPointDataList ccipdl = ccird.getPointDataList();

	if ( ccipdl.length() > 0 ) {

	    PointDataList point_data_list = new PointDataList();

	    for ( int j = 0; j < ccipdl.length(); j++ ) {

		SLASHWorkerPointData point_data = ccipdl.get( j );

		int offset = point_data.getOffset();
		SLASHWorkerAnnotation annotation = point_data.getAnnotation();

		int annotation_id = annotation.getAnnotationID();
		int geometry_id = annotation.getGeometryID();

		Annotation points =
		    this._geometry_table.get( geometry_id );

		if ( points == null ) {

		    points = new Annotation( annotation_id );
		    this._geometry_table.put( geometry_id, points );

		} else
		    points.clear();

		JsArrayNumber xp = annotation.getX();
		JsArrayNumber yp = annotation.getY();

		for ( int i = 0; i < xp.length(); i++ )
		    points.add( new double[] { xp.get( i ), yp.get( i ) } );
		points.setGeometryID( geometry_id );

		if ( annotation.isClosed() )
		    points.close();

		points.setObjectName( annotation.getObjectName() );
		points.setUserName( annotation.getUserName() );
		points.setURI( annotation.getURI() );
		points.setModelID( annotation.getModelID() );

		points.setRGB( annotation.getRGB() );

		String app_data_string = annotation.getAppData();

		String description = "";

		if ( app_data_string != null ) {

		    Element app_data = XML.convertToXML( app_data_string );

		    if ( app_data != null ) {

			Element note = XML.descendTo( app_data, "note" );

			if ( note != null ) {

			    Text t = ( Text ) note.getFirstChild();

			    if ( t != null )
				description = t.getNodeValue();

			}

		    }

		}
		points.setDesanitizedDescription( description );

		points.setBoundingBox( annotation.getX0(), annotation.getY0(),
				       annotation.getX1(), annotation.getY1() );

		point_data_list.add( new PointData( offset, points ) );

	    }

	    PointProcessor pp = new PointProcessor( point_data_list, this );

	    pp.scheduleRepeating( 250 );

	}

    }

    private double _imagex;
    private double _imagey;
    private int _pointerx;
    private int _pointery;

    private void updateDatatipTimer( Point xy )
    {

        this._imagex = xy.getX();
        this._imagey = xy.getY();

        this._pointerx = xy.getPointerX();
        this._pointery = xy.getPointerY();

        this._datatip_timer.cancel();
        this._datatip_timer.schedule( 250 );

    }

    private void datatip()

    {

        Annotation inside = this.findInside();

        String s;
        if ( inside != null )
            s = "Object: " +
                ( inside.getObjectName().equals( "" ) ?
		  "UNASSIGNED" : inside.getObjectName() ) +
                ", AnnotationID: " +
                inside.getAnnotationID() +
                ", GeometryID: " +
                inside.getGeometryID();
        else
            s = "";

        new InformationMessage().send( s );

    }

    private Annotation findInside()

    {

        Annotation inside = null;

        for ( Drawable p : this._visible )
            if ( p.pointInPolygon( this._imagex, this._imagey ) ) {

                inside = ( Annotation ) p;
                break;

            }

        return inside;

    }

    private ImageFactoryInterface _image_factory;

    @Override
    public ImageFactoryInterface getImageFactory()

    {

        return this._image_factory;

    }

    @Override
    public ControlsPanel getControlsPanel()

    {

        return new SLASHControlsPanel();

    }

    private void getDimensions()

    {

	try {

	    String transact = this.getTransactionURL();

	    String dims = transact +
		( this._image_manager_mode ? "dimensions" : "index.xml" );

	    HTTPRequest.get( dims, new RequestHandler( this ) );

	} catch ( Exception e ) {

	    Debug.traceback( e );

	}

    }

    private class RequestHandler
	extends AbstractXMLRequestCallback

    {

	private SLASHPlugin _slash_plugin;

	RequestHandler( SLASHPlugin slash_plugin )

	{

	    this._slash_plugin = slash_plugin;

	}

        @Override
        public void handleXMLResponse( Element root )
        {

	    int width = 0;
	    int height = 0;
	    int depth = 0;
	    int tilesize = 0;

	    if ( root.getTagName().equals( "index" ) ) {

		width = Integer.parseInt( root.getAttribute( "width" ) );
		height = Integer.parseInt( root.getAttribute( "height" ) );
		depth = Integer.parseInt( root.getAttribute( "planes" ) );
		tilesize = Integer.parseInt( root.getAttribute( "tilesize" ) );

	    } else if ( root.getTagName().equals( "root" ) ) {

		Element dims = XML.find( root, "dimension" );

		width = Integer.parseInt( dims.getAttribute( "width" ) );
		height = Integer.parseInt( dims.getAttribute( "height" ) );
		depth = Integer.parseInt( dims.getAttribute( "depth" ) );
		tilesize = Integer.parseInt( dims.getAttribute( "tilesize" ) );

	    }
	    if ( width > 0 ) {

		this._slash_plugin.setMaxDepth( depth );
		new DisplayInitMessage( width, height, depth, tilesize,
					1, null, -1,
					null,
					0, 1, 2 ).send();
		this._slash_plugin.reset();

	    } else
		Window.alert( "Error:\n\n" +
			      "Unable to parse XML\n" +
			      root.toString() );

	}

    }

    private static int _depth_digits = 0;

    private void setMaxDepth( int depth )

    {

	SLASHPlugin._depth_digits =
	    ( int ) Math.ceil( Math.log( depth ) / Math.log( 10 ) );

	if ( SLASHPlugin._depth_digits < 3 )
	    SLASHPlugin._depth_digits = 3;

    }

    static String format( int plane )

    {

	String p = plane + "";

	for ( int i = p.length(); i < SLASHPlugin._depth_digits; i++ )
	    p = "0" + p;

	return p;

    }

    private void reset()

    {

        new SetZoomBarDeltaMessage().send( 1.0 );
        new SetZoomBarMaxMessage().send( 0.0 );

    }

    @Override
    public AbstractImageMenu getImageMenu()

    {

        return new SLASHImageMenu();

    }

    private static class DatatipTimer
        extends Timer

    {

        private SLASHPlugin _sp;

        DatatipTimer( SLASHPlugin sp )
        {

            this._sp = sp;

        }

        @Override
        public void run()
        {

            this._sp.datatip();

        }

    }

    private static class PointData

    {

        private final int _offset;
        private final Annotation _points;

        PointData( int offset, Annotation points )

        {

            this._offset = offset;
            this._points = points;

        }

        int getOffset()

        {

            return this._offset;

        }

        Annotation getPoints()

        {

            return this._points;

        }

    }

    private static class PointProcessor
        extends Timer

    {

        private final SLASHPlugin _sp;
        private final Iterator<PointData> _point_data;

        PointProcessor( ArrayList<PointData> point_data, SLASHPlugin sp )

        {

            this._sp = sp;

            this._point_data = point_data.iterator();

        }

        @Override
        public void run()

        {

	    Date now = new Date();

            Date d = new Date( now.getTime() + 125 );

            while ( new Date().before( d ) )
                if ( this._point_data.hasNext() ) {

                    PointData pd = this._point_data.next();

                    int offset = pd.getOffset();
                    Annotation points = pd.getPoints();

                    this._sp.addPoints( points, offset );

                } else {

                    this.cancel();
                    break;

                }

	    new RenderRequestMessage().send();

        }

    }

    private static class PointDataList
        extends ArrayList<PointData>

    {

        PointDataList()

        {

            super();

        }

        @Override
        public Iterator<PointData> iterator()

        {

            return new PointIterator( this.toSortedArray() );

        }

        private static class PointIterator
            implements Iterator<PointData>

        {

            private int _index = 0;
            private PointData[] _array;

            private PointIterator( PointData[] array )

            {

                this._array = array;

            }

            @Override
            public boolean hasNext()

            {

                return this._index < this._array.length;

            }

            @Override
            public PointData next()

            {

                return this._array[this._index++];

            }

            @Override
            public void remove()

            {

                // Does nothing since it's never called.

            }

        }

        private PointData[] toSortedArray()

        {

            PointData[] plist = this.toArray( new PointData[0] );

            Arrays.sort( plist, new PointDataComparator() );

            return plist;

        }

        private static class PointDataComparator
            implements Comparator<PointData>

        {

            @Override
            public int compare( PointData o1, PointData o2 )

            {

                double diff =
                    o2.getPoints().getBBoxArea() - o1.getPoints().getBBoxArea();

                return ( diff > 0 ) ? 1 : ( ( diff < 0 ) ? -1 : 0 );

            }

        }

    }

}