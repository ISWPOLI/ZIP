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

public class Graficador {

    public static final int TAMANOARREGLO = 100;
    
    public int contador = 0;
    
    public static String[] data = new String[TAMANOARREGLO];

    private static String[] time = new String[TAMANOARREGLO];

    public static XYSeries xySeries = new XYSeries("Señal cardiaca");

    private static JFrame ventana;
     
    public Graficador() {        
        for (int i = 0; i < data.length; i++) {
            data[i] = "0";
        }
        ventana = new JFrame("Gráfica Señales");
        ventana.setVisible(true);
        ventana.setSize(800, 600);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pintar();
    }

    public void pintar(){
        for (int i = 0; i < time.length; i++) {
            time[i] = String.valueOf(i);
        }

        Runnable myRunnable = new Runnable(){

            public synchronized void run(){ 
                while(true){
                   while(contador < data.length){
                        xySeries.add(Double.parseDouble(time[contador]), Double.parseDouble(data[contador]));
                        renderizarPantalla(xySeries);
                        contador++;                    
                    }
                    ventana = new JFrame("Gráfica Señales");                   
                    for (int i = 0; i < xySeries.getItemCount(); i++) {                        
                        if(i == xySeries.getItemCount()-1) xySeries.updateByIndex(xySeries.getItemCount()-1, Double.parseDouble(data[data.length-1]));                            
                        else xySeries.updateByIndex(i, xySeries.getY(i+1));
                        //else xySeries.updateByIndex(i, Double.parseDouble(data[i+1]));
                    }
                    renderizarPantalla(xySeries);                    
                }
            }
            };
            Thread thread = new Thread(myRunnable);
            thread.start();
    }
    
    public static void renderizarPantalla(XYSeries xySeries){
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(xySeries);

        JFreeChart xyLineChart = ChartFactory.createXYLineChart("Señal cardiaca", "Tiempo", "Data", dataset,
                        PlotOrientation.VERTICAL, true, true, true);

        XYPlot plot = xyLineChart.getXYPlot();

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.BLACK);
        renderer.setSeriesStroke(0, new BasicStroke(5f));
        plot.setRenderer(renderer);					

        ChartPanel panel = new ChartPanel(xyLineChart);					

        ventana.add(panel); 
    }
    
    public static void main(String[] args) throws IOException {
    }
}