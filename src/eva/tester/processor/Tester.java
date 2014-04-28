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
import org.university.stcav.eva.model.FontElement;
import org.university.stcav.eva.model.MediaElement;
import org.university.stcav.eva.processor.Processor;

/**
 *
 * @author johaned
 */
public class Tester implements Runnable{
    
    public static int RESIZE_VIDEO_HD = 0;
    public static int TRANSCODE_VIDEO_PALMPEG2 = 1;
    public static int TRANSCODE_AUDIO_PALMPEG2 = 2;
    public static int CUT_VIDEO_HD = 3;
    public static int CREATE_VIDEO_FROM_IMAGE = 4;
    public static int INSERT_SILENCE_TO_VIDEO = 5;
    public static int CREATE_IMAGE_FROM_VIDEO = 6;
    public static int INSERT_TEXT_TO_VIDEO = 7;
    public static int INSERT_TEXT_TO_VIDEO_IN_TIME = 8;
    public static int INSERT_TEXT_FROM_FILE_TO_VIDEO = 9;
    public static int INSERT_TEXT_FROM_FILE_TO_VIDEO_IN_TIME = 10;
    public static int DO_FADE_TO_VIDEO = 11;
    public static int DO_MERGE_VIDEOS_X264_RETURN = 12;
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
    // origin path
    private String originPath;

    public Tester(MediaElement me, MediaElement meConfig, List actions) throws UnknownHostException, InterruptedException, IOException {
        AppConfig appConfig = AppConfig.getInstance();
        this.me = me;
        this.meConfig = meConfig;
        this.actions = actions;
        this.id = String.valueOf(System.currentTimeMillis());
        this.performedActions = new ArrayList<>();
        this.rc = new RestClient();
        this. originPath = System.getProperty("user.dir")+"/"+appConfig.getProperty("media_path");
        appConfig = null;
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
        FontElement fe;
        String command = "";
        switch(process){
            // adapt video to hd
            case 0:
                command = Processor.resize_video_hd(me, meConfig, originPath, false, false);
                break;
            case 1:
                command = Processor.transcode_video_PALMPEG2(me.getName(), meConfig.getName().replace(".mp4", ".mpg"), originPath, false, false);
                break;
            case 2:
                command = Processor.transcode_audio_MPEG2(me.getName(), meConfig.getName().replace(".mp4", ".mp2"), originPath, false, false);
                break;
            case 3:
                command = Processor.cut_video_hd(3, 10, me, meConfig, originPath, false, false);
                break;
            case 4:
                command = Processor.create_video_from_image(originPath+"/stcav.png", 10, meConfig.getVideoElement(), meConfig.getName(), originPath, false, false);
                break;
            case 5:
                command = Processor.insert_silence_to_video(me, meConfig, originPath, false, false);
                break;
            case 6:
                command = Processor.create_image_from_video(me, 15, meConfig.getName().replace(".mp4", ".jpg"), originPath, false, false);
                break;
            case 7:
                fe = new FontElement("Hey!", "21", "black", System.getProperty("user.dir")+"/"+appConfig.getProperty("fonts_path")+"/"+appConfig.getProperty("font_default_name"), "10", "10");
                command = Processor.insert_text_to_video(me, meConfig, fe, originPath, false, false);
                break;
            case 8:
                fe = new FontElement("Hey!", "21", "black", System.getProperty("user.dir")+"/"+appConfig.getProperty("fonts_path")+"/"+appConfig.getProperty("font_default_name"), "10", "10");
                command = Processor.insert_text_to_video(me, meConfig, fe, 3, 5, originPath, false, false);
                break;
            case 9:
                fe = new FontElement(System.getProperty("user.dir")+"/"+appConfig.getProperty("fonts_path")+"/"+appConfig.getProperty("font_default_text_file"), "21", "black", System.getProperty("user.dir")+"/"+appConfig.getProperty("fonts_path")+"/"+appConfig.getProperty("font_default_name"), "10", "10");
                command = Processor.insert_text_from_file_to_video(me, meConfig, fe, originPath, false, false);
                break;
            case 10:
                fe = new FontElement(System.getProperty("user.dir")+"/"+appConfig.getProperty("fonts_path")+"/"+appConfig.getProperty("font_default_text_file"), "21", "black", System.getProperty("user.dir")+"/"+appConfig.getProperty("fonts_path")+"/"+appConfig.getProperty("font_default_name"), "10", "10");
                command = Processor.insert_text_from_file_to_video(me, meConfig, fe, 3, 6, originPath, false, false);
                break;
            case 11:
                command = Processor.do_fade_to_video(me, meConfig, 3, 3, originPath, false, false);
                break;
            case 12:
                List<MediaElement> mes = new ArrayList<MediaElement>();
                mes.add(me);
                mes.add(me);
                mes.add(me);
                command = Processor.do_merge_videos_x264_return( mes, originPath, meConfig.getName(), false, false);
                String tsPath = System.getProperty("user.dir")+"/"+appConfig.getProperty("output_path")+ "/";
                // add tsPath in order to set ts path in remote excute server
                command = command.replace(" ", "¬")+"*"+originPath+"*";
                break;
            default:
                break;
        }
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
        String mediaPath = originPath;
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
    
    public void join() throws InterruptedException{
        process.join();
    }
    
}
