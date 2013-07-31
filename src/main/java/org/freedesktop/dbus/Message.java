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
import org.freedesktop.dbus.DBus._Message;
import org.freedesktop.dbus.DBus._MessageIter;

import java.util.*;

import static org.bridj.Pointer.*;

/**
 * @author Benjamin P. Jung
 */
public final class Message implements Iterable<Message.MethodArgument<?>> {

    @Internal
    final Pointer<_Message> _peer;

    @Internal
    Message(final Pointer<_Message> _message) {
        super();
        this._peer = _message;
    }

    @Internal
    Message(final _Message _message) {
        super();
        this._peer = _message._getPeer();
    }


    /**
     * Constructs a new message of the given message type.
     * <p>Types include {@link Type#METHOD_CALL}, {@link Type#SIGNAL}, and so forth.</p>
     * <p>Usually you want to use {@link #newMethodCall(String, String, String, String) newMethodCall(..)},
     * {@link #newMethodReturn(Message) newMessageReturn(..)},
     * {@link #newSignal(String, String, String) newSignal(..)},
     * {@link #newError(Message, String, String) newError(..)} instead.
     * @param type
     *         Type of message.
     * @return
     *         New message or {@code null} if no memory.
     */
    public static Message newMessage(final Type type) {
        return new Message(DBus._messageNew(Type.fromEnum(type)).as(_Message.class).get());
    }

    /**
     * Constructs a new message to invoke a method on a remote object.
     *
     * <p>Returns {@code null} if memory can't be allocated for the message.
     * The destination may be {@code null} in which case no destination is set;
     * this is appropriate when using D-Bus in a peer-to-peer context (no message bus).</p>
     * <p></p>The interface may be {@code null}, which means that if multiple methods with the given name exist it is
     * undefined which one will be invoked.</p>
     * <p>The path and method names may not be {@code null}.</p>
     * <p>Destination, path, interface, and method name can't contain any invalid characters
     * (see the D-Bus specification).</p>
     *
     * @param destination
     *         Name that the message should be sent to or {@code null}.
     * @param path
     *         Object path the message should be sent to.
     * @param _interface
     *         interface to invoke method on, or {@code null}.
     * @param method
     *         Method to invoke.
     * @return
     *         A new {@link #Message}.
     */
    public static Message newMethodCall(String destination, String path, String _interface, String method) {
        final Pointer<_Message> _message = DBus._messageNewMethodCall(
                pointerToCString(destination),
                pointerToCString(path),
                pointerToCString(_interface),
                pointerToCString(method));
        if (_message == null) {
            throw new DBusException("Unable to create method call.");
        }
        final Message message = new Message(_message.as(_Message.class).get());
        return message;
    }

    public static Message newMethodReturn(final Message message) {
        return new Message(DBus._messageNewMethodReturn(message._peer).as(_Message.class).get());
    }

    public static Message newSignal(final String path, final String _interface, final String name) {
        final Message message = new Message(DBus._messageNewSignal(
                pointerToCString(path),
                pointerToCString(_interface),
                pointerToCString(name)
        ).as(_Message.class).get());
        return message;
    }

    public static Message newError(final Message replyTo, final String errorName, final String errorMessage) {
        final Message message = new Message(DBus._messageNewError(
                replyTo._peer,
                pointerToCString(errorName),
                pointerToCString(errorMessage)
        ).as(_Message.class).get());
        return message;
    }

    /**
     * Gets the type of a message.
     * <p>Types include {@link Type#METHOD_CALL}, {@link Type#METHOD_RETURN}, {@link Type#ERROR},
     * {@link Type#SIGNAL}.</p>
     * @return
     */
    public Type getType() {
        return Type.fromValue(DBus._messageGetType(this._peer));
    }

    /**
     * Returns {@code true} if this is an error message.
     * @return
     *         {@code true} if this is an error message.
     */
    public boolean isError() {
        return DBus._messageGetType(this._peer) == Type.ERROR.value();
    }

    /**
     * Sets the object path this message is being sent to (for {@link Type#METHOD_CALL}) or the one a signal is being
     * emitted from (for {@link Type#SIGNAL}).
     * <p>The path must contain only valid characters as defined in the D-Bus specification.</p>
     *
     * @param path
     *         The path or {@code null} to unset.
     * @return
     *         {@code false} if not enough memory.
     */
    public boolean setPath(final String path) {
        return DBus._messageSetPath(this._peer, pointerToCString(path));
    }

    /**
     * Gets the object path this message is being sent to (for {@link Type#METHOD_CALL}) or being emitted from
     * (for {@link Type#SIGNAL}).
     * <p>Returns {@code null} if none.</p>
     * <p>The returned String becomes invalid if the message is modified, since it points into the wire-marshaled
     * message data.</p>
     * @see DBus#_messageGetPathDecomposed(org.bridj.Pointer, org.bridj.Pointer).
     * @return
     */
    public String getPath() {
        return DBus._messageGetPath(this._peer).getCString();
    }

