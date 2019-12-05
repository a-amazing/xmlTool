package com.wangyi.xmlTool;

import com.wangyi.xmlTool.dataObject.Activity;
import com.wangyi.xmlTool.dataObject.Transition;
import com.wangyi.xmlTool.util.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:wangyi
 * @Date:2019/12/2
 */
public class Parser {
    private Document document;
    private Element root;
    private List<Activity> activities;
    private List<Transition> transitions;

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    public void setTransitions(List<Transition> transitions) {
        this.transitions = transitions;
    }

    public Parser(String path) {
        try {
            document = XmlUtil.getDocument(path);
            root = document.getDocumentElement();
        } catch (Exception e) {
            System.out.println("parser对象创建失败!");
        }
    }

    public void parse(){
        domRootRead(root);
    }

    public void domRootRead(Element currentNode) {
        if (!("workflowProcess".equals(currentNode.getNodeName()))) {
            throw new RuntimeException("该文件不是工作流文件!");
        }
        NodeList nodes = currentNode.getChildNodes();
        Node temp;
        Element element;
        for (int i = 0; i < nodes.getLength(); i++) {
            temp = nodes.item(i);
            if (Node.ELEMENT_NODE == temp.getNodeType()) {
                element = (Element) temp;
                domRead(element);
            }
        }
    }

    public void domRead(Element currentNode) {
        NodeList nodes;
        Node node;
        Element element;
        if ("transitions".equals(currentNode.getNodeName())) {
            nodes = currentNode.getElementsByTagName("transition");
            for (int i = 0; i < nodes.getLength(); i++) {
                node = nodes.item(i);
                if (Node.ELEMENT_NODE == node.getNodeType()) {
                    element = (Element) node;
                    NodeList from = element.getElementsByTagName("from");
                    Element fromE = (Element) from.item(0);
                    NodeList to = element.getElementsByTagName("to");
                    Element toE = (Element) from.item(0);
                    if (transitions == null) {
                        transitions = new ArrayList<Transition>();
                    }
                    Transition transition = new Transition();
                    transition.setFrom(fromE.getTextContent());
                    transition.setTo(toE.getTextContent());
                    transitions.add(transition);
                }
            }
        } else if ("activities".equals(currentNode.getNodeName())) {
            nodes = currentNode.getElementsByTagName("activity");
            for (int i = 0; i < nodes.getLength(); i++) {
                node = nodes.item(i);
                if (Node.ELEMENT_NODE == node.getNodeType()) {
                    element = (Element) node;
                    NodeList activityId = element.getElementsByTagName("activityId");
                    Element activityIdE = (Element) activityId.item(0);
                    NodeList activityName = element.getElementsByTagName("activityName");
                    Element activityNameE = (Element) activityName.item(0);
                    NodeList activityType = element.getElementsByTagName("activityType");
                    Element activityTypeE = (Element) activityType.item(0);
                    if (activities == null) {
                        activities = new ArrayList<Activity>();
                    }
                    Activity activity = new Activity();
                    activity.setActivityName(activityNameE.getTextContent());
                    activity.setActivityType(activityTypeE.getTextContent());
                    activity.setActivityId(activityIdE.getTextContent());
                    activities.add(activity);
                }
            }
        }
    }

    public static void main(String[] args) {
        String file = Parser.class.getClassLoader().getResource("test.xml").getFile();
        Parser parser = new Parser(file);
        System.out.println(parser.activities);
        System.out.println(parser.transitions);
    }

}
