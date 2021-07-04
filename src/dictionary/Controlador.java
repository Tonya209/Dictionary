package dictionary;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Antonina Hales
 */
public class Controlador {

    ArrayList<Palabra> diccionario = new ArrayList<>();
    Connection conn;

    
    /**
     * El constructor donde creamos la connecion la base de datos
     */
    public Controlador() {

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException ex) {
            System.out.println("Error" + ex);
        }
        try {
            conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "sys as sysdba", "Tonya2904");
         //si conectamos a la base de datos, creamos las tablas
            if (conn != null) {
                System.out.println("Connected to the database!");
                crearTablas();
               diccionario = cargarDiccionario();
            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (SQLException ex) {
            System.out.println("Error" + ex);
        }
    }
    
   
    /**
     * Este metodo permite crear las tablas
     * 
     */
        public void crearTablas(){
       
        String sql1 ="CREATE TABLE Palabras(palabra VARCHAR2(30) PRIMARY KEY, consultado NUMBER)";
        String sql2 ="CREATE TABLE Traducciones(palabra VARCHAR2(30), idioma VARCHAR2(30),"
                + "traduccion VARCHAR2(30), CONSTRAINT FK_palabras FOREIGN KEY(palabra) REFERENCES "
                + "palabras(palabra), CONSTRAINT PK_traducciones PRIMARY KEY(palabra, idioma))";
        
        Statement stmt;
         try { 
            stmt = conn.createStatement();//enviamos Statement a bases de datos
              stmt.executeUpdate(sql1);
        } catch (SQLException ex) {
           //System.out.println("Tabla existente");
        }
        
         try{
             stmt=conn.createStatement(); // enviamos Statement a bases de datos
             stmt.executeUpdate(sql2);
         }catch(SQLException ex){
          //System.out.println("Tabla existente");
         }
        }
        

   
    /**
     * Este metodo permite añadir palabras en la tabla
     * @return boolean Devuelve true si la palabra ha sido añadida corerctamente,
     * en el caso contrario devuelve false
     */
    public boolean addPalabra() {
        System.out.println("Introduce la palabra a añadir:");
        Scanner sc = new Scanner(System.in);
        String palabra = sc.nextLine();//recibimos la palabra del usuario
        for (Palabra p : diccionario) {//comprobamos si esta palabra ya existe en el diccionario
            if (p.getPalabra().equalsIgnoreCase(palabra)) {
                System.out.println("La palabra que quiere insertar ya existe en la base de datos");
                return false;
            }
        }
        HashMap<String, String> traducciones = new HashMap<>();
        String idioma = null;
        do {
            System.out.println("Introduzca la idioma (escribe 'Salir' para no añadir mas): ");
            idioma = sc.nextLine();//recibimos la idioma
            if (!idioma.equalsIgnoreCase("Salir")) {
                System.out.println("Introduzca la traduccion: ");
                String traduccion = sc.nextLine();// recibimos la traduccion
                traducciones.put(idioma, traduccion);
            }
        } while (!idioma.equalsIgnoreCase("Salir"));//si el usuario escribe Salir,el buscle se termina

        String query = "Insert into palabras values('" + palabra + "', 0)";

        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);//enviamos el comando a bases de datos
            for (Map.Entry<String, String> entry : traducciones.entrySet()) {
                query = "Insert into traducciones values('" + palabra + "','" + entry.getKey() + "', '" + entry.getValue() + "')";
                stmt = conn.createStatement();
                stmt.executeUpdate(query);
            }
           //confirmamos que hemos añadido la palabra y las traducciones
            System.out.println("La palabra y las traducciones han sido añadido correctamente en bases de datos");
            diccionario = cargarDiccionario();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    
    /**
     * Este metodo permite cargar el diccionario en el ArrayList
     * @return ArrayList Devuelve ArrayList que contiene el diccionario
     */
    public ArrayList<Palabra> cargarDiccionario() {
        ArrayList<Palabra> diccionario = new ArrayList<>();
        String sql = "SELECT P.PALABRA AS PALABRA, CONSULTADO, IDIOMA, TRADUCCION FROM PALABRAS P,TRADUCCIONES T WHERE T.PALABRA =P.PALABRA ";

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            String termino = "";
            Palabra palabra = new Palabra();

            while (rs.next()) {
                if (termino.equalsIgnoreCase(rs.getString("PALABRA"))) {
                    HashMap<String, String> traducciones = palabra.getTraducciones();
                    traducciones.put(rs.getString("IDIOMA"), rs.getString("TRADUCCION"));
                    palabra.setTraducciones(traducciones);
                } else {
                    if (palabra.getPalabra() != null) {
                        diccionario.add(palabra);
                    }

                    palabra = new Palabra();
                    termino = rs.getString("PALABRA");
                    palabra.setPalabra(termino);
                    palabra.setConsultado(rs.getInt("CONSULTADO"));
                    HashMap<String, String> traducciones = new HashMap<>();
                    traducciones.put(rs.getString("IDIOMA"), rs.getString("TRADUCCION"));
                    palabra.setTraducciones(traducciones);
                }
            }

            if (palabra.getPalabra() != null) {
                diccionario.add(palabra);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return diccionario;
    }

    
    
    /**
     * Este metodo permite consultar palabras creadas
     */
    public void consultarPalabras() {

        ArrayList<Palabra> palabras = cargarDiccionario();
        for (Palabra p : palabras) { //recorremos el ArrayList y mostramos las palabras
            System.out.println(p);

        }
        actualizarConsultado("");

    }

    
    /**
     * Este metodo permite consultar palabras usando el filtro
     */
    public void consultarPalabrasFiltro() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduzca el filtro: ");
        String filtro = sc.nextLine(); //recibimos el filtro

        ArrayList<Palabra> palabras = cargarDiccionario();
        for (Palabra p : palabras) {
            if (p.getPalabra().contains(filtro)) { //mostramos las palabras que contiene el filtro
                System.out.println(p);
            }
        }
        actualizarConsultado(filtro); //cambiamos el numero de veces la palabra ha sido consultada
    }

    
    