    /**
     * Checks if the message has a particular object path.
     * <p>The object path is the destination object for a method call or the emitting object for a signal.</p>
     *
     * @param path
     *         The path name.
     * @return
     *         {@code true} if there is a path field in the header, {@code false} otherwise.
     */
    public boolean hasPath(final String path) {
        return DBus._messageHasPath(this._peer, pointerToCString(path));
    }

    public boolean setInterface(final String _interface) {
        return DBus._messageSetInterface(this._peer, pointerToCString(_interface));
    }

    public String getInterface() {
        return DBus._messageGetInterface(this._peer).getCString();
    }
    public boolean hasInterface(final String _interface) {
        return DBus._messageHasInterface(this._peer, pointerToCString(_interface));
    }

    public boolean setMember(final String member) {
        return DBus._messageSetMember(this._peer, pointerToCString(member));
    }

    public String getMember() {
        final Pointer<Byte> _member = DBus._messageGetMember(this._peer);
        return _member == Pointer.NULL ? null: _member.getCString();
    }

    public boolean hasMember(final String member) {
        return DBus._messageHasMember(this._peer, pointerToCString(member));
    }

    public boolean setErrorName(final String errorName) {
        return DBus._messageSetErrorName(this._peer, pointerToCString(errorName));
    }

    public String getErrorName() {
        final Pointer<Byte> _error = DBus._messageGetErrorName(this._peer);
        return _error == Pointer.NULL ? null : _error.getCString();
    }

    public boolean setDestination(final String destination) {
        return DBus._messageSetDestination(this._peer, pointerToCString(destination));
    }

    public String getDestination() {
        return DBus._messageGetDestination(this._peer).getCString();
    }

    public boolean setSender(final String sender) {
        return DBus._messageSetSender(this._peer, pointerToCString(sender));
    }

    public String getSender() {
        return DBus._messageGetSender(this._peer).getCString();
    }

    public String getSignature() {
        return DBus._messageGetSignature(this._peer).getCString();
    }

    public void setNoReply(final boolean noReply) {
        DBus._messageSetNoReply(this._peer, noReply);
    }

    public boolean isNoReply() {
        return DBus._messageGetNoReply(this._peer);
    }

    public long getSerial() {
        return DBus._messageGetSerial(this._peer);
    }

    public long getReplySerial() {
        return DBus._messageGetReplySerial(this._peer);
    }

    public void setAutoStart(final boolean autoStart) {
        DBus._messageSetAutoStart(this._peer, autoStart);
    }

    public boolean isAutoStart() {
        return DBus._messageGetAutoStart(this._peer);
    }

    public boolean containsUnixFds() {
        return DBus._messageContainsUnixFds(this._peer);
    }

    public void addArguments(final MethodArgument... arguments) {
        final Pointer<_MessageIter> _iter = DBus._new(_MessageIter.class, 1);
        DBus._messageIterInitAppend(this._peer, _iter);
        for (MethodArgument arg: arguments) {
            if (arg.isBasicType()) {
                DBus._messageIterAppendBasic(_iter, arg.getType(), arg.value);
            } else if (arg.isContainerType()) {
                // TODO Implement me!
            } else if (arg.isFixedType()) {
                // TODO Implement me!
            } else {
                throw new IllegalStateException("Unknown argument type: " + arg.getType());
            }
        }
    }

    @Override
    public Iterator<MethodArgument<?>> iterator() {

        return new MethodArgumentIterator(this._peer);

    }


    /**
     * @author Benjamin P. Jung
     */
    public static enum Type {

        /** This value is never a valid message type */
        INVALID(0),

        /** Message type of a method call message, */
        METHOD_CALL(1),

        /** Message type of a method return message */
        METHOD_RETURN(2),

        /** Message type of an error reply message */
        ERROR(3),

        /** Message type of a signal message */
        SIGNAL(4),

        /** Special marker to specify unknown message types. */
        OTHER(-1);

        private static final Map<Integer, Type> INT_MAPPING = new HashMap<>();
        private static final Map<Type, Integer> OBJ_MAPPING = new HashMap<>();
        static {
            for (final Type t: Type.values()) {
                INT_MAPPING.put(Integer.valueOf(Integer.valueOf(t.value)) , t);
                OBJ_MAPPING.put(t, Integer.valueOf(Integer.valueOf(t.value)));
            }
        }

        public static Type fromValue(final int value) {
            if (!INT_MAPPING.containsKey(value)) {
                // Special workaround because according to the D-Bus specification
                // every integer value can be used as a message type flag.
                return OTHER;
            }
            return INT_MAPPING.get(Integer.valueOf(value));
        }

