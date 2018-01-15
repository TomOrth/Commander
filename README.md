# Commander
Annotation based command line parser

## Setup
In order to use this library, add `jitpack.io` to the `build.gradle`:
```gradle
repositories {
    ...
    maven { url 'https://jitpack.io' }
    ...
}
```
Then add the library to dependencies:
```gradle
dependencies {
    ...
    implementation 'com.github.tomorth:commander:1.0.0'
    ...
}
```

## Usage
Now to start off, create a file called `CommandModel.java`:
```java
import com.tomorth.commander.annotations.Command;
import com.tomorth.commander.annotations.CommandSet;

@CommandSet
public class CommandModel {

    @Command
    public void print(String s) {
        System.out.println(s);
    }
    
    @Command
    public void printTwo(String s, String t) {
        Systen.out.println(s + " " + t);
    }
}
```
This is an example of a set of commands to parse.  A class to be parsed must have the `@CommandSet` annotation on it and a method to be parsed must have the `@Command` annotation on it.

Now inside your main class: 
```java
import com.tomorth.commander.Commander;

public class Main {
    public static void main(String[] args) {
        Commander.parse(CommandModel.class, args);
    }
}
```
This will result in the command line arguments being passed to `Commander` and parsed to invoke the correct methods

The next step is to pass the command line arguments when you run the gradle program.

The method I have found to do this:
1. Add to your `build.gradle` the following line: `apply plugin: 'application'`
2. Define the property `mainClass` property in your `build.gradle` file
3. Define the following task:
```task
run {
    args findProperty('args').split(" ")
}
```
4. Run the following command from the terminal: `gradlew run -Pargs=--print Hello` (Note: Methods to be invoked must be prepended with a -- in the arguments)

This will result in printing to the terminal "Hello"

## Additional Notes
* Any parameter for a method of type `List` must be the last parameter
* Please see [CONTRIBUTING.md](CONTRIBUTING.md) for contribution guidelines