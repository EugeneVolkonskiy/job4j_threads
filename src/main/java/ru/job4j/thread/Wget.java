package ru.job4j.thread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.*;
import java.nio.file.Files;

public class Wget implements Runnable {
    private final String url;
    private final int speed;
    private final String fileName;
    private static final long ONE_SECOND = 1000;

    public Wget(String url, int speed, String fileName) {
        this.url = url;
        this.speed = speed;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        var startAt = System.currentTimeMillis();
        var file = new File(fileName);
        try (var in = new URL(url).openStream();
             var out = new FileOutputStream(file)) {
            System.out.println("Open connection: " + (System.currentTimeMillis() - startAt) + " ms");
            var dataBuffer = new byte[speed];
            int bytesRead;
            long bytesReadCount = 0;
            var downloadAt = System.currentTimeMillis();
            while ((bytesRead = in.read(dataBuffer, 0, dataBuffer.length)) != -1) {
                out.write(dataBuffer, 0, bytesRead);
                bytesReadCount += bytesRead;
                if (bytesReadCount == speed) {
                    long now = System.currentTimeMillis();
                    if ((now - downloadAt) < ONE_SECOND) {
                        Thread.sleep(ONE_SECOND - (downloadAt - now));
                    }
                    downloadAt = now;
                    bytesReadCount = 0;
                }
                System.out.printf("Read %s bytes : " + (System.currentTimeMillis() - downloadAt) + " ms.\n", speed);
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

    private static void validate(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("The number of parameters must be 3");
        }
        if (Integer.parseInt(args[1]) <= 0) {
            throw new IllegalArgumentException(String.format("Speed is not correct, %s", args[1]));
        }
        if (!(args[2].contains(".") && args[2].length() > 2)) {
            throw new IllegalArgumentException(String.format("File name is not correct, %s", args[2]));
        }
        try {
            URL url = new URL(args[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            if (!(connection.getResponseCode() == HttpURLConnection.HTTP_OK)) {
                throw new IllegalArgumentException(String.format("URL is not correct, %s", args[0]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        validate(args);
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        String fileName = args[2];
        Thread wget = new Thread(new Wget(url, speed, fileName));
        wget.start();
        wget.join();
    }
}

