package com.dimai.project.demo.thread_sysn;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by pijiang on 2019/9/12.
 */
class Test extends Thread {
    private int count;

    void setCount(int count) {
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


class Demo1 {
    final Lock lock = new ReentrantLock();

    //Lock配合Condition使用
    void conditionDemo() throws InterruptedException {
        final Condition notFull = lock.newCondition();
        final Condition notEmpty = lock.newCondition();

        lock.lock();
        try {
            notFull.await();
        } finally {
            lock.unlock();
        }
        notEmpty.signal();
    }

    public static void main(String[] args) throws Exception {
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


class Demo2 {

    //利用计数器处理批量任务后再执行后续操作
    public static void main(String[] args) throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(2);

        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "执行了");
            countDownLatch.countDown();
        }).start();

        new Thread(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "执行了");
            countDownLatch.countDown();
        }).start();

        countDownLatch.await();
        System.out.println("子线程全部执行了，开始执行主线程。。。");
    }
}

class Demo3{
    //原子自增
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger();
        System.out.println(atomicInteger.incrementAndGet());
        System.out.println(atomicInteger.incrementAndGet());
    }
}

class Demo4{
    //线程池
    public static void main(String[] args) {
        ExecutorService scheduledExecutor = Executors.newScheduledThreadPool(3);
        for (int i = 0; i < 3; i++)
        scheduledExecutor.execute(new Thread(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "执行完毕");
        }));
        scheduledExecutor.shutdown();
    }
}