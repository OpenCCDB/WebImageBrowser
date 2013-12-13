#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <stdarg.h>
#include <float.h>
#include <time.h>
#include <sys/time.h>
#include <signal.h>
#include <sys/prctl.h>
#include <sys/types.h>
#include <sys/param.h>
#include <sys/stat.h>
#include <sys/resource.h>
#include <errno.h>
#include <math.h>
#include <openssl/sha.h>

#include <spl.h>
#include <splweb.h>
#include <splxml.h>
#include <splpq.h>
#include <bjhash.h>

#include "version.h"
#include "build.h"

#include "SLASHdb_defs.h"
#include "SLASHdb_statics.h"

int main( int argc, char **argv )

{

    initLogger( "/var/tmp/SLASHdb.log" );

    catch( signal_handler );

    struct timeval tv1;
    gettimeofday( &tv1, NULL );
    
    prctl( PR_SET_PDEATHSIG, SIGHUP );    
    umask( 0111 );

    char *ip = getenv( "REMOTE_ADDR" );
    if ( ip == NULL )
	ip = "unknown";
    logger( "SLASH DB Version %s Query Start [IP %s]", VERSION, ip );

    if ( !parseQuerystring() )
	die( "Missing or empty QUERY_STRING" );
    logger( "QUERY_STRING: %s", getQuerystring() );
    logQuerystring();

    Logical ok = False;

    char *request = getQuerystringString( "request" );

    ElementP root = XMLCreateElement( "root" );

    if ( SET_EXCEPTION_HANDLER( exception_handler, root ) ) {

	signal( SIGCHLD, SIG_IGN );

	XMLAddProperty( root, "process_id", "%d", getpid() );
	
	if ( strcmp( request, "get_contours" ) == 0 )
	    ok = get_contours( root );
	else if ( strcmp( request, "get_contour_data" ) == 0 )
	    ok = get_contour_data( root );
	else if ( strcmp( request, "dataset" ) == 0 )
	    ok = get_dataset( root );
	else if ( strcmp( request, "get_sequence" ) == 0 )
	    ok = get_sequence( root );
	else if ( strcmp( request, "get_objects" ) == 0 )
	    ok = get_objects( root );
	else if ( strcmp( request, "get_all_contours" ) == 0 )
	    ok = get_all_contours( root );
	else if ( strcmp( request, "get_surface" ) == 0 )
	    ok = get_surface( root );
	else if ( strcmp( request, "get_annotation_z_levels" ) == 0 )
	    ok = get_annotation_z_levels( root );
	else if ( strcmp( request, "add_contour" ) == 0 )
	    ok = add_contour( root );
	else if ( strcmp( request, "delete_contour" ) == 0 )
	    ok = delete_contour( root );
	else if ( strcmp( request, "update_contour" ) == 0 )
	    ok = update_contour( root );
	else if ( strcmp( request, "add_annotation" ) == 0 )
	    ok = add_annotation( root );
	else if ( strcmp( request, "update_annotation" ) == 0 )
	    ok = update_annotation( root );
	else if ( strcmp( request, "get_annotation" ) == 0 )
	    ok = get_annotation( root );
	else if ( strcmp( request, "delete_annotation" ) == 0 )
	    ok = delete_annotation( root );
	else if ( strcmp( request, "set_geometry_type" ) == 0 )
	    ok = set_geometry_type( root );
	else if ( strcmp( request, "validate_name" ) == 0 )
	    ok = validate_name( root );
	else {
	    
	    ElementP error = XMLCreateElement( "error" );
	    XMLAddChild( root, error );
	    XMLAddContent( error, "Request '%s' is unknown.", request );
	    logger( "Request '%s' is unknown.", request );
	    
	}

    }

    struct timeval tv2;
    gettimeofday( &tv2, NULL );
    
    XMLAddProperty( root, "status", ok ? "success" : "fail" );

    XMLAddProperty( root, "elapsed_seconds", "%g", 
		    ( tv2.tv_sec - tv1.tv_sec ) +
		    ( ( tv2.tv_usec - tv1.tv_usec ) / 1.0e6 ) );


    signal( SIGCHLD, SIG_IGN );
    FILE *f = popen( "gzip -f", "w" );
    XMLWrite( f, root );
    pclose( f );

    logger( "Request complete." );
    exit( 0 );
    
}

static void exception_handler( char *message, void *data )

{

    ElementP root = ( ElementP ) data;

    ElementP error = XMLCreateElement( "error" );
    XMLAddChild( root, error );
    XMLAddContent( error, message );
    logger( message );
    
}    

static int get_user_id( PGconn *conn )

{

    char *user_name = getQuerystringString( "user_name" );

    PGresult *result = 
	PQQuery( conn,
		 "select user_id from slash_user where user_name='%s'",
		 user_name );
    
    return PQGetInt( result, 0, "user_id" );

}

static Logical delete_contour( ElementP root )

{

    PGconn *conn = PQGetConnection();

    int geom_id = getQuerystringInt( "id" );

    PGresult *result = PQQuery( conn, ANNOTATION_FROM_GEOMID_FMT, geom_id );

    int annotation_id = PQGetInt( result, 0, "annotation_id" );

    delete_geometry( conn, geom_id );

    result = PQQuery( conn, COUNT_GEOM_FMT, annotation_id );

    int count = PQGetInt( result, 0, "count" );
    
    ElementP deleted = XMLCreateElement( "deleted" );
    XMLAddChild( root, deleted );
    XMLAddProperty( deleted, "geometry_id", "%d", geom_id );
    XMLAddProperty( deleted, "annotation_id", "%d", annotation_id );
    XMLAddProperty( deleted, "empty", "%s", count == 0 ? "true" : "false" );

    PQfinish( conn );

    return True;

}

static Logical get_annotation_z_levels( ElementP root )