        public static int fromEnum(final Type type) {
            return OBJ_MAPPING.get(type);
        }

        private final int value;
        private Type(final int value) {
            this.value = value;
        }
        int value() { return this.value; }

    }

    @Override
    public void finalize() throws Throwable {
        DBus._messageUnref(_peer);
        super.finalize();
    }


    /**
     * A single argument for in- and outgoing messages.
     * Basically a wrapper around the MessageIter logic found in the native D-Bus implementation.
     */
    public static abstract class MethodArgument<T> {

        protected final Pointer<?> value;

        protected MethodArgument(Pointer<?> value) {
            super();
            this.value = value;
        }

        public static BooleanMethodArgument booleanParam(final boolean value) {
            return new BooleanMethodArgument(pointerToInt(value ? 1 : 0));
        }

        public static ByteMethodArgument byteParam(final byte value) {
            return new ByteMethodArgument(pointerToByte(value));
        }

        public static Uint16MethodArgument uint16Param(final short value) {
            return new Uint16MethodArgument(pointerToShort(value));
        }

        public static Uint32MethodArgument uint32Param(final int value) {
            return new Uint32MethodArgument(pointerToInt(value));
        }

        public static Uint64MethodArgument uint64Param(final long value) {
            return new Uint64MethodArgument(pointerToLong(value));
        }

        public static Int16MethodArgument int16Param(final short value) {
            return new Int16MethodArgument(pointerToShort(value));
        }

        public static Int32MethodArgument int32Param(final int value) {
            return new Int32MethodArgument(pointerToInt(value));
        }

        public static Int64MethodArgument int64Param(final long value) {
            return new Int64MethodArgument(pointerToLong(value));
        }

        public static MethodArgument stringParam(final String value) {
            return new StringMethodArgument(pointerToPointer(pointerToCString(value)));
        }

        boolean isBasicType() {
            return DBus._typeIsBasic(this.getType());
        }

        boolean isContainerType() {
            return DBus._typeIsContainer(this.getType());
        }

        boolean isFixedType() {
            return DBus._typeIsFixed(this.getType());
        }

        @Internal
        static MethodArgument<?> _basic(final int type, final Pointer<_MessageIter> _iter) {
            switch (type) {
                case DBus.TYPE_BOOLEAN:
                    Pointer<Integer> _boolean = DBus._new0(Pointer.class, 1);
                    DBus._messageIterGetBasic(_iter, _boolean);
                    return new BooleanMethodArgument(_boolean);
                case DBus.TYPE_BYTE:
                    Pointer<Byte> _byte = DBus._new0(Pointer.class, 1);
                    DBus._messageIterGetBasic(_iter, _byte);
                    return new ByteMethodArgument(_byte);
                case DBus.TYPE_UINT16:
                    Pointer<Short> _uint16 = DBus._new0(Pointer.class, 1);
                    DBus._messageIterGetBasic(_iter, _uint16);
                    return new Uint16MethodArgument(_uint16);
                case DBus.TYPE_UINT32:
                    Pointer<Integer> _uint32 = DBus._new0(Pointer.class, 1);
                    DBus._messageIterGetBasic(_iter, _uint32);
                    return new Uint32MethodArgument(_uint32);
                case DBus.TYPE_UINT64:
                    Pointer<Long> _uint64 = DBus._new0(Pointer.class, 1);
                    DBus._messageIterGetBasic(_iter, _uint64);
                    return new Uint64MethodArgument(_uint64);
                case DBus.TYPE_INT16:
                    Pointer<Short> _int16 = DBus._new0(Pointer.class, 1);
                    DBus._messageIterGetBasic(_iter, _int16);
                    return new Int16MethodArgument(_int16);
                case DBus.TYPE_INT32:
                    Pointer<Integer> _int32 = DBus._new0(Pointer.class, 1);
                    DBus._messageIterGetBasic(_iter, _int32);
                    return new Int32MethodArgument(_int32);
                case DBus.TYPE_INT64:
                    Pointer<Long> _int64 = DBus._new0(Pointer.class, 1);
                    DBus._messageIterGetBasic(_iter, _int64);
                    return new Int64MethodArgument(_int64);
                case DBus.TYPE_DOUBLE:
                    Pointer<Double> _double = DBus._new0(Pointer.class, 1);
                    DBus._messageIterGetBasic(_iter, _double);
                    return new DoubleMethodArgument(_double);
                case DBus.TYPE_STRING:
                    Pointer<Pointer<Byte>> _string = DBus._new(Pointer.class, 1);
                    DBus._messageIterGetBasic(_iter, _string);
                    return new StringMethodArgument(_string);
                default:
                    throw new IllegalStateException("Unknown basic argument type: " + type);
            }
        }

