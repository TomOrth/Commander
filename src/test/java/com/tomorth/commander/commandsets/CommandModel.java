package com.tomorth.commander.commandsets;

import com.tomorth.commander.annotations.Command;
import com.tomorth.commander.annotations.CommandSet;

import java.util.List;

@CommandSet
public class CommandModel {

    @Command
    public void print(String s) {
        System.out.println(s);
    }

    @Command
    public void printList(List<String> s) {
        System.out.println(s.get(0) + " " + s.get(1));
    }

    @Command
    public void printComplex(String a, List<String> list) {
        System.out.println(a + " " + list.get(0) + " " + list.get(1));
    }

    @Command
    public void printListFail(List<String> list, String a) {
        System.out.println(a + " " + list.get(0) + " " + list.get(1));
    }
}
