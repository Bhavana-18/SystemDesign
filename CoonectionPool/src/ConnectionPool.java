import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConnectionPool {

    private final BlockingQueue<Connection> pool;

    public ConnectionPool(int size, String url, String user, String pass) throws SQLException {
        pool = new ArrayBlockingQueue<>(size);

        for (int i = 0; i < size; i++) {
            Connection realConn = DriverManager.getConnection(url, user, pass);
            pool.add(realConn);
        }
    }

    public Connection getConnection() throws InterruptedException {
        Connection realConn = pool.take();
        return new PooledConnection(realConn, this);
    }

    protected void releaseConnection(Connection conn) {
        pool.offer(conn);
    }
}