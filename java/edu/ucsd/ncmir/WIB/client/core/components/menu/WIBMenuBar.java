package edu.ucsd.ncmir.WIB.client.core.components.menu;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;

/**
 *
 * @author spl
 */
public class WIBMenuBar
    extends MenuBar

{

    /**
     * Instantiates a <code>MenuBar</code> object with the
     * <code>WIB-menu</code> style.
     * @param vertical <code>true</code> to orient the menu bar vertically.
     */
    public WIBMenuBar( boolean vertical )

    {

        super( vertical );

        this.addStyleName( "WIB-menu" );

    }

    /**
     * Instantiates a <code>MenuBar</code> object with the
     * <code>WIB-menu</code> style.
     */
    public WIBMenuBar()

    {

        this( false );

    }

    /**
     * Prepends some spaces before the <code>MenuItem</code> text,
     * then adds it to the menu tree.  This assumes that the text is
     * just a plain old <code>String</code>.
     * @param menu_item The <code>MenuItem</code> to be added.
     * @return The <code>MenuItem</code> added to the tree.
     */
    public MenuItem addOptionItem( MenuItem menu_item )

    {

	menu_item.setHTML( WIBMenuBar.spaces( menu_item.getText() ) );

	return this.addItem( menu_item );

    }

    /**
     * Adds a submenu to the current menu.
     * @param label The submenu's label.
     * @param menu The menu to be added.
     * @return The <code>MenuItem</code> added to the tree.
     */
    public MenuItem addSubmenu( String label, MenuBar menu )

    {

	return this.addItem( WIBMenuBar.spaces( label ), menu );

    }

    /**
     * Adds a <code>Command</code> menu item to the tree.
     * @param label The <code>Command</code>'s labe.
     * @param cmd The <code>Command</code> to be executed when this
     * item is chosen.
     * @return The <code>MenuItem</code> added to the tree.
     */
    public MenuItem addCommandItem( String label,
				    Scheduler.ScheduledCommand cmd )

    {

	return this.addItem( WIBMenuBar.spaces( label ), cmd );

    }

    private static final String SPACES = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";

    public static SafeHtml spaces( String label )

    {

	return SafeHtmlUtils.fromSafeConstant( WIBMenuBar.SPACES + label );

    }

}
