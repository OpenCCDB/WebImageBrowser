package edu.ucsd.ncmir.WIB.client.core.components;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

/**
 * A simple extension to the <code>Grid</code> object which centers
 * its <code>Widget</code>s within the <code>Grid</code> cells.
 * @author spl
 */
public class CenteredGrid
    extends Grid

{

    /**
     * Creates a <code>CenteredGrid</code> with specified rows and columns.
     * @param rows Number of rows in the <code>CenteredGrid</code>.
     * @param columns Number of columns in the <code>CenteredGrid</code>.
     */
    public CenteredGrid( int rows, int columns )
        
    {
        
        super( rows, columns );
        
	HTMLTable.CellFormatter formatter = this.getCellFormatter();

	for ( int j = 0; j < rows; j++ )
	    for ( int i = 0; i < columns; i++ )
		formatter.setAlignment( j, i,
				        HasHorizontalAlignment.ALIGN_CENTER,
				        HasVerticalAlignment.ALIGN_MIDDLE );
        
    }

    @Override
    public void resizeRows( int r )

    {

	HTMLTable.CellFormatter formatter = this.getCellFormatter();

	super.resizeRows( r );
	for ( int j = 0; j < r; j++ )
	    for ( int i = 0; i < this.getColumnCount(); i++ )
		formatter.setAlignment( j, i,
				        HasHorizontalAlignment.ALIGN_CENTER,
				        HasVerticalAlignment.ALIGN_MIDDLE );
        
    }
    
}
