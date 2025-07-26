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

@RestControllerAdvice
    class GlobalExceptionHandler {
    record ErrorResponse(
        @NonNull String message,
        @NonNull String details) {}

    @ExceptionHandler(EqualPokemonsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleEqualPokemonsException(
        @NonNull final EqualPokemonsException thrown) {

        return new ErrorResponse("Bad Request", thrown.getMessage());
    }

    @ExceptionHandler(BattleEndedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBattleEndedException(
        @NonNull final BattleEndedException thrown) {

        return new ErrorResponse("Bad Request", thrown.getMessage());
    }

    @ExceptionHandler(BattleNotEndedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBattleNotEndedException(
        @NonNull final BattleNotEndedException thrown) {

        return new ErrorResponse("Bad Request", thrown.getMessage());
    }

    @ExceptionHandler(UnsupportedPokemonTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUnsupportedPokemonTypeException(
        @NonNull final UnsupportedPokemonTypeException thrown) {

        return new ErrorResponse("Bad Request", thrown.getMessage());
    }
}
