package Operators;

import hu.akarnokd.rxjava2.math.MathObservable;
import io.reactivex.Observable;
/*
    Mathematics operators are in the rxjava2-extension module. For Rxjava 1.0, these operations are in rxjava-math module. But since this module
    does not support rxjava 2.0. We use rxjava2-extension module here.
 */
public class MathematicsOperators {

    /*
        This operator calculate the average of the emitted numbers.
     */
    public void testUsingAverage() {
        Observable<Integer> obs = Observable.just(1, 2, 3, 4, 5);
        MathObservable.averageDouble(obs)
                .subscribe((num) -> System.out.println(num));
    }
}
