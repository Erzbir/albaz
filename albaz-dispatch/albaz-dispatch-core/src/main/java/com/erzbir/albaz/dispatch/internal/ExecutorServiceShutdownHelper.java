package com.erzbir.albaz.dispatch.internal;

import com.erzbir.albaz.logging.Log;
import com.erzbir.albaz.logging.LogFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public class ExecutorServiceShutdownHelper {
    private static final Log log = LogFactory.getLog(ExecutorServiceShutdownHelper.class);

    public static boolean shutdown(ExecutorService executorService, long shutdownTimeout, TimeUnit unit) {
        if (executorService == null || executorService.isShutdown() || executorService.isTerminated()) {
            return true;
        }
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(shutdownTimeout, unit)) {
                log.warn("Dispatch thread pool did not terminate in [{} {}]. Forcing shutdown", shutdownTimeout, unit.name());
                executorService.shutdownNow();
                if (!executorService.awaitTermination(shutdownTimeout, unit)) {
                    log.warn("Dispatch thread pool did not terminate in [{} {}] even after forced shutdown", shutdownTimeout, unit.name());
                    return false;
                }
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
            return false;
        }

        return true;
    }

    public static void shutdownNow(ExecutorService executorService) {
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }
}
