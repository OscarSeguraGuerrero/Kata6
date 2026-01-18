package software.ulpgc.application.database;

import software.ulpgc.application.*;
import software.ulpgc.architecture.model.Movie;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.stream.Stream;

public class main {
    public static void main(String[] args) throws SQLException, IOException {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:movies.db")){
            connection.setAutoCommit(false);
            importIfNeededInto(connection);
            Display.create(new DatabaseStore(connection))
                    .mostrar()
                    .setVisible(true);
        }
    }

    private static void importIfNeededInto(Connection connection) throws IOException, SQLException {
        if (new File("movies.db").length() > 0) return;
        Stream<Movie> movies = new RemoteStore(MovieDeserializer::fromTsv).loadAll();
        new DatabaseRecorder(connection).record(movies);
    }
}
