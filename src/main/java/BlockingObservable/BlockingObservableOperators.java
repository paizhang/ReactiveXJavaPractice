package BlockingObservable;

import io.reactivex.Observable;

public class BlockingObservableOperators {

    /*
        The blocking operator family will block the current thread until it returns the item with original type.
        Below is an example for using blockingFirst() which will returns the first item emit by the Observable with Integer type.
     */
    public void testUsingToBlockingAndFirst() {
        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5);
        Integer num = observable.blockingFirst();
        observable.subscribe(n -> System.out.println(n));
        System.out.println(num);
    }
    
}
