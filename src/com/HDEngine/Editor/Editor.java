package com.HDEngine.Editor;

import com.HDEngine.Editor.Object.Road.EditorRoadChunk;
import com.HDEngine.Editor.Object.TrafficLight.TrafficLight;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Map;
import java.io.*;
import com.HDEngine.Utilities.FileManageTools.FileManager;

import processing.app.syntax.InputHandler.repeat;

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
        while(true)
        {
        String action;
        action = input.next();
        action.toLowerCase();
            if(action.equals("add chunk"))
            {
                addNewChunk();
            }
            else if(action.equals("edit chunk"))
            {
                editRoadChunk();
            }
            else if(action.equals("delete chunk"))
            {
                deleteChunk();
            }
            else if(action.equals("add traffic light group"))
            {

            }
            else if(action.equals("edit traffic light group"))
            {

            }
            else if(action.equals("save"))
            {

            }
            else if(action.equals("load"))
            {

            }
            else if(action.equals("quit"))
            {
                
            }
            else
            {
                throw new IllegalArgumentException("Coordinates must be in the range of 1 to 20.");
            }
        }
    }
    
    public void addNewChunk()//create a new chunk
    {
        boolean valid = false;
        while (!valid) 
        {
            try
            {
                EditorRoadChunk newRoadChunk = new EditorRoadChunk();
                int IDX=0,IDY=0;
                IDX = input.nextInt();
                IDY = input.nextInt();
                if (IDX > 20 || IDX <= 0 || IDY > 20 || IDY <= 0) 
                {
                    throw new IllegalArgumentException("Coordinates must be in the range of 1 to 20.");
                }
                if(map[IDX][IDY] == null)
                {
                    newRoadChunk.getData(IDX, IDY);
                    map[IDX][IDY] = newRoadChunk;
                    connectionAdd(map, newRoadChunk);
                    valid = true;
                }
                else
                {
                    System.out.println("This axis already have data. Switching to edit mode");
                    editRoadChunk(IDX, IDY);
                    valid = true;
                }
            }
            catch(IllegalArgumentException e)
            {
                System.out.println(e.getMessage());
                input.nextLine();
            }
        }
    }

    public void editRoadChunk(int IDX,int IDY)
    {
        boolean valid = false;
        while(!valid)
        {
            try
            {
                if (IDX > 20 || IDX <= 0 || IDY > 20 || IDY <= 0) 
                {
                    throw new IllegalArgumentException("Coordinates must be in the range of 1 to 20.");
                }
                EditorRoadChunk target = map[IDX][IDY];
                System.out.print("Enter the attribute to edit (PS.axis can't edit):");
                String attribute = input.next().toLowerCase();
                switch (attribute) {
                    case "start flag","start","startflag": target.setStartPoint();break;
                    case "speedlimit","speed limit": target.setSpeedLimit();  break;
                    case "direction":target.setDirection();break;
                    case "weight": target.setWeight();break;
                    case "intersection": target.setIntersection();break;
                    case "traffic light","trafficlight","trafficlights","traffic lights":target.setTrafficLight();break;
                    default:throw new InputMismatchException("we dont have this attribute");
                }
                map[IDX][IDY] = target;
                connectionAdd(map, map[IDX][IDY]);
                valid = true;
            }
            catch(IllegalArgumentException e)
            {
                System.out.println(e.getMessage());
                input.nextLine();
            }
            catch(InputMismatchException e)
            {
                input.nextLine();
                System.out.println(e.getMessage());
            }
        }
    }

    public void editRoadChunk()
    {
        boolean valid = false;
        int IDX=0,IDY=0;
        while(!valid)
        {
            try
            {
                if (IDX > 20 || IDX <= 0 || IDY > 20 || IDY <= 0) 
                {
                    throw new IllegalArgumentException("Coordinates must be in the range of 1 to 20.");
                }
                EditorRoadChunk target = map[IDX][IDY];
                System.out.print("Enter the attribute to edit (PS.axis can't edit):");
                String attribute = input.next().toLowerCase();
                switch (attribute) {
                    case "start flag","start","startflag": target.setStartPoint();break;
                    case "speedlimit","speed limit": target.setSpeedLimit();  break;
                    case "direction":target.setDirection();break;
                    case "weight": target.setWeight();break;
                    case "intersection": target.setIntersection();break;
                    case "traffic light","trafficlight","trafficlights","traffic lights":target.setTrafficLight();break;
                    default:throw new InputMismatchException("we dont have this attribute");
                }
                map[IDX][IDY] = target;
                connectionAdd(map, map[IDX][IDY]);
                valid = true;
            }
            catch(IllegalArgumentException e)
            {
                System.out.println(e.getMessage());
                input.nextLine();
            }
            catch(InputMismatchException e)
            {
                input.nextLine();
                System.out.println(e.getMessage());
            }
        }
    }

    public void deleteChunk()
    {
        boolean valid = false;
        while(!valid)
        {
            try
            {
                int IDX = 0, IDY = 0;
                IDX = input.nextInt();
                IDY = input.nextInt();
                if (IDX > 20 || IDX <= 0 || IDY > 20 || IDY <= 0) 
                {
                    throw new IllegalArgumentException("Coordinates must be in the range of 1 to 20.");
                }
                map[IDX][IDY] = null;
                valid = true;
            }
            catch(IllegalArgumentException e)
            {
                System.out.println(e.getMessage());
            }
            catch(InputMismatchException e)
            {
                input.nextLine();
                System.out.println("input type mismatch");
            }
        }
    }

    public void newTrafficLightGroup()//record the traffic light group at map
    {
        boolean valid = false;
        while (!valid) 
        {
            
        }
    }

    public void editTrafficLightTeamTimer() {
        boolean valid = false;
        while(!valid)
        {    
            try {
                
                valid = true;
            } catch (InputMismatchException e) {
                System.out.println("Input type mismatch. Please enter a valid number.");
                input.nextLine(); // clear the invalid input
            }    
        }
    }

    public void deleteTrafficLightGroup()
    {
        
    }

    public EditorRoadChunk getChunk(int ID_X, int ID_Y)
    {
        return map[ID_X][ID_Y];
    }

    public void connectionAdd(EditorRoadChunk[][] map, EditorRoadChunk target) {
        int x = target.getIDX();
        int y = target.getIDY();
        // Check and set connection for NORTH
    if (y + 1 < map[0].length && map[x][y + 1] != null && (target.getDirection()[NORTH] != 0) && (map[x][y + 1].getDirection()[SOUTH] != 0)) {
        if (y < y + 1) {
            target.setConnection(NORTH);
        } else {
            map[x][y + 1].setConnection(SOUTH);
        }
    }

    // Check and set connection for EAST
    if (x + 1 < map.length && map[x + 1][y] != null && (target.getDirection()[EAST]) != 0 && (map[x + 1][y].getDirection()[WEST]) != 0) {
        if (x < x + 1) {
            target.setConnection(EAST);
        } else {
            map[x + 1][y].setConnection(WEST);
        }
    }

    // Check and set connection for SOUTH
    if (y - 1 >= 0 && map[x][y - 1] != null && (target.getDirection()[SOUTH]) != 0 && (map[x][y - 1].getDirection()[NORTH]) != 0) {
        if (y > y - 1) {
            target.setConnection(SOUTH);
        } else {
            map[x][y - 1].setConnection(NORTH);
        }
    }

    // Check and set connection for WEST
    if (x - 1 >= 0 && map[x - 1][y] != null && (target.getDirection()[WEST]) != 0 && (map[x - 1][y].getDirection()[EAST]) != 0) {
        if (x > x - 1) {
            target.setConnection(WEST);
        } else {
            map[x - 1][y].setConnection(EAST);
        }
    }

    // Check and set connection for NORTH-EAST
    if (x + 1 < map.length && y + 1 < map[0].length && map[x + 1][y + 1] != null) {
        if ((map[x][y + 1] != null || map[x + 1][y] != null) && (target.getDirection()[NE]) != 0 && (map[x + 1][y + 1].getDirection()[SW]) != 0) {
            if (x < x + 1 && y < y + 1) {
                target.setConnection(NE);
            } else {
                map[x + 1][y + 1].setConnection(SW);
            }
        }
    }

    // Check and set connection for SOUTH-EAST
    if (x + 1 < map.length && y - 1 >= 0 && map[x + 1][y - 1] != null) {
        if ((map[x][y - 1] != null || map[x + 1][y] != null) && (target.getDirection()[SE]) != 0 && (map[x + 1][y - 1].getDirection()[NW]) != 0) {
            if (x < x + 1 && y > y - 1) {
                target.setConnection(SE);
            } else {
                map[x + 1][y - 1].setConnection(NW);
            }
        }
    }

    // Check and set connection for SOUTH-WEST
    if (x - 1 >= 0 && y - 1 >= 0 && map[x - 1][y - 1] != null) {
        if ((map[x][y - 1] != null || map[x - 1][y] != null) && (target.getDirection()[SW]) != 0 && (map[x - 1][y - 1].getDirection()[NE]) != 0) {
            if (x > x - 1 && y > y - 1) {
                target.setConnection(SW);
            } else {
                map[x - 1][y - 1].setConnection(NE);
            }
        }
    }

    // Check and set connection for NORTH-WEST
    if (x - 1 >= 0 && y + 1 < map[0].length && map[x - 1][y + 1] != null) {
        if ((map[x][y + 1] != null || map[x - 1][y] != null) && (target.getDirection()[NW]) != 0 && (map[x - 1][y + 1].getDirection()[SE]) != 0) {
            if (x > x - 1 && y < y + 1) {
                target.setConnection(NW);
            } else {
                map[x - 1][y + 1].setConnection(SE);
            }
        }
    }
        
    }

    public FileManager exportData() 
    {
        FileManager fileManager = new FileManager();
        fileManager.setData(map,trafficLight);
        return fileManager;
    }

    // read file from FileManager
    public void importData(FileManager fileManager)
    {
        this.map = fileManager.getMap();
        this.trafficLight = fileManager.getTrafficLight();
    }

    public String assignPath(Scanner input) {
        System.out.print("\nEnter the file path where you want to save the data:");
        String path = input.nextLine();

        System.out.print("\nEnter the file name:");
        String fileName = input.nextLine();

        return path + "\\" + fileName+".obj";
    }

/* for GUI
    public void updateRoadParameter(String buttonId, int parameterIndex, String newValue) {
        int id = Integer.parseInt(buttonId);
        if (templateChunks[id] != null) {
            try {
                switch (parameterIndex) {
                    case INTERSECTION:
                        // 需修改：處理 intersection 陣列
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

    public void updateTrafficLightParameter(String buttonId, int parameterIndex, String newValue) {
        int id = Integer.parseInt(buttonId);
        if (templateChunks[id] != null) {
            try {
                switch (parameterIndex) {
                    case INTERSECTION:
                        // 需修改：處理 intersection 陣列
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
    

}
