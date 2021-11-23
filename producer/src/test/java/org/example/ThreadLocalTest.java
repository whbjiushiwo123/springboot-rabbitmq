package org.example;

public class ThreadLocalTest {
    static ThreadLocal<String> s = new ThreadLocal<>();
    public static void main(String[] args){
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                s.set("localVar1");
                s.set("localVar11");
//                print("thread1");
                System.out.println("1after remove : " + s.get());
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                s.set("localVar2");
                s.set("localVar22");
//                print("thread2");
                System.out.println("2after remove : " + s.get());
            }
        });

        t1.start();
        t2.start();


    }
    static void print(String str){
        System.out.println(str+":"+s.get());
        s.remove();

    }

}
