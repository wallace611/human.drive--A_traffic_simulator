package com.HDEngine.Editor;

import com.HDEngine.Editor.Object.Road.EditorRoadChunk;

public class Editor 
{
    private EditorRoadChunk[][] map = new EditorRoadChunk[102][102];//if possible, keep the outer side of the array clear(eg. map[0][0] should always be null)
    private int chunkCount = 0;
    public Editor()
    {
        initializemap();
    }

    private void initializemap()//set the map to null to initiialize
    {
        for(int i = 0 ; i < 100 ; i++)
        {
            for(int j = 0 ; j<100 ; j++)
            {
                map[i][j]=null;
            }
        }
    }

    public void addNewChink()//create a new chunk
    {
        EditorRoadChunk newRoadChunk = new EditorRoadChunk();
        newRoadChunk.getData();
        if(newRoadChunk.outOfMap(map))
            expandMap(map);
    }

    public void connectionRequest(EditorRoadChunk[][] map,EditorRoadChunk target){//to put road chunk into map, target is the one to put in the chunk
        if(map[target.getIDX()][target.getIDY()] == null)
            if(connectionJudge(map[target.getIDX()][target.getIDY()+1], target, 6) && connectionJudge(map[target.getIDX()+1][target.getIDY()], target, 4) && connectionJudge(map[target.getIDX()][target.getIDY()-1], target, 2) && connectionJudge(map[target.getIDX()-1][target.getIDY()], target, 0))
            {
                map[target.getIDX()][target.getIDY()] = target;
                chunkCount+=1;
            }
        else if(chunkCount =0)
        {
            map[target.getIDX()][target.getIDY()] = target;
            chunkCount+=1;
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
        for(int i = 0 ; i < map.length ; i++)
        {
            for(int j = 0 ; j < map[i].length ; j++)
            {
                newmap[i][j]=map[i][j];
            }
        }
        map = newmap;
    }
}
