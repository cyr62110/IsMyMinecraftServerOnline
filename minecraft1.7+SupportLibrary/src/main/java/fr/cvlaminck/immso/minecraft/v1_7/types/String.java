/**
 * Copyright 2014 Cyril Vlaminck
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
package fr.cvlaminck.immso.minecraft.v1_7.types;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * A string as described in Minecraft 1.7 server protocol
 */
public class String {

    private java.lang.String value;

    /**
     * Charset used in the Minecraft 1.7 server protocol.
     */
    private static final Charset protocolCharset = Charset.forName("UTF-8");

    public String(java.lang.String value) {
        this.value = value;
    }

    public void writeOnOutputStream(final OutputStream out) throws IOException {
        final byte[] bValue = value.getBytes(protocolCharset);
        final VarInt length = new VarInt(bValue.length);
        length.writeOnOutputStream(out);
        out.write(bValue);
    }

    public static String readFromInputStream(final InputStream in) throws IOException {
        final VarInt length = VarInt.readFromInputStream(in);
        final byte[] bValue = new byte[length.getValue()];
        int totalRead = 0;
        int read = 0;
        while (totalRead < length.getValue()) {
            read = in.read(bValue, totalRead, length.getValue() - totalRead);
            if (read == -1)
                throw new IOException("read returned -1");
            totalRead += read;
        }
        return new String(new java.lang.String(bValue, protocolCharset));
    }

    /**
     * Same as java.lang.String#length
     */
    public int length() {
        return value.length();
    }

    /**
     * Size in bytes of the byte array representing this string when written on
     * a stream.
     */
    public int getLength() {
        final VarInt length = new VarInt(value.length());
        final byte[] bValue = value.getBytes(protocolCharset);
        return length.getLength() + bValue.length;
    }

    public java.lang.String getValue() {
        return value;
    }
}
