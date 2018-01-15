package com.tomorth.commander;

import com.tomorth.commander.annotations.Command;
import com.tomorth.commander.annotations.CommandSet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class Commander {
    private Commander() {
        throw new IllegalStateException("Utility Class");
    }
    public static void parse(Class c, String[] args) {
        if (c.getAnnotation(CommandSet.class) != null) {
            methodParse(c, args);
        }
        else {
            throw new IllegalStateException("Class must have the CommandSet annotation in order to run");
        }
    }
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
                           Class type = params[0].getType();
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
                    System.err.println("PARAM: " + paramValues.size());
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static Map<String, Method> methodMap(Class c) {
        Map<String, Method> methods = new HashMap<>();
        for (Method m : c.getDeclaredMethods()) {
            methods.put(m.getName(), m);
        }
        return methods;
    }

    private static List<Object> buildList(Map<String, Method> methods, String[] args, int i) {
        List<Object> objList = new ArrayList<>();
        String temp = "";
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
