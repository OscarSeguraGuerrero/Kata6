package software.ulpgc.architecture.io;

import software.ulpgc.architecture.model.Movie;

import java.io.IOException;
import java.util.stream.Stream;

public interface Store {
    Stream<Movie> loadAll() throws IOException;
}
