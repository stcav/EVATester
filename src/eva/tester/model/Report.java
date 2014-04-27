/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eva.tester.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author johaned
 */
public class Report {
    private Date date;
    private List<MediaProcess> mediaProcesses;

    public Report() {
        mediaProcesses = new ArrayList<MediaProcess>();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<MediaProcess> getMediaProcesses() {
        return mediaProcesses;
    }

    public void setMediaProcesses(List<MediaProcess> mediaProcesses) {
        this.mediaProcesses = mediaProcesses;
    }
    
    public void addMediaProcess(MediaProcess mp){
        mediaProcesses.add(mp);
    }

    
}
