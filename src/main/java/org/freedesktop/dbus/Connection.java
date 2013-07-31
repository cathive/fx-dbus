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
import org.freedesktop.dbus.DBus._Connection;
import org.freedesktop.dbus.DBus._Error;

import java.util.HashMap;
import java.util.Map;

import static org.bridj.Pointer.pointerToCString;
import static org.bridj.Pointer.pointerToPointer;
import static org.freedesktop.dbus.DBus._Message;
import static org.freedesktop.dbus.DBus._PendingCall;

/**
 * @author Benjamin P. Jung
 */
public class Connection implements AutoCloseable {

    @Internal
    final Pointer<_Connection> _peer;

    @Internal
    private Connection(final _Connection _connection) {
        super();
        this._peer = _connection._getPeer();
    }


    /**
     * Fetches a D-Bus connection instance.
     * @param type
     *         Type of the connection to be fetched.
     * @param _private
     *         {@code} true if a private connection shall be fetched, {@code false} otherwise.
     * @return
     *         A connection to the bus with the designated type.
     */
    public static Connection getConnection(final BusType type, boolean _private) {
        final Pointer<_Error> _error = DBus._new(_Error.class, 1);
        DBus._errorInit(_error);
        final Pointer<_Connection> _connection = _private ? DBus._busGetPrivate(type, _error) : DBus._busGet(type, _error);
        if (DBus._errorIsSet(_error)) {
            final DBusException exception = new DBusException(_error.get());
            DBus._errorFree(_error);
            throw exception;
        }
        return new Connection(_connection.as(_Connection.class).get());
    }


    /**
     * Asks the bus to return its globally unique ID, as described in the D-Bus specification.
     * <p>For the session bus, this is useful as a way to uniquely identify each user session.
     * For the system bus, probably the bus ID is not useful; instead, use the machine ID since it's accessible without
     * necessarily connecting to the bus and may be persistent beyond a single bus instance
     * (across reboots for example). See {@link org.freedesktop.dbus.DBus#getLocalMachineId()}.</p>
     * <p>In addition to an ID for each bus and an ID for each machine, there is an ID for each address that the bus is
     * listening on; that can be retrieved with {@link #getServerId()}, though it is probably not very useful.
     * @return
     *         The bus ID.
     */
    public String getBusId() {
        final Pointer<_Error> _error = DBus._new(_Error.class, 1);
        DBus._errorInit(_error);
        final Pointer<Byte> _id = DBus._busGetId(this._peer, _error);
        if (DBus._errorIsSet(_error)) {
            final DBusException exception = new DBusException(_error.get());
            DBus._errorFree(_error);
            throw exception;
        }
        return _id.getCString();
    }

    /**
     * Returns the unique name of this connection.
     * @return
     *         The unique name of this connection.
     */
    public String getUniqueName() {
        return DBus._busGetUniqueName(this._peer).getCString();
    }

    public boolean isConnected() {
        return DBus._connectionGetIsConnected(_peer);
    }

    public boolean isAuthenticated() {
        return DBus._connectionGetIsAuthenticated(_peer);
    }

    public boolean isAnonymous() {
        return DBus._connectionGetIsAnonymous(_peer);
    }

    public String getServerId() {
        final Pointer<Byte> _serverId = DBus._connectionGetServerId(_peer);
        return _serverId == Pointer.NULL ? null : _serverId.getCString();
    }

    public boolean canSendType(final Message.Type type) {
        return DBus._connectionCanSendType(_peer, type.value());
    }

    /**
     * Asks the bus to assign the given name to this connection by invoking the RequestName method on the bus.
     *
     * @param name
     *         The name to request.
     * @param flags
     *         Flags.
     * @return
     *         A result code.
     */
    public RequestNameReply requestName(final String name, final NameFlag... flags) {

        // Prepare parameters
        final Pointer<_Connection> _this = this._peer;
        final Pointer<Byte> _name = pointerToCString(name);
        int _flags = 0;
        for (final NameFlag flag: flags) {
            _flags |= flag.value();
        }
        final Pointer<_Error> _error = DBus._new(_Error.class, 1);
        DBus._errorInit(_error);

        // Perform call
        final int reply = DBus._busRequestName(_this, _name, _flags, _error);
        if (reply == -1 || DBus._errorIsSet(_error)) {
            final DBusException exception = new DBusException(_error.get());
            DBus._errorFree(_error);
            throw exception;
        }

        // Transform and return result
        return RequestNameReply.valueOf(reply);

    }

    public ReleaseNameReply releaseName(final String uniqueName) {

        // Prepare parameters
        final Pointer<_Connection> _this = this._peer;
        final Pointer<Byte> _name = pointerToCString(uniqueName);
        final Pointer<_Error> _error = DBus._new(_Error.class, 1);
        DBus._errorInit(_error);

        // Perform call
        final int reply = DBus._busReleaseName(_this, _name, _error);
        if (reply == -1 || DBus._errorIsSet(_error)) {
            final DBusException exception = new DBusException(_error.get());
            DBus._errorFree(_error);
            throw exception;
        }

        // Transform and return result
        return ReleaseNameReply.valueOf(reply);

    }

