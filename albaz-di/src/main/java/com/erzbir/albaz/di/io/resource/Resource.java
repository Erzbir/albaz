package com.erzbir.albaz.di.io.resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface Resource {
    InputStream getInputStream() throws IOException;
}
