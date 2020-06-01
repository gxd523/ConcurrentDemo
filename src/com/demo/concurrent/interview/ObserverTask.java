package com.demo.concurrent.interview;

public class ObserverTask {
    int count;

    private ObserverTask(Observable observable) {
        observable.register(event ->
                System.out.println(String.format("\n我得到的数字是%s", count))
        );

        for (int i = 0; i < 99999; i++) {
            String[] strings = new String[9999];
        }

        count = 100;
    }

    public static ObserverTask getInstance(Observable observable) {
        ObserverTask observerTask = new ObserverTask(observable);
        return observerTask;
    }

    public static void main(String[] args) {
        Observable observable = new Observable();
        new ObserverTask(observable);

        new Thread(() -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            observable.postEvent(new Event());
        }
        ).start();


//        ObserverTask.getInstance(observable);
    }

    interface Observer {
        void onEvent(Event event);
    }

    static class Observable {
        private Observer observer;

        public void register(Observer observer) {
            this.observer = observer;
        }

        public void postEvent(Event event) {
            if (observer != null) {
                observer.onEvent(event);
            }
        }
    }

    static class Event {
    }
}
