/**
 * <p>
 * The original code in this module is from spring-framework
 * </p>
 * <a href="https://github.com/spring-projects/spring-framework/tree/v6.2.6/spring-jcl">spring-jcl</a>
 *
 * @author Erzbir
 * @since 1.0.0
 */
module com.erzbir.albaz.jcl {
    requires static org.apache.logging.log4j;
    requires static org.slf4j;
    requires static java.logging;
    exports com.erzbir.albaz.logging;
}