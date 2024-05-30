package com.HDEngine.Utilities.FileManageTools;

import java.io.*;

public class SaveFile {

    public static void save(Object obj, String filename) 
    {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filename))) 
        {
            objectOutputStream.writeObject(obj);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    public static Object load(String filename) 
    {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filename))) 
        {
            return objectInputStream.readObject();
        } 
        catch (IOException | ClassNotFoundException e) 
        {
            e.printStackTrace();
            return null;
        }
    }
}
