/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eva.tester.processor;

import eva.tester.EVATester;
import eva.tester.config.AppConfig;
import eva.tester.conn.RestClient;
import eva.tester.model.Action;
import eva.tester.model.MediaProcess;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
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
    // Perfomed Action List
    private List<Action> performedActions;
    // execute from local
    private Boolean executeFromLocal = false;
    // communication interface
    private RestClient rc;
    // thread pointer
    private Thread process;

    public Tester(MediaElement me, MediaElement meConfig, List actions) throws UnknownHostException, InterruptedException, IOException {
        this.me = me;
        this.meConfig = meConfig;
        this.actions = actions;
        this.id = String.valueOf(System.currentTimeMillis());
        this.performedActions = new ArrayList<>();
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
        Action action;
        int actionId;
        long startTime, endTime;
        while(iterator.hasNext()){
           actionId = (Integer) iterator.next();
           action = new Action();
           action.setId(actionId);
            try {
                command = getCommand(actionId);
                action.setStartTime(System.currentTimeMillis());
                if(!executeFromLocal){
                    String response = rc.get(appConfig.getProperty("process_executor_server_url") + command.replace(" ", "¬"));
                    System.out.println("[LOG] from <"+id+"> :"+response);
                }else{
                    ProcessExecutor.execute_process(command.split(" "), "/", true);
                }
                action.setEndTime(System.currentTimeMillis());
                action.setSuccess(true);
            } catch (    InterruptedException | IOException ex) {
                action.setSuccess(false);
                Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
            }
            performedActions.add(action);
        }
        MediaProcess mp = new MediaProcess();
        mp.setId(this.id);
        mp.setMe(this.me);
        mp.setMeConfig(this.meConfig);
        mp.setActions(performedActions);
        EVATester.report.addMediaProcess(mp);
    }
    
    private void buildTestScenary() throws UnknownHostException, InterruptedException, IOException{
        AppConfig appConfig = AppConfig.getInstance();
        String command = "mkdir "+this.id;
        String mediaPath = System.getProperty("user.dir")+"/"+appConfig.getProperty("media_path");
        String outputPath = System.getProperty("user.dir")+"/"+appConfig.getProperty("output_path");
        
        ProcessExecutor.execute_process(command.split(" "), outputPath, true);
        
        command = "cp "+this.me.getName()+" "+outputPath+"/"+this.id;
        ProcessExecutor.execute_process(command.split(" "), mediaPath, true);
        
        this.me.setName(meConfig.getName()+this.id+"/"+ this.me.getName());
        this.meConfig.setName(meConfig.getName()+this.id+"/"+this.id+".mp4");
    }

    public void execute() {
       process = new Thread(this);
       process.start();
    }
    
}
