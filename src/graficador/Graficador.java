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

	private static ArrayList<String> dato2 = new ArrayList<String>();
	private static ArrayList<String> dato3 = new ArrayList<String>();
	private static ArrayList<String> dato4 = new ArrayList<String>();
	private static ArrayList<String> dato5 = new ArrayList<String>();

	private static XYSeries datos1 = new XYSeries("Señal cardiaca");

	public static String separarDato(String n) {
		char[] temp = n.toCharArray();
		String resultado = "";
		int cont = 0;
		for (int i = 0; i < temp.length; i++) {
			if (temp[i] != ',') {
				resultado += temp[i];
			}
			if ((temp[i] == ',' || temp[i] == temp.length + 1) && cont == 0) {
				dato2.add(resultado);
				resultado = "";
				cont++;
			} else if ((temp[i] == ',' || temp[i] == temp.length + 1) && cont == 1) {
				dato3.add(resultado);
				resultado = "";
				cont++;
			} else if ((temp[i] == ',' || temp[i] == temp.length + 1) && cont == 2) {
				dato4.add(resultado);
				resultado = "";
				cont++;
			}
		}
		dato5.add(resultado);
		return resultado;
	}

	public static void leerArchivo() throws IOException {
		int contadorLineas = 0;
		File archivo = new File("../../Physionet/Datos.txt");
		FileReader fr;

		try {
			fr = new FileReader(archivo);
			BufferedReader br = new BufferedReader(fr);
			String linea;
			while ((linea = br.readLine()) != null) {
				contadorLineas++;
				if (contadorLineas <= Integer.valueOf(numeroDatos)) {
					separarDato(linea);
				}

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void imprimir() {
		System.out.println();
		for (int i = 0; i < dato2.size(); i++) {
			System.out.println(dato2.get(i));
		}
	}

	public static void main(String[] args) throws IOException {
		numeroDatos = JOptionPane.showInputDialog(null, "Ingrese el número de datos que desea ver:", "Datos",
				JOptionPane.QUESTION_MESSAGE);
		dato = JOptionPane.showInputDialog(null, "Ingrese el dato que desea ver:", "Datos",
				JOptionPane.QUESTION_MESSAGE);

		ArrayList<String> datoVer = new ArrayList<String>();

		if (Integer.valueOf(dato) == 1) {
			datoVer = dato3;
		} else if (Integer.valueOf(dato) == 2) {
			datoVer = dato4;
		} else if (Integer.valueOf(dato) == 3) {
			datoVer = dato5;
		}

		leerArchivo();

		for (int i = 0; i < dato2.size(); i++) {
			datos1.add(Double.parseDouble(dato2.get(i)), Double.parseDouble(datoVer.get(i)));
		}

		imprimir();

		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(datos1);

		JFreeChart xyLineChart = ChartFactory.createXYLineChart("Señal cardiaca", "", "", dataset,
				PlotOrientation.VERTICAL, true, true, false);

		XYPlot plot = xyLineChart.getXYPlot();

		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesPaint(0, Color.BLACK);
		renderer.setSeriesStroke(0, new BasicStroke(0.5f));
		plot.setRenderer(renderer);

		ChartPanel panel = new ChartPanel(xyLineChart);

		JFrame ventana = new JFrame("Gráfica Señales");
		ventana.setVisible(true);
		ventana.setSize(800, 600);
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		ventana.add(panel);
	}
}