package com.wangyi.xmlTool;

import com.wangyi.xmlTool.dataObject.Activity;
import com.wangyi.xmlTool.dataObject.Transition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author:wangyi
 * @Date:2019/12/5
 */
public class Order {
    private List<Transition> transitions;
    private List<Activity> activities;
    private List<LinkedList<String>> result;
    private Map<String, List<Transition>> relations;
    private Map<String, Activity> activityMap;
    private Set<String> passed = new HashSet<String>();
    private Activity current;
    private Activity start;
    private Activity end;

    public void setTransitions(List<Transition> transitions) {
        this.transitions = transitions;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    /**
     * 初始化方法,准备后续可能用到的数据
     */
    public void init() {
        getEdge();
        getRelations();
    }

    private void getEdge() {
        for (Activity activity : activities) {
            if ("start".equals(activity.getActivityType())) {
                this.start = activity;
            } else if ("finish".equals(activity.getActivityType())) {
                this.end = activity;
            }
        }
    }

    private void getRelations() {
        if (relations == null) {
            relations = new HashMap<String, List<Transition>>();
        }
        for (Transition transition : transitions) {
            String key = transition.getFrom();
            List<Transition> relation = relations.get(key);
            if (relation == null) {
                relation = new ArrayList<Transition>();
            }
            relation.add(transition);
        }
        if (activityMap == null) {
            activityMap = new HashMap<String, Activity>();
        }
        for (Activity activity : activities) {
            activityMap.put(activity.getActivityId(), activity);
        }
    }

    public void getPossibleWays() {
        init();

    }

    private void getNextLink() {
        if (current == null) {
            current = start;
            passed.add(current.getActivityId());
        } else {
            List<Transition> relations = this.relations.get(current.getActivityId());
            for (Transition relation : relations) {
                //已经被使用过了,不重复使用
                if(passed.contains(relation.getTo())){
                    continue;
                }
            }
        }
    }

    private void getNextLink(Activity activity){
        if(activity == null){
            return;
        }
    }

    public static void main(String[] args) {
        Order order = new Order();
        String file = Parser.class.getClassLoader().getResource("test.xml").getFile();
        Parser parser = new Parser(file);
        parser.parse();
        order.setActivities(parser.getActivities());
        order.setTransitions(parser.getTransitions());
        order.getEdge();

    }
}
