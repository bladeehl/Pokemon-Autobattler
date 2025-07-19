package com.github.bladeehl.io;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.PrintStream;
import java.util.Scanner;

@RequiredArgsConstructor
@Component
public class IOContext {
    final Scanner in;
    final PrintStream out;

    public IOContext() {
        this.in = new Scanner(System.in);
        this.out = new PrintStream(System.out, true);
    }

    public Scanner in() {
        return in;
    }

    public PrintStream out() {
        return out;
    }
}
