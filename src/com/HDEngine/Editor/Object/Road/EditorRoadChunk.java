package com.HDEngine.Editor.Object.Road;

import java.util.Scanner;
import java.io.*;

public class EditorRoadChunk implements Serializable
{
    private byte intersection;//using bit refence to record where is the road going
    private boolean trafficLightFlag;//have or dont have traffic light
    private double trafficLightTimer;//timer of traffic light(this part should follow group, still working on it)
    private byte trafficLightPosition;//where is the traffic light
    private int trafficLightGroup;// the group
    private double speedLimit;//the speed limit (if = -1 then there is no speed limit on this chunk)
    private int idX;//to seperate differe road chunk
    private int idY;
    private boolean startFlag;//is this the start point
    private double weights;//recording the weight of different road
    private int[] connection = new int[8];//to record where is the road going (if data==0 -> no)(if data == 1  ==  the road is connected)(1 is on the right hand side, clockwise)
    transient Scanner input = new Scanner(System.in);

    public void getData()
    {
        System.out.print("ID:");
        setID();
        System.out.print("Start:");
        setStartPoint();
        System.out.print("Speedlimit:");
        setSpeedLimit();
        System.out.print("intersection:");
        setIntersection();
        System.out.print("TrafficLight:");
        setTrafficLight();
        System.out.print("Weight:");
        setWeight();
        for(int i = 0 ; i < 8 ;  i++)
        {
            connection[i] = 0;
        }
    }

    public void setIntersection() {
        int[] directions = {1, 3, 5, 7}; // Specific directions to handle
        for (int i = 0; i < directions.length; i++) {
            int input = this.input.nextInt(); // Read user input
            if (input == 1) {
                // Set the bit corresponding to the direction
                this.intersection |= (1 << (2 * i)); // Shift bit to positions 0, 2, 4, and 6
            } else 
            {
                // Optionally clear the bit if you want to reset it
                this.intersection &= ~(1 << (2 * i));
            }
        }
    }
    
    public void setTrafficLight()//1 = have traffic light , set whick group its in
    {
        int flag = input.nextInt();
        if(flag == 1)
        {
            this.trafficLightFlag = true;
            this.trafficLightGroup = input.nextInt();
        }
        else
            return;
    }

    public void setTrafficLightTimer(double timer){// set traffic light group, the record of group will be in the main function
        this.trafficLightTimer = timer;
    }

    public void setSpeedLimit(){
        double limit = input.nextDouble();
        if( limit > 0)
            this.speedLimit = limit;
        else
            this.speedLimit = -1;//-1 = didn't set limit;
    }

    public void setID(){
        this.idX = input.nextInt();
        this.idY = input.nextInt();
    }

    public void setStartPoint()
    {
        int ref = this.input.nextInt();
        if(ref == 1 )
            this.startFlag = true;
        else
            this.startFlag = false;
    }

    public void setWeight(){
        this.weights = this.input.nextDouble();
    }
    
    public void setConnection(int facing){
        this.connection[facing] = 1;
    }

    public byte getIntersection(){
        return intersection;
    }

    public boolean haveTrafficLight(){
        return trafficLightFlag;
    }

    public double getTrafficLightTimer()
    {
        if(haveTrafficLight())
        {
            return trafficLightTimer;
        }
        return -1;
    }

    public byte getTrafficLightPosition(){
        return trafficLightPosition;
    }

    public int getTrafficLightGroup()
    {
        if(haveTrafficLight())
        {
            return trafficLightGroup;
        }
        return -1;
    }

    public double getSpeedLimit(){
        return speedLimit;
    }

    public int getIDX(){
        return idX;
    }

    public int getIDY(){
        return idY;
    }

    public boolean startCheck(){
        return startFlag;
    }

    public double getWeight(){
        return weights;
    }

    public int[] getConnection(){
        return connection;
    }

    public boolean outOfMap(EditorRoadChunk[][] map)//if the target location is out of the current map size ,return true to editor
    {
        int lenthOfArray = map.length;
        int widthOfArray = map[0].length;
        if(idX > lenthOfArray || idY > widthOfArray)
        {
            return true;
        }
        return false;
    }

    @Override
    public String toString()
    {
        return "intersection =" + intersection + " , ID = " + idX + "," + idY + " , start = " + startFlag + ", Weight = " + weights + ", Speed limit = " + speedLimit + " ,traffic light falg = " + trafficLightFlag;
    }
}
