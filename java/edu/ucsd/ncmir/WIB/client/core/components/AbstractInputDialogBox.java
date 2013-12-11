package edu.ucsd.ncmir.WIB.client.core.components;

import edu.ucsd.ncmir.WIB.client.core.messages.KeyEventHandlerStateMessage;

/**
 *
 * @author spl
 */
public class AbstractInputDialogBox
    extends AbstractDialogBox

{

    /**
     * Creates an empty AbstractDialogBox box.
     */
    protected AbstractInputDialogBox()
    {

        this( true, false );

    }

    protected AbstractInputDialogBox( boolean autohide )
    {

        this( autohide, false );

    }

    protected AbstractInputDialogBox( boolean autohide, boolean modal )
    {

        this( autohide, modal, false );

    }

    protected AbstractInputDialogBox( boolean autohide,
				      boolean modal,
				      boolean resizable )
    {

        super( autohide, modal, resizable );

    }

    protected AbstractInputDialogBox( boolean autohide,
                                      boolean modal,
                                      boolean show_close_icon,
                                      boolean resizable )

    {

	super( autohide, modal, false, show_close_icon, resizable );

    }

    protected AbstractInputDialogBox( boolean autohide,
                                      boolean modal,
                                      boolean show_minimize_icon,
                                      boolean show_close_icon,
                                      boolean resizable )

    {

	super( autohide,
	       modal,
	       show_minimize_icon,
	       show_close_icon,
	       resizable );

    }

    @Override
    protected void onAttach()

    {

    	new KeyEventHandlerStateMessage().send( false );
    	super.onAttach();

    }

    @Override
    protected void onDetach()

    {

    	new KeyEventHandlerStateMessage().send( true );
    	super.onDetach();

    }

}
