package DatabaseCore;

import java.util.List;

public class RecordStorage {

    private enum headers {
        NEXT_BLOCK_ID, PREV_BLOCK_ID, RECORD_LENGTH, IS_DISPOSED
    }

    private BlockStorage blockStorage;

    public RecordStorage(BlockStorage blockStorage){

        if(blockStorage == null)
            throw new IllegalArgumentException("Illegal blockStorage");

        this.blockStorage = blockStorage;

    }

    public byte[] find(int recordId){

        return null;
    }

    public void create(byte bytes[]){

    }

    public void delete(int recordId){

    }

    public void update(int recordId, int bytes[]){

    }

    public List<Block> findBlocks(int recordId){

        return null;
    }

}

