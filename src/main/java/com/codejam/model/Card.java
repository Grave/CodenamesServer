package com.codejam.model;

public class Card {

    public enum Team {
        NEUTRAL,
        BLUE_TEAM,
        RED_TEAM
    }

    private String text;
    private Team belongsTo;
    private boolean active;

    public Card(String text, Team belongsTo, boolean active) {
        this.text = text;
        this.belongsTo = belongsTo;
        this.active = active;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Team getBelongsTo() {
        return belongsTo;
    }

    public void setBelongsTo(Team belongsTo) {
        this.belongsTo = belongsTo;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean hasText(String keyword)
    {
        return text.equals(keyword);
    }
}
