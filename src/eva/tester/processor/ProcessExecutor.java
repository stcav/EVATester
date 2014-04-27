/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eva.tester.processor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author johaned
 *
 */
public class ProcessExecutor {

    public static ProcessorResponse execute_process(String[] comando, String path, boolean debug) throws InterruptedException, IOException {
        File f = new File(path);
        ProcessorResponse pr = new ProcessorResponse();
        try {
            Process p = Runtime.getRuntime().exec(comando, null, f);
            p.waitFor();   
            System.out.println("Comando: " + comando + " \n");
            BufferedReader stdOut = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            return pr;
        } catch (IOException e) {
            System.out.println("Excepción: ");
            e.printStackTrace();
            return null;
        }
    }
}
