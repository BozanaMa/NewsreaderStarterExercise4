package at.ac.fhcampuswien.newsanalyzer.downloader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ParallelDownloader extends Downloader{

    @Override
    public int process(List<String> urls) {
        int count = 0;

        int number = Runtime.getRuntime().availableProcessors();
        ExecutorService pool = Executors.newFixedThreadPool(number);
        List<Future<String>> futures = new ArrayList<>();

        for (String url : urls){
            futures.add(pool.submit(() -> saveUrl2File(url)));
        }
        for (Future<String> future : futures){
            while (!future.isDone()){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                if (future.get() != null){
                    count++;
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            } finally {
                pool.shutdown();
            }
        }
        return count;
    }
}
