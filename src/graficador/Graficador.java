package graficador;


import static graficador.CapturarSenal.data;
import java.awt.BasicStroke;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.*;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Graficador {

    private static String numeroDatos = "";
    private static String dato = "";

    public static ArrayList<String> data = new ArrayList<String>();

    private static ArrayList<String> dato2 = new ArrayList<String>();
    private static ArrayList<String> dato3 = new ArrayList<String>();
    private static ArrayList<String> dato4 = new ArrayList<String>();
    private static ArrayList<String> dato5 = new ArrayList<String>();

    private static XYSeries xySeries = new XYSeries("Señal cardiaca");

    private static JFrame ventana;
     
    public Graficador() {
        data.clear();
        ventana = new JFrame("Gráfica Señales");
        ventana.setVisible(true);
        ventana.setSize(1000, 800);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    
    public static void main(String[] args) throws IOException {
        new Graficador();

        ArrayList<Integer> datoVer = new ArrayList<Integer>();

        for (int i = 0; i < 100000; i++) {
            dato2.add(String.valueOf(i));
        }

        Runnable myRunnable = new Runnable(){

            public synchronized void run(){                
                for (int i = 0; i < dato2.size(); i++) {
                    try {
                        Thread.sleep(0);
                    } catch (InterruptedException e) {                            
                        e.printStackTrace();
                    }

                    //System.out.println("IMPRIMIENDO!!! -> "+data.get(i));
                    xySeries.add(Double.parseDouble(dato2.get(i)), Double.parseDouble(dato2.get(i)));

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
                }
            }
            };
            Thread thread = new Thread(myRunnable);
            thread.start();
    }
}