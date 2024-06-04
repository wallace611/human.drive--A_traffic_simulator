package com.HDEngine.Editor.Object.Road;

import java.io.*;
import java.util.*;
import com.HDEngine.Editor.Object.TrafficLight.TrafficLight;

import processing.app.syntax.InputHandler.repeat;

public class EditorRoadChunk implements Serializable
{
    private int idX;//to seperate differe road chunk
    private int idY;
    private boolean startFlag;//is this the start point
    private double speedLimit;//the speed limit (if = -1 then there is no speed limit on this chunk)
    private int[] direction = new int[8];//using bit refence to record where is the road going
    private boolean isWeighted;
    private double[] weights = new double[8];//recording the weight of different road
    private int[] connection = new int[8];//to record where is the road going (if data==0 -> no)(if data == 1  ==  the road is connected)(1 is on the right hand side, clockwise)
    transient Scanner input = new Scanner(System.in);
    private EditorRoadChunk[] connected =new EditorRoadChunk[8];//do it last
    private boolean trafficLightFlag;
    private TrafficLight trafficLight;
    private boolean intersection;

    public void getData(int IDX, int IDY)
    {
        System.out.println("Setting axis...");
        setID(IDX,IDY);
        System.out.println("Setting start point...");
        setStartPoint();
        System.out.println("Setting speed limit...");
        setStartPoint();
        System.out.println("Setting direction...");
        setDirection();
        System.out.println("Setting weight...");
        setWeight();
        System.out.println("Setting traffic light info...");
        setTrafficLight();
        System.out.println("Setting intersection info...");
        setTrafficLight();
        System.out.println("done!! Adding the current chunk to map...");
        for(int i = 0 ; i < 8 ;  i++)
        {
            connection[i] = 0;
        }
    }

