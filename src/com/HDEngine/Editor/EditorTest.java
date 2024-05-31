package com.HDEngine.Editor;

import com.HDEngine.Utilities.FileManageTools.FileManager;
import java.util.Scanner;

public class EditorTest {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int runset=0;
        runset = input.nextInt();
        input.nextLine();
        Editor editor = new Editor();
        System.out.println("EditorTest start");
        for(int i = 0 ; i < runset ; i++)
        {
            editor.addNewChunk();
        }
        String filePath = editor.assignPath(input);
        FileManager fileManager = editor.exportData();
        fileManager.saveToFile("src/SavedFile/editor_map.obj");
        //fileManager.saveToFile(filePath);
        input.close();
    }
}
