package DatabaseCore;

import static Utilities.ByteUtils.*;

public class Block {

    private boolean isDisposed;

    private BlockStorage blockStorage;

    private int id;

    public Block(BlockStorage blockStorage, int id){
        this.blockStorage = blockStorage;
        this.id = id;
    }

    public long getHeader(int headerField) {
        if(headerField >= blockStorage.numBlockHeaderFields)
            throw new IllegalArgumentException("Illegal headerField");

        return bytesToInt(blockStorage.byteSequence.read(id * blockStorage.blockSize + headerField * blockStorage.blockHeaderFieldSize, 8));
    }

    public void setHeader(int headerField, int value) {
        if(headerField >= blockStorage.numBlockHeaderFields)
            throw new IllegalArgumentException("Illegal headerField");

         blockStorage.byteSequence.write(intToBytes(value), id * blockStorage.blockSize + headerField * blockStorage.blockHeaderFieldSize);
    }

    public void read(byte dest[], int destOffSet, int srcOffSet, int count) {
        if (destOffSet + count > dest.length || srcOffSet + count > blockStorage.blockSize)
            throw new IndexOutOfBoundsException("Out of block's bounds");

        byte src[] = blockStorage.byteSequence.read(id * blockStorage.blockSize + srcOffSet, count);
        int lastByte = destOffSet + count;

        for(int i = destOffSet, j = 0; i < lastByte; i++, j++){
            dest[i] = src[j];
        }
    }

    public void write(byte[] src, int srcOffSet, int destOffSet, int count) {
        if(srcOffSet + count > src.length || destOffSet + count > blockStorage.blockSize)
            throw new IndexOutOfBoundsException("Out of block's bounds");

        byte toWrite[] = new byte[src.length - srcOffSet];

        for(int i = 0, j = srcOffSet; i < toWrite.length; i++, j++) {
            toWrite[i] = src[j];
        }

        blockStorage.byteSequence.write(toWrite, id * blockStorage.blockSize + destOffSet);
    }

    public boolean isDisposed() {
        return isDisposed;
    }

    public void dispose() {
        isDisposed = true;
    }

}
