package CallableAndFuture;

import java.util.concurrent.*;

public class App {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Callable<String> hello = new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(2000L);
                return "Hello";
            }
        };

        Future<String> helloFuture = executorService.submit(hello);
        System.out.println(helloFuture.isDone());
        System.out.println("Started!");

        helloFuture.cancel(true);
//        helloFuture.get(); // 여기서 결과값을 가지고 올 때까지 기다리고 있음 - blocking call

        System.out.println(helloFuture.isDone());
        System.out.println("End!");
        executorService.shutdown();
    }
}
