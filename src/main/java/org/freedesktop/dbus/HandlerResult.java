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
 * @author Benjamin P. Jung
 */
@Name("DBusHandlerResult")
public enum HandlerResult implements IntValuedEnum<HandlerResult> {

    /** Message has had its effect - no need to run more handlers. */
    HANDLED(0),

    /** Message has not had any effect - see if other handlers want it. */
    NOT_YET_HANDLED(1),

    /**
     * Need more memory in order to return #DBUS_HANDLER_RESULT_HANDLED or #DBUS_HANDLER_RESULT_NOT_YET_HANDLED.
     * Please try again later with more memory.
     */
    NEED_MEMORY(2);


    // =================================================================================================================
    // ---- Ugly stuff to make sure that the conversion between native types and this enum works -----------------------
    // =================================================================================================================

    private static final Map<Integer, HandlerResult> INT_MAPPING = new HashMap<>();
    static {
        for (HandlerResult hr: HandlerResult.values()) {
            INT_MAPPING.put(hr.value, hr);
        }
    }

    @Internal
    public static HandlerResult fromValue(final int value) {
        return INT_MAPPING.get(Integer.valueOf(value));
    }

    @Internal
    public static HandlerResult fromValue(final long value) {
        return INT_MAPPING.get(Integer.valueOf((int) value));
    }

    private final int value;
    private HandlerResult(final int value) {
        this.value = value;
    }

    @Internal
    @Override
    public long value() {
        return this.value;
    }

    @Internal
    @Override
    public Iterator<HandlerResult> iterator() {
        return EnumSet.<HandlerResult>of(this).iterator();
    }

}
