package Utilities;

import java.io.*;

public class SerializeHelper {

    public static byte[] serialize(Object object){
        if(object == null)
            return null;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try(ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)){
            objectOutputStream.writeObject(object);
            return byteArrayOutputStream.toByteArray();
        }catch (IOException ex){
            return null;
        }
    }

    public static Object deserialize(byte bytes[]){
        if(bytes == null)
            return null;

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        try(ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)){
            return objectInputStream.readObject();
        }catch (IOException|ClassNotFoundException ex){
            return null;
        }
    }

}
