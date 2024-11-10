package com.erzbir.di;

import com.erzbir.albaz.di.beans.annotation.Component;
import com.erzbir.albaz.di.beans.annotation.register.AbstractAnnotationRegistryFactory;
import com.erzbir.albaz.di.beans.annotation.register.AnnotationType;

/**
 * @author Erzbir
 * @since 1.0.0
 */
@Component("annotationRegistryFactory")
public class CommonAnnotationRegistryFactory extends AbstractAnnotationRegistryFactory {
    @Override
    public void registerAnnotations() {
        addAnnotation(new AnnotationType(Filter.class));
        addAnnotation(new AnnotationType(Handler.class));
    }
}
