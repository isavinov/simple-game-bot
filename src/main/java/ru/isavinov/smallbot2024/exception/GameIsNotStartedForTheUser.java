package ru.isavinov.smallbot2024.exception;

public class GameIsNotStartedForTheUser extends RuntimeException {

    public GameIsNotStartedForTheUser(Long userId){
        super("Game has not been started for the user "+userId);
    }
}
