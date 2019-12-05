package com.wangyi.xmlTool.dataObject;

/**
 * @author:wangyi
 * @Date:2019/12/2
 */
public class Transition {
    private String from;
    private String to;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "Transition{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                '}';
    }
}
