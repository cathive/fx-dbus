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

import org.bridj.*;
import org.bridj.ann.*;
import org.bridj.ann.Runtime;

import java.lang.reflect.Type;

/**
 * @author Benjamin P. Jung
 */
@Library(DBus.LIBRARY_NAME)
@Runtime(CRuntime.class)
public class DBus {

    static {
        // Binds all native methods in this class and its subclasses
        BridJ.register(DBus.class);
    }

    /**
     * Name of the native library that is wrapped by this class.
     * <p>On a typical Linux installation this would map to "libdbus-1.so".</p>
     */
    static final String LIBRARY_NAME = "dbus-1";

    // Private c-tor to avoid instantiation.
    private DBus() { /* Intentionally left empty. */ }

    // =================================================================================================================
    // ---- Some #defines that have been picked up here and there... ---------------------------------------------------
    // =================================================================================================================

    /**
     * <pre>[dbus/dbus-pending-call-h]
     * #define DBUS_TIMEOUT_INFINITE ((int) 0x7fffffff)</pre>
     */
    public static final int TIMEOUT_INFINITE = ((int) 0x7fffffff);

    /**
     * <pre>[dbus/dbus-pending-call.h]
     * #define DBUS_TIMEOUT_USE_DEFAULT (-1)</pre>
     */
    public static final int TIMEOUT_USE_DEFAULT = -1;


    /**
     * <pre>[dbus/dbus-shared.h]
     * #define DBUS_SERVICE_DBUS      "org.freedesktop.DBus"</pre>
     * The bus name used to talk to the bus itself.
     */
    @Name("DBUS_SERVICE_DBUS")
    public static final String SERVICE_DBUS = "org.freedesktop.DBus";

    /**
     * <pre>[dbus/dbus-shared.h]
     * #define DBUS_PATH_DBUS  "/org/freedesktop/DBus"</pre>
     * The object path used to talk to the bus itself.
     */
    @Name("DBUS_PATH_DBUS")
    public static final String PATH_DBUS = "/org/freedesktop/DBus";

    /**
     * <pre>[dbus/dbus-shared.h]
     * #define DBUS_PATH_LOCAL "/org/freedesktop/DBus/Local"</pre>
     * The object path used in local/in-process-generated messages.
     */
    @Name("DBUS_PATH_LOCAL")
    public static final String PATH_LOCAL = "/org/freedesktop/DBus/Local";


    /**
     * <pre>[dbus/dbus-shared.h]
     * #define DBUS_INTERFACE_DBUS           "org.freedesktop.DBus"</pre>
     * The interface exported by the object with {@link #SERVICE_DBUS} and {@link #PATH_DBUS}.
     */
    @Name("DBUS_INTERFACE_DBUS")
    public static final String INTERFACE_DBUS = "org.freedesktop.DBus";

    /**
     * <pre>[dbus/dbus-shared.h]
     * #define DBUS_INTERFACE_INTROSPECTABLE "org.freedesktop.DBus.Introspectable"</pre>
     * The interface supported by introspectable objects
     */
    @Name("DBUS_INTERFACE_INTROSPECTABLE")
    public static final String INTERFACE_INTROSPECTABLE = "org.freedesktop.DBus.Introspectable";

    /**
     * <pre>[dbus/dbus-shared.h]
     * #define DBUS_INTERFACE_PROPERTIES     "org.freedesktop.DBus.Properties"</pre>
     * The interface supported by objects with properties
     */
    @Name("DBUS_INTERFACE_PROPERTIES")
    public static final String INTERFACE_PROPERTIES = "org.freedesktop.DBus.Properties";

    /**
     * <pre>[dbus/dbus-shared.h]
     * #define DBUS_INTERFACE_PEER           "org.freedesktop.DBus.Peer"</pre>
     * The interface supported by most dbus peers
     */
    @Name("DBUS_INTERFACE_PEER")
     public static final String INTERFACE_PEER = "org.freedesktop.DBus.Peer";

    /**
     * <pre>[dbus/dbus-shared.h]
     * #define DBUS_INTERFACE_LOCAL "org.freedesktop.DBus.Local"</pre>
     * This is a special interface whose methods can only be invoked
     * by the local implementation (messages from remote apps aren't
     * allowed to specify this interface).
     */
    @Name("DBUS_INTERFACE_LOCAL")
    public static final String INTERFACE_LOCAL = "org.freedesktop.DBus.Local";

    /**
     * <pre>[dbus/dbus-shared.h]
     * #define DBUS_NAME_FLAG_ALLOW_REPLACEMENT 0x1</pre>
     * <strong>Owner flag</strong><br/>
     * Allow another service to become the primary owner if requested
     */
    @Name("DBUS_NAME_FLAG_ALLOW_REPLACEMENT")
    public static final int NAME_FLAG_ALLOW_REPLACEMENT = 0x1;

    /**
     * <pre>[dbus/dbus-shared.h]
     * #define DBUS_NAME_FLAG_REPLACE_EXISTING  0x2</pre>
     * <strong>Owner flag</strong><br/>
     * Request to replace the current primary owner
     */
    @Name("DBUS_NAME_FLAG_REPLACE_EXISTING")
    public static final int NAME_FLAG_REPLACE_EXISTING = 0x2;

    /**
     * <pre>[dbus/dbus-shared.h]
     * #define DBUS_NAME_FLAG_DO_NOT_QUEUE      0x4</pre>
     * <strong>Owner flag</strong><br/>
     * If we can not become the primary owner do not place us in the queue
     */
    @Name("DBUS_NAME_FLAG_DO_NOT_QUEUE")
    public static final int NAME_FLAG_DO_NOT_QUEUE = 0x4;

    /**
     * <pre>[dbus/dbus-shared.h]
     * #define DBUS_REQUEST_NAME_REPLY_PRIMARY_OWNER  1</pre>
     * <strong>Reply to request for a name</strong><br/>
     * Service has become the primary owner of the requested name
     */
    @Name("DBUS_REQUEST_NAME_REPLY_PRIMARY_OWNER")
    public static final int REQUEST_NAME_REPLY_PRIMARY_OWNER = 1;

    /**
     * <pre>[dbus/dbus-shared.h]
     * #define DBUS_REQUEST_NAME_REPLY_IN_QUEUE       2</pre>
     * <strong>Reply to request for a name</strong><br/>
     * Service could not become the primary owner and has been placed in the queue
     */
    @Name("DBUS_REQUEST_NAME_REPLY_IN_QUEUE")
    public static final int REQUEST_NAME_REPLY_IN_QUEUE = 2;

    /**
     * <pre>[dbus/dbus-shared.h]
     * #define DBUS_REQUEST_NAME_REPLY_EXISTS         3</pre>
     * <strong>Reply to request for a name</strong><br/>
     *  Service is already in the queue
     */
    @Name("DBUS_REQUEST_NAME_REPLY_EXISTS")
    public static final int REQUEST_NAME_REPLY_EXISTS = 3;

    /**
     * <pre>[dbus/dbus-shared.h]
     * #define DBUS_REQUEST_NAME_REPLY_ALREADY_OWNER  4</pre>
     * <strong>Reply to request for a name</strong><br/>
     * Service is already the primary owner
     */
    @Name("DBUS_REQUEST_NAME_REPLY_ALREADY_OWNER")
    public static final int REQUEST_NAME_REPLY_ALREADY_OWNER = 4;

    /**
     * <pre>[dbus/dbus-shared.h]
     * #define DBUS_RELEASE_NAME_REPLY_RELEASED        1</pre>
     * <strong>Reply to releasing a name</strong><br/>
     * Service was released from the given name
     */
    @Name("DBUS_RELEASE_NAME_REPLY_RELEASED")
    public static final int RELEASE_NAME_REPLY_RELEASED = 1;

    /**
     * <pre>[dbus/dbus-shared.h]
     * #define DBUS_RELEASE_NAME_REPLY_NON_EXISTENT    2</pre>
     * <strong>Reply to releasing a name</strong><br/>
     * The given name does not exist on the bus
     */
    @Name("DBUS_RELEASE_NAME_REPLY_NON_EXISTENT")
    public static final int RELEASE_NAME_REPLY_NON_EXISTENT = 2;
    /**
     * <pre>[dbus/dbus-shared.h]
     * #define DBUS_RELEASE_NAME_REPLY_NOT_OWNER       3</pre>
     * <strong>Reply to releasing a name</strong><br/>
     * Service is not an owner of the given name
     */
    @Name("DBUS_RELEASE_NAME_REPLY_NOT_OWNER")
    public static final int RELEASE_NAME_REPLY_NOT_OWNER = 3;

    /**
     * <pre>[dbus/dbus-shared.h]
     * #define DBUS_START_REPLY_SUCCESS         1</pre>
     * <strong>Reply to service starts</strong><br/>
     * Service was auto started
     */
    @Name("DBUS_START_REPLY_SUCCESS")
    public static final int START_REPLY_SUCCESS = 1;
    /**
     * <pre>[dbus/dbus-shared.h]
     * #define DBUS_START_REPLY_ALREADY_RUNNING 2</pre>
     * <strong>Reply to service starts</strong><br/>
     * Service was already running
     */
    @Name("DBUS_START_REPLY_ALREADY_RUNNING")
    public static final int START_REPLY_ALREADY_RUNNING = 2;

    /**
     * Code marking LSB-first byte order in the wire protocol.
     * <pre>[dbus/dbus-types.h]
     * #define DBUS_LITTLE_ENDIAN ('l')  /&#42;&#42;&lt; Code marking LSB-first byte order in the wire protocol. &#42;/</pre>
     */
    @Name("DBUS_LITTLE_ENDIAN")
    public static final char LITTLE_ENDIAN = 'l';

    /**
     * Code marking MSB-first byte order in the wire protocol.
     * <pre>[dbus/dbus-types.h]
     * #define DBUS_BIG_ENDIAN    ('B')  /&#42;&#42;&lt; Code marking MSB-first byte order in the wire protocol. &#42;/</pre>
     */
    @Name("DBUS_BIG_ENDIAN")
    public static final char BIG_ENDIAN = 'B';

    /**
     * Protocol version.
     * <pre>[dbus/dbus-types.h]
     * #define DBUS_MAJOR_PROTOCOL_VERSION 1</pre>
     */
    @Name("DBUS_MAJOR_PROTOCOL_VERSION")
    public static final int MAJOR_PROTOCOL_VERSION = 1;

    /**
     * Type code that is never equal to a legitimate type code
     * <pre>[dbus/dbus-types.h]
     * #define DBUS_TYPE_INVALID       ((int) '\0')</pre>
     */
    @Name("DBUS_TYPE_INVALID")
    public static final int TYPE_INVALID = ((int) '\0');

