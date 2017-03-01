package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MainGenerator {
    private static final String PROJECT_DIR = System.getProperty("user.dir");
    private static final String pathEndpoint = "\\app\\src\\main\\java";

    public static void main(String[] args) {
        Schema schema = new Schema(1, "prakhar.com.githubrepo.db");
        schema.enableKeepSectionsByDefault();

        addTables(schema);

        try {
            /* Use forward slashes if you're on macOS or Unix, i.e. "/app/src/main/java"  */
            new DaoGenerator().generateAll(schema, PROJECT_DIR + pathEndpoint);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addTables(final Schema schema) {
        Entity repo = addRepo(schema);
    }


    private static Entity addRepo(final Schema schema) {
        Entity repo = schema.addEntity("Repo");
        repo.addIdProperty().primaryKey().autoincrement();
        repo.addStringProperty("title").notNull();
        repo.addStringProperty("html_url");

        return repo;
    }
}
