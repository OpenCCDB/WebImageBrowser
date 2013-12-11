package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.mode.SLASHDatabaseMode;

import java.util.HashMap;

/**
 *
 * @author spl
 */
public class ElementMap

{

    private final HashMap<Integer,ElementData> _ed_map =
	new HashMap<Integer,ElementData>();

    ElementMap() {}

    private static class ElementData

    {

	private final String _object_name;
	private final String _user_name;
	private final int _annotation_id;
	private final String _geometry_type;
	private final int _color;

	ElementData( String object_name, 
		     String user_name, 
		     int annotation_id,
		     String geometry_type,
		     int color )

	{

	    this._object_name = object_name;
	    this._user_name = user_name;
	    this._annotation_id = annotation_id;
	    this._geometry_type = geometry_type;
	    this._color = color;

	}

        String getObjectName()

        {

            return this._object_name;

        }

        String getUserName()

        {

            return this._user_name;

        }

        int getAnnotationID()

        {

            return this._annotation_id;

        }

        String getGeometryType()

        {

            return this._geometry_type;

        }

        int getColor()

        {

            return this._color;

        }

    }

    void addInfo( int geometry_id,
		  String object_name,
		  String user_name,
		  int annotation_id,
		  String geometry_type,
		  int color )

    {

	this._ed_map.put( geometry_id,
			  new ElementData( object_name,
					   user_name,
					   annotation_id,
					   geometry_type,
					   color ) );

    }

    public String getObjectName( int geometry_id )
	
    {
	
	return this._ed_map.get( geometry_id ).getObjectName();
	
    }
    
    public String getUserName( int geometry_id )
	
    {
	
	return this._ed_map.get( geometry_id ).getUserName();
	
    }
    
    public int getAnnotationID( int geometry_id )
	
    {
	
	return this._ed_map.get( geometry_id ).getAnnotationID();
	
    }
    
    public String getGeometryType( int geometry_id )
	
    {
	
	return this._ed_map.get( geometry_id ).getGeometryType();
	
    }
    
    public int getColor( int geometry_id )
	
    {
	
	return this._ed_map.get( geometry_id ).getColor();
	
    }

}