{

    PGconn *conn = PQGetConnection();

    int annotation_id = getQuerystringInt( "id" );

    PGresult *result = PQQuery( conn, ANNOTATION_Z_RANGE_FMT, annotation_id );

    int zmin = INT_MAX;
    int zmax = -INT_MAX;

    int n = PQntuples( result );

    Logical ok = False;
    if ( n > 0 ) {

	ElementP z_range = XMLCreateChildElement( root, "z_range" );
	XMLAddProperty( z_range, "id", "%d", annotation_id );
	int *zlist = ( int *) malloc( sizeof( int ) * n );
	
	for ( int i = 0; i < n; i++ )
	    zlist[i] = PQGetInt( result, i, "z_index" );
	
	for ( int i = 0; i < n - 1; i++ )
	    for ( int j = i; j < n; j++ )
		if ( zlist[i] > zlist[j] ) {
		    
		    int temp = zlist[i];
		    
		    zlist[i] = zlist[j];
		    zlist[j] = temp;
		    
		}
	
	XMLAddProperty( z_range, "min", "%d", zlist[0] );
	XMLAddProperty( z_range, "max", "%d", zlist[n - 1] );
	
	for ( int i = 0; i < n; i++ )
	    XMLAddContent( z_range, "%d ", zlist[i] );
	ok = True;

    } else
	XMLAddProperty( root, "reason", "id %d not found", annotation_id ); 
    PQfinish( conn );

    return ok;

}

static Logical validate_name( ElementP root )

{

    PGconn *conn = PQGetConnection();

    int annotation_id = getQuerystringInt( "id" );
    int model_id = getQuerystringInt( "model_id" );
    char *name = getQuerystringString( "name" );

    PGresult *result;

    if ( ( model_id != -1 ) && ( model_id != INT_MAX ) )
	result = PQQuery( conn,
			  COUNT_NAME_VERSION_FMT,
			  name, annotation_id, model_id );
    else
	result = PQQuery( conn,
			  COUNT_NAME_FMT,
			  name, annotation_id );

    int count = PQGetInt( result, 0, "count" );

    ElementP validated = XMLCreateChildElement( root, "validated" );

    XMLAddProperty( validated, "annotation_id", "%d", annotation_id );
    XMLAddProperty( validated, "model_id", "%d", model_id );
    XMLAddProperty( validated, "name", "%s", name );
    XMLAddProperty( validated, "unique", "%s", count == 0 ? "true" : "false" );
    
    PQfinish( conn );

    return True;

}

static Logical update_contour( ElementP root )

{

    PGconn *conn = PQGetConnection();

    int geom_id = getQuerystringInt( "id" );

    char *points = get_points();
    
    PQQuery( conn,
	     "update slash_geometry set polyline = "
	     "ST_GeomFromText('LINESTRING(%s)')"
	     " where geom_id = %d", 
	     points, geom_id );
    
    PQfinish( conn );

    ElementP updated = XMLCreateElement( "updated" );
    XMLAddChild( root, updated );
    XMLAddProperty( updated, "geometry_id", "%d", geom_id );

    return True;

}

static Logical set_geometry_type( ElementP root )

{

    PGconn *conn = PQGetConnection();

    int geom_id = getQuerystringInt( "id" );
    Logical closed = getQuerystringLogical( "closed" );

    PQQuery( conn,
	     "update slash_geometry set "
	     "geometry_type = 'poly%s' "
	     "where geom_id = %d", 
	     closed ? "gon" : "line", geom_id );
    
    PQfinish( conn );

    ElementP updated = XMLCreateElement( "updated" );
    XMLAddChild( root, updated );
    XMLAddProperty( updated, "geometry_id", "%d", geom_id );

    return True;

}

static void delete_geometry( PGconn *conn, int geom_id )

{
    
    PQQuery( conn,
	     "delete from slash_annot_geom_map where geometry_id=%d",
	     geom_id );
    
    PQQuery( conn,
	     "delete from slash_geometry where geom_id=%d",
	     geom_id );

}

static Logical delete_annotation( ElementP root )

{

    PGconn *conn = PQGetConnection();

   int annotation_id = getQuerystringInt( "id" );

    PGresult *result;

    result = PQQuery( conn,
		      "select geometry_id from slash_annot_geom_map where "
		      "annotation_id = %d", annotation_id );

    int ntuples = PQntuples( result );
    
    for ( int tuple = 0; tuple < ntuples; tuple++ ) {

	int geom_id = PQGetInt( result, tuple, "geometry_id" );

	delete_geometry( conn, geom_id );

    }

    int model_id = getQuerystringInt( "model_id" );

    if ( ( model_id != -1 ) && ( model_id != INT_MAX ) ) {

	PQQuery( conn,
		 "delete from slash_applicatoin_data where "
		 "annotation_id=%d and "
		 "application_key='WIB:SLASH' and "
		 "version_number=%d",
		 annotation_id, model_id );
	PQQuery( conn,
		 "delete from slash_annotation where "
		 "annotation_id=%d and "
		 "version_number=%d",
		 annotation_id, model_id );

    } else {

	PQQuery( conn,
		 "delete from slash_applicatoin_data where "
		 "application_key='WIB:SLASH' and "
		 "annotation_id=%d",
		 annotation_id );
	PQQuery( conn,
		 "delete from slash_annotation where "
		 "annotation_id=%d",
		 annotation_id );

    }    
    PQfinish( conn );

    return True;

}

static Logical add_annotation( ElementP root )

