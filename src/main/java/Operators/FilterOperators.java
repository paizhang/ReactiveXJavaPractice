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
}