    /**
     * {@link #TYPE_INVALID} as a string literal instead of an int literal
     * <pre>[dbus/dbus-types.h]
     * #define DBUS_TYPE_INVALID_AS_STRING        "\0"</pre>
     */
    @Name("DBUS_TYPE_INVALID_AS_STRING")
     public static final String TYPE_INVALID_AS_STRING = "\0";

    /**
     * Type code marking an 8-bit unsigned integer
     * <pre>[dbus/dbus-types-h]
     * #define DBUS_TYPE_BYTE          ((int) 'y')</pre>
     */
    @Name("DBUS_TYPE_BYTE")
     public static final int TYPE_BYTE = ((int) 'y');

    /**
     * {@link #TYPE_BYTE} as a string literal instead of a int literal
     * <pre>[dbus/dbus-types-h]
     * #define DBUS_TYPE_BYTE_AS_STRING           "y"</pre>
     */
    @Name("DBUS_TYPE_BYTE_AS_STRING")
    public static final String TYPE_BYTE_AS_STRING = "y";

    /**
     * Type code marking a boolean
     * <pre>[dbus/dbus-types-h]
     * #define DBUS_TYPE_BOOLEAN       ((int) 'b')</pre>
     */
    @Name("DBUS_TYPE_BOOLEAN")
    public static final int TYPE_BOOLEAN = ((int) 'b');

    /**
     * {@link #TYPE_BOOLEAN} as a string literal instead of a int literal
     * <pre>[dbus/dbus-types.h]
     * #define DBUS_TYPE_BOOLEAN_AS_STRING        "b"</pre>
     */
    @Name("DBUS_TYPE_BOOLEAN_AS_STRING")
    public static final String TYPE_BOOLEAN_AS_STRING = "b";

    /**
     * Type code marking a 16-bit signed integer
     * <pre>[dbus/dbus-types.h]
     * #define DBUS_TYPE_INT16         ((int) 'n')</pre>
     */
    @Name("DBUS_TYPE_INT16")
     public static final int TYPE_INT16 = ((int) 'n');

    /**
     * {@link #TYPE_INT16} as a string literal instead of a int literal
     * <pre>[dbus/dbus-types.h]
     * #define DBUS_TYPE_INT16_AS_STRING          "n"</pre>
     */
    @Name("DBUS_TYPE_INT16_AS_STRING")
    public static final String TYPE_INT16_AS_STRING = "n";

    /**
     * Type code marking a 16-bit unsigned integer
     * <pre>[dbus/dbus-types-h]
     * #define DBUS_TYPE_UINT16        ((int) 'q')</pre>
     */
    @Name("DBUS_TYPE_UINT16")
    public static final int TYPE_UINT16 = ((int) 'q');

    /**
     * {@link #TYPE_UINT16} as a string literal instead of a int literal
     * <pre>[dbus/dbus-types-h]
     * #define DBUS_TYPE_UINT16_AS_STRING         "q"</pre>
     */
    @Name("DBUS_TYPE_UINT16_AS_STRING")
    public static final String TYPE_UINT16_AS_STRING = "q";

    /**
     * Type code marking a 32-bit signed integer
     * <pre>[dbus/dbus-types-h]
     * #define DBUS_TYPE_INT32         ((int) 'i')</pre>
     */
    @Name("DBUS_TYPE_INT32")
    public static final int TYPE_INT32 = ((int) 'i');

    /**
     * {@link #TYPE_INT32} as a string literal instead of a int literal
     * <pre>[dbus/dbus-types-h]
     * #define DBUS_TYPE_INT32_AS_STRING          "i"</pre>
     */
    @Name("DBUS_TYPE_INT32_AS_STRING")
    public static final String TYPE_INT32_AS_STRING = "i";

    /**
     * Type code marking a 32-bit unsigned integer
     * <pre>[dbus/dbus-types-h]
     * #define DBUS_TYPE_UINT32        ((int) 'u')</pre>
     */
    @Name("DBUS_TYPE_UINT32")
    public static final int TYPE_UINT32 = ((int) 'u');

    /**
     * {@link #TYPE_UINT32} as a string literal instead of a int literal
     * <pre>[dbus/dbus-types-h]
     * #define DBUS_TYPE_UINT32_AS_STRING         "u"</pre>
     */
    @Name("DBUS_TYPE_UINT32_AS_STRING")
    public static final String TYPE_UINT32_AS_STRING = "u";

    /**
     * Type code marking a 64-bit signed integer
     * <pre>[dbus/dbus-types-h]
     * #define DBUS_TYPE_INT64         ((int) 'x')</pre>
     */
    @Name("DBUS_TYPE_INT64")
    public static final int TYPE_INT64 = ((int) 'x');

    /**
     * {@link #TYPE_INT64} as a string literal instead of a int literal
     * <pre>[dbus/dbus-types-h]
     * #define DBUS_TYPE_INT64_AS_STRING          "x"</pre>
     */
    @Name("DBUS_TYPE_INT64_AS_STRING")
    public static final String TYPE_INT64_AS_STRING = "x";

    /**
     * Type code marking a 64-bit unsigned integer
     * <pre>[dbus/dbus-types-h]
     * #define DBUS_TYPE_UINT64        ((int) 't')</pre>
     */
    @Name("DBUS_TYPE_UINT64")
    public static final int TYPE_UINT64 = ((int) 't');

    /**
     * {@link #TYPE_UINT64} as a string literal instead of a int literal
     * <pre>[dbus/dbus-types-h]
     * #define DBUS_TYPE_UINT64_AS_STRING         "t"</pre>
     */
    @Name("DBUS_TYPE_UINT64_AS_STRING")
    public static final String TYPE_UINT64_AS_STRING = "t";

    /**
     * Type code marking an 8-byte double in IEEE 754 format
     * <pre>[dbus/dbus-types-h]
     * #define DBUS_TYPE_DOUBLE        ((int) 'd')</pre>
     */
    @Name("DBUS_TYPE_DOUBLE")
    public static final int TYPE_DOUBLE = ((int) 'd');

    /**
     * {@link #TYPE_DOUBLE} as a string literal instead of a int literal
     * <pre>[dbus/dbus-types-h]
     * #define DBUS_TYPE_DOUBLE_AS_STRING         "d"</pre>
     */
    @Name("DBUS_TYPE_DOUBLE_AS_STRING")
    public static final String TYPE_DOUBLE_AS_STRING = "d";

    /**
     * Type code marking a UTF-8 encoded, nul-terminated Unicode string
     * <pre>[dbus/dbus-types-h]
     * #define DBUS_TYPE_STRING        ((int) 's')</pre>
     */
    @Name("DBUS_TYPE_STRING")
    public static final int TYPE_STRING = ((int) 's');

    /**
     * {@link #TYPE_STRING} as a string literal instead of a int literal
     * <pre>[dbus/dbus-types-h]
     * #define DBUS_TYPE_STRING_AS_STRING         "s"</pre>
     */
    @Name("DBUS_TYPE_STRING_AS_STRING")
    public static final String TYPE_STRING_AS_STRING = "s";

    /**
     * Type code marking a D-Bus object path
     * <pre>[dbus/dbus-types-h]
     * #define DBUS_TYPE_OBJECT_PATH   ((int) 'o')</pre>
     */
    @Name("DBUS_TYPE_OBJECT_PATH")
    public static final int TYPE_OBJECT_PATH = ((int) 'o');

    /**
     * {@link #TYPE_OBJECT_PATH} as a string literal instead of a int literal
     * <pre>[dbus/dbus-types-h]
     * #define DBUS_TYPE_OBJECT_PATH_AS_STRING    "o"</pre>
     */
    @Name("DBUS_TYPE_OBJECT_PATH_AS_STRING")
    public static final String TYPE_OBJECT_PATH_AS_STRING = "o";

    /**
     * Type code marking a D-Bus type signature
     * <pre>[dbus/dbus-types-h]
     * #define DBUS_TYPE_SIGNATURE     ((int) 'g')</pre>
     */
    @Name("DBUS_TYPE_SIGNATURE")
    public static final int TYPE_SIGNATURE = ((int) 'g');

    /**
     * {@link #TYPE_SIGNATURE} as a string literal instead of a int literal
     * <pre>[dbus/dbus-types-h]
     * #define DBUS_TYPE_SIGNATURE_AS_STRING      "g"</pre>
     */
    @Name("DBUS_TYPE_SIGNATURE_AS_STRING")
    public static final String TYPE_SIGNATURE_AS_STRING = "g";

    /**
     * Type code marking a unix file descriptor
     * <pre>[dbus/dbus-types-h]
     * #define DBUS_TYPE_UNIX_FD      ((int) 'h')</pre>
     */
    @Name("DBUS_TYPE_UNIX_FD")
    public static final int TYPE_UNIX_FD = ((int) 'h');

    /**
     * {@link #TYPE_UNIX_FD} as a string literal instead of a int literal
     * <pre>[dbus/dbus-types-h]
     * #define DBUS_TYPE_UNIX_FD_AS_STRING        "h"</pre>
     */
    @Name("DBUS_TYPE_UNIX_FD_AS_STRING")
    public static final String TYPE_UNIX_FD_AS_STRING = "h";


    // =================================================================================================================
    // ---- Functions that need to be wrapped to make up for a nice Java API -------------------------------------------
    // =================================================================================================================

    // ---- dbus/dbus-address.h ----------------------------------------------------------------------------------------

    /**
     * <pre>[dbus/dbus-address.h]
     * dbus_bool_t dbus_parse_address            (const char         *address,
     *                                            DBusAddressEntry ***entry,
     *                                            int                *array_len,
     *                                            DBusError          *error);</pre>
     *
     */
    @Name("dbus_parse_address")
    protected static native boolean _parseAddress(Pointer<Byte> address,
                                                  Pointer<Pointer<_AddressEntry[]>> entry,
                                                  Pointer<Integer> arrayLen,
                                                  Pointer<Error> error);

    /**
     * <pre>[dbus/dbus-address.h]#
     * const char *dbus_address_entry_get_value  (DBusAddressEntry   *entry,
     *                                            const char         *key);</pre>
     */
    @Name("dbus_address_entry_get_value")
    protected static native Pointer<Byte> _addressEntryGetValue(Pointer<_AddressEntry> entry,
                                                                Pointer<Byte> key);

    /**
     * <pre>[dbus/dbus-address.h]
     * const char *dbus_address_entry_get_method (DBusAddressEntry   *entry);</pre>
     */
    @Name("dbus_address_entry_get_method")
    protected static native Pointer<Byte> _addressEntryGetMethod(Pointer<_AddressEntry> entry);

    /**
     * <pre>[dbus/dbus-address.h
     * void        dbus_address_entries_free     (DBusAddressEntry  **entries);</pre>
     */
    @Name("dbus_address_entries_free")
    protected static native void _addressEntriesFree(Pointer<_AddressEntry[]> entries);

