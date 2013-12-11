package edu.ucsd.ncmir.WIB.client.core.components;

import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.message.MessageListener;
import edu.ucsd.ncmir.WIB.client.core.message.MessageManager;

/**
 *
 * @author spl
 */
public class IntegerMessageInputBox
    extends FilteredInputBox
    implements MessageListener,
               Filter

{

    public IntegerMessageInputBox( Class<? extends Message> message_class,
				   FilteredDataCallback callback )

    {

        super( callback );
        super.setFilter( this );

        MessageManager.registerListener( message_class, this );
        super.addKeyDownHandler( this );

    }

    @Override
    public void action( Message m, Object o )

    {

        int value = ( ( Integer ) o ).intValue();

        this.setText( value + "" );

    }

    @Override
    public boolean validate( int ch )
    {

	return ( '0' <= ch ) && ( ch <= '9' );

    }

}
