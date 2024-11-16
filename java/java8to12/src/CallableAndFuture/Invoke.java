package CallableAndFuture;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class Invoke {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        Callable<String> hello = () -> {
            Thread.sleep(2000L);
            return "Hello";
        };

        Callable<String> java = () -> {
            Thread.sleep(3000L);
            return "java";
        };

        Callable<String> gngsn = () -> {
            Thread.sleep(1000L);
            return "gngsn";
        };


//        List<Future<String>> futures = executorService.invokeAll(Arrays.asList(hello, java, gngsn));
//        for (Future<String> f : futures) {
//            System.out.println(f.get());
//        }
        String s = executorService.invokeAny(Arrays.asList(hello, java, gngsn));
        System.out.println(s);

        executorService.shutdown();
    }
}