    /**
     * <pre>[dbus/dbus-address.h]
     * char* dbus_address_escape_value   (const char *value);</pre>
     */
    @Name("dbus_address_escape_value")
    protected static native Pointer<Byte> _addressEscapeValue(Pointer<Byte> value);

    /**
     * <pre>[dbus/dbus-address.h
     * char* dbus_address_unescape_value (const char *value,
     *                                    DBusError  *error);</pre>
     */
    @Name("dbus_address_unescape_value")
    protected static native Pointer<Byte> _addressUnescapeValue(Pointer<Byte> value,
                                                                Pointer<_Error> error);

    // ---- dbus/dbus-bus.h --------------------------------------------------------------------------------------------

    /**
     * <pre>[dbus/dbus-bus.h]
     * DBusConnection *dbus_bus_get              (DBusBusType     type,
     *                                            DBusError      *error);
     * </pre>
     */
    @Name("dbus_bus_get")
    protected static native Pointer<_Connection> _busGet(BusType type,
                                                        Pointer<_Error> error);

    /**
     * <pre>[dbus/dbus-bus.h
     * DBusConnection *dbus_bus_get_private      (DBusBusType     type,
     *                                            DBusError      *error);</pre>
     */
    @Name("dbus_bus_get_private")
    protected static native Pointer<_Connection> _busGetPrivate(BusType type,
                                                               Pointer<_Error> error);

    /**
     * <pre>[dbus/dbus-bus.h]
     * dbus_bool_t     dbus_bus_register         (DBusConnection *connection,
     *                                            DBusError      *error);</pre>
     */
    @Name("dbus_bus_register")
    protected static native boolean _busRegister(Pointer<_Connection> connection,
                                                 Pointer<_Error> error);

    /**
     * <pre>[dbus/dbus-bus.h]
     * dbus_bool_t     dbus_bus_set_unique_name  (DBusConnection *connection,
     *                                            const char     *unique_name);</pre>
     */
    @Name("dbus_bus_set_unique_name")
    protected static native boolean _busSetUniqueName(Pointer<_Connection> connection,
                                                      Pointer<Byte> uniqueName);

    /**
     * <pre>[dbus/dbus-bus.h]
     * const char*     dbus_bus_get_unique_name  (DBusConnection *connection);</pre>
     */
    @Name("dbus_bus_get_unique_name")
    protected static native Pointer<Byte> _busGetUniqueName(Pointer<_Connection> connection);

    /**
     * <pre>[dbus/dbus-bus.h]
     * unsigned long   dbus_bus_get_unix_user    (DBusConnection *connection,
     *                                            const char     *name,
     *                                            DBusError      *error);</pre>
     */
    @Name("dbus_bus_get_unix_user")
    protected static native long _busGetUnixUser(Pointer<_Connection> connection,
                                                 Pointer<Byte> name,
                                                 Pointer<_Error> error);

    /**
     * <pre>[dbus/dbus-bus.h]
     * char*           dbus_bus_get_id           (DBusConnection *connection,
     *                                            DBusError      *error);
     * </pre>
     */
    @Name("dbus_bus_get_id")
    protected static native Pointer<Byte> _busGetId(Pointer<_Connection> connection,
                                                    Pointer<_Error> error);

    /**
     * <pre>[dbus/dbus-bus.h]
     * int             dbus_bus_request_name     (DBusConnection *connection,
     *                                            const char     *name,
     *                                            unsigned int    flags,
     *                                            DBusError      *error);</pre>
     */
    @Name("dbus_bus_request_name")
    protected static native int _busRequestName(Pointer<_Connection> connection,
                                                Pointer<Byte> name,
                                                int flags,
                                                Pointer<_Error> error);

    /**
     * <pre>[dbus/dbus-bus.h]
     * int             dbus_bus_release_name     (DBusConnection *connection,
     *                                            const char     *name,
     *                                            DBusError      *error);</pre>
     */
    @Name("dbus_bus_release_name")
    protected static native int _busReleaseName(Pointer<_Connection> connection,
                                                Pointer<Byte> name,
                                                Pointer<_Error> error);

    /**
     * <pre>[dbus/dbus-bus.h]
     * dbus_bool_t     dbus_bus_name_has_owner   (DBusConnection *connection,
     *                                            const char     *name,
     *                                            DBusError      *error);</pre>
     */
    @Name("dbus_bus_name_has_owner")
    protected static native boolean _busNameHasOwner(Pointer<_Connection> connection,
                                                     Pointer<Byte> name,
                                                     Pointer<_Error> error);

    /**
     * <pre>[dbus/dbus-bus.h]
     * dbus_bool_t     dbus_bus_start_service_by_name (DBusConnection *connection,
     *                                                 const char     *name,
     *                                                 dbus_uint32_t   flags,
     *                                                 dbus_uint32_t  *reply,
     *                                                 DBusError      *error);</pre>
     */
    @Name("dbus_bus_start_service_by_name")
    protected static native boolean _busStartServiceByName(Pointer<_Connection> connection,
                                                           Pointer<Byte> name,
                                                           int flags,
                                                           Pointer<Integer> reply,
                                                           Pointer<_Error> error);

    /**
     * <pre>[dbus/dbus-bus.h]
     * void            dbus_bus_add_match        (DBusConnection *connection,
     *                                            const char     *rule,
     *                                            DBusError      *error);</pre>
     */
    @Name("dbus_bus_add_match")
    protected static native void _busAddMatch(Pointer<_Connection> connection,
                                              Pointer<Byte> rule,
                                              Pointer<_Error> error);

    /**
     * <pre>[dbus/dbus-bus.h]
     * void            dbus_bus_remove_match     (DBusConnection *connection,
     *                                            const char     *rule,
     *                                            DBusError      *error);</pre>
     */
    @Name("dbus_bus_remove_match")
    protected static native void _busRemoveMatch(Pointer<_Connection> connection,
                                                 Pointer<Byte> rule,
                                                 Pointer<_Error> error);

    // ---- dbus/dbus-connection.h -------------------------------------------------------------------------------------

    /**
     * <pre>[dbus/dbus-connection.h]
     * DBusConnection*    dbus_connection_open                         (const char                 *address,
     *                                                                  DBusError                  *error);</pre>
     */
    @Name("dbus_connection_open")
    protected static native Pointer<_Connection> _connectionOpen(Pointer<Byte> address,
                                                                 Pointer<_Error> error);

    /**
     * <pre>[dbus/dbus-connection.h]
     * DBusConnection*    dbus_connection_open_private                 (const char                 *address,
     *                                                                  DBusError                  *error);</pre>
     */
    @Name("dbus_connection_open_private")
    protected static native Pointer<_Connection> _connectionOpenPrivate(Pointer<Byte> address,
                                                                        Pointer<_Error> error);

    /**
     * <pre>[dbus/dbus-connection-h
     * DBusConnection*    dbus_connection_ref                          (DBusConnection             *connection);</pre>
     */
    @Name("dbus_connection_ref")
    protected static native Pointer<_Connection> _connectionRef(Pointer<_Connection> connection);

    /**
     * <pre>[dbus/dbus-connection.h]
     * void               dbus_connection_unref                        (DBusConnection             *connection);</pre>
     */
    @Name("dbus_connection_unref")
    protected static native void _connectionUnref(Pointer<_Connection> connection);

    /**
     * <pre>[dbus/dbus-connection.h]
     * void               dbus_connection_close                        (DBusConnection             *connection);</pre>
     */
    @Name("dbus_connection_close")
    protected static native void _connectionClose(Pointer<_Connection> connection);

    /**
     * <pre>[dbus/dbus-connection.h]
     * dbus_bool_t        dbus_connection_get_is_connected             (DBusConnection             *connection);</pre>
     */
    @Name("dbus_connection_get_is_connected")
    protected static native boolean _connectionGetIsConnected(Pointer<_Connection> connection);

    /**
     * <pre>[dbus/dbus-connection.h]
     * dbus_bool_t        dbus_connection_get_is_authenticated         (DBusConnection             *connection);</pre>
     */
    @Name("dbus_connection_get_is_authenticated")
    protected static native boolean _connectionGetIsAuthenticated(Pointer<_Connection> connection);

    /**
     * <pre>[dbus/dbus-connection.h]
     * dbus_bool_t        dbus_connection_get_is_anonymous             (DBusConnection             *connection);</pre>
     */
    @Name("dbus_connection_get_is_anonymous")
    protected static native boolean _connectionGetIsAnonymous(Pointer<_Connection> connection);

    /**
     * <pre>[dbus/dbus-connection.h]
     * char*              dbus_connection_get_server_id                (DBusConnection             *connection);</pre>
     */
    @Name("dbus_connection_get_server_id")
    protected static native Pointer<Byte> _connectionGetServerId(Pointer<_Connection> connection);

    /**
     * <pre>[dbus/dbus-connection.h]
     * dbus_bool_t        dbus_connection_can_send_type                (DBusConnection             *connection,
     *                                                                  int                         type);</pre>
     */
    @Name("dbus_connection_can_send_type")
    protected static native boolean _connectionCanSendType(Pointer<_Connection> connection, int type);

    /**
     * <pre>[dbus/dbus-connection.h]
     * void               dbus_connection_set_exit_on_disconnect       (DBusConnection             *connection,
     *                                                                  dbus_bool_t                 exit_on_disconnect);</pre>
     */
    @Name("dbus_connection_set_exit_on_disconnect")
    protected static native void _connectionSetExitOnDisconnect(Pointer<_Connection> connection,
                                                                boolean exitOnDisconnect);

    /**
     * <pre>[dbus/dbus-connection.h]
     * void               dbus_connection_flush                        (DBusConnection             *connection);</pre>
     */
    @Name("dbus_connection_flush")
    protected static native void _connectionFlush(Pointer<_Connection> connection);

    /**
     * <pre>[dbus/dbus-connection.h]
     * dbus_bool_t        dbus_connection_send                         (DBusConnection             *connection,
     *                                                                  DBusMessage                *message,
     *                                                                  dbus_uint32_t              *client_serial);</pre>
     */
    @Name("dbus_connection_send")
    protected static native boolean _connectionSend(Pointer<_Connection> connection,
                                                    Pointer<_Message> message,
                                                    Pointer<Long> clientSerial);

    /**
     * <pre>[dbus/dbus-connection.h]
     * dbus_bool_t        dbus_connection_send_with_reply              (DBusConnection             *connection,
     *                                                                  DBusMessage                *message,
     *                                                                  DBusPendingCall           **pending_return,
     *                                                                  int                         timeout_milliseconds);</pre>
     */
    @Name("dbus_connection_send_with_reply")
    protected static native boolean _connectionSendWithReply(Pointer<_Connection> connection,
                                                             Pointer<_Message> message,
                                                             Pointer<Pointer<_PendingCall>> pendingReturn,
                                                             int timeoutMilliseconds);

