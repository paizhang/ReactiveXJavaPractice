package Scheduler;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class TestObserveOn {

    private static void testSubscribeOnAndObserveOn() throws InterruptedException {
        Observable.just("short", "longer", "longest")
                .doOnNext(c -> System.out.println("Processing in thread: " + Thread.currentThread().getName()))
                .subscribeOn(Schedulers.newThread())
                .map(String::length)
                // Operators below will be executed in a computation thread
                .observeOn(Schedulers.computation())
                .filter(t -> t >= 6)
                .subscribe(length -> System.out.println(String.valueOf(length) + "Processing in thread: " + Thread.currentThread().getName()));

        Thread.sleep(5000);
    }

    private static void testMultipleObserveOn() {
        Observable.just("short", "longer", "longest")
                .doOnNext(c -> System.out.println("Processing in thread: " + Thread.currentThread().getName()))
                .observeOn(Schedulers.newThread())
                .map(String::length)
                .observeOn(Schedulers.newThread())
                .filter(t -> t >= 6)
                .subscribe(length -> System.out.println(String.valueOf(length) + "Processing in thread: " + Thread.currentThread().getName()));
    }

    public static void main(String[] args) throws InterruptedException {
        // testSubscribeOnAndObserveOn();
        testMultipleObserveOn();
    }
}
