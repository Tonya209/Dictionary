
package dictionary;


import java.util.Scanner;

/**
 *
 * @author Antonina Hales
 */
public class Aplicacion {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Controlador c = new Controlador();
       c.crearTablas();
        
        //Menu
        int opcion=0;
        do{
           
                System.out.println("Menu:");
                System.out.println("1. Crear palabra y traducciones");
                System.out.println("2. Eliminar palabra y sus traducciones");
                System.out.println("3. Consultar todas las palabras");
                System.out.println("4. Consultar palabras con filtro");
                System.out.println("5. Salir");
                System.out.println("Elige una opcion: ");
        
          try{
            Scanner sc= new Scanner(System.in); //recibimos una eleccion
            opcion = Integer.parseInt(sc.nextLine());
        
        switch(opcion){
            case 1:c.addPalabra(); //añadir palabra
                break;
            case 2: c.eliminarPalabra(); //eliminar la palabra
                break;
            case 3: c.consultarPalabras();// mostrar las palabras en diccionario
                break;
            case 4: c.consultarPalabrasFiltro(); //mostrar las palabras que contienen el filtro
                break;   
            case 5:
                System.out.println("Hasta luego");
                break;
            default: System.out.println("Intrduzca una opcion de 1 a 5");    
        }
             }catch(NumberFormatException ex){
                System.out.println("Introduzca una opcion valida");
            }
            
        } while(opcion!=5);
    
    }
}