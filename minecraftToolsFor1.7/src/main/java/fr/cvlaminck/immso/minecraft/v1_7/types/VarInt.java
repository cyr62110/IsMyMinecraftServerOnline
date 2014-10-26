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

/**
 *
 */
public class VarInt {

    private final int value;

    public VarInt(int value) {
        this.value = value;
    }

    public void writeOnOutputStream(final OutputStream out) throws IOException {
        int value = this.value;
        do {
            final int nextByteToWrite = (((value & 0xFFFFFF80) != 0) ? 0x80 : 0x00) | (value & 0x7F);
            out.write(nextByteToWrite);
            value >>= 7;
        } while (value != 0);
    }

    public static VarInt readFromInputStream(final InputStream in) throws IOException {
        int value = 0;
        int lastByteRead = 0;
        int loopCount = 0;
        do {
            lastByteRead = in.read();
            if (lastByteRead == -1)
                throw new IOException("read returned -1");
            value = ((lastByteRead & 0x7F) << (7 * loopCount)) | value;
            loopCount++;
        } while ((lastByteRead & 0x80) != 0);
        return new VarInt(value);
    }

    /**
     * Size in bytes of the byte array representing this varint when written on
     * a stream.
     */
    public int getLength() {
        int value = this.value;
        int size = 0;
        do {
            size++;
            value >>= 7;
        } while (value != 0);
        return size;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VarInt)) return false;

        VarInt varInt = (VarInt) o;

        if (value != varInt.value) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value;
    }
}
