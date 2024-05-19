package com.HDEngine.Editor;

import com.HDEngine.Editor.Object.Road.EditorRoadChunk;
import java.util.Scanner;
import java.io.*;
import com.HDEngine.Utilities.FileManager;

public class Editor implements Serializable
{
    private static final int NORTH = 2;
    private static final int EAST = 0;
    private static final int SOUTH = 6;
    private static final int WEST = 4;
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

    public void connectionAdd(EditorRoadChunk[][] map,EditorRoadChunk target)//change the connection in the road chunk
    {
        if(target.getConnection()[NORTH] == 0 && map[target.getIDX()][target.getIDY()+1] != null)//north of target
        {
            target.setConnection(NORTH);
            map[target.getIDX()][target.getIDY()].setConnection(SOUTH);
        }
        if(target.getConnection()[EAST] == 0 && map[target.getIDX()+1][target.getIDY()] != null)//north of target
        {
            target.setConnection(EAST);
            map[target.getIDX()][target.getIDY()].setConnection(WEST);
        }
        if(target.getConnection()[SOUTH] == 0 && map[target.getIDX()][target.getIDY()-1] != null)//north of target
        {
            target.setConnection(SOUTH);
            map[target.getIDX()][target.getIDY()].setConnection(NORTH);
        }
        if(target.getConnection()[WEST] == 0 && map[target.getIDX()-1][target.getIDY()] != null)//north of target
        {
            target.setConnection(WEST);
            map[target.getIDX()][target.getIDY()].setConnection(EAST);
        }
    }

    private boolean connectionJudge(EditorRoadChunk connectToChink,EditorRoadChunk target, int facing)//check is the connection between check is legal (fasing is the fasing of the original chunk)
    {// 0<-->4   2<-->6    (the road have to be open on both side)
        if((connectToChink == null)  || (connectToChink.getIntersection() & (1 << (facing-1))) != 0)
        {
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