    // ---- dbus/dbus-error.h ------------------------------------------------------------------------------------------

    /**
     * <pre>[dbus/dbus-error.h]
     * void        dbus_error_init      (DBusError       *error);
     * </pre>
     */
    @Name("dbus_error_init")
    protected static native void _errorInit(Pointer<_Error> error);

    /**
     * <pre>[dbus/dbus-error.h]
     * void        dbus_error_free      (DBusError       *error);</pre>
     */
    @Name("dbus_error_free")
    protected static native void _errorFree(Pointer<_Error> error);

    /**
     * <pre>[dbus/dbus-error.h]
     * void        dbus_set_error       (DBusError       *error,
     *                                   const char      *name,
     *                                   const char      *message,
     *                                   ...);</pre>e
     */
    @Name("dbus_set_error")
    protected static native void _setError(Pointer<_Error> error,
                                           Pointer<Byte> name,
                                           Pointer<Byte> message,
                                           Object... varargs);

    /**
     * <pre>[dbus/dbus-error.h]
     * void        dbus_set_error_const (DBusError       *error,
     *                                   const char      *name,
     *                                   const char      *message);</pre>
     */
    @Name("dbus_set_error_const")
    protected static native void _setErrorConst(Pointer<_Error> error,
                                                Pointer<Byte> name,
                                                Pointer<Byte> message);

    /**
     * <pre>[dbus/dbus-error.h]
     * void        dbus_move_error      (DBusError       *src,
     *                                   DBusError       *dest);</pre>
     */
    @Name("dbus_move_error")
    protected static native void _moveError(Pointer<_Error> source,
                                            Pointer<_Error> destination);

    /**
     * <pre>[dbus/dbus-error.h]
     * dbus_bool_t dbus_error_has_name  (const DBusError *error,
     *                                   const char      *name);</pre>
     */
    @Name("dbus_error_has_name")
    protected static native boolean _errorHasName(Pointer<_Error> error,
                                                  Pointer<Byte> name);

    /**
     * <pre>[dbus/dbus-error.h]
     * dbus_bool_t dbus_error_is_set    (const DBusError *error);</pre>
     */
    @Name("dbus_error_is_set")
    protected static native boolean _errorIsSet(Pointer<_Error> error);

    // ---- dbus/dbus-memory.h -----------------------------------------------------------------------------------------

    /**
     * <pre>[dbus/dbus-memory.h]
     * void* dbus_malloc        (size_t bytes);</pre>
     */
    @Name("dbus_malloc")
    protected static native <T> Pointer<T> _malloc(SizeT bytes);

    /**
     * <pre>[dbus/dbus-memory.h]
     * void* dbus_malloc0       (size_t bytes);</pre>
     */
    @Name("dbus_malloc0")
    protected static native <T> Pointer<T> _malloc0(SizeT bytes);

    /**
     * <pre>[dbus/dbus-memory.h]
     * void* dbus_realloc       (void  *memory,
     *                           size_t bytes);</pre>
     */
    @Name("dbus_realloc")
    protected static native void _realloc(Pointer<?> memory,
                                          SizeT bytes);

    /**
     * <pre>[dbus/dbus-memory.h]
     * void  dbus_free          (void  *memory);</pre>
     */
    @Name("dbus_free")
    protected static native void _free(Pointer<?> memory);

    /**
     * <pre>[dbus/dbus-memory.h]
     * void dbus_free_string_array (char **str_array);</pre>
     */
    @Name("dbus_free_string_array")
    protected static native void _freeStringArray(Pointer<Pointer<Byte>> strArray);

    /**
     * MACRO (rewritten in Java)
     * <pre>[dbus/dbus-memory.h]
     * #define dbus_new(type, count)  ((type*)dbus_malloc (sizeof (type) * (count)))</pre>
     */
    @Name("dbus_new")
    protected static <T> Pointer<T> _new(Type type, int count) {
        return _malloc(SizeT.valueOf(BridJ.sizeOf(type) * count));
    }

    /**
     * MACRO (rewritten in Java)
     * <pre>[dbus/dbus-memory.h]
     * #define dbus_new0(type, count) ((type*)dbus_malloc0 (sizeof (type) * (count)))
     </pre>
     */
    @Name("dbus_new0")
    protected static <T> Pointer<T>_new0(Class<?> type, int count) {
        return _malloc0(SizeT.valueOf(BridJ.sizeOf(type) * count));
    }

    /**
     * <pre>[dbus/dbus-memory.h]
     * void dbus_shutdown (void);</pre>
     */
    @Name("dbus_shutdown")
    protected static native void _shutdown();

    // ---- dbus/dbus-message.h ----------------------------------------------------------------------------------------

    /**
     * <pre>[dbus/dbus-message.h]
     * DBusMessage* dbus_message_new               (int          message_type);</pre>
     */
    @Name("dbus_message_new")
    protected static native Pointer<_Message> _messageNew(int messageType);

    /**
     * <pre>[dbus/dbus-message.h]
     * DBusMessage* dbus_message_new_method_call   (const char  *bus_name,
     *                                              const char  *path,
     *                                              const char  *interface,
     *                                              const char  *method);</pre>
     */
    @Name("dbus_message_new_method_call")
    protected static native Pointer<_Message> _messageNewMethodCall(Pointer<Byte> busName,
                                                                   Pointer<Byte> path,
                                                                   Pointer<Byte> _interface,
                                                                   Pointer<Byte> method);

    /**
     * <pre>[dbus/dbus-message.h]
     * DBusMessage* dbus_message_new_method_return (DBusMessage *method_call);</pre>
     */
    @Name("dbus_message_new_method_return")
    protected static native Pointer<_Message> _messageNewMethodReturn(Pointer<_Message> methodCall);

    /**
     * <pre>[dbus/dbus-message.h]
     * DBusMessage* dbus_message_new_signal        (const char  *path,
     *                                              const char  *interface,
     *                                              const char  *name);</pre>
     */
    @Name("dbus_message_new_signal")
    protected static native Pointer<_Message> _messageNewSignal(Pointer<Byte> path,
                                                               Pointer<Byte> _interface,
                                                               Pointer<Byte> name);

    /**
     * <pre>[dbus/dbus-message.h]
     * DBusMessage* dbus_message_new_error         (DBusMessage *reply_to,
     *                                              const char  *error_name,
     *                                              const char  *error_message);</pre>
     */
    @Name("dbus_message_new_error")
    protected static native Pointer<_Message> _messageNewError(Pointer<_Message> replyTo,
                                                              Pointer<Byte> errorName,
                                                              Pointer<Byte> errorMessage);

    /**
     * <pre>[dbus/dbus-message.h]
     * DBusMessage* dbus_message_new_error_printf  (DBusMessage *reply_to,
     *                                              const char  *error_name,
     *                                              const char  *error_format,
     *                                              ...)</pre>
     */
    @Name("dbus_message_new_error_printf")
    protected static native Pointer<_Message> _messageNewErrorPrintf(Pointer<_Message> replyTo,
                                                                    Pointer<Byte> errorName,
                                                                    Pointer<Byte> errorFormat,
                                                                    Object... varargs);

    /**
     * Creates a new message that is an exact replica of the message specified, except that its refcount is set to 1,
     * its message serial is reset to 0, and if the original message was "locked" (in the outgoing message queue and
     * thus not modifiable) the new message will not be locked.
     * <pre>[dbus/dbus-message.h]
     * DBusMessage* dbus_message_copy              (const DBusMessage *message);</pre>
     */
    @Name("dbus_message_copy")
    protected static native Pointer<_Message> _messageCopy(Pointer<_Message> message);

    /**
     * <pre>[dbus/dbus-message.h]
     * DBusMessage*  dbus_message_ref              (DBusMessage   *message);</pre>
     */
    @Name("dbus_message_ref")
    protected static native Pointer<_Message> _messageRef(Pointer<_Message> message);

    /**
     * <pre>[dbus/dbus-message.h]
     * DBusMessage*  dbus_message_unref            (DBusMessage   *message);</pre>
     */
    @Name("dbus_message_unref")
    protected static native Pointer<_Message> _messageUnref(Pointer<_Message> message);

    /**
     * <pre>[dbus/dbus-message.h]
     * int           dbus_message_get_type         (DBusMessage   *message);</pre>
     */
    @Name("dbus_message_get_type")
    protected static native int _messageGetType(Pointer<_Message> message);

    /**
     * <pre>[dbus/dbus-message.h]
     * dbus_bool_t   dbus_message_set_path         (DBusMessage   *message,
     *                                              const char    *object_path);</pre>
     */
    @Name("dbus_message_set_path")
    protected static native boolean _messageSetPath(Pointer<_Message> message,
                                                    Pointer<Byte> objectPath);

    /**
     * <pre>[dbus/dbus-message.h]
     * const char*   dbus_message_get_path         (DBusMessage   *message);</pre>
     */
    @Name("dbus_message_get_path")
    protected static native Pointer<Byte> _messageGetPath(Pointer<_Message> message);

    /**
     * <pre>[dbus/dbus-message.h]
     * dbus_bool_t   dbus_message_has_path         (DBusMessage   *message,
     *                                              const char    *object_path);</pre>
     */
    @Name("dbus_message_has_path")
    protected static native boolean _messageHasPath(Pointer<_Message> message,
                                                    Pointer<Byte> objectPath);

    /**
     * <pre>[dbus/dbus-message.h]
     * dbus_bool_t   dbus_message_set_interface    (DBusMessage   *message,
     *                                              const char    *interface);</pre>
     */
    @Name("dbus_message_set_interface")
    protected static native boolean _messageSetInterface(Pointer<_Message> message,
                                                         Pointer<Byte> _interface);

    /**
     * <pre>[dbus/dbus-message.h]
     * const char*   dbus_message_get_interface    (DBusMessage   *message);</pre>
     */
    @Name("dbus_message_get_interface")
    protected static native Pointer<Byte> _messageGetInterface(Pointer<_Message> message);

    /**
     * <pre>[dbus/dbus-message.h]
     * dbus_bool_t   dbus_message_has_interface    (DBusMessage   *message,
     *                                              const char    *interface);</pre>
     */
    @Name("dbus_message_has_interface")
    protected static native boolean _messageHasInterface(Pointer<_Message> message,
                                                         Pointer<Byte> _interface);

    /**
     * <pre>[dbus/dbus-message.h]
     * dbus_bool_t   dbus_message_set_member       (DBusMessage   *message,
     *                                              const char    *member);</pre>
     */
    @Name("dbus_message_set_member")
    protected static native boolean _messageSetMember(Pointer<_Message> message,
                                                      Pointer<Byte> member);

