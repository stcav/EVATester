/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eva.tester.processor;

import eva.tester.config.AppConfig;
import eva.tester.conn.RestClient;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.university.stcav.eva.model.MediaElement;
import org.university.stcav.eva.processor.Processor;

/**
 *
 * @author johaned
 */
public class Tester implements Runnable{
    
    public static int RESIZE_VIDEO_HD = 0;
    // MediaElement instance that points to real media object
    private MediaElement me;
    // MedoaElement that contains the final configuration (output config)
    private MediaElement meConfig;
    // Process Name
    private String id;
    // Action List
    private List<Integer> actions;
    // execute from local
    private Boolean executeFromLocal = false;
    // communication interface
    private RestClient rc;

    public Tester(MediaElement me, MediaElement meConfig, List actions) throws UnknownHostException, InterruptedException, IOException {
        this.me = me;
        this.meConfig = meConfig;
        this.actions = actions;
        this.id = String.valueOf(System.currentTimeMillis());
        this.meConfig.setName(meConfig.getName()+this.id+"/"+this.id+".mp4");
        this.rc = new RestClient();
        buildTestScenary();    
    }

    public Boolean getExecuteFromLocal() {
        return executeFromLocal;
    }

    public void setExecuteFromLocal(Boolean executeFromLocal) {
        this.executeFromLocal = executeFromLocal;
    }
    
    public String getCommand(int process) throws InterruptedException, IOException{
        AppConfig appConfig = AppConfig.getInstance();
        String command = "";
        switch(process){
            // adapt video to hd
            case 0:
                command = Processor.resize_video_hd(me, meConfig, System.getProperty("user.dir")+"/"+appConfig.getProperty("media_path"), false, false);
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
            case 10:
                break;
            case 11:
                break;
            default:
                break;
        }
        System.err.println("--> command: "+command);
        return command;
    }

    @Override
    public void run() {
        AppConfig appConfig=null;
        try {
            appConfig = AppConfig.getInstance();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
        }
        Iterator iterator = actions.iterator();
        String command;
        while(iterator.hasNext()){
            try {
                command = getCommand((Integer) iterator.next());
                if(!executeFromLocal){
                    String response = rc.get(appConfig.getProperty("process_executor_server_url") + command.replace(" ", "¬"));
                    System.out.println("[LOG] from <"+id+"> :"+response);
                }else{
                    ProcessExecutor.execute_process(command.split(" "), "/", true);
                }
            } catch (    InterruptedException | IOException ex) {
                Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void buildTestScenary() throws UnknownHostException, InterruptedException, IOException{
        AppConfig appConfig = AppConfig.getInstance();
        String command = "mkdir "+this.id;
        String mediaPath = System.getProperty("user.dir")+"/"+appConfig.getProperty("media_path");
        String outputPath = System.getProperty("user.dir")+"/"+appConfig.getProperty("output_path");
        
        ProcessExecutor.execute_process(command.split(" "), outputPath, true);
        
        command = "cp "+this.me.getName()+" "+outputPath+"/"+this.id;
        ProcessExecutor.execute_process(command.split(" "), mediaPath, true);
        
        this.me.setName(meConfig.getName()+this.id+"/"+ me.getName());
        
    }
    
}
