package com.dimai.project.demo.thread_sysn;

/**
 * Created by pijiang on 2019/9/12.
 */
class Test extends Thread {
    private int count;

    void setCount(int count){
        this.count = count;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (this) {
            for (int i = 0; i < 5; i++) {
                --count;
                System.out.println(Thread.currentThread().getName() + ": " + count);
            }
        }
        System.out.println(Thread.currentThread().getName() + "结束");
    }
}


public class Demo1 {
    public static void main(String[] args) throws Exception{
        int num = 10;
        Test test = new Test();
        test.setCount(num);
        test.setName("子线程");
//        test.setDaemon(true);   //守护线程
        test.start();
//        test.join();        //其他线程阻塞


        for (int i = 0; i < 5; i++) {
            --num;
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + ": " + num);
        }
        System.out.println("main结束");
    }
}