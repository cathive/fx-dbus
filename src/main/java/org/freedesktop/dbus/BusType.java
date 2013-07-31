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
@Name("DBusBusType")
public enum BusType implements IntValuedEnum<BusType> {

    /** The login session bus */
    SESSION(0),

    /** The systemwide bus */
    SYSTEM(1),

    /** The bus that started us, if any */
    STARTER(2);


    // =================================================================================================================
    // ---- Ugly stuff to make sure that the conversion between native types and this enum works -----------------------
    // =================================================================================================================

    private static final Map<Integer, BusType> INT_MAPPING = new HashMap<>();
    static {
        for (final BusType bt: BusType.values()) {
            INT_MAPPING.put(Integer.valueOf(Integer.valueOf(bt.value)) , bt);
        }
    }

    public static BusType fromValue(final int value) {
        return INT_MAPPING.get(Integer.valueOf(value));
    }

    public static BusType fromValue(final long value) {
        return INT_MAPPING.get(Integer.valueOf((int) value));
    }


    private final int value;
    private BusType(final int value) {
        this.value = value;
    }

    @Internal
    @Override
    public long value() {
        return this.value;
    }

    @Internal
    @Override
    public Iterator<BusType> iterator() {
        return EnumSet.<BusType>of(this).iterator();
    }



}
