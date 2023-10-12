package ru.job4j.thread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.*;
import java.nio.file.Files;

public class Wget implements Runnable {
    private final String url;
    private final int speed;

    public Wget(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }

    @Override
    public void run() {
        var startAt = System.currentTimeMillis();
        var file = new File("tmp.xml");
        try (var in = new URL(url).openStream();
             var out = new FileOutputStream(file)) {
            System.out.println("Open connection: " + (System.currentTimeMillis() - startAt) + " ms");
            var dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, dataBuffer.length)) != -1) {
                var downloadAt = System.nanoTime();
                out.write(dataBuffer, 0, bytesRead);
                double downloadSpeed = (1024d / (System.nanoTime() - downloadAt)) * 1_000_000;
                if (downloadSpeed > speed) {
                    Thread.sleep((long) downloadSpeed / speed);
                }
                System.out.println("Read 1024 bytes : " + (System.nanoTime() - downloadAt) + " nano.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(Files.size(file.toPath()) + " bytes");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void validate(String location, int speed) {
        if (speed <= 0) {
            throw new IllegalArgumentException(String.format("Speed is not correct, %s", speed));
        }
        try {
            URL url = new URL(location);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            if (!(connection.getResponseCode() == HttpURLConnection.HTTP_OK)) {
                throw new IllegalArgumentException(String.format("URL is not correct, %s", location));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        validate(url, speed);
        Thread wget = new Thread(new Wget(url, speed));
        wget.start();
        wget.join();
    }
}

