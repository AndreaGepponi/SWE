import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostgreSQLDatabaseHelper {
    private Connection connection;

    // Metodo per stabilire la connessione al database PostgreSQL
    public void connect(String url, String username, String password) throws SQLException {
        connection = DriverManager.getConnection(url, username, password);
        System.out.println("Connessione al database PostgreSQL stabilita.");
    }

    // Metodo per chiudere la connessione al database PostgreSQL
    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Connessione al database PostgreSQL chiusa.");
        }
    }

    // Metodo per eseguire una query SQL e restituire un ResultSet
    public ResultSet executeQuery(String query) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        return statement.executeQuery();
    }

    // Metodo per eseguire un'operazione SQL (INSERT, UPDATE, DELETE)
    public int executeUpdate(String query) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        return statement.executeUpdate();
    }

    public void printData(ResultSet resultSet) throws SQLException {
        int columnCount = resultSet.getMetaData().getColumnCount();
        System.out.println("---------------------------------------------------------------------------");
        for (int i = 1; i <= columnCount; i++) {
            String columnName = resultSet.getMetaData().getColumnName(i);
            System.out.print(columnName + " | ");
        }
        System.out.println();
        System.out.println("---------------------------------------------------------------------------");

        while (resultSet.next()){
            for (int i = 1; i <= columnCount; i++) {
                String columnValue = resultSet.getString(i);
                System.out.print(columnValue + " | ");
            }
            System.out.println();
        }
        System.out.println("---------------------------------------------------------------------------");
    }
}
