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

    public void testUsingDistinct() {
        Observable.just(0, 1, 2, 2, 3, 4, 3, 7)
                .distinct()
                .subscribe(num -> System.out.println(num));
    }

    /*
        distinct(Func) operator take a function as a input. This function will generate a key from the source observable. And it is this key which
        will be used to distinct those upstream observables.
     */
    public void testUsingDistinctWithFunc() {
        Observable.just(1, 0.1, true, "Yes", 2, "No", 5)
                .distinct(item -> {
                    if (item instanceof Integer) {
                        return "Int";
                    } else if (item instanceof String) {
                        return "String";
                    } else if (item instanceof Boolean) {
                        return "Boolean";
                    } else {
                        return "Others";
                    }
                })
                .subscribe(item -> System.out.println(item));
    }

    /*
        The elementAt operator will emit a solitary item with specific index.
        If the item cannot be found, it can throw an exception or return a default item.  
     */
    public void testUsingElementAt() {
        Observable.just(1, 2, 3, 4, 5)
                .elementAt(7, 0)
                .subscribe(num -> System.out.println(num));
    }
}