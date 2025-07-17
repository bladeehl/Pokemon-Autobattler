package com.github.bladeehl;

import com.github.bladeehl.config.AppConfig;
import com.github.bladeehl.services.DatabaseHelper;
import com.github.bladeehl.ui.ConsoleUI;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(final String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(DatabaseHelper::shutdown));

        new AnnotationConfigApplicationContext(AppConfig.class)
            .getBean(ConsoleUI.class)
            .run();
    }
}
