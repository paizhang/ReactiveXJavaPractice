package Scheduler;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class TestSubscribeOn {

    /*
        This scheduler will spawn a new thread. This is an expensive operation because 1) spawning a new thread is expensive, 2)
        these threads are not reusable.
     */
    private static void testSubscribeOnNewThreadWithJust() {
        Observable.just("short", "longer", "longest")
                // Do on next will be called every time before the OnNext() is called
                .doOnNext(c -> System.out.println("Processing in thread: " + Thread.currentThread().getName()))
                .subscribeOn(Schedulers.newThread())
                .map(String::length)
                .subscribe(length -> System.out.println(length));
    }

    /*
        This is backed by bounded thread pool.
     */
    private static void testSubscribeOnComputationWithJust() throws InterruptedException {
        Observable.just("short", "longer", "longest")
                // Do on next will be called every time before the OnNext() is called
                .doOnNext(c -> System.out.println("Processing in thread: " + Thread.currentThread().getName()))
                .subscribeOn(Schedulers.computation())
                .map(String::length)
                .subscribe(length -> System.out.println(length));
        Thread.sleep(5000);
    }

    /*
        This is backed by unbounded thread pool.
     */
    private static void testSubscribeOnIOWithJust() throws InterruptedException {
        Observable.just("short", "longer", "longest")
                // Do on next will be called every time before the OnNext() is called
                .doOnNext(c -> System.out.println("Processing in thread: " + Thread.currentThread().getName()))
                .subscribeOn(Schedulers.io())
                .map(String::length)
                .subscribe(length -> System.out.println(length));
        // Thread.sleep(5000);
    }

    private static void testMultipleSubscribeOn() throws InterruptedException {
        Observable.just("short", "longer", "longest")
                .doOnNext(c -> System.out.println("Processing in thread: " + Thread.currentThread().getName()))
                .subscribeOn(Schedulers.computation())
                .map(String::length)
                .subscribeOn(Schedulers.computation())
                .subscribe(length -> System.out.println(length));
        Thread.sleep(5000);
    }

    public static void main(String[] args) throws InterruptedException {
        // testSubscribeOnNewThreadWithJust();
        // testSubscribeOnComputationWithJust();
        // testSubscribeOnIOWithJust();
        testMultipleSubscribeOn();
    }
}
