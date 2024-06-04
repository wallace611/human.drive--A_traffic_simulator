package com.HDEngine.Editor.Object.TrafficLight;

import java.io.*;

public class TrafficLight implements Serializable {
    private int group;
    private int teams ;
    private int Timer;//timer of traffic light(this part should follow group, still working on it)

    public void setTrafficLightGroup(int group){
        this.group = group;
    }

    public void setTrafficLightTeams(int teams){
        this.teams = teams;
    }

    public void setTrafficLightTimer(int timer){// set traffic light group, the record of group will be in the main function
        this.Timer = timer;
    }

    public int getTrafficLightGroup(){
        return group;
    }

    public int getTrafficLightTeams(){
        return teams;
    }

    public int getTrafficLightGroupTimer(){
        return Timer;
    }

}
