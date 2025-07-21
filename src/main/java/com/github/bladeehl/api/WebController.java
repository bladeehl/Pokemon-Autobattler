package com.github.bladeehl.api;

import com.github.bladeehl.services.ConsoleWebService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/console")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@Tag(name = "Pokemon API", description = "API для работы с тренерами и покемонами")
public class WebController {
    ConsoleWebService consoleWebService;

    @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(summary = "Получить текущее состояние консоли",
            description = "Возвращает текущее состояние консоли в виде текста")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Вывод консоли"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public String getConsoleOutput() {
        return consoleWebService.getCurrentOutput();
    }

    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(summary = "Отправить ввод",
            description = "Посылает ввод и возвращает вывод")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ввод обработан"),
            @ApiResponse(responseCode = "400", description = "Неверный ввод"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public String sendConsoleInput(@NonNull @RequestBody final String input) {
        return consoleWebService.processInput(input);
    }
}
