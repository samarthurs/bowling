package com.codingkata.bowling.controller;

import com.codingkata.bowling.model.Frame;
import com.codingkata.bowling.repository.BowlingGameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;


@Controller
public class BowlingGameController {

    private static final Logger logger = LoggerFactory.getLogger(BowlingGameController.class);

    @Autowired
    private BowlingGameRepository bowlingGameRepo;

    @GetMapping("/bowlingGame/play")
    public String scoreBowlingGame(@RequestParam(value = "roll", required = false) String roll, @ModelAttribute @Valid Frame newFrame, Model model) {

        int[] rolls = new int[21];
        int[] frameScore = new int[10];

        List<Frame> frameList = bowlingGameRepo.findAll();

        int currentPinsDown = Integer.valueOf(roll);

        if (frameList.size() == 10) {
            Frame tenthFrame = frameList.get(frameList.size() - 1);

            if (!tenthFrame.isFrameComplete()) { // 10th is not complete yet
                tenthFrame.setPinsDownSecondRoll(currentPinsDown);
                tenthFrame.setFrameScore(tenthFrame.getFrameScore() + currentPinsDown);
                if (tenthFrame.getPinsDownFirstRoll() + currentPinsDown == 10) {
                    tenthFrame.setSpare(true);
                }
                tenthFrame.setFrameComplete(true);
                bowlingGameRepo.save(tenthFrame);
                frameList = bowlingGameRepo.findAll();
            } else if (tenthFrame.isFrameComplete()) {
                if (tenthFrame.isStrike()) { // this means TenthFrame was a strike
                    tenthFrame.setFrameScore(tenthFrame.getFrameScore() + currentPinsDown);
                    tenthFrame.setPinsDownSecondRoll(currentPinsDown);
                    bowlingGameRepo.save(tenthFrame);
                    model.addAttribute("alertLastRoll", true);
                } else if (tenthFrame.isSpare()) {
                    logger.info("Spare on the 10th frame. One Additional Role and the Last roll of the game");
                    tenthFrame.setFrameScore(tenthFrame.getFrameScore() + currentPinsDown);
                    bowlingGameRepo.save(tenthFrame);
                    frameList = bowlingGameRepo.findAll();
                    model.addAttribute("gameOver", true);
                    model.addAttribute("totalScore", frameList.stream().mapToInt(i -> i.getFrameScore()).sum());
                } else {
                    //tenthFrame.setFrameScore(tenthFrame.getFrameScore() + currentPinsDown); // add the last most roll of the game to tenthFrame score
                    //bowlingGameRepo.save(tenthFrame);
                    frameList = bowlingGameRepo.findAll();
                    model.addAttribute("gameOver", true);
                    model.addAttribute("totalScore", frameList.stream().mapToInt(i -> i.getFrameScore()).sum());
                }
            }
        }

        if (frameList.isEmpty()) {
            // Creat First Frame and set the currentPinsDown to frame.pinsDownFirstRoll
            createNewFrame(currentPinsDown);
        } else {
            // Check the last index of the frameList which would be the last frame. For example : If we are in 4th frame, frameList.size will be 3
            if (frameList.size() < 10) {
                Frame currentFrame = frameList.get(frameList.size() - 1);

                if (currentFrame.isFrameComplete()) {
                    // This is a new Frame
                    createNewFrame(currentPinsDown);
                } else {
                    int pinsDownFirstRoll = currentFrame.getPinsDownFirstRoll();
                    int pinsDownSecondRoll = currentPinsDown;

                    currentFrame.setPinsDownSecondRoll(pinsDownSecondRoll);

                    if (pinsDownFirstRoll + pinsDownSecondRoll == 10) currentFrame.setSpare(true);
                    currentFrame.setFrameScore(pinsDownFirstRoll + pinsDownSecondRoll);
                    currentFrame.setFrameComplete(true);
                    bowlingGameRepo.save(currentFrame);
                }

                // always have the updated list of Frames from persistence layer
                frameList = bowlingGameRepo.findAll();
                int prevIndex = frameList.size() - 2;
                int prevOfPrevIndex = frameList.size() - 3;

                if (prevIndex >= 0) {
                    Frame previousFrame = frameList.get(prevIndex);
                    if (previousFrame != null) {
                        logger.info("Inside Iterator. Check for Strike or Spare in Previous Frame");
                        //Frame previousFrame = frameListIterator.previous();
                        scoreStrikeOrSpare(currentPinsDown, previousFrame);
                    }
                }

                if (prevOfPrevIndex >= 0) {
                    Frame prevOfPreviousFrame = frameList.get(prevOfPrevIndex);
                    logger.info("Inside Iterator. Check for Strike or Spare in Previous->Previous Frame");
                    if (prevOfPreviousFrame != null) {
                        scoreStrikeOrSpare(currentPinsDown, prevOfPreviousFrame);
                    }
                }
            }
        }
        // refresh the frameList with new values from persistence.
        frameList = bowlingGameRepo.findAll();
        for (int frameIndex = 0, rollIndex = 0; frameIndex < frameList.size(); frameIndex++) {
            frameScore[frameIndex] = frameList.get(frameIndex).getFrameScore();
            rolls[rollIndex] = frameList.get(frameIndex).getPinsDownFirstRoll();
            rolls[rollIndex + 1] = frameList.get(frameIndex).getPinsDownSecondRoll();
            rollIndex += 2;
        }

        model.addAttribute("rolls", rolls);
        model.addAttribute("frameScore", frameScore);
        model.addAttribute("frame", frameList);

        return "bowlingGameUI";
    }

    private void scoreStrikeOrSpare(int currentPinsDown, Frame previousFrame) {
        if (previousFrame.isStrike()) {
            if (!previousFrame.isStrikeFristRollHandled()) { // if strike or spare not handled previously, only then add it.
                previousFrame.setFrameScore(previousFrame.getFrameScore() + currentPinsDown);
                previousFrame.setStrikeFristRollHandled(true);
                bowlingGameRepo.save(previousFrame);
            } else if (!previousFrame.isStrikeSecondRollHandled()) {
                previousFrame.setFrameScore(previousFrame.getFrameScore() + currentPinsDown);
                previousFrame.setStrikeSecondRollHandled(true);
                bowlingGameRepo.save(previousFrame);
            }
        }
        if (previousFrame.isSpare()) {
            if (!previousFrame.isSpareHandled()) { // if strike or spare not handled previously, only then add it.
                previousFrame.setFrameScore(previousFrame.getFrameScore() + currentPinsDown);
                previousFrame.setSpareHandled(true);
                bowlingGameRepo.save(previousFrame);
            }
        }
    }

    private void createNewFrame(int pinsDown) {
        Frame frame = new Frame();
        frame.setPinsDownFirstRoll(pinsDown);
        frame.setFrameScore(pinsDown);
        if (pinsDown == 10) {
            frame.setStrike(true);
            frame.setFrameComplete(true);
        }
        bowlingGameRepo.save(frame);
    }

    @GetMapping("/bowlingGame/home")
    public String viewBowlingUI(Model model) {
        model.addAttribute("frame", new Frame());
        return "home";
    }

}
