package kz.tastamat.jooq;

import org.jooq.util.GenerationTool;
import org.jooq.util.jaxb.Configuration;
import org.jooq.util.jaxb.Database;
import org.jooq.util.jaxb.Generator;
import org.jooq.util.jaxb.Jdbc;
import org.jooq.util.jaxb.Strategy;
import org.jooq.util.jaxb.Target;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        try (InputStream is = new FileInputStream("/Users/baur/git/toolpar/tastamat/jooq-generator/run.properties")) {
            properties.load(is);
        }

        Configuration configuration = new Configuration()
            .withJdbc(new Jdbc()
                .withDriver("org.postgresql.Driver")
                .withUrl("jdbc:postgresql:" + properties.getProperty("db", "tastamat"))
                .withUser(properties.getProperty("user", "postgres"))
                .withPassword(properties.getProperty("password", "postgres")))
            .withGenerator(new Generator()
                .withStrategy(new Strategy()
                    .withName("kz.tastamat.jooq.CustomStrategy")
                )
                .withDatabase(new Database()
                    .withName("org.jooq.util.postgres.PostgresDatabase")
                    .withIncludes(".*")
                    .withExcludes("")
                    .withInputSchema("public"))
                .withTarget(new Target()
                    .withPackageName("kz.tastamat.db.model.jooq")
                    .withDirectory(properties.getProperty("destination"))));

        GenerationTool.generate(configuration);
    }
}
