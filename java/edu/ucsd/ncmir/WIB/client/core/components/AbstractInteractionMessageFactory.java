package edu.ucsd.ncmir.WIB.client.core.components;

import edu.ucsd.ncmir.WIB.client.core.messages.AbstractActivationMessage;

/**
 * A factory which creates messages for sundry user interactions.
 * @author spl
 */

abstract public class AbstractInteractionMessageFactory
    extends AbstractActionMessageFactory

{

    /**
     * Called on activation to notify any components of a status change.
     */
    abstract public AbstractActivationMessage activateMessage();

}