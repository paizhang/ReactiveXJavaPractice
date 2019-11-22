package Operators;

import io.reactivex.Observable;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class TransformingOperators {

    private void processBufferData(List<Integer> nums) {
        System.out.println("Start printing...");
        for (Integer i : nums) {
            System.out.println(i);
        }
    }

    public void testUsingBufferForCountWithoutSkip() {
        Observable.range(0, 10)
                .buffer(3)
                .subscribe(s -> processBufferData(s));
    }

    public void testUsingBufferForCountWithSkip() {
        Observable.range(0, 10)
                .buffer(3, 5)
                .subscribe(s -> processBufferData(s));
    }

    /*
        Map can be used for applying a function on the top of each item and transform it into a new item.
     */
    public void testUsingMap() {
        Observable.fromArray(new String[]{"First", "Second", "Third"})
                .map(s -> {return s.toUpperCase();})
                .subscribe(res -> System.out.println(res));
    }

    // This will a List of items for every interval amount of time
    public void testUsingBufferForInterval() throws InterruptedException {
        Observable.interval(1, TimeUnit.SECONDS)
                .buffer(3, TimeUnit.SECONDS)
                .subscribe(s -> System.out.println(s));
        Thread.sleep(100000);
    }
}
