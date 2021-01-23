package com.grinner.tarkov.charts.chart;

import com.grinner.tarkov.charts.model.AbstractChartModel;
import com.grinner.tarkov.charts.printer.Printable;
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
    @Getter
    protected Map<String, FlowChartNode> nodeMap;
    @Getter
    protected Map<String, Map<String, FlowChartLine>> lineMap;
    private Map<String, FlowChartSubGraph> subGraphMap;
    private FlowChart() {
        this.chartModel = new FlowChartModel();
        this.nodeMap = new HashMap<>();
        this.lineMap = new HashMap<>();
        this.subGraphMap = new HashMap<>();
    }

    private FlowChart(Direction direction) {
        this();
        setDirection(direction);
    }

    @Override
    public FlowChartModel getChartModel() {
        return chartModel;
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
        this.getChartModel().getHeader().setDirection(direction);
        return this;
    }

    /**
     * 添加单行结点
     * @return
     */
    public FlowChartNode useNode(String id) {
        FlowChartNode flowChartNode = getNodeMap().get(id);
        if (flowChartNode == null) {
            flowChartNode = new FlowChartNode(id);
            Map<String, FlowChartNode> nodeMap = getNodeMap();
            FlowChartNode node = nodeMap.put(id, flowChartNode);
//            this.getChartModel().getContentList().add(flowChartNode);
        }
        return flowChartNode;
    }

    public FlowChartNode node(String id) {
        FlowChartNode flowChartNode = getNodeMap().get(id);
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
        Map<String, FlowChartLine> toMap = getLineMap().get(fromNodeId);
        if (toMap == null) {
            toMap = new HashMap<>();
            getLineMap().put(fromNodeId, toMap);
        }

        FlowChartLine line = toMap.get(toNodeId);
        if (line == null) {
            line = new FlowChartLine(fromNode,lineStyle, toNode);
            toMap.put(toNodeId, line);
            this.getChartModel().getContentList().add(line);
        }
        line.setLineStyle(lineStyle);
        return line;
    }

    public FlowChartSubGraph useSubGraph(String graphId) {
        FlowChartSubGraph subGraph = subGraphMap.get(graphId);
        if (subGraph == null) {
            subGraph = new FlowChartSubGraph(graphId);
            subGraphMap.put(graphId, subGraph);
            this.getChartModel().getContentList().add(subGraph);
        }
        return subGraph;
    }

    public FlowChart setLayer(int layer) {
        getChartModel().setLayer(layer);
        return this;
    }

    @Getter
    @Setter
    private class FlowChartHeader implements Printable {
        private Direction direction;

        FlowChartHeader() {
            this.direction = Direction.TOP_TO_BOTTOM;
        }

        @Override
        public String print() {
            StringBuffer stringBuffer = new StringBuffer();
            return stringBuffer.append("graph ").append(direction.content)
//                    .append("\n")
                    .toString();
        }
    }


    @Getter
    @Setter
    public class FlowChartSubGraph  extends FlowChart {

        private String id;
        private String content;
        private FlowChartSubGraphModel subGraphModel;
        FlowChartSubGraph(String id) {
            this.id = id;
            this.subGraphModel = new FlowChartSubGraphModel(id);
        }

        @Override
        public FlowChartSubGraph useSubGraph(String graphId) {
            return this;
        }

        public void remove() {
            subGraphMap.remove(id);
        }

        public FlowChartSubGraph content(String content) {
            this.content = content;
            return this;
        }

        @Override
        public FlowChartModel getChartModel() {
            return subGraphModel;
        }

        @Getter
        @Setter
        private class FlowChartSubGraphModel extends FlowChartModel {
            private SubFlowChartHeader subHeader;
            private SubChartFooter subFooter;

            FlowChartSubGraphModel(String id) {
                super();
                this.subHeader = new SubFlowChartHeader(id);
                this.subFooter = new SubChartFooter();
            }

            @Override
            public List<Printable> getResultContentList() {
                this.contentList.add(0, subHeader);
                this.contentList.add(subFooter);
                return this.contentList;
            }

        }
    }


    @Getter
    @Setter
    private class SubFlowChartHeader implements Printable {
        private String id;
        private String content;

        public SubFlowChartHeader(String id) {
            this.id = id;
        }


        @Override
        public String print() {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("subgraph ").append(id);
            if (content != null && !content.equals("")) {
                stringBuffer.append(" [").append(content).append("] ");
            }
            return stringBuffer.toString();
        }
    }


    @Getter
    @Setter
    private class SubChartFooter implements Printable {
        private Direction direction;

        @Override
        public String print() {
            StringBuffer stringBuffer = new StringBuffer();
            return stringBuffer.append("end").toString();
        }
    }

    @Getter
    @Setter
    public class FlowChartNode implements Printable {
        private String id;
        private String content;
        private NodeStyle style;

        FlowChartNode(String id) {
            this.id = id;
            this.style = NodeStyle.ROUND;
        }

        @Override
        public String print() {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(id);
            if (content != null && !"".equals(content)) {
                String replacedContent = style.template.replace("{content}", content);
                stringBuffer.append(replacedContent);
            }
            return stringBuffer.toString();
        }

        public void remove() {
            Printable node = nodeMap.remove(id);
            chartModel.getContentList().remove(node);
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

        public FlowChartLine(FlowChartNode from, LineStyle lineStyle, FlowChartNode to) {
            this.from = from;
            this.to = to;
            this.lineStyle = lineStyle;
        }

        @Override
        public String print() {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(from.print());
            if (content != null && !"".equals(content)) {
                String replacedContent = lineStyle.contentTemplate.replace("{content}", content);
                stringBuffer.append(replacedContent);
            } else {
                String replacedContent = lineStyle.linkTemplate;
                stringBuffer.append(replacedContent);
            }
            return stringBuffer.append(to.print())
//                    .append("\n")
                    .toString();
        }

        public void remove() {
            Map<String, FlowChartLine> toMap = getLineMap().get(from.getId());
            if (toMap != null) {
                FlowChartLine flowChartLine = toMap.get(to.getId());
                if (flowChartLine != null) {
                    Printable line = toMap.remove(to.getId());
                    chartModel.getContentList().remove(line);
                }
                if (toMap.isEmpty()) {
                    lineMap.remove(from.getId());
                }
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
    private class FlowChartModel extends AbstractChartModel {

        private FlowChartHeader header;

        FlowChartModel() {
            super();
            this.header = new FlowChartHeader();
        }

        @Override
        public List<Printable> getResultContentList() {
            this.contentList.add(0, header);
            return this.contentList;
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