    /**
     * <pre>[dbus/dbus-message.h]
     * const char*   dbus_message_get_member       (DBusMessage   *message);</pre>
     */
    @Name("dbus_message_get_member")
    protected static native Pointer<Byte> _messageGetMember(Pointer<_Message> message);

    /**
     * <pre>[dbus/dbus-message.h]
     * dbus_bool_t   dbus_message_has_member       (DBusMessage   *message,
     *                                              const char    *member);</pre>
     */
    @Name("dbus_message_has_member")
    protected static native boolean _messageHasMember(Pointer<_Message> message,
                                                      Pointer<Byte> member);

    /**
     * <pre>[dbus/dbus-message.h]
     * dbus_bool_t   dbus_message_set_error_name   (DBusMessage   *message,
     *                                              const char    *name);</pre>
     */
    @Name("dbus_message_set_error_name")
    protected static native boolean _messageSetErrorName(Pointer<_Message> message,
                                                         Pointer<Byte> name);

    /**
     * <pre>[dbus/dbus-message.h]
     * const char*   dbus_message_get_error_name   (DBusMessage   *message);</pre>
     */
    @Name("dbus_message_get_error_name")
    protected static native Pointer<Byte> _messageGetErrorName(Pointer<_Message> message);

    /**
     * <pre>[dbus/dbus-message.h]
     * dbus_bool_t   dbus_message_set_destination  (DBusMessage   *message,
     *                                              const char    *destination);</pre>
     */
    @Name("dbus_message_set_destination")
    protected static native boolean _messageSetDestination(Pointer<_Message> message,
                                                           Pointer<Byte> destination);

    /**
     * <pre>[dbus/dbus-message.h]
     * const char*   dbus_message_get_destination  (DBusMessage   *message)</pre>
     */
    @Name("dbus_message_get_destination")
    protected static native Pointer<Byte> _messageGetDestination(Pointer<_Message> message);

    /**
     * <pre>[dbus/dbus-message.h]
     * dbus_bool_t   dbus_message_set_sender       (DBusMessage   *message,
     *                                              const char    *sender);</pre>
     */
    @Name("dbus_message_set_sender")
    protected static native boolean _messageSetSender(Pointer<_Message> message,
                                                      Pointer<Byte> sender);

    /**
     * <pre>[dbus/dbus-message.h]
     * const char*   dbus_message_get_sender       (DBusMessage   *message);</pre>
     */
    @Name("dbus_message_get_sender")
    protected static native Pointer<Byte> _messageGetSender(Pointer<_Message> message);

    /**
     * <pre>[dbus/dbus-message.h]
     * const char*   dbus_message_get_signature    (DBusMessage   *message);</pre>
     */
    @Name("dbus_message_get_signature")
    protected static native Pointer<Byte> _messageGetSignature(Pointer<_Message> message);

    /**
     * <pre>[dbus/dbus-message.h]
     * void          dbus_message_set_no_reply     (DBusMessage   *message,
     *                                              dbus_bool_t    no_reply);</pre>
     */
    @Name("dbus_message_set_no_reply")
    protected static native void _messageSetNoReply(Pointer<_Message> message,
                                                    boolean noReply);

    /**
     * <pre>[dbus/dbus-message.h]
     * dbus_bool_t   dbus_message_get_no_reply     (DBusMessage   *message);</pre>
     */
    @Name("dbus_message_get_no_reply")
    protected static native boolean _messageGetNoReply(Pointer<_Message> message);

    /**
     * <pre>[dbus/dbus-message.h]
     * dbus_bool_t   dbus_message_is_method_call   (DBusMessage   *message,
     *                                              const char    *interface,
     *                                              const char    *method);</pre>
     */
    @Name("dbus_message_is_method_call")
    protected static native boolean _messageIsMethodCall(Pointer<_Message> message,
                                                         Pointer<Byte> _interface,
                                                         Pointer<Byte> method);

    /**
     * <pre>[dbus/dbus-message.h]
     * dbus_bool_t   dbus_message_is_signal        (DBusMessage   *message,
     *                                              const char    *interface,
     *                                              const char    *signal_name);</pre>
     */
    @Name("dbus_message_is_signal")
    protected static native boolean _messageIsSignal(Pointer<_Message> message,
                                                     Pointer<Byte> _interface,
                                                     Pointer<Byte> signalName);

    /**
     * <pre>[dbus/dbus-message.h]
     * dbus_bool_t   dbus_message_is_error         (DBusMessage   *message,
     *                                              const char    *error_name);</pre>
     */
    @Name("dbus_message_is_error")
    protected static native boolean _messageIsError(Pointer<_Message> message,
                                                    Pointer<Byte> errorName);

    /**
     * <pre>[dbus/dbus-message.h]
     * dbus_bool_t   dbus_message_has_destination  (DBusMessage   *message,
     *                                              const char    *bus_name);</pre>
     */
    @Name("dbus_message_has_destination")
    protected static native boolean _messageHasDestination(Pointer<_Message> message,
                                                           Pointer<Byte> busName);

    /**
     * <pre>[dbus/dbus-message.h]
     * dbus_bool_t   dbus_message_has_sender       (DBusMessage   *message,
     *                                              const char    *unique_bus_name);</pre>
     */
    @Name("dbus_message_has_sender")
    protected static native boolean _messageHasSender(Pointer<_Message> message,
                                                      Pointer<Byte> sender);

    /**
     * <pre>[dbus/dbus-message.h]
     * dbus_bool_t   dbus_message_has_signature    (DBusMessage   *message,
     *                                              const char    *signature);</pre>
     */
    @Name("dbus_message_has_signature")
    protected static native boolean _messageHasSignature(Pointer<_Message> message,
                                                         Pointer<Byte> signature);

    /**
     * <pre>[dbus/dbus.message.h]
     * dbus_uint32_t dbus_message_get_serial       (DBusMessage   *message);</pre>
     */
    @Name("dbus_message_get_serial")
    protected static native long _messageGetSerial(Pointer<_Message> message);

    /**
     * <pre>[dbus/dbus.message.h]
     * void          dbus_message_set_serial       (DBusMessage   *message,
     *                                              dbus_uint32_t  serial);</pre>
     */
    @Name("dbus_message_set_serial")
    protected static native void _messageSetSerial(Pointer<_Message> message, long serial);

    /**
     * <pre>[dbus/dbus.message.h]
     * dbus_bool_t   dbus_message_set_reply_serial (DBusMessage   *message,
     *                                              dbus_uint32_t  reply_serial);</pre>
     */
    @Name("dbus_message_set_reply_serial")
    protected static native boolean _messageSetReplySerial(Pointer<_Message> message,
                                                           long replySerial);

    /**
     * <pre>[dbus/dbus.message.h]
     * dbus_uint32_t dbus_message_get_reply_serial (DBusMessage   *message);</pre>
     */
    @Name("dbus_message_get_reply_serial")
    protected static native long _messageGetReplySerial(Pointer<_Message> message);

    /**
     * <pre>[dbus/dbus.message.h]
     * void          dbus_message_set_auto_start   (DBusMessage   *message,
     *                                              dbus_bool_t    auto_start);</pre>
     */
    @Name("dbus_message_set_auto_start")
    protected static native void _messageSetAutoStart(Pointer<_Message> message,
                                                      boolean autoStart);

    /**
     * <pre>[dbus/dbus.message.h]
     * dbus_bool_t   dbus_message_get_auto_start   (DBusMessage   *message);</pre>
     */
    @Name("dbus_message_get_auto_start")
    protected static native boolean _messageGetAutoStart(Pointer<_Message> message);

    /**
     * <pre>[dbus/dbus-message.h]
     * dbus_bool_t   dbus_message_get_path_decomposed (DBusMessage   *message,
     *                                                 char        ***path);</pre>
     */
    @Name("dbus_message_get_path_decomposed")
    protected static native boolean _messageGetPathDecomposed(Pointer<_Message> message,
                                                              Pointer<Byte[]> path);

    /**
     * <pre>[dbus/dbus-message.h]
     * dbus_bool_t dbus_message_append_args          (DBusMessage     *message,
     *                                                int              first_arg_type,
     *                                                ...);
     </pre>
     */
    @Name("dbus_message_append_args")
    protected static native boolean _messageAppendArgs(Pointer<_Message> message,
                                                       int firstArgType,
                                                       Object... varargs);

    /**
     * <pre>[dbus/dbus-message.h]
     * dbus_bool_t dbus_message_append_args_valist   (DBusMessage     *message,
     *                                                int              first_arg_type,
     *                                                va_list          var_args);
     </pre>
     */
    @Name("dbus_message_append_args_valist")
    protected static native boolean _messageAppendArgsValist(Pointer<_Message> message,
                                                             int firstArgType,
                                                             Object... varargs);

    /**
     * <pre>[dbus/dbus-message.h]
     * dbus_bool_t dbus_message_get_args             (DBusMessage     *message,
     *                                                DBusError       *error,
     *                                                int              first_arg_type,
     *                                                ...);</pre>
     */
    @Name("dbus_message_get_args")
    protected static native boolean _messageGetArgs(Pointer<_Message> message,
                                                    Pointer<_Error> error,
                                                    int firstArgType,
                                                    Object... varargs);

    /**
     * <pre>[dbus/dbus-message.h]
     * dbus_bool_t dbus_message_get_args_valist      (DBusMessage     *message,
     *                                                DBusError       *error,
     *                                                int              first_arg_type,
     *                                                va_list          var_args);</pre>
     */
    @Name("dbus_message_get_args_valist")
    protected static native boolean _messageGetArgsValist(Pointer<_Message> message,
                                                          Pointer<_Error> error,
                                                          int firstArgType,
                                                          Object... varargs);

    /**
     * <pre>[dbus/dbus-message.h]
     * dbus_bool_t dbus_message_contains_unix_fds    (DBusMessage *message);</pre>
     */
    @Name("dbus_message_contains_unix_fds")
    protected static native boolean _messageContainsUnixFds(Pointer<_Message> message);

    /**
     * <pre>[dbus/dbus-message.h]
     * dbus_bool_t dbus_message_iter_init             (DBusMessage     *message,
     *                                                 DBusMessageIter *iter);</pre>
     */
    @Name("dbus_message_iter_init")
    protected static native boolean _messageIterInit(Pointer<_Message> message,
                                                     Pointer<_MessageIter> _iter);

    /**
     * <pre>[dbus/dbus-message.h]
     * dbus_bool_t dbus_message_iter_has_next         (DBusMessageIter *iter);</pre>
     */
    @Name("dbus_message_iter_has_next")
    protected static native boolean _messageIterHasNext(Pointer<_MessageIter> iter);

