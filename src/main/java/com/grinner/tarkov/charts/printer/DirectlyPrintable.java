package com.grinner.tarkov.charts.printer;

public interface DirectlyPrintable extends Printable{
    String getContent();

    @Override
    default String print() {
        return getContent();
    }
}
