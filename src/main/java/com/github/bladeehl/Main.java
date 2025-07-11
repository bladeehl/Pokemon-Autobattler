package com.github.bladeehl;

import com.github.bladeehl.services.DatabaseHelper;
import com.github.bladeehl.ui.ConsoleUI;
import lombok.val;

public class Main {
    public static void main(final String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(DatabaseHelper::shutdown));
        val ui = new ConsoleUI();
        ui.run();
    }
}
