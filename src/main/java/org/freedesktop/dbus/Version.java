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

import org.bridj.Pointer;

import java.beans.ConstructorProperties;
import java.util.Objects;

/**
 * Representation of the version as returned by
 * {@link DBus#_getVersion(org.bridj.Pointer, org.bridj.Pointer, org.bridj.Pointer)}).
 * @see #get()
 * @author Benjamin P. Jung
 */
public final class Version implements Comparable<Version> {

    /** Major version */
    private final int major;

    /** Minor version */
    private final int minor;

    /** Micro version */
    private final int micro;

    @ConstructorProperties({ "major", "minor", "micro" })
    Version(final int major, final int minor, final int micro) {
        super();
        this.major = major;
        this.minor = minor;
        this.micro = micro;
    }


    public Integer getMajor() { return this.major; }

    public Integer getMinor() { return this.minor; }

    public Integer getMicro() { return this.micro; }

    @Override
    public String toString() {
        return String.format("%d.%d.%d", this.getMajor(), this.getMinor(), this.getMicro());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getMajor(), this.getMinor(), this.getMicro());
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) { return true; }
        if (o == null || !(o instanceof Version)) { return false; }
        final Version that = (Version) o;
        return Objects.equals(this.getMajor(), that.getMajor())
            && Objects.equals(this.getMinor(), that.getMinor())
            && Objects.equals(this.getMicro(), that.getMicro());
    }

    @Override
    public int compareTo(final Version that) {

        final int maj = this.getMajor() - that.getMajor();
        if (maj != 0) { return maj; }

        final int min = this.getMinor() - that.getMinor();
        if (min != 0) { return min; }

        return this.getMicro() - that.getMicro();

    }

    /**
     * Fetches the version of the wrapped native D-Bus library.
     * @return
     *     The underlying native D-Bus version being used.
     */
    public static Version get() {

        // Create pointers to hold version information
        final Pointer<Integer> major = DBus._new0(int.class, 1);
        final Pointer<Integer> minor = DBus._new0(int.class, 1);
        final Pointer<Integer> micro = DBus._new0(int.class, 1);

        // Call native method to retrieve version information.
        DBus._getVersion(major, minor, micro);

        // Store information in wrapper object.
        final Version version = new Version(major.getInt(), minor.getInt(), micro.getInt());

        // Release memory and free pointers.
        DBus._free(major);
        DBus._free(minor);
        DBus._free(micro);

        // Return result.
        return version;

    }

}
