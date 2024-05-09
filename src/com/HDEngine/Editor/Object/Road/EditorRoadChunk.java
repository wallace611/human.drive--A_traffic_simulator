package com.HDEngine.Editor.Object.Road;

import java.util.Arrays;

public class EditorRoadChunk 
{
    private final byte intersection;//using bit refence to record where is the road going
    private final double speed_limit;
    private final int ID;//to seperate differe road chunk
    private int[] weights =  new int[8];//recording the weight of different road
    private int[] connection = new int[8];//to record where is the road going (if data==null -> no)(if data == someone's ID  ==  the road is connected)(1 is on the right hand side, clockwise)
    

    public EditorRoadChunk(byte intersection,int[] weight, int ID , double speed_limit)
    {
        this.intersection = intersection;
        this.weights = Arrays.copyOf(weight,weight.length);
        this.ID = ID;
        this.speed_limit = speed_limit;
    }

    public int getID()
    {
        return ID;
    }

    public double getSpeedLimit()
    {
        return speed_limit;
    }

    public byte getIntersection()
    {
        return intersection;
    }

    public int[] getWeight()
    {
        return weights;
    }

    public void connectionRequest(EditorRoadChunk targetChunk, int fasing,int weight)
    {
        if(connectionCheck(targetChunk, fasing))
        {
            connection[fasing] = targetChunk.getID();
            weights[fasing] = weight;
        }
    }

    private boolean connectionCheck(EditorRoadChunk targetChunk, int fasing)//check is the connection between check is legal (fasing is the fasing of the original chunk)
    {// 1<-->5    2<-->6    3<-->7    4<-->8    (the road have to be open on both side)
        int intersectionChecker = 1 << fasing;
        if( (intersection&intersectionChecker) !=0 )
        {
            switch (fasing) 
            {
                case 1:
                    intersectionChecker = 1 << 4;//1+4=5th bit
                    if((targetChunk.getIntersection() & intersectionChecker) != 0)
                        return true;
                    else
                        return false;

                case 2:
                    intersectionChecker = 1 << 5;//1+5=6th bit
                    if((targetChunk.getIntersection() & intersectionChecker) != 0)
                        return true;
                    else
                        return false;
                case 3:
                    intersectionChecker = 1 << 6;//1+6=7th bit
                    if((targetChunk.getIntersection() & intersectionChecker) != 0)
                        return true;
                    else
                        return false;

                case 4:
                    intersectionChecker = 1 << 7;//1+7=8th bit
                    if((targetChunk.getIntersection() & intersectionChecker) != 0)
                        return true;
                    else
                        return false;

                case 5:
                    intersectionChecker = 1; // at first bit
                    if((targetChunk.getIntersection() & intersectionChecker) != 0)
                        return true;
                    else
                        return false;

                case 6:
                    intersectionChecker = 1 << 1;//1+1=2nd bit
                    if((targetChunk.getIntersection() & intersectionChecker) != 0)
                        return true;
                    else
                        return false;

                case 7:
                    intersectionChecker = 1 << 2;//1+2=3rd bit
                    if((targetChunk.getIntersection() & intersectionChecker) != 0)
                        return true;
                    else
                        return false;

                case 8:
                    intersectionChecker = 1 << 3;//1+3=4th bit
                    if((targetChunk.getIntersection() & intersectionChecker) != 0)
                        return true;
                    else
                        return false;
            }
        }
        return false;
    }
}
