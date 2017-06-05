package graficador;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.comm.CommPortIdentifier;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import javax.comm.UnsupportedCommOperationException;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class CapturarSenal implements Runnable, SerialPortEventListener {
    static CommPortIdentifier portId;
    static Enumeration portList;
    
    public static ArrayList<String> data = new ArrayList<String>();
    private static XYSeries datos1 = new XYSeries("Señal cardiaca");
     private static ArrayList<String> dato2 = new ArrayList<String>();
    
     
     private  JFrame ventana;

    private static ArrayList<String> datoVer = new ArrayList<String>();
    InputStream inputStream;
    SerialPort serialPort;
    Thread readThread;
    
    ExecutorService executor = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {        
        portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                System.err.println(portId.getName());
                 if (portId.getName().equals("COM3")) {
                    
                    new CapturarSenal();
                }
            }
        }
    }

    public CapturarSenal() {
        data.clear();
        ventana = new JFrame("Gráfica Señales");
        //ventana.setVisible(true);
        ventana.setSize(1000, 800);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        try {
            serialPort = (SerialPort) portId.open("SimpleReadApp", 3600);
        } catch (PortInUseException e) {
            System.out.println(e);
        }
        try {
            inputStream = serialPort.getInputStream();
        } catch (IOException e) {
            System.err.println("Puerto en uso!");
        }
	try {
            serialPort.addEventListener(this);
	} catch (TooManyListenersException e) {
            System.out.println(e);
        }
        serialPort.notifyOnDataAvailable(true);
        try {
            serialPort.setSerialPortParams(9600,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);
        } catch (UnsupportedCommOperationException e) {
            System.out.println(e);
        }
        readThread = new Thread(this);
        executor.execute(this);
        readThread.start();
    }

    public void run() {
        
    }
    
    //@Override
    public void serialEvent(SerialPortEvent event) {
        switch(event.getEventType()) {
        case SerialPortEvent.BI:
        case SerialPortEvent.OE:
        case SerialPortEvent.FE:
        case SerialPortEvent.PE:
        case SerialPortEvent.CD:
        case SerialPortEvent.CTS:
        case SerialPortEvent.DSR:
        case SerialPortEvent.RI:
        case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
            break;
        case SerialPortEvent.DATA_AVAILABLE:
            byte[] readBuffer = new byte[20];
            String temp = "";
            try {
                while (inputStream.available() > 0) {
                    int numBytes = inputStream.read(readBuffer);
                    
                }
                data.add(new String(readBuffer));
                
                System.err.println(new String(readBuffer));
                
                            
                Runnable myRunnable = new Runnable(){
                    
                    public synchronized void run(){                
                        for (int i = 0; i < data.size(); i++) {
                            try {
                                Thread.sleep(0);
                            } catch (InterruptedException e) {                            
                                e.printStackTrace();
                            }
                            
                            System.out.println("IMPRIMIENDO!!! -> "+data.get(i));
                            datos1.add(Double.parseDouble(data.get(i).replaceAll("\n", "")), Double.parseDouble(dato2.get(i)));

                           /* XYSeriesCollection dataset = new XYSeriesCollection();
                            dataset.addSeries(datos1);

                            JFreeChart xyLineChart = ChartFactory.createXYLineChart("Señal cardiaca", "", "", dataset,
                                            PlotOrientation.VERTICAL, true, true, false);

                            XYPlot plot = xyLineChart.getXYPlot();

                            XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
                            renderer.setSeriesPaint(0, Color.BLACK);
                            renderer.setSeriesStroke(0, new BasicStroke(0.1f));
                            plot.setRenderer(renderer);					

                            ChartPanel panel = new ChartPanel(xyLineChart);					

                            ventana.add(panel);*/
                        }
                    }
                };
                Thread thread = new Thread(myRunnable);
                thread.start();
                executor.execute(myRunnable);
            } catch (IOException e) {
                System.out.println(e);
            }
            break;
        }
    }

   
}