package com.HDEngine.Editor;

import com.HDEngine.Utilities.FileManager;
import java.util.Scanner;

public class EditorTest2 {
    public static void main(String[] args) {
        
        Scanner input = new Scanner(System.in);
        int ID_X = 0, ID_Y = 0, runset=0;
        runset = input.nextInt();
        Editor editor = new Editor();
        FileManager loadedFileManager = FileManager.loadFromFile("D:\\coding\\java\\Project\\human.drive--A_traffic_simulator\\src\\com\\HDEngine\\Utilities\\SavedFile\\editor_map.obj");
        if (loadedFileManager != null) 
        {
            editor.importData(loadedFileManager);
            System.out.println("Map loaded!!!!!!");
        }
        System.out.println("EditorTest start");
        for(int i = 0 ; i < runset ; i++)
        {
            ID_X = input.nextInt();
            ID_Y = input.nextInt();
            System.out.println(editor.getChunk(ID_X, ID_Y));
        }
        input.close();
    }
}
