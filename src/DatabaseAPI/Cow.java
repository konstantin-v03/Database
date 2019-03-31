package DatabaseAPI;

import java.io.Serializable;
import java.util.Arrays;

public class Cow implements Serializable {

    private int id;

    private int age;

    private byte DNA[];

    private String breed;

    public Cow(){

    }

    public Cow(int id, int age, byte[] DNA, String breed) {
        this.id = id;
        this.age = age;
        this.DNA = DNA;
        this.breed = breed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public byte[] getDNA() {
        return DNA;
    }

    public void setDNA(byte[] DNA) {
        this.DNA = DNA;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    @Override
    public String toString() {
        return "Cow{" +
                "id=" + id +
                ", age=" + age +
                ", DNA=" + Arrays.toString(DNA) +
                ", breed='" + breed + '\'' +
                '}';
    }
}
