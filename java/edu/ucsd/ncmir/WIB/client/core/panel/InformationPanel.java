package edu.ucsd.ncmir.WIB.client.core.panel;

import com.google.gwt.user.client.ui.Label;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.message.MessageListener;
import edu.ucsd.ncmir.WIB.client.core.messages.InformationMessage;

/**
 *
 * @author spl
 */
public class InformationPanel
    extends AbstractWIBPanel
    implements MessageListener

{

    private final Label _label = new Label();

    public InformationPanel()

    {

        super( InformationMessage.class );

        this.setStyleName( "WIB-status-panel" );

        this.add( this._label );


    }

    @Override
    public void action( Message m, Object o )

    {

        this._label.setText( ( String ) o );

    }

}
