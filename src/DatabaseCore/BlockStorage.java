package DatabaseCore;

import Utilities.ByteSequence;

public class BlockStorage {
    public final ByteSequence byteSequence;

    public final int blockSize;
    public final int blockHeadersSize;
    public final int blockContentSize;

    public final int blockHeaderFieldSize;
    public final int numBlockHeaderFields;

    public BlockStorage(ByteSequence byteSequence, int blockSize, int blockHeadersSize, int blockHeaderFieldSize){
        if(byteSequence == null)
            throw new IllegalArgumentException("Illegal byteSequence");

        if(blockHeadersSize >= blockSize)
            throw new IllegalArgumentException("Illegal blockHeaderSize");

        if(blockHeaderFieldSize > blockHeadersSize || blockHeaderFieldSize != 4 || blockHeadersSize % blockHeaderFieldSize != 0)
            throw new IllegalArgumentException("Illegal blockHeaderFieldSize");

        this.byteSequence = byteSequence;

        this.blockSize = blockSize;
        this.blockHeadersSize = blockHeadersSize;
        this.blockContentSize = blockSize - blockHeadersSize;

        this.blockHeaderFieldSize = blockHeaderFieldSize;
        numBlockHeaderFields = blockHeadersSize / blockHeaderFieldSize;
    }

    public Block findBlock(int id) {
        if(id * blockSize >= byteSequence.length())
            return null;
        else
            return new Block(this, id);
    }

    public Block createBlock() {
        int id = byteSequence.length() / blockSize;

        byteSequence.extend(blockSize);

        return new Block(this, id);
    }

    public int getNumBlocks() {
        return byteSequence.length() / blockSize;
    }

}
