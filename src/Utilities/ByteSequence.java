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
        byte extendedSource[] = new byte[source.length + sizeOfExtending];

        int i;

        for(i = 0; i < source.length; i++){
            extendedSource[i] = source[i];
        }

        for(; i < extendedSource.length; i++){
            extendedSource[i] = 0;
        }

        source = extendedSource;
    }

    public int length(){
        return source.length;
    }

}
