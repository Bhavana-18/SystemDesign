package CountDeliveryZones;

public class DeliveryZones {
    int[][] grid ;
    int[] d = {-1,0,1,0,-1};
    int m ,n;
    DeliveryZones(int m , int n){
        grid = new int[m][n];
         this.m= m;
         this.n = n;
    }
    private void dfs(int[][] grid, boolean[][] visited, int r , int c){

            visited[r][c] = true;
            for(int i = 0; i<4; i++){
                int row = r + d[i];
                int col = c + d[i+1];
                if(row >= 0 && row<m && col>=0 && col<n && !visited[row][col])
                    dfs(grid, visited, row, col);
            }

    }
    public void openRestaurant(int r, int c){
        if(r >= 0 && r<m && c>=0 && c<n){
            grid[r][c] += 1;
        }
    }

    public int countOpenRestaurants(int r , int c){
        if(r >= 0 && r< m && c>=0 && c<n){
            return grid[r][c];
        }
        return 0;
    }
    public int countDeliveryZones(){
        int count = 0;
        boolean[][] visited = new boolean[m][n];

        for(int i = 0; i<m ; i++){
            for(int j = 0; j<n; j++){
                if(!visited[i][j]){
                    count++;
                    dfs(grid, visited, i,j);
                }

            }
        }

        return count;
    }


}
