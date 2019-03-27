package DatabaseCore;

import static Utilities.ByteUtils.*;

public class Block {

    private BlockStorage blockStorage;

    private int id;

    public Block(BlockStorage blockStorage, int id){
        this.blockStorage = blockStorage;
        this.id = id;
    }

    public int getHeader(int headerField) {
        if(headerField >= blockStorage.numBlockHeaderFields)
            throw new IllegalArgumentException("Illegal headerField");

        return bytesToInt(blockStorage.byteSequence.read(id * blockStorage.blockSize + headerField * blockStorage.blockHeaderFieldSize, blockStorage.blockHeaderFieldSize));
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

        byte toWrite[] = new byte[count];

        for(int i = 0, j = srcOffSet; i < toWrite.length; i++, j++) {
            toWrite[i] = src[j];
        }

        blockStorage.byteSequence.write(toWrite, id * blockStorage.blockSize + destOffSet);
    }

    public void clearContent(){
        byte bytes[] = new byte[blockStorage.blockSize];

        for(int i = 0; i < bytes.length; i++){
            bytes[i] = 0;
        }

        blockStorage.byteSequence.write(bytes,id * blockStorage.blockSize);
    }

    public int getId() {
        return id;
    }
}
