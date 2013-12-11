package edu.ucsd.ncmir.WIB.client.core.components;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.LayoutPanel;

/**
 *
 * @author spl
 */
public abstract class AbstractSpinner
    extends LayoutPanel
    implements UpDownBumpHandler

{

    private final UpDownArrowButton _up_down_arrow_button;
    private int _w;
    private int _h;

    protected AbstractSpinner()

    {

	this.getElement().getStyle().setPadding( 2, Style.Unit.PX );

	this._up_down_arrow_button = new UpDownArrowButton( this );

	this._w = this._up_down_arrow_button.getPixelWidth();
	this._h = this._up_down_arrow_button.getPixelHeight();

	this.add( this._up_down_arrow_button );

	this.setWidgetLeftWidth( this._up_down_arrow_button,
				 0, Style.Unit.PX,
				 this._w + 2, Style.Unit.PX );
	this.setWidgetTopHeight( this._up_down_arrow_button,
				 4, Style.Unit.PX,
				 this._h + 2, Style.Unit.PX );
        this.setHeight( ( this._h += 4 ) + "px" );

    }

    protected int getW()

    {

	return this._w;

    }

    protected int getH()

    {

	return this._h;

    }

}