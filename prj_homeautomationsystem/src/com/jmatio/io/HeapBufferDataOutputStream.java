package com.jmatio.io;

import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.nio.ByteBuffer;
/**
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
public class HeapBufferDataOutputStream extends ByteArrayOutputStream implements DataOutputStream
{
    private final int BUFFER_SIZE = 1024;
    
    public ByteBuffer getByteBuffer() throws IOException
    {
        return ByteBuffer.wrap( super.buf );
    }

    public void write(ByteBuffer byteBuffer) throws IOException
    {
        
        byte[] tmp = new byte[BUFFER_SIZE]; 
        
        while ( byteBuffer.hasRemaining() )
        {
            int length = Math.min( byteBuffer.remaining(), tmp.length );
            byteBuffer.get( tmp, 0, length);
            write(tmp, 0, length);
        }
    }
}
