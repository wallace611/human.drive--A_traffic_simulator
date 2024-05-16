package com.HDEngine.Editor;

import com.HDEngine.Editor.Object.Road.EditorRoadChunk;
import java.util.Scanner;

public class Editor 
{
    private static final int NORTH_FACE = 2;
    private static final int EAST_FACE = 0;
    private static final int SOUTH_FACE = 6;
    private static final int WEST_FACE = 4;
    private EditorRoadChunk[][] map = new EditorRoadChunk[102][102];//if possible, keep the outer side of the array clear(eg. map[0][0] should always be null)
    private int[][] traffic_light_group = new int[102][102];//if(is same group){[a][a] = [b][b]for road_a and road_b}
    private double[] traffic_light_group_timer = new double[102];
    private int chunkCount = 0;
    private Scanner input = new Scanner(System.in);

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
                traffic_light_group[i][j] = 0;  
                
            }
            traffic_light_group_timer[i] = 0;
        }
    }

    public void addNewChink()//create a new chunk
    {
        EditorRoadChunk newRoadChunk = new EditorRoadChunk();
        newRoadChunk.getData();//get ID,StartPoint,speedLimit,intersection,traffic light,weight to other road
        if(newRoadChunk.outOfMap(map))//the ID is out of map
            expandMap(map,newRoadChunk);
        if(newRoadChunk.haveTrafficLight())//there is a traffic light in this chunk
        {
            int group = newRoadChunk.getTrafficLightGroup();//which group is the traffic light in
            addToTrafficLightGroup(newRoadChunk);
            newRoadChunk.setTrafficLightTimer(getTrafficLightGroupTimer(group));//set the data of traffic(timer)
        }
    }

    public void addToTrafficLightGroup(EditorRoadChunk target)//record the traffic light group at map
    {
        traffic_light_group[target.getIDX()][target.getIDY()] = target.getTrafficLightGroup();
    }

    public void setTrafficLightGroupTimer()//set group timer
    {
        int group = input.nextInt();
        double timer = input.nextDouble();
        traffic_light_group_timer[group] = timer;
    }

    public double getTrafficLightGroupTimer(int group)
    {
        return traffic_light_group_timer[group];
    }

    public void addMapRequest(EditorRoadChunk[][] map,EditorRoadChunk target){//to put road chunk into map, target is the one to put in the chunk
        if(map[target.getIDX()][target.getIDY()] == null)
            if(connectionJudge(map[target.getIDX()][target.getIDY()+1], target, SOUTH_FACE) && connectionJudge(map[target.getIDX()+1][target.getIDY()], target, WEST_FACE) && connectionJudge(map[target.getIDX()][target.getIDY()-1], target, NORTH_FACE) && connectionJudge(map[target.getIDX()-1][target.getIDY()], target, EAST_FACE))
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

    public void connectionAdd(EditorRoadChunk[][] map,EditorRoadChunk target)//change the connection in the road chunk
    {
        if(target.getConnection()[NORTH_FACE] == 0 && map[target.getIDX()][target.getIDY()+1] != null)//north of target
        {
            target.setConnection(NORTH_FACE);
            map[target.getIDX()][target.getIDY()].setConnection(SOUTH_FACE);
        }
        if(target.getConnection()[EAST_FACE] == 0 && map[target.getIDX()+1][target.getIDY()] != null)//north of target
        {
            target.setConnection(EAST_FACE);
            map[target.getIDX()][target.getIDY()].setConnection(WEST_FACE);
        }
        if(target.getConnection()[SOUTH_FACE] == 0 && map[target.getIDX()][target.getIDY()-1] != null)//north of target
        {
            target.setConnection(SOUTH_FACE);
            map[target.getIDX()][target.getIDY()].setConnection(NORTH_FACE);
        }
        if(target.getConnection()[WEST_FACE] == 0 && map[target.getIDX()-1][target.getIDY()] != null)//north of target
        {
            target.setConnection(WEST_FACE);
            map[target.getIDX()][target.getIDY()].setConnection(EAST_FACE);
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
        int[][] new_traffic_light_group = new int[target.getIDX()+2][target.getIDY()+2];
        double[] new_traffic_light_group_timer = new double[target.getIDX()+2];

        for(int i = 0 ; i < map.length ; i++)
        {
            for(int j = 0 ; j < map[i].length ; j++)
            {
                newmap[i][j]=map[i][j];
                new_traffic_light_group[i][j] = traffic_light_group[i][j];
            }
            new_traffic_light_group_timer[i] = traffic_light_group_timer[i];
        }
        map = newmap;
        traffic_light_group = new_traffic_light_group;
        traffic_light_group_timer = new_traffic_light_group_timer;
    }

}
