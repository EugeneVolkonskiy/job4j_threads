package ru.job4j.concurrent;

public class ConsoleProgress implements Runnable {
    @Override
    public void run() {
        boolean interrupted = Thread.currentThread().isInterrupted();
        while (!interrupted) {
            try {
                var process = new char[]{'-', '\\', '|', '/'};
                for (char c : process) {
                    System.out.print("\rLoading ... " + c);
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                interrupted = true;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread progress = new Thread(new ConsoleProgress());
        progress.start();
        Thread.sleep(5000);
        progress.interrupt();
    }
}
