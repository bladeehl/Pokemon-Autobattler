package com.github.bladeehl.services;

import com.github.bladeehl.model.ConsoleHistory;
import lombok.NonNull;

import java.util.List;

public interface ConsoleWebService {
    String getCurrentOutput();

    String processInput(@NonNull final String input);

    List<ConsoleHistory> getHistorySince(long lastEntryId);
}