{

    PGconn *conn = PQGetConnection();

    int annotation_id = get_next_sequence( conn );

    int model_id = getQuerystringInt( "model_id" );
    if ( model_id == INT_MAX )
	throw_exception( "model_id '%s' is invalid number.",
			 getQuerystringString( "model_id" ) );

    PQQuery( conn,
	     "insert into slash_annotation("
	     "annotation_id,"
	     "dataset_id,"
	     "object_name,"
	     "object_name_ont_uri,"
	     "ontology_name,"
	     "orientation_w,"
	     "orientation_x,"
	     "orientation_y,"
	     "orientation_z,"
	     "color_int,"
	     "is_training_data) values ("
	     "%d, %d, '%s', '%s', '%s', 1, 0, 0, 0, %d, 'f')",
	     annotation_id,
	     getQuerystringInt( "dataset_id" ),
	     getQuerystringString( "name" ),
	     getQuerystringString( "name_ontology_uri" ),
	     getQuerystringString( "ontology_name" ),
	     getQuerystringInt( "color" ) );
    
    if ( model_id != -1 )
	PQQuery( conn,
		 "update slash_annotation "
		 "set version_number = %d "
		 "where annotation_id = %d",
		 model_id, annotation_id );

    int application_data_id = get_next_sequence( conn );
    char *xml = generate_app_data();

    if ( ( model_id == -1 ) || ( model_id == INT_MAX ) )
	PQQuery( conn,
		 "insert into slash_applicatoin_data ( "
		 "application_data_id, "
		 "annotation_id, "
		 "application_key, "
		 "application_value ) "
		 "values ( "
		 "%d, "
		 "%d, "
		 "'WIB:SLASH', "
		 "'%s' )",
		 application_data_id,
		 annotation_id, 
		 xml );
    else
	PQQuery( conn,
		 "insert into slash_applicatoin_data ( "
		 "application_data_id, "
		 "annotation_id, "
		 "version_id, "
		 "application_key, "
		 "application_value ) "
		 "values ( "
		 "%d, "
		 "%d, "
		 "%d, "
		 "'WIB:SLASH', "
		 "'%s' )",
		 application_data_id,
		 annotation_id, 
		 model_id,
		 xml );

    ElementP annotation = XMLCreateElement( "annotation" );
    XMLAddChild( root, annotation );
    
    XMLAddProperty( annotation, "id", "%d", annotation_id );

    PQfinish( conn );

    return True;

}

static Logical update_annotation( ElementP root )

{

    PGconn *conn = PQGetConnection();

    int annotation_id = getQuerystringInt( "annotation_id" );
    if ( annotation_id == INT_MAX )
	throw_exception( "annotation_id '%s' is invalid number.",
			 getQuerystringString( "annotation_id" ) );

    PQQuery( conn,
	     "update slash_annotation set "
	     "object_name='%s', "
	     "object_name_ont_uri='%s', "
	     "color_int=%d, "
	     "ontology_name='%s' "
	     "where "
	     "annotation_id=%d",
	     getQuerystringString( "name" ),
	     getQuerystringString( "name_ontology_uri" ),
	     getQuerystringInt( "color" ),
	     getQuerystringString( "ontology_name" ),
	     annotation_id );
    
    PQQuery( conn,
	     "update slash_applicatoin_data set "
	     "application_value='%s' "
	     "where "
	     "application_key='WIB:SLASH' and "
	     "annotation_id=%d",
	     generate_app_data(),
	     annotation_id );

    ElementP annotation = XMLCreateElement( "annotation" );
    XMLAddChild( root, annotation );
    
    XMLAddProperty( annotation, "id", "%d", annotation_id );

    PQfinish( conn );

    return True;

}

static char *generate_app_data( void )

{

    ElementP app_data = XMLCreateElement( "app_data" );
    ElementP ontology = XMLCreateElement( "ontology" );
    XMLAddChild( app_data, ontology );
    ElementP note = XMLCreateElement( "note" );
    XMLAddChild( ontology, note );

    XMLAddContent( note, "%s", sanitize( getQuerystringString( "note" ) ) );
    
    return XMLToString( app_data );

}

static char *sanitize( char *s )

{

    char *ctable[][2] = {
	{ "%", "%25" },
	{ "&", "%26" },
	{ "<", "%3c" },
	{ ">", "%3e" },
	{ "?", "%3f" },
	{ "'", "%27" },
    };

    for ( int i = 0; i < N( ctable ); i++ ) {

	char **ch = ctable[i];

	char *os = s;
	char *sos = "";

	char *sp;
	while ( sp = strstr( s, ch[0] ) ) {

	    *sp = '\0';

	    sos = string( "%s%s%s", sos, s, ch[1] );

	    s = sp + 1;

	}
	s = string( "%s%s", sos, s );
	free( os );

    }

    return s;

}
	    
static Logical get_annotation( ElementP root )

{

    PGconn *conn = PQGetConnection();

    int annotation_id = getQuerystringInt( "id" );
    int model_id = getQuerystringInt( "model_id" );

    PGresult *result;

    result = ( ( model_id == -1 ) ||
	       ( model_id == INT_MAX ) ) ? 
	PQQuery( conn,
		 "select * from slash_annotation "
		 "where annotation_id = %d ",
		 annotation_id ) :
	PQQuery( conn,
		 "select * from slash_annotation "
		 "where annotation_id = %d and version_number = %d",
		 annotation_id, model_id );

    if ( PQntuples( result ) == 1 ) {

	ElementP annotation = XMLCreateElement( "annotation" );
	
	XMLAddChild( root, annotation );
	
	XMLAddProperty( annotation, "id", "%d", annotation_id );
	XMLAddProperty( annotation, "model_id", "%d", model_id );
	XMLAddProperty( annotation, "geometry_type", 
			PQGetString( result, 0, "geometry_type" ) );
	XMLAddProperty( annotation, "object_name", 
			PQGetString( result, 0, "object_name" ) );
	XMLAddProperty( annotation, "ontology_name", 
			PQGetString( result, 0, "ontology_name" ) );
	XMLAddProperty( annotation, "color", 
			PQGetString( result, 0, "color_int" ) );
	
	result = ( ( model_id == -1 ) ||
		   ( model_id == INT_MAX ) ) ? 
	    PQQuery( conn,
		     "select application_value from slash_applicatoin_data "
		     "where annotation_id = %d and application_key='WIB:SLASH'",
		     annotation_id ) :
	    PQQuery( conn,
		     "select application_value from slash_applicatoin_data "
		     "where annotation_id = %d and application_key='WIB:SLASH' "
		     "and version_id = %d",
		     annotation_id, model_id );
	
	ElementP application_data = XMLCreateElement( "application_data" );

	XMLAddChild( annotation, application_data );
     
	XMLAddContent( application_data,
		       encode( PQGetString( result,
					    0,
					    "application_value" ) ) );

    } else
	throw_exception( "annotation id: %d, model id: %d does not exist.",
			 annotation_id, model_id );
    return True;

}

