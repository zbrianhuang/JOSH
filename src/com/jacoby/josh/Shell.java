package com.jacoby.josh;
/**
 * Author: Jacoby Blanke 
 * Program Name: (J)ava (O)riented (Sh)ell - or JOSh 
 * Description: A basic interactive shell written in java. Builtin commands 
 *      include: "cd", "history", and "exit". * Date: April 29, 2021 
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Shell {  
    private static int append;  
    private static String currentDir, historyArray[] = new String[500];  
    public Shell(int app, String dir) {     
        append = app;    
        currentDir = dir;
    }  
    public static void cd(String directory) {  
        if (directory.startsWith("~")) {       
            File f = new File(System.getProperty("user.home") + "/" + directory.substring(2));      
            if (f.isDirectory()) {        
                try {          
                    PrintWriter writer = new PrintWriter(
                            new FileOutputStream(System.getProperty("java.io.tmpdir")             
                                + "/joshdir-" + append, false));          
                    writer.println((System.getProperty("user.home") + '/' + directory.substring(2))
                            .replaceAll("//","/").replaceAll(".$", ""));
                    writer.close();        
                } catch (Exception e) {System.out.println(e);}      
            } else        
                System.out.println("No such directory");    
        } else if (directory.startsWith("/")) {     
            File f = new File(directory);      
            if (f.isDirectory()) {        
                try {          
                    PrintWriter 
                        writer = new PrintWriter(new FileOutputStream(System.getProperty("java.io.tmpdir")            
                                    + "/joshdir-" + append, false));          
                    writer.println(directory.replaceAll("//","/").replaceAll(".$", ""));          
                    writer.close();        
                } catch (Exception e) {System.out.println(e);}      
            }    
        } else {      
            File f = new File(currentDir + '/' + directory);      
            if (f.isDirectory()) {        
                try {         
                    PrintWriter writer = new PrintWriter(new FileOutputStream(System.getProperty("java.io.tmpdir")            
                                + "/joshdir-" + append, false));          
                    writer.println((currentDir + '/' + directory).replaceAll("//", "/").replaceAll(".$", ""));
                    writer.close();       
                } catch (Exception e) {System.out.println(e);}   
            }    
        }  
    }  
    public static void execute(String command) { 
        try{      
            File cmdHistory = new File(System.getProperty("user.home") + "/.josh_history"); 
            if(!cmdHistory.exists())        
                cmdHistory.createNewFile();      
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(cmdHistory,true)));      
            writer.println(command);      
            writer.close();      
            String[] commandArray = command.split(" ");      
            ProcessBuilder processBuilder = new ProcessBuilder(commandArray);
            processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);      
            processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);      
            processBuilder.redirectInput(ProcessBuilder.Redirect.INHERIT);  
            processBuilder.directory(new File(currentDir)); 
            Process p = processBuilder.start();  
            p.waitFor(); 
        } catch (Exception e) {      
            System.out.println("Command not found.");  
        }  
    } 
    public static void history(String cmd) { 
        try {   
            BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.home") + "/.josh_history")); 
            String line;  
            for (int i = 0; (i <= 500 && ((line = br.readLine()) != null)); i++)  
                historyArray[i] = line; 
        } catch (Exception e) { 
            System.out.println("History file inaccessible");
        }    
        for (int i = 0; i < 500; i++) {   
            if (historyArray[i] != null) 
                System.out.println(historyArray[i]);   
        } 
    }
    public static boolean run(String dir) {
        Config conf = new Config();
        boolean exit = false; 
        String command;  
        Scanner in = new Scanner(System.in); 
        System.out.print("\n" + conf.PROMPT(append));  
        command = in.nextLine(); 
        if (command.startsWith("cd "))  
            cd(command.substring(3));
        else if (command.trim().equals("history"))   
            history(command); 
        else if (command.trim().length() == 0) {}
        else if (command.trim().equals("exit"))  
            exit = true;  
        else    
            execute(command);
        return exit;
    }  
}
