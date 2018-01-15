package com.tomorth.commander.commandsets;

import com.tomorth.commander.annotations.Command;

public class MalformedCommandSet {

    @Command
    public void print(String s) {
        System.out.println(s);
    }
}
