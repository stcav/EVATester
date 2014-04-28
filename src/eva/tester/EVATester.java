/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eva.tester;

import com.google.gson.Gson;
import eva.tester.config.AppConfig;
import eva.tester.model.Report;
import eva.tester.processor.Tester;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.university.stcav.eva.model.AudioElement;
import org.university.stcav.eva.model.MediaElement;
import org.university.stcav.eva.model.VideoElement;
import org.university.stcav.eva.processor.Processor;

/**
 *
 * @author johaned
 */
public class EVATester {

    /**
     * @param args the command line arguments
     */
    public static Report report;
    
    public static void main(String[] args) throws InterruptedException, IOException {
        AppConfig appConfig = AppConfig.getInstance();
        MediaElement me_4_3 = Processor.get_mediaElement("4_3.avi", System.getProperty("user.dir")+"/"+appConfig.getProperty("media_path"), true);
        MediaElement me_16_9 = Processor.get_mediaElement("16_9.mp4", System.getProperty("user.dir")+"/"+appConfig.getProperty("media_path"), true);
        MediaElement meConfig = new MediaElement(new AudioElement("libfdk_aac", "48000", "stereo", "224k"), new VideoElement("libx264", "1280x720", "16:9", "2000k", "25"), System.getProperty("user.dir")+"/"+appConfig.getProperty("output_path")+ "/");
        
        // creating report
        report = new Report();
        report.setDate(new Date(System.currentTimeMillis()));
        
        List actions = new ArrayList();
        actions.add(Tester.RESIZE_VIDEO_HD);
        
        List<Tester> threadMaster = new ArrayList<>();
        Iterator iter;
        threadMaster.add(new Tester(new MediaElement(me_16_9), new MediaElement(meConfig), actions));
        iter = threadMaster.iterator();
        
        while(iter.hasNext()){
            ((Tester)iter.next()).execute();
        }
        
        // it waits that all threads have finished
        iter = threadMaster.iterator();
        while(iter.hasNext()){
            ((Tester)iter.next()).join();
        }
             
        Gson gson = new Gson();
        System.err.println("output: "+gson.toJson(report));
    }
}
