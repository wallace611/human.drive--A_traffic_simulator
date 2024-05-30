package com.HDEngine.Editor;

import com.HDEngine.Utilities.FileManager;
import java.util.Scanner;

public class EditorTest3 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int runset=0;
        int ID_X = 0, ID_Y = 0;
        runset = input.nextInt();
        Editor editor = new Editor();
        System.out.println("EditorTest start");
        for(int i = 0 ; i < runset ; i++)
        {
            editor.addNewChunk();
        }
        for(int i = 0 ; i < runset ; i++)
        {
            ID_X = input.nextInt();
            ID_Y = input.nextInt();
            System.out.println(editor.getChunk(ID_X, ID_Y));
        }
        input.close();
    }
}
