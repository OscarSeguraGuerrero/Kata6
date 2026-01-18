package software.ulpgc.application;

import software.ulpgc.architecture.io.Store;
import software.ulpgc.architecture.model.Movie;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;

public class RemoteStore implements Store {
    private final Function<String, Movie> deserialize;

    public RemoteStore(Function<String, Movie> deserialize) {
        this.deserialize = deserialize;
    }

    @Override
    public Stream<Movie> loadAll() throws IOException {
        return LoadFrom(new URL("https://datasets.imdbws.com/title.basics.tsv.gz"));
    }

    private Stream<Movie> LoadFrom(URL url) throws IOException {
        return LoadFrom(url.openConnection());
    }

    private Stream<Movie> LoadFrom(URLConnection urlConnection) throws IOException {
        return LoadFrom(unzip(urlConnection.getInputStream()));
    }

    private Stream<Movie> LoadFrom(InputStream is) throws IOException {
        return LoadFrom(new BufferedReader(new InputStreamReader(is)));
    }

    private Stream<Movie> LoadFrom(BufferedReader reader) throws IOException {
        return reader.lines().skip(1).map(deserialize);
    }

    private Movie toMovie(String line) {
        return deserialize.apply(line);
    }

    private InputStream unzip(InputStream is) throws IOException {
        return new GZIPInputStream(new BufferedInputStream(is, 4096));
    }
}
