package org.example;

import org.openjdk.jol.info.ClassLayout;

public class TT implements Runnable{
    @Override
    public void run() {
        
    }
    public static void main(String[] args) {
        Object o = new Object();
        System.out.println(ClassLayout.parseInstance(o).toPrintable());
    }
    public void shutdown() {
    }
}
