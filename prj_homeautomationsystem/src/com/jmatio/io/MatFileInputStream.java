package com.jmatio.io;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.jmatio.common.MatDataTypes;
import com.jmatio.types.ByteStorageSupport;

/**
 * MAT-file input stream class. 
 * 
 * @author Wojciech Gradkowski <wgradkowski@gmail.com>
 * 
 * Copyright (c) 2004, Wojciech Gradkowski
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:

    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in
      the documentation and/or other materials provided with the distribution

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
 */
class MatFileInputStream
{
    private final int type;
    private final ByteBuffer buf;
    
    /**
     * Attach MAT-file input stream to <code>InputStream</code>
     * 
     * @param is - input stream
     * @param type - type of data in the stream
     * @see com.jmatio.common.MatDataTypes
     */
    public MatFileInputStream( ByteBuffer buf, int type )
    {
        this.type = type;
        this.buf = buf;
    }
    
    /**
     * Reads data (number of bytes red is determined by <i>data type</i>)
     * from the stream to <code>int</code>.
     * 
     * @return
     * @throws IOException
     */
    public int readInt() throws IOException
    {
        switch ( type )
        {
            case MatDataTypes.miUINT8:
                return (int)( buf.get() & 0xFF);
            case MatDataTypes.miINT8:
                return (int) buf.get();
            case MatDataTypes.miUINT16:
                return (int)( buf.getShort() & 0xFFFF);
            case MatDataTypes.miINT16:
                return (int) buf.getShort();
            case MatDataTypes.miUINT32:
                return (int)( buf.getInt() & 0xFFFFFFFF);
            case MatDataTypes.miINT32:
                return (int) buf.getInt();
            case MatDataTypes.miUINT64:
                return (int) buf.getLong();
            case MatDataTypes.miINT64:
                return (int) buf.getLong();
            case MatDataTypes.miDOUBLE:
                return (int) buf.getDouble();
            default:
                throw new IllegalArgumentException("Unknown data type: " + type);
        }
    }
    /**
     * Reads data (number of bytes red is determined by <i>data type</i>)
     * from the stream to <code>char</code>.
     * 
     * @return - char
     * @throws IOException
     */
    public char readChar() throws IOException
    {
        switch ( type )
        {
            case MatDataTypes.miUINT8:
                return (char)( buf.get() & 0xFF);
            case MatDataTypes.miINT8:
                return (char) buf.get();
            case MatDataTypes.miUINT16:
                return (char)( buf.getShort() & 0xFFFF);
            case MatDataTypes.miINT16:
                return (char) buf.getShort();
            case MatDataTypes.miUINT32:
                return (char)( buf.getInt() & 0xFFFFFFFF);
            case MatDataTypes.miINT32:
                return (char) buf.getInt();
            case MatDataTypes.miDOUBLE:
                return (char) buf.getDouble();
            case MatDataTypes.miUTF8:
                return (char) buf.get();
            default:
                throw new IllegalArgumentException("Unknown data type: " + type);
        }
    }
    /**
     * Reads data (number of bytes red is determined by <i>data type</i>)
     * from the stream to <code>double</code>.
     * 
     * @return - double
     * @throws IOException
     */
    public double readDouble() throws IOException
    {
        switch ( type )
        {
            case MatDataTypes.miUINT8:
                return (double)( buf.get() & 0xFF);
            case MatDataTypes.miINT8:
                return (double) buf.get();
            case MatDataTypes.miUINT16:
                return (double)( buf.getShort() & 0xFFFF);
            case MatDataTypes.miINT16:
                return (double) buf.getShort();
            case MatDataTypes.miUINT32:
                return (double)( buf.getInt() & 0xFFFFFFFF);
            case MatDataTypes.miINT32:
                return (double) buf.getInt();
            case MatDataTypes.miDOUBLE:
                return (double) buf.getDouble();
            default:
                throw new IllegalArgumentException("Unknown data type: " + type);
        }
    }

    public byte readByte()
    {
        switch ( type )
        {
            case MatDataTypes.miUINT8:
                return (byte)( buf.get() & 0xFF);
            case MatDataTypes.miINT8:
                return (byte) buf.get();
            case MatDataTypes.miUINT16:
                return (byte)( buf.getShort() & 0xFFFF);
            case MatDataTypes.miINT16:
                return (byte) buf.getShort();
            case MatDataTypes.miUINT32:
                return (byte)( buf.getInt() & 0xFFFFFFFF);
            case MatDataTypes.miINT32:
                return (byte) buf.getInt();
            case MatDataTypes.miDOUBLE:
                return (byte) buf.getDouble();
            case MatDataTypes.miUTF8:
                return (byte) buf.get();
            default:
                throw new IllegalArgumentException("Unknown data type: " + type);
        }
    }

