package com.fitpass.libfitpass.fitpasschallenge.fitpasschallengemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class UpcomingChallenge implements Serializable
{

    @SerializedName("amway_challenge_id")
    @Expose
    private String amwayChallengeId;
    @SerializedName("challenge_name")
    @Expose
    private String challengeName;
    @SerializedName("challenge_description")
    @Expose
    private String challengeDescription;

    @SerializedName("challenge_days_metrics")
    @Expose
    private String challengeDaysMatrics;

    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("challenge_url")
    @Expose
    private String challengeUrl;
    @SerializedName("challenge_photo")
    @Expose
    private String challengePhoto;
    @SerializedName("challenge_type")
    @Expose
    private String challengeType;
    @SerializedName("challenge_metrics")
    @Expose
    private String challengeMetrics;
    @SerializedName("target_type")
    @Expose
    private String targetType;
    @SerializedName("target_value")
    @Expose
    private String targetValue;
    @SerializedName("participant")
    @Expose
    private List<Object> participant = null;
    @SerializedName("challenge_created_user_id")
    @Expose
    private String challengeCreatedUserId;
    @SerializedName("number_of_members")
    @Expose
    private String numberOfMembers;
    private final static long serialVersionUID = -4257890510898904416L;

    public String getAmwayChallengeId() {
        return amwayChallengeId;
    }

    public void setAmwayChallengeId(String amwayChallengeId) {
        this.amwayChallengeId = amwayChallengeId;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }

    public String getChallengeDescription() {
        return challengeDescription;
    }

    public void setChallengeDescription(String challengeDescription) {
        this.challengeDescription = challengeDescription;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getChallengeUrl() {
        return challengeUrl;
    }

    public void setChallengeUrl(String challengeUrl) {
        this.challengeUrl = challengeUrl;
    }

    public String getChallengePhoto() {
        return challengePhoto;
    }

    public void setChallengePhoto(String challengePhoto) {
        this.challengePhoto = challengePhoto;
    }

    public String getChallengeType() {
        return challengeType;
    }

    public void setChallengeType(String challengeType) {
        this.challengeType = challengeType;
    }

    public String getChallengeMetrics() {
        return challengeMetrics;
    }

    public void setChallengeMetrics(String challengeMetrics) {
        this.challengeMetrics = challengeMetrics;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(String targetValue) {
        this.targetValue = targetValue;
    }

    public List<Object> getParticipant() {
        return participant;
    }

    public void setParticipant(List<Object> participant) {
        this.participant = participant;
    }

    public String getChallengeCreatedUserId() {
        return challengeCreatedUserId;
    }

    public void setChallengeCreatedUserId(String challengeCreatedUserId) {
        this.challengeCreatedUserId = challengeCreatedUserId;
    }

    public String getNumberOfMembers() {
        return numberOfMembers;
    }

    public void setNumberOfMembers(String numberOfMembers) {
        this.numberOfMembers = numberOfMembers;
    }

    public String getChallengeDaysMatrics() {
        return challengeDaysMatrics;
    }

    public void setChallnengeDaysMatrics(String challengeDaysMatrics) {
        this.challengeDaysMatrics = challengeDaysMatrics;
    }
}