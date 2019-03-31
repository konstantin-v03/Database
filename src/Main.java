import DatabaseAPI.Cow;
import DatabaseAPI.CowDatabase;
public class Main {

    public static void main(String[] args) {
        Cow cow1 = new Cow(1, 12, null, "Brown");
        Cow cow2 = new Cow(2, 14, null, "Black");
        Cow cow3 = new Cow(3, 7, null, "Red");
        Cow cow4 = new Cow(4, 8, null, "Yellow");

        CowDatabase cowDatabase = new CowDatabase();

        cowDatabase.insert(cow1);
        cowDatabase.insert(cow2);
        cowDatabase.insert(cow3);
        cowDatabase.insert(cow4);

        System.out.println(cowDatabase.find(1));
        System.out.println(cowDatabase.find(2));
        System.out.println(cowDatabase.find(3));
        System.out.println(cowDatabase.find(4));
    }
}
