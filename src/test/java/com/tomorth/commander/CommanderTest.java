package com.tomorth.commander;

import com.tomorth.commander.commandsets.CommandModel;
import com.tomorth.commander.commandsets.MalformedCommandSet;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class CommanderTest {
    ByteArrayOutputStream bo;
    @Before
    public void setup() {
        bo = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bo));
    }

    @Test(expected = IllegalStateException.class)
    public void testCommandSetException() {
        Commander.parse(MalformedCommandSet.class, new String[]{"--print", "test"});
    }

    @Test()
    public void testCommandExecution() throws Exception {
        Commander.parse(CommandModel.class, new String[]{"--print", "Hello Test"});
        String lines = new String(bo.toByteArray());
        assertTrue(lines.contains("Hello Test"));
    }

    @Test()
    public void testCommandExecutionList() throws Exception {
        Commander.parse(CommandModel.class, new String[]{"--printList", "Hello", "World"});
        String lines = new String(bo.toByteArray());
        assertTrue(lines.contains("Hello World"));
    }

    @Test()
    public void testCommandExecutionComplex() throws Exception {
        Commander.parse(CommandModel.class, new String[]{"--printComplex", "Hello", "there,", "World"});
        String lines = new String(bo.toByteArray());
        assertTrue(lines.contains("Hello there, World"));
    }

    @Test(expected = IllegalStateException.class)
    public void testCommandListException() throws Exception {
        Commander.parse(CommandModel.class, new String[]{"--printListFail", "Hello", "there,", "World"});
    }
}
