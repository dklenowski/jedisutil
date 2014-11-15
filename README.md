jedisutil
=========

Utilities for Jedis/Redis.

### To Build

    gradle build


### To run

    gradle writecp
    . ./setclasspath.sh
    $ java -server -Xmx2048m com.orbious.google.ngrams.Parser
    you must specify a <inputfile>?
    Usage: Parser [-h]
       -h               Print this help message and exit.
       -i <inputfile>   The inputfile to process.

### To deploy to a local maven repository

    gradle uploadArchives
