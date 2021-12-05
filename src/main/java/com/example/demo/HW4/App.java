package com.example.demo.HW4;

public class App {
    private final Object mon = new Object();
    private volatile char currentLetter = 'A';

    public static void main(String[] args) {
        App app = new App();
        Thread thread1 = new Thread(() -> app.printChar('A','B'));
        Thread thread2 = new Thread(() -> app.printChar('B','C'));
        Thread thread3 = new Thread(() -> app.printChar('C','A'));

        thread1.start();
        thread2.start();
        thread3.start();
    }

    public void printChar(char from, char to){
        synchronized (mon) {
            for (int i = 0; i < 5; i++) {
                while (currentLetter!=from) {
                    try {
                        mon.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.print(from);
                currentLetter = to;
                mon.notifyAll();
            }
        }
    }

}
