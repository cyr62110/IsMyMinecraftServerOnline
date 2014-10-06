package fr.cvlaminck.immso.minecraft.v1_7.io;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import fr.cvlaminck.immso.minecraft.v1_7.types.VarInt;
import fr.cvlaminck.immso.minecraft.v1_7.types.String;

/**
 * Data input stream written to support Minecraft 1.7+ server protocols.
 */
public class MC17DataInputStream extends DataInputStream {

    public MC17DataInputStream(InputStream in) {
        super(in);
    }

    public VarInt readVarInt() throws IOException {
        return VarInt.readFromInputStream(in);
    }

    public String readString() throws IOException {
        return String.readFromInputStream(in);
    }

}
