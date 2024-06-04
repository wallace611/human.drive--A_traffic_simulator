package com.HDEngine.Utilities.FileManageTools;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.HDEngine.Editor.Object.Road.EditorRoadChunk;

public class FileManager implements Serializable {
    private static final long serialVersionUID = 1L;

    private EditorRoadChunk[][] map;
    private Map<Integer,int[]> trafficLight = new HashMap<>();

    // transfer Editor's data to FileManager
    public void setData(EditorRoadChunk[][] map, Map<Integer,int[]> trafficLight) {
        this.map = map;
        this.trafficLight = trafficLight;
    }

    public EditorRoadChunk[][] getMap() {
        return map;
    }

    public Map<Integer,int[]> getTrafficLight() {
        return trafficLight;
    }

    public void saveToFile(String filename) {
        SaveFile.save(this, filename);
    }

    public static FileManager loadFromFile(String filename) {
        return (FileManager) SaveFile.load(filename);
    }
}
