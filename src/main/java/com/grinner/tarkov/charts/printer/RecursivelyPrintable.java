package com.grinner.tarkov.charts.printer;

import java.util.List;

public interface RecursivelyPrintable extends Printable {
    List<Printable> getResultContentList();

    @Override
    default String print() {
        StringBuffer stringBuffer = new StringBuffer();
        List<Printable> contentList = getResultContentList();
        contentList.forEach(printable -> {
//            for (int i = 0; i < getLayer(); i++) {
//                stringBuffer.append("\t");
//            }
            stringBuffer.append(printable.print()).append("\n");
        });
        return stringBuffer.toString();
    }

    int getLayer();
    void setLayer(int layer);
}
