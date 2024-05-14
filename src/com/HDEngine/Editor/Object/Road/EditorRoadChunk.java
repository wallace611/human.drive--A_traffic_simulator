package com.HDEngine.Editor.Object.Road;

import java.util.Scanner;

public class EditorRoadChunk 
{
    private byte intersection;//using bit refence to record where is the road going
    private boolean traffic_light_flag;//have or dont have traffic light
    private double traffic_light_timer;//timer of traffic light(this part should follow group, still working on it)
    private byte traffic_light_position;//where is the traffic light
    private int traffic_light_group;// the group
    private double speed_limit;//the speed limit (if = -1 then there is no speed limit on this chunk)
    private int ID_X;//to seperate differe road chunk
    private int ID_Y;
    private boolean start_flag;//is this the start point
    private double weights;//recording the weight of different road
    private int[] connection = new int[8];//to record where is the road going (if data==null -> no)(if data == someone's ID  ==  the road is connected)(1 is on the right hand side, clockwise)
    Scanner input = new Scanner(System.in);

    public void getData()
    {
        setIntersection();
        setTraffic_light();
        setSpeedLimit();
        setID();
        setStartPoint();
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
    
    public void setTraffic_light()//1 = have traffic light 
    {
        int flag = input.nextInt();
        if(flag == 1)
        {
            this.traffic_light_flag = true;
            this.traffic_light_timer = this.input.nextInt();
        }
        else
            return;
    }

    public void setTrafficLightGroup(){// set traffic light group, the record of group will be in the main function
        this.traffic_light_group=this.input.nextInt();
    }

    public void setTrafficLightPosition(int facing)//    **todo**  still thinking how to record**
    {

    }

    public void setSpeedLimit(){
        double limit = input.nextDouble();
        if( limit > 0)
            this.speed_limit = limit;
        else
            this.speed_limit = -1;//-1 = didn't set limit;
    }

    public void setID(){
        this.ID_X = input.nextInt();
        this.ID_Y = input.nextInt();
    }

    public void setStartPoint()
    {
        int ref = this.input.nextInt();
        if(ref == 1 )
            this.start_flag = true;
        else
            this.start_flag = false;
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
        return traffic_light_flag;
    }

    public double getTrafficLightTimer()
    {
        if(haveTrafficLight())
        {
            return traffic_light_timer;
        }
        return -1;
    }

    public byte getTrafficLightPosition(){
        return traffic_light_position;
    }

    public int getTrafficLightGroup()
    {
        if(haveTrafficLight())
        {
            return traffic_light_group;
        }
        return -1;
    }

    public double getSpeedLimit(){
        return speed_limit;
    }

    public int getIDX(){
        return ID_X;
    }

    public int getIDY(){
        return ID_Y;
    }

    public boolean startCheck(){
        return start_flag;
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
        if(ID_X > lenthOfArray || ID_Y > widthOfArray)
        {
            return true;
        }
        return false;
    }
}
