package software.ulpgc.architecture.io;

import software.ulpgc.architecture.model.Movie;

import java.util.stream.Stream;

public interface Recorder {
    void record(Stream<Movie> movies);
}
