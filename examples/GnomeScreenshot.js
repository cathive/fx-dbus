/*
 * Copyright (C) 2013 The Cat Hive Developers.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * A simple example to demonstrate how to use fx-dbus (via JavaScript) to take
 * a screenshots using the gnome-screenshot D-Bus interface.
 *
 * @author Benjamin P. Jung
 */

importClass(org.freedesktop.dbus.BusType);
importClass(org.freedesktop.dbus.Connection);
importClass(org.freedesktop.dbus.DBus);
importClass(org.freedesktop.dbus.Message);

importClass(java.io.File);
importClass(java.util.List);

importClass(org.freedesktop.dbus.Message.MethodArgument);

var DESTINATION = "org.gnome.Shell.Screenshot";
var PATH = "/org/gnome/Shell/Screenshot";
var INTERFACE = "org.gnome.Shell.Screenshot";

// Filename that shall be used to store the screenshot.
var filename = arguments != null && arguments.length >= 1 ? arguments[0] : File.createTempFile("screenshot", ".png").getAbsolutePath();

// Initializes the D-Bus library correctly.
DBus.initialize();

// Fetches a new (private) D-Bus connection.
var connection = Connection.getConnection(BusType.SESSION, true);

// Call "SelectArea()" and use the result to call "ScreenshotArea()"
var area = connection.sendWithReply(Message.newMethodCall(DESTINATION, PATH, INTERFACE, "SelectArea"));
if (area != null && !area.isError()) {
    var ssRequest = Message.newMethodCall(DESTINATION, PATH, INTERFACE, "ScreenshotArea");
    var areaIt = area.iterator();
    while (areaIt.hasNext()) {
        ssRequest.addArguments(areaIt.next());
    }
    ssRequest.addArguments(org.freedesktop.dbus.Message.MethodArgument.booleanParam(true), org.freedesktop.dbus.Message.MethodArgument.stringParam(filename));
    var ssResponse = connection.sendWithReply(ssRequest);
    if (ssResponse != null && !ssResponse.isError()) {
        var ssParam = ssResponse.iterator();
        if (ssParam.next().get()) {
            print("Successfully saved screenshot to " + ssParam.next().get() + "\n");
        }
    }
}

// Close the D-Bus connection.
connection.close();

// Cleanly shutdown dbus.
DBus.shutdown();
