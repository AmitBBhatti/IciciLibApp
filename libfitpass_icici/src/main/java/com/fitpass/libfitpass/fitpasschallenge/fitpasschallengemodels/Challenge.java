package com.fitpass.libfitpass.fitpasschallenge.fitpasschallengemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Challenge implements Serializable
{

    @SerializedName("type_id")
    @Expose
    private String type_id;
    @SerializedName("challenge_name")
    @Expose
    private String challenge_name;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("auto_fill_description")
    @Expose
    private String auto_fill_description;


    @SerializedName("goal_input_label")
    @Expose
    private String goalInputLabel;
    @SerializedName("metric")
    @Expose
    private List<String> metric = null;
    @SerializedName("goal_input_display")

    @Expose
    private Boolean goalInputDisplay;
    @SerializedName("goal_input_options")
    @Expose
    private List<String> goalInputOptions = null;
    @SerializedName("images")
    @Expose
    private List<Image> images = null;

    @SerializedName("circuits_details")
    @Expose
    private CircuitDetails circuitDetails = null;


    private final static long serialVersionUID = 6833249204962078690L;

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getChallenge_name() {
        return challenge_name;
    }

    public void setChallenge_name(String challenge_name) {
        this.challenge_name = challenge_name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getMetric() {
        return metric;
    }

    public void setMetric(List<String> metric) {
        this.metric = metric;
    }

    public Boolean getGoalInputDisplay() {
        return goalInputDisplay;
    }

    public void setGoalInputDisplay(Boolean goalInputDisplay) {
        this.goalInputDisplay = goalInputDisplay;
    }

    public List<String> getGoalInputOptions() {
        return goalInputOptions;
    }

    public void setGoalInputOptions(List<String> goalInputOptions) {
        this.goalInputOptions = goalInputOptions;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getGoalInputLabel() {
        return goalInputLabel;
    }

    public void setGoalInputLabel(String goalInputLabel) {
        this.goalInputLabel = goalInputLabel;
    }

    public String getAuto_fill_description() {
        return auto_fill_description;
    }

    public void setAuto_fill_description(String auto_fill_description) {
        this.auto_fill_description = auto_fill_description;
    }

    public CircuitDetails getCircuitDetails() {
        return circuitDetails;
    }

    public void setCircuitDetails(CircuitDetails circuitDetails) {
        this.circuitDetails = circuitDetails;
    }
}
