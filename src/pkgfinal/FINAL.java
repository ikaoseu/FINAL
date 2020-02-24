/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgfinal;

//clases necesarias para la conexion SAX.
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

//clases necesarias para acceder a la base de datos desde JAVA.
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;


//clases necesarias para acceso DOM.
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.FileOutputStream;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

/**
 *
 * @author Manu
 */
public class FINAL {

   
    public static void main(String[] args) throws SQLException{
            FINAL it = new FINAL();  
            
            
        Scanner sc = new Scanner(System.in);
        System.out.println("indique si quiere acceder al DTD (1), o a la base de datos(2): \n");
        int menu = sc.nextInt();
        
        if(menu==1){
            Scanner sc2 = new Scanner(System.in);
            System.out.println("indique ahora si quiere acceder al DTD desde DOM(1), o desde SAX(2): \n");
            int menu2 = sc2.nextInt();
                 if(menu2==1){
                        try{
                            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                            DocumentBuilder builder = dbf.newDocumentBuilder();
                            File f = new File("alumnos.xml");
                            Document documento = builder.parse(f); 

                            //inicializamos la variable que vamos a ir modificando manualmente para
                            //indicar al sistema qué vamos a querer realizar.
                            Scanner sc_dom = new Scanner(System.in);
                            System.out.println("pulse 0 si quiere recorrer el archivo, 1 si lo quiere hacer de manera secuencial y 2 si quiere modificar un archivo: \n");
                            int var1= sc_dom.nextInt();
                            
                            
                            if (var1== 0){
                            //procedimiento que muestra todo el documento
                                recorrerRamaDom(documento);

                                }else if (var1==1){
                                //procedimiento que muestra los datos de un alumno en concreto
                                   recorrerRamaDomSecuencial(documento, "74857485F");

                                    }else if(var1==2){
                                    //procedimiento que modifica un DNI
                                        Scanner sc_dom_dni1 = new Scanner(System.in);
                                        Scanner sc_dom_dni2 = new Scanner(System.in);
                                        System.out.println("escriba ahora el dni a modificar y acto seguido el nuevo: \n");
                                        String dni1= sc_dom_dni1.nextLine();
                                        String dni2= sc_dom_dni2.nextLine();
                                        
                                        modificarDNI(documento, dni1, dni2);
                            }
                        }catch(Exception e){
                             e.printStackTrace();
                        }

                    }else if(menu2==2){
                          
                        try {
                            //Creamos el manejador SAX
                            SAXParserFactory factory = SAXParserFactory.newInstance();
                            SAXParser saxParser = factory.newSAXParser();
                            DefaultHandler handler = new DefaultHandler() {
                                
                                //cuando se llega al principio de un elemento imprimimos su nombre por pantalla
                                public void startElement(String uri, String localName, String qName, Attributes attributes)throws SAXException {
                                System.out.println("Start Element :" + qName);
                                }

                                //cuando se llega al final del elemento lo indicamos por pantalla
                                public void endElement(String uri, String localName,String qName)throws SAXException {
                                System.out.println("End Element :" + qName);
                                }

                                //cuando leemos el contenido de un elemento lo mostramos por pantalla
                                public void characters(char ch[], int start, int length)throws SAXException {
                                System.out.println(new String(ch, start, length));
                                }
                            };
                            
                            //Encapsulamos el fichero xml a leer indicando que tiene formato utf-8
                            File file = new File("alumnos.xml");
                            InputStream inputStream= new FileInputStream(file);
                            Reader reader = new InputStreamReader(inputStream,"UTF-8");
                            InputSource is = new InputSource(reader);
                            is.setEncoding("UTF-8");
                            saxParser.parse(is, handler);
                        } catch (Exception e) {
                                e.printStackTrace();
                        }
 
                    }
        }else if(menu==2){
            try{
                    Class.forName("com.mysql.jdbc.Driver");
                    Scanner sc_BDD = new Scanner(System.in);
                    System.out.println("indique si quiere leer(1), añadir usuario(2), dar de baja(3): \n");
                    int menu3 = sc_BDD.nextInt();


                    if(menu3 == 1){
                        System.out.println("usted ha seleccionado leer \n");

                        it.leeDatos();
                    }else{
                        if(menu3 == 2){

                             System.out.println("usted ha seleccionado añadir usuario\n");
                             System.out.println("escriba ahora los valores en el siguiente orden, Dni, nombre, correo y telefono: \n");     

                             Scanner sc_d = new Scanner(System.in);
                             Scanner sc_n = new Scanner(System.in);
                             Scanner sc_c = new Scanner(System.in);
                             Scanner sc_t = new Scanner(System.in);


                             String _dni = sc_d.nextLine();
                             String _nombre = sc_n.nextLine();
                             String _correo = sc_c.nextLine();
                             String _tlf = sc_t.nextLine();


                             it.grabaRegistro(_dni,_nombre,_correo,_tlf);
                       

                        }else{ if(menu3 == 3){
                             System.out.println("usted ha seleccionado borrar registro \n");
                             System.out.println("escriba ahora su dni: \n");

                             Scanner borrador = new Scanner(System.in);
                             String usuario = borrador.nextLine();

                             it.borraDatos(usuario);
                        }
                        }
                        
                    }
                         }catch(Exception e){
                                 e.printStackTrace();
                                }
                    }
    }
    
