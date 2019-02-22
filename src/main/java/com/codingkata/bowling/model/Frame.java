package com.codingkata.bowling.model;

import javax.persistence.*;

@Entity
public class Frame {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long frameId;

    private int pinsDownFirstRoll;

    private int pinsDownSecondRoll;

    private int frameScore;

    private boolean isStrike;

    private boolean isSpare;

    private boolean frameComplete;

    private boolean spareHandled;

    private boolean strikeFristRollHandled;

    private boolean strikeSecondRollHandled;

    public Long getFrameId() {
        return frameId;
    }

    public void setFrameId(Long frameId) {
        this.frameId = frameId;
    }

    public int getPinsDownFirstRoll() {
        return pinsDownFirstRoll;
    }

    public void setPinsDownFirstRoll(int pinsDownFirstRoll) {
        this.pinsDownFirstRoll = pinsDownFirstRoll;
    }

    public int getPinsDownSecondRoll() {
        return pinsDownSecondRoll;
    }

    public void setPinsDownSecondRoll(int pinsDownSecondRoll) {
        this.pinsDownSecondRoll = pinsDownSecondRoll;
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

    public boolean isSpareHandled() {
        return spareHandled;
    }

    public void setSpareHandled(boolean spareHandled) {
        this.spareHandled = spareHandled;
    }

    public boolean isStrikeFristRollHandled() {
        return strikeFristRollHandled;
    }

    public void setStrikeFristRollHandled(boolean strikeFristRollHandled) {
        this.strikeFristRollHandled = strikeFristRollHandled;
    }

    public boolean isStrikeSecondRollHandled() {
        return strikeSecondRollHandled;
    }

    public void setStrikeSecondRollHandled(boolean strikeSecondRollHandled) {
        this.strikeSecondRollHandled = strikeSecondRollHandled;
    }
}
