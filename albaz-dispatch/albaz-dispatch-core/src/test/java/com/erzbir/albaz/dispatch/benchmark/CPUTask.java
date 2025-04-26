package com.erzbir.albaz.dispatch.benchmark;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public class CPUTask implements Runnable {

    private final int limit;

    public CPUTask(int limit) {
        this.limit = limit;
    }

    @Override
    public void run() {
        int count = 0;
        for (int i = 2; i < limit; i++) {
            if (isPrime(i)) {
                count++;
            }
        }
    }

    private boolean isPrime(int num) {
        if (num <= 1) return false;
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) return false;
        }
        return true;
    }
}