    /**
     * Reads the data into a <code>{@link ByteBuffer}</code>. This method is
     * only supported for arrays with backing ByteBuffer (<code>{@link ByteStorageSupport}</code>).
     * 
     * @param dest
     *            the destination <code>{@link ByteBuffer}</code>
     * @param elements
     *            the number of elements to read into a buffer
     * @param storage
     *            the backing <code>{@link ByteStorageSupport}</code> that
     *            gives information how data should be interpreted
     * @return reference to the destination <code>{@link ByteBuffer}</code>
     * @throws IOException
     *             if buffer is under-fed, or another IO problem occurs
     */
    public ByteBuffer readToByteBuffer(ByteBuffer dest, int elements,
                    ByteStorageSupport<?> storage) throws IOException
    {
        
        int bytesAllocated = storage.getBytesAllocated();
        int size = elements * storage.getBytesAllocated();
        
        //direct buffer copy
        if ( MatDataTypes.sizeOf(type) == bytesAllocated && buf.order().equals(dest.order()) )
        {
            int bufMaxSize = 1024;
            int bufSize = Math.min(buf.remaining(), bufMaxSize);
            int bufPos = buf.position();
            
            byte[] tmp = new byte[ bufSize ];
            
            while ( dest.remaining() > 0 )
            {
                int length = Math.min(dest.remaining(), tmp.length);
                buf.get( tmp, 0, length );
                dest.put( tmp, 0, length );
            }
            buf.position( bufPos + size );
        }
        else
        {
            //because Matlab writes data not respectively to the declared
            //matrix type, the reading is not straight forward (as above)
            Class<?> clazz = storage.getStorageClazz();
            while ( dest.remaining() > 0 )
            {
                if ( clazz.equals( Double.class) )
                {
                    dest.putDouble( readDouble() );
                }
                else if ( clazz.equals( Byte.class) )
                {
                    dest.put( readByte() );
                }
                else if ( clazz.equals( Integer.class) )
                {
                    dest.putInt( readInt() );
                }
                else if ( clazz.equals( Long.class) )
                {
                    dest.putLong( readLong() );
                }
                else  if ( clazz.equals( Float.class) )
                {
                    dest.putFloat( readFloat() );
                }
                else if ( clazz.equals( Short.class) )
                {
                    dest.putShort( readShort() );
                }
                else
                {
                    throw new RuntimeException("Not supported buffer reader for " + clazz );
                }
            }
        }
        dest.rewind();
        return dest;
    }

    private float readFloat()
    {
        switch ( type )
        {
            case MatDataTypes.miUINT8:
                return (float)( buf.get() & 0xFF);
            case MatDataTypes.miINT8:
                return (float) buf.get();
            case MatDataTypes.miUINT16:
                return (float)( buf.getShort() & 0xFFFF);
            case MatDataTypes.miINT16:
                return (float) buf.getShort();
            case MatDataTypes.miUINT32:
                return (float)( buf.getInt() & 0xFFFFFFFF);
            case MatDataTypes.miINT32:
                return (float) buf.getInt();
            case MatDataTypes.miSINGLE:
                return (float) buf.getFloat();
            case MatDataTypes.miDOUBLE:
                return (float) buf.getDouble();
            default:
                throw new IllegalArgumentException("Unknown data type: " + type);
        }
    }
    private short readShort()
    {
        switch ( type )
        {
            case MatDataTypes.miUINT8:
                return (short)( buf.get() & 0xFF);
            case MatDataTypes.miINT8:
                return (short) buf.get();
            case MatDataTypes.miUINT16:
                return (short)( buf.getShort() & 0xFFFF);
            case MatDataTypes.miINT16:
                return (short) buf.getShort();
            case MatDataTypes.miUINT32:
                return (short)( buf.getInt() & 0xFFFFFFFF);
            case MatDataTypes.miINT32:
                return (short) buf.getInt();
            case MatDataTypes.miUINT64:
                return (short) buf.getLong();
            case MatDataTypes.miINT64:
                return (short) buf.getLong();
            case MatDataTypes.miDOUBLE:
                return (short) buf.getDouble();
            default:
                throw new IllegalArgumentException("Unknown data type: " + type);
        }
    }
    private long readLong()
    {
        switch ( type )
        {
            case MatDataTypes.miUINT8:
                return (long)( buf.get() & 0xFF);
            case MatDataTypes.miINT8:
                return (long) buf.get();
            case MatDataTypes.miUINT16:
                return (long)( buf.getShort() & 0xFFFF);
            case MatDataTypes.miINT16:
                return (long) buf.getShort();
            case MatDataTypes.miUINT32:
                return (long)( buf.getInt() & 0xFFFFFFFF);
            case MatDataTypes.miINT32:
                return (long) buf.getInt();
            case MatDataTypes.miUINT64:
                return (long) buf.getLong();
            case MatDataTypes.miINT64:
                return (long) buf.getLong();
            case MatDataTypes.miDOUBLE:
                return (long) buf.getDouble();
            default:
                throw new IllegalArgumentException("Unknown data type: " + type);
        }
    }

	public void skip(int padding) 
	{
		buf.position( buf.position() + padding );
	}
    

}
