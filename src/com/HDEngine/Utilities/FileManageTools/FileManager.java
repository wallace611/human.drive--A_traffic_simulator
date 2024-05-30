package com.HDEngine.Utilities.FileManageTools;

import java.io.Serializable;
import com.HDEngine.Editor.Object.Road.EditorRoadChunk;

public class FileManager implements Serializable {
    private static final long serialVersionUID = 1L;

    private EditorRoadChunk[][] map;
    private int[][] trafficLightGroup;
    private double[] trafficLightGroupTimer;

    // transfer Editor's data to FileManager
    public void setData(EditorRoadChunk[][] map, int[][] trafficLightGroup, double[] trafficLightGroupTimer) {
        this.map = map;
        this.trafficLightGroup = trafficLightGroup;
        this.trafficLightGroupTimer = trafficLightGroupTimer;
    }

    public EditorRoadChunk[][] getMap() {
        return map;
    }

    public int[][] getTrafficLightGroup() {
        return trafficLightGroup;
    }

    public double[] getTrafficLightGroupTimer() {
        return trafficLightGroupTimer;
    }

    public void saveToFile(String filename) {
        SaveFile.save(this, filename);
    }

    public static FileManager loadFromFile(String filename) {
        return (FileManager) SaveFile.load(filename);
    }
}
