package software.ulpgc.architecture.viewmodel;

import software.ulpgc.architecture.model.Movie;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class HistogramBuilder {
    private final Stream<Movie> movies;
    private final Map<String, String >labels;

    public static HistogramBuilder with(Stream<Movie> movies){
        return new HistogramBuilder(movies);
    }

    private HistogramBuilder(Stream<Movie> movies) {
        this.movies = movies.filter(m-> m.year() >= 1900).filter(m-> m.year() <= 2026);
        this.labels = new HashMap<>();
    }

    public HistogramBuilder tittle(String label){
        labels.put("tittle", label);
        return this;
    }

    public HistogramBuilder x(String label){
        labels.put("x", label);
        return this;
    }

    public HistogramBuilder y(String label){
        labels.put("y", label);
        return this;
    }

    public HistogramBuilder leyend(String label){
        labels.put("leyend", label);
        return this;
    }

    public Histogram build(Function<Movie, Integer> binarize){
        Histogram histogram = new Histogram(labels);
        movies.map(binarize).forEach(histogram::addTo);
        return histogram;
    }
}
