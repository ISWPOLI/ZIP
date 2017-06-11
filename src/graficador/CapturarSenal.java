package graficador;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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


public class CapturarSenal implements Runnable, SerialPortEventListener {
    
    static CommPortIdentifier portId;
    static Enumeration portList;
    
    static Graficador graf;
    
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
                    graf = new Graficador();
                }
            }
        }
    }

    public CapturarSenal() {        
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
            byte[] readBuffer = new byte[5];
            
            try {
                while (inputStream.available() > 0) {
                    int numBytes = inputStream.read(readBuffer);
                }
                String union = "";
                String temp = new String (readBuffer);
                temp = temp.trim().replaceAll("\n", "").replaceAll("\"", "").replaceAll(" ", "");
                char[] numeros = temp.toCharArray();
                for (int j = 0; j < numeros.length; j++) {
                    if(esNumero(String.valueOf(numeros[j]))){ 
                        union += numeros[j];
                    }
                }
                
                if(Integer.valueOf(union) < 900 && Integer.valueOf(union) > 100) agregar(union);
                
            } catch (IOException e) {
                System.out.println(e);
            }            
            break;
        }
    }
    
    private static boolean esNumero(String num){
        ArrayList<String> numeros = new ArrayList<String>();
        numeros.add("1");numeros.add("2");numeros.add("3"); numeros.add("4"); numeros.add("5"); 
        numeros.add("6"); numeros.add("7"); numeros.add("8"); numeros.add("9"); numeros.add("0");
        if(numeros.contains(num))     return true;
        else return false;       
    }
    
    private static void agregar(String n){
        for (int i = 0; i < graf.data.length; i++) {
            if(i == graf.data.length-1) graf.data[graf.data.length-1] = n;
            else graf.data[i] = graf.data[i+1];
        }
        
    }
   
}