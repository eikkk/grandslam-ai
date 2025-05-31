package com.plainprog.grandslam_ai.object.response_models.competition;

public class UpcomingCompetitionItemModel {
    private Integer themeId;
    private String name;

    public UpcomingCompetitionItemModel() {
    }

    public UpcomingCompetitionItemModel(Integer themeId, String name) {
        this.themeId = themeId;
        this.name = name;
    }

    public Integer getThemeId() {
        return themeId;
    }

    public void setThemeId(Integer themeId) {
        this.themeId = themeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
