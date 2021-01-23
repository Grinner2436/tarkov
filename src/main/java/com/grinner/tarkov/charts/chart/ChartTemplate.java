package com.grinner.tarkov.charts.chart;

public enum  ChartTemplate {
    FLOW_CHART("<html lang='en'><head><meta charset='utf-8'></head><body><div class='mermaid'>\n",

            "\n</div><script src='https://cdn.jsdelivr.net/npm/mermaid/dist/mermaid.min.js'></script><script>mermaid.initialize({startOnLoad:true});</script></body></html>");

    private String head;
    private String foot;
    ChartTemplate(String head, String foot) {
        this.head = head;
        this.foot = foot;
    }

    public String getHead() {
        return head;
    }

    public String getFoot() {
        return foot;
    }
}
