package com.HDEngine.Editor;

import com.HDEngine.Editor.Object.Road.EditorRoadChunk;
import java.util.Scanner;

public class EditorTest3 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter the number of chunks to add: ");
        int runset = input.nextInt();
        Editor editor = new Editor();
        System.out.println("EditorTest start");

        for(int i = 0; i < runset; i++) {
            editor.addNewChunk();
        }

        System.out.println("Enter coordinates to check the chunk (Enter -1 -1 to stop):");
        while(true) {
            System.out.print("ID_X: ");
            int ID_X = input.nextInt();
            System.out.print("ID_Y: ");
            int ID_Y = input.nextInt();
            if (ID_X == -1 && ID_Y == -1) break;

            EditorRoadChunk chunk = editor.getChunk(ID_X, ID_Y);
            if (chunk != null) {
                System.out.println(chunk);
            } else {
                System.out.println("No chunk found at coordinates (" + ID_X + ", " + ID_Y + ")");
            }
        }

        input.close();
    }
}
