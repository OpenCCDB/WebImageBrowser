package edu.ucsd.ncmir.WIB.client.core;

import edu.ucsd.ncmir.WIB.client.core.drawable.Drawable;
import edu.ucsd.ncmir.spl.LinearAlgebra.Quaternion;
import java.util.HashMap;

/**
 *
 * @author spl
 */
public final class Annotation
    extends Drawable

{

    private static HashMap<Integer,AnnotationData> _annotation_table =
	new HashMap<Integer,AnnotationData>();

    private AnnotationData _annotation_data = null;

    public Annotation()

    {

	super();
	this._annotation_data = new AnnotationData();

    }

    public Annotation( int annotation_id )

    {

	super();
	this.setAnnotationID( annotation_id );

    }

    @Override
    public void setRGB( int red, int green, int blue )

    {

	this._annotation_data.setRed( red );
	this._annotation_data.setGreen( green );
	this._annotation_data.setBlue( blue );

    }

    @Override
    public int getRed()

    {

	return this._annotation_data.getRed();

    }

    @Override
    public int getGreen()

    {

	return this._annotation_data.getGreen();

    }

    @Override
    public int getBlue()

    {

	return this._annotation_data.getBlue();

    }

    private int _plane;
    private Quaternion _quaternion;

    /**
     * @return the plane
     */

    public int getPlane()

    {

        return this._plane;

    }

    /**
     * @param plane the plane to set
     */

    public void setPlane( int plane )

    {

        this._plane = plane;

    }

    /**
     * @return the quaternion
     */

    public Quaternion getQuaternion()

    {

        return this._quaternion;

    }

    /**
     * @param quaternion the quaternion to set
     */

    public void setQuaternion( Quaternion quaternion )

    {

        this._quaternion = quaternion;

    }

    private int _model_id;

    public void setModelID( int model_id )

    {

        this._model_id = model_id;

    }

    /**
     * Returns the Model ID of this set of points.
     * @return the Model ID
     */

    public int getModelID()

    {

        return this._model_id;

    }

    private int _annotation_id;

    public void setAnnotationID( int annotation_id )

    {


	this._annotation_id = annotation_id;

	Integer a_id = new Integer( annotation_id );

	if ( this._annotation_data == null ) {

	    this._annotation_data = Annotation._annotation_table.get( a_id );
	    if ( this._annotation_data == null ) {

		this._annotation_data = new AnnotationData();
		Annotation._annotation_table.put( a_id, this._annotation_data );

	    }

	} else
	    Annotation._annotation_table.put( a_id, this._annotation_data );

    }

    /**
     * Returns the Annotation ID of this set of points.
     * @return The Annotation ID.
     */
    public int getAnnotationID()

    {

	return this._annotation_id;

    }

    private int _geometry_id;

    /**
     * Sets the Geometry ID of this set of points.
     * @param geometry_id The Geometry ID.
     */
    public void setGeometryID( int geometry_id )

    {

	this._geometry_id = geometry_id;

    }

    public int getGeometryID()

    {

	return this._geometry_id;

    }

    @Override
    public void setObjectName( String object_name )

    {

        this._annotation_data.setName( object_name.toLowerCase() );

    }

    @Override
    public String getObjectName()

    {

        return this._annotation_data.getName();

    }

    public void setUserName( String user_name )

    {

        this._annotation_data.setUserName( user_name );

    }

    public String getUserName()

    {

        return this._annotation_data.getUserName();

    }

    public void setDescription( String description )

    {

        this._annotation_data.setDescription( description );

    }

    public void setDesanitizedDescription( String description )

    {

        this._annotation_data.setDesanitizedDescription( description );

    }

    public String getSanitizedDescription()

    {

        return this._annotation_data.getSanitizedDescription();

    }

    public String getDescription()

    {

        return this._annotation_data.getDescription();

    }

    public void setURI( String uri )

    {

        this._annotation_data.setURI( uri );

    }

    public String getURI()

    {

        return this._annotation_data.getURI();

    }

    private static class AnnotationData

    {

        private String _user_name;
        private String _name;
        private String _uri;
        private String _description;
        private int _red;
        private int _green;
        private int _blue;

        /**
         * @return the _user_name
         */

        public String getUserName()

        {

            return this._user_name;

        }

        /**
         * @param user_name the _user_name to set
         */

        public void setUserName( String user_name )

        {

            this._user_name = user_name;

        }

        /**
         * @return the _name
         */

        public String getName()

        {

            return this._name;

        }

        /**
         * @param name the _name to set
         */

        public void setName( String name )

        {

            this._name = name;

        }

        /**
         * @return the _uri
         */

        public String getURI()

        {

            return this._uri;

        }

        /**
         * @param uri the _uri to set
         */

        public void setURI( String uri )

        {

            this._uri = uri;

        }

        /**
         * @return the _description
         */

        public String getDescription()

        {

            return this._description;

        }

        public String getSanitizedDescription()

        {

	    return AnnotationData.sanitize( this.getDescription() );

        }

        /**
         * @param description the _description to set
         */

        public void setDescription( String description )

        {

            this._description = description;

        }

	public void setDesanitizedDescription( String description )

	{

	    this.setDescription( AnnotationData.desanitize( description ) );

	}

        /**
         * @return the _red
         */

        public int getRed()

        {

            return this._red;

        }

        /**
         * @param red the _red to set
         */

        public void setRed( int red )

        {

            this._red = red;

        }

        /**
         * @return the _green
         */

        public int getGreen()

        {

            return this._green;

        }

        /**
         * @param green the _green to set
         */

        public void setGreen( int green )

        {

            this._green = green;

        }

        /**
         * @return the _blue
         */

        public int getBlue()

        {

            return this._blue;

        }

        /**
         * @param blue the _blue to set
         */

        public void setBlue( int blue )

        {

            this._blue = blue;

        }

	private static CharSequence[][] _table = {
	    { "%25", "%" },
	    { "%26", "&" },
	    { "%3c", "<" },
	    { "%3e", ">" },
	    { "%3f", "?" },
	    { "%27", "'" }
	};

	private static String desanitize( String s )

	{

	    for ( CharSequence[] st : AnnotationData._table )
		s = s.replace( st[0], st[1] );

	    return s;

	}

	private static String sanitize( String s )

	{

	    for ( CharSequence[] st : AnnotationData._table )
		s = s.replace( st[1], st[0] );

	    return s;

	}

    }

}
