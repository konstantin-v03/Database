package Utilities;

import java.nio.ByteBuffer;

public class ByteUtils {

    public static long bytesToInt(byte bytes[]){
        if(bytes.length != 4)
            throw new IllegalArgumentException("Illegal integer representation");

        ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.BYTES);
        byteBuffer.put(bytes);
        byteBuffer.flip();

        return byteBuffer.getInt();
    }

    public static byte[] intToBytes(int var){
        ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.BYTES);
        byteBuffer.putInt(var);
        byteBuffer.flip();

        return byteBuffer.array();
    }

}
