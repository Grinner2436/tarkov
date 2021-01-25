package com.grinner.tarkov.charts.chart;

import com.grinner.tarkov.charts.model.ChartModel;

public abstract class AbstractChart implements Chart {

    protected ChartModel chartModel;

    @Override
    public String print() {
        return getChartModel().getModelContent();
    }

    public ChartModel getChartModel() {
        return chartModel;
    }

}
