package DatabaseCore;

import Utilities.ByteSequence;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Arrays;
import java.util.List;

import static Utilities.ByteSequence.extendByteArray;

public class RecordStorage {

    private enum BLOCK_HEADERS {
        NEXT_BLOCK_ID, PREV_BLOCK_ID, CONTENT_LENGTH, IS_DISPOSED
    }

    private BlockStorage blockStorage;

    public RecordStorage(int blockContentSize){

        blockStorage = new BlockStorage(new ByteSequence(new byte[0]), blockContentSize + 16, 16, 4);

    }

    public byte[] getRecordContent(int recordId){
        Block block = blockStorage.findBlock(recordId);

        if(block == null)
            return null;

        int offSet = 0;
        int nextBlockId;
        int blockSize = block.getHeader(BLOCK_HEADERS.CONTENT_LENGTH.ordinal());

        byte bytes[] = new byte[blockSize];

        while(true){
            block.read(bytes, offSet, blockStorage.blockHeaderSize, blockSize);
            nextBlockId = block.getHeader(BLOCK_HEADERS.NEXT_BLOCK_ID.ordinal());

            if(nextBlockId == -1) return bytes;

            offSet += blockSize;

            block = blockStorage.findBlock(nextBlockId);
            blockSize = block.getHeader(BLOCK_HEADERS.CONTENT_LENGTH.ordinal());
            bytes = extendByteArray(bytes, blockSize);
        }
    }

    public void createRecord(byte bytes[]){
        Block current = createBlock();
        Block prev = null;

        int toWrite = bytes.length;
        int written = 0;

        int toWriteInCurrent;

        while(true){
            toWriteInCurrent = Math.min(toWrite - written, blockStorage.blockContentSize);

            current.write(bytes, written, blockStorage.blockHeaderSize, toWriteInCurrent);

            current.setHeader(BLOCK_HEADERS.PREV_BLOCK_ID.ordinal(), (prev == null) ? -1 : prev.getId());
            current.setHeader(BLOCK_HEADERS.NEXT_BLOCK_ID.ordinal(), -1);
            current.setHeader(BLOCK_HEADERS.CONTENT_LENGTH.ordinal(), toWriteInCurrent);
            current.setHeader(BLOCK_HEADERS.IS_DISPOSED.ordinal(), 0);

            written += toWriteInCurrent;

            if(toWrite > written){
                prev = current;
                current = createBlock();
                prev.setHeader(BLOCK_HEADERS.NEXT_BLOCK_ID.ordinal(), current.getId());
            }
            else
                break;
        }

        Block block;
        byte bytes1[] = new byte[blockStorage.blockContentSize];

        for(int i = 0; i < blockStorage.getNumBlocks(); i++){
            block = blockStorage.findBlock(i);
            System.out.println("Block " + i + ": " + block.getHeader(BLOCK_HEADERS.NEXT_BLOCK_ID.ordinal()) + ", " + block.getHeader(BLOCK_HEADERS.PREV_BLOCK_ID.ordinal()) + ", " +
                    block.getHeader(BLOCK_HEADERS.CONTENT_LENGTH.ordinal()) + ", " + block.getHeader(BLOCK_HEADERS.IS_DISPOSED.ordinal()));
            block.read(bytes1, 0, blockStorage.blockHeaderSize, bytes1.length);
            System.out.println("Content: " + Arrays.toString(bytes1) + " = " +  new String(bytes1) + "\n");
        }
    }

    public void deleteRecord(int recordId){
        throw new NotImplementedException();
    }

    public void updateRecord(int recordId, int bytes[]){
        throw new NotImplementedException();
    }

    public List<Block> findBlocksInRecord(int recordId){
        throw new NotImplementedException();
    }

    public byte[] getBytes(){
        return blockStorage.byteSequence.getBytes();
    }

    private Block createBlock(){
        Block block;

        int numBlocks = blockStorage.getNumBlocks();

        for(int i = 0; i < numBlocks; i++){
            block = blockStorage.findBlock(i);
            if(block.getHeader(BLOCK_HEADERS.IS_DISPOSED.ordinal()) == 1){
                return block;
            }
        }

        return blockStorage.createBlock();
    }
}

