package com.HDEngine.Editor.Object.Road;

import java.io.*;
import java.util.*;

public class EditorRoadChunk implements Serializable
{
    private int[] intersection = new int[8];//using bit refence to record where is the road going
    private boolean trafficLightFlag;//have or dont have traffic light
    private double trafficLightTimer;//timer of traffic light(this part should follow group, still working on it)
    private byte trafficLightPosition;//where is the traffic light
    private int trafficLightGroup;// the group
    private double speedLimit;//the speed limit (if = -1 then there is no speed limit on this chunk)
    private int idX;//to seperate differe road chunk
    private int idY;
    private boolean startFlag;//is this the start point
    private double[] weights = new double[8];//recording the weight of different road
    private int[] connection = new int[8];//to record where is the road going (if data==0 -> no)(if data == 1  ==  the road is connected)(1 is on the right hand side, clockwise)
    transient Scanner input = new Scanner(System.in);

    public void getData()
    {
        System.out.print("\nID\n");
        setID();
        System.out.print("\nStart\n");
        setStartPoint();
        System.out.print("\nSpeedlimit");
        setSpeedLimit();
        System.out.print("\nintersection");
        setIntersection();
        System.out.print("\nTrafficLight");
        setTrafficLight();
        for(int i = 0 ; i < 8 ;  i++)
        {
            connection[i] = 0;
        }
    }

    public void setIntersection() {
        boolean valid = false;
        while(!valid)
        {
            for (int i = 0; i < 8; i++) {
                switch (i) {
                    case 0:System.out.print("Does East side have intersection (Please input True or False):");break;
                    case 1:System.out.print("Is switching lanes towards North-East allowed? (Please input True or False):");break;
                    case 2:System.out.print("Does North side have intersection (Please input True or False):");break;
                    case 3:System.out.print("Is switching lanes towards North-West allowed? (Please input True or False):");break;
                    case 4:System.out.print("Does West side have intersection (Please input True or False):");break;
                    case 5:System.out.print("Is switching lanes towards South-West allowed? (Please input True or False):");break;
                    case 6:System.out.print("Does South side have intersection (Please input True or False):");break;
                    case 7:System.out.print("Is switching lanes towards South-East allowed? (Please input True or False):");break;
                }
                try
                {
                    String input = this.input.next(); // Read user input
                    if (input.equalsIgnoreCase("true")  || input.equalsIgnoreCase("t")) {
                        // Set the bit corresponding to the direction
                        this.intersection[i] = 1; // Shift bit to positions 0, 2, 4, and 6
                    } else 
                    {
                        // Optionally clear the bit if you want to reset it
                        this.intersection[i] = 0;;
                    }
                    if(i == 7)
                        valid = true;
                } catch(InputMismatchException e)
                {
                    input.nextLine();
                    System.out.println("Input was not a valid string!");
                }
            }
        }
    }
    
    public void setTrafficLight()//1 = have traffic light , set whick group its in
    {
        boolean valid = false;
        while (!valid) 
        {
            try
            {
                System.out.print("Is there a traffic light in this chunk(1 = true , other number = false)");
                int flag = input.nextInt();
                valid = true;
                if(flag == 1)
                {
                    this.trafficLightFlag = true;
                    this.trafficLightGroup = input.nextInt();
                }
                else
                    return;
            }catch(InputMismatchException e)
            {
                input.nextLine();
                System.out.println("input tpye missmatch");
            }
        }
    }

    public void setTrafficLightTimer(double timer){// set traffic light group, the record of group will be in the main function
        this.trafficLightTimer = timer;
    }

    public void setSpeedLimit(){
        boolean valid = false;
        while (!valid)
        {
            try
            {
                System.out.println("please input speed limit for this chunk( input >0  = valid ,input <=0 = no speed limit)");
                double limit = input.nextDouble();
                valid = true;
                if( limit > 0)
                    this.speedLimit = limit;
                else
                    this.speedLimit = -1;//-1 = didn't set limit;
            }catch(InputMismatchException e)
            {
                input.nextLine();
                System.out.println("input tpye missmatch");
            }
        }
    }

    public void setID(){
        boolean valid = false;
        while (!valid)
            {
            try{
                System.out.print("please input the coordinate of this chunk:");
                this.idX = input.nextInt();
                this.idY = input.nextInt();
                valid = true;
            }catch(InputMismatchException e)
            {
                input.nextLine();
                System.out.println("input must be an integer");
            }
        }
    }

    public void setStartPoint()
    {
        boolean valid = false;
        while (!valid)
        {
            try {
                System.out.print("Is this chunk the start point of the car (Please enter True or False): ");
                String ref = this.input.next();
                ref = ref.toLowerCase();
                if (ref.equals("true") || ref.equals("t")) {
                    this.startFlag = true;
                } else if (ref.equals("false") || ref.equals("f")) {
                    this.startFlag = false;
                } else {
                    throw new InputMismatchException();
                }
                valid = true;
            } catch (InputMismatchException e) {
                System.out.println("input type mismatch");
                input.nextLine(); // Clear the invalid input from the scanner buffer
            }
        }
    }

    public void setWeight(double weight,int position){
        boolean valid = false;
        while(!valid)
        {
            try 
            {
                this.weights[position] = weight;
                valid = true;
            } catch (InputMismatchException e) {
                System.out.println("wrong input");
            }
        }
    }
    
    public void setConnection(int facing){
        this.connection[facing] = 1;
    }

    public int[] getIntersection(){
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

    public double[] getWeight(){
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

    public int connectionStatus() {
        boolean north = connection[2] == 1;
        boolean south = connection[6] == 1;
        boolean east = connection[0] == 1;
        boolean west = connection[4] == 1;

        if (north && !south && !east && !west) return 1;
        if (!north && south && !east && !west) return 2;
        if (!north && !south && east && !west) return 3;
        if (!north && !south && !east && west) return 4;

        if (north && east) return 5;
        if (north && south) return 6;
        if (north && west) return 7;
        if (south && east) return 8;
        if (south && west) return 9;
        if (east && west) return 10;

        if (north && east && south) return 11;
        if (north && east && west) return 12;
        if (east && south && west) return 13;
        if (north && south && east && west) return 14;

        return -1; // If no connection matches
    }

    @Override
    public String toString() {
        return "Intersection = " + Arrays.toString(intersection) +
                ", ID = " + idX + "," + idY +
                ", Start = " + startFlag +
                ", Speed limit = " + speedLimit +
                ", Traffic light flag = " + trafficLightFlag +
                ", Connection status = " + connectionStatus();
    }

    public void setWeight() 
    {
        double weight = 1.0; 
        for (int i = 0; i < 8; i++) 
        {
            if (connection[i] == 1) 
            {
                weight = input.nextDouble();
                this.weights[i] =weight;
            }
        }
    }

}
