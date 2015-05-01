/**
 *
 * @author Edulynch
 * Descargado de:
 * http://programando.cf/wordpress/
 * Mysql Java Conectar a la Base de Datos
 */

//importamos todas las librerias referentes a sql.
import java.sql.*;
import javax.swing.JOptionPane;
 
public class Conector {
 
    static String db = "javadb"; //Nombre de la base de datos.
    static String user = "root"; //Usuario de la base de datos.
    static String pass = ""; //Contraseña, si tuviesemos una.
    static String url = "jdbc:mysql://127.0.0.1/" + db;
 
    public static Connection GetConnection()
    {
        Connection conexion=null;
     
        try
        {
            //Iniciamos el Driver de MySQL
            Class.forName("com.mysql.jdbc.Driver");
            //Pasamos los Parametros, previamente configurados.
            conexion= DriverManager.getConnection(url,user,pass);
        }
        catch(ClassNotFoundException ex)
        {
            JOptionPane.showMessageDialog(null, ex, "Error1 en la Conexión con la BD "+ex.getMessage(), 
                    JOptionPane.ERROR_MESSAGE);
            conexion=null;
        }
        catch(SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex, "Error2 en la Conexión con la BD "+ex.getMessage(), 
                    JOptionPane.ERROR_MESSAGE);
            conexion=null;
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, ex, "Error3 en la Conexión con la BD "+ex.getMessage(), 
                    JOptionPane.ERROR_MESSAGE);
            conexion=null;
        }
        //lo que hace esta linea, es que apesar de todo, se ejecute igual. Es decir siempre nos va a retornar una conexion.
        finally
        {
            return conexion;
        }
    }
}