/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eva.tester;

import com.google.gson.Gson;
import eva.tester.config.AppConfig;
import eva.tester.model.Action;
import eva.tester.model.MediaProcess;
import eva.tester.model.Report;
import eva.tester.processor.FileProcessor;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import org.university.stcav.eva.model.AudioElement;
import org.university.stcav.eva.model.VideoElement;

/**
 *
 * @author johaned
 */
public class Analyzer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnknownHostException {
        AppConfig appConfig = AppConfig.getInstance();
        try (BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir")+"/"+appConfig.getProperty("output_path")+"/"+appConfig.getProperty("report_file_name")))) {

            String serializedReport;
            Report report;
            Gson gson = new Gson();
            List<MediaProcess> mes;
            MediaProcess me;
            List<Action> actions;
            Action action;
            Iterator meIter;
            Iterator actionIter;
            VideoElement ve;
            AudioElement ae;
            Long totalTimeProcesses = 0L;
            Long timeProcess;
            
            String performanceReportPath = System.getProperty("user.dir")+"/"+appConfig.getProperty("output_path")+"/"+appConfig.getProperty("performance_report_file_name");
            
            while ((serializedReport = br.readLine()) != null) {
                report = gson.fromJson(serializedReport, Report.class);
                FileProcessor.do_file_write(performanceReportPath, "=============================================================================================");
                FileProcessor.do_file_write(performanceReportPath, "Date: "+report.getDate());
                mes = report.getMediaProcesses();
                FileProcessor.do_file_write(performanceReportPath, "Concurrent processes: "+mes.size());
                FileProcessor.do_file_write(performanceReportPath, "-------------------------------------------------------------------------------------");
                meIter = mes.iterator();
                while(meIter.hasNext()){
                    me = (MediaProcess) meIter.next();
                    FileProcessor.do_file_write(performanceReportPath, "- Media Process ID: " + me.getId());
                    FileProcessor.do_file_write(performanceReportPath, "- Media Element: ");
                    FileProcessor.do_file_write(performanceReportPath, "    Name: " + me.getMe().getName());
                    FileProcessor.do_file_write(performanceReportPath, "    Duration: "+me.getMe().getDuration());
                    ve = me.getMe().getVideoElement();
                    ae = me.getMe().getAudioElement();
                    FileProcessor.do_file_write(performanceReportPath, "    Video Element: codec ->" + ve.getCodec()+" | resolution ->" + ve.getResolution());
                    FileProcessor.do_file_write(performanceReportPath, "    Audio Element: codec -> " + ae.getCodec());
                    actions = me.getActions();
                    if(!actions.isEmpty()){
                        timeProcess = actions.get(actions.size()-1).getEndTime() - actions.get(0).getStartTime();
                        FileProcessor.do_file_write(performanceReportPath, "    Process Time (seg): "+ timeProcess/1000.000);
                        totalTimeProcesses += timeProcess;
                    }
                    me.getActions().get(0);
                    actionIter = actions.iterator();
                    while(actionIter.hasNext()){
                        action = (Action) actionIter.next();
                        FileProcessor.do_file_write(performanceReportPath, "  - Action: " + Analyzer.actionName(action.getId()));
                        FileProcessor.do_file_write(performanceReportPath, "      is Succes? -> "+action.getSuccess());
                        FileProcessor.do_file_write(performanceReportPath, "      Spent Time (seg): "+(action.getEndTime()-action.getStartTime())/1000.000);       
                    }
                    FileProcessor.do_file_write(performanceReportPath, "*****************************************************************");
                }            
                FileProcessor.do_file_write(performanceReportPath, "-------------------------------------------------------------------------------------");
                FileProcessor.do_file_write(performanceReportPath, "Average concurrent media process: (seg) "+(totalTimeProcesses/mes.size())/1000.000);
                FileProcessor.do_file_write(performanceReportPath, "=============================================================================================");
                FileProcessor.do_file_write(performanceReportPath, " ");
                FileProcessor.do_file_write(performanceReportPath, " ");
                FileProcessor.do_file_write(performanceReportPath, " ");
                totalTimeProcesses = 0L;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static String actionName(int actionId){
        String[] names = {"RESIZE_VIDEO_HD", "TRANSCODE_VIDEO_PALMPEG2", "TRANSCODE_AUDIO_PALMPEG2", "CUT_VIDEO_HD", "CREATE_VIDEO_FROM_IMAGE", "INSERT_SILENCE_TO_VIDEO", "CREATE_IMAGE_FROM_VIDEO", "INSERT_TEXT_TO_VIDEO ", "INSERT_TEXT_TO_VIDEO_IN_TIME", "INSERT_TEXT_FROM_FILE_TO_VIDEO", "INSERT_TEXT_FROM_FILE_TO_VIDEO_IN_TIME", "DO_FADE_TO_VIDEO", "DO_MERGE_VIDEOS_X264_RETURN"};
        return names[actionId];
    }
}
