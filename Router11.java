import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Router11
{
    public Address address;
    public DatagramSocket socket;
    public static ArrayList<Neighbour> neighbours;

    public ArrayList<LSPRecord> receivedLSP;
    private final int NUM_NODES = 4;

    public Router11() throws SocketException, UnknownHostException {
        Initialise();
        receivedLSP=new ArrayList<>();
    }


    public void Initialise() throws UnknownHostException, SocketException
    {
        this.address = new Address(InetAddress.getByName("localhost"),1919);
        this.socket = new DatagramSocket(this.address.port);   //takes current ip by default

        //setting neighbours
        neighbours = new ArrayList<>();
        Neighbour temp1 = new Neighbour(InetAddress.getByName("localhost"),2222,2);
        Neighbour temp2 = new Neighbour(InetAddress.getByName("localhost"),3333,1);

        neighbours.add(temp1);
        neighbours.add(temp2);
    }

    public void Send(final Address destAddress,final LSP2 lsp)
    {
        try
        {
            byte[] incomingData = new byte[1024];
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(outputStream);
            os.writeObject(lsp);
            byte[] data = outputStream.toByteArray();
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, destAddress.ipAddr, destAddress.port);
            socket.send(sendPacket);
            System.out.println("LSP sent!");
            DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
            socket.receive(incomingPacket);
            String response = new String(incomingPacket.getData());
            System.out.println("Response" + response);
            //Thread.sleep(2000);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void Receive()
    {
        try {
            byte[] incomingData = new byte[1024];
            while (true) {
                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                socket.receive(incomingPacket);
                byte[] data = incomingPacket.getData();
                ByteArrayInputStream in = new ByteArrayInputStream(data);
                ObjectInputStream is = new ObjectInputStream(in);
                try {
                    LSP lsp = (LSP) is.readObject();
                    System.out.println("LSP received = "+lsp);
                    //store the lsp and forward it
//                    for (int i=0;i<receivedLSP.size();i++)
//                    {
//                        if(receivedLSP.get(i).lsp.neighbours)
//                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                InetAddress IPAddress = incomingPacket.getAddress();
                int port = incomingPacket.getPort();
                String reply = "LSP received";
                byte[] replyBytea = reply.getBytes();
                DatagramPacket replyPacket =
                        new DatagramPacket(replyBytea, replyBytea.length, IPAddress, port);
                socket.send(replyPacket);
                Thread.sleep(2000);

            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Receive(int numLSP)
    {
        System.out.println("Receiving...");
        try {
            byte[] incomingData = new byte[1024];
            while (numLSP>0) {
                numLSP--;
                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                socket.receive(incomingPacket);
                byte[] data = incomingPacket.getData();
                ByteArrayInputStream in = new ByteArrayInputStream(data);
                ObjectInputStream is = new ObjectInputStream(in);
                try {
                    LSP2 lsp = (LSP2) is.readObject();
                    System.out.println("LSP received = "+lsp);
                    //store the lsp and forward it
                    int flag = 0;
                    for(int i=0;i<receivedLSP.size();i++)
                    {
                        if(receivedLSP.get(i).lsp.ipAddr == lsp.ipAddr && receivedLSP.get(i).lsp.portNo == lsp.portNo && receivedLSP.get(i).lsp.sequenceNo < lsp.sequenceNo)
                        {
                            flag=1;
                            receivedLSP.set(i,new LSPRecord(lsp,0));
                            break;
                        }
                        else if(receivedLSP.get(i).lsp.ipAddr == lsp.ipAddr && receivedLSP.get(i).lsp.portNo == lsp.portNo)
                        {
                            flag = 1;
                            break;
                        }
                    }
                    if(flag==0)
                    {
                        receivedLSP.add(new LSPRecord(lsp,0));
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                InetAddress IPAddress = incomingPacket.getAddress();
                int port = incomingPacket.getPort();
                String reply = "LSP received";
                byte[] replyBytea = reply.getBytes();
                DatagramPacket replyPacket =
                        new DatagramPacket(replyBytea, replyBytea.length, IPAddress, port);
                socket.send(replyPacket);
                Thread.sleep(2000);
                //System.exit(0);
                System.out.println("Received 1");
            }
            numLSP--;

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SocketException, UnknownHostException {
        Router11 router8 = new Router11();

        router8.Receive(2);
        System.out.println("Exit");

    }
}
