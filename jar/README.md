Xi Java Archive
===============

This JAR-file is composed of the code in the [`src`](https://github.com/arshajii/xi/tree/master/src) folder. Refer to the [commit history](https://github.com/arshajii/xi/commits/master/jar/xi.jar) for information regarding when it was last updated.

Running Xi
----------

To execute a Xi source file called `file.xi`, the following command would be used:

    $ java -jar xi.jar file.xi
    
For example:

    $ touch myscript.xi
    $ echo "println \"hello world\"" >> myscript.xi
    $ cat myscript.xi
    println "hello world"
    $ java -jar xi.jar myscript.xi
    hello world