/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto_sin;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author Melany
 */
public class Esteganografia_clase {
    //La variabla var "var" nos sirve como un identificador para saber is la imagen tiene algun mensaje oculto o no
    private String var="jc";
        /* Almacenara el tamaño del mensaje mas el tamaño de la firma (2) mas su propio tamaño (2) */

    public int tamaño=0;
    //Se crea una imagen con un bufer accesible de valor nulo inicialmente
    public BufferedImage foto=null;
    ///Inicializamoslas variable para el manejo de los pixeles 
    public int R,G,B;
    //Tenemos la variable de tipo color la cuual nos permitira.... 
    public Color color;
    //Una variable para almacenar el texto en binario
    public String texto_binario;
    //Una variable para almacenar el textooriginal sin modificaciones
    public String texto_original;
    public int cont = 0;
    
    
    
    /////CONVERSIONES
    //Este procedimiento nos permite convertir un caracter a binario,tenemos la variable "byteDeCaracter" la cual almacena un byte de tipo caracter
    // y tambien se crea una variable "binario" de tipo cadena,la cual retornara el valor en binario ,con la ayuda de un contador lo que se hace es 
   // crear una variable que inicia en 7 y el cicloo debe repetirse hasta que la avreiable "i" sea mayor o igual que cero ,dentro del ciclo for lo que se realiza es 
   // el almacenamiento de la cadena binaria y lo que es el byte desplazandose ahacia la izquierda la "i" veces de acuerdo a eso se retornaran los valores de 1 y 0
    private String aBinario(byte caracter){
        byte byteDeCaracter = (byte)caracter;
        String bin="";
        for( int i = 7; i>=0; i--){
           bin = bin + ( ( ( byteDeCaracter & ( 1<<i ) ) > 0 ) ? "1" : "0" ) ;
        }
         return bin;
    }

    /* convierte un valor binario a caracter */
    ///Para este procedimiento lo que se hizo fue crear una variable de tipo entero la cual toma como parametros la variable binario,definida anteriormente
    //y el tipo de sistema binario,posteriormente se crea una variable de tipo String que sera un caracter y  convierte el valor binario,finalmente se retorna 
 //                   el valor en caracter
    private String aCaracter(String bin){
        int i = Integer.parseInt(bin ,2);
        String Char = new Character((char)i).toString();
        return Char;        
    }
//Para la conversion a entero inicialmente se crea una variable ,la cual toma como parametro el tipo de sistema binario y asic onvertirlo a entero
    private int aEntero(String binario){
        int i = Integer.parseInt(binario ,2);        
        return i;
    }


    /* Dado el mensaje (String) que se quiere ocultar, lo une a la firma
       como a la longitud total del mensaje */
    /*Inicialmente se crea una variable denominda "binario",la cual almacenara el tamaño o longitud del texto pero en binario
    ,tenbemos la variable tamaño,descrita anteriormente para almacenar la longitud del mensaje  */
    public void Tamaño_TextoBin(String mensaje){
        String binario="";
        //se lamacena la longituf total del mensaje
        tamaño = mensaje.length() + var.length() * 2;
        //se transforma el valor de la longitud entera del mensjae y lo convierte la longitud en un valor BINARIO
        for( int i = 15; i>=0; i--){
           binario = binario + ( ( ( tamaño & ( 1<<i ) ) > 0 ) ? "1" : "0" ) ;
        }
        /*Se concatena el identificador llamando a el procedimiento que nos permite descomponer el mensaje
        a su equivalente en binario y tambien se concatena el mensaje descomponiendolo  su equivalente en  y se agrega 
        lo que es la variable "binario" la cual tiene la conversion en binario */
        texto_binario = DescompTexto(var) + binario + DescompTexto(mensaje);
        //Podemos visualizar el mensaje en binaario por la consola
        System.out.print("El mensaje en binario es :" + texto_binario);
    }

