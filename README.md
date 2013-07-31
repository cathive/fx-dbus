D-Bus integration for Java and JavaFX
=====================================


Introduction
------------

This library provides an easy way to integrate D-Bus into
your Java / JavaFX applications.


Highlights
----------

*   Native bindings to libdbus-1 via BridJ (no need to install any native libraries)
*   Nice object-oriented API
*   Supported platforms: Linux and Mac OS X, possibly others (untested)


Requirements
------------

*   libdbus-1 library
*   Running D-Bus instance
*   Oracle Java SE 7 Runtime (possibly OpenJDK 7 works, haven't tested)

Usually every modern Linux distribution should meet the first of the two requirements mentioned above.


Creation of the API
-------------------

I analyzed the output of JNAerator and came to the conclusion
that it woul take a bit more work to make sure, that the
native mappings would work the way I wanted them them.

```sh
# Command line used to create the initial mappings.
# I threw away everything and started from scratch
# once I figured out that I was not really satisfied
# with the resulting API.
java -jar jnaerator.jar \
-I/usr/include/dbus-1.0 -I/usr/include \
-package org.freedesktop.dbus \
-library dbus \
-mode Directory \
-beautifyNames \
-direct \
-runtime BridJ \
/usr/include/dbus-1.0/dbus/dbus.h
```
