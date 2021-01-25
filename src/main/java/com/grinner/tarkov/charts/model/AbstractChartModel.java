package com.grinner.tarkov.charts.model;

import com.grinner.tarkov.charts.printer.Printable;
import com.grinner.tarkov.charts.printer.RecursivelyPrintable;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class AbstractChartModel implements ChartModel, RecursivelyPrintable {
    protected List<Printable> contentList;
    @Setter
    protected int layerNum;
    public AbstractChartModel() {
        this.contentList = new ArrayList<>();
    }

    @Override
    public List<Printable> getResultContentList() {
        return contentList;
    }

    @Override
    public String getModelContent() {
        return print();
    }

    @Override
    public void setLayer(int layerNum) {
        this.layerNum = layerNum;
    }

    @Override
    public int getLayer() {
        return layerNum;
    }
}
