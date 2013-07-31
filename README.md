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


Features
--------

Below you'll find a list of already existing features as well as features that are planned for
future releases:

[x] Bindings to the C-API of D-Bus (libdbus-1) about 75% done
[x] Support for synchronous calls
[x] Support for all known basic types
[ ] Support for asynchronous calls
[ ] Nice JavaFX API using Workers, Services, Callbacks and Properties
[ ] Annotation-based method bindings
[ ] Support for structures and arrays


Example usage
-------------

The Java code below shows how to use the gnome-screenshot tool via D-Bus:

```java
final String destination = "org.gnome.Shell.Screenshot";
final String path = "/org/gnome/Shell/Screenshot";
final String interface = "org.gnome.Shell.Screenshot";

// Filename that shall be used to store the screenshot.
final String filename = File.createTempFile("screenshot", ".png").getAbsolutepath();

// Initializes the D-Bus library correctly.
DBus.initialize();

// Fetches a new (private) D-Bus connection.
final Connection connection = Connection.getConnection(BusType.SESSION, true);

// Call "SelectArea()" and use the result to call "ScreenshotArea()"
final Message area = connection.sendWithReply(Message.newMethodCall(destination, path, interface, "SelectArea"));
if (area != null && !area.isError()) {
    final Message ssRequest = Message.newMethodCall(destination, path, interface, "ScreenshotArea");
    for (final MethodArgument<?> arg: area) {
        ssRequest.addArguments(arg);
    }
    ssRequest.addArguments(booleanParam(true), stringParam(filename));
    final Message ssResponse = connection.sendWithReply(ssRequest);
    if (ssResponse != null && !ssResponse.isError()) {
        final Iterator<MethodArgument<?>> ssParam = ssResponse.iterator();
        if (ssParam.next().get().equals(Boolean.TRUE)) {
            System.out.println("Successfully saved screenshot to " + ssParam.next().get());
        }
    }
}

// Close the D-Bus connection.
connection.close();

// Cleanly shutdown dbus.
DBus.shutdown();
```


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