static char *encode( char *string )

{

    char *out = ( char *) calloc( 1, 1 );

    if ( string != NULL ) {

	int l = strlen( string );

	out = ( char *) realloc( out, ( l * 2 ) + 1 );

	for ( int i = 0; i < l; i++ )
	    snprintf( out + ( i * 2 ), 3, "%02x", 
		      ( unsigned int ) string[i] );
	out[l * 2] = '\0';

    }

    return out;

}

static Logical add_contour( ElementP root )

{

    PGconn *conn = PQGetConnection();

    int geom_id = get_next_sequence( conn );
    int map_id = get_next_sequence( conn );
    int user_id = get_user_id( conn );

    int annotation_id = getQuerystringInt( "annotation_id" );
    double offset = getQuerystringDouble( "z" );

    struct timeval tv;
    gettimeofday( &tv, NULL );
    
    struct tm *tmp = localtime( &tv.tv_sec );
    
    char buffer[512];
    strftime( buffer, sizeof( buffer ), "%Y-%m-%d %H:%M:%S", tmp );
    
    char *points = get_points();
    
    PGresult *result;
    
    result = PQQuery( conn,
		      "insert into slash_geometry(geom_id, "
		      "polyline, user_id, z_index, modified_time, "
		      "geometry_type) values( %d, "
		      "ST_GeomFromText('LINESTRING(%s)'),"
		      "%d, %g, '%s.%03d', 'poly%s' )",
		      geom_id, points, user_id, offset, buffer,
		      tv.tv_usec / 1000, 
		      ( getQuerystringLogical( "closed" ) ? "gon" : "line" ) );
    
    result = PQQuery( conn,
		      "insert into slash_annot_geom_map(map_id,"
		      "annotation_id, "
		      "geometry_id) values( %d, %d, %d )",
		      map_id, annotation_id, geom_id );
    
    ElementP geometry = XMLCreateElement( "geometry" );
    XMLAddChild( root, geometry );
    
    XMLAddProperty( geometry, "annotation_id", "%d", annotation_id );
    XMLAddProperty( geometry, "geometry_id", "%d", geom_id );
    
    free( points );

    PQfinish( conn );

    return True;

}

static char *get_points( void )

{

    char buffer[512];

    char *text = ( char *) calloc( sizeof( char ), 1 );
    int len = 1;

    while ( fgets( &buffer[0], sizeof( buffer ), stdin ) ) {

	buffer[strlen( buffer ) - 1] = '\0';
	text = ( char *) realloc( text, len += strlen( buffer ) + 1 );
	strcat( text, buffer );
	strcat( text, "," );

    }
    text[len - 2] = '\0';	/* Overwrite the trailing comma */

    return text;

}

static void add_contour_trap( char *message, void *data )

{

    ElementP root = ( ElementP ) data;

    ElementP error = XMLCreateElement( "error" );
    XMLAddChild( root, error );

    XMLAddContent( error, message );

}    

static Logical get_sequence( ElementP root )

{

    PGconn *conn = PQGetConnection();

    int next_sequence = get_next_sequence( conn );

    ElementP sequence = XMLCreateElement( "sequence" );
    XMLAddChild( root, sequence );
    XMLAddProperty( sequence, "number", "%d", next_sequence );

    PQfinish( conn );

    return True;

}    

static Logical get_dataset( ElementP root )

{

    PGconn *conn = PQGetConnection();
    PGresult *result;
    int dataset_id = getQuerystringInt( "id" );
    result = PQQuery( conn, DSID_FMT, dataset_id );

    ElementP resource_path_element =
	XMLCreateChildElement( root, "resource_path" );

    XMLAddProperty( resource_path_element,
		    "uri", 
		    PQGetString( result, 0, "resource_path" ) );
    XMLAddProperty( resource_path_element,
		    "zoomify_folder", 
		    PQGetString( result, 0, "zoomify_folder" ) );
    
    PQfinish( conn );

    return True;

}

static Logical get_objects( ElementP root )

{

    int dataset_id = getQuerystringInt( "id" ); 
    int model_id = getQuerystringInt( "model_id" );

    Logical ok;
    if ( dataset_id != INT_MAX ) {

	PGconn *conn = PQGetConnection();
	PGresult *result;

	if ( ( model_id != -1 ) && ( model_id != INT_MAX ) )
	    result =
		PQQuery( conn, OBJECT_MODEL_REQ_FMT, dataset_id, model_id );
	else
	    result = PQQuery( conn, OBJECT_REQ_FMT, dataset_id );
	
	int n = PQntuples( result );

	ElementP objects_element = XMLCreateChildElement( root, "objects" );
	
	char *context = ( char *) calloc( 1, 1 );
	size_t sc = 1;
	off_t oc = 0;
	
	for ( int i = 0; i < n; i++ ) {
	    
	    char buffer[65536];
	    
	    snprintf( buffer, sizeof( buffer ),
		      "%s,%s\n",
		      PQGetString( result, i, "object_name" ),
		      PQGetString( result, i, "annotation_id" ) );
	    
	    size_t l = strlen( buffer );
	    
	    while ( sc < ( oc + l ) ) {
		
		sc *= 2;
		context = ( char *) realloc( context, sc );
		
	    }
	    memcpy( context + oc, buffer, l );
	    oc += l;
	    
	}
	if ( oc > 0 ) {

	    context = ( char *) realloc( context, sc + 1 );
	    context[oc] = '\0';
	    XMLAddContent( objects_element, context );

	}
	
	PQfinish( conn );
	ok = True;

    } else {

	XMLAddProperty( root, "reason", "Dataset ID missing!" );
	ok = False;

    }

    return ok;

}

static Logical get_all_contours( ElementP root )

