package com.grinner.tarkov.haha.chart;

import com.grinner.tarkov.haha.model.AbstractChartModel;
import com.grinner.tarkov.haha.printer.Printable;
import com.grinner.tarkov.haha.printer.RecursivelyPrintable;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MVC模式的C，输出V，调整M
 * 同时又是整个分类的门面，提供概念描述
 */
public class FlowChart extends AbstractChart {

//    private final String template = "";
    protected FlowChartModel chartModel;
    private Map<String, FlowChartNode> nodeMap;
    private Map<String, Map<String, FlowChartLine>> lineMap;
    private FlowChart() {
        this.chartModel = new FlowChartModel();
        this.nodeMap = new HashMap<>();
    }

    private FlowChart(Direction direction) {
        this();
        setDirection(direction);
    }


    /**
     * 产生实例的方法
     * @return
     */
    public static FlowChart newChart() {
        return newChart(Direction.TOP_TO_BOTTOM);
    }

    public static FlowChart newChart(Direction direction) {
        return new FlowChart(direction);
    }

    /**
     * 设置表头的方法
     * @param direction
     * @return
     */
    public FlowChart setDirection(Direction direction) {
        chartModel.getHeader().setDirection(direction);
        return this;
    }

    /**
     * 操作结点的方法
     * @return
     */

    /**
     * 添加单行结点
     * @return
     */
    public FlowChartNode useNode(String id) {
        FlowChartNode flowChartNode = nodeMap.get(id);
        if (flowChartNode == null) {
            flowChartNode = new FlowChartNode(id);
            nodeMap.put("id", flowChartNode);
        }
        return flowChartNode;
    }

    public FlowChartNode node(String id) {
        FlowChartNode flowChartNode = nodeMap.get(id);
        return flowChartNode;
    }

    public FlowChartLine useLine(String fromNodeId, LineStyle lineStyle, String toNodeId) {
        FlowChartNode fromNode = useNode(fromNodeId);
        FlowChartNode toNode = useNode(toNodeId);
        return useLine(fromNode, lineStyle, toNode);
    }
    public FlowChartLine useLine(String fromNodeId, LineStyle lineStyle, FlowChartNode toNode) {
        FlowChartNode fromNode = useNode(fromNodeId);
        return useLine(fromNode, lineStyle, toNode);
    }
    public FlowChartLine useLine(FlowChartNode fromNode, LineStyle lineStyle, String toNodeId) {
        FlowChartNode toNode = useNode(toNodeId);
        return useLine(fromNode, lineStyle, toNode);
    }
    public FlowChartLine useLine(FlowChartNode fromNode, LineStyle lineStyle, FlowChartNode toNode) {
        String fromNodeId = fromNode.getId();
        String toNodeId = toNode.getId();
        Map<String, FlowChartLine> toMap = lineMap.get(fromNodeId);
        if (toMap == null) {
            toMap = new HashMap<>();
            lineMap.put(fromNodeId, toMap);
        }

        FlowChartLine line = toMap.get(toNodeId);
        if (line == null) {
            line = new FlowChartLine();
            toMap.put(toNodeId, line);
        }
        line.setLineStyle(lineStyle);
        return line;
    }

    @Getter
    @Setter
    private class FlowChartHeader implements Printable {
        private Direction direction;

        @Override
        public String print() {
            StringBuffer stringBuffer = new StringBuffer();
            return stringBuffer.append("graph ").append(direction.content).append("\n").toString();
        }
    }


    @Getter
    @Setter
    private class FlowChartSubGraph  extends AbstractChartModel implements RecursivelyPrintable {
        private SubFlowChartHeader header;
        private SubChartFooter footer;

        @Override
        public List<Printable> getContentList() {
            contentList.add(0, header);
            contentList.add(footer);
            return contentList;
        }
    }


    @Getter
    @Setter
    private class SubFlowChartHeader implements Printable {
        private String id;
        private String content;

        @Override
        public String print() {
            StringBuffer stringBuffer = new StringBuffer();
            return stringBuffer.append("subgraph ").append(id).append("\n").toString();
        }
    }