    /* Este procedimiento permite ocultar el mensaje en la imagen*/
    public void OcultarTexto(BufferedImage b,String mensaje){
        int cont2=0;
        //Se llama a el procedimiento "Tamaño_TextoBin" el cual como se hasbia explicado realiza el almacenamiento del mensaje en binario juntamente con el identificadopr 
        //para saber si una imagen tiene exto oculto o no
        Tamaño_TextoBin(mensaje);
        //Se crea una imagen con la que se trabajara utilizando la clase de acceso a dartos "BufferImage" tomando como atributos el ancho,alto y el tipo de datos de la imagen
      
        foto = new BufferedImage(b.getWidth(), b.getHeight(), BufferedImage.TYPE_INT_RGB);
        //Este ciclo For recorre toda la imagen pixel a pixel añadiendo 1 y 0 en los bits menos significativos
        for(int fila=0;fila<foto.getHeight();fila++){
          for(int columna=0;columna<foto.getWidth();columna++){
                //Esta variable obtiene el color del pixel en coordenadas tanto en  filas como columnas(i,j)
                color = new Color( b.getRGB(columna, fila) );
                //L siguiente sentencia valida la existencia de un mensaje ,es deci que mientras exista un mensaje, 
                //se procede a reemplazar los bits menos significativos
                if(cont2<=this.texto_binario.length()){
                    //Se tieen variables de tipo cadena las cuales representan a los colores RGB 
                    //estos se convierten a su equivalente en binario
                    String rojo = aBinario( (byte) color.getRed() );
                    String verde = aBinario( (byte) color.getGreen() );
                    String azul = aBinario( (byte) color.getBlue() );
                    //se reemplaza el ultimo bit de cada color RGB 
                    rojo = ReemplazarLSB(rojo);
                    verde = ReemplazarLSB(verde);
                    azul = ReemplazarLSB(azul);
                    //cambia de binario a entero
                    //Se almacena y se realiza la conversion de binario a entero 
                    R = Integer.parseInt(rojo ,2);
                    G = Integer.parseInt(verde ,2);
                    B = Integer.parseInt(azul ,2);
                }else{
                    //Si la sentencia no se cumple entonces solamente se obtienen los colores RGB en las variables 
                   R = color.getRed();
                   G = color.getGreen();
                   B = color.getBlue();
                }
                //Se coloca en la nueva imagen con los valores en blanco y negro
                foto.setRGB(columna, fila, new Color(R,G,B).getRGB());
                cont2+=3;
          }
        }
    }

    
    
   //Se verificara la existencia de el ientificador en la image
    //Es asi que se leen los primeros 6 pixeles para formar los bits necesarios para "jc" y se devuelve TRUE/FALSE
   private boolean VerificarVar(BufferedImage b){
       boolean existencia=false;
       String p = "";
       ///Este ciclo for nos indica hasta que pixel se debe recorrer y asi fromar los bits nbecesarios
        for(int j=0;j<6;j++){
            
            //Se crea un objeto de tipo Color,en el cual se obtiene el color RGB de la imagen 
            color = new Color(b.getRGB(j, 0));
            //Y seconvierte cada color RGB a binario 
            String rojo = aBinario( (byte) color.getRed() );
            String verde = aBinario( (byte) color.getGreen() );
            String azul = aBinario( (byte) color.getBlue() );
            ///Y se obtiene el bit menos signifactivo de cada color RGB
            rojo = getLSB(rojo);
            verde = getLSB(verde);
            azul = getLSB(azul);
            p = p + rojo + verde + azul;
        }  
//Esta sentencia valida la existencia de el identificado convirtiendo el vlaor binario a caracte,tomando en cuenta el valor de la variable "p"
//hasta que se haya llegado a los 6 primeros pixeles ,si esta sentencia  es valida la existencia de texto en la imagen es verdadero
        if( aCaracter(p.substring(0, 8)).equals("j") &&  aCaracter(p.substring(8, 16)).equals("c") ){
            existencia=true;
        }
       return existencia;
   }
       /* Se reconstruye el mensaje del array binario a un String */
    private String aCadena(String[] mensaje){
        String cad ="";
        //Se lee a partir del 5 elemento ya que los cuatro primeros pertenecen a el identificador y la longitud del mensaje
        for(int i=4; i<mensaje.length;i++){
            cad = cad + aCaracter(mensaje[i]) ;
        }
        return cad;
    }
    

