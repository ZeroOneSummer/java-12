package com.dimai.project.demo.thread_sysn;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pijiang on 2019/9/12.
 */
public class Notify {

    private static volatile List list = new ArrayList();
    private static volatile boolean flag = false;

    void addList(int num){
        list.add("工人"+num);
    }

    public static void main(String[] args){
        Notify notify = new Notify();
        Object obj = new Object();

        Thread t1 = new Thread(() -> {
            System.out.println("HR上班");
            while(true) {
                if (flag) {
                    synchronized (obj) {
                        for (int i = 0; i < 10; i++) {
                            try {
                                Thread.sleep(1000);
                                notify.addList(i);
                                System.out.println("HR招到" + (i + 1) + "人");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        obj.notify();
                        System.out.println("HR招人完成，共计" + list.size() + ", 通知包工头");
                        break;
                    }
                } else {
                    System.out.println("包工头还没准备好");
                }
            }
        });

        Thread t2 = new Thread(() -> {
            System.out.println("包工头上班");
            flag = true;
            System.out.println("包工头准备好了");
            synchronized (obj){
                try {
                    obj.wait();
                    System.out.println("包工头收到通知，开始安排工人干活，共计"+list.size()+"人");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        t1.start();
        t2.start();
    }

}