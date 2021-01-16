package com.grinner.tarkov.haha.printer;

public interface DirectlyPrintable extends Printable{
    String getContent();

    @Override
    default String print() {
        return getContent();
    }
}
