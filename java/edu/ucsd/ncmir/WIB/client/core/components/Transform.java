package edu.ucsd.ncmir.WIB.client.core.components;

import com.google.gwt.canvas.dom.client.Context2d;
import edu.ucsd.ncmir.spl.LinearAlgebra.AbstractDoubleMatrix;
import edu.ucsd.ncmir.spl.LinearAlgebra.AbstractDoubleSquareMatrix;
import edu.ucsd.ncmir.spl.LinearAlgebra.AbstractDoubleVector;
import edu.ucsd.ncmir.spl.LinearAlgebra.DoubleVector;
import edu.ucsd.ncmir.spl.LinearAlgebra.Matrix2D;
import edu.ucsd.ncmir.spl.LinearAlgebra.SingularMatrixException;

/**
 *
 * @author spl
 */
public class Transform
    extends Matrix2D
{

    public Transform()

    {

        super();

    }

    public Transform getInverse()
        throws SingularMatrixException

    {

	AbstractDoubleSquareMatrix adsm = this.inverse();

	Transform t = new Transform();

	t.copyIn( adsm );

        return t;

    }

    public double[] transform( double x, double y )

    {

        DoubleVector dv = new DoubleVector( 3 );

        dv.setComponent( 0, x );
        dv.setComponent( 1, y );
        dv.setComponent( 2, 1 );

        AbstractDoubleVector adv = this.multiply( dv );

        return new double[] { adv.getComponent( 0 ), adv.getComponent( 1 ) };

    }

    public void translate( double x, double y )

    {

        this.update( 1, 0, 0, 1, x, y );

    }

    public void rotate( double r )

    {

	double sin = Math.sin( r );
	double cos = Math.cos( r );

	// | cos -sin  0 |
	// | sin  cos  0 |
	// |  0    0   1 |

	this.update( cos, sin, -sin, cos, 0, 0 );

    }

    public void scale( double h_scale, double v_scale )

    {

        this.update( h_scale, 0, 0, v_scale, 0, 0 );

    }

    private void update( double m11, double m12,
			 double m21, double m22,
			 double x, double y )

    {

        AbstractDoubleMatrix mout =
	    this.multiply( new Matrix2D( m11, m12, m21, m22, x, y ) );
        this.copyIn( mout );

    }

    private void copyIn( AbstractDoubleMatrix min )

    {

        for ( int j = 0; j < min.rows(); j++ )
	    for ( int i = 0; i < min.columns(); i++ )
		this.setElement( j, i, min.getElement( j, i ) );

    }

    public void setContextTransform( Context2d c2d )

    {

        c2d.setTransform( this.getM11(),
			  this.getM12(),
			  this.getM21(),
			  this.getM22(),
			  this.getX(),
			  this.getY() );
    }

    public void updateContextTransform( Context2d c2d )

    {

	c2d.transform( this.getM11(),
		       this.getM12(),
		       this.getM21(),
		       this.getM22(),
		       this.getX(),
		       this.getY() );

   }

}
