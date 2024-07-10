package ru.isavinov.smallbot2024.service;

import org.springframework.stereotype.Component;
import ru.isavinov.smallbot2024.exception.GameIsNotStartedForTheUser;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
public class SimpleGame {

    private final Map<Long, Integer> scores = new HashMap<>();

    private final Random random = new Random();

    public void startGame(Long userId){
        scores.put(userId, random.nextInt(0,10));
    }

    public String checkAnswer(Long userId, Integer answer){
        if (!scores.containsKey(userId)){
            throw new GameIsNotStartedForTheUser(userId);
        }

        if(answer > scores.get(userId)){
            return "Меньше";
        } else if(answer < scores.get(userId)){
            return "Больше";
        } else{
            startGame(userId);
            return "Поздравляю! Загадано новое число";
        }
    }
}