        public abstract int getType();

        public abstract <T> T get();

        @Override
        public String toString() {
            return this.get().toString();
        }

        @Override
        public void finalize() throws Throwable {
            DBus._free(this.value);
            super.finalize();
        }

    }

    public static final class BooleanMethodArgument extends MethodArgument<Boolean> {
        protected BooleanMethodArgument(Pointer<Integer> value) { super(value); }
        @Override public int getType() { return DBus.TYPE_BOOLEAN; }
        @Override public Boolean get() { return this.value.getInt() == 1 ? Boolean.TRUE : Boolean.FALSE; }
    }

    public static final class ByteMethodArgument extends MethodArgument<Byte> {
        protected ByteMethodArgument(Pointer<Byte> value) { super(value); }
        @Override public int getType() { return DBus.TYPE_BYTE; }
        @Override public Byte get() { return Byte.valueOf(value.getByte()); }
    }

    public static final class Uint16MethodArgument extends MethodArgument<Short> {
        protected Uint16MethodArgument(Pointer<Short> value) { super(value); }
        @Override public int getType() { return DBus.TYPE_UINT16; }
        @Override public Short get() { return Short.valueOf(this.value.getShort()); }
    }

    public static final class Uint32MethodArgument extends MethodArgument<Integer> {
        protected Uint32MethodArgument(Pointer<Integer> value) { super(value); }
        @Override public int getType() { return DBus.TYPE_UINT32; }
        @Override public Integer get() { return Integer.valueOf(this.value.getInt()); }
    }

    public static final class Uint64MethodArgument extends MethodArgument<Long> {
        protected Uint64MethodArgument(Pointer<Long> value) { super(value); }
        @Override public int getType() { return DBus.TYPE_UINT64; }
        @Override public Long get() { return Long.valueOf(this.value.getLong()); }
    }

    public static final class Int16MethodArgument extends MethodArgument<Short> {
        protected Int16MethodArgument(Pointer<Short> value) { super(value); }
        @Override public int getType() { return DBus.TYPE_INT16; }
        @Override public Short get() { return Short.valueOf(this.value.getShort()); }
    }

    public static final class Int32MethodArgument extends MethodArgument<Integer> {
        protected Int32MethodArgument(Pointer<Integer> value) { super(value); }
        @Override public int getType() { return DBus.TYPE_INT32; }
        @Override public Integer get() { return Integer.valueOf(this.value.getInt()); }
    }

    public static final class Int64MethodArgument extends MethodArgument<Long> {
        protected Int64MethodArgument(Pointer<Long> value) { super(value); }
        @Override public int getType() { return DBus.TYPE_INT64; }
        @Override public Long get() { return Long.valueOf(this.value.getLong()); }
    }

    public static final class DoubleMethodArgument extends MethodArgument<Double> {
        protected DoubleMethodArgument(Pointer<Double> value) { super(value); }
        @Override public int getType() { return DBus.TYPE_DOUBLE; }
        @Override public Double get() { return Double.valueOf(value.getDouble()); }
    }

    public static final class StringMethodArgument extends MethodArgument<String> {
        protected StringMethodArgument(Pointer<Pointer<Byte>> value) { super(value); }
        @Override public int getType() { return DBus.TYPE_STRING; }
        @Override public String get() { return this.value.getPointer(Byte.class).getCString(); }
    }

    public final class MethodArgumentIterator implements Iterator<MethodArgument<?>> {

        private final Pointer<_Message> _message;
        private final Pointer<_MessageIter> _iter;
        private final boolean empty;
        private int idx = -1;


        protected MethodArgumentIterator(final Pointer<_Message> _message) {
            super();
            this._message = _message;
            this._iter = DBus._new(_MessageIter.class, 1);
            this.empty = (!DBus._messageIterInit(_message, _iter));
        }

        @Override
        public boolean hasNext() {
            if (empty) {
                return false;
            } else if (idx < 0) {
                return !this.empty;
            } else {
                return DBus._messageIterHasNext(_iter);
            }
        }

        @Override
        public MethodArgument next() {

            if (idx >= 0) {
                DBus._messageIterNext(_iter);
            }
            idx++;

            final int _argType = DBus._messageIterGetArgType(_iter);

            if (DBus._typeIsBasic(_argType)) {
                return MethodArgument._basic(_argType, _iter);

            } else if (DBus._typeIsContainer(_argType)) {
                // TODO Implement me!
                throw new IllegalStateException("Unknown container argument type: " + _argType);

            } else if (DBus._typeIsFixed(_argType)) {
                // TODO Implement me!
                throw new IllegalStateException("Unknown fixed argument type: " + _argType);

            } else {
                throw new IllegalStateException("Unknown argument type: " + _argType);
            }

        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove() not implemented.");
        }

    }

}
