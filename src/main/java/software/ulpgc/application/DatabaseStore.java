package software.ulpgc.application;

import software.ulpgc.architecture.io.Store;
import software.ulpgc.architecture.model.Movie;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.stream.Stream;

public class DatabaseStore implements Store {
    private final Connection connection;

    public DatabaseStore(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Stream<Movie> loadAll() throws IOException {
        try {
            return moviesIn(query());
        } catch (Exception e){
            throw new RuntimeException();
        }
    }

    private Stream<Movie> moviesIn(ResultSet query) {
        return Stream.generate(() -> {
            try {
                return movieIn(query);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }).takeWhile(Objects::nonNull);
    }

    private Movie movieIn(ResultSet rs) throws SQLException {
        try {
            return rs.next() ? readFrom(rs) : null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Movie readFrom(ResultSet rs) throws SQLException {
        return new Movie(
                rs.getString(1),
                rs.getInt(2),
                rs.getInt(3)
        );
    }


    private ResultSet query() throws SQLException {
        return connection.createStatement().executeQuery("SELECT * FROM movies");
    }
}