{

    int annotation_id = getQuerystringInt( "id" ); 

    Logical ok;

    if ( annotation_id != INT_MAX ) {

	PGconn *conn = PQGetConnection();

	PGresult *result;

	result =
	    PQQuery( conn,
		     "select color_int "
		     "from slash_annotation where "
		     "annotation_id = %d",
		     annotation_id );

	int color = PQGetInt( result, 0, "color_int" );

	result = PQQuery( conn, GET_GEOM_FMT, annotation_id );
	
	int n = PQntuples( result );

	ElementP contours = XMLCreateChildElement( root, "contours" );

	XMLAddProperty( contours, "annotation_id", "%d", annotation_id );
	XMLAddProperty( contours, "color", "%d", color );

	for ( int i = 0; i < n; i++ ) {

	    double *xpoints;
	    double *ypoints;
	    size_t n_points;

	    ElementP contour = XMLCreateChildElement( contours, "contour" );

	    XMLAddProperty( contour, "geometry_type", 
			    PQGetString( result, i, "geometry_type" ) );
	    XMLAddProperty( contour, "z_index", "%d",
			    PQGetInt( result, i, "z_index" ) );

	    parse_points( &n_points, &xpoints, &ypoints,
			  PQGetString( result, i, "polyline" ) );
	    
	    for ( int p = 0; p < n_points; p++ )
		XMLAddContent( contour, "%g %g\n", xpoints[p], ypoints[p] );

	    free( xpoints );
	    free( ypoints );

	}
	ok = True;
	PQfinish( conn );

    } else {

	XMLAddProperty( root, "reason", "Annotation ID missing!" );
	ok = False;

    }

    return ok;

}

static Logical get_contours( ElementP root )

{

    int model_id = getQuerystringInt( "model_id" );
    int id = getQuerystringInt( "id" );
    int z = getQuerystringInt( "z" );

    XMLAddProperty( root, "z", "%d", z );

    PGconn *conn = PQGetConnection();

    char *modid = "";
    if ( model_id != -1 )
	modid = string( "and a.version_number = %d", model_id );

    PGresult *ares =
	PQQuery( conn, 
		 "select distinct a.annotation_id, "
		 "a.color_int, "
		 "a.object_name "
		 "from slash_annotation a, "
		 "slash_geometry g, "
		 "slash_annot_geom_map m "
		 "where "
		 "a.dataset_id = %d "
		 "and "
		 "g.geom_id = m.geometry_id "
		 "and "
		 "m.annotation_id = a.annotation_id "
		 "and "
		 "g.z_index = %d %s",
		 id, z, modid );

    if ( ares == NULL )
	throw_exception( "libpq error: NULL response from request" );

    int anns = PQntuples( ares );

    for ( int i = 0; i < anns; i++ ) {
	
	int annotation_id = PQGetInt( ares, i, "annotation_id" );

	PGresult *gres = 
	    PQQuery( conn,
		     "select "
		     "md5( g.polyline || g.geom_id), "
		     "g.geometry_type, "
		     "u.user_name, "
		     "map.geometry_id "
		     "from slash_geometry g "
		     "LEFT JOIN slash_annot_geom_map map "
		     "on g.geom_id = map.geometry_id "
		     "LEFT JOIN slash_user u "
		     "on u.user_id = g.user_id "
		     "where "
		     "map.annotation_id = %d and "
		     "g.z_index = %d",
		     annotation_id, z );	

	if ( gres == NULL )
	    throw_exception( "libpq error: NULL response from request" );

	int geoms = PQntuples( gres );

	if ( geoms > 0 ) {

	    ElementP annotation = XMLCreateChildElement( root, "annotation" );
	    
	    XMLAddProperty( annotation, "id", "%d", annotation_id );
	    XMLAddProperty( annotation, "color", "%d", 
			    PQGetInt( ares, i, "color_int" ) );
	    XMLAddProperty( annotation, "object_name",
			    PQGetString( ares, i, "object_name" ) );
	
	    for ( int j = 0; j < geoms; j++ ) {
		
		ElementP geometry =
		    XMLCreateChildElement( annotation, "geometry" );
		
		XMLAddProperty( geometry, "user_name", 
				PQGetString( gres, j, "user_name" ) );
		XMLAddProperty( geometry, "md5", 
				PQGetString( gres, j, "md5" ) );
		XMLAddProperty( geometry, "geometry_id", 
				"%d", PQGetInt( gres, j, "geometry_id" ) );
		XMLAddProperty( geometry, "geometry_type", 
				PQGetString( gres, j, "geometry_type" ) );
		
	    }

	}

    }

    PQfinish( conn );

    return True;

}

static Logical get_contour_data( ElementP root )

{

    PGconn *conn = PQGetConnection();

    char *ids = strdup( getQuerystringString( "id" ) );
//Willy edit
    Logical simC = False;

    char *simplify = strdup( getQuerystringString( "simplify_contour" ) );
    if(strcmp("true",simplify)==0)
	simC = True;
	

///////////


    char *id;
    
    char *s = "";
    char *u = "";
    while ( id = strtok( ids, "," ) ) {

	s = string( "%s%sselect g.polyline, "
		    "g.geom_id, "
		    "g.z_index, "
		    "m.annotation_id from "
		    "slash_geometry g, slash_annot_geom_map m "
		    "where m.geometry_id = g.geom_id and "
		    "g.geom_id = %s", s, u, id );
	u = " union ";
	ids = NULL;

    }

    PGresult *result = PQQuery( conn, s );

    int n = PQntuples( result );
    for ( int i = 0; i < n; i++ ) {

	ElementP geometry = XMLCreateChildElement( root, "geometry" );

	XMLAddProperty( geometry, "geometry_id", "%d",
			PQGetInt( result, i, "geom_id" ) );
	XMLAddProperty( geometry, "annotation_id", "%d",
			PQGetInt( result, i, "annotation_id" ) );
	XMLAddProperty( geometry, "z", "%d",
			PQGetInt( result, i, "z_index" ) );

	double *xpoints;
	double *ypoints;
	size_t n_points;

	parse_points( &n_points, &xpoints, &ypoints,
		      PQGetString( result, i, "polyline" ) );
	
 	//Willy
	if(n_points > 2000 || simC)
	  simplify_points( &n_points, &xpoints, &ypoints );

	for ( int p = 0; p < n_points; p++ )
	    XMLAddContent( geometry, "%g %g\n", xpoints[p], ypoints[p] );
	free( xpoints );
	free( ypoints );

    }
    PQfinish( conn );

    return True;

}

