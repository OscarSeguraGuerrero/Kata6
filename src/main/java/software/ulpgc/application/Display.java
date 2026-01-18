package software.ulpgc.application;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import software.ulpgc.architecture.io.Store;
import software.ulpgc.architecture.model.Movie;
import software.ulpgc.architecture.viewmodel.Histogram;
import software.ulpgc.architecture.viewmodel.HistogramBuilder;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.stream.Stream;

public class Display extends JFrame {
    private final Store store;

    public static Display create(Store store){
        return new Display(store);
    }

    private Display(Store store){
        this.store = store;
        this.setTitle("Histograma");
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);

    }

    public Display mostrar() throws IOException{
        this.getContentPane().add(chartPannelOf(histogram()));
        return this;
    }

    private Component chartPannelOf(Histogram histogram) {
        return new ChartPanel(chartOf(histogram));
    }

    private JFreeChart chartOf(Histogram histogram) {
        return ChartFactory.createHistogram(
                histogram.tittle(),
                histogram.x(),
                histogram.y(),
                datasetOf(histogram)
        );
    }

    private XYSeriesCollection datasetOf(Histogram histogram) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(seriesIn(histogram));
        return dataset;
    }

    private XYSeries seriesIn(Histogram histogram) {
        XYSeries series = new XYSeries(histogram.leyend());
        for(int bin: histogram){
            series.add(bin, histogram.count(bin));
        }
        return series;
    }

    private Histogram histogram() throws IOException {
        Histogram histogram = HistogramBuilder.
                with(movies(store)).
                tittle("Histogram").
                x("EJEX").
                y("EJEY").
                leyend("Movies").
                build(m -> (m.year() / 10) * 10);
        return histogram;
    }

    static Stream<Movie> movies(Store store) throws IOException {
        return store.loadAll();
    }
}
