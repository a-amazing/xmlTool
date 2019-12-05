package com.wangyi.xmlTool.dataObject;

/**
 * @author:wangyi
 * @Date:2019/12/2
 */
public class Activity {
    private String activityId;
    private String activityName;
    private String activityType;

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "activityId='" + activityId + '\'' +
                ", activityName='" + activityName + '\'' +
                ", activityType='" + activityType + '\'' +
                '}';
    }
}
