package Operators;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.Arrays;
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

    // This will a List of items for every interval amount of time
    public void testUsingBufferForInterval() throws InterruptedException {
        Observable.interval(1, TimeUnit.SECONDS)
                .buffer(3, TimeUnit.SECONDS)
                .subscribe(s -> System.out.println(s));
        Thread.sleep(100000);
    }
}
