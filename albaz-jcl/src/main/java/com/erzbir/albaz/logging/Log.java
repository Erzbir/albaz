package com.erzbir.albaz.logging;

/**
 * A simple logging interface abstracting logging APIs.  In order to be
 * instantiated successfully by {@link LogFactory}, classes that implement
 * this interface must have a constructor that takes a single String
 * parameter representing the "name" of this Log.
 *
 * <p>The six logging levels used by <code>Log</code> are (in order):
 * <ol>
 * <li>trace (the least serious)</li>
 * <li>debug</li>
 * <li>info</li>
 * <li>warn</li>
 * <li>error</li>
 * <li>fatal (the most serious)</li>
 * </ol>
 * <p>
 * The mapping of these log levels to the concepts used by the underlying
 * logging system is implementation dependent.
 * The implementation should ensure, though, that this ordering behaves
 * as expected.
 *
 * <p>Performance is often a logging concern.
 * By examining the appropriate property,
 * a component can avoid expensive operations (producing information
 * to be logged).
 *
 * <p>For example,
 * <pre>
 *    if (log.isDebugEnabled()) {
 *        ... do something expensive ...
 *        log.debug(theResult);
 *    }
 * </pre>
 *
 * <p>Configuration of the underlying logging system will generally be done
 * external to the Logging APIs, through whatever mechanism is supported by
 * that system.
 *
 * @author Juergen Hoeller
 * @author Erzbir
 * @since 1.0
 */
public interface Log {

    boolean isFatalEnabled();

    boolean isErrorEnabled();

    boolean isWarnEnabled();

    boolean isInfoEnabled();

    boolean isDebugEnabled();

    boolean isTraceEnabled();


    /**
     * Logs a message with fatal log level.
     *
     * @param message log this message
     */
    void fatal(Object message);

    /**
     * Logs an error with fatal log level.
     *
     * @param message log this message
     * @param t       log this cause
     */
    void fatal(Object message, Throwable t);

    /**
     * Logs a message with fatal log level with parameter substitution.
     *
     * @param format log this message with {} placeholders
     * @param args   parameters to substitute
     */
    void fatal(String format, Object... args);

    /**
     * Logs a message with error log level.
     *
     * @param message log this message
     */
    void error(Object message);

    /**
     * Logs an error with error log level.
     *
     * @param message log this message
     * @param t       log this cause
     */
    void error(Object message, Throwable t);


    /**
     * Logs a message with error log level with parameter substitution.
     *
     * @param format log this message with {} placeholders
     * @param args   parameters to substitute
     */
    void error(String format, Object... args);

    /**
     * Logs a message with warn log level.
     *
     * @param message log this message
     */
    void warn(Object message);

    /**
     * Logs an error with warn log level.
     *
     * @param message log this message
     * @param t       log this cause
     */
    void warn(Object message, Throwable t);

    /**
     * Logs a message with warn log level with parameter substitution.
     *
     * @param format log this message with {} placeholders
     * @param args   parameters to substitute
     */
    void warn(String format, Object... args);

    /**
     * Logs a message with info log level.
     *
     * @param message log this message
     */
    void info(Object message);

    /**
     * Logs an error with info log level.
     *
     * @param message log this message
     * @param t       log this cause
     */
    void info(Object message, Throwable t);

    /**
     * Logs a message with info log level with parameter substitution.
     *
     * @param format log this message with {} placeholders
     * @param args   parameters to substitute
     */
    void info(String format, Object... args);

    /**
     * Logs a message with debug log level.
     *
     * @param message log this message
     */
    void debug(Object message);

    /**
     * Logs an error with debug log level.
     *
     * @param message log this message
     * @param t       log this cause
     */
    void debug(Object message, Throwable t);

    /**
     * Logs a message with debug log level with parameter substitution.
     *
     * @param format log this message with {} placeholders
     * @param args   parameters to substitute
     */
    void debug(String format, Object... args);

    /**
     * Logs a message with trace log level.
     *
     * @param message log this message
     */
    void trace(Object message);

    void trace(Object message, Throwable exception);

    /**
     * Logs a message with trace log level with parameter substitution.
     *
     * @param format log this message with {} placeholders
     * @param args   parameters to substitute
     */
    void trace(String format, Object... args);
}