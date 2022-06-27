/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto_sin;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
/**
 *
 * @author Melany
 */
public class config extends JPanel {
    private BufferedImage foto;
    //tamaño del contenedor
    private int ancho=0;
    private int alto=0;
    private BufferedImage Imagen_en_memoria;
//
    private JFileChooser selector_archivo = new JFileChooser();
    private FileNameExtensionFilter extension = new FileNameExtensionFilter("Archivo de Imagen","jpg","png","bmp");
    private File Directorio = selector_archivo.getCurrentDirectory();
    private String ruta_archivo = "";

    public config(){
        try {
            //se carga la imagen default - lienzo blanco
            foto = ImageIO.read(getClass().getResource("subir.png"));
            ancho = foto.getWidth();
            alto = foto.getHeight();
            this.setPreferredSize(new Dimension(ancho, alto));
            this.setSize(new Dimension(ancho, alto));
            this.setVisible(false);
            this.repaint();
        } catch (IOException ex) {            
        }
    }

    public Dimension getTamanio(){
        return new Dimension(ancho,alto);
    }

    //se sobreescribe el metodo paint
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        //se crea una imagen en memoria
        Imagen_en_memoria = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = Imagen_en_memoria.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //se dibuja las imagenes en el contenedor
        g2d.drawImage(foto,0,0,this);
        //se dibuja toda la imagen
        g2.drawImage(Imagen_en_memoria, 0, 0, this);
    }

    //
    public void setFoto(BufferedImage f){
        this.foto = f;
        ancho =f.getWidth();
        alto = f.getHeight();
        this.setSize(new Dimension(ancho, alto));
        this.repaint();
    }

    public BufferedImage getFoto(){
        return this.foto;
    }

    //muestra una ventana de dialgo para abrir un archivo de imagen
    public boolean Abrir_Imagen(){
       boolean ok=false;
       selector_archivo = new JFileChooser();
       selector_archivo.setFileFilter(extension);
       //fileChooser.setCurrentDirectory(new java.io.File("e:/"));
       selector_archivo.setCurrentDirectory( Directorio );
       int result = selector_archivo.showOpenDialog(null);
       if ( result == JFileChooser.APPROVE_OPTION ){
            try {                
                foto = ImageIO.read( selector_archivo.getSelectedFile() );
                ruta_archivo = selector_archivo.getSelectedFile().getPath();
                ancho =foto.getWidth();
                alto = foto.getHeight();
                this.setSize(new Dimension(ancho, alto));
                            this.setVisible(true);

                this.repaint();
                this.Directorio = selector_archivo.getCurrentDirectory();
                ok=true;
            } catch (IOException ex) {

            }
        }
       return ok;
    }

     //metodo que guarda la imagen en disco
    public void guardar_imagen( BufferedImage foto ){        
        try {
            String tmp_file = this.ruta_archivo.substring(0, this.ruta_archivo.length()-4) + "_copia.bmp";
            //se escribe en disco            
            ImageIO.write(foto, "bmp", new File( tmp_file ));
            //System.out.println( tmp_file );
            JOptionPane.showMessageDialog(null, "La imagen se guardo en:\n"
                    + "File: " + tmp_file);
	} catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error: no se pudo guardar la imagen...");
	}
   }

}

