package tech.hackaton.client;

import com.microsoft.azure.sdk.iot.device.DeviceClient;
import com.microsoft.azure.sdk.iot.device.IotHubClientProtocol;
import com.microsoft.azure.sdk.iot.device.Message;
import com.microsoft.azure.sdk.iot.device.IotHubStatusCode;
import com.microsoft.azure.sdk.iot.device.IotHubEventCallback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by j.hernandez on 3/12/2017.
 */
public class SendSerializedEvent {

    private static class Argumentos {
        public static final String CONNECTION_STRING = "HostName=fgIoTHubTest.azure-devices.net;DeviceId=myFirstJavaDevice;SharedAccessKey=yq1D5DIHkCf4wQCiJheWzQ==";
        public IotHubClientProtocol protocol;

        public Argumentos(){
            protocol = IotHubClientProtocol.HTTPS;
        }
    }

    protected static class EventCallback implements IotHubEventCallback
    {
        public void execute(IotHubStatusCode status, Object context)
        {
            System.out.println("IoT Hub respondió con el siguiente estado: " + status.name());

            if (context != null) {
                synchronized (context) {
                    context.notify();
                }
            }
        }
    }

    public static class EnviarMetricas extends Thread{

        private DeviceClient client;

        public EnviarMetricas(DeviceClient client){
            this.client = client;
        }

        @Override
        public void run(){

            try{
                while (true){
                    Mechero mechero = new Mechero();
                    mechero.cambiarLlama(valorAleatorio());

                    String msgStr = mechero.serialize();

                    Message msg = new Message(msgStr);
                    msg.setExpiryTime(5000);

                    Object lockobj = new Object();
                    EventCallback callback = new EventCallback();
                    client.sendEventAsync(msg, callback, lockobj);

                    synchronized (lockobj) {
                        lockobj.wait();
                    }

                    sleep(30000);
                }
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args)
            throws IOException, URISyntaxException {
        System.out.println("Iniciando...");
        System.out.println("Cargando configuración.");

        Argumentos arguments = new Argumentos();

        System.out.println("Configuración establecida.");
        System.out.format("Usando el protocolo %s.\n", arguments.protocol.name());

        try {
            DeviceClient client = new DeviceClient(arguments.CONNECTION_STRING, arguments.protocol);
            System.out.println("Se creó el cliente IoT Hub satisfactoriamente.");
            client.open();
            System.out.println("Se abrió una conexión a IoT Hub.");

            EnviarMetricas metricas = new EnviarMetricas(client);
            metricas.start();
            esperarInterrupcion();
            metricas.stop();
            metricas.join();

            client.close();
        } catch (Exception e) {
            System.out.println("Falla: " + e.toString());
        }

        System.out.println("Finalizando conexión...");
    }

    private static void esperarInterrupcion() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        br.readLine();
    }

    private static double valorAleatorio() {
        return ThreadLocalRandom.current().nextDouble(0, 100);
    }
}
