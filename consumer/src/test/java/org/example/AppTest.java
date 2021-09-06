package org.example;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openjdk.jol.info.ClassLayout;

import java.io.File;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() throws InterruptedException {
        TT t= new TT();
        for(int i=0;i<10;i++){
            new Thread(t).start();
        }
        t.shutdown();
        Thread.sleep(10000000);
    }

//    public static volatile int race = 0;
    public synchronized static void increase(){
        race.incrementAndGet();
    }
    public static AtomicInteger race = new AtomicInteger();
    private static final int THREADS_COUNT = 20;

    @Test
    public void test1(){
        Thread[] t = new Thread[THREADS_COUNT];
        for(int i=0;i<THREADS_COUNT;i++){
            t[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int i=0;i<100000;i++){
                        increase();
                    }
                }
            });
            t[i].start();
        }
        while (Thread.activeCount()>1){
            Thread.yield();
        }
        System.out.println(race);
    }

    private static Vector<Integer> v = new Vector<>();

    @Test
    public void test02(){
        while(true){
            for(int i=0;i<10;i++){
                v.add(i);
            }
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int i =0;i<v.size();i++){
                        v.remove(i);
                    }
                }
            });

            Thread t2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int i=0;i<v.size();i++){
                        System.out.println(v.get(i));
                    }
                }
            });
            t1.start();
            t2.start();

            while(Thread.activeCount()>20);
        }
    }

    /**
     * 强引用
     * 强引用是最普遍的引用，如果一个对象具有强引用，垃圾回收器不会回收该对象，
     * 当内存空间不足时，JVM 宁愿抛出 OutOfMemoryError异常；只有当这个对象没有被引用时，才有可能会被回收。
     */
    @Test
    public void StrongReferenceTest (){
        List<Byte[]>  l = new ArrayList<>();
        while(true){
            Byte[]data = new Byte[3*1024*1024];
            l.add(data);
        }

    }

    @Test
    public void SoftReferenceTest() throws InterruptedException {
        Person p = new Person("张三");
        SoftReference<Person> softReference = new SoftReference<>(p);
        p = null;//去掉强引用，只有弱引用
//        System.gc();
        Person anotherPerson = new Person("李四");
        Thread.sleep(15000);
        System.err.println("软引用的对象 ------->" + softReference.get());
    }
    static class Person {

        private String name;
        private Byte[] bytes = new Byte[1024 * 1024];

        public Person(String name) {
            this.name = name;
        }
    }

    @Test
    public void weakReference() throws InterruptedException {
        Person person = new Person("李四");
        ReferenceQueue<Person> rf = new ReferenceQueue<>();
        WeakReference wr = new WeakReference(person,rf);
        person = null;
        System.gc();
        Thread.sleep(2000);
        System.err.println("弱引用的对象 ------->" + wr.get());

        Reference reference = rf.poll();
        if (reference != null) {
            System.err.println("WeakReference对象中保存的弱引用对象已经被GC，下一步需要清理该Reference对象");
            //清理softReference
        } else {
            System.err.println("WeakReference对象中保存的软引用对象还没有被GC，或者被GC了但是获得对列中的引用对象出现延迟");
        }


    }

    @Test
    public void PermGenOomMock1(){
        URL url = null;
        List<ClassLoader> loaders= new ArrayList<>();
        try {
            url =  new  File( "H:\\sources\\springboot-rabbitmq\\consumer\\target\\classes" ).toURI().toURL();
            URL[] urls = {url};
            while(true){
                ClassLoader cl = new URLClassLoader(urls);
                loaders.add(cl);
                cl.loadClass( "com.whb.ConsumerApplication" );
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    static Object generate() {
        Map<String, Object> map = new HashMap<>();
        map.put("a", new Integer(1));
        map.put("b", "b");
        map.put("c", new Date());

        for (int i = 0; i < 10; i++) {
            map.put("d"+i,new byte[6*1024*1024]);
            map.put(String.valueOf(i), String.valueOf(i));
        }

        return map;
    }
    @Test
    public void test05(){
        Object obj = generate();
        ClassLayout layout = ClassLayout.parseInstance(obj);
        System.out.println(layout.toPrintable());
    }

    @Test
    public void PermGenOomMock2(){
        String s = "string";
        List<String> list = new ArrayList<>();
        for(int i =0;i<Integer.MAX_VALUE;i++){
            list.add(s.intern());
        }
    }

    private static Object  o;
    @Test
    public void test06(){
        o = new Object();
        synchronized (o){
            System.out.println(ClassLayout.parseInstance(o).toPrintable(o));
        }
    }
}
