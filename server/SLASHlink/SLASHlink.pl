#!/usr/bin/perl
use strict;

delete @ENV{ 'IFS', 'CDPATH', 'ENV', 'BASH_ENV' };

$ENV{'BASE'} = "/var/www/html/WebImageBrowser";

print "Content-type: text/xml\n\n";

exec "/usr/local/bin/SLASHlink";

