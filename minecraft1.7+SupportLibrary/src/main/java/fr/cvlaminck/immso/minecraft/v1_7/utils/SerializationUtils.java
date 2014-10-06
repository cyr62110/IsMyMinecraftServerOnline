package fr.cvlaminck.immso.minecraft.v1_7.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 */
public final class SerializationUtils {

    private static ObjectMapper instance;

    private SerializationUtils() {
    }

    public static ObjectMapper objectMapper() {
        if(instance == null) {
            synchronized (SerializationUtils.class) {
                if(instance == null)
                    instance = new ObjectMapper();
            }
        }
        return instance;
    }
}
