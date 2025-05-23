package com.plainprog.grandslam_ai.object.response_models.competition;

import java.util.List;

public class OpenCompetitionItemModel {
    private long id;
    private String name;
    private int capacity;
    private int currentParticipants;
    private int themeId;
    private List<MyCompetitionSubmissionModel> mySubmissions;

    public OpenCompetitionItemModel(long id, String name, int capacity, int currentParticipants, int themeId, List<MyCompetitionSubmissionModel> mySubmissions) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.currentParticipants = currentParticipants;
        this.themeId = themeId;
        this.mySubmissions = mySubmissions;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCurrentParticipants() {
        return currentParticipants;
    }

    public void setCurrentParticipants(int currentParticipants) {
        this.currentParticipants = currentParticipants;
    }

    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
    }

    public List<MyCompetitionSubmissionModel> getMySubmissions() {
        return mySubmissions;
    }

    public void setMySubmissions(List<MyCompetitionSubmissionModel> mySubmissions) {
        this.mySubmissions = mySubmissions;
    }
}
