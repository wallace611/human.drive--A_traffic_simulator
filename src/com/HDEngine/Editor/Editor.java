package com.HDEngine.Editor;

import com.HDEngine.Editor.Object.Road.EditorRoadChunk;
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
    private static final int STARTFLAG = 1;
    private static final int SPEEDLIMIT = 2;
    private static final int DIRECTION = 3;
    private static final int ISWEIGHTED = 4;
    private static final int WEIGHTS = 5;
    private EditorRoadChunk[][] map = new EditorRoadChunk[22][22];//if possible, keep the outer side of the array clear(eg. map[0][0] should always be null)
    private EditorRoadChunk[] templateChunks = new EditorRoadChunk[20];
    private Map<Integer,int[]> trafficLight = new HashMap<>();
    private transient FileManager fileManager = new FileManager();
    //private transient FileManager loadedFileManager = FileManager.loadFromFile("src/SavedFile/editor_map.obj");
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
        showHow();
        boolean quit = false;
        while(!quit)
        {
            showChunk();
            showTrafficLight();
            try
            {
                String action;
                action = input.nextLine();
                action.toLowerCase();
                switch (action) {
                    case "add chunk","new chunk":addNewChunk();break;
                    case "edit chunk": editRoadChunk();break;
                    case "delete chunk": deleteChunk();break;
                    case "add traffic light group", "add trafficlight group": newTrafficLightGroup();break;
                    case "edit traffic light group","edit trafficlight group": editTrafficLightTeamTimer();break;
                    case "save": exportData();break;
                    case "load": importData(); break;
                    case "quit","leave": 
                        exportData();
                        System.out.println("See you next time!");
                        quit = true;
                    break;
                    case "how" , "help":showHow();break;
                    case "chunk info","chunkinfo": chunkInfo();break;
                }
            }
            catch (IllegalArgumentException e) 
            {
                System.out.println("we dont have this attribute");
                input.nextLine();
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
                System.out.print("please endet axis:");
                EditorRoadChunk newRoadChunk = new EditorRoadChunk();
                newRoadChunk.initializeScanner();
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

    public void addNewChunk(int IDX, int IDY)//create a new chunk
    {
        boolean valid = false;
        while (!valid) 
        {
            try
            {
                System.out.print("please endet axis:");
                EditorRoadChunk newRoadChunk = new EditorRoadChunk();
                newRoadChunk.initializeScanner();
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
                target.initializeScanner();
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
                System.out.print("please endet axis:");
                IDX= input.nextInt();
                IDY = input.nextInt();
                if (IDX > 20 || IDX <= 0 || IDY > 20 || IDY <= 0) 
                {
                    throw new IllegalArgumentException("Coordinates must be in the range of 1 to 20.");
                }
                if(map[IDX][IDY] == null)
                {
                    System.out.println("there is no data in that axis. Swithching to add mode...");
                    addNewChunk();
                }
                EditorRoadChunk target = map[IDX][IDY];
                target.initializeScanner();
                System.out.print("Enter the attribute to edit (PS.axis can't edit):");
                String attribute = input.next().toLowerCase();;
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
        int key ,teamnum, timer;
        while (!valid) 
        {
            try
            {
                System.out.print("please enter group number:");
                key = input.nextInt();
                if(trafficLight.get(key) != null)
                {
                    System.out.println("\nyou have already created this group. Swithching to edit mode...");
                    editTrafficLightTeamTimer(key);
                    valid = true;
                }
                else
                {
                    System.out.print("how many teams is allowed in the group:");
                    teamnum = input.nextInt();
                    int[] teams = new int[teamnum];
                    for(int i = 0 ; i < teamnum ; i++)
                    {
                        System.out.print("please enter team " + i + "'s wait time:");
                        timer = input.nextInt();
                        teams[i] = timer;
                    }
                    trafficLight.put(key, teams);
                    valid = true;
                }
            }
            catch(InputMismatchException e)
            {
                input.nextLine();
                System.out.println("wrong input");
            }
        }
    }

    public void newTrafficLightGroup(int key)//record the traffic light group at map
    {
        boolean valid = false;
        int teamnum, timer;
        while (!valid) 
        {
            try
            {
                if(trafficLight.get(key) != null)
                {
                    System.out.println("\nyou have already created this group. Swithching to edit mode...");
                    editTrafficLightTeamTimer(key);
                    valid = true;
                }
                else
                {
                    System.out.print("how many teams is allowed in the group:");
                    teamnum = input.nextInt();
                    int[] teams = new int[teamnum];
                    for(int i = 0 ; i < teamnum ; i++)
                    {
                        System.out.print("please enter team " + i + "'s wait time:");
                        timer = input.nextInt();
                        teams[i] = timer;
                    }
                    trafficLight.put(key, teams);
                    valid = true;
                }
            }
            catch(InputMismatchException e)
            {
                input.nextLine();
                System.out.println("wrong input");
            }
        }
    }

    public void editTrafficLightTeamTimer(int key) {
        boolean valid = false;
        while(!valid)
        {    
            try 
            {
                System.out.println("Start editing group " + key + "!");
                int[] target = trafficLight.get(key);
                int newtime;
                for(int i = 0 ; i < trafficLight.get(key).length ; i ++)
                {
                    System.out.print("current team " + i + " wait time is " + trafficLight.get(key)[i] + "\n");
                    System.out.print("new time:");
                    newtime = input.nextInt();
                    target[i] = newtime;
                }
                trafficLight.replace(key, target);
                valid = true;
            } 
            catch (InputMismatchException e) 
            {
                System.out.println("Input type mismatch. Please enter a valid number.");
                input.nextLine(); // clear the invalid input
            }    
        }
    }

    public void editTrafficLightTeamTimer() {
        boolean valid = false;
        while(!valid)
        {    
            try 
            {
                int key;
                System.out.print("which group do you want to edit:");
                key = input.nextInt();
                if(trafficLight.get(key) == null)
                {
                    System.out.print("there is no group " + key +". Switching to add mode");
                    newTrafficLightGroup(key);
                }
                System.out.println("Start editing group " + key + "!");
                int[] target = trafficLight.get(key);
                int newtime;
                for(int i = 0 ; i < trafficLight.get(key).length ; i ++)
                {
                    System.out.print("current team " + i + " wait time is" + trafficLight.get(key)[i]);
                    System.out.print("new time:");
                    newtime = input.nextInt();
                    target[i] = newtime;
                }
                trafficLight.replace(key, target);
                valid = true;
            } 
            catch (InputMismatchException e) 
            {
                System.out.println("Input type mismatch. Please enter a valid number.");
                input.nextLine(); // clear the invalid input
            }    
        }
    }

    public void deleteTrafficLightGroup()
    {
        try
        {
            int key = input.nextInt();
            if(trafficLight.containsKey(key))
            {
                trafficLight.remove(key);
                System.out.println("Removed!");
            }
            else
            {
                System.out.println("Remove unsucessful. Key not found.");
            }
        }
        catch(InputMismatchException e )
        {
            System.out.println("input mismatch");
            input.nextLine();
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
    if (y + 1 < map[0].length && map[x][y + 1] != null && (target.getDirection()[NORTH] != 0) && (map[x][y + 1].getDirection()[SOUTH] != 0)) {
        if (y < y + 1) {
            target.setConnection(NORTH);
            target.setConnected(map[x][y+1], NORTH);
        } else {
            map[x][y + 1].setConnection(SOUTH);
            map[x][y+1].setConnected(target, SOUTH);
        }
    }

    // Check and set connection for EAST
    if (x + 1 < map.length && map[x + 1][y] != null && (target.getDirection()[EAST]) != 0 && (map[x + 1][y].getDirection()[WEST]) != 0) {
        if (x < x + 1) {
            target.setConnection(EAST);
            target.setConnected(map[x+1][y], EAST);
        } else {
            map[x + 1][y].setConnection(WEST);
            map[x+1][y].setConnected(target, WEST);
        }
    }

    // Check and set connection for SOUTH
    if (y - 1 >= 0 && map[x][y - 1] != null && (target.getDirection()[SOUTH]) != 0 && (map[x][y - 1].getDirection()[NORTH]) != 0) {
        if (y > y - 1) {
            target.setConnection(SOUTH);
            target.setConnected(map[x][y - 1], SOUTH);
        } else {
            map[x][y - 1].setConnection(NORTH);
            map[x][y - 1].setConnected(target, NORTH);
        }
    }

    // Check and set connection for WEST
    if (x - 1 >= 0 && map[x - 1][y] != null && (target.getDirection()[WEST]) != 0 && (map[x - 1][y].getDirection()[EAST]) != 0) {
        if (x > x - 1) {
            target.setConnection(WEST);
            target.setConnected(map[x-1][y], WEST);
        } else {
            map[x - 1][y].setConnection(EAST);
            map[x-1][y].setConnected(target, EAST);
        }
    }

    // Check and set connection for NORTH-EAST
    if (x + 1 < map.length && y + 1 < map[0].length && map[x + 1][y + 1] != null) {
        if ((map[x][y + 1] != null || map[x + 1][y] != null) && (target.getDirection()[NE]) != 0 && (map[x + 1][y + 1].getDirection()[SW]) != 0) {
            if (x < x + 1 && y < y + 1) {
                target.setConnection(NE);
                target.setConnected(map[x + 1][y + 1], NE);
            } else {
                map[x + 1][y + 1].setConnection(SW);
                map[x + 1][y + 1].setConnected(target, SW);
            }
        }
    }

    // Check and set connection for SOUTH-EAST
    if (x + 1 < map.length && y - 1 >= 0 && map[x + 1][y - 1] != null) {
        if ((map[x][y - 1] != null || map[x + 1][y] != null) && (target.getDirection()[SE]) != 0 && (map[x + 1][y - 1].getDirection()[NW]) != 0) {
            if (x < x + 1 && y > y - 1) {
                target.setConnection(SE);
                target.setConnected(map[x + 1][y - 1], SE);
            } else {
                map[x + 1][y - 1].setConnection(NW);
                map[x + 1][y - 1].setConnected(target, NW);
            }
        }
    }

    // Check and set connection for SOUTH-WEST
    if (x - 1 >= 0 && y - 1 >= 0 && map[x - 1][y - 1] != null) {
        if ((map[x][y - 1] != null || map[x - 1][y] != null) && (target.getDirection()[SW]) != 0 && (map[x - 1][y - 1].getDirection()[NE]) != 0) {
            if (x > x - 1 && y > y - 1) {
                target.setConnection(SW);
                target.setConnected(map[x - 1][y - 1], SW);
            } else {
                map[x - 1][y - 1].setConnection(NE);
                map[x - 1][y - 1].setConnected(target, NE);
            }
        }
    }

    // Check and set connection for NORTH-WEST
    if (x - 1 >= 0 && y + 1 < map[0].length && map[x - 1][y + 1] != null) {
        if ((map[x][y + 1] != null || map[x - 1][y] != null) && (target.getDirection()[NW]) != 0 && (map[x - 1][y + 1].getDirection()[SE]) != 0) {
            if (x > x - 1 && y < y + 1) {
                target.setConnection(NW);
                target.setConnected(map[x - 1][y + 1], NW);
            } else {
                map[x - 1][y + 1].setConnection(SE);
                map[x - 1][y + 1].setConnected(target, SE);
            }
        }
    }
        
    }

    public void exportData() 
    {
        fileManager.setData(map,trafficLight);
        fileManager.saveToFile("src/SavedFile/editor_map.obj");
    }

    // read file from FileManager
    public void importData() {
        FileManager fileManager = FileManager.loadFromFile("src/SavedFile/editor_map.obj");
        if (fileManager != null) {
            this.map = fileManager.getMap();
            this.trafficLight = fileManager.getTrafficLight();
            System.out.println("Data loaded successfully.");
        } else {
            System.out.println("Failed to load data.");
        }
    }

    public String assignPath(Scanner input) {
        System.out.print("\nEnter the file path where you want to save the data:");
        String path = input.nextLine();

        System.out.print("\nEnter the file name:");
        String fileName = input.nextLine();

        return path + "\\" + fileName+".obj";
    }

    private void showChunk() {
        for (int i = 20; i > 0; i--) {
            for (int j = 1; j <= 20; j++) {
                if (map[j][i] == null) {
                    System.out.print("| |");
                } else if (map[j][i].haveTrafficLight()) {
                    System.out.print("|T|");
                } else if (!map[j][i].getIntersection()) {
                    System.out.print("|U|");
                } else {
                    System.out.print("|T|");
                }
            }
            System.out.println();
        }
        System.out.println("V = this chunk dont have traffic light, " +
        "T = this chunk have traffic light, "+
        "U = this chunk dont get effected by traffic light"
        );
    }

    private void showTrafficLight() {
        if (trafficLight.isEmpty()) {
            System.out.println("There is no traffic light group.");
        } else {
            for (Map.Entry<Integer, int[]> entry : trafficLight.entrySet()) {
                int groupNumber = entry.getKey();
                int[] teamTimes = entry.getValue();
                System.out.print("group #" + groupNumber + " : ");
                for (int i = 0; i < teamTimes.length; i++) {
                    System.out.print("team#" + i + "   " + teamTimes[i]);
                    if (i < teamTimes.length - 1) {
                        System.out.print(" , ");
                    }
                }
                System.out.println();
            }
        }
    }

    private void showHow() {
        System.out.println("Commands and their functions:");
        System.out.println("add chunk - add new chunk to map");
        System.out.println("new chunk - add new chunk to map (same as 'add chunk')");
        System.out.println("edit chunk - edit an existing chunk in the map");
        System.out.println("delete chunk - delete a chunk from the map");
        System.out.println("add traffic light group - add a new traffic light group");
        System.out.println("add trafficlight group - add a new traffic light group (same as 'add traffic light group')");
        System.out.println("edit traffic light group - edit an existing traffic light group");
        System.out.println("edit trafficlight group - edit an existing traffic light group (same as 'edit traffic light group')");
        System.out.println("save - save the map and traffic light group data to a file");
        System.out.println("load - load the map and traffic light group data from a file");
        System.out.println("quit - save data and exit the program");
        System.out.println("leave - save data and exit the program (same as 'quit')");
    }

    private void chunkInfo() {
        boolean valid = false;
        while (!valid) {
            try {
                System.out.print("Please enter the coordinates of the chunk (IDX IDY): ");
                int IDX = input.nextInt();
                int IDY = input.nextInt();
                if (IDX > 20 || IDX <= 0 || IDY > 20 || IDY <= 0) {
                    throw new IllegalArgumentException("Coordinates must be in the range of 1 to 20.");
                }
                EditorRoadChunk chunk = map[IDX][IDY];
                if (chunk != null) {
                    System.out.println(chunk.toString());
                } else {
                    System.out.println("No chunk found at the specified coordinates.");
                }
                valid = true;
            } catch (InputMismatchException e) {
                input.nextLine(); // Clear the invalid input
                System.out.println("Input must be an integer.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
//for normal attribute
    public void updateRoadParameter(String buttonId, int parameterIndex, String newValue) {
        int id = Integer.parseInt(buttonId);
        if (templateChunks[id] != null) {
            try {
                switch (parameterIndex) {
                    case STARTFLAG:
                        if(newValue.toLowerCase().equals("true"))
                            templateChunks[id].setStartFlag(true);
                        else{
                            templateChunks[id].setStartFlag(false);
                        }
                    break;

                    case SPEEDLIMIT:
                        double newval = Double.parseDouble(newValue);
                        templateChunks[id].setSpeedLimit(newval);
                    break;

                    case ISWEIGHTED:
                        if(newValue.toLowerCase().equals("true")){
                            templateChunks[id].setIsWeighted(true);
                        }
                        else{
                            templateChunks[id].setIsWeighted(false);
                        }
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input for parameter " + parameterIndex + ": " + newValue);
            }
        }
    }
//for Direction
    public void updateRoadParameter(String buttonId, int parameterIndex, int[] newValue) {
        int id = Integer.parseInt(buttonId);
        if (templateChunks[id] != null) {
            try {
                templateChunks[id].setDirection(newValue);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input for parameter " + parameterIndex + ": " + newValue);
            }
        }
    }
//for Weight
    public void updateRoadParameter(String buttonId, int parameterIndex, double[] newValue) {
        int id = Integer.parseInt(buttonId);
        if (templateChunks[id] != null) {
            try {
                templateChunks[id].setWeights(newValue);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input for parameter " + parameterIndex + ": " + newValue);
            }
        }
    }

    public void mapToMap(int oldIDX,int oldIDY, int newIDX, int newIDY){
        map[newIDX][newIDY] = map[oldIDX][oldIDY];
        map[oldIDX][oldIDY] = null;
    }

    public void templateToMap(int tempalteID,int IDX, int IDY){
        EditorRoadChunk newchunk = new EditorRoadChunk();
        newchunk = templateChunks[tempalteID];
        map[IDX][IDY] = newchunk;
    }

    public void deleteMapChunk(int IDX, int IDY){
        map[IDX][IDY] = null;
    }
}