    /**
     * <pre>[dbus/dbus-message.h]
     * dbus_bool_t dbus_message_iter_next             (DBusMessageIter *iter);</pre>
     */
    @Name("dbus_message_iter_next")
    protected static native boolean _messageIterNext(Pointer<_MessageIter> iter);

    /**
     * <pre>[dbus/dbus-message.h]
     * char*       dbus_message_iter_get_signature    (DBusMessageIter *iter);</pre>
     */
    @Name("dbus_message_iter_get_signature")
    protected static native Pointer<Byte> _messageIterGetSignature(Pointer<_MessageIter> iter);

    /**
     * <pre>[dbus/dbus-message.h]
     * int         dbus_message_iter_get_arg_type     (DBusMessageIter *iter);</pre>
     */
    @Name("dbus_message_iter_get_arg_type")
    protected static native int _messageIterGetArgType(Pointer<_MessageIter> iter);

    /**
     * <pre>[dbus/dbus-message.h]
     * int         dbus_message_iter_get_element_type (DBusMessageIter *iter);</pre>
     */
    @Name("dbus_message_iter_get_element_type")
    protected static native int _messageIterGetElementType(Pointer<_MessageIter> iter);

    /**
     * <pre>[dbus/dbus-message.h]
     * void        dbus_message_iter_recurse          (DBusMessageIter *iter,
     *                                                 DBusMessageIter *sub);</pre>
     */
    @Name("dbus_message_iter_recurse")
    protected static native void _messageIterRecurse (Pointer<_MessageIter> iter,
                                                      Pointer<_MessageIter> sub);

    /**
     * <pre>[dbus/dbus-message.h]
     * void        dbus_message_iter_get_basic        (DBusMessageIter *iter,
     *                                                 void            *value);</pre>
     */
    @Name("dbus_message_iter_get_basic")
    protected static native void _messageIterGetBasic(Pointer<_MessageIter> iter,
                                                      Pointer<?> value);

    /**
     * <pre>[dbus/dbus-message.h]
     * void        dbus_message_iter_get_fixed_array  (DBusMessageIter *iter,
     *                                                 void            *value,
     *                                                 int             *n_elements);
     </pre>
     */
    @Name("dbus_message_iter_get_fixed_array")
    protected static native void _messageIterGetFixedArray(Pointer<_MessageIter> iter,
                                                           Pointer<?> value,
                                                           Pointer<Integer> nElements);

    /**
     * <pre>[dbus/dbus-message.h]
     * void        dbus_message_iter_init_append        (DBusMessage     *message,
     *                                                   DBusMessageIter *iter);</pre>
     */
    @Name("dbus_message_iter_init_append")
    protected static native void _messageIterInitAppend(Pointer<_Message> message,
                                                        Pointer<_MessageIter> iter);

    /**
     * <pre>[dbus/dbus-message.h]
     * dbus_bool_t dbus_message_iter_append_basic       (DBusMessageIter *iter,
     *                                                   int              type,
     *                                                   const void      *value);</pre>
     */
    @Name("dbus_message_iter_append_basic")
    protected static native boolean _messageIterAppendBasic(Pointer<_MessageIter> iter,
                                                            int type,
                                                            Pointer<?> value);

    /**
     * <pre>[dbus/dbus-message.h]
     * dbus_bool_t dbus_message_iter_append_fixed_array (DBusMessageIter *iter,
     *                                                   int              element_type,
     *                                                   const void      *value,
     *                                                   int              n_elements);</pre>
     */
    @Name("dbus_message_iter_append_fixed_array")
    protected static native boolean _messageIterAppendFixedArray(Pointer<_MessageIter> iter,
                                                                 int elementType,
                                                                 Pointer<?> value,
                                                                 int nElements);

    // ---- dbus/dbus-misc.h -------------------------------------------------------------------------------------------

    /**
     * <pre>[dbus/dbus-misc.h]
     * void       dbus_get_version  (int *major_version_p,
     *                               int *minor_version_p,
     *                               int *micro_version_p);</pre>
     *
     * @param major
     *     Int reference to store the major version.
     * @param minor
     *     Int reference to store the minor version.
     * @param micro
     *     Int reference to store the micro version.
     *
     */
    @Name("dbus_get_version")
    protected static native void _getVersion(Pointer<Integer> major,
                                             Pointer<Integer> minor,
                                             Pointer<Integer> micro);

    /**
     * <pre>[dbus/dbus-misc.h]
     * char*       dbus_get_local_machine_id  (void);</pre>
     *
     * @return
     *     The unique local machine identifier.
     */
    @Name("dbus_get_local_machine_id")
    protected static native Pointer<Byte> _getLocalMachineId();

    // ---- dbus/dbus-pending-call.h -----------------------------------------------------------------------------------

    /**
     * <pre>[dbus/dbus-pending-call.h]
     * DBusPendingCall* dbus_pending_call_ref       (DBusPendingCall               *pending);</pre>
     */
    @Name("dbus_pending_call_ref")
    protected static native Pointer<_PendingCall> _pendingCallRef(Pointer<_PendingCall> pending);

    /**
     * <pre>[dbus/dbus-pending-call.h]
     * void         dbus_pending_call_unref         (DBusPendingCall               *pending);</pre>
     */
    @Name("dbus_pending_call_unref")
    protected static native void _pendingCallUnref(Pointer<_PendingCall> pending);

    /**
     * <pre>[dbus/dbus-pending-call.h]
     * dbus_bool_t  dbus_pending_call_set_notify    (DBusPendingCall               *pending,
     *                                               DBusPendingCallNotifyFunction  function,
     *                                               void                          *user_data,
     *                                               DBusFreeFunction               free_user_data);</pre>
     */
    @Name("dbus_pending_call_set_notify")
    protected static native boolean _pendingCallSetNotify(Pointer<_PendingCall> pending,
                                                          _PendingCallNotifyFunction function,
                                                          Pointer<?> userData,
                                                          _FreeFunction freeUserData);

    /**
     * <pre>[dbus/dbus-pending-call.h]
     * void         dbus_pending_call_cancel        (DBusPendingCall               *pending);</pre>
     */
    @Name("dbus_pending_call_cancel")
    protected static native void _pendingCallCancel(_PendingCall pending);

    /**
     * <pre>[dbus/dbus-pending-call.h]
     * dbus_bool_t  dbus_pending_call_get_completed (DBusPendingCall               *pending);</pre>
     * @return
     */
    @Name("dbus_pending_call_get_completed")
    protected static native void _pendingCallGetCompleted(Pointer<_PendingCall> pending);

    /**
     * <pre>[dbus/dbus-pending-call.h]
     * DBusMessage* dbus_pending_call_steal_reply   (DBusPendingCall               *pending);</pre>
     */
    @Name("dbus_pending_call_steal_reply")
    protected static native Pointer<_Message> _pendingCallStealReply(Pointer<_PendingCall> pending);

    /**
     * <pre>[dbus/dbus-pending-call.h]
     * void         dbus_pending_call_block         (DBusPendingCall               *pending);</pre>
     */
    @Name("dbus_pending_call_block")
    protected static native void _pendingCallBlock(Pointer<_PendingCall> pending);

    /**
     * <pre>[dbus/dbus-pending-call.h]
     * dbus_bool_t dbus_pending_call_allocate_data_slot (dbus_int32_t     *slot_p);</pre>
     */
    @Name("dbus_pending_call_allocate_data_slot")
    protected static native boolean _pendingCallAllocateDataSlot(Pointer<Integer> slotP);

    /**
     * <pre>[dbus/dbus-pending-call.h]
     * void        dbus_pending_call_free_data_slot     (dbus_int32_t     *slot_p);</pre>
     */
    @Name("dbus_pending_call_free_data_slot")
    protected static native void _pendingCallFreeDataSlot(Pointer<Integer> slotP);

    /**
     * <pre>[dbus/dbus-pending-call.h]
     * dbus_bool_t dbus_pending_call_set_data           (DBusPendingCall  *pending,
     *                                                   dbus_int32_t      slot,
     *                                                   void             *data,
     *                                                   DBusFreeFunction  free_data_func);</pre>
     */
    @Name("dbus_pending_call_set_data")
    protected static native boolean _pendingCallSetData(Pointer<_PendingCall> pending,
                                                        int slot,
                                                        Pointer<?> data,
                                                        _FreeFunction freeDataFunc);

    /**
     * <pre>[dbus/dbus-pending-call.h]
     * void*       dbus_pending_call_get_data           (DBusPendingCall  *pending,
     *                                                   dbus_int32_t      slot);</pre>
     */
    @Name("dbus_pending_call_get_data")
    protected static native void _pendingCallGetData(Pointer<_PendingCall> pending,
                                                     int slot);

    // ---- dbus/dbus-signature.h --------------------------------------------------------------------------------------

    /**
     * <pre>[dbus/dbus-signature.h]
     * dbus_bool_t     dbus_type_is_basic                   (int            typecode);</pre>
     */
    @Name("dbus_type_is_basic")
    protected static native boolean _typeIsBasic(int typecode);

    /**
     * <pre>[dbus/dbus-signature.h]
     * dbus_bool_t     dbus_type_is_container               (int            typecode);</pre>
     */
    @Name("dbus_type_is_container")
    protected static native boolean _typeIsContainer(int typecode);

    /**
     * <pre>[dbus/dbus-signature.h]
     * dbus_bool_t     dbus_type_is_fixed                   (int            typecode);</pre>
     */
    @Name("dbus_type_is_fixed")
    protected static native boolean _typeIsFixed(int typecode);


    // ---- dbus/dbus-threads.h ----------------------------------------------------------------------------------------

    /**
     * Initializes threads.
     * <p>If this method is not called, the D-Bus library will not lock any data structures. If it is called, D-Bus will
     * do locking, at some cost in efficiency.
     * Note that this function must be called BEFORE the second thread is started.</p>
     * <p>It's safe to call {@link #_threadsInitDefault()} as many times as you want,
     * but only the first time will have an effect.</p>
     * <p>{@link #_shutdown()} reverses the effects of this function when it resets all global state in libdbus.</p>
     *
     * <pre>[dbus/dbus-threads.h]
     * dbus_bool_t  dbus_threads_init_default (void);</pre>
     *
     * @return
     *         {@code true} on success, {@code false} if not enough memory.
     */
    @Name("dbus_threads_init_default")
    protected static native boolean _threadsInitDefault();


    // =================================================================================================================
    // ---- Structures -------------------------------------------------------------------------------------------------
    // =================================================================================================================

    @Name("DBusAddressEntry")
    @Struct
    public static final class _AddressEntry extends StructObject {
        public _AddressEntry() {
            super();
        }
        public _AddressEntry(Pointer<_AddressEntry> peer) {
            super(peer);
        }
        Pointer<_AddressEntry> _getPeer() {
            return (Pointer<_AddressEntry>) this.peer;
        }
    }

