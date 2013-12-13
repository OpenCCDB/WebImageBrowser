#include <stdio.h>
#include <stdlib.h>
#include <stdarg.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include <openssl/md5.h>
#include <string.h>
#include <sys/uio.h>
#include <errno.h>
#include <signal.h>

#include <spl.h>
#include <splweb.h>
#include <splxml.h>

#include "build.h"
#include "version.h"

#include "SLASHlink.h"

#include "protos.h"

#include "SLASHlink_defs.h"
#include "SLASHlink_statics.h"

int main( int argc, char **argv )

{

    enroll( argv[0], "" );
    initLogger( argv[0] );

    logger( "Version: %s, Build Date: %s", VERSION, BUILD );

    if ( !parseQuerystring() )
	die( "Unable to parse query string!" );

    logQuerystring();

    char *request = getQuerystringString( "request" );
    char *key = NULL;

    TransactP transact = NULL;
    if ( strcmp( request, "link" ) == 0 ) {

	transact = make_link;

	char *qs = getQuerystring();
	
        unsigned char *md5key = MD5( qs, strlen( qs ), NULL );
	char hexmd5key[33] = { 0 };
	
	if ( md5key == NULL )
	    die( "MD5 key of %s is NULL!", qs );
	for ( int i = 0, k = 0; i < 16; i++, k += 2 )
	    snprintf( &hexmd5key[k], 3, "%02x", md5key[i] );
	    
	key = strdup( hexmd5key );

    } else {

	if ( strcmp( request, "keepalive" ) == 0 )
	    transact = keep_alive;
	else
	    die( "Unknown request '%s'", request );
	key = getQuerystringString( "key" );

    }

    char *worker_fifo = string( WORKER_FIFO_FMT, key );
    char *transact_fifo = string( TRANSACT_FIFO_FMT, getpid() );

    if ( ( key != NULL ) && ( strlen( key ) > 0 ) )
	transaction( worker_fifo, transact_fifo, NULL, worker, transact, key );
    else
	logger( "No key!" );

    exit( 0 );

}

static void worker( int fifo, void *data )

{

    char *linkpath = NULL;

    logger( "Worker start." );

    char *initial_server_wait = getenv( "INITIAL_SERVER_WAIT" );
    char *keepalive_server_wait = getenv( "KEEPALIVE_SERVER_WAIT" );

    int timeout = ( initial_server_wait != NULL ) ?
	atoi( initial_server_wait ) : 5;
    int keepalive = ( keepalive_server_wait != NULL ) ?
	atoi( keepalive_server_wait ) : 30;

    while ( waitForRequest( fifo, timeout ) ) {

	int l;
	read( fifo, &l, sizeof( l ) );

	if ( l == 0 )
	    logger( "keepalive %s", linkpath );
	else {

	    linkpath = ( char *) calloc( l + 1, 1 );
	    read( fifo, linkpath, l );
	    timeout = keepalive;

	    logger( "linkpath: '%s'", linkpath );
	    pid_t pid;

	    read( fifo, &pid, sizeof( pid ) );

	    int transact = openWriteFIFO( TRANSACT_FIFO_FMT, pid );

	    int ok = 0;

            write( transact, &ok, sizeof( ok ) );
            close( transact );

	}  

    }
    if ( linkpath != NULL ) {

	unlink( linkpath );
	logger( "Timeout.  Removed link %s", linkpath );

    } else
	logger( "Timeout waiting for linkpath from transact." );

}

static Logical make_link( int send, int receive, void *data )

{

    signal( SIGCHLD, SIG_IGN );	/* Mostly for debugging convenience. */

    char *base = getenv( "BASE" );
    char *path = getQuerystringString( "path" );

    char *p = strchr( path, ':' );
    if ( p != NULL )
	path = p + 1;

    Logical ok = False;
    char *error;

    ElementP root = XMLCreateElement( "root" );
	    
    if ( file_exists( path, S_IFDIR | S_IFLNK ) ) {

	char *file = strrchr( path, '/' ) + 1;
	
	char *linkpath = string( "%s/SLASH/%s", base, file );
	
	int status = 0;
	
	unlink( linkpath );

	status = symlink( path, linkpath );
	
	if ( status == 0 ) {
	    
	    logger( "Link from %s to %s created.", path, linkpath );
	    
	    pid_t pid = getpid();
	    int lpl = strlen( linkpath );
	    write( send, &lpl, sizeof( lpl ) );
	    write( send, linkpath, lpl );
	    write( send, &pid, sizeof( pid ) );

	    if ( waitForRequest( receive, 100 ) ) {
		
		int wstatus;
		read( receive, &wstatus, sizeof( wstatus ) );

		if ( !( ok = ( wstatus == 0 ) ) )
		    error = 
			string( "Worker startup failed, internal status %d",
				wstatus );
		
	    } else
		error = string( "Worker startup timeout!" );
	    
	} else
	    error = string( "%s -> %s: %s",
			    path, linkpath, strerror( errno ) );
	    
	if ( ok ) {
	    
	    char *key = ( char *) data;
	    
	    ElementP zoomify_path =
		XMLCreateChildElement( root, "zoomify_path" );
	    
	    XMLAddProperty( zoomify_path, "key", key );
	    XMLAddProperty( zoomify_path, "path", file );
	    
	} 
	    
    } else
	error = string( "stat error %s: %s", path, strerror( errno ) );

    if ( !ok )
	XMLAddContent( XMLCreateChildElement( root, "error" ), error );
	
    XMLAddProperty( root, "status", ok ? "success" : "fail" );

    XMLWrite( stdout, root );

    return True;

}

static Logical keep_alive( int send, int receive, void *data )

{

    int l = 0;

    write( send, &l, sizeof( l ) );

    return True;

}
