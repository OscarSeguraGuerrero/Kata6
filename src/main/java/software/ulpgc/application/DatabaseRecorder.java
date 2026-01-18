package software.ulpgc.application;

import software.ulpgc.architecture.io.Recorder;
import software.ulpgc.architecture.model.Movie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.stream.Stream;

public class DatabaseRecorder implements Recorder {
    private final Connection connection;
    private final PreparedStatement statement;

    public DatabaseRecorder(Connection connection) throws SQLException {
        this.connection = connection;
        this.createTableIfNotExists();
        this.statement = connection.prepareStatement("INSERT INTO movies (tittle, year, duration) VALUES (?, ?, ?)");
    }

    private void createTableIfNotExists() throws SQLException {
        connection.createStatement().execute("CREATE TABLE IF NOT EXISTS movies (tittle STRING, year INTEGER, duration INTEGER)");

    }

    @Override
    public void record(Stream<Movie> movies) {
        try {
            movies.forEach(this::record);
            flush();
            connection.commit();
        }catch (SQLException e){
            throw new RuntimeException();
        }
    }

    private int count = 0;
    private void record(Movie movie) {
        try {
            write(movie);
            flushIfNeeded();
        } catch (Exception e){
            throw new RuntimeException();
        }
    }

    private void flushIfNeeded() throws SQLException {
        if (mustFlush()) flush();
    }

    private void flush() throws SQLException {
        statement.executeBatch();
    }

    private boolean mustFlush() {
        return ++count % 10000 == 0;
    }

    private void write(Movie movie) throws SQLException {
        statement.setString(1, movie.tittle());
        statement.setInt(2, movie.year());
        statement.setInt(3, movie.duration());
        statement.addBatch();
    }
}
