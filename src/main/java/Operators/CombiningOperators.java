package Operators;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    /*
        This operator will merge multiple observables into one observable.
     */
    public void testUsingMerge() throws InterruptedException {
        System.out.println("Example #1");
        Observable<Integer> obs1 = Observable.range(0, 5);
        Observable<Integer> obs2 = Observable.range(7, 5);
        Observable.merge(obs1, obs2)
                .subscribe(num -> System.out.println(num));

        System.out.println("Example #2");
        Observable<Long> obs3 = Observable.interval(1, TimeUnit.SECONDS);
        Observable<Long> obs4 = Observable.interval(2, TimeUnit.SECONDS);
        Observable.merge(obs3, obs4)
            .subscribe(num -> System.out.println(num));
        Thread.sleep(10000);

        System.out.println("Example #3");
        Observable<Integer> obs5 = Observable.range(15, 5);
        List<Observable<Integer>> list = new ArrayList<>();
        list.add(obs1);
        list.add(obs2);
        list.add(obs5);
        Observable.merge(list)
                .subscribe(num -> System.out.println(num));
    }

    /*
        This operator will merge the source observable with another observable.
     */
    public void testUsingMergeWith() {
        Observable<Integer> obs = Observable.range(0, 5);
        Observable.range(6, 5)
                .mergeWith(obs)
                .subscribe(num -> System.out.println(num));
    }

    /*
        This operator will emit a specific sequence of items before emitting the items from the source observable.
     */
    public void testUsingStartWith() {
        System.out.println("Example #1");
        Observable.range(0, 5)
                .startWith(100)
                .subscribe(num -> System.out.println(num));

        System.out.println("Example #2");
        Observable.just(100)
                .startWith(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5)))
                .subscribe(num -> System.out.println(num));
    }

    /*
        Let's say we have several observables started to emit items at different points of time.
        This operator will output one observable which will always emit items that are emitted by the latest started source observable.
     */
    public void testUsingSwitchOnNext() {
        Observable.switchOnNext(Observable.create(
                new ObservableOnSubscribe<ObservableSource<Long>>() {
                    @Override
                    public void subscribe(ObservableEmitter<ObservableSource<Long>> observableEmitter) throws Exception {
                        for (int i = 0; i < 5; i++) {
                            observableEmitter.onNext(Observable.interval(1, TimeUnit.SECONDS));

                            Thread.sleep(5000);
                        }
                    }
                }
        )).subscribe(num -> System.out.println(num));
    }

    /*
        This operator will zip multiple source observables into one observable, in sequence. For example, the first item emitted by the first observable
        will match with the first item emitted by the second observable. The second item emitted by the first observable will match with the second
        item emitted by the second observable. It will only emit as many items as the number of items emitted by the source Observable that emits the fewest items.
     */
    public void testUsingZip() throws InterruptedException {
        System.out.println("Example #1");
        Observable<Integer> obs1 = Observable.range(0, 5);
        Observable<Integer> obs2 = Observable.range(10, 5);

        Observable.zip(obs1, obs2, (item1, item2) -> item1 + ":" + item2)
                .subscribe(res -> System.out.println(res));

        System.out.println("Example #2");
        Observable<Long> obs3 = Observable.interval(1, TimeUnit.SECONDS);
        Observable<Long> obs4 = Observable.interval(2, TimeUnit.SECONDS);
        Observable.zip(obs3, obs4, (item1, item2) -> item1 + ":" + item2)
                .subscribe(res -> System.out.println(res));

        Thread.sleep(20000);

        System.out.println("Example #3");
        Observable<Integer> obs5 = Observable.range(20, 5);
        List<Observable<Integer>> list = new ArrayList<>(Arrays.asList(obs1, obs2, obs5));

        Observable.zip(list, new Function<Object[], String>() {
            @Override
            public String apply(@NonNull Object[] objects) {
                String ret = "";
                for (Object num : objects) {
                    ret += String.valueOf(num);
                }
                return ret;
            }
        })
        .subscribe(s -> System.out.println(s));
    }

    public void testUsingZipWith() {
        Observable<Integer> obs1 = Observable.range(0, 5);
        Observable.range(10, 5)
                .zipWith(obs1, (item1, item2) -> item1 + ":" + item2)
                .subscribe(s -> System.out.println(s));
    }

    /*
        This operator is used to concatenate multiple source observables into a single observable which emits items based on the order of
        emission of the source observables. It will subscrible to a new observable after the previous subscribed observable terminates.  
     */
    public void testUsingConcat() throws InterruptedException {
        Observable<Integer> obs1 = Observable.range(1, 5);
        Observable<Integer> obs2 = Observable.range(10, 5).delay(2000, TimeUnit.MILLISECONDS);
        Observable<Integer> obs3 = Observable.range(20, 5).delay(3000, TimeUnit.MILLISECONDS);

        Observable.concat(obs1, obs2, obs3)
                .subscribe(num -> System.out.println(num));

        Thread.sleep(10000);
    }
}
