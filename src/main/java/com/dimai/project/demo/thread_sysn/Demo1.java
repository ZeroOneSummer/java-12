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

class IntLock implements Runnable{
    public static ReentrantLock lock1 = new ReentrantLock();
    public static ReentrantLock lock2 = new ReentrantLock();
    int lock;
    /**
     * 控制加锁顺序，产生死锁
     */
    public IntLock(int lock) {
        this.lock = lock;
    }
    public void run() {
        try {
            if (lock == 1) {
                lock1.lockInterruptibly(); // 如果当前线程未被 中断，则获取锁。
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock2.lockInterruptibly();
                System.out.println(Thread.currentThread().getName()+"，执行完毕！");
            } else {
                lock2.lockInterruptibly();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock1.lockInterruptibly();
                System.out.println(Thread.currentThread().getName()+"，执行完毕！");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 查询当前线程是否保持此锁。
            if (lock1.isHeldByCurrentThread()) {
                lock1.unlock();
            }
            if (lock2.isHeldByCurrentThread()) {
                lock2.unlock();
            }
            System.out.println(Thread.currentThread().getName() + "，退出。");
        }
    }
    public static void main(String[] args) throws InterruptedException {
        IntLock intLock1 = new IntLock(1);
        IntLock intLock2 = new IntLock(2);
        Thread thread1 = new Thread(intLock1, "线程1");
        Thread thread2 = new Thread(intLock2, "线程2");
        thread1.start();
        thread2.start();
        Thread.sleep(1000);
        thread2.interrupt(); // 中断线程2，放弃lock1获取，释放lock2
    }
}