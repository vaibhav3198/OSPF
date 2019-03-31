import java.io.*;
import java.net.*;

public class Router2
{
    InetAddress _ipAddr;
    int _port;
    Socket _socket;

    void Init() throws UnknownHostException {
        _ipAddr=InetAddress.getByName("localhost");
    }

    public static void main(String[] args) {
        LSP test = new LSP("100.100.100.100-3333",1,16,"101.101.101.101-1111-2;101.101.101.101-1121-2");
//        //System.out.println(test);
//        String[] toPrint = test.toString().split(":");
//        System.out.println(toPrint[0]);
//        System.out.println(toPrint[1]);
//        System.out.println(toPrint[2]);
//        String[] neighbour = toPrint[3].split(";");
//        for (String str: neighbour)
//        {
//            System.out.println(str);
//        }

        try {

            DatagramSocket Socket = new DatagramSocket(3333);
            InetAddress IPAddress = InetAddress.getByName("localhost");
            byte[] incomingData = new byte[1024];
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(outputStream);
            os.writeObject(test);
            byte[] data = outputStream.toByteArray();
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress, 9876);
            Socket.send(sendPacket);
            System.out.println("Message sent from client");
            DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
            Socket.receive(incomingPacket);
            String response = new String(incomingPacket.getData());
            System.out.println("Response from server:" + response);
            Thread.sleep(2000);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
