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

package org.freedesktop.dbus;

import org.bridj.IntValuedEnum;
import org.bridj.ann.Name;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Indicates the status of a {@link Watch}.
 * <pre>[dbus/dbus-connection.h]
 * typedef enum
 * {
 *   DBUS_WATCH_READABLE = 1 << 0, /&#42;&#42;< As in POLLIN &#42;/
 *
 *   DBUS_WATCH_WRITABLE = 1 << 1, /&#42;&#42;< As in POLLOUT &#42;/
 *
 *   DBUS_WATCH_ERROR    = 1 << 2, /&#42;&#42;< As in POLLERR (can't watch for
 *                                    this, but can be present in
 *                                    current state passed to
 *                                    dbus_watch_handle()). &#42;/
 *
 *   DBUS_WATCH_HANGUP   = 1 << 3  /&#42;&#42;< As in POLLHUP (can't watch for
 *                                    it, but can be present in current
 *                                    state passed to
 *                                    dbus_watch_handle()). &#42;/
 *
 *   /&#42; Internal to libdbus, there is also _DBUS_WATCH_NVAL in dbus-watch.h &#42;/
 * } DBusWatchFlags;</pre>
 *
 * @author Benjamin P. Jung
 */
@Name("DBusWatchFlags")
public enum WatchFlag implements IntValuedEnum<WatchFlag> {

    /** As in POLLIN */
    DBUS_WATCH_READABLE(1 << 0),

    /** As in POLLOUT */
    DBUS_WATCH_WRITABLE(1 << 1),

    /**
     * As in POLLERR (can't watch for this, but can be present in current state passed to {@link DBus#_watchHandle()}).
     */
    DBUS_WATCH_ERROR(1 << 2),

    /**
     *  As in POLLHUP (can't watch for it, but can be present in current state passed to {@link DBus#_watchHandle()}).
     */
    DBUS_WATCH_HANGUP(1 << 3);


    private static final Map<Integer, WatchFlag> INT_MAPPING = new HashMap<>();
    static {
        for (final WatchFlag wf: WatchFlag.values()) {
            INT_MAPPING.put(Integer.valueOf(Integer.valueOf(wf.value)) , wf);
        }
    }

    public static WatchFlag fromValue(final int value) {
        return INT_MAPPING.get(Integer.valueOf(value));
    }

    public static WatchFlag fromValue(final long value) {
        return INT_MAPPING.get(Integer.valueOf((int) value));
    }

    private final int value;
    private WatchFlag(final int value) {
        this.value = value;
    }

    @Override
    public long value() {
        return this.value;
    }

    @Override
    public Iterator<WatchFlag> iterator() {
       return EnumSet.<WatchFlag>of(this).iterator();
    }

}
