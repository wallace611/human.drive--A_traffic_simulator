package com.HDEngine.Editor;

import com.HDEngine.Editor.Object.Road.EditorRoadChunk;
import com.HDEngine.Editor.Object.TrafficLight.TrafficLight;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Map;
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
    /*private static final int INTERSECTION = 1;
    private static final int TRAFFICLIGHTFLAG = 2;
    private static final int TRAFFICLIGHTTIMER = 3;
    private static final int TRAFFICLIGHTGROUP = 4;
    private static final int SPEEDLIMIT = 5;
    private static final int IDX = 6;
    private static final int IDY = 7;
    private static final int STARTFLAG = 8;
    private static final int WEIGHTS = 9;
    private static final int CONNECTION = 10;*/
    private EditorRoadChunk[][] map = new EditorRoadChunk[22][22];//if possible, keep the outer side of the array clear(eg. map[0][0] should always be null)
    private EditorRoadChunk[] templateChunks = new EditorRoadChunk[10];
    private Map<Integer,int[]> trafficLight = new HashMap<>();
    private int chunkCount = 0;
    private transient Scanner input = new Scanner(System.in);

    public Editor()
    {
        initializemap();
        startEditing();
    }

    private void initializemap()//set the map to null to initiialize
    {
        for(int i = 0 ; i < map.length ; i++)
        {
            for(int j = 0 ; j < map.length ; j++)
            {
                map[i][j]=null;
            }
        }
    }

    public void startEditing()
    {
        String action;
        action = input.next();
        action.toLowerCase();
    }

    public void addNewChunk()//create a new chunk
    {
        EditorRoadChunk newRoadChunk = new EditorRoadChunk();
        int IDX=0,IDY=0;
        IDX = input.nextInt();
        IDY = input.nextInt();
        if(map[IDX][IDY] == null)
        {
            newRoadChunk.getData(IDX, IDY);
            map[IDX][IDY] = newRoadChunk;
            connectionAdd(map, newRoadChunk);
        }
        else
        {

        }
    }

    public void addNewChunk(int id,int IDX, int IDY) {
        EditorRoadChunk newRoadChunk = new EditorRoadChunk();
        newRoadChunk = templateChunks[id];
        map[IDX][IDY] = newRoadChunk;
        connectionAdd(map, newRoadChunk);
    }

    public void addToTrafficLightGroup(EditorRoadChunk target)//record the traffic light group at map
    {
        trafficLightGroup[target.getIDX()][target.getIDY()] = target.getTrafficLightGroup();
    }

    /*public void setTrafficLightGroupTimer()//set group timer
    {
        int group = input.nextInt();
        double timer = input.nextDouble();
        trafficLightGroupTimer[group] = timer;
    }*/

    public void setTrafficLightGroupTimer() {
        boolean valid = false;
        while(!valid)
        {    
            try {
                System.out.print("Enter traffic light group number: ");
                int group = input.nextInt();
                System.out.print("Enter timer for the group: ");
                double timer = input.nextDouble();
                trafficLightGroupTimer[group] = timer;
                valid = true;
            } catch (InputMismatchException e) {
                System.out.println("Input type mismatch. Please enter a valid number.");
                input.nextLine(); // clear the invalid input
            }    
        }
    }

    public double getTrafficLightGroupTimer(int group)
    {
        return trafficLightGroupTimer[group];
    }

    /*public void addMapRequest(EditorRoadChunk[][] map,EditorRoadChunk target){//to put road chunk into map, target is the one to put in the chunk
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
    }*/

    public void addMapRequest(EditorRoadChunk[][] map, EditorRoadChunk target) {
        if(map[target.getIDX()][target.getIDY()] == null) {
            if(connectionJudge(map[target.getIDX()][target.getIDY()+1], target, SOUTH) &&
                connectionJudge(map[target.getIDX()+1][target.getIDY()], target, WEST) &&
                connectionJudge(map[target.getIDY()][target.getIDY()-1], target, NORTH) &&
                connectionJudge(map[target.getIDX()-1][target.getIDY()], target, EAST)) {
                map[target.getIDX()][target.getIDY()] = target;
                chunkCount++;
            }
        } else if(chunkCount == 0) {
            map[target.getIDX()][target.getIDY()] = target;
            chunkCount++;
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
    if (y + 1 < map[0].length && map[x][y + 1] != null && (target.getIntersection()[NORTH] != 0) && (map[x][y + 1].getIntersection()[SOUTH] != 0)) {
        if (y < y + 1) {
            target.setConnection(NORTH);
        } else {
            map[x][y + 1].setConnection(SOUTH);
        }
    }

    // Check and set connection for EAST
    if (x + 1 < map.length && map[x + 1][y] != null && (target.getIntersection()[EAST]) != 0 && (map[x + 1][y].getIntersection()[WEST]) != 0) {
        if (x < x + 1) {
            target.setConnection(EAST);
        } else {
            map[x + 1][y].setConnection(WEST);
        }
    }

    // Check and set connection for SOUTH
    if (y - 1 >= 0 && map[x][y - 1] != null && (target.getIntersection()[SOUTH]) != 0 && (map[x][y - 1].getIntersection()[NORTH]) != 0) {
        if (y > y - 1) {
            target.setConnection(SOUTH);
        } else {
            map[x][y - 1].setConnection(NORTH);
        }
    }

    // Check and set connection for WEST
    if (x - 1 >= 0 && map[x - 1][y] != null && (target.getIntersection()[WEST]) != 0 && (map[x - 1][y].getIntersection()[EAST]) != 0) {
        if (x > x - 1) {
            target.setConnection(WEST);
        } else {
            map[x - 1][y].setConnection(EAST);
        }
    }

    // Check and set connection for NORTH-EAST
    if (x + 1 < map.length && y + 1 < map[0].length && map[x + 1][y + 1] != null) {
        if ((map[x][y + 1] != null || map[x + 1][y] != null) && (target.getIntersection()[NE]) != 0 && (map[x + 1][y + 1].getIntersection()[SW]) != 0) {
            if (x < x + 1 && y < y + 1) {
                target.setConnection(NE);
            } else {
                map[x + 1][y + 1].setConnection(SW);
            }
        }
    }

    // Check and set connection for SOUTH-EAST
    if (x + 1 < map.length && y - 1 >= 0 && map[x + 1][y - 1] != null) {
        if ((map[x][y - 1] != null || map[x + 1][y] != null) && (target.getIntersection()[SE]) != 0 && (map[x + 1][y - 1].getIntersection()[NW]) != 0) {
            if (x < x + 1 && y > y - 1) {
                target.setConnection(SE);
            } else {
                map[x + 1][y - 1].setConnection(NW);
            }
        }
    }

    // Check and set connection for SOUTH-WEST
    if (x - 1 >= 0 && y - 1 >= 0 && map[x - 1][y - 1] != null) {
        if ((map[x][y - 1] != null || map[x - 1][y] != null) && (target.getIntersection()[SW]) != 0 && (map[x - 1][y - 1].getIntersection()[NE]) != 0) {
            if (x > x - 1 && y > y - 1) {
                target.setConnection(SW);
            } else {
                map[x - 1][y - 1].setConnection(NE);
            }
        }
    }

    // Check and set connection for NORTH-WEST
    if (x - 1 >= 0 && y + 1 < map[0].length && map[x - 1][y + 1] != null) {
        if ((map[x][y + 1] != null || map[x - 1][y] != null) && (target.getIntersection()[NW]) != 0 && (map[x - 1][y + 1].getIntersection()[SE]) != 0) {
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
        if (connectToChunk != null && (connectToChunk.getIntersection()[facing]) != 0) {
            return true;
        }
        return false;
    }

    /*private void expandMap(EditorRoadChunk[][]map,EditorRoadChunk target){//expand map if the current new chunk is out of range
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
    }*/

    /*private void expandMap(EditorRoadChunk[][] map, EditorRoadChunk target) {
        EditorRoadChunk[][] newMap = new EditorRoadChunk[target.getIDX() + 2][target.getIDY() + 2];
        int[][] newTrafficLightGroup = new int[target.getIDX() + 2][target.getIDY() + 2];
        double[] newTrafficLightGroupTimer = new double[target.getIDX() + 2];

        for(int i = 0 ; i < map.length ; i++) {
            for(int j = 0 ; j < map[i].length ; j++) {
                newMap[i][j] = map[i][j];
                newTrafficLightGroup[i][j] = trafficLightGroup[i][j];
            }
            newTrafficLightGroupTimer[i] = trafficLightGroupTimer[i];
        }
        map = newMap;
        trafficLightGroup = newTrafficLightGroup;
        trafficLightGroupTimer = newTrafficLightGroupTimer;
    }*/

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

    public String assignPath(Scanner input) {
        System.out.print("\nEnter the file path where you want to save the data:");
        String path = input.nextLine();

        System.out.print("\nEnter the file name:");
        String fileName = input.nextLine();

        return path + "\\" + fileName+".obj";
    }

/* 
    public void updateRoadParameter(String buttonId, int parameterIndex, String newValue) {
        int id = Integer.parseInt(buttonId);
        if (templateChunks[id] != null) {
            try {
                switch (parameterIndex) {
                    case INTERSECTION:
                        // 需修改：處理 intersection 陣列//TODO
                        break;
                    case TRAFFICLIGHTFLAG:
                        templateChunks[id].setTrafficLightFlag(Boolean.parseBoolean(newValue));
                        break;
                    case TRAFFICLIGHTTIMER:
                        templateChunks[id].setTrafficLightTimer(Double.parseDouble(newValue));
                        break;
                    case TRAFFICLIGHTGROUP:
                        templateChunks[id].setTrafficLightGroup(Integer.parseInt(newValue));
                        break;
                    case SPEEDLIMIT:
                        templateChunks[id].setSpeedLimit(Double.parseDouble(newValue));
                        break;
                    case IDX:
                        templateChunks[id].setIdX(Integer.parseInt(newValue));
                        break;
                    case IDY:
                        templateChunks[id].setIdY(Integer.parseInt(newValue));
                        break;
                    case STARTFLAG:
                        templateChunks[id].setStartFlag(Boolean.parseBoolean(newValue));
                        break;
                    case WEIGHTS:
                        // 需修改：處理 weights 陣列 //TODO
                        break;
                    case CONNECTION:
                        // 需修改：處理 connection 陣列//TODO
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input for parameter " + parameterIndex + ": " + newValue);
            }
        }
    }

    public void updateTrafficLightParameter(String buttonId, int parameterIndex, String newValue) {
        int id = Integer.parseInt(buttonId);
        if (templateChunks[id] != null) {
            try {
                switch (parameterIndex) {
                    case INTERSECTION:
                        // 需修改：處理 intersection 陣列//TODO
                        break;
                    case TRAFFICLIGHTFLAG:
                        templateChunks[id].setTrafficLightFlag(Boolean.parseBoolean(newValue));
                        break;
                    case TRAFFICLIGHTTIMER:
                        templateChunks[id].setTrafficLightTimer(Double.parseDouble(newValue));
                        break;
                    case TRAFFICLIGHTGROUP:
                        templateChunks[id].setTrafficLightGroup(Integer.parseInt(newValue));
                        break;
                    case SPEEDLIMIT:
                        templateChunks[id].setSpeedLimit(Double.parseDouble(newValue));
                        break;
                    case IDX:
                        templateChunks[id].setIdX(Integer.parseInt(newValue));
                        break;
                    case IDY:
                        templateChunks[id].setIdY(Integer.parseInt(newValue));
                        break;
                    case STARTFLAG:
                        templateChunks[id].setStartFlag(Boolean.parseBoolean(newValue));
                        break;
                    case WEIGHTS:
                        // 需修改：處理 weights 陣列
                        break;
                    case CONNECTION:
                        // 需修改：處理 connection 陣列
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input for parameter " + parameterIndex + ": " + newValue);
            }
        }
    }
*/
    public void deleteChunk(int IDX , int IDY)
    {
        map[IDX][IDY] = null;
    }

}
