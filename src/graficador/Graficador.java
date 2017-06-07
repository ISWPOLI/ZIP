package graficador;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.*;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Graficador {

    public static final int TAMANOARREGLO = 1000;
    
    public int contador = 0;
    
    public static ArrayList<String> data = new ArrayList<String>();

    private static ArrayList<String> time = new ArrayList<String>();

    public static XYSeries xySeries = new XYSeries("Señal cardiaca");

    private static JFrame ventana;
     
    public Graficador() {        
        data.clear();
        for (int i = 0; i < 50; i++) {
            data.add(String.valueOf(i));
        }
        ventana = new JFrame("Gráfica Señales");
        ventana.setVisible(true);
        ventana.setSize(800, 600);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pintar();
    }

    public void pintar(){
        for (int i = 0; i < 100000; i++) {
            time.add(String.valueOf(i));
        }

        Runnable myRunnable = new Runnable(){

            public synchronized void run(){ 
                
                while(contador < data.size()){
                    
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    
                    if(data.size() > TAMANOARREGLO){
                        data.remove(data.size()-999);
                        xySeries.delete(0, 1);
                        contador -= 1;
                    }
                    
                    xySeries.add(Double.parseDouble(time.get(contador)), Double.parseDouble(data.get(contador)));
                    
                    System.err.println("X -> "+time.get(contador)+" Y -> "+data.get(contador));
                    
                    XYSeriesCollection dataset = new XYSeriesCollection();
                    dataset.addSeries(xySeries);

                    JFreeChart xyLineChart = ChartFactory.createXYLineChart("Señal cardiaca", "", "", dataset,
                                    PlotOrientation.VERTICAL, true, true, false);

                    XYPlot plot = xyLineChart.getXYPlot();

                    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
                    renderer.setSeriesPaint(0, Color.BLACK);
                    renderer.setSeriesStroke(0, new BasicStroke(0.9f));
                    plot.setRenderer(renderer);					

                    ChartPanel panel = new ChartPanel(xyLineChart);					

                    ventana.add(panel);
                    contador++;
                    /*if(data.size() > TAMANOARREGLO){
                        contador = 0;
                        System.err.println("TAMAÑO ANTES DE LIMPIAR -> "+data.size());
                        //data.clear();                       
                        System.err.println("TAMAÑO DESPUES DE LIMPIAR -> "+data.size());
                    }*/
                }
            }
            };
            Thread thread = new Thread(myRunnable);
            thread.start();
    }
            
    public static void main(String[] args) throws IOException {
        //new Graficador();
        System.err.println(data.size());
        
    }
}