/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eva.tester.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author johaned
 */
public class AppConfig {
    
    private static AppConfig instance;
    private Map<String, String> descriptor;
    
     public static AppConfig getInstance() throws UnknownHostException {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }
     
     public AppConfig(){
        InputStream yml = null;
        try {
            yml = new FileInputStream(new File(System.getProperty("user.dir")+"/src/eva/tester/config/config.yml"));
            Yaml yaml = new Yaml();
            descriptor = (Map<String, String>) yaml.load(yml);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AppConfig.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                yml.close();
            } catch (IOException ex) {
                Logger.getLogger(AppConfig.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
     }
     
     public String getProperty(String key){
         return descriptor.get(key);
     }
    
}
