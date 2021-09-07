package com.whb.demo;


import org.openjdk.jol.info.ClassLayout;

/**
 * 休眠5秒，Mark Word不一样
 * Syn锁升级之后，jdk1.8版本的一个底层默认设置4s之后偏向锁开启。
 * 也就是说在4s内是没有开启偏向锁的，加了锁就直接升级为轻量级锁了。
 */
public class JOLDemo {
    String s;
    private static Object  o;
//    public static void main(String[] args) {
//        o = new Object();
//        synchronized (o){
//            System.out.println(ClassLayout.parseInstance(o).toPrintable());
//        }
//    }
    public static void main(String[] args) {
        try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }
        o = new Object();
        synchronized (o){
            System.out.println(ClassLayout.parseInstance(o).toPrintable());
        }
    }
}
