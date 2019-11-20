package Creation;

import io.reactivex.Single;

public class SingleCreation {

    private static void createdSingleUsingJust() {
        Single<String> single = Single.just("Single");
        single.subscribe(s -> System.out.println("Succeed from Single created using Just. "), throwable -> {System.out.println("Exception from Single. ");});
    }

    private static void createdSingleFromCallable() {
        Single<Integer> single = Single.fromCallable(() -> {throw new RuntimeException("This is runtime exception. ");});
        single.subscribe(s -> System.out.println("Single from Callable. "), throwable -> {System.out.println(throwable.toString());});
    }

    public static void main(String[] args) {
        createdSingleUsingJust();
    }
}
