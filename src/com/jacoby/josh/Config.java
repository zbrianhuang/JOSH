package com.jacoby.josh;
/** 
 * Author: Jacoby Blanke
 * Program Name: (J)ava (O)riented (Sh)ell - or JOSh 
 */
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;

public class Config {
    //Some helpful variables to use when setting your prompt
    private static String name = System.getProperty("user.name");
    private static String dir;
    private static String date = java.time.LocalDate.now().toString();
    private static String time = java.time.LocalTime.now().toString();
    
    public static String PROMPT(int append) { 
        try {
            dir = Files.readAllLines(Paths.get(System.getProperty("java.io.tmpdir")
                            + "/joshdir-" + append)).get(0) + '/';
        } catch (Exception e) {
            dir = System.getProperty("user.dir");
        }

        return "(" + name + ":" + dir + ":" + date + ") - "; //this is where you actually change the prompt.
    }    
}

