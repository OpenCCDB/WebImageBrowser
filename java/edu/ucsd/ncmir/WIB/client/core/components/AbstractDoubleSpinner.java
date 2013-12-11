package edu.ucsd.ncmir.WIB.client.core.components;

import com.google.gwt.dom.client.Style;

/**
 *
 * @author spl
 */

public class AbstractDoubleSpinner
    extends AbstractSpinner
    implements Filter

{

    private final FilteredInputBox _text;
    private final double _bump_value;
    private final int _decimal_places;

    protected AbstractDoubleSpinner( double initial_value,
				     int decimal_places,
				     FilteredDataCallback callback )

    {

	super();

	this._bump_value = Math.pow( 10.0, -decimal_places );

	this._decimal_places = decimal_places;

	this._text = new FilteredInputBox( callback );
	this._text.setFilter( this );
	this._text.setText( initial_value + "" );

	this.add( this._text );

	int padding =
	    Integer.parseInt( this.getElement().
			      getStyle().
			      getPadding().
			      replaceAll( "[^0-9]+", "" ) );

        int slop = padding * 2;
	this.setWidgetLeftWidth( this._text,
				 this.getW() + 5,
				 Style.Unit.PX,
				 80 - ( this.getW() + 5 + slop ),
				 Style.Unit.PX );
	this.setWidgetTopHeight( this._text,
				 0, Style.Unit.PX,
				 this.getH(),
				 Style.Unit.PX );

    }

    @Override
    public void bump( int dir )

    {

	double value = Double.parseDouble( this._text.getText() );

	value += this._bump_value * dir;

	int sign = ( value < 0 ) ? -1 : 1;
	value = Math.abs( value ) + ( this._bump_value / 2.0 );

	String[] parts = ( value + "" ).split( "\\." );

	String vstring;

	if ( parts.length == 2 ) {

	    if ( parts[1].length() > this._decimal_places )
		parts[1] = parts[1].substring( 0, this._decimal_places );
	    else 
		while ( parts[1].length() < this._decimal_places )
		    parts[1] += "0";

	    vstring = parts[0] + "." + parts[1];

	} else {

	    String frac = "";

	    while ( frac.length() < this._decimal_places )
		frac += "0";
	    vstring = parts[0] + "." + frac;

	}
	if ( sign < 0 )
	    vstring = "-" + vstring;

	this._text.setText( vstring );

    }

    @Override
    public boolean validate( int ch )

    {

	boolean ok;

	try {

	    ok = new String( new byte[] { ( byte ) ch } ).matches( "[0-9.+-]" );

	} catch ( Throwable t ) {

	    ok = false;

	}

	return ok;

    }

}
