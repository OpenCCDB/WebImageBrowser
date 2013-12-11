package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.message_factory;

import edu.ucsd.ncmir.WIB.client.core.messages.AbstractActivationMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.DrawCompleteMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.DrawMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.DrawPolygonSetupMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.DrawPolygonActivationMessage;

/**
 *
 * @author spl
 */
public class DrawPolygonMessageFactory
    extends AbstractDrawMessageFactory

{

    public DrawPolygonMessageFactory()

    {

	super( new DrawPolygonSetupMessage(),
	       new DrawMessage(),
	       new DrawCompleteMessage() );

    }

    @Override
    public String toString()

    {

        return "Draw Object";

    }

    @Override
    public AbstractActivationMessage activateMessage()

    {

        return new DrawPolygonActivationMessage();

    }

}