static void fail( char *message, void *data )

{

    ElementP root = ( ElementP ) data;
    ElementP error = XMLCreateElement( "error" );
    
    XMLAddChild( root, error );
    XMLAddContent( error, message );

    logger( message );

}

static int get_next_sequence( PGconn *conn )

{

    PGresult *result = PQQuery( conn, "select nextval('general_sequence')" );
    int nv = PQGetInt( result, 0, "nextval" );

    return nv;

}

/*

	Willy test method to print data to a file

*/
void adx_store_data(const char *filepath, const char *data)
{
    FILE *fp = fopen(filepath, "ab");
    if (fp != NULL)
    {
        fputs(data, fp);
        fclose(fp);
    }
}

static void parse_points( size_t *n, double **x, double **y, char *string )

{

    //adx_store_data("/tmp/SLASBdb.log",string);

    unsigned int type;
    parse_hex( &type, string + 2, 4 );
	
    off_t offset = 0;
    if ( type == 2 )
	offset = 10;
    else if ( type == 3 )
	offset = 18;

    unsigned int length;
    parse_hex( &length, string + offset, 4 );

    offset += 8;

    *n = length;

    *x = ( double *) malloc( sizeof( double ) * length );
    *y = ( double *) malloc( sizeof( double ) * length );

    for ( int i = 0; i < length; i++ ) {

	double v[2];

	parse_hex( &v[0], string + offset, 16 );

	(*x)[i] = v[0];
	(*y)[i] = v[1];

	offset += 32;

    }

}

static void parse_hex( void *value, char *s, int l )

{

    char c[3] = { 0 };

    for ( int i = 0; i < l; i++ ) {

	c[0] = *s++;
	c[1] = *s++;
	
	*( ( unsigned char *) value ) = strtol( &c[0], NULL, 16 );

	value++;

    }

}    

static void signal_handler( int sig )

{

    chdir( "/var/tmp" );

    logger( "dumping core to /var/tmp." );

    struct rlimit core;
    getrlimit( RLIMIT_CORE, &core );
    logger( "core limits set to %ld %ld", core.rlim_cur, core.rlim_max );
    core.rlim_cur = core.rlim_max = RLIM_INFINITY;
    setrlimit( RLIMIT_CORE, &core );
    abort();

}

static void smooth( size_t *n, double **x, double **y )

{

    double *xout = ( double *) malloc( sizeof( double ) * *n );
    double *yout = ( double *) malloc( sizeof( double ) * *n );
    
    xout[0] = (*x)[0];
    yout[0] = (*y)[0];

    for ( int i = 1; i < *n - 1; i++ ) {

	xout[i] = ( (*x)[i - 1] + (*x)[i] + (*x)[i + 1] ) / 3;
	yout[i] = ( (*y)[i - 1] + (*y)[i] + (*y)[i + 1] ) / 3;

    }
    
    xout[*n - 1] = (*x)[*n - 1];
    yout[*n - 1] = (*y)[*n - 1];

    free( *x );
    free( *y );

    *x = xout;
    *y = yout;

}

static void simplify_points( size_t *n, double **x, double **y )

{

    smooth( n, x, y );

    size_t npts = 1;
    double *xout = ( double *) malloc( sizeof( double ) * *n );
    double *yout = ( double *) malloc( sizeof( double ) * *n );

    xout[0] = (*x)[0];
    yout[0] = (*y)[0];

    for ( int i0 = 0, i1 = 1, i2 = 2; i2 < *n; ) {

	double v1x = (*x)[i0] - (*x)[i1];
	double v1y = (*y)[i0] - (*y)[i1];
	double lv1 = sqrt( ( v1x * v1x ) + ( v1y * v1y ) );

	if ( lv1 == 0 ) {

	    i1++;
	    i2++;
	    continue;

	}

	double v2x = (*x)[i2] - (*x)[i1];
	double v2y = (*y)[i2] - (*y)[i1];
	double lv2 = sqrt( ( v2x * v2x ) + ( v2y * v2y ) );

	if ( lv2 == 0 ) {

	    i2++;
	    continue;

	}

	v1x /= lv1;
	v1y /= lv1;
	
	v2x /= lv2;
	v2y /= lv2;
	
	double dot = ( v1x * v2x ) + ( v1y * v2y );

	if ( dot <= -0.98480775307948240157 ) { /* >= 170 degrees */

	    i1++;
	    i2++;
	    continue;

	}

	xout[npts] = (*x)[i1];
	yout[npts] = (*y)[i1];
	npts++;

	i0 = i1;
	i1 = i2;
	i2++;

    }

    logger( "Number of points reduced from %lld to %lld", *n, npts );

    free( *x );
    free( *y );
    *x = ( double *) realloc( xout, sizeof( double ) * npts );
    *y = ( double *) realloc( yout, sizeof( double ) * npts );
    *n = npts;

}

static Logical get_surface( ElementP root )

{

    int annotation_id = getQuerystringInt( "id" ); 

    Logical ok;

    if ( annotation_id != INT_MAX ) {

	PGconn *conn = PQGetConnection();

	PGresult *result;

	result =
	    PQQuery( conn,
		     "select color_int "
		     "from slash_annotation where "
		     "annotation_id = %d",
		     annotation_id );

	int color = PQGetInt( result, 0, "color_int" );

	result = PQQuery( conn, GET_CLOSED_FMT, annotation_id );
	
	int tuples = PQntuples( result );
	
	if ( tuples > 0 )
	    ok = make_surface( root, color, annotation_id, tuples, result );
	else {

	    XMLAddProperty( root, "reason", "No contours returned." );
	    ok = False;

	}
	PQfinish( conn );

    } else {

	XMLAddProperty( root, "reason", "Annotation ID missing!" );
	ok = False;

    }

    return ok;

}

