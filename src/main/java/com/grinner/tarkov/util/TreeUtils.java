package com.grinner.tarkov.util;

import com.grinner.tarkov.charts.chart.ChartTemplate;
import com.grinner.tarkov.charts.chart.FlowChart;
import com.grinner.tarkov.ntree.Node;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.grinner.tarkov.util.LocaleUtil.getName;

//计算任务树
public class TreeUtils {

    public static <T> void treeToGraph(Node<T>  root, String path) {
        List<Node<T> > firstLevelNodes = root.getChildren();
        for (Node<T>  firstLevelNode :firstLevelNodes) {
            FlowChart flowChart = FlowChart.newChart(FlowChart.Direction.RIGHT_TO_LEFT).setLayer(0);
            if (firstLevelNode.getChildren().isEmpty()) {
                System.out.println("没有一级子节点");
                continue;
            }
            //创建一级子节点对应的图
            String firstLevelNodeName = getName(firstLevelNode.getId());
            FlowChart.FlowChartSubGraph subGraph = flowChart.useSubGraph(firstLevelNodeName).content(firstLevelNodeName);
            subGraph.setLayer(1);
            //树解析成图
            parse(firstLevelNode, subGraph);
            //打印图
            String filePath = path + "/" + firstLevelNodeName + ".html";
            write(filePath, flowChart.print());
        }
    }

    private static void write(String filePath, String print) {
        StringBuffer stringBuffer = new StringBuffer()
                .append(ChartTemplate.FLOW_CHART.getHead())
                .append(print)
                .append(ChartTemplate.FLOW_CHART.getFoot());
        print = stringBuffer.toString();
        try {
            Path file = Paths.get(filePath);
            Files.write(file, print.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static <T> void parse(Node<T>  node, FlowChart flowChart) {
        List<Node<T> > children = node.getChildren();
        for (int index = 0; index < children.size(); index++) {
            Node<T>  child = children.get(index);
            flowChart.useLine(child.getId(), FlowChart.LineStyle.LINK, node.getId());
            FlowChart.FlowChartNode flowChartNode = flowChart.useNode(child.getId());
            String nodeName = getName(child.getId());
            flowChartNode.content(nodeName);
            parse(child, flowChart);
        }
        flowChart.useNode(node.getId()).content(getName(node.getId()));
    }

}
