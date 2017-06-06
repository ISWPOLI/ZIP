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


    public static ArrayList<String> data = new ArrayList<String>();

    private static ArrayList<String> dato2 = new ArrayList<String>();

    private static XYSeries xySeries = new XYSeries("Señal cardiaca");

    private static JFrame ventana;
     
    public Graficador() {        
        data.clear();
        for (int i = 0; i < 50; i++) {
            data.add(String.valueOf(i));
        }
        ventana = new JFrame("Gráfica Señales");
        ventana.setVisible(true);
        ventana.setSize(1000, 800);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pintar();
    }

    public void pintar(){
        for (int i = 0; i < 100000; i++) {
            dato2.add(String.valueOf(i));
        }

        Runnable myRunnable = new Runnable(){

            public synchronized void run(){ 
                int i = 0;
                while(i < data.size()){
                    try {
                        Thread.sleep(50);
                        System.out.println("IMPRIMIENDO!!! -> "+data.get(i));
                    } catch (InterruptedException e) {                            
                        System.err.println("Se salio esta webada");
                        e.printStackTrace();
                    }

                    
                    xySeries.add(Double.parseDouble(data.get(i)), Double.parseDouble(dato2.get(i)));

                    XYSeriesCollection dataset = new XYSeriesCollection();
                    dataset.addSeries(xySeries);

                    JFreeChart xyLineChart = ChartFactory.createXYLineChart("Señal cardiaca", "", "", dataset,
                                    PlotOrientation.VERTICAL, true, true, false);

                    XYPlot plot = xyLineChart.getXYPlot();

                    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
                    renderer.setSeriesPaint(0, Color.BLACK);
                    renderer.setSeriesStroke(0, new BasicStroke(0.1f));
                    plot.setRenderer(renderer);					

                    ChartPanel panel = new ChartPanel(xyLineChart);					

                    ventana.add(panel);
                    i++;
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