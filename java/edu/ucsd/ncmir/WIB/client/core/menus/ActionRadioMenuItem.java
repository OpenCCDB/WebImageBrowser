package edu.ucsd.ncmir.WIB.client.core.menus;

import edu.ucsd.ncmir.WIB.client.core.components.AbstractInteractionMessageFactory;

/**
 * Template for an "Action" menu item.
 * @author spl
 */
public class ActionRadioMenuItem
    extends AbstractActionRadioMenuItem

{

    /**
     * Creates a <code>ActionRadioMenuItem</code>.
     * @param label The label of this item.
     * @param imf An <code>AbstractInteractionMessageFactory</coce> object to
     * be used when this item is selectd.
     */
    public ActionRadioMenuItem( String label,
				AbstractInteractionMessageFactory imf )

    {

        super( label, imf );

    }

}
