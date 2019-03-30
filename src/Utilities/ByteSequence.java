package Utilities;

public class ByteSequence {

    private byte source[];

    public ByteSequence(byte source[]){
        this.source = source;
    }

    public byte[] read(int sourceOffSet, int count){
        if(sourceOffSet + count > source.length)
            throw new IndexOutOfBoundsException("Out of source's bounds");

        byte bytes[] = new byte[count];

        for(int i = 0, j = sourceOffSet; i < count; i++, j++) {
            bytes[i] = source[j];
        }

        return bytes;
    }

    public void write(byte bytes[], int sourceOffSet) {
        if(sourceOffSet + bytes.length > source.length)
            throw new IndexOutOfBoundsException("Out of source's bounds");

        for (int i = 0, j = sourceOffSet; i < bytes.length; i++, j++) {
            source[j] = bytes[i];
        }
    }

    public void extend(int sizeOfExtending){
        source = extendByteArray(source, sizeOfExtending);
    }

    public int length(){
        return source.length;
    }

    public byte[] getBytes(){
        return source;
    }

    public static byte[] extendByteArray(byte array[], int sizeOfExtending){
        byte extendedArray[] = new byte[array.length + sizeOfExtending];

        int i = 0;

        for(; i < array.length; i++){
            extendedArray[i] = array[i];
        }

        for(; i < extendedArray.length; i++){
            extendedArray[i] = 0;
        }

        return extendedArray;
    }

}
