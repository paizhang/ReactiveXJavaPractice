package Operators;

import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class FilterOperators {

    /*
        Filter operator take a timespan as a input. It starts a timer after a observable is emitted. If another new observable is emitted before the
        end of a previous timespan, then a new timer will start and the previous observable will be ignored.
     */
    public void testUsingThrottleWithTimeout() throws InterruptedException {
        Observable.interval(1, TimeUnit.SECONDS)
                .throttleWithTimeout(1500, TimeUnit.MILLISECONDS)
                .subscribe(num -> System.out.println(num));

        Thread.sleep(10000);
    }

    /*
        The only difference between throttleWithTimeout and debounce is that debounce can take a function as an input instead of a timespan.
        If a new observable is emitted before the end of the execution for current observable, current execution will be terminated and ignore.
        Then it will start the execute the function for the new observable.
     */
    public void testUsingDebounce() throws InterruptedException {
        Observable.interval(1, TimeUnit.SECONDS)
                .debounce(1500, TimeUnit.MILLISECONDS)
                .subscribe(num -> System.out.println(num));

        Thread.sleep(10000);
    }
}
