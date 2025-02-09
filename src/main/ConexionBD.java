package main;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;  
import java.sql.ResultSet;
public class ConexionBD {
    // Configuración de la base de datos
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USUARIO = "postgres";
    private static final String CONTRASEÑA = "admin";
    public static final int CREDENCIALES_VALIDAS = 1; // Credenciales correctas
    public static final int ID_NO_ENCONTRADO = 2; // ID no existe en la base de datos
    public static final int CONTRASEÑA_INCORRECTA = 3; // ID existe, pero la contraseña es incorrecta
    public static final int ERROR_GENERAL = 4; // Otros errores (conexión, etc.)

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
    
    public static boolean esSoloNumeros(String str) {
    return str.matches("\\d+"); // Solo números del 0-9
    }
    
       // Método para insertar un usuario en la base de datos
    public static String insertarUsuario(String id, String usuario, String contraseña) {
        Connection conexion = null;
        PreparedStatement ps = null;

        try {
            // Conectar a la base de datos
            conexion = conectar();
            if (conexion == null) {
                System.err.println("No se pudo establecer la conexión.");
                return "";
            }

            // Consulta SQL para insertar un usuario
            String query = "INSERT INTO usuarios (id, usuario,password) VALUES (?, ?, ?)";
            ps = conexion.prepareStatement(query);
            ps.setString(1, id);
            ps.setString(2, usuario);
            ps.setString(3, contraseña);

            // Ejecutar la consulta
            String filasAfectadas = ""+ps.executeUpdate();
            return filasAfectadas; // Retorna las filas intercambiadas

        } catch (SQLException e) {
            // Verificar si el error es por duplicación de cédula
        if (e.getSQLState().equals("23505")) { // Código de error para violación de restricción única
            return "23505";
        } else {
            System.err.println("Error al insertar el usuario: " + e.getMessage());
            return "";
        }
      
        } finally {
            // Cerrar la conexión y el PreparedStatement
            try {
                if (ps != null) ps.close();
                if (conexion != null) conexion.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
    
// Metodo para la verificacion de usuario
    public static int verificarCredenciales(String id, String contraseña) {
    Connection conexion = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        // Conectar a la base de datos
        conexion = conectar();
        if (conexion == null) {
            System.err.println("No se pudo establecer la conexión.");
            return ERROR_GENERAL;
        }

        // Consulta SQL para obtener la contraseña hasheada del usuario
        String query = "SELECT password FROM usuarios WHERE id = ?";
        ps = conexion.prepareStatement(query);
        ps.setString(1, id);

        // Ejecutar la consulta
        rs = ps.executeQuery();

        // Verificar si se encontró un usuario con ese ID
        if (rs.next()) {
            String contraseña1 = rs.getString("password");
            // Verificar si la contraseña coincide con el hash almacenado
            if (contraseña.equals(contraseña1)) {
                return CREDENCIALES_VALIDAS; // Credenciales correctas
            } else {
                return CONTRASEÑA_INCORRECTA; // Contraseña incorrecta
            }
        } else {
            return ID_NO_ENCONTRADO; // ID no encontrado
        }

    } catch (SQLException e) {
        System.err.println("Error al verificar las credenciales: " + e.getMessage());
        e.printStackTrace();
        return ERROR_GENERAL; // Error general
    } finally {
        // Cerrar la conexión y los recursos
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conexion != null) conexion.close();
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}

// Main para probar la conexion
    public static void main(String[] args) {
        conectar();
    }
    
}
