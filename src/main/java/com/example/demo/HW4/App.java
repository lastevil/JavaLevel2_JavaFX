package com.example.demo.HW4;

public class App {
    private final Object mon = new Object();
    private volatile char currentLetter = 'A';

    public static void main(String[] args) {
        App app = new App();
        Thread thread1 = new Thread(() -> app.printA());
        Thread thread2 = new Thread(() -> app.printB());
        Thread thread3 = new Thread(() -> app.printC());

        thread1.start();
        thread2.start();
        thread3.start();
    }

    public void printA(){
        synchronized (mon) {
            for (int i = 0; i < 5; i++) {
                while (currentLetter!='A') {
                    try {
                        mon.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.print('A');
                currentLetter = 'B';
                mon.notifyAll();
            }
        }
    }

    public void printB(){
        synchronized (mon) {
            for (int i = 0; i < 5; i++) {
                while (currentLetter!='B') {
                    try {
                        mon.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.print('B');
                currentLetter = 'C';
                mon.notifyAll();
            }
        }
    }

    public void printC(){
        synchronized (mon) {
            for (int i = 0; i < 5; i++) {
                while (currentLetter!='C') {
                    try {
                        mon.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.print('C');
                currentLetter = 'A';
                mon.notifyAll();
            }
        }
    }

}
