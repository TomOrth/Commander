package com.tomorth.commander;

import com.tomorth.commander.annotations.Command;
import com.tomorth.commander.annotations.CommandSet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * Main entry point for the Commander library
 */
public class Commander {

    /**
     * Private constructor to prevent generation of the public constructor
     */
    private Commander() {
        throw new IllegalStateException("Utility Class");
    }

    /**
     * Method that allows for parsing of a class c with the set of command line args
     * @param c The class being parsed
     * @param args The args from the command line
     */
    public static void parse(Class c, String[] args) {
        if (c.getAnnotation(CommandSet.class) != null) {
            methodParse(c, args);
        }
        else {
            throw new IllegalStateException("Class must have the CommandSet annotation in order to run");
        }
    }

    /**
     * Private internal method to perform parsing once the class c has been verified to have {@link com.tomorth.commander.annotations.CommandSet} annotation
     * @param c The class being parsed
     * @param args The args from the command line
     */
    private static void methodParse(Class c, String[] args) {
        List<String> argList = Arrays.asList(args);
        Map<String, Method> methods = methodMap(c);
        List<Object> paramValues = new ArrayList<>();
        for (int i = 0; i < argList.size()-1; ++i) {
            String arg = argList.get(i);
            if (arg.startsWith("--") && methods.containsKey(argList.get(i).substring(2))) {
               Method m = methods.get(argList.get(i).substring(2));
               if (m.getAnnotation(Command.class) != null) {
                   Parameter[] params = m.getParameters();
                   int count =  m.getParameterCount();
                   for (int j = 0; j < count; ++j) {
                       if (params[j].getType().equals(List.class)) {
                           if (j != count-1) {
                               throw new IllegalStateException("A parameter of type list must be the last parameter in a method");
                           }
                           else {
                               List<Object> listParam = buildList(methods, args, i+1);
                               if (!listParam.isEmpty()) paramValues.add(listParam);
                           }
                       }
                       else {
                           paramValues.add(args[i+j+1]);
                           ++i;
                       }
                   }
               }
                try {
                    Object obj = c.newInstance();
                    m.invoke(obj, paramValues.toArray());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Maps all the methods in the Class c to its string name
     * @param c The class with the methods
     * @return A Map where the class methods are mapped to a string key
     */
    private static Map<String, Method> methodMap(Class c) {
        Map<String, Method> methods = new HashMap<>();
        for (Method m : c.getDeclaredMethods()) {
            methods.put(m.getName(), m);
        }
        return methods;
    }

    /**
     * Helper method to build a List parameter for an invoked method
     * @param methods Map of the methods to check when to stop building
     * @param args The command line args
     * @param i The iterator count for the loop
     * @return A List object for an invoked method
     */
    private static List<Object> buildList(Map<String, Method> methods, String[] args, int i) {
        List<Object> objList = new ArrayList<>();
        String temp;
        for (; i < args.length; ++i) {
            temp = args[i];
            if (temp.startsWith("--")) {
                if (methods.containsKey(temp.substring(2))) {
                    break;
                }
            }
            objList.add(temp);
        }
        return objList;
    }
}