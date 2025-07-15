package com.github.bladeehl;

import com.github.bladeehl.services.DatabaseHelper;
import com.github.bladeehl.ui.ConsoleUI;

public class Main {
    public static void main(final String[] args) {
        Runtime.getRuntime()
            .addShutdownHook(new Thread(DatabaseHelper::shutdown));

        new ConsoleUI().run();
    }
}