    public long getUnixUserId(final String name) {
        final Pointer<_Error> _error = DBus._new(_Error.class, 1);
        DBus._errorInit(_error);
        final Pointer<Byte> _name = pointerToCString(name);
        final long unixUserId = DBus._busGetUnixUser(this._peer, _name, _error);
        if (DBus._errorIsSet(_error)) {
            final DBusException exception = new DBusException(_error.get());
            DBus._errorFree(_error);
            throw exception;
        }
        return unixUserId;
    }

    public long send(final Message message) {
        final Pointer<Long> _clientSerial = DBus._new0(long.class, 1);
        if (!DBus._connectionSend(this._peer, message._peer, _clientSerial)) {
            throw new DBusException("Sending of D-Bus message failed.");
        }
        final Long clientSerial = _clientSerial.getCLong();
        DBus._free(_clientSerial);
        return clientSerial.longValue(); // No NPE possible, because possible errors handled above.
    }

    public Message sendWithReply(final Message message, int timeout) {

        final Pointer<_Connection> _connection = this._peer;
        final Pointer<_Message> _message = message._peer;
        Pointer<Pointer<_PendingCall>> _pending = pointerToPointer(Pointer.NULL);

        if (!DBus._connectionSendWithReply(_connection, _message, _pending, timeout)) {
            throw new DBusException("Sending of D-Bus message failed.");
        }
        DBus._connectionFlush(_connection);

        DBus._pendingCallBlock(_pending.get());
        final Pointer<_Message> _reply = DBus._pendingCallStealReply(_pending.get());
        DBus._pendingCallUnref(_pending.get());
        return new Message(_reply);
    }

    public Message sendWithReply(final Message message) {
        return sendWithReply(message, DBus.TIMEOUT_USE_DEFAULT);
    }


    @Override
    public String toString() {
        return String.format("D-Bus Connection (%s)", this.getUniqueName());
    }

    @Override
    public void finalize() throws Throwable {
        DBus._connectionUnref(this._peer);
        super.finalize();
    }

    @Override
    public void close() {
        if (this._peer != Pointer.NULL) {
            DBus._connectionClose(this._peer);
        }
    }


    /**
     * Flags that can be passed when invoking
     * {@link Connection#requestName(String, Connection.NameFlag...)}
     */
    public static enum NameFlag {

        /*
         * Allow another service to become the primary owner if requested
         */
        ALLOW_REPLACEMENT(DBus.NAME_FLAG_ALLOW_REPLACEMENT),

        /**
         * Request to replace the current primary owner
         */
        REPLACE_EXISTING(DBus.NAME_FLAG_REPLACE_EXISTING),

        /**
         * If we can not become the primary owner do not place us in the queue
         */
        DO_NOT_QUEUE(DBus.NAME_FLAG_DO_NOT_QUEUE);

        private final int value;
        private int value() { return this.value; }
        private NameFlag(final int value) {
            this.value = value;
        }

    }

    /**
     * Possible return codes when invoking
     * {@link Connection#requestName(String, Connection.NameFlag...)}
     */
    public static enum RequestNameReply {

        /**
         * Service has become the primary owner of the requested name
         */
        PRIMARY_OWNER(DBus.REQUEST_NAME_REPLY_PRIMARY_OWNER),

        /**
         * Service could not become the primary owner and has been placed in the queue
         */
        IN_QUEUE(DBus.REQUEST_NAME_REPLY_IN_QUEUE),

        /**
         * Service is already in the queue
         */
        EXISTS(DBus.REQUEST_NAME_REPLY_EXISTS),

        /**
         * Service is already the primary owner
         */
        ALREADY_OWNER(DBus.REQUEST_NAME_REPLY_ALREADY_OWNER);

        private static final Map<Integer, RequestNameReply> INT_MAPPING = new HashMap<>();
        static {
            for (RequestNameReply rnr: RequestNameReply.values()) {
                INT_MAPPING.put(Integer.valueOf(rnr.value()), rnr);
            }
        }
        private static final RequestNameReply valueOf(int value) {
            return INT_MAPPING.get(Integer.valueOf(value));
        }

        private final int value;
        private int value() { return this.value; }
        private RequestNameReply(final int value) {
            this.value = value;
        }

    }

    /**
     * Possible return codes when invoking
     * {@link Connection#releaseName(String)}
     */
    public static enum ReleaseNameReply {

        /**
         * Service was released from the given name
         */
        RELEASED(DBus.RELEASE_NAME_REPLY_RELEASED),

        /**
         * The given name does not exist on the bus
         */
        NON_EXISTENT (DBus.RELEASE_NAME_REPLY_NON_EXISTENT),

        /**
         * Service is not an owner of the given name
         */
        NOT_OWNER(DBus.RELEASE_NAME_REPLY_NOT_OWNER);

        private static final Map<Integer, ReleaseNameReply> INT_MAPPING = new HashMap<>();
        static {
            for (ReleaseNameReply rnr: ReleaseNameReply.values()) {
                INT_MAPPING.put(Integer.valueOf(rnr.value()), rnr);
            }
        }
        private static final ReleaseNameReply valueOf(int value) {
            return INT_MAPPING.get(Integer.valueOf(value));
        }

        private final int value;
        private int value() { return this.value; }
        private ReleaseNameReply(final int value) {
            this.value = value;
        }

    }

}
