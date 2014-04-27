/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eva.tester.model;

/**
 *
 * @author johaned
 */
public class Action {
    private int id;
    private Long startTime;
    private Long endTime;
    private Boolean success;

    public Action() {
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
    
    
}
