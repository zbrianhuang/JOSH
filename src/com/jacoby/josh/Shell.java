package com.jacoby.josh;
/**
 * Author: Jacoby Blanke 
 * Program Name: (J)ava (O)riented (Sh)ell - or JOSh 
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
import javax.swing.JFrame;
import javax.swing.JRootPane;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class Shell {  
    private static int append;  
    private static String currentDir, historyArray[] = new String[500];  
    private static int in;

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
        String command = "";  
        Scanner in = new Scanner(System.in); 
        System.out.print("\n" + conf.PROMPT(append));  
        
        for (;;) {
            int i = getCh();
            if (i == 10) 
                break;
            else if (i == 9) {
                System.out.print(autoComplete(command));
                command += autoComplete(command);
            }
            else {
                command += (char)i;
                System.out.print((char)i);
            }
        }

        System.out.println();
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
    
    public static String autoComplete(String command) {
            
        List<String> results = new ArrayList<String>();

        File[] bin = new File("/bin").listFiles();
        File[] sbin = new File("/sbin").listFiles();
        File[] usrBin = new File("/usr/bin").listFiles();
        File[] usrSbin = new File("/usr/sbin").listFiles();
        for (File file : bin) {
            if (file.isFile())
                results.add(file.getName());
        }
        for (File file : sbin) {
            if (file.isFile())
                results.add(file.getName());
        }
        for (File file : usrBin) {
            if (file.isFile())
                results.add(file.getName());
        }
        for (File file : usrSbin) {
            if (file.isFile())
                results.add(file.getName());
        }
        
        for (int i = 0; i < results.size(); i++) {
            if (results.get(i).startsWith(command)) {
                return results.get(i).replaceFirst(command, "");
            }
        }
        return "";
    }

    public static int getCh() {
        final JFrame frame = new JFrame();
        synchronized (frame) {
            frame.setUndecorated(true);
            frame.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
            frame.setAlwaysOnTop(true);
            frame.setFocusTraversalKeysEnabled(false);
            frame.addKeyListener(new KeyListener() {
                @Override
                public void keyPressed(KeyEvent e) {
                    synchronized (frame) {
                        frame.setVisible(false);
                        frame.dispose();
                        frame.notify();
                    }
                    if (e.getKeyCode() != 9 && e.getKeyCode() != 10)
                        in = (int) e.getKeyChar();
                    else 
                        in = e.getKeyCode();
                }
                @Override
                public void keyReleased(KeyEvent e) {
                }
                @Override
                public void keyTyped(KeyEvent e) {
                }
            });
            frame.setVisible(true);
            try {
                frame.wait();
            } catch (InterruptedException e1) {
            }
        }
        return in;
    }
}
