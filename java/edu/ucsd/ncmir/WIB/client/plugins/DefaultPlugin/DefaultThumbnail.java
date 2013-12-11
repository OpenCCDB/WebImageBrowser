package edu.ucsd.ncmir.WIB.client.plugins.DefaultPlugin;

import edu.ucsd.ncmir.WIB.client.core.image.AbstractThumbnail;
import edu.ucsd.ncmir.spl.LinearAlgebra.Quaternion;

/**
 *
 * @author spl
 */
public class DefaultThumbnail
    extends AbstractThumbnail

{


    public DefaultThumbnail( Quaternion q, int depth, int timestep )

    {

	super( "thumbnail" +
               "&depth=" + depth +
	       "&timestep=" + timestep +
	       q.getFormatted( "&qr=%g&qi=%g&qj=%g&qk=%g" ) );

    }

}
