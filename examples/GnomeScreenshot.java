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

import org.freedesktop.dbus.BusType;
import org.freedesktop.dbus.Connection;
import org.freedesktop.dbus.DBus;
import org.freedesktop.dbus.Message;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import static org.freedesktop.dbus.Message.MethodArgument;
import static org.freedesktop.dbus.Message.MethodArgument.booleanParam;
import static org.freedesktop.dbus.Message.MethodArgument.stringParam;

/**
 * A simple example to demonstrate how to use fx-dbus to take
 * a screenshots using the gnome-screenshot D-Bus interface.
 * @author Benjamin P. Jung
 */
public class GnomeScreenshot {

    static final String DESTINATION = "org.gnome.Shell.Screenshot";
    static final String PATH = "/org/gnome/Shell/Screenshot";
    static final String INTERFACE = "org.gnome.Shell.Screenshot";

    public static void main(String[] args) throws Throwable {

        // Filename that shall be used to store the screenshot must be the first parameter!
        final String filename = args != null && args.length >= 1 ? args[0] : File.createTempFile("screenshot", ".png").getAbsolutePath();

        // Initializes the D-Bus library correctly.
        DBus.initialize();

        // Fetches a new (private) D-Bus connection.
        final Connection connection = Connection.getConnection(BusType.SESSION, true);

        // Call "SelectArea()" and use the result to call "ScreenshotArea()"
        final Message area = connection.sendWithReply(Message.newMethodCall(DESTINATION, PATH, INTERFACE, "SelectArea"));
        if (area != null && !area.isError()) {
            final Message ssRequest = Message.newMethodCall(DESTINATION, PATH, INTERFACE, "ScreenshotArea");
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

    }

}