   /* pues que te digo extrae la parte que corresponde al tamaño total del mensaje */
   private void VerificarTamañoTexto(BufferedImage b){
        String p = "";
        for(int j=5;j<12;j++){
            color = new Color(b.getRGB(j,0));
            String rojo = aBinario( (byte) color.getRed() );
            String verde = aBinario( (byte) color.getGreen() );
            String azul = aBinario( (byte) color.getBlue() );
            rojo = getLSB(rojo);
            verde = getLSB(verde);
            azul = getLSB(azul);
            p = p + rojo + verde + azul;
        }
        this.tamaño = aEntero(p.substring(1, 17));        
      }

      /*Se extrae los bits de la imagen y forma de nuevo el mensaje oculto */
      public String ExtraerBits(BufferedImage b){
        //Se valida que la imagen tenga el identificador de existencia del mensaje 
        texto_original="No existe ningún mensaje oculto";
        if( VerificarVar(b) ){//Si existe el identificador ,entonces se procede llamar a la funcionpara 
            //verificar el tamaño del texto 
            VerificarTamañoTexto(b);
            //Se almacena la longitud del tamaño en un  String
            String[] s = new String[this.tamaño];
            String txt_tmp="";
            //recorre toda la imagen pixel x pixel(filas columnas)
            for(int fila=0;fila<b.getHeight();fila++){
                for(int columna=0;columna<b.getWidth();columna++){
                    //Se obtiene el color del pixel en las coordenadas defilas y columnas (i,j)
                    color = new Color(b.getRGB(columna, fila));
                    //Los colores RGB se convierten a su equivalente en binario
                    String rojo = aBinario( (byte) color.getRed() );
                    String verde = aBinario( (byte) color.getGreen() );
                    String azul = aBinario( (byte) color.getBlue() );
                    //Se obtiene el bit menos signifactivo de cada color
                    rojo = getLSB(rojo);
                    verde = getLSB(verde);
                    azul = getLSB(azul);
                    //cuando se termino de leer todo el mensaje se sale
                    if(txt_tmp.length()<=(this.tamaño*8)){
                        txt_tmp = txt_tmp + rojo + verde + azul;
                    }else{
                        break;
                    }
                }
            }
            //el String obtenido de 1 y 0, lo va separando en un array de bytes
            int cont_tmp =0;
            for(int a=0; a<(this.tamaño*8);a = a + 8){
                s[cont_tmp]=txt_tmp.substring(a, a + 8);                
                cont_tmp++;
            }
            //llama a un procedimiento privado para reconstruir el mensaje
            texto_original = aCadena(s);
        }//fin si
        return texto_original;
    }

    public BufferedImage getFoto(){
        return this.foto;
    }


    /*Este procedimiento descompone el mensaje en una variable de tipo String con su equivalente en binario*/
    private String DescompTexto(String mensaje){
        String mb = "";
        char[] mensaje_tmp = mensaje.toCharArray();
        for(int i=0; i<mensaje_tmp.length;i++){
            mb = mb + aBinario( (byte) mensaje_tmp[i]);
        }
        return mb;
    }

    /* Se reemplaza el bit menos significativo con un bit del mensaje */
    private String  ReemplazarLSB(String colorRGB){
        /*Esta utiliza la variable 'cont'que tiene como valor 0 ,se tiene la sentencia en la cual el valor del
        contador debe ser menor a la longitud del texto en binario,mientras esa sentencia se cumpla lo que se hace es
                almacernar en una variable el subconjunto de 8 bits y el texto binario*/
        if(cont < texto_binario.length()){
            colorRGB = colorRGB.substring(0,7) + texto_binario.substring(cont, cont+1);
            cont++;    
        }
        return colorRGB;
    }
//Obtiene el bit menos signifcativo
    private String getLSB(String binario){
        return binario.substring(7, 8);
    }


}
