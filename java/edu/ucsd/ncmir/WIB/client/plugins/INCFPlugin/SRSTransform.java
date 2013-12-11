package edu.ucsd.ncmir.WIB.client.plugins.INCFPlugin;

import com.google.gwt.xml.client.Element;
import edu.ucsd.ncmir.spl.LinearAlgebra.DoubleSquareMatrix;
import edu.ucsd.ncmir.spl.XMLUtil.XML;

/**
 *
 * @author spl
 */
class SRSTransform
{

    private final DoubleSquareMatrix _dsm = new DoubleSquareMatrix( 4 );
    private final String _srs_name;

    SRSTransform( Element element )

    {

	this._srs_name = element.getAttribute( "srs_name" );
	String[] rows = XML.extractSubstringsFromElement( element, "\n" );

	for ( int j = 0; j < rows.length; j++ ) {

	    String[] cols = rows[j].split( " " );

	    for ( int i = 0; i < cols.length; i++ )
		this._dsm.setElement( j, i, Double.parseDouble( cols[i] ) );

	}

    }

    /**
     * @return The transformation matrix.
     */
    public DoubleSquareMatrix getMatrix()

    {

        return this._dsm;

    }

    /**
     * @return The SRS Name attached to this transform.
     */

    public String getSRSName()

    {

        return this._srs_name;

    }

}
