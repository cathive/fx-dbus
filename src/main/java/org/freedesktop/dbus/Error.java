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

import org.freedesktop.dbus.DBus._Error;

/**
 * @author Benjamin P. Jung
 */
public class Error {

    @Internal
    final _Error _error;

    @Internal
    private Error(final _Error _error) {
        this._error = _error;
    }

    public String getName() {
        return this._error._name().getCString();
    }

    public String getMessage() {
        return this._error._message().getCString();
    }

    /**
     * Returns {@code true} if this error has been set, {@code false} otherwise.
     * @return
     */
    public boolean isSet() {
        return DBus._errorIsSet(this._error._getPeer());
    }

    @Override
    public String toString() {
        return String.format("%s: %s", this.getName(), this.getMessage());
    }

}
