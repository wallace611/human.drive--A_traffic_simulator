package com.HDEngine.Editor;

import com.HDEngine.Simulator.Simulator;
import com.HDEngine.Utilities.FileManageTools.FileManager;
import java.util.Scanner;

public class EditorTest {
    public static void main(String[] args) throws InterruptedException {
        Scanner input = new Scanner(System.in);
        System.out.println("EditorTest start");
        Editor editor = new Editor();
        System.out.println("EditorTest end");
        input.close();
    }
}
