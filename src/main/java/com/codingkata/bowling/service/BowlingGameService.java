package com.codingkata.bowling.service;


import com.codingkata.bowling.model.BowlingGame;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BowlingGameService {

    private List<BowlingGame> games = new ArrayList<>();

    public List<BowlingGame> getGames() {
        return games;
    }

    public void addGame(BowlingGame game) {
        games.add(game);
    }
}
