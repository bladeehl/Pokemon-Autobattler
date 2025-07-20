package com.github.bladeehl.api;

import com.github.bladeehl.exceptions.BattleEndedException;
import com.github.bladeehl.exceptions.BattleNotEndedException;
import com.github.bladeehl.exceptions.EqualPokemonsException;
import com.github.bladeehl.exceptions.UnsupportedPokemonTypeException;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/*
* TODO: накатать исключения для всего, что может
*  произойти в контроллерах
 */

@RestControllerAdvice
public class GlobalExceptionHandler {
    record ErrorResponse(String message, String details) {}

    @ExceptionHandler(EqualPokemonsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleEqualPokemonsException(
        final @NonNull EqualPokemonsException thrown) {
        return new ErrorResponse("Bad Request", thrown.getMessage());
    }

    @ExceptionHandler(BattleEndedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBattleEndedException(
        final @NonNull BattleEndedException thrown) {
        return new ErrorResponse("Bad Request", thrown.getMessage());
    }

    @ExceptionHandler(BattleNotEndedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBattleNotEndedException(
        final @NonNull BattleNotEndedException thrown) {
        return new ErrorResponse("Bad Request", thrown.getMessage());
    }

    @ExceptionHandler(UnsupportedPokemonTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUnsupportedPokemonTypeException(
        final @NonNull UnsupportedPokemonTypeException thrown) {
        return new ErrorResponse("Bad Request", thrown.getMessage());
    }
}
