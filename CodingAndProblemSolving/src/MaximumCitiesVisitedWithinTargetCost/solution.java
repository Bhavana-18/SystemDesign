package MaximumCitiesVisitedWithinTargetCost;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

class State{
    int city, cost, visited;
    State(int city, int cost, int visited){
        this.city = city;
        this.cost = cost;
        this.visited = visited;
    }
}

public class solution {

    public int maxCitiesVisited(int[][] edges, int n, int balance, int source, int destination){
        List<int[]>[] graph = new ArrayList[n];
        for(int i = 0; i<n; i++)
            graph[i] = new ArrayList<>();
        for(int[] edge: edges){
            int u = edge[0];
            graph[u].add(new int[]{edge[1], edge[2]});
            graph[edge[1]].add(new int[]{edge[0], edge[2]});
        }
        int maxCities = 0;

        int[][] visitedDP = new int[n][balance+1];
        visitedDP[source][0] = 1;
        for(int[] row: visitedDP) {
            Arrays.fill(row, -1);
        }

        PriorityQueue<State> pq = new PriorityQueue<>((a, b) ->(b.visited - a.visited));

        pq.add(new State(source, 0, 1));

        while(!pq.isEmpty()){
            State curr = pq.poll();
            if (curr.city == destination) {
                maxCities = Math.max(maxCities, curr.visited);
            }
            for(int[] nbr : graph[curr.city]){
                int nextCity = nbr[0];
                int nextCost = nbr[1] + curr.cost;

                if(nextCost > balance) continue;

                if(visitedDP[nextCity][nextCost] < 1 + curr.visited){
                    visitedDP[nextCity][nextCost] = 1+ curr.visited;
                    pq.offer(new State(nextCity, nextCost, curr.visited + 1));
                }



            }
        }

   return  maxCities == 0? -1: maxCities;
    }


}
