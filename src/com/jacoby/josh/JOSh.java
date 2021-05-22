package com.jacoby.josh;
/** 
 * Author: Jacoby Blanke
 * Program Name: (J)ava (O)riented (Sh)ell - or JOSh 
 */

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

class JOSh {  
    public static void main(String args[]) {    
        String dir;    
        Random r = new Random();    
        int append = r.nextInt(10000000);     
        Shell cmd;    
        for (;;) {       
            try {        
                dir = Files.readAllLines(Paths.get(System.getProperty("java.io.tmpdir") 
                            + "/joshdir-" + append)).get(0) + '/';      
            } catch (Exception e) {        
                dir = System.getProperty("user.dir");
            }      
            cmd = new Shell(append, dir);      
            if (cmd.run(dir))        
                break;    
        }  
    }
}
