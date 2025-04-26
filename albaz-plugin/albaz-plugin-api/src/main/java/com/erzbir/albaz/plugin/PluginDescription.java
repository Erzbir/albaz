package com.erzbir.albaz.plugin;


/**
 * @author Erzbir
 * @since 1.0.0
 */
public record PluginDescription(String id, String name, String desc, String author, String version) {

    private PluginDescription(Builder builder) {
        this(builder.id, builder.name, builder.desc, builder.author, builder.version);
    }

    public PluginDescription(String id, String name, String desc, String author, String version) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.author = author;
        this.version = version;
    }

    public PluginDescription(String id, String version) {
        this(id, "", "", "", version);
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
            this.name = "";
            this.desc = "";
            this.author = "";
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