static Logical make_surface( ElementP root, int color, int annotation_id,
			     int tuples, PGresult *result )

{

    int hm1 = getQuerystringInt( "height" ) - 1; 

    ElementP surface = XMLCreateChildElement( root, "surface" );
    
    XMLAddProperty( surface, "annotation_id", "%d", annotation_id );
    XMLAddProperty( surface, "color", "%d", color );
    
    typedef struct _Polygon {
	size_t n_points;
	int *xpoints;
	int *ypoints;
	struct _Polygon *next;
    } Polygon, *PolygonP;
    
    HashTable *ztable = HashCreate( 3 );
    int zmin = INT_MAX;
    int zmax = -INT_MAX;
    
    for ( int i = 0; i < tuples; i++ ) {
	
	PolygonP polygon = ( PolygonP ) calloc( sizeof( Polygon ), 1 );
	
	double *xpoints;
	double *ypoints;
	
	parse_points( &polygon->n_points, &xpoints, &ypoints,
		      PQGetString( result, i, "polyline" ) );
	int zi = PQGetInt( result, i, "z_index" );
	
	polygon->xpoints = 
	    ( int *) malloc( sizeof( int ) * polygon->n_points );
	polygon->ypoints =
	    ( int *) malloc( sizeof( int ) * polygon->n_points );
	for ( int p = 0; p < polygon->n_points; p++ ) {
	    
	    polygon->xpoints[p] = xpoints[p];
	    polygon->ypoints[p] = hm1 - ypoints[p];
	    
	}
	free( xpoints );
	free( ypoints );
	
	if ( HashFind( ztable, &zi, sizeof( int ) ) ) {
	    
	    PolygonP pl = ( PolygonP ) HashData( ztable );
	    
	    polygon->next = pl->next;
	    pl->next = polygon;
	    
	} else {
	    
	    int *zip = ( int *) malloc( sizeof( int ) );
	    *zip = zi;
	    
	    HashAdd( ztable, zip, sizeof( int ), polygon );
	    
	    if ( zmin > zi )
		zmin = zi;
	    if ( zmax < zi )
		zmax = zi;
	    
	}
	
    }
    
    int n = ( zmax - zmin ) + 1;
    
    int *z = ( int *) alloca( sizeof( int ) * n );
    size_t *np = ( size_t *) alloca( sizeof( size_t ) * n );
    size_t **npp = ( size_t **) alloca( sizeof( size_t *) * n );
    int ***x = ( int ***) alloca( sizeof( int **) * n );
    int ***y = ( int ***) alloca( sizeof( int **) * n );
    
    for ( int zv = zmin, i = 0; zv <= zmax; zv++, i++ ) {
	
	np[i] = 0;
	z[i] = zv;
	
	if ( HashFind( ztable, &zv, sizeof( int ) ) ) {
	    
	    PolygonP pl = ( PolygonP ) HashData( ztable );
	    
	    npp[i] = INITIALIZE( size_t );
	    x[i] = INITIALIZE( int *);
	    y[i] = INITIALIZE( int *);
	    
	    do {
		
		npp[i] = ONE_MORE( npp[i], size_t, np[i] );
		x[i] = ONE_MORE( x[i], int *, np[i] );
		y[i] = ONE_MORE( y[i], int *, np[i] );
		
		npp[i][np[i]] = pl->n_points;
		x[i][np[i]] = pl->xpoints;
		y[i][np[i]] = pl->ypoints;
		np[i]++;
		
	    } while ( ( pl = pl->next ) != NULL );
	    
	}
	
    }
    
    GeometryBuffer gb = { 0 };
    
    gb.max_vertices = 1024;
    gb.vertex_list = ( VertexP ) malloc( sizeof( Vertex ) * gb.max_vertices );
    gb.vertex_table = HashCreate( 4 );
    gb.max_triangles = 1024;
    gb.triangle_list =
	( Triangle *) malloc( sizeof( Triangle ) * gb.max_triangles );
    
    marching_cubes( n, z, np, npp, x, y, 
		    triangle_handler, ( void *) &gb );
    
    for ( int i = 0; i < gb.n_vertices; i++ ) {
	
	float nx = gb.vertex_list[i].nx;
	float ny = gb.vertex_list[i].ny;
	float nz = gb.vertex_list[i].nz;
	
	float l = sqrt( ( nx * nx ) + ( ny * ny ) + ( nz * nz ) );
	
	gb.vertex_list[i].nx /= l;
	gb.vertex_list[i].ny /= l;
	gb.vertex_list[i].nz /= l;
	
    }	

    build_indexed_triangle_list( surface, gb );

    return True;
        
}

static void build_indexed_triangle_list( ElementP surface, GeometryBuffer gb )

{

    HashTable *vtable = HashCreate( 5 );

    int ntmax = USHRT_MAX / 3;
    int nt = 0;
    Triangle *tarray = ( Triangle *) malloc( sizeof( Triangle ) * ntmax );
    for ( int i = 0; i < gb.n_triangles; i++ ) {

	Triangle *t = &gb.triangle_list[i];
	    
	Triangle new_tri;
	while ( True ) {

	    for ( int tx = 0; tx < 3; tx++ ) {
		
		VertexP vertex = &gb.vertex_list[(*t)[tx]];
		
		if ( !HashFind( vtable, vertex->v, sizeof( float ) * 3 ) ) {
		    
		    vertex->index = HashCount( vtable );
		    HashAdd( vtable, vertex->v, sizeof( float ) * 3, vertex );
		    
		}
		new_tri[tx] = vertex->index;

	    }
	    if ( HashCount( vtable ) > MAX_VERTICES ) {

		dump_triangles( surface, tarray, nt, vtable );
		nt = 0;
		HashDestroy( vtable );
		vtable = HashCreate( 5 );

	    } else
		break;

	}
	if ( nt == ntmax )
	    tarray =
		( Triangle *) realloc( tarray,
				       sizeof( Triangle ) * ( ntmax *= 2 ) );

	memcpy( &tarray[nt++], &new_tri[0], sizeof( Triangle ) );

    }

    if ( nt > 0 )
	dump_triangles( surface, tarray, nt, vtable );

}

