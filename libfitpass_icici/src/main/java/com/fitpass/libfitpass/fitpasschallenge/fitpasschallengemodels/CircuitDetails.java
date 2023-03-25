package com.fitpass.libfitpass.fitpasschallenge.fitpasschallengemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CircuitDetails implements Serializable {

    @SerializedName("background_color")
    @Expose
    private String background_color;
    @SerializedName("coach_circuit_id")
    @Expose
    private String coach_circuit_id;
    @SerializedName("coach_circuit_name")

    @Expose
    private String coach_circuit_name;
    @SerializedName("image_url")
    @Expose
    private String image_url;

    @SerializedName("image_name")
    @Expose
    private String image_name;

    private String exercise_id;


    public String getBackground_color() {
        return background_color;
    }

    public void setBackground_color(String background_color) {
        this.background_color = background_color;
    }

    public String getCoach_circuit_id() {
        return coach_circuit_id;
    }

    public void setCoach_circuit_id(String coach_circuit_id) {
        this.coach_circuit_id = coach_circuit_id;
    }

    public String getCoach_circuit_name() {
        return coach_circuit_name;
    }

    public void setCoach_circuit_name(String coach_circuit_name) {
        this.coach_circuit_name = coach_circuit_name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getExercise_id() {
        return exercise_id;
    }

    public void setExercise_id(String exercise_id) {
        this.exercise_id = exercise_id;
    }
}
