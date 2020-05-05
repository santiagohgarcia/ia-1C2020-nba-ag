/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nbagenetic.domain;

/**
 *
 * @author santi
 */
public class Player {
    
    private Integer id;
    private String name;
    private String team;
    private Double overallPoints;
    private String primaryPosition;
    private String secondaryPosition;
    private Double height;

    public Player(Integer id, String name, String team, Double overallPoints, String primaryPosition, String secondaryPosition, Double height) {
        this.id = id;
        this.name = name;
        this.team = team;
        this.overallPoints = overallPoints;
        this.primaryPosition = primaryPosition;
        this.secondaryPosition = secondaryPosition;
        this.height = height;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public Double getOverallPoints() {
        return overallPoints;
    }

    public void setOverallPoints(Double overallPoints) {
        this.overallPoints = overallPoints;
    }

    public String getPrimaryPosition() {
        return primaryPosition;
    }

    public void setPrimaryPosition(String primaryPosition) {
        this.primaryPosition = primaryPosition;
    }

    public String getSecondaryPosition() {
        return secondaryPosition;
    }

    public void setSecondaryPosition(String secondaryPosition) {
        this.secondaryPosition = secondaryPosition;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }
    
}
