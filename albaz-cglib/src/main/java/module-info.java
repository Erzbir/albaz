/**
 * @author Erzbir
 * @since 1.0.0
 */
module albaz.cglib {
    requires java.desktop;
    exports org.springframework.cglib to albaz.context;
    exports org.springframework.cglib.proxy to albaz.context;
}