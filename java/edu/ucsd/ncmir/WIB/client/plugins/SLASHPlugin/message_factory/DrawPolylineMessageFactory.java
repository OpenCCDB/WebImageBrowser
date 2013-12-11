package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.message_factory;

import edu.ucsd.ncmir.WIB.client.core.messages.AbstractActivationMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.DrawCompleteMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.DrawMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.DrawPolylineActivationMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.DrawPolylineSetupMessage;

/**
 *
 * @author spl
 */
public class DrawPolylineMessageFactory
    extends AbstractDrawMessageFactory

{

    public DrawPolylineMessageFactory()

    {

	super( new DrawPolylineSetupMessage(),
	       new DrawMessage(),
	       new DrawCompleteMessage() );

    }

    @Override
    public String toString()

    {

        return "Draw Open Object";

    }

    @Override
    public AbstractActivationMessage activateMessage()
    {

        return new DrawPolylineActivationMessage();

    }

}
