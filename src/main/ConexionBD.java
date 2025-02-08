package main;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class ConexionBD {
    // Configuración de la base de datos
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USUARIO = "postgres";
    private static final String CONTRASEÑA = "admin";

//Metodo para conectar con la base de datos
   
    public static Connection conectar() {
        try {
            // Cargar el driver (opcional en Java 8+)
            Class.forName("org.postgresql.Driver");
            
            // Establecer conexión
            Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
            System.out.println("¡Conexión exitosa!");
            return conexion;
        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se encontró el driver JDBC.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error de conexión a PostgreSQL.");
            e.printStackTrace();
        }
        return null;
    }


// Main para provar la conexion
    public static void main(String[] args) {
        conectar();
    }
    
}
