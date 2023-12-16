package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBConnectionWrapper {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost/";

    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final int TIMEOUT = 5;


    private Connection connection;

    public JDBConnectionWrapper(String schema){
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL + schema, USER, PASSWORD);
            //Bootstrap
            //createTablesBook();
            //createTablesUserRole();
            //createTablesRoleRight();
            //createTablesUser();
            //insertTablesBook();


        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }


    private void createTablesBook() throws SQLException {
        Statement statement = connection.createStatement();

        String sql = "CREATE TABLE IF NOT EXISTS book(" +
                "id bigint NOT NULL AUTO_INCREMENT," +
                "author varchar(500) NOT NULL," +
                "title varchar(500) NOT NULL," +
                "publishedDate datetime DEFAULT NULL," +
                "quantity int(11), " +
                "price float(8,2)," +
                "PRIMARY KEY(id),"+
                "UNIQUE KEY id_UNIQUE(id)" +
                ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;";
        statement.execute(sql);
    }

    private void createTablesUserRole() throws SQLException{
        Statement statement = connection.createStatement();

        String sql="CREATE TABLE IF NOT EXISTS user_role (" +
                "  id INT NOT NULL AUTO_INCREMENT," +
                "  user_id INT NOT NULL," +
                "  role_id INT NOT NULL," +
                "  PRIMARY KEY (id)," +
                "  UNIQUE INDEX id_UNIQUE (id ASC)," +
                "  INDEX user_id_idx (user_id ASC)," +
                "  INDEX role_id_idx (role_id ASC)," +
                "  CONSTRAINT user_fkid" +
                "    FOREIGN KEY (user_id)" +
                "    REFERENCES user (id)" +
                "    ON DELETE CASCADE" +
                "    ON UPDATE CASCADE," +
                "  CONSTRAINT role_fkid" +
                "    FOREIGN KEY (role_id)" +
                "    REFERENCES role (id)" +
                "    ON DELETE CASCADE" +
                "    ON UPDATE CASCADE);";
        statement.execute(sql);
    }

    public void createTablesRoleRight() throws SQLException{
        Statement statement = connection.createStatement();

        String sql="  CREATE TABLE IF NOT EXISTS role_right (" +
                "  id INT NOT NULL AUTO_INCREMENT," +
                "  role_id INT NOT NULL," +
                "  right_id INT NOT NULL," +
                "  PRIMARY KEY (id)," +
                "  UNIQUE INDEX id_UNIQUE (id ASC)," +
                "  INDEX role_id_idx (role_id ASC)," +
                "  INDEX right_id_idx (right_id ASC)," +
                "  CONSTRAINT role_id" +
                "    FOREIGN KEY (role_id)" +
                "    REFERENCES role (id)" +
                "    ON DELETE CASCADE" +
                "    ON UPDATE CASCADE," +
                "  CONSTRAINT right_id" +
                "    FOREIGN KEY (right_id)" +
                "    REFERENCES `right` (id)" +
                "    ON DELETE CASCADE" +
                "    ON UPDATE CASCADE);";
        statement.execute(sql);
    }

    public void insertTablesBook() throws SQLException{
        Statement statement = connection.createStatement();

        String sql= "INSERT INTO book values(null,'Eminescu','uewiywero','1930-06-10',70,30);";

        statement.execute(sql);
    }

    public void createTablesUser() throws SQLException{
        Statement statement = connection.createStatement();

        String sql="CREATE TABLE IF NOT EXISTS user (" +
                "  id INT NOT NULL AUTO_INCREMENT," +
                "  username VARCHAR(200) NOT NULL," +
                "  password VARCHAR(64) NOT NULL," +
                "  PRIMARY KEY (id)," +
                "  UNIQUE INDEX id_UNIQUE (id ASC)," +
                "  UNIQUE INDEX username_UNIQUE (username ASC));";
        statement.execute(sql);
    }


    public boolean testConnection() throws SQLException {
        return connection.isValid(TIMEOUT);
    }

    public Connection getConnection(){
        return connection;
    }

}