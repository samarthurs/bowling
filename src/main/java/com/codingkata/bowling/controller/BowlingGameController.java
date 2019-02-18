package com.codingkata.bowling.controller;

import com.codingkata.bowling.model.Frame;
import com.codingkata.bowling.repository.BowlingGameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;


@Controller
@RequestMapping(value = "/bowling")
public class BowlingGameController {

    private static final Logger logger = LoggerFactory.getLogger(BowlingGameController.class);

    @Autowired
    private BowlingGameRepository bowlingGameRepo;

    @GetMapping("/play")
    public String scoreBowlingGame(@RequestParam(value = "roll", required = false) String roll, @ModelAttribute @Valid Frame newFrame, BindingResult bindingResult, Model model, HttpServletRequest request) {

        int[] rolls = new int[21];
        int[] frameScore = new int[10];

        int rollIndex = 0;

        //rolls[rollIndex++] = Integer.parseInt(roll);

        List<Frame> frameList = bowlingGameRepo.findAll();

        int pinsDown = Integer.valueOf(roll);
        //pinsDown = Integer.valueOf(request.getParameter("roll"));
        //if (bowlingGameRepo.findById(Long.valueOf(1L)) != null) {

        if(frameList.size() == 10)bowlingGameRepo.deleteAll();

        if (frameList.isEmpty()) {
            // First Frame
            createNewFrame(pinsDown);
        }else{
            for (Frame frame : frameList){

                if(frame != null && frame.isFrameComplete()){
                    // This is a new Frame
                    createNewFrame(pinsDown);
                }else{
                    int pinsDownFirstRoll = frame.getPinsKnockedDownOnFirstRoll();
                    int pinsDownSecondRoll = pinsDown;
                    if(pinsDownFirstRoll != 0)pinsDownSecondRoll = pinsDown;

                    if(pinsDownFirstRoll + pinsDownSecondRoll == 10)frame.setSpare(true);

                    frame.setFrameScore(pinsDownFirstRoll + pinsDownSecondRoll);
                    frame.setFrameComplete(true);
                }


            }
        }


        //frameList.stream().forEach(frame -> frameScore[0] = frame.getFrameScore());
        //Arrays.asList(frameScore).replaceAll();

        for(int i = 0; i<frameList.size(); i++){
            frameScore[i] = frameList.get(i).getFrameScore();
        }
        //model.addAttribute("frame",frame);

        model.addAttribute("rolls", rolls);
        model.addAttribute("frameScore", frameScore);

        return "bowlingGameUI";
    }

    private void createNewFrame(int pinsDown) {
        Frame frame = new Frame();
        frame.setPinsKnockedDownOnFirstRoll(pinsDown);
        if (pinsDown == 10) {
            frame.setStrike(true);
        }
        bowlingGameRepo.save(frame);
    }

    @GetMapping(value = "/")
    public String viewBowlingUI(Model model) {
        Frame newFrame = new Frame();

        bowlingGameRepo.save(newFrame);
        model.addAttribute("frame", newFrame);
        return "bowlingGameUI";
    }

}
