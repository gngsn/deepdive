package Concurrent;

public class App {
    public static void main(String[] args) throws InterruptedException {
//        System.out.println(Thread.currentThread().getName());

        // Thread 만들기 2 가지 방법 (1)
//        MyThread myThread = new MyThread();
//        myThread.start();
//

        // Thread 만들기 2 가지 방법 (2)
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("Thread: " + Thread.currentThread().getName());
//            }
//        });

        Thread thread = new Thread(() -> {
            System.out.println("Thread: " + Thread.currentThread().getName());
            try {
                Thread.sleep(1000L); // 다른 스레드를 먼저 처리함. (여기서는 main thread)
            } catch (InterruptedException e) {   // 자는 동안에 누군가가 스레드를 깨우면 catch에 걸림 (깨우는 방법이 있구나 ~)
                throw new IllegalStateException(e);
            }
        });

        thread.start();

        System.out.println("Hello: " + Thread.currentThread().getName());
//        Thread.sleep(3000L);
//        thread.interrupt();

        thread.join();                    // thread가 끝날 때까지 기다린 후 아래 실행
        System.out.println(thread + " is finished");

    }


    static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println("Thread : " + Thread.currentThread().getName());
            super.run();
        }
    }
}
