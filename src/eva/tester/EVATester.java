/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eva.tester;

import com.google.gson.Gson;
import eva.tester.config.AppConfig;
import java.io.IOException;
import org.university.stcav.eva.model.MediaElement;
import org.university.stcav.eva.processor.Processor;

/**
 *
 * @author johaned
 */
public class EVATester {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) throws InterruptedException, IOException {
        AppConfig appConfig = AppConfig.getInstance();
        MediaElement me = Processor.get_mediaElement(appConfig.getProperty("video_name"), System.getProperty("user.dir")+"/"+appConfig.getProperty("media_path"), true);
        
    }
}