    /**
     * Este metodo permite eleminar una palabra del diccionario
     * @return boolean Devuelve true si la palabra y las traducciones han sido eliminado,
     * devuelve false en el caso contrario
     */
    public boolean eliminarPalabra() {

        System.out.println("Introduzca la palabra a eliminar: ");

        Scanner sc = new Scanner(System.in);
        String palabra = sc.nextLine();
        for (Palabra p : diccionario) {
            if (p.getPalabra().equalsIgnoreCase(palabra)) {
                Statement stmt;
                try {
                    String sql = "DELETE FROM TRADUCCIONES WHERE palabra='" + palabra + "'";
                    stmt = conn.createStatement();
                    stmt.executeUpdate(sql);
                    sql = "DELETE FROM PALABRAS WHERE palabra='" + palabra + "'";
                    stmt = conn.createStatement();
                    stmt.executeUpdate(sql);
                    System.out.println("La palabra y las traducciones han sido borradas");
                    diccionario = cargarDiccionario();
                    return true;
                } catch (SQLException ex) {
                    Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        System.out.println("La palabra especificada no existe");
        return false;
    }

    
    
    /**
     * Este metodo permite cambiar el numero de veces la palabra ha sido consultada
     * @param filtro
     */
    public void actualizarConsultado(String filtro) {
        for (Palabra p : diccionario) {
            if (filtro.equalsIgnoreCase("")) {
                String sql = "UPDATE PALABRAS SET Consultado = Consultado+1 WHERE palabra = '" + p.getPalabra() + "'";
               // System.out.println(sql);
                Statement stmt;
                try {
                    stmt = conn.createStatement();
                    stmt.executeUpdate(sql);
                } catch (SQLException ex) {
                    Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                if (p.getPalabra().contains(filtro)) {
                    String sql = "UPDATE PALABRAS SET Consultado = Consultado+1 WHERE palabra = '" + p.getPalabra() + "'";
                   // System.out.println(sql);
                    Statement stmt;
                    try {
                        stmt = conn.createStatement();
                        stmt.executeUpdate(sql);
                    } catch (SQLException ex) {
                        Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        }
    }
}
