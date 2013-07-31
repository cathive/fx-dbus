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

import java.util.EnumSet;
import java.util.Iterator;

/**
 * Indicates the status of incoming data on a {@link Connection}. This determines whether
 * {@link DBus#_connectionDispatch()} needs to be called.
 *
 * <pre>[dbus/dbus-connection.h]
 * typedef enum
 * {
 *   DBUS_DISPATCH_DATA_REMAINS,  / &#42;&#42;< There is more data to potentially convert to messages.  &#42;/
 *   DBUS_DISPATCH_COMPLETE,      / &#42;&#42;< All currently available data has been processed.  &#42;/
 *   DBUS_DISPATCH_NEED_MEMORY    / &#42;&#42;< More memory is needed to continue.  &#42;/
 * } DBusDispatchStatus;
 * </pre>
 *
 * @author Benjamin P. Jung
 */
public enum DispatchStatus implements IntValuedEnum<DispatchStatus> {

    /** There is more data to potentially convert to messages. */
    DBUS_DISPATCH_DATA_REMAINS(0),

    /** All currently available data has been processed. */
    DBUS_DISPATCH_COMPLETE(1),

    /** More memory is needed to continue. */
    DBUS_DISPATCH_NEED_MEMORY(2);

    private final int value;
    private DispatchStatus(final int value) {
        this.value = value;
    }

    @Internal
    @Override
    public long value() {
        return this.value;
    }

    @Internal
    @Override
    public Iterator<DispatchStatus> iterator() {
        return EnumSet.<DispatchStatus>of(this).iterator();
    }

}
