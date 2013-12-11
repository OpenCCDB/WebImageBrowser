package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.CustomButton;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import edu.ucsd.ncmir.WIB.client.core.components.AbstractSpinnerElement;
import edu.ucsd.ncmir.WIB.client.core.components.FilteredDataCallback;
import edu.ucsd.ncmir.WIB.client.core.components.IntegerMessageInputBox;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.message.MessageListener;
import edu.ucsd.ncmir.WIB.client.core.message.MessageManager;
import edu.ucsd.ncmir.WIB.client.core.messages.SetPlaneSliderPositionMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.UpdatePlanePositionMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.AddAnnotationMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.BumpObjectMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.BumpObjectPlaneMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.DeleteAnnotationMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.DeleteAnnotationVerifyMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.DeleteCurrentObjectMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.FlashCurrentObjectMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.GetSpinnerMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.HideAllButThisObjectMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.HideAllMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.HideNoneMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.HideThisObjectMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.IdentifyCurrentObjectMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.SearchObjectMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.SelectAnnotationMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.SelectObjectMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.SetCurrentAnnotationMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.SetVisibilityMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.SpinnerUpdateMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.WarpToObjectMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.resources.icons.SelectAnnotationWidgetClientBundle;

/**
 *
 * @author spl
 */
class SelectAnnotationWidget
    extends Grid
    implements MessageListener

