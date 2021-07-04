package dictionary;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 *
 * @author Antonina Hales
 */
public class Palabra {
    //declaramos las variables
    String palabra;
    int consultado;
    HashMap<String,String> traducciones= new HashMap<>();
    
    //consructor sin argumentos
    public Palabra(){}
    
    /**
     * El constructor
     * @param palabra
     * @param consultado
     * @param traducciones
     */
    public Palabra(String palabra, int consultado, HashMap<String,String> traducciones){
        this.palabra=palabra;
        this.consultado=consultado;
        this.traducciones=traducciones;
    }
    
    //metodos getters y setters
    public String getPalabra() {
        return palabra;
    }

    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }

    public int getConsultado() {
        return consultado;
    }

    public void setConsultado(int consultado) {
        this.consultado = consultado;
    }

    public HashMap<String, String> getTraducciones() {
        return traducciones;
    }

    public void setTraducciones(HashMap<String, String> traducciones) {
        this.traducciones = traducciones;
    }
    
    /**
     * El metodo que muestra la informacion sobre la palabra
     * @return String devuelve la palabra, el numero de veces que la palabra ha sido consultada, 
     * idiomas y traducciones
     */
 
    @Override
    public String toString(){
        
        String t ="\n";
        Iterator<Map.Entry<String, String>> iterator = this.traducciones.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            t += "IDIOMA: " + entry.getKey() + " TRADUCCION: " + entry.getValue() + "\n";
        }

        return "Palabra: " + this.palabra + " Consultado: " + this.consultado + "\nTraducciones: " + t;
        
    }
}
