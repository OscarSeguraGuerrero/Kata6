package software.ulpgc.architecture.viewmodel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Histogram implements Iterable<Integer>{
    private final Map<Integer, Integer> map;
    private final Map<String, String> labels;

    public Histogram(Map<String, String> labels) {
        this.map = new HashMap<>();
        this.labels = labels;
    }

    public void addTo(int bin){
        map.put(bin, count(bin) + 1);
    }

    public Integer count(int bin) {
        return map.getOrDefault(bin, 0);
    }

    public Set<Integer> bins(){
        return map.keySet();
    }

    @Override
    public Iterator<Integer> iterator(){
        return map.keySet().iterator();
    }

    public int size(){
        return map.size();
    }

    public String tittle(){
        return labels.getOrDefault("tittle", "");
    }

    public String x(){
        return labels.getOrDefault("x", "");
    }

    public String y(){
        return labels.getOrDefault("y", "");
    }

    public String leyend(){
        return labels.getOrDefault("leyend", "");
    }
}
