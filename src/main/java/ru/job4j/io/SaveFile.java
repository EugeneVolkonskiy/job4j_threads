package ru.job4j.io;

import java.io.*;

public final class SaveFile {

    private final File file;

    public SaveFile(File file) {
        this.file = file;
    }

    public synchronized void saveContent(String content) {
        try (BufferedWriter o = new BufferedWriter(new FileWriter(file))) {
            o.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
