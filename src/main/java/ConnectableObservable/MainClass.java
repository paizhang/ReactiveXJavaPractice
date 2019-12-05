package ConnectableObservable;

public class MainClass {

    public static void main(String[] args) throws InterruptedException {
        ConnectableObservableOperator connectableObservableOperator = new ConnectableObservableOperator();
        connectableObservableOperator.testUsingReplyAndSubscribeAndConnect();
    }
}
