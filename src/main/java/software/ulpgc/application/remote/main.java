package software.ulpgc.application.remote;

import software.ulpgc.application.Display;
import software.ulpgc.application.MovieDeserializer;
import software.ulpgc.application.RemoteStore;

import java.io.IOException;

public class main {
    public static void main(String[] args) throws IOException {
        Display
                .create(new RemoteStore(MovieDeserializer::fromTsv))
                .mostrar()
                .setVisible(true);
    }

}
