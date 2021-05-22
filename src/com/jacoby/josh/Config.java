package com.jacoby.josh;
/** 
 * Author: Jacoby Blanke
 * Program Name: (J)ava (O)riented (Sh)ell - or JOSh 
 * Description: A basic interactive shell written in java. Builtin commands 
 *       include: "cd", "history", and "exit". * Date: April 29, 2021 
 */
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;

public class Config {
    //Some helpful variables to use when setting your prompt
    private static String name = System.getProperty("user.name");
    private static String dir = System.getProperty("user.dir");
    private static String date = java.time.LocalDate.now().toString();
    private static String time = java.time.LocalTime.now().toString();

    public static final String PROMPT = //sets the shell's command prompt 
        "(" + name + ":" + dir + ":" + date + ") - ";
    
}

