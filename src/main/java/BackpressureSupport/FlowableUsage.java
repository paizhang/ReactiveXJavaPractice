package BackpressureSupport;

import io.reactivex.Flowable;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

public class FlowableUsage {

    /*
        In Rxjava 2.0, Observable does not support backpressure by designed. It introduces a Observable type called Flowable which supports
        backpressure. Here is a nice article explaining backpressure. (https://juejin.im/post/582d413c8ac24700619cceed)
        Use cases:
        For observable that emits items in less than 1000 per second, use Observable type for cost efficiency.
        For observable that emits items in more that 1000 per second, use Flowable type for handling backpressure.

        The main strategy that Flowable handling backpressure is reactive pull strategy. The subscriber can use subscription.request(n) to
        reactively pull items from the flowable. Thus the traffic rate can be control on the subscriber side.

        Observable can also apply sample operator or window operator to handle backpressure by dropping or buffering some items.

        TODO: I tried to emit 10^9 items per second and consume 3 items per second using Observable. But it seems it can still handle properly without throwing MissingBackpressureException exception.
        Find a way to test it again to find out the limit of observable and the ways to monitor the performance. 
     */
    public void testUsingFlowable() throws InterruptedException {
        Flowable<Long> flowable = Flowable.interval(1, TimeUnit.MILLISECONDS);
        flowable.subscribe(new Subscriber<Long>() {
            Subscription sub;
            @Override
            public void onSubscribe(Subscription s) {
                sub = s;
                s.request(1);
            }

            @Override
            public void onNext(Long integer) {
                System.out.println("OnNext:");
                System.out.println(integer);
                sub.request(2);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {
                System.out.println("Complete!");
            }
        });

        Thread.sleep(10000);
    }
}
