package graficador;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.IOException;

import javax.swing.JFrame;

import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * Clase encargada de la GUI de la aplicación
 * @author Juan Rubiano
 * @version 1.0
 */
public class Graficador {

    //Indica el tamaño del rango en eje x
    public static final int TAMANOARREGLO = 200;
    
    //Arreglo que almacena los datos enviados desde la clase CapturarSenal.java
    public static String[] data = new String[TAMANOARREGLO];

    //Arreglo que tiene los datos en el eje x
    private static String[] time = new String[TAMANOARREGLO];
    
    //Arreglo de dos dimensiones que almacena los datos en x y en y
    public static XYSeries xySeries = new XYSeries("Señal cardiaca");
    
    //Contador usado para pintar los primeros datos 
    public int contador = 0;
    
    //Ventana que contiene la gráfica
    private static JFrame ventana;
     
    //Método constructor
    public Graficador() {
        //Ciclo que almacena ceros en el arreglo de datos mientras el sensor envia
        for (int i = 0; i < data.length; i++) {
            data[i] = "0";
        }
        
        //Atributos de la ventana
        ventana = new JFrame("Gráfica Señales");
        ventana.setVisible(true);
        ventana.setSize(800, 600);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Llamada al método encargado de la renderización de la ventana
        pintar();
    }

    public void pintar(){
        //Llena el método en x con valores de tiempo en segundos.
        for (int i = 0; i < time.length; i++) {
            time[i] = String.valueOf(i);
        }
        
        //Instaciación del runnable para la ejecución del hilo
        Runnable myRunnable = new Runnable(){

            public synchronized void run(){ 
                //While en true para que nunca salga y se mantenga pintando los puntos.
                while(true){
                    //Prime while el cual pinta los primeros datos en pantalla hasta TAMANOARREGLO indique
                    while(contador < data.length){
                        //Almacena en x el tiempo y en y almacena el dato que se encuentre en la posición "contador"
                        xySeries.add(Double.parseDouble(time[contador]), Double.parseDouble(data[contador]));
                        //Llamada al método que renderiza la pantalla pasándole el arreglo de dos dimensiones
                        renderizarPantalla(xySeries);
                        //Aumento en 1 el contador
                        contador++;                    
                    }
                    //Inicializo nuevamente la pantalla
                    ventana = new JFrame("Gráfica Señales");
                    //Cuando se llena todo el arreglo de tiempo, es decir, toda la pantalla en x, entra al for 
                    for (int i = 0; i < xySeries.getItemCount(); i++) {
                        //Si el valor en i es igual al tamaño total del arreglo, se trae un dato nuevo del arreglo data
                        if(i == xySeries.getItemCount()-1) xySeries.updateByIndex(xySeries.getItemCount()-1, Double.parseDouble(data[data.length-1]));                            
                        /**
                         * La aplicación tiene dos modos de graficar:
                         * 1. Sobreescribe el último punto corriendo la gráfica tal y como lo hace arduino
                         * 2. Sobreescribe toda la gráfica reenderizando toda la pantalla
                         */
                        /** 1.  *///else xySeries.updateByIndex(i, xySeries.getY(i+1));
                        /** 2.  */else xySeries.updateByIndex(i, Double.parseDouble(data[i+1]));
                    }
                    //Reenderización de la pantalla pasandole el arreglo de dos dimensiones.
                    renderizarPantalla(xySeries);                    
                }
            }
            };
            //Creación del hilo pasándole el Runnable creado
            Thread thread = new Thread(myRunnable);
            //Inicialización del hilo
            thread.start();
    }
    
    public static void renderizarPantalla(XYSeries xySeries){
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(xySeries);
        
        //Creación de la gráfica en si, título de la gráfica, título de x, título de y, orientación.
        JFreeChart xyLineChart = ChartFactory.createXYLineChart("Señal cardiaca", "Tiempo", "Data", dataset,
                        PlotOrientation.VERTICAL, true, true, true);
        
        XYPlot plot = xyLineChart.getXYPlot();

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        //Color de los puntos.
        renderer.setSeriesPaint(0, Color.BLACK);
        //Grosor de la gráfica.
        renderer.setSeriesStroke(0, new BasicStroke(1f));
        plot.setRenderer(renderer);					
        
        //Panel que contiene la gráfica.
        ChartPanel panel = new ChartPanel(xyLineChart);					
        
        //Se agrega el panel a la ventana.
        ventana.add(panel); 
    }    
}