    @Name("DBusConnection")
    @Struct
    public static final class _Connection extends StructObject {

        public _Connection() {
            super();
        }

        public _Connection(Pointer<_Connection> peer) {
            super(peer);
        }

        Pointer<_Connection> _getPeer() {
            return (Pointer<_Connection>) this.peer;
        }

    }

    @Name("DBusError")
    @Struct(fieldCount = 8)
    public static final class _Error extends StructObject {

        @Field(0) @Name("name")
        public Pointer<Byte> _name() { return this.io.getPointerField(this, 0); }
        @Field(0) @Name("name")
        public _Error _name(final Pointer<Byte> name) { this.io.setPointerField(this, 0, name); return this; }

        @Field(1) @Name("message")
        public Pointer<Byte> _message() { return this.io.getPointerField(this, 1); };
        @Field(1) @Name("message")
        public _Error _message(final Pointer<Byte> message) { this.io.setPointerField(this, 1, message); return this; }

        @Field(2) @Bits(1) @Name("dummy1")
        public int _dummy1() { return this.io.getIntField(this, 2); };
        @Field(2) @Bits(1) @Name("dummy1")
        public _Error _dummy1(final int dummy1) { this.io.setIntField(this, 2, dummy1); return this; };

        @Field(3) @Bits(1) @Name("dummy2")
        public int _dummy2() { return this.io.getIntField(this, 3); };
        @Field(3) @Bits(1) @Name("dummy2")
        public _Error _dummy2(final int dummy2) { this.io.setIntField(this, 3, dummy2); return this; };

        @Field(4) @Bits(1) @Name("dummy3")
        public int _dummy3() { return this.io.getIntField(this, 4); };
        @Field(4) @Bits(1) @Name("dummy3")
        public _Error _dummy3(final int dummy3) { this.io.setIntField(this, 4, dummy3); return this; };

        @Field(5) @Bits(1) @Name("dummy4")
        public int _dummy4() { return this.io.getIntField(this, 5); };
        @Field(5) @Bits(1) @Name("dummy4")
        public _Error _dummy4(final int dummy4) { this.io.setIntField(this, 5, dummy4); return this; };

        @Field(6) @Bits(1) @Name("dummy5")
        public int _dummy5() { return this.io.getIntField(this, 6); };
        @Field(6) @Bits(1) @Name("dummy5")
        public _Error _dummy5(final int dummy5) { this.io.setIntField(this, 6, dummy5); return this; };

        @Field(7) @Name("padding1")
        public Pointer<?> _padding1() { return this.io.getPointerField(this, 7); };
        @Field(7) @Name("padding1")
        public _Error _padding1(final Pointer<?> ptr) { this.io.setPointerField(this, 7, ptr); return this; };

        public _Error() {
            super();
        }

        public _Error(Pointer<_Error> pointer) {
            super(pointer);
        }

        Pointer<_Error> _getPeer() {
            return (Pointer<_Error>) this.peer;
        }

    }

    @Struct
    @Name("DBusMessage")
    public static final class _Message extends StructObject
            implements Cloneable {

        public _Message() {
            super();
        }

        public _Message(Pointer<_Message> peer) {
            super(peer);
        }

        Pointer<_Message> _getPeer() {
            return (Pointer<_Message>) this.peer;
        }

        @Override
        public _Message clone() {
            return DBus._messageCopy((Pointer<_Message>) this.peer).as(_Message.class).get();
        }

    }

    @Struct(fieldCount = 14)
    @Name("DBusMessageIter")
    public static final class _MessageIter extends StructObject {

        @Field(0) @Name("dummy1")
        public Pointer<?> _dummy1() { return this.io.getPointerField(this, 0); }
        @Field(0) @Name("dummy1")
        public void _dummy1(final Pointer<?> dummy1) { this.io.setPointerField(this, 0, dummy1); }

        @Field(1) @Name("dummy2")
        public Pointer<?> _dummy2() { return this.io.getPointerField(this, 1); }
        @Field(1) @Name("dummy2")
        public void _dummy2(final Pointer<?> dummy2) { this.io.setPointerField(this, 1, dummy2); }

        @Field(2) @Name("dummy3")
        public int _dummy3() { return this.io.getIntField(this, 2); }
        @Field(2) @Name("dummy3")
        public void _dummy3(final int dummy3) { this.io.setIntField(this, 2, dummy3); }

        @Field(3) @Name("dummy4")
        public int _dummy4() { return this.io.getIntField(this, 3); }
        @Field(3) @Name("dummy4")
        public void _dummy4(final int dummy4) { this.io.setIntField(this, 3, dummy4); }

        @Field(4) @Name("dummy5")
        public int _dummy5() { return this.io.getIntField(this, 4); }
        @Field(4) @Name("dummy5")
        public void _dummy5(final int dummy5) { this.io.setIntField(this, 4, dummy5); }

        @Field(5) @Name("dummy6")
        public int _dummy6() { return this.io.getIntField(this, 5); }
        @Field(5) @Name("dummy6")
        public void _dummy6(final int dummy6) { this.io.setIntField(this, 5, dummy6); }

        @Field(6) @Name("dummy7")
        public int _dummy7() { return this.io.getIntField(this, 6); }
        @Field(6) @Name("dummy7")
        public void _dummy7(final int dummy7) { this.io.setIntField(this, 6, dummy7); }

        @Field(7) @Name("dummy8")
        public int _dummy8() { return this.io.getIntField(this, 7); }
        @Field(7) @Name("dummy8")
        public void _dummy8(final int dummy8) { this.io.setIntField(this, 7, dummy8); }

        @Field(8) @Name("dummy9")
        public int _dummy9() { return this.io.getIntField(this, 8); }
        @Field(8) @Name("dummy9")
        public void _dummy9(final int dummy9) { this.io.setIntField(this, 8, dummy9); }

        @Field(9) @Name("dummy10")
        public int _dummy10() { return this.io.getIntField(this, 9); }
        @Field(9) @Name("dummy10")
        public void _dummy10(final int dummy10) { this.io.setIntField(this, 9, dummy10); }

        @Field(10) @Name("dummy11")
        public int _dummy11() { return this.io.getIntField(this, 10); }
        @Field(10) @Name("dummy11")
        public void _dummy11(final int dummy11) { this.io.setIntField(this, 10, dummy11); }

        @Field(11) @Name("pad1")
        public int _pad1() { return this.io.getIntField(this, 11); }
        @Field(11) @Name("pad1")
        public void _pad1(final int pad1) { this.io.setIntField(this, 11, pad1); }

        @Field(12) @Name("pad2")
        public int _pad2() { return this.io.getIntField(this, 12); }
        @Field(12) @Name("pad2")
        public void _pad2(final int pad2) { this.io.setIntField(this, 12, pad2); }

        @Field(13) @Name("pad3")
        public Pointer<?> _pad3() { return this.io.getPointerField(this, 13); }
        @Field(13) @Name("pad3")
        public void _pad3(final Pointer<?> ptr) { this.io.setPointerField(this, 13, ptr); }

        public _MessageIter() {
            super();
        }

        public _MessageIter(Pointer<_MessageIter> peer) {
            super(peer);
        }

        Pointer<_MessageIter> _getPeer() {
            return (Pointer<_MessageIter>) this.peer;
        }

    }

    @Struct(size = 6)
    @Name("DBusObjectPathVTable")
    public static final class _ObjectPathVTable extends StructObject {

        @Field(0) @Name("unregister_function")
        public Pointer<_ObjectPathUnregisterFunction> _unregisterFunction() { return this.io.getPointerField(this, 0); }
        @Field(0) @Name("unregister_function")
        public _ObjectPathVTable unregisterFunction(final Pointer<_ObjectPathUnregisterFunction> unregisterFunction) { this.io.setPointerField(this, 0, unregisterFunction); return this;}

        @Field(1) @Name("message_function")
        public Pointer<_ObjectPathMessageFunction> _messageFunction() { return this.io.getPointerField(this, 1); }
        @Field(1) @Name("message_function")
        public _ObjectPathVTable messageFunction(final Pointer<_ObjectPathMessageFunction> messageFunction) { this.io.setPointerField(this, 1, messageFunction); return this;}

        @Field(2) @Name("dbus_internal_pad1")
        public Pointer<Callback<?>> _internal_pad1() { return this.io.getPointerField(this, 2); }
        @Field(2) @Name("dbus_internal_pad1")
        public void _internalPad1(final Pointer<Callback<?>> ptr) { this.io.setPointerField(this, 2, ptr); }

        @Field(3) @Name("dbus_internal_pad2")
        public Pointer<Callback<?>> _internal_pad2() { return this.io.getPointerField(this, 3); }
        @Field(3) @Name("dbus_internal_pad2")
        public void _internalPad2(final Pointer<Callback<?>> ptr) { this.io.setPointerField(this, 3, ptr); }

        @Field(4) @Name("dbus_internal_pad3")
        public Pointer<Callback<?>> _internal_pad3() { return this.io.getPointerField(this, 4); }
        @Field(4) @Name("dbus_internal_pad3")
        public void _internalPad3(final Pointer<Callback<?>> ptr) { this.io.setPointerField(this, 4, ptr); }

        @Field(5) @Name("dbus_internal_pad4")
        public Pointer<Callback<?>> _internal_pad4() { return this.io.getPointerField(this, 5); }
        @Field(5) @Name("dbus_internal_pad4")
        public void _internalPad4(final Pointer<Callback<?>> ptr) { this.io.setPointerField(this, 5, ptr); }

        public _ObjectPathVTable() {
            super();
        }
        public _ObjectPathVTable(Pointer<_ObjectPathVTable> peer) {
            super(peer);
        }
        Pointer<_ObjectPathVTable> _getPeer() {
            return (Pointer<_ObjectPathVTable>) this.peer;
        }
    }

    @Struct
    @Name("DBusPendingCall")
    public static final class _PendingCall extends StructObject {
        public _PendingCall() {
            super();
        }
        public _PendingCall(Pointer<_PendingCall> peer) {
            super(peer);
        }
        Pointer<_PendingCall> _getPeer() {
            return (Pointer<_PendingCall>) this.peer;
        }
    }

    @Struct
    @Name("DBusPreallocatedSend")
    public static final class _PreallocatedSend extends StructObject {
        public _PreallocatedSend() {
            super();
        }
        public _PreallocatedSend(Pointer<_PreallocatedSend> peer) {
            super(peer);
        }
        Pointer<_PreallocatedSend> _getPeer() {
            return (Pointer<_PreallocatedSend>) this.peer;
        }
    }

    @Struct
    @Name("DBusTimeout")
    public static final class _Timeout extends StructObject {
        public _Timeout() {
            super();
        }
        public _Timeout(Pointer<_Timeout> peer) {
            super(peer);
        }
        Pointer<_Timeout> _getPeer() {
            return (Pointer<_Timeout>) this.peer;
        }
    }

    @Struct
    @Name("DBusWatch")
    public static final class _Watch extends StructObject {
        public _Watch() {
            super();
        }
        public _Watch(Pointer<_Watch> peer) {
            super(peer);
        }
        Pointer<_Watch> _getPeer() {
            return (Pointer<_Watch>) this.peer;
        }
    }

    // =================================================================================================================
    // ---- Callbacks / function pointers ------------------------------------------------------------------------------
    // =================================================================================================================

    /**
     * Called when libdbus needs a new watch to be monitored by the main
     * loop. Returns {@code false} if it lacks enough memory to add the
     * watch. Set by {@link #_connectionSetWatchFunctions()} or
     * {@link #_serverSetWatchFunctions()}.
     *
     * <pre>[dbus/dbus-connection.h]
     * typedef dbus_bool_t (* DBusAddWatchFunction)       (DBusWatch      *watch,
     *                                                     void           *data);</pre>
     */
    @Name("DBusAddWatchFunction")
    public static abstract class _AddWatchFunction extends Callback<_AddWatchFunction> {
        public abstract boolean run(Pointer<_Watch> watch, Pointer<?> data);
    }

    /**
     * Called when a message is sent to a registered object path. Found in
     * #DBusObjectPathVTable which is registered with dbus_connection_register_object_path()
     * or dbus_connection_register_fallback().
     * <pre>[dbus/dbus-connection-h]
     * typedef DBusHandlerResult (* DBusObjectPathMessageFunction)    (DBusConnection  *connection,
     *                                                                 DBusMessage     *message,
     *                                                                 void            *user_data);</pre>
     */
    @Name("DBusObjectPathMessageFunction")
    public static abstract class _ObjectPathMessageFunction extends Callback<_ObjectPathMessageFunction> {
        public abstract Pointer<HandlerResult> run(Pointer<_Connection> connection,
                                                   Pointer<_Message> message,
                                                   Pointer<?> userData);
    }


    /**
     * Called when a #DBusObjectPathVTable is unregistered (or its connection is freed).
     * Found in #DBusObjectPathVTable.
     * <pre>[dbus/dbus-connection.h]
     * typedef void              (* DBusObjectPathUnregisterFunction) (DBusConnection  *connection,
     *                                                                 void            *user_data);</pre>
     */
    @Name("DBusObjectPathUnregisterFunction")
    public static abstract class _ObjectPathUnregisterFunction extends Callback<_ObjectPathUnregisterFunction> {
        public abstract void run(Pointer<_Connection> connection,
                                 Pointer<?> userData);
    }

    /**
     * Called when {@link #_watchGetEnabled()} may return a different value
     * than it did before.  Set by {@link #_connectionSetWatchFunctions()}
     * or {@link #_serverSetWatchFunctions()}.
     *
     * <pre>[dbus/dbus-connection.h]
     * typedef void        (* DBusWatchToggledFunction)   (DBusWatch      *watch,
     *                                                     void           *data);</pre>
     */
    @Name("DBusWatchToggledFunction")
    public static abstract class _WatchToggledFunction extends Callback<_WatchToggledFunction> {
        public abstract void run(Pointer<_Watch> watch, Pointer<?> data);
    }

    /**
     * Called when libdbus no longer needs a watch to be monitored by the
     * main loop. Set by {@link #_connectionSetWatchFunctions()} or
     * {@link #_server_set_watch_functions()}.
     * <pre>[dbus/dbus-connection.h]
     * typedef void        (* DBusRemoveWatchFunction)    (DBusWatch      *watch,
     *                                                     void           *data);</pre>
     */
    @Name("DBusRemoveWatchFunction")
    public static abstract class _RemoveWatchFunction extends Callback<_RemoveWatchFunction> {
        public abstract void run(Pointer<_Watch> watch, Pointer<?> data);
    }

    /**
     * Called when libdbus needs a new timeout to be monitored by the main
     * loop. Returns #FALSE if it lacks enough memory to add the
     * watch. Set by dbus_connection_set_timeout_functions() or
     * dbus_server_set_timeout_functions().
     * <pre>[dbus/dbus-connection-h]
     * typedef dbus_bool_t (* DBusAddTimeoutFunction)     (DBusTimeout    *timeout,
     *                                                     void           *data);</pre>
     */
    @Name("DBusAddTimeoutFunction")
    public static abstract class _AddTimeoutFunction extends Callback<_AddTimeoutFunction> {
        public abstract void run(Pointer<_Timeout> timeout,
                                 Pointer<?> data);
    }

    /**
     * Called when {@link #_timeout_get_enabled()} may return a different
     * value than it did before.
     * <p>Set by {@link #_connectionSetTimeoutFunctions()} or
     * {@link #_serverSetTimeoutFunctions()}.</p>
     * <pre>[dbus/dbus-connection.h]
     * typedef void        (* DBusTimeoutToggledFunction) (DBusTimeout    *timeout,
     *                                                     void           *data);</pre>
     */
    @Name("DBusTimeoutToggledfunction")
    public static abstract class _TimeoutToggledFunction extends Callback<_TimeoutToggledFunction> {
        public abstract void run(Pointer<_Timeout> timeout,
                                 Pointer<?> data);
    }

    /**
     * Called when libdbus no longer needs a timeout to be monitored by the
     * main loop.
     * <p>Set by {@link #_dbusConnectionSetTimeoutFunctions()} or
     * {link #_serverSetTimeoutFunctions()}.</p>
     * <pre>[dbus/dbus-connection.h]
     * typedef void        (* DBusRemoveTimeoutFunction)  (DBusTimeout    *timeout,
     *                                                     void           *data);</pre>
     */
    @Name("DBusRemoveTimeoutFunction")
    public static abstract class _RemoveTimeoutFunction extends Callback<_RemoveTimeoutFunction> {
        public abstract void run(Pointer<_Timeout> timeout,
                                 Pointer<?> data);
    }

    /**
     * Called when the return value of dbus_connection_get_dispatch_status()
     * may have changed. Set with dbus_connection_set_dispatch_status_function().
     * <pre>[dbus/dbus-connection-h]
     * typedef void        (* DBusDispatchStatusFunction) (DBusConnection *connection,
     *                                                     DBusDispatchStatus new_status,
     *                                                     void           *data);</pre>
     */
    @Name("DBusDispatchStatusFunction")
    public static abstract class _DispatchStatusFunction extends Callback<_DispatchStatusFunction> {
        public abstract void run(Pointer<_Connection> connection,
                                 DispatchStatus newStatus,
                                 Pointer<?> data);
    }

    /**
     * Called when the main loop's thread should be notified that there's now work
     * to do. Set with dbus_connection_set_wakeup_main_function().
     * <pre>[dbus/dbus-connection.h]
     * typedef void        (* DBusWakeupMainFunction)     (void           *data);</pre>
     */
    @Name("DBusWakeupMainFunction")
    public static abstract class _WakeupMainFunction extends Callback<_WakeupMainFunction> {
        public abstract void run(Pointer<?> data);
    }

    /**
     * Called during authentication to check whether the given UNIX user
     * ID is allowed to connect, if the client tried to auth as a UNIX
     * user ID. Normally on Windows this would never happen. Set with
     * dbus_connection_set_unix_user_function().
     * <pre>[dbus/dbus-connection.h]
     * typedef dbus_bool_t (* DBusAllowUnixUserFunction)  (DBusConnection *connection,
     *                                                     unsigned long   uid,
     *                                                     void           *data);</pre>
     */
    @Name("DBusAllowUnixUserFunction")
    public static abstract class _AllowUnixUserFunction extends Callback<_AllowUnixUserFunction> {
        public abstract boolean run(Pointer<_Connection> connection,
                                    long uid,
                                    Pointer<?> data);
    }

    /**
     * Called during authentication to check whether the given Windows user
     * ID is allowed to connect, if the client tried to auth as a Windows
     * user ID. Normally on UNIX this would never happen. Set with
     * dbus_connection_set_windows_user_function().
     * <pre>[dbus/dbus-connection.h]
     * typedef dbus_bool_t (* DBusAllowWindowsUserFunction)  (DBusConnection *connection,
     *                                                        const char     *user_sid,
     *                                                        void           *data);</pre>
     */
    @Name("DBusAllowWindowsUserFunction")
    public static abstract class _AllowWindowsUserFunction extends Callback<_AllowWindowsUserFunction> {
        public abstract boolean run(Pointer<_Connection> connection,
                                    Pointer<Byte> userSid,
                                    Pointer<?> data);
    }

    /**
     * <pre>[dbus/dbus-memory.h]
     * typedef void (* DBusFreeFunction) (void *memory);</pre>
     */
    @Name("DBusFreeFunction")
    public static abstract class _FreeFunction extends Callback<_FreeFunction> {
        public abstract void run(Pointer<?> memory);
    }

    /**
     * Called when a pending call now has a reply available. Set with
     * dbus_pending_call_set_notify().
     * <pre>[dbus/dbus-connection.h]
     * typedef void (* DBusPendingCallNotifyFunction) (DBusPendingCall *pending,
     *                                                 void            *user_data);</pre>
     */
    @Name("DBusPendingCallNotifyFunction")
    public static abstract class _PendingCallNotifyFunction extends Callback<_PendingCallNotifyFunction> {
         public abstract void run(Pointer<_PendingCall> pending,
                                  Pointer<?> userData);
    }

    /**
     * Called when a message needs to be handled. The result indicates whether or
     * not more handlers should be run. Set with dbus_connection_add_filter().
     * <pre>[dbus/dbus-connection.h]
     * typedef DBusHandlerResult (* DBusHandleMessageFunction) (DBusConnection     *connection,
     *                                                          DBusMessage        *message,
     *                                                          void               *user_data);</pre>
     */
    @Name("DBusHandleMessageFunction")
    public static abstract class _HandleMessageFunction extends Callback<_HandleMessageFunction> {
        public abstract HandlerResult run(Pointer<_Connection> connection,
                                           Pointer<_Message> message,
                                           Pointer<?> userData);
    }





    // =================================================================================================================
    // ---- Wrapper methods to make functions defined further above better usable --------------------------------------
    // =================================================================================================================


    public static void initialize() {
        _threadsInitDefault();
    }

    public static void shutdown() {
        _shutdown();
    }

    /**
     * Returns the (unique) identifier of the local machine as reported
     * by the wrapped native D-Bus library.
     * @return
     *     The unique local machine identifier.
     */
    public static String getLocalMachineId() {
        return _getLocalMachineId().getCString();
    }

}