    @Getter
    @Setter
    private class SubChartFooter implements Printable {
        private Direction direction;

        @Override
        public String print() {
            StringBuffer stringBuffer = new StringBuffer();
            return stringBuffer.append("end").append("\n").toString();
        }
    }

    @Getter
    @Setter
    private class FlowChartNode implements Printable {
        private String id;
        private String content;
        private NodeStyle style;

        FlowChartNode(String id) {
            this.id = id;
        }

        @Override
        public String print() {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(id);
            if (content != null && !"".equals(content)) {
                String replacedContent = style.template.replace("{content}", content);
                stringBuffer.append(replacedContent);
            }
            return stringBuffer.append("\n").toString();
        }

        public void remove() {
            nodeMap.remove(id);
        }

        public FlowChartNode nodeStyle(NodeStyle style) {
            this.style = style;
            return this;
        }

        public FlowChartNode content(String content) {
            this.content = content;
            return this;
        }
    }


    @Getter
    @Setter
    private class FlowChartLine implements Printable {
        private FlowChartNode from;
        private FlowChartNode to;
        private String content;
        private LineStyle lineStyle;

        @Override
        public String print() {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(from.print());
            if (content != null && !"".equals(content)) {
                String replacedContent = lineStyle.linkTemplate;
                stringBuffer.append(replacedContent);
            } else {
                String replacedContent = lineStyle.contentTemplate.replace("{content}", content);
                stringBuffer.append(replacedContent);
            }
            return stringBuffer.append(to.print())
                    .append("\n").toString();
        }

        public void remove() {
            Map<String, FlowChartLine> toMap = lineMap.get(from.getId());
            if (toMap != null) {
                FlowChartLine flowChartLine = toMap.get(to.getId());
                if (flowChartLine != null) {
                    toMap.remove(to.getId());
                }
                if (toMap.isEmpty()) {
                    toMap.remove(to.getId());
                }
            }
            if (toMap.isEmpty()) {
                lineMap.remove(from.getId());
            }
        }

        public FlowChartLine nodeStyle(LineStyle lineStyle) {
            this.lineStyle = lineStyle;
            return this;
        }

        public FlowChartLine content(String content) {
            this.content = content;
            return this;
        }


        public FlowChartLine from(String fromNodeId) {
            FlowChartNode fromNode = useNode(fromNodeId);
            this.from = fromNode;
            return this;
        }

        public FlowChartLine from(FlowChartNode fromNode) {
            return from(fromNode.getId());
        }

        public FlowChartLine to(String toNodeId) {
            FlowChartNode toNode = useNode(toNodeId);
            this.to = toNode;
            return this;
        }

        public FlowChartLine to(FlowChartNode toNode) {
            return to(toNode.getId());
        }

    }

    /**
     * MVC模式的Model, 仅用于存储数据
     */
    @Getter
    @Setter
    private class FlowChartModel extends AbstractChartModel implements RecursivelyPrintable {

        private FlowChartHeader header;

        @Override
        public List<Printable> getContentList() {
            contentList.add(0, header);
            return contentList;
        }
    }

    /**
     * 门面下的概念分类之一
     */
    public enum Direction {
        TOP_TO_DOWN("TD"),
        TOP_TO_BOTTOM("TB"),
        BOTTOM_TO_TOP("BT"),
        LEFT_TO_RIGHT("LR"),
        RIGHT_TO_LEFT("RL")
        ;

        private String content;

        Direction(String content) {
            this.content = content;
        }
    }

    public enum NodeStyle {
        ROUND("({content})"),
//        TOP_TO_BOTTOM("TB"),
//        BOTTOM_TO_TOP("BT"),
//        LEFT_TO_RIGHT("LR"),
//        RIGHT_TO_LEFT("RL")
        ;

        private String template;

        NodeStyle(String template) {
            this.template = template;
        }
    }

    public enum LineStyle {
        LINK("-->", "-- {content} -->"),
        ;

        private String linkTemplate;
        private String contentTemplate;

        LineStyle(String linkTemplate, String contentTemplate) {
            this.linkTemplate = linkTemplate;
            this.contentTemplate = contentTemplate;
        }
    }
}
