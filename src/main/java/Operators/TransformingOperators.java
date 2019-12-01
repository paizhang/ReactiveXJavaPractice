package Operators;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
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

    public void testUsingFlatMap() throws InterruptedException {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(4, 5, 6));
        List<Integer> list3 = new ArrayList<>(Arrays.asList(7, 8, 9));
        List<List<Integer>> listDic = new ArrayList<>();
        listDic.add(list1);
        listDic.add(list2);
        listDic.add(list3);

        Observable.fromIterable(listDic)
                .doOnNext(s -> System.out.println("Processing in thread: " + Thread.currentThread().getName()))
                .subscribeOn(Schedulers.computation())
                .flatMap(new Function<List<Integer>, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(List<Integer> integers) throws Exception {
                        return getNumsFromSubList(integers);
                    }
                })
                .subscribe(num -> System.out.println(num));
        Thread.sleep(10000);
    }

    /*
        Apply multiple threading for each flatMap operation. 
     */
    private Observable<Integer> getNumsFromSubList(List<Integer> list) {
        return Observable.fromIterable(list)
                .subscribeOn(Schedulers.computation());
    }

    /*
        The return value of groupBy is a Observable with a type as GroupedObservable<K, T> which is a class that extends Observable class.

     */
    public void testUsingGroupBy() {
        Observable.range(0, 10)
                .groupBy(num -> num % 3)
                .subscribe(groupedObservable -> groupedObservable.subscribe(num -> System.out.println("Group ID:" + groupedObservable.getKey() + "Value:" + String.valueOf(num))));
    }

    /*

     */
    public void testUsingScan() {
        Observable.range(0, 10)
                .scan((x, y) -> x + y)
                .subscribe(num -> System.out.println(num));
    }

    public void testUsingWindowForCount() {
        Observable.range(0,20)
                .window(5)
                .subscribe(obs -> {System.out.println("OnNext"); obs.subscribe(num -> System.out.println(num));});
    }

    public void testUsingWindowForTimespan() throws InterruptedException {
        Observable.interval(1, TimeUnit.SECONDS)
                .window(3, TimeUnit.SECONDS)
                .subscribe(obs -> {System.out.println("OnNext"); obs.subscribe(num -> System.out.println(num));});
        Thread.sleep(20000);
    }

    // This will a List of items for every interval amount of time
    public void testUsingBufferForInterval() throws InterruptedException {
        Observable.interval(1, TimeUnit.SECONDS)
                .buffer(3, TimeUnit.SECONDS)
                .subscribe(s -> System.out.println(s));
        Thread.sleep(100000);
    }

    /*
        This operator apply a function to items emitted by a observable sequentially and output the final result.
        This operator is similar with scan operator. The only difference is that scan operator output the results for each step. And
        reduce operator only outputs the final result.
     */
    public void testUsingReduce() {
        Observable.range(1, 5)
                .reduce((x, y) -> x + y)
                .subscribe(num -> System.out.println(num));
    }

    /*
        This operator collect each individual elements into a collection. The return value of this operator is a Single observable with the type
        of that collection.
     */
    public void testUsingCollect() {
        Observable.range(1, 5)
                .collect(new Callable<ArrayList<Integer>>() {
                    @Override
                    public ArrayList<Integer> call() {
                        return new ArrayList<Integer>();
                    }
                }, new BiConsumer<ArrayList<Integer>, Integer>() {
                    @Override
                    public void accept(ArrayList<Integer> integers, Integer integer) throws Exception {
                        integers.add(integer);
                    }
                })
        .subscribe(nums -> System.out.println(String.valueOf(nums)));
    }
}
