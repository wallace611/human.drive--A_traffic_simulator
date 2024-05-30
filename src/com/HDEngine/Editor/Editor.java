package com.HDEngine.Editor;

import com.HDEngine.Editor.Object.Road.EditorRoadChunk;
import java.util.Scanner;
import java.io.*;
import com.HDEngine.Utilities.FileManageTools.FileManager;

public class Editor implements Serializable
{
    private static final int NORTH = 2;
    private static final int EAST = 0;
    private static final int SOUTH = 6;
    private static final int WEST = 4;
    private static final int NE = 1;
    private static final int SE = 7;
    private static final int SW = 5;
    private static final int NW = 3;
    private EditorRoadChunk[][] map = new EditorRoadChunk[102][102];//if possible, keep the outer side of the array clear(eg. map[0][0] should always be null)
    private int[][] trafficLightGroup = new int[102][102];//if(is same group){[a][a] = [b][b]for road_a and road_b}
    private double[] trafficLightGroupTimer = new double[102];
    private int chunkCount = 0;
    private transient Scanner input = new Scanner(System.in);

    public Editor()
    {
        initializemap();
    }

    private void initializemap()//set the map to null to initiialize
    {
        for(int i = 0 ; i < map.length ; i++)
        {
            for(int j = 0 ; j < map.length ; j++)
            {
                map[i][j]=null;
                trafficLightGroup[i][j] = 0;  
                
            }
            trafficLightGroupTimer[i] = 0;
        }
    }

    public void addNewChunk()//create a new chunk
    {
        EditorRoadChunk newRoadChunk = new EditorRoadChunk();
        newRoadChunk.getData();//get ID,StartPoint,speedLimit,intersection,traffic light,weight to other road
        if(newRoadChunk.outOfMap(map))//the ID is out of map
            expandMap(map,newRoadChunk);
        else
            map[newRoadChunk.getIDX()][newRoadChunk.getIDY()] = newRoadChunk;
        if(newRoadChunk.haveTrafficLight())//there is a traffic light in this chunk
        {
            int group = newRoadChunk.getTrafficLightGroup();//which group is the traffic light in
            addToTrafficLightGroup(newRoadChunk);
            newRoadChunk.setTrafficLightTimer(getTrafficLightGroupTimer(group));//set the data of traffic(timer)
        }
    }

    public void addToTrafficLightGroup(EditorRoadChunk target)//record the traffic light group at map
    {
        trafficLightGroup[target.getIDX()][target.getIDY()] = target.getTrafficLightGroup();
    }

    public void setTrafficLightGroupTimer()//set group timer
    {
        int group = input.nextInt();
        double timer = input.nextDouble();
        trafficLightGroupTimer[group] = timer;
    }

    public double getTrafficLightGroupTimer(int group)
    {
        return trafficLightGroupTimer[group];
    }

    public void addMapRequest(EditorRoadChunk[][] map,EditorRoadChunk target){//to put road chunk into map, target is the one to put in the chunk
        if(map[target.getIDX()][target.getIDY()] == null)
            if(connectionJudge(map[target.getIDX()][target.getIDY()+1], target, SOUTH) && connectionJudge(map[target.getIDX()+1][target.getIDY()], target, WEST) && connectionJudge(map[target.getIDX()][target.getIDY()-1], target, NORTH) && connectionJudge(map[target.getIDX()-1][target.getIDY()], target, EAST))
            {
                map[target.getIDX()][target.getIDY()] = target;
                chunkCount+=1;
            }
        else if(chunkCount == 0)
        {
            map[target.getIDX()][target.getIDY()] = target;
            chunkCount+=1;
        }
    }

    public EditorRoadChunk getChunk(int ID_X, int ID_Y)
    {
        return map[ID_X][ID_Y];
    }

