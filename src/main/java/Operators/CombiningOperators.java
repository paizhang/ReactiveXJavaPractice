package Operators;

import io.reactivex.Observable;

import java.sql.Timestamp;
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

    /*
        The intent of this operator is to join the items emitted by two observables.
        We have a source observable that emits a sequence of items. Then this observable call join() to join another observable. There are four
        parameters in the join function.
        First one: the second observable
        Second one: The duration time of lifespan of each item emitted by the first observable. (For example, item 1 is emitted by first observable at 1st second.
        If the duration is 2 seconds, then item 1 will be active in the following 2nd second and third second. )
        Third one: The duration time of window for each item emitted by the second observable. When the window is active, the current item that emitted
        by the second observable will join with all active items that emitted by the first observable. (For example, item A is emitted by second observable
        at 2nd second. If the duration of the window is 3 seconds, then item A will join with all active items emitted by first observable from the
        2nd second to 5th second.)
        Forth one: The resultSelector function that specify the way for joining two items.
    */
    public void testUsingJoin() throws InterruptedException {
        /*
        Observable<Integer> ob =Observable.create(subscriber -> {
            for (int i = 0; i < 4; i++) {
                subscriber.onNext(i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Observable.just("hello")
                .join(ob, s -> {
                    System.out.println(new Timestamp(System.currentTimeMillis()));
                    System.out.println("This is from Func1: " + s);
                    //使Observable延迟3000毫秒执行
                    return Observable.timer(3000, TimeUnit.MILLISECONDS);
                }, integer -> {
                            System.out.println(new Timestamp(System.currentTimeMillis()));
                            System.out.println("This is from Func2: " + String.valueOf(integer));
                            //使Observable延迟2000毫秒执行
                            return Observable.timer(2000, TimeUnit.MILLISECONDS);
                        },
                    //结合上面发射的数据
                    (s, integer) -> s+integer)
        .subscribe(o -> {System.out.println(new Timestamp(System.currentTimeMillis())); System.out.println("This is from subscriber: " + o);});
        */

        Observable<Long> obser1 = Observable.interval(1, TimeUnit.SECONDS);
        Observable<Long> obser2 = Observable.interval(10, TimeUnit.SECONDS);

        obser1.join(obser2,
                num1 -> {System.out.println(new Timestamp(System.currentTimeMillis()));System.out.println("This is from the func1: " + num1);return Observable.timer(2, TimeUnit.SECONDS);},
                num2 -> {System.out.println(new Timestamp(System.currentTimeMillis()));System.out.println("This is from the func2: " + num2);return Observable.timer(4, TimeUnit.SECONDS);},
                (num1, num2) -> String.valueOf(num1) + ":" + String.valueOf(num2))
                .subscribe(o -> {System.out.println(new Timestamp(System.currentTimeMillis()));System.out.println("This is from subscriber: " + o);});

        Thread.sleep(20000);
    }
}
