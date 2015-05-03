
/**
 *
 * @author Edulynch Descargado de: http://programando.cf/wordpress/ MySQL Java
 * Insertar, ModificarPuntos, Eliminar, Mostrar.
 */

//importamos todas las librerias a utilizar.
import java.sql.*;
import java.util.ArrayList;
import javax.swing.*;
import java.util.Calendar;
import javax.swing.table.DefaultTableModel;

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
               JOptionPane.showMessageDialog(null, "Registrado Correctamente...", "Registro:", JOptionPane.INFORMATION_MESSAGE);
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
    //apellido, email, etc.
    void limpiaTabla(JTable tabla){
        try{
            DefaultTableModel temp = (DefaultTableModel) tabla.getModel();
            int a =temp.getRowCount();
            for(int i=0; i<a; i++)
                temp.removeRow(0);
        }catch(Exception e){
            System.out.println(e);
        }
    }
    

    public void eliminar(JTable tabla){
        try {
            Connection miConexion = (Connection) Conector.GetConnection();
            //Lo que hace esta consulta es eliminar al usuario con el id = x que se encuentra 
            //en la tabla users.
            
            String query = "delete from users where id = ?";
            
            PreparedStatement preparedStmt = miConexion.prepareStatement(query);
            int x = Integer.parseInt(tabla.getValueAt(tabla.getSelectedRow(),0)+"");
            preparedStmt.setInt(1, x);
            
            preparedStmt.execute();
            
            miConexion.close();
               JOptionPane.showMessageDialog(null, "Eliminado Correctamente...", "Registro:", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla", "Error:", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void mostrar(JTable tabla) {
        try {
            DefaultTableModel dtm;
            Connection miConexion = (Connection) Conector.GetConnection();
            //Seleccionamos todos los datos de la tabla users
            String query = "SELECT * FROM users";
            //Statement se usa para hace una consulta en la base de datos, y obtener los resultados.
            Statement st = miConexion.createStatement();

            //ResultSet es un puntero a -1 por asi decirlo, es decir apunta antes de los datos.
            ResultSet rs = st.executeQuery(query);

            //Como ResultSet es un puntero anterior a la existencia de datos.
            
            ResultSetMetaData rsm = rs.getMetaData();
            
            //Creamos un ArrayList de objetos para luego mostrarlos.
            ArrayList<Object[]> datos = new ArrayList<>();
            //Por eso se usa el metodo next() que lo hace avanzar a la posicion del primer dato :P
            //basicamente rs.next() seria algo como rs.siguiente();
            while (rs.next()) //Recorremos la cantidad de registros.
            {
                Object[] filas = new Object[rsm.getColumnCount()];
                for(int i=0;i< filas.length;i++){
            //Como rs apunta a la posicion anterior a los datos, por eso le ponemos +1, para que comience en 1;
                    filas[i] = rs.getObject(i+1);
                }
                datos.add(filas);
            }
            dtm = (DefaultTableModel) tabla.getModel();
            for(int i=0;i<datos.size();i++){
            dtm.addRow(datos.get(i));
            }
            
        } catch (Exception ex) {
            System.out.println("Error: " + ex.toString());
        }
    }

}