    public void connectionAdd(EditorRoadChunk[][] map, EditorRoadChunk target) {
        int x = target.getIDX();
        int y = target.getIDY();
        // Check and set connection for NORTH
    if (y + 1 < map[0].length && map[x][y + 1] != null && (target.getIntersection() & (1 << (NORTH - 1))) != 0 && (map[x][y + 1].getIntersection() & (1 << (SOUTH - 1))) != 0) {
        if (y < y + 1) {
            target.setConnection(NORTH);
        } else {
            map[x][y + 1].setConnection(SOUTH);
        }
    }

    // Check and set connection for EAST
    if (x + 1 < map.length && map[x + 1][y] != null && (target.getIntersection() & (1 << (EAST - 1))) != 0 && (map[x + 1][y].getIntersection() & (1 << (WEST - 1))) != 0) {
        if (x < x + 1) {
            target.setConnection(EAST);
        } else {
            map[x + 1][y].setConnection(WEST);
        }
    }

    // Check and set connection for SOUTH
    if (y - 1 >= 0 && map[x][y - 1] != null && (target.getIntersection() & (1 << (SOUTH - 1))) != 0 && (map[x][y - 1].getIntersection() & (1 << (NORTH - 1))) != 0) {
        if (y > y - 1) {
            target.setConnection(SOUTH);
        } else {
            map[x][y - 1].setConnection(NORTH);
        }
    }

    // Check and set connection for WEST
    if (x - 1 >= 0 && map[x - 1][y] != null && (target.getIntersection() & (1 << (WEST - 1))) != 0 && (map[x - 1][y].getIntersection() & (1 << (EAST - 1))) != 0) {
        if (x > x - 1) {
            target.setConnection(WEST);
        } else {
            map[x - 1][y].setConnection(EAST);
        }
    }

    // Check and set connection for NORTH-EAST
    if (x + 1 < map.length && y + 1 < map[0].length && map[x + 1][y + 1] != null) {
        if ((map[x][y + 1] != null || map[x + 1][y] != null) && (target.getIntersection() & (1 << (NE - 1))) != 0 && (map[x + 1][y + 1].getIntersection() & (1 << (SW - 1))) != 0) {
            if (x < x + 1 && y < y + 1) {
                target.setConnection(NE);
            } else {
                map[x + 1][y + 1].setConnection(SW);
            }
        }
    }

    // Check and set connection for SOUTH-EAST
    if (x + 1 < map.length && y - 1 >= 0 && map[x + 1][y - 1] != null) {
        if ((map[x][y - 1] != null || map[x + 1][y] != null) && (target.getIntersection() & (1 << (SE - 1))) != 0 && (map[x + 1][y - 1].getIntersection() & (1 << (NW - 1))) != 0) {
            if (x < x + 1 && y > y - 1) {
                target.setConnection(SE);
            } else {
                map[x + 1][y - 1].setConnection(NW);
            }
        }
    }

    // Check and set connection for SOUTH-WEST
    if (x - 1 >= 0 && y - 1 >= 0 && map[x - 1][y - 1] != null) {
        if ((map[x][y - 1] != null || map[x - 1][y] != null) && (target.getIntersection() & (1 << (SW - 1))) != 0 && (map[x - 1][y - 1].getIntersection() & (1 << (NE - 1))) != 0) {
            if (x > x - 1 && y > y - 1) {
                target.setConnection(SW);
            } else {
                map[x - 1][y - 1].setConnection(NE);
            }
        }
    }

    // Check and set connection for NORTH-WEST
    if (x - 1 >= 0 && y + 1 < map[0].length && map[x - 1][y + 1] != null) {
        if ((map[x][y + 1] != null || map[x - 1][y] != null) && (target.getIntersection() & (1 << (NW - 1))) != 0 && (map[x - 1][y + 1].getIntersection() & (1 << (SE - 1))) != 0) {
            if (x > x - 1 && y < y + 1) {
                target.setConnection(NW);
            } else {
                map[x - 1][y + 1].setConnection(SE);
            }
        }
    }
        
    }

    public int whichGraph(EditorRoadChunk target)//for UI to know which picture should be used for the chunk
    {
        return target.connectionStatus();
    }

    private boolean connectionJudge(EditorRoadChunk connectToChunk, EditorRoadChunk target, int facing) {
        if (connectToChunk != null && (connectToChunk.getIntersection() & (1 << (facing - 1))) != 0) {
            return true;
        }
        return false;
    }

    private void expandMap(EditorRoadChunk[][]map,EditorRoadChunk target){//expand map if the current new chunk is out of range
        EditorRoadChunk[][] newmap = new EditorRoadChunk[target.getIDX()+2][target.getIDY()+2];//to keep the margin clear so +2
        int[][] newTrafficLightGroup = new int[target.getIDX()+2][target.getIDY()+2];
        double[] newTrafficLightGroupTimer = new double[target.getIDX()+2];

        for(int i = 0 ; i < map.length ; i++)
        {
            for(int j = 0 ; j < map[i].length ; j++)
            {
                newmap[i][j]=map[i][j];
                newTrafficLightGroup[i][j] = trafficLightGroup[i][j];
            }
            newTrafficLightGroupTimer[i] = trafficLightGroupTimer[i];
        }
        map = newmap;
        trafficLightGroup = newTrafficLightGroup;
        trafficLightGroupTimer = newTrafficLightGroupTimer;
    }

    public FileManager exportData() 
    {
        FileManager fileManager = new FileManager();
        fileManager.setData(map, trafficLightGroup, trafficLightGroupTimer);
        return fileManager;
    }

    // read file from FileManager
    public void importData(FileManager fileManager) 
    {
        this.map = fileManager.getMap();
        this.trafficLightGroup = fileManager.getTrafficLightGroup();
        this.trafficLightGroupTimer = fileManager.getTrafficLightGroupTimer();
    }

}