    public void setID(int IDX, int IDY) {
        boolean valid = false;
        while (!valid) {
            try {
                if (idX > 20 || idX <= 0 || idY > 20 || idY <= 0) 
                {
                    throw new IllegalArgumentException("Coordinates must be in the range of 1 to 20.");
                }
                else
                {
                    this.idX = IDX;
                    this.idY = IDY;
                    valid = true;
                }
            } catch (InputMismatchException e) {
                input.nextLine(); // Clear the invalid input
                System.out.println("Input must be an integer.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
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

    public void setSpeedLimit(){
        boolean valid = false;
        while (!valid)
        {
            try
            {
                System.out.println("please input speed limit for this chunk( input > 0  = valid ,input <=0 = no speed limit):");
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

    public void setDirection() {
        boolean valid = false;
        while(!valid)
        {
            for (int i = 0; i < 8; i++) {
                switch (i) {
                    case 0:System.out.print("Can this chunc drive towards East (Please input True or False):");break;
                    case 1:System.out.print("Is switching lanes towards North-East allowed? (Please input True or False):");break;
                    case 2:System.out.print("Can this chunc drive towards North (Please input True or False):");break;
                    case 3:System.out.print("Is switching lanes towards North-West allowed? (Please input True or False):");break;
                    case 4:System.out.print("Can this chunc drive towards West (Please input True or False):");break;
                    case 5:System.out.print("Is switching lanes towards South-West allowed? (Please input True or False):");break;
                    case 6:System.out.print("Can this chunc drive towards South(Please input True or False):");break;
                    case 7:System.out.print("Is switching lanes towards South-East allowed? (Please input True or False):");break;
                }
                try
                {
                    String input = this.input.next();
                    if (input.equalsIgnoreCase("true")  || input.equalsIgnoreCase("t")) 
                    {
                        this.direction[i] = 1;
                    } 
                    else if(input.equalsIgnoreCase("flase")|| input.equalsIgnoreCase("f"))
                    {
                        this.direction[i] = 0;;
                    }
                    else 
                    {
                        throw new InputMismatchException();
                    }
                    if(i == 7)
                        valid = true;
                } 
                catch(InputMismatchException e)
                {
                    input.nextLine();
                    System.out.println("Input was not a valid string!");
                }
            }
        }
    }
    
    public void setWeight() {
        boolean valid = false;
        while (!valid) {
            try {
                System.out.print("Is the chunk weighted? (Please input True or False): ");
                String ref = this.input.next();
                ref = ref.toLowerCase();
                if (ref.equals("true") || ref.equals("t")) {
                    isWeighted = true;
                    for (int i = 0; i < 8; i++) {
                        if (direction[i] == 1) 
                        {
                            switch (i) 
                            {
                                case 0:System.out.print("Please input weight for East side (the number can be float): ");break;
                                case 1:System.out.print("Please input weight for North-East direction (the number can be float): ");break;
                                case 2:System.out.print("Please input weight for North side (the number can be float): ");break;
                                case 3:System.out.print("Please input weight for North-West direction (the number can be float): ");break;
                                case 4:System.out.print("Please input weight for West side (the number can be float): ");break;
                                case 5:System.out.print("Please input weight for South-West direction (the number can be float): ");break;
                                case 6:System.out.print("Please input weight for South side (the number can be float): ");break;
                                case 7:System.out.print("Please input weight for South-East direction (the number can be float): ");break;
                            }
                            weights[i] = input.nextDouble();
                        } 
                        else 
                        {
                            weights[i] = 1.0;
                        }
                    }
                } 
                else 
                {
                    isWeighted = false;
                    for (int i = 0; i < 8; i++) 
                    {
                        weights[i] = 1.0;
                    }
                }
                valid = true;
            } catch (InputMismatchException e) {
                input.nextLine();
                System.out.println("Input was not valid. Please enter a number.");
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
                System.out.print("Is there a traffic light in this chunk(Please enter True or False):");
                String flag = input.nextLine().toLowerCase();
                if(flag.equals("true") || flag.equals("t"))
                {
                    this.trafficLightFlag = true;
                    trafficLight = new TrafficLight();
                    System.out.print("Which group is this chunk in(0 ~ the max group in the traffic light system):");
                    trafficLight.setTrafficLightGroup(input.nextInt());
                    System.out.print("Which team is this chunk in(0 ~ the max team in the traffic light group):");
                    trafficLight.setTrafficLightTeams(input.nextInt());
                    valid = true;
                }
                else if(flag.equals("false") || flag.equals("f"))
                {
                    this.trafficLightFlag = false;
                    valid = true;
                    return;
                }
                else
                {
                    throw new InputMismatchException();
                }
            }catch(InputMismatchException e)
            {
                input.nextLine();
                System.out.println("\ninput missmatch!");
            }
        }
    }

    public void setConnection(int facing){
        this.connection[facing] = 1;
    }

    public void setIntersection()
    {
        boolean valid = false;
        while (!valid)
        {
            try {
                System.out.print("Is this chunk the effected by traffic light (Please enter True or False): ");
                String ref = this.input.next();
                ref = ref.toLowerCase();
                if (ref.equals("true") || ref.equals("t")) {
                    this.intersection = true;
                } else if (ref.equals("false") || ref.equals("f")) {
                    this.intersection = false;
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

    public int getIDX(){
        return idX;
    }

    public int getIDY(){
        return idY;
    }

    public boolean startCheck(){
        return startFlag;
    }

    public double getSpeedLimit(){
        return speedLimit;
    }

    public int[] getDirection(){
        return direction;
    }

    public boolean isWeighted(){
        return isWeighted;
    }

    public double[] getWeight(){
        return weights;
    }

    public boolean haveTrafficLight(){
        return trafficLightFlag;
    }

    public int getTrafficLightGroup()
    {
        if(haveTrafficLight())
        {
            return trafficLight.getTrafficLightGroup();
        }
        return -1;
    }

    public int getTrafficLightTeams()
    {
        if(haveTrafficLight())
        {
            return trafficLight.getTrafficLightTeams();
        }
        return -1;
    }

    public int[] getConnection(){
        return connection;
    }

    public boolean getIntersection(){
        return intersection;
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
        return "Intersection = " + Arrays.toString(direction) +
                ", ID = " + idX + "," + idY +
                ", Start = " + startFlag +
                ", Speed limit = " + speedLimit +
                ", Traffic light flag = " + trafficLightFlag +
                ", Connection status = " + connectionStatus();
    }

}

    /*//function overload for GUI

    public void setIntersection(int[] intersection) {
        this.intersection = intersection;//需要修改 intersection陣列
    }

    public void setTrafficLightFlag(boolean trafficLightFlag) {
        this.trafficLightFlag = trafficLightFlag;
    }


    public void setTrafficLightGroup(int trafficLightGroup) {
        this.trafficLightGroup = trafficLightGroup;
    }

    public void setSpeedLimit(double speedLimit) {
        this.speedLimit = speedLimit;
    }

    public void setIdX(int idX) {
        this.idX = idX;
    }

    public void setIdY(int idY) {
        this.idY = idY;
    }

    public void setStartFlag(boolean startFlag) {
        this.startFlag = startFlag;
    }

    public void setWeights(double[] weights) {
        this.weights = weights;  // 需修改：處理 weights 陣列
    }

    public void setConnection(int[] connection) {
        this.connection = connection;  // 需修改：處理 connection 陣列
    }
}*/
