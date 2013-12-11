package edu.ucsd.ncmir.WIB.client.core.menus;

import edu.ucsd.ncmir.WIB.client.core.components.AbstractDialogBox;
import edu.ucsd.ncmir.WIB.client.core.components.menu.WIBMenuBar;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.message.MessageListener;
import edu.ucsd.ncmir.WIB.client.core.message.MessageManager;
import edu.ucsd.ncmir.WIB.client.core.messages.SetDialogVisibilityMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.ToggleDialogVisibilityMessage;

/**
 *
 * @author spl
 */
public class ToggleDialogMenuItem
    extends AbstractMenuItem
    implements MessageListener

{

    private final String _label;
    private boolean _visible = false;
    private final AbstractDialogBox _dialog_box;

    public ToggleDialogMenuItem( String label,
				 AbstractDialogBox dialog_box,
				 Class<? extends SetDialogVisibilityMessage> svm,
				 Class<? extends ToggleDialogVisibilityMessage> tvm )

    {

        super( "Show " + label );
        this._label = label;
        MessageManager.registerAsListener( this, svm, tvm );

        this._dialog_box = dialog_box;

    }

    @Override
    public void execute()
    {

        this.toggleDialogVisibility();

    }

    private void toggleDialogVisibility()

    {

        this._visible = !this._visible;
        this.setDialogVisibility();

    }

    private void setDialogVisibility()

    {

        if ( this._visible ) {

            this.setHTML( WIBMenuBar.spaces( "Hide " + this._label ) );
            this._dialog_box.show();

        } else {

            this.setHTML( WIBMenuBar.spaces( "Show "+ this._label ) );
            this._dialog_box.hide();

        }
        this._dialog_box.setVisible( this._visible );

    }

    @Override
    public void action( Message m, Object o )

    {

        if ( m instanceof SetDialogVisibilityMessage ) {

            this._visible = ( ( Boolean ) o ).booleanValue();

            this.setDialogVisibility();

        } else if ( m instanceof ToggleDialogVisibilityMessage )
            this.toggleDialogVisibility();

    }

}