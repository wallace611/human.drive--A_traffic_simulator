package com.HDEngine.Editor;

import com.HDEngine.Utilities.FileManager;
import java.util.Scanner;

public class EditorTest {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int ID_X = 0, ID_Y = 0;
        Editor editor = new Editor();
        System.out.println("EditorTest start");
        editor.addNewChunk();
        ID_X = input.nextInt();
        ID_Y = input.nextInt();
        System.out.println(editor.getChunk(ID_X, ID_Y));

        FileManager fileManager = editor.exportData();
        fileManager.saveToFile("D:\\coding\\java\\Project\\human.drive--A_traffic_simulator\\src\\com\\HDEngine\\Utilities\\SavedFile\\editor_map.obj");
        input.close();
    }
}
