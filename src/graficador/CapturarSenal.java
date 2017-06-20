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

/**
 * Clase que se encarga de recibir los datos del sensor.
 * @author Juan Rubiano
 * @version 1.0
 */

public class CapturarSenal implements Runnable, SerialPortEventListener {
    
    /**
    * Atributos del sensor
    */
    static CommPortIdentifier portId;
    static Enumeration portList;
    InputStream inputStream;
    SerialPort serialPort;
    
    //Objeto hilo
    Thread readThread;
    
    //Objeto del tipo Graficador
    static Graficador graf; 
    
    //Método constructor
    public CapturarSenal() {    
        //Abre el puerto
        try {
            serialPort = (SerialPort) portId.open("SimpleReadApp", 3600);
        } catch (PortInUseException e) {
            System.out.println(e);
        }
        //Inidica si el puerto está en uso
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
        //Atributos del puerto
        try {
            serialPort.setSerialPortParams(9600,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);
        } catch (UnsupportedCommOperationException e) {
            System.out.println(e);
        }
        //Inicialización del hilo
        readThread = new Thread(this);
        //Arranca el hilo
        readThread.start();
    }

    public void run() {}
    
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
                //Si el número de puertos displonibles es mayor a 0
                while (inputStream.available() > 0) {
                    //Leo el dato que trae el puerto
                    int numBytes = inputStream.read(readBuffer);
                }
                //Se crea un String vacio
                String union = "";
                //Se cambia el arreglo "numBytes" a String
                String temp = new String (readBuffer);
                //Se quitan los espacios al final, se cambian los saltos de línea, las comillas y los espacios en blanco
                temp = temp.trim().replaceAll("\n", "").replaceAll("\"", "").replaceAll(" ", "");
                //Se pasa el dato a arreglo de char
                char[] numeros = temp.toCharArray();
                //Se recorre todo el arreglo
                for (int j = 0; j < numeros.length; j++) {
                    //Si el dato que se almacena es un número, se concatena al String unión.
                    if(esNumero(String.valueOf(numeros[j]))){ 
                        union += numeros[j];
                    }
                }
                
                //Si el String concatenado convertido en entero es menor a 900 y mayor a 100, se llama el método "agregar"
                if(Integer.valueOf(union) < 900 && Integer.valueOf(union) > 100) agregar(union);
                
            } catch (IOException e) {
                System.out.println(e);
            }            
            break;
        }
    }
    
    /**
     * Método que valida si el String es un número.
     * @param String a validar
     * @return true si es número, false si no
     */
    private static boolean esNumero(String num){
        ArrayList<String> numeros = new ArrayList<String>();
        numeros.add("1");numeros.add("2");numeros.add("3"); numeros.add("4"); numeros.add("5"); 
        numeros.add("6"); numeros.add("7"); numeros.add("8"); numeros.add("9"); numeros.add("0");
        if(numeros.contains(num))     return true;
        else return false;       
    }
    
    /**
     * El método agrega el String al arreglo de datos de la clase Graficador
     * @param String correspondiente al dato a almacena
     */
    private static void agregar(String n){
        for (int i = 0; i < graf.data.length; i++) {
            if(i == graf.data.length-1) graf.data[graf.data.length-1] = n;
            else graf.data[i] = graf.data[i+1];
        }
    }
    
    public static void main(String[] args) {
        //Inicialización del programa y apertura del puerto
        portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                System.err.println(portId.getName());
                if (portId.getName().equals("COM3")) {  
                    //Inicialización de la recepción del datos desde el sensor
                    new CapturarSenal();
                    //Instanciación del Graficador.
                    graf = new Graficador();
                }
            }
        }
    }
   
}