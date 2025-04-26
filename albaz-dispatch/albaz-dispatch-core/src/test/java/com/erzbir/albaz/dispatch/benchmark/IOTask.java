package com.erzbir.albaz.dispatch.benchmark;

public class IOTask implements Runnable {
    private final String filePath;

    public IOTask(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void run() {
        try (var writer = new java.io.BufferedWriter(new java.io.FileWriter(filePath))) {
            for (int i = 0; i < 100_000; i++) {
                writer.write("Line " + i + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
