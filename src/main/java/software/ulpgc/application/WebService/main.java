package software.ulpgc.application.WebService;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.json.JavalinGson;
import io.javalin.json.JavalinJackson;
import org.jetbrains.annotations.NotNull;
import software.ulpgc.application.MovieDeserializer;
import software.ulpgc.application.RemoteStore;
import software.ulpgc.architecture.model.Movie;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class main {
    static final List<Movie> movies;

    static {
        try {
            movies = new RemoteStore(MovieDeserializer::fromTsv).loadAll().limit(500).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.jsonMapper(new JavalinGson()); // Usar Gson
        });
        app.start(8080);

        app.get("/movies", main::getAllMovies);
        app.get("/movies/year", main::getMoviesByYear);
        app.get("/movies/random", main::getRandomMovie);
    }

    private static void getRandomMovie(@NotNull Context context) {
        if (movies.isEmpty()){
            context.status(400).result("No hay películas cargadas");
            return;
        }

        Random rand = new Random();
        Movie randomMovie = movies.get(rand.nextInt(movies.size()));

        context.json(randomMovie);
    }

    private static void getMoviesByYear(@NotNull Context context) {
        String yearParam = context.queryParam("y");
        if (yearParam == null){
            context.status(400).result("Falta el parametro del año");
            return;
        }

        try {
            int year = Integer.parseInt(yearParam);

            List<Movie> filtered = movies.stream().filter(m -> m.year() == year).toList();

            context.json(filtered);
        }catch (NumberFormatException e){
            context.status(400).result("El año debe ser un entero");
        }
    }

    private static void getAllMovies(@NotNull Context context) {
        context.json(movies);
    }
}
