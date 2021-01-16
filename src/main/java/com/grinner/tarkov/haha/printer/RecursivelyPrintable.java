package com.grinner.tarkov.haha.printer;

import java.util.List;

public interface RecursivelyPrintable extends Printable{
    List<Printable> getContentList();

    @Override
    default String print() {
        StringBuffer stringBuffer = new StringBuffer();
        List<Printable> contentList = getContentList();
        contentList.forEach(printable -> {
            stringBuffer.append(printable.print());
        });
        return stringBuffer.toString();
    }
}
