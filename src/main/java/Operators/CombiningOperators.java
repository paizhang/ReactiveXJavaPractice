package Operators;

import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class CombiningOperators {
    /*
        This operator combines the latest items emitted by multiple operators in a customized way.  
     */
    public void testUsingCombineLatest() throws InterruptedException {
        Observable<Long> worker1 = Observable.interval(500, TimeUnit.MILLISECONDS);
        Observable<Long> worker2 = Observable.interval(1000, TimeUnit.MILLISECONDS);
        Observable<Long> worker3 = Observable.interval(1500, TimeUnit.MILLISECONDS);

        Observable.combineLatest(worker1, worker2, worker3, (worker1Score, worker2Score, worker3Score) -> "worker1: " + worker1Score + " worker2: " +
                worker2Score + " worker3: " + worker3Score)
                .subscribe(s -> System.out.println(s));

        Thread.sleep(10000);
    }
}
