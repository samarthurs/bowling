package com.codingkata.bowling.model;

import javax.persistence.*;

@Entity
public class Frame {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long frameId;

    private int pinsKnockedDownOnFirstRoll;

    private int pinsKnockedDownOnSecondRoll;

    private int frameScore;

    private boolean isStrike;

    private boolean isSpare;

    private boolean frameComplete;

    public Long getFrameId() {
        return frameId;
    }

    public void setFrameId(Long frameId) {
        this.frameId = frameId;
    }

    public int getPinsKnockedDownOnFirstRoll() {
        return pinsKnockedDownOnFirstRoll;
    }

    public void setPinsKnockedDownOnFirstRoll(int pinsKnockedDownOnFirstRoll) {
        this.pinsKnockedDownOnFirstRoll = pinsKnockedDownOnFirstRoll;
    }

    public int getPinsKnockedDownOnSecondRoll() {
        return pinsKnockedDownOnSecondRoll;
    }

    public void setPinsKnockedDownOnSecondRoll(int pinsKnockedDownOnSecondRoll) {
        this.pinsKnockedDownOnSecondRoll = pinsKnockedDownOnSecondRoll;
    }

    public int getFrameScore() {
        return frameScore;
    }

    public void setFrameScore(int frameScore) {
        this.frameScore = frameScore;
    }

    public boolean isStrike() {
        return isStrike;
    }

    public void setStrike(boolean strike) {
        isStrike = strike;
    }

    public boolean isSpare() {
        return isSpare;
    }

    public void setSpare(boolean spare) {
        isSpare = spare;
    }

    public boolean isFrameComplete() {
        return frameComplete;
    }

    public void setFrameComplete(boolean frameComplete) {
        this.frameComplete = frameComplete;
    }
}
