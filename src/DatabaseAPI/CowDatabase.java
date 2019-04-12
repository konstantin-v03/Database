package DatabaseAPI;

public class CowDatabase extends Database<Cow, Integer> {

    public CowDatabase(String fileName, boolean isReCreate) {
        super(fileName, isReCreate);
    }

    @Override
    protected Integer getKey(Cow item) {
        return item.getId();
    }
}

