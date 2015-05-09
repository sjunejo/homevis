package com.jmatio.io;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
* @author Wojciech Gradkowski <wgradkowski@gmail.com>
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
interface DataOutputStream 
{
    /**
     * Returns the current size of this stream.
     * 
     * @return the current size of this stream.
     * @throws IOException
     */
    public abstract int size() throws IOException;

    /**
     * Returns the current {@link ByteBuffer} mapped on the target file.
     * <p>
     * Note: the {@link ByteBuffer} has <strong>READ ONLY</strong> access.
     * 
     * @return the {@link ByteBuffer}
     * @throws IOException
     */
    public abstract ByteBuffer getByteBuffer() throws IOException;

    /**
     * Writes a sequence of bytes to this stream from the given buffer.
     * 
     * @param byteBuffer
     *            the source {@link ByteBuffer}
     * @throws IOException
     */
    public abstract void write(ByteBuffer byteBuffer) throws IOException;

}