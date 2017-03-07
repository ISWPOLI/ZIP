import java.awt.BasicStroke;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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


	public static void main(String[] args) {		
		
		XYSeries datos1 = new XYSeries("Señal cardiaca");
	
		datos1.add(1.0,3.0);
		datos1.add(1.2,4.5);
		datos1.add(1.4,1.2);
		datos1.add(1.6,3.0);
		datos1.add(2.0,3.0);
		
		datos1.add(2.0,3.0);
		datos1.add(2.2,4.5);
		datos1.add(2.4,1.2);
		datos1.add(2.6,3.0);
		datos1.add(3.1,3.0);
		
		datos1.add(3.0,3.0);
		datos1.add(3.2,4.5);
		datos1.add(3.4,1.2);
		datos1.add(3.6,3.0);
		datos1.add(4.1,3.0);
		
		datos1.add(4.0,3.0);
		datos1.add(4.2,4.5);
		datos1.add(4.4,1.2);
		datos1.add(4.6,3.0);
		datos1.add(5.1,3.0);
		
		datos1.add(5.0,3.0);
		datos1.add(5.2,4.5);
		datos1.add(5.4,1.2);
		datos1.add(5.6,3.0);
		datos1.add(6.1,3.0);
		
		datos1.add(6.0,3.0);
		datos1.add(6.2,4.5);
		datos1.add(6.4,1.2);
		datos1.add(6.6,3.0);
		datos1.add(7.1,3.0);
		
		datos1.add(7.0,3.0);
		datos1.add(7.2,4.5);
		datos1.add(7.4,1.2);
		datos1.add(7.6,3.0);
		datos1.add(8.1,3.0);
		
		datos1.add(8.0,3.0);
		datos1.add(8.2,4.5);
		datos1.add(8.4,1.2);
		datos1.add(8.6,3.0);
		datos1.add(9.1,3.0);
		
		datos1.add(9.0,3.0);
		datos1.add(9.2,4.5);
		datos1.add(9.4,1.2);
		datos1.add(9.6,3.0);
		datos1.add(10.1,3.0);
		
		datos1.add(5.0,3.0);
		datos1.add(5.2,4.5);
		datos1.add(5.4,1.2);
		datos1.add(5.6,3.0);
		datos1.add(6.1,3.0);

		
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(datos1);
		
		JFreeChart xyLineChart = ChartFactory.createXYLineChart(
				"Prueba Gráfica Señales", 
				"", 
				"", 
				dataset, 
				PlotOrientation.VERTICAL, true, true, false);
		
		XYPlot plot = xyLineChart.getXYPlot();
		
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesPaint(0, Color.BLACK);
		renderer.setSeriesStroke(0, new BasicStroke(2.0f));
		plot.setRenderer(renderer);
		
		ChartPanel panel = new ChartPanel(xyLineChart);
		
		JFrame ventana = new JFrame("Gráfica Señales");
		ventana.setVisible(true);
		ventana.setSize(800, 600);
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ventana.add(panel);
	}
}