{

    private static final SelectAnnotationWidgetClientBundle _images =
	GWT.create( SelectAnnotationWidgetClientBundle.class );

    private final AbsolutePanel _ap;
    private final Grid _input_box;
    private final IntegerMessageInputBox _input;
    private final Counter _counter;
    private final SelectAnnotationSpinner _spinner;

    private final HideButton[] _hide = new HideButton[4];

    private final SearchButton _search_button;
    private final IdentifyObjectButton _identify_object_button;
    private final GoToFirstButton _go_to_first_button;
    private final GoToLastButton _go_to_last_button;
    private final DeleteButton _delete_button;
    private final Show3DToggle _show_3d_toggle;
    private final ShowSurfaceToggle _show_surface_toggle;

    private final FirstPlaneButton _first_plane_button;
    private final PreviousPlaneButton _previous_plane_button;
    private final NextPlaneButton _next_plane_button;
    private final LastPlaneButton _last_plane_button;

    SelectAnnotationWidget()

    {

	super( 6, 1 );

	super.setCellSpacing( 0 );
	super.setCellPadding( 0 );

	MessageManager.registerAsListener( this,
					   BumpObjectMessage.class,
					   BumpObjectPlaneMessage.class,
					   DeleteAnnotationMessage.class,
					   DeleteCurrentObjectMessage.class,
                                           FlashCurrentObjectMessage.class,
                                           GetSpinnerMessage.class,
					   SearchObjectMessage.class,
					   SelectAnnotationMessage.class,
					   SpinnerUpdateMessage.class,
					   UpdatePlanePositionMessage.class,
					   WarpToObjectMessage.class );

	this._ap = new AbsolutePanel();

	this._spinner = new SelectAnnotationSpinner();
	this._spinner.setTitle( "Select object" );
	this._counter = new Counter( this._spinner );

	this._input = new IntegerMessageInputBox( SelectObjectMessage.class,
						  this._counter );
	this._input.setTitle( "Type object number, press enter" );
	this._input.setWidth( "40px" );

        this._input_box = new Grid( 1, 2 );

        this._input_box.setWidget( 0, 0, this._input );
        this._input_box.setWidget( 0, 1, this._counter );

	this._ap.add( this._input_box );
        this._ap.add( this._spinner );

	this._hide[0] = new HideNoneButton( this._spinner );
	this._hide[1] = new HideAllButton( this._spinner );
	this._hide[2] = new HideThisButton( this._spinner );
	this._hide[3] = new HideOthersButton( this._spinner );

	this._search_button =
	    new SearchButton( this._spinner );
	this._identify_object_button =
	    new IdentifyObjectButton( this._spinner );
	this._go_to_first_button =
	    new GoToFirstButton( this._spinner );
	this._go_to_last_button =
	    new GoToLastButton( this._spinner );
	this._delete_button =
	    new DeleteButton( this._spinner );
	this._show_surface_toggle =
	    new ShowSurfaceToggle( this._spinner );
	this._show_3d_toggle =
	    new Show3DToggle( this._spinner, this._show_surface_toggle );

	this._first_plane_button =
	    new FirstPlaneButton( this._spinner );
	this._previous_plane_button =
	    new PreviousPlaneButton( this._spinner );
	this._next_plane_button =
	    new NextPlaneButton( this._spinner );
	this._last_plane_button =
	    new LastPlaneButton( this._spinner );

	ControlsGrid object_control_row = new ControlsGrid( 1, 7 );
	object_control_row.setWidget( 0, 0, this._search_button );
	object_control_row.setWidget( 0, 1, this._identify_object_button );
	object_control_row.setWidget( 0, 2, this._go_to_first_button );
	object_control_row.setWidget( 0, 3, this._go_to_last_button );
	object_control_row.setWidget( 0, 4, this._delete_button );
        object_control_row.setWidget( 0, 5, this._show_3d_toggle );
        object_control_row.setWidget( 0, 6, this._show_surface_toggle );

	ControlsGrid plane_control_row = new ControlsGrid( 1, 4 );
	plane_control_row.setWidget( 0, 0, this._first_plane_button );
	plane_control_row.setWidget( 0, 1, this._previous_plane_button );
	plane_control_row.setWidget( 0, 2, this._next_plane_button );
	plane_control_row.setWidget( 0, 3, this._last_plane_button );

	Grid.CellFormatter cf = super.getCellFormatter();
	this.setWidget( 0, 0, new Label( "Object" ) );
	cf.setAlignment( 0, 0,
			 HasHorizontalAlignment.ALIGN_CENTER,
			 HasVerticalAlignment.ALIGN_MIDDLE );
	this.setWidget( 1, 0, this._ap );
	Grid hide = new Grid( 1, this._hide.length + 1 );
	hide.setWidget( 0, 0, new Label( "Hide:" ) );
	for ( int i = 0; i < this._hide.length; i++ )
	    hide.setWidget( 0, i + 1, this._hide[i] );

	this.setWidget( 2, 0, hide );
	this.setWidget( 3, 0, object_control_row );
	this.setWidget( 4, 0, new Label( "Plane" ) );
	cf.setAlignment( 4, 0,
			 HasHorizontalAlignment.ALIGN_CENTER,
			 HasVerticalAlignment.ALIGN_MIDDLE );
        this.setWidget( 5, 0, plane_control_row );

    }

    private static class ControlsGrid
	extends Grid

    {

	ControlsGrid( int rows, int columns )

	{

	    super( rows, columns );

	    Style s = super.getElement().getStyle();
	    s.setMargin( 0, Style.Unit.PX );
	    s.setPadding( 0, Style.Unit.PX );
	    s.setBorderWidth( 0, Style.Unit.PX );
	    s.setBorderStyle( Style.BorderStyle.NONE );

	    super.setCellSpacing( 0 );
	    super.setCellPadding( 0 );
	    super.setBorderWidth( 0 );

	}

    }

    private int _z = -1;

    @Override
    public void action( Message m, Object o )
    {

	SpinnerElement se = ( SpinnerElement ) this._spinner.getCurrentItem();

	// This may occur before the spinner is loaded, so. . .

	if ( se != null ) {

	    if ( m instanceof BumpObjectMessage )
		this.bumpObject( ( ( Integer ) o ).intValue() );
	    else if ( m instanceof DeleteCurrentObjectMessage )
		this.deleteCurrentObject();
	    else if ( m instanceof BumpObjectPlaneMessage )
		this.bumpObjectPlane( ( ( Integer ) o ).intValue() );
	    else if ( m instanceof FlashCurrentObjectMessage )
		this.flashCurrentObject();
	    else if ( m instanceof SearchObjectMessage )
		this.search( ( SearchObjectMessage ) m );
	    else if ( m instanceof GetSpinnerMessage ) {

		GetSpinnerMessage gsm = ( GetSpinnerMessage ) m;

		gsm.setSpinner( this._spinner );

	    } else {

		SLASHAnnotationSpinnerInfo sasi = se.getObjectInfo();

		if ( m instanceof SelectAnnotationMessage )
		    this.selectAnnotation( ( ( Integer ) o ).intValue(), sasi );
		else if ( m instanceof DeleteAnnotationMessage )
		    this.clearSurfaceSelections();
		else if ( m instanceof WarpToObjectMessage )
		    this.warpToObject( ( ( Integer ) o ).intValue(), sasi );
		else if ( m instanceof UpdatePlanePositionMessage )
		    this._z = ( ( Integer ) o ).intValue();

		int zmin = sasi.getZMin();
		int zmax = sasi.getZMax();

		if ( zmin > -1 ) {

		    this._first_plane_button.setEnabled( zmin != this._z );
		    this._previous_plane_button.setEnabled( zmin != this._z );
		    this._next_plane_button.setEnabled( zmax != this._z );
		    this._last_plane_button.setEnabled( zmax != this._z );

		} else {

		    this._first_plane_button.setEnabled( false );
		    this._previous_plane_button.setEnabled( false );
		    this._next_plane_button.setEnabled( false );
		    this._last_plane_button.setEnabled( false );

		}

	    }

	    for ( HideButton hb : this._hide )
		if ( hb.getValue() ) {

		    hb.handler( this._spinner );
		    break;

		}

	}

    }

    private void deleteCurrentObject()

    {

	SpinnerElement ase =
	    ( SpinnerElement ) this._spinner.getCurrentItem();

	if ( ase != null )
	    new DeleteAnnotationVerifyMessage().send( ase.getObjectInfo() );

    }

    private void bumpObject( int dir )

    {

	switch ( dir ) {

	case Integer.MIN_VALUE: {

	    this._spinner.selectFirstItem();
	    break;

	}
	case Integer.MAX_VALUE: {

	    this._spinner.selectLastItem();
	    break;

	}
	default: {

	    this._spinner.bump( dir );
	    break;

	}

	}

    }

    private void bumpObjectPlane( int dir )

    {


	SpinnerElement ase =
	    ( SpinnerElement ) this._spinner.getCurrentItem();

	if ( ase != null ) {

	    SLASHAnnotationSpinnerInfo sasi =
		( SLASHAnnotationSpinnerInfo ) ase.getObjectInfo();

	    int z = -1;

	    switch ( dir ) {

	    case Integer.MIN_VALUE: {

		z = sasi.firstZ();
		break;

	    }
	    case -1: {

		z = sasi.previousZ();
		break;

	    }
	    case 1: {

		z = sasi.nextZ();
		break;

	    }
	    case Integer.MAX_VALUE: {

		z = sasi.lastZ();
		break;

	    }

	    }

	    if ( z > 0 ) {

		new SetPlaneSliderPositionMessage().send( z );
		new UpdatePlanePositionMessage().send( z );

	    }

	}

    }

    private void flashCurrentObject()

    {

	SpinnerElement ase =
	    ( SpinnerElement ) this._spinner.getCurrentItem();

	if ( ase != null )
	    new IdentifyCurrentObjectMessage().send( ase.getObjectInfo() );

    }

    private void search( SearchObjectMessage som )

    {

	String name = som.getName();

	int i = 0;
	for ( AbstractSpinnerElement ase :
		  this._spinner.getSpinnerElementList() )
	    if ( ase.getText().equals( name ) )
		break;
	    else
		i++;

	boolean ok = i < this._spinner.size();

	if ( ok )
	    new WarpToObjectMessage().send( i );
	else
	    Window.alert( "Not found." );

	som.setStatus( ok );

    }


    private void clearSurfaceSelections()

    {

	this._show_3d_toggle.setValue( false );
	this._show_surface_toggle.setValue( false );
	this._show_surface_toggle.setEnabled( false );

    }

    private void selectAnnotation( int input, SLASHAnnotationSpinnerInfo sasi )

    {

	this.setInput( input );

	this._show_3d_toggle.setValue( sasi.is3DEnabled() );
	this._show_surface_toggle.setValue( sasi.isSurfaceEnabled() );
	this._show_surface_toggle.setEnabled( sasi.is3DEnabled() );
	new SetCurrentAnnotationMessage().send( sasi );

    }

    private void warpToObject( int input, SLASHAnnotationSpinnerInfo sasi )

    {

	this.selectAnnotation( input, sasi );
	sasi.setWarpPending();

    }

    @Override
    public void onAttach()

    {

	int ibox_width = this._input_box.getOffsetWidth();
	int ibox_height = this._input_box.getOffsetHeight();

	int width = this.getOffsetWidth();
	int spinner_width = width - this._input_box.getOffsetWidth();
	this._spinner.setWidth( spinner_width + "px" );

	int spinner_height = this._spinner.getOffsetHeight();

	int h = spinner_height > ibox_height ? spinner_height : ibox_height;

	int padding =
	    Integer.parseInt( this._spinner.getElement().
			      getStyle().
			      getPadding().
			      replaceAll( "[^0-9]+", "" ) );

        int slop = padding * 2;

	this._ap.setPixelSize( ibox_width + slop + 4 + spinner_width,
			       h + slop + 4 );

	this._ap.setWidgetPosition( this._input_box,
				    0,
				    ( h - ibox_height ) / 2 );

	this._ap.setWidgetPosition( this._spinner,
				    ibox_width + 5,
				    ( h - spinner_height ) / 2 );
	super.onAttach();

    }

    private void setInput( int id )

    {

	this._input.setValue( id + "" );

	this._go_to_first_button.setEnabled( id != 0 );

	this._go_to_last_button.setEnabled( id !=
					    ( this._spinner.size() - 1 ) );

	this._identify_object_button.setEnabled( this._spinner.size() > 0 );
	this._delete_button.setEnabled( this._spinner.size() > 0 );

    }

    private static class Counter
	extends Label
	implements MessageListener,
		   FilteredDataCallback


    {

        private final SelectAnnotationSpinner _sns;

	Counter( SelectAnnotationSpinner sns )

	{

	    super( "of 0" );

	    this._sns = sns;
	    MessageManager.registerAsListener( this,
					       AddAnnotationMessage.class,
					       DeleteAnnotationMessage.class );

	}


        @Override
        public void action( Message m, Object o )

        {

	    this.setText( "of " + this._sns.size() );

        }

	private int _value = 0;

	@Override
	public boolean setValue( double value )

	{

	    boolean ok;

            ok = ( 0 <= value ) && ( value < this._sns.size() );

            if ( ok ) {

                this._value = ( int ) value;
                this._sns.setCurrentIndex( this._value );

            }

	    return ok;

	}

	@Override
	public double getValue()

	{

	    return this._value;

	}

    }

    private static abstract class HideButton
	extends RadioButton
	implements MessageListener,
		   ValueChangeHandler

    {

	private final SelectAnnotationSpinner _spinner;

	HideButton( SelectAnnotationSpinner spinner,
		    String label,
		    String tooltip,
		    Class<? extends Message> message )

	{

	    super( "hide", label );
	    super.setTitle( tooltip );
	    this._spinner = spinner;
	    this.addValueChangeHandler( this );
	    MessageManager.registerListener( message, this );

	}

        @Override
        public void onValueChange( ValueChangeEvent event )
        {

	    this.handler( this._spinner );

        }

	@Override
        public void action( Message m, Object o )

        {

	    this.handler( this._spinner );

        }
	abstract void handler( SelectAnnotationSpinner spinner );

    }

    private static class HideNoneButton
	extends HideButton

    {

	HideNoneButton( SelectAnnotationSpinner spinner )

	{

	    super( spinner, 
		   "None",
		   "Make all objects visible.",
		   HideNoneMessage.class );
	    this.setValue( true );

	}

        @Override
	void handler( SelectAnnotationSpinner spinner )

	{

	    new SetVisibilityMessage().send( Integer.MAX_VALUE );

	}

    }

    private static class HideAllButton
	extends HideButton

    {

	HideAllButton( SelectAnnotationSpinner spinner )

	{

	    super( spinner,
		   "All",
		   "Make all objects invisible.",
		   HideAllMessage.class );

	}

        @Override
	void handler( SelectAnnotationSpinner spinner )

	{

	    new SetVisibilityMessage().send( Integer.MIN_VALUE );

	}

    }

    private static class HideThisButton
	extends HideButton

    {

	HideThisButton( SelectAnnotationSpinner spinner )

	{

	    super( spinner,
		   "This",
		   "Make current object invisible.",
		   HideThisObjectMessage.class );

	}

        @Override
	void handler( SelectAnnotationSpinner spinner )

	{

	    SpinnerElement se = ( SpinnerElement ) spinner.getCurrentItem();
	    if ( se != null ) {

		SLASHAnnotationSpinnerInfo sasi = se.getObjectInfo();
		new SetVisibilityMessage().send( sasi.getAnnotationID() );

	    }

	}

    }

    private static class HideOthersButton
	extends HideButton

    {

	HideOthersButton( SelectAnnotationSpinner spinner )

	{

	    super( spinner,
		   "Others",
		   "Hide all but this object.",
		   HideAllButThisObjectMessage.class );

	}

        @Override
	void handler( SelectAnnotationSpinner spinner )

	{

	    SpinnerElement se = ( SpinnerElement ) spinner.getCurrentItem();
	    if ( se != null ) {

		SLASHAnnotationSpinnerInfo sasi = se.getObjectInfo();
		new SetVisibilityMessage().send( -sasi.getAnnotationID() );

	    }

	}

    }

    private static class GoToFirstButton
        extends CustomButton
	implements ClickHandler

    {

	private final SelectAnnotationSpinner _spinner;

        public GoToFirstButton( SelectAnnotationSpinner spinner )

        {

	    super( new Image( SelectAnnotationWidget._images.first_object() ) );

	    super.setTitle( "Go to the first object in the list." );
	    this._spinner = spinner;
	    this.addClickHandler( this );

        }

        @Override
        public void onClick( ClickEvent event )

        {

	    this._spinner.selectFirstItem();

        }

    }

    private static class GoToLastButton
        extends CustomButton
	implements ClickHandler

    {

	private final SelectAnnotationSpinner _spinner;

        public GoToLastButton( SelectAnnotationSpinner spinner )

        {

	    super( new Image( SelectAnnotationWidget._images.last_object() ) );

	    super.setTitle( "Go to the last object in the list." );
	    this._spinner = spinner;
	    this.addClickHandler( this );

        }

        @Override
        public void onClick( ClickEvent event )

        {

	    this._spinner.selectLastItem();

        }

    }

    private static class DeleteButton
        extends CustomButton
	implements ClickHandler

    {

	private final SelectAnnotationSpinner _spinner;

        public DeleteButton( SelectAnnotationSpinner spinner )

        {

	    super( new Image( SelectAnnotationWidget._images.delete() ) );
	    super.setTitle( "Delete the current object." );
	    this._spinner = spinner;
	    this.addClickHandler( this );

        }

        @Override
        public void onClick( ClickEvent event )

        {

	    SpinnerElement ase =
		( SpinnerElement ) this._spinner.getCurrentItem();

	    if ( ase != null )
		new DeleteAnnotationVerifyMessage().send( ase.getObjectInfo() );

        }

    }

    private static class SearchButton
        extends CustomButton
	implements ClickHandler

    {

	private final SelectAnnotationSpinner _spinner;

        public SearchButton( SelectAnnotationSpinner spinner )

        {

	    super( new Image( SelectAnnotationWidget._images.search() ) );
	    super.setTitle( "Search." );
	    this.addClickHandler( this );
	    this._spinner = spinner;

        }


        @Override
        public void onClick( ClickEvent event )

        {

	    new SearchBox( this._spinner ).display();

        }

    }

    private static class IdentifyObjectButton
        extends CustomButton
	implements ClickHandler

    {

	private final SelectAnnotationSpinner _spinner;

        public IdentifyObjectButton( SelectAnnotationSpinner spinner )

        {

	    super( new Image( SelectAnnotationWidget._images.id() ) );
	    super.setTitle( "Identify the current object." );
	    this._spinner = spinner;
	    this.addClickHandler( this );

        }

        @Override
        public void onClick( ClickEvent event )

        {

	    new FlashCurrentObjectMessage().send();

        }

    }

    private static class Show3DToggle
	extends CheckBox
	implements ClickHandler

    {

	private final SelectAnnotationSpinner _spinner;
        private final ShowSurfaceToggle _sst;

        public Show3DToggle( SelectAnnotationSpinner spinner,
                             ShowSurfaceToggle sst )

        {

	    super( "3D" );
	    super.setTitle( "Show/Hide this object in 3D viewer." );
	    this._spinner = spinner;
            this._sst = sst;
	    this.addClickHandler( this );

        }

        @Override
        public void onClick( ClickEvent event )

        {

	    SpinnerElement ase =
		( SpinnerElement ) this._spinner.getCurrentItem();

	    if ( ase != null ) {

		boolean on = this.getValue().booleanValue();
		ase.getObjectInfo().enable3D( on );
		this._sst.setEnabled( on );

            }

	}

    }

    private static class ShowSurfaceToggle
	extends CheckBox
	implements ClickHandler

    {

	private final SelectAnnotationSpinner _spinner;

        public ShowSurfaceToggle( SelectAnnotationSpinner spinner )

        {

	    super( "Surface" );
	    super.setTitle( "Show/Hide this object as a surface in 3D viewer." );
	    this._spinner = spinner;
	    this.addClickHandler( this );

        }

        @Override
        public void onClick( ClickEvent event )

        {

	    SpinnerElement ase =
		( SpinnerElement ) this._spinner.getCurrentItem();

	    if ( ase != null ) {

		boolean on = this.getValue().booleanValue();
		ase.getObjectInfo().enableSurface( on );

	    }

	}

    }

    private static abstract class PlaneButton
        extends CustomButton
	implements ClickHandler

    {

	private final SelectAnnotationSpinner _spinner;

        PlaneButton( SelectAnnotationSpinner spinner,
		     Image icon,
		     String title )

        {

	    super( icon );

	    super.setTitle( title );
	    this._spinner = spinner;
	    this.addClickHandler( this );

        }

        @Override
        public final void onClick( ClickEvent event )

        {

	    SpinnerElement ase =
		( SpinnerElement ) this._spinner.getCurrentItem();

	    if ( ase != null ) {

		SLASHAnnotationSpinnerInfo sasi =
		    ( SLASHAnnotationSpinnerInfo ) ase.getObjectInfo();
		int z = this.getZ( sasi );

		new SetPlaneSliderPositionMessage().send( z );
		new UpdatePlanePositionMessage().send( z );

	    }

        }
	protected abstract int getZ( SLASHAnnotationSpinnerInfo sasi );


    }

    private static class FirstPlaneButton
	extends PlaneButton

    {

        public FirstPlaneButton( SelectAnnotationSpinner spinner )

        {

	    super( spinner,
		   new Image( SelectAnnotationWidget._images.first_plane() ),
		   "Visit the first plane of the current object." );

        }

        @Override
	protected int getZ( SLASHAnnotationSpinnerInfo sasi )

	{

	    return sasi.firstZ();

        }

    }

    private static class PreviousPlaneButton
	extends PlaneButton

    {

        public PreviousPlaneButton( SelectAnnotationSpinner spinner )

        {

	    super( spinner,
		   new Image( SelectAnnotationWidget._images.previous_plane() ),
		   "Visit the previous plane of the current object." );

        }

        @Override
	protected int getZ( SLASHAnnotationSpinnerInfo sasi )

	{

	    return sasi.previousZ();

        }

    }

    private static class NextPlaneButton
	extends PlaneButton

    {

        public NextPlaneButton( SelectAnnotationSpinner spinner )

        {

	    super( spinner,
		   new Image( SelectAnnotationWidget._images.next_plane() ),
		   "Visit the next plane of the current object." );

        }

        @Override
	protected int getZ( SLASHAnnotationSpinnerInfo sasi )

	{

	    return sasi.nextZ();

        }

    }

    private static class LastPlaneButton
	extends PlaneButton

    {

        public LastPlaneButton( SelectAnnotationSpinner spinner )

        {

	    super( spinner,
		   new Image( SelectAnnotationWidget._images.last_plane() ),
		   "Visit the last plane of the current object." );

        }

        @Override
	protected int getZ( SLASHAnnotationSpinnerInfo sasi )

	{

	    return sasi.lastZ();

        }

    }

}
