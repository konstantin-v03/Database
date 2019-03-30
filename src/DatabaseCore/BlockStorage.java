package DatabaseCore;

import Utilities.ByteSequence;

public class BlockStorage {
    private int numBlocks;

    public final ByteSequence byteSequence;

    public final int blockSize;
    public final int blockHeaderSize;
    public final int blockContentSize;

    public final int blockHeaderFieldSize;
    public final int numBlockHeaderFields;

    public BlockStorage(ByteSequence byteSequence, int blockSize, int blockHeaderSize, int blockHeaderFieldSize){
        if(byteSequence == null)
            throw new IllegalArgumentException("Illegal byteSequence");

        if(blockHeaderSize >= blockSize)
            throw new IllegalArgumentException("Illegal blockHeaderSize");

        if(blockHeaderFieldSize > blockHeaderSize || blockHeaderFieldSize != 4 || blockHeaderSize % blockHeaderFieldSize != 0)
            throw new IllegalArgumentException("Illegal blockHeaderFieldSize");

        this.byteSequence = byteSequence;

        this.blockSize = blockSize;
        this.blockHeaderSize = blockHeaderSize;
        this.blockContentSize = blockSize - blockHeaderSize;

        this.blockHeaderFieldSize = blockHeaderFieldSize;
        numBlockHeaderFields = blockHeaderSize / blockHeaderFieldSize;
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

        numBlocks++;

        return new Block(this, id);
    }

    public int getNumBlocks() {
        return numBlocks;
    }

}
