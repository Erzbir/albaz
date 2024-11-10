package com.erzbir.albaz.plugin;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Erzbir
 * @since 1.0.0
 */
@Setter
@Getter
public class PluginDescription {
    private String id;
    private String name;
    private String desc;
    private String author;
    private String version;

    private PluginDescription(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.desc = builder.desc;
        this.author = builder.author;
        this.version = builder.version;
    }

    public PluginDescription(String id, String version) {
        this.id = id;
        this.version = version;
    }

    public static class Builder {
        private String id;
        private String name;
        private String desc;
        private String author;
        private String version;

        public Builder(String id, String version) {
            this.id = id;
            this.version = version;
        }


        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder desc(String desc) {
            this.desc = desc;
            return this;
        }

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public PluginDescription build() {
            return new PluginDescription(this);
        }
    }
}
