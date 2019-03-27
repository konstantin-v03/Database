package DatabaseCore;

import Utilities.ByteSequence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static Utilities.ByteSequence.extendByteArray;

public class RecordStorage {

    private static class BLOCK_HEADERS{
        static int NEXT_BLOCK_ID  = 0;
        static int PREV_BLOCK_ID  = 1;
        static int CONTENT_LENGTH = 2;
        static int IS_DISPOSED    = 3;
    }

    private BlockStorage blockStorage;

    public RecordStorage(int blockContentSize){

        blockStorage = new BlockStorage(new ByteSequence(new byte[0]), blockContentSize + 16, 16, 4);

    }

    public byte[] getRecordContent(int recordId){
        Block block = blockStorage.findBlock(recordId);

        if(block == null)
            return null;

        if(block.getHeader(BLOCK_HEADERS.IS_DISPOSED) == 1){
            return null;
        }

        int offSet = 0;
        int nextBlockId;
        int blockSize = block.getHeader(BLOCK_HEADERS.CONTENT_LENGTH);

        byte bytes[] = new byte[blockSize];

        while(true){
            block.read(bytes, offSet, blockStorage.blockHeaderSize, blockSize);
            nextBlockId = block.getHeader(BLOCK_HEADERS.NEXT_BLOCK_ID);

            if(nextBlockId == -1) return bytes;

            offSet += blockSize;

            block = blockStorage.findBlock(nextBlockId);
            blockSize = block.getHeader(BLOCK_HEADERS.CONTENT_LENGTH);
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

            current.setHeader(BLOCK_HEADERS.PREV_BLOCK_ID, (prev == null) ? -1 : prev.getId());
            current.setHeader(BLOCK_HEADERS.NEXT_BLOCK_ID, -1);
            current.setHeader(BLOCK_HEADERS.CONTENT_LENGTH, toWriteInCurrent);
            current.setHeader(BLOCK_HEADERS.IS_DISPOSED, 0);

            written += toWriteInCurrent;

            if(toWrite > written){
                prev = current;
                current = createBlock();
                prev.setHeader(BLOCK_HEADERS.NEXT_BLOCK_ID, current.getId());
            }
            else
                break;
        }
    }

    public void disposeRecord(int recordId){
        List<Block> blocks = findBlocksInRecord(recordId);

        for(Block block : blocks){
            block.setHeader(BLOCK_HEADERS.IS_DISPOSED, 1);
        }
    }

    public void updateRecord(int recordId, byte bytes[]){
        List<Block> blocks = findBlocksInRecord(recordId);

        if(blocks == null)
            throw new IllegalArgumentException("Illegal recordId");

        int nextId = 0;

        Block current = blocks.get(nextId);
        Block prev = null;

        int toWrite = bytes.length;
        int written = 0;

        int toWriteInCurrent;

        while(true){
            toWriteInCurrent = Math.min(toWrite - written, blockStorage.blockContentSize);

            current.write(bytes, written, blockStorage.blockHeaderSize, toWriteInCurrent);

            current.setHeader(BLOCK_HEADERS.PREV_BLOCK_ID, (prev == null) ? -1 : prev.getId());
            current.setHeader(BLOCK_HEADERS.NEXT_BLOCK_ID, -1);
            current.setHeader(BLOCK_HEADERS.CONTENT_LENGTH, toWriteInCurrent);
            current.setHeader(BLOCK_HEADERS.IS_DISPOSED, 0);

            written += toWriteInCurrent;

            if(toWrite > written){
                prev = current;

                if(blocks.size() > ++nextId){
                    current = blocks.get(nextId);
                }else {
                    current = createBlock();
                }

                prev.setHeader(BLOCK_HEADERS.NEXT_BLOCK_ID, current.getId());
            }
            else
                break;

        }

        for(int i = nextId; i < blocks.size(); i++){
            blockStorage.findBlock(i).setHeader(BLOCK_HEADERS.IS_DISPOSED, 1);
        }

    }

    public List<Block> findBlocksInRecord(int recordId){
        List<Block> blocks = new ArrayList<>();

        Block block;
        int nextId = recordId;

        do {
            block = blockStorage.findBlock(nextId);
            blocks.add(block);
            nextId = block.getHeader(BLOCK_HEADERS.NEXT_BLOCK_ID);
        }while(nextId != -1);

        return (blocks.size() == 0) ? null : blocks;
    }

    public byte[] getBytes(){
        return blockStorage.byteSequence.getBytes();
    }

    public void printBlocks(){
        Block block;
        byte bytes[] = new byte[blockStorage.blockContentSize];

        for(int i = 0; i < blockStorage.getNumBlocks(); i++){
            block = blockStorage.findBlock(i);
            System.out.println("Block " + i + ": " + block.getHeader(BLOCK_HEADERS.NEXT_BLOCK_ID) + ", " + block.getHeader(BLOCK_HEADERS.PREV_BLOCK_ID) + ", " +
                    block.getHeader(BLOCK_HEADERS.CONTENT_LENGTH) + ", " + block.getHeader(BLOCK_HEADERS.IS_DISPOSED));
            block.read(bytes, 0, blockStorage.blockHeaderSize, bytes.length);
            if(block.getHeader(BLOCK_HEADERS.IS_DISPOSED) == 1){
                System.out.println("Content: DISPOSED\n");
            }
            else {
                System.out.println("Content: " + Arrays.toString(bytes) + " = " + new String(bytes) + "\n");
            }
        }
    }

    private Block createBlock(){
        Block block;

        int numBlocks = blockStorage.getNumBlocks();

        for(int i = 0; i < numBlocks; i++){
            block = blockStorage.findBlock(i);
            if(block.getHeader(BLOCK_HEADERS.IS_DISPOSED) == 1){
                block.clearContent();
                return block;
            }
        }

        return blockStorage.createBlock();
    }
}