static int vindex_compare( const void *p1, const void *p2 )

{

    VertexP v1 = *( VertexPP ) p1;
    VertexP v2 = *( VertexPP ) p2;

    return v1->index - v2->index;

}

static void dump_triangles( ElementP surface,
			    Triangle *tarray, int nt, HashTable *vtable )

{
	    
    ElementP indexed_triangles =
	XMLCreateChildElement( surface, "indexed_triangles" );
							
    int nverts = HashCount( vtable );
    VertexList vlist = ( VertexList ) alloca( sizeof( VertexP ) * nverts );

    HashFirst( vtable );
    for ( int i = 0; i < nverts; i++, HashNext( vtable ) )
	vlist[i] = ( VertexP ) HashData( vtable );

    qsort( vlist, nverts, sizeof( VertexP ), vindex_compare );

    ElementP vertices = XMLCreateChildElement( indexed_triangles, "vertices" );
    
    XMLAddProperty( vertices, "n", "%d", nverts );
    
    TextBufferP vertex_buffer = create_text_buffer();
    
    for ( int i = 0; i < nverts; i++ )
	append( vertex_buffer, 
		"%g %g %g %g %g %g\n",
		vlist[i]->v[0],
		vlist[i]->v[1],
		vlist[i]->v[2],
		vlist[i]->nx,
		vlist[i]->ny,
		vlist[i]->nz );
    
    XMLAddContent( vertices, vertex_buffer->buffer );
    
    ElementP triangles =
	XMLCreateChildElement( indexed_triangles, "triangles" );
    
    XMLAddProperty( triangles, "n", "%d", nt );
    
    TextBufferP triangle_buffer = create_text_buffer();
    
    for ( int i = 0; i < nt; i++ ) 
	append( triangle_buffer, 
		"%d %d %d\n",
		tarray[i][0], tarray[i][1], tarray[i][2] );
    
    XMLAddContent( triangles, triangle_buffer->buffer );
    
}

static TextBufferP create_text_buffer( void )

{

    TextBufferP text_buffer = ( TextBufferP ) calloc( 1, sizeof( TextBuffer ) );

    text_buffer->buffer_size = 10 * 1024 * 1024;
    text_buffer->buffer = ( char *) malloc( text_buffer->buffer_size );

    return text_buffer;

}

static void triangle_handler( float t[3][3], void *data )

{

    GeometryBufferP gb = ( GeometryBufferP ) data;

    float x1 = t[0][0] - t[1][0];
    float x2 = t[0][1] - t[1][1];
    float x3 = t[0][2] - t[1][2];

    float y1 = t[2][0] - t[1][0];
    float y2 = t[2][1] - t[1][1];
    float y3 = t[2][2] - t[1][2];

    float t1 = ( x2 * y3 ) - ( x3 * y2 );
    float t2 = ( x3 * y1 ) - ( x1 * y3 );
    float t3 = ( x1 * y2 ) - ( x2 * y1 );

    float s2sqd = ( t1 * t1 ) + ( t2 * t2 ) + ( t3 * t3 );

    if ( s2sqd > 0 ) {

	float l = sqrt( s2sqd );

	if ( gb->n_triangles == gb->max_triangles )
	    gb->triangle_list = 
		( Triangle *) realloc( gb->triangle_list,
				       sizeof( Triangle ) *
				       ( gb->max_triangles *= 2 ) );
	
	Triangle *tr = &gb->triangle_list[gb->n_triangles++];

	float nx = t1 / l;
	float ny = t2 / l;
	float nz = t3 / l;

	for ( int i = 0; i < 3; i++ )
	    if ( HashFind( gb->vertex_table, &t[i], sizeof( float ) * 3 ) ) {

		int nt = (*tr)[i] = 
		    *( ( int *) HashData( gb->vertex_table ) );
		
		float dot = 
		    ( nx * gb->vertex_list[nt].nx ) +
		    ( ny * gb->vertex_list[nt].ny ) +
		    ( nz * gb->vertex_list[nt].nz );

		if ( dot < 0 ) {

		    nx = -nx;
		    ny = -ny;
		    nz = -nz;

		}
		
		gb->vertex_list[nt].nx += nx;
		gb->vertex_list[nt].ny += ny;
		gb->vertex_list[nt].nz += nz;
		
	    } else {

		float *v = ( float *) malloc( sizeof( float ) * 3 );

		v[0] = t[i][0];
		v[1] = t[i][1];
		v[2] = t[i][2];

		if ( gb->n_vertices == gb->max_vertices ) 
		    gb->vertex_list = 
			( VertexP ) realloc( gb->vertex_list,
					     sizeof( Vertex ) * 
					     ( gb->max_vertices *= 2 ) );

		int nt = (*tr)[i] = gb->n_vertices++;

		gb->vertex_list[nt].v = v;
		gb->vertex_list[nt].index = nt;
		gb->vertex_list[nt].nx = nx;
		gb->vertex_list[nt].ny = ny;
		gb->vertex_list[nt].nz = nz;

		int *index = ( int *) malloc( sizeof( int ) );
		*index = nt;
		HashAdd( gb->vertex_table, v,  sizeof( float ) * 3, index );

	    }

    }

}	

static void append( TextBufferP tb, char *fmt, ... )

{
		
    va_list list;

    va_start( list, fmt );

    size_t lbuf;
    size_t lp;

    do {
	
	lbuf = tb->buffer_size - tb->offset;
	
	va_list alist;

	va_copy( alist, list );
	lp = vsnprintf( tb->buffer + tb->offset,
			lbuf,
			fmt,
			alist );
	va_end( alist );
	
	if ( lp >= lbuf ) {
	    
	    tb->buffer_size *= 2;
	    tb->buffer =
		( char *) realloc( tb->buffer,
				   tb->buffer_size );
	    
	}
	
    } while ( lbuf <= lp );
    tb->offset += lp;

}
