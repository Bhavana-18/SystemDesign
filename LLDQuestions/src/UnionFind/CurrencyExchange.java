package UnionFind;

import java.util.HashMap;
import java.util.Map;

public class CurrencyExchange {

    Map<String, String> parent = new HashMap<>();
    Map<String, Double> value = new HashMap<>();
    private String findPar(String u){
        if(!parent.get(u).equals(u)){
            String originalParent = parent.get(u);
            String ultimateParent = findPar(originalParent);
            value.put(u, value.getOrDefault(u, 1.0) * value.getOrDefault(originalParent,1.0));
            parent.put(u , ultimateParent);
        }
        return parent.get(u);
    }
    private void union(String u , String v, double val){
        if(!parent.containsKey(u)){
            parent.put(u , u);
            value.put(u,1.0);
        }
        if(!parent.containsKey(v)){
            parent.put(v , v);
            value.put(v,1.0);
        }

        String ulp_u = findPar(u);
        String ulp_v = findPar(v);
        if(!ulp_u.equals(ulp_v)){
            parent.put(ulp_u, ulp_v);
            double total = value.get(v) * val/value.get(u);

            value.put(ulp_u, total * value.get(ulp_u));
        }
    }

    public double getCurrencyExchange(String from, String to, double amount) {
        if (!parent.containsKey(from) || !parent.containsKey(to)) {
            return -1.0;
        }

        String rootFrom = findPar(from);
        String rootTo = findPar(to);

        if (!rootFrom.equals(rootTo)) {
            return -1.0;
        }

        // Formula: amount × (value[from] / value[to])
        return amount * value.get(from) / value.get(to);
    }

    // Helper to build from edges
    public void buildRates(String[][] pairs, double[] rates) {
        for (int i = 0; i < pairs.length; i++) {
            union(pairs[i][0], pairs[i][1], rates[i]);
        }
    }
}
