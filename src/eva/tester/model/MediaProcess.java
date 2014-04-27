/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eva.tester.model;

import java.util.List;
import org.university.stcav.eva.model.MediaElement;

/**
 *
 * @author johaned
 */
public class MediaProcess {
    private String id;
    private MediaElement me;
    private MediaElement meConfig;
    private List<Action> actions;

    public MediaProcess() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public MediaElement getMe() {
        return me;
    }

    public void setMe(MediaElement me) {
        this.me = me;
    }

    public MediaElement getMeConfig() {
        return meConfig;
    }

    public void setMeConfig(MediaElement meConfig) {
        this.meConfig = meConfig;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }
    
    
}
