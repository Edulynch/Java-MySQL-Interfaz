
/**
 *
 * @author Edulynch Descargado de: http://programando.cf/wordpress/ MySQL Java
 * Insertar, ModificarPuntos, Eliminar, Mostrar.
 */

//importamos todas las librerias a utilizar.
import java.sql.*;
import javax.swing.*;
import java.util.Calendar;

public class Metodos {

    //Metodo que nos servira para agregar Datos de Manera Eficiente.
    //El cual necesita solo 4 parametros, el nombre, apellido, (boolean)esAdmin, y el puntaje.

    public void insertar(String nom, String ape, boolean adm, int pun) {
        try {
            // Creamos la conexion apartir de la clase Conector
            Connection miConexion = (Connection) Conector.GetConnection();

            //Creamos un objeto fecha, para que la podamos insertar.
            // create a sql date object so we can use it in our INSERT statement
            Calendar calendar = Calendar.getInstance(); // Obtiene la zona horario por defecto.
            //java.sql.Date = toma solo la fecha, es decir que si hay horas, minutos, segundos, los elimina.
            //pero este necesita un tipo de dato long.
            //calendar.getTime() = obtiene la fecha en forma Fecha. y para transformarlo a long.
            //le agregamos otro .getTime(); por segunda vez que lo convierte en long.
            java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());

            //yo use la variable query para referirme a la consulta que se hará en la base de datos.
            //en esta ocasion, será insertar estos datos.
            //insert into = insertar dentro de
            //users = nombre de la tabla
            //values = Puede ser usado para enviar datos directamente o preparar un objeto con los datos 
            //para ser enviados, lo explico mejor mas abajo :P
            String query = " insert into users (first_name, last_name, date_created, is_admin, num_points)"
                    + " values (?, ?, ?, ?, ?)"; //El signo (?), hace referencia a que le pasaremos los
            //datos luegos, con un PreparedStatement.

            // Creamos el PreparedStatement con los datos a ingresar.
            //PreparedStatement = Es un objeto el cual podemos llenar todos nuestro.
            //prepareStatement = Se encarga de preparar la base de datos para lo que se viene, en este caso, 
            //nuestra consulta se refiere a insertar datos.
            //Imaginen que PreparedStatement es una carta y prepareStatement es un cartero.
            PreparedStatement preparedStmt = miConexion.prepareStatement(query);
            //El objeto preparedSmt del tipo PreparedStatement, tiene los metodos, de todos los tipo de datos.
            //setString(x, "texto") => la x se refiere a la posicion del query, en este caso first_name es 
            //el primer dato.
            //Puedes enviar un texto o una variable como yo lo hice.
            preparedStmt.setString(1, nom);
            //Igual que el anterior :D
            preparedStmt.setString(2, ape);
            //Aqui solo cambiamos el tipo de Dato a Date (Fecha).
            preparedStmt.setDate(3, startDate);
            //Booleanos :P
            preparedStmt.setBoolean(4, adm);
            //Enteros e.e
            preparedStmt.setInt(5, pun);

            //Ejecutamos la consulta en la base de datos. El cartero entrego la carta y nos trae la respuesta.
            //Si esta parte falla, el catch, se encarga de decirnos que paso :3
            preparedStmt.execute();
            //Despues de hacer la consulta, cerramos entre java y la base de datos.
            miConexion.close();
        } //Si hay un error en cualquier parte del try, nos mostrara cual es. En algun formato extraño...
        catch (Exception e) {
            System.err.println("Ocurrio un Error: " + e.getMessage());
        }
    }

    //Este metodo Modifica los puntso del usuario con id=x y los cambia por lo que mandemos.
    public void modificarPuntos(int id, int puntos) {
        try {
            Connection miConexion = (Connection) Conector.GetConnection();
            //Lo mas normal en una base de datos es que tenga un id cada usuario, pero si no lo hubiese,
            //podemos comparar el nombre,apellido,email, en fin lo que quieras, al momento de buscar.

            //Esta consulta lo que hace es actualizar los puntos del usuario "?" (Esto se usa para decirle
            //mas adelante), que se encuentra en la tabla users, con el id= que nosotros 
            //digamos en el metodo.
            String query = "update users set num_points = ? where id='" + id + "'";
            PreparedStatement preparedStmt = miConexion.prepareStatement(query);
            //Como solo enviaremos un dato, sera setInt(1, cantidadDePuntos)
            preparedStmt.setInt(1, puntos);

            preparedStmt.executeUpdate();

            miConexion.close();
        } catch (Exception e) {
            System.err.println("Dato no Modificado: Error: " + e.getMessage());
        }
    }
    //Este Metodo Elimna por id, recuerda que modificandolo un poco, podrias hacerlo por nombre,
    //apellido, emai, etc.

    public void eliminar(int id) {
        try {
            Connection miConexion = (Connection) Conector.GetConnection();
            //Lo que hace esta consulta es eliminar al usuario con el id = x que se encuentra 
            //en la tabla users.
            String query = "delete from users where id = ?";
            PreparedStatement preparedStmt = miConexion.prepareStatement(query);
            preparedStmt.setInt(1, id);

            preparedStmt.execute();

            miConexion.close();
        } catch (Exception e) {
            System.err.println("No se pudo Eliminar: Error: " + e.getMessage());
        }

    }

    public void mostrar() {
        try {
            Connection miConexion = (Connection) Conector.GetConnection();
            //Seleccionamos todos los datos de la tabla users
            String query = "SELECT * FROM users";
            //Statement se usa para hace una consulta en la base de datos, y obtener los resultados.
            Statement st = miConexion.createStatement();

            //ResultSet es un puntero a -1 por asi decirlo, es decir apunta antes de los datos.
            ResultSet rs = st.executeQuery(query);

            //Como ResultSet es un puntero anterior a la existencia de datos.
            //Por eso se usa el metodo next() que lo hace avanzar a la posicion del primer dato :P
            //basicamente rs.next() seria algo como rs.siguiente();
            while (rs.next()) //Recorremos la cantidad de registros.
            {
                //obtenemos el id... Recuerda que si eliminas un registro, el id, seguira avanzando, pero
                //al momento de obtener los datos, no tendras ningun error, es decir que si eliminas el 
                //registro 1 con todos sus datos, y agregar otro usuario, se registrara con id=2.
                int id = rs.getInt("id");
                //Obtenemos el nombre.
                String firstName = rs.getString("first_name");
                //Obtenemos el apellido.
                String lastName = rs.getString("last_name");
                //Obtenemos la fecha.
                Date dateCreated = rs.getDate("date_created");
                //Obtenemos un true/false si es admin o no.
                boolean isAdmin = rs.getBoolean("is_admin");
                //Obtenemos los puntos.
                int numPoints = rs.getInt("num_points");

                //Mostramos los resultados, le agregamos un salto de linea al final de cada registro.
                System.out.format("%s, %s, %s, %s, %s, %s\n", id, firstName, lastName, dateCreated, isAdmin,
                        numPoints);
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.toString());
        }
    }

}