    //DOM.
    
    public static void recorrerRamaDom(Node nodo){
        //recibimos el nodo y evaluemos si está vacío
        if (nodo!=null){
        //mostramos su nombre y su valor por pantalla
            System.out.println(nodo.getNodeName()+": "+ nodo.getNodeValue());
            //generamos una lista de nodos hijos a partir del nodo actual
            NodeList hijos = nodo.getChildNodes();
            //generamos un bucle que recorra los nodos hijos de la lista
        for (int i=0;i < hijos.getLength();i++){
        /*declaramos una variable de tipo nodo y le asignamos el nodo actual de
            la lista*/
             Node nodoNieto = hijos.item(i);
             /*volvemos a llamar de manera recursiva a este mismo procedimeinto,
            así hasta que ya no haya más nodos hijos*/
             recorrerRamaDom(nodoNieto);
             }
        }
 }
    
    public static void recorrerRamaDomSecuencial(Document miDocumento, String dni){
    try {
        boolean encontrado= false;
        //Generamos una lista que contenga todos los nodos cuyo nombre es DNI
            NodeList list = miDocumento.getElementsByTagName("dni");
        int i=0;
        //recorremos la lista para buscar el DNI
        while(i<list.getLength()) {
                     Node n = list.item(i);
                 //evaluamos si el nodo actual de la lista su valor es el dni
                if (n.getFirstChild().getNodeValue().equals(dni)){
                          //nos posicionamos en el nodo padre, que es el nodo alumno
                        Node Padre= n.getParentNode();
                        /*llamamos al procedimiento que muestra por pantalla todos los
                       nodos hijos junto a sus valores*/
                        recorrerRamaDom(Padre);
                        //ponemos la variable de control a verdadero
                        encontrado = true;
                break;
                }
                  i++;
                  }
                if (encontrado==false){
                    System.out.println("DNI no se encuentra como alumno");
            }
        } catch (Exception e) {
                e.printStackTrace();
        }
 }

    public static void modificarDNI(Document miDocumento, String dni, String dniNuevo){
 try {
        boolean encontrado= false;
        //Generamos una lista que contenga todos los nodos cuyo nombre es DNI
        NodeList list = miDocumento.getElementsByTagName("dni");
        int i=0;
        //recorremos la lista para buscar el DNI
        while(i<list.getLength()) {
        Node n = list.item(i);
        //evaluamos si el nodo actual de la lista su valor es el dni
        if (n.getFirstChild().getNodeValue().equals(dni)){
        //modificamos el valor del primer hijo, que es el tecto donde se

        n.getFirstChild().setNodeValue(dniNuevo);

        //nos posicionamos en el nodo padre, que es el nodo alumno
        Node Padre= n.getParentNode();
        /*llamamos al procedimiento que muestra por pantalla todos los
       nodos hijos junto a sus valores para comprobar que se ha cambiado el DNI*/
        recorrerRamaDom(Padre);
        //ponemos la variable de control a verdadero
        encontrado = true;
        guardarDOMcomoFILE(miDocumento);
 break;    
  }
                i++;
         }
                if (encontrado==false){
                    System.out.println("DNI no se encuentra como alumno");
                         }
            } catch (Exception e) {
                e.printStackTrace();
        }
 }
   
    public static void guardarDOMcomoFILE(Document doc){
        try{

             // Crea un fichero llamado salida.xml
            File archivo_xml = new File("alumnos.xml");
            // Especifica el formato de salida
            OutputFormat format = new OutputFormat(doc);

            // Especifica que la salida esté indentada
            format.setIndenting(true);

            // Escribe el contenido en el FILE
            XMLSerializer serializer = new XMLSerializer(new
            FileOutputStream(archivo_xml), format);
 
            serializer.serialize(doc);


        }
        catch (Exception e) {
             e.printStackTrace();
        }
 }
    
    //JAVABDD.
    
    public void leeDatos()throws SQLException{
        
          Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost/usuarios","root", "");
          Statement st = conexion.createStatement();

            
          ResultSet rs = st.executeQuery("SELECT * FROM USUARIOS");
           
          while (rs.next()){
              System.out.println("dni="+rs.getObject("dni")+
                       ",nombre=" + rs.getObject("nombre")+
                       ",correo="+ rs.getObject("correo")+
                       ",tlf="+ rs.getObject("tlf"));
           }
           rs.close();
    }
    
    public void grabaRegistro(String dni, String nombre, String correo, String tlf)throws SQLException{
        
           Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost/usuarios","root", "");
           Statement st = conexion.createStatement();
           st.executeUpdate("INSERT INTO usuarios (dni, nombre, correo, tlf) VALUES('"+dni+"', '"+nombre+"', '"+correo+"','"+tlf+"')");
           
    }
     
    public void borraDatos(String dni)throws SQLException{
        
             Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost/usuarios","root", "");
             Statement st = conexion.createStatement();
             st.executeUpdate("DELETE FROM `usuarios` WHERE dni='"+dni+"'");
    }
    
}
