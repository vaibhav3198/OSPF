import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class RouterD
{
    public Address address;
    public DatagramSocket socket;
    public  ArrayList<Neighbour> neighbours;

    public ArrayList<LSPRecord> receivedLSP;
    private final int NUM_NODES = 3;

    RouterD() throws SocketException, UnknownHostException {
        Initialise();
        receivedLSP = new ArrayList<>();
        receivedLSP.add(new LSPRecord(new LSP2(this.address.ipAddr,this.address.port,0,16,this.neighbours),0));
    }


    public void Initialise() throws UnknownHostException, SocketException
    {
        this.address = new Address(InetAddress.getByName("localhost"),5003);
        this.socket = new DatagramSocket(this.address.port);   //takes current ip by default

        //setting neighbours
        neighbours = new ArrayList<>();
        Neighbour temp1 = new Neighbour(InetAddress.getByName("localhost"),5001,1);

        neighbours.add(temp1);
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
        System.out.println("Receiving...");
        try {
            byte[] incomingData = new byte[1024];
            while (true) {
                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                socket.receive(incomingPacket);
                byte[] data = incomingPacket.getData();
                ByteArrayInputStream in = new ByteArrayInputStream(data);
                ObjectInputStream is = new ObjectInputStream(in);
                try {
                    LSP2 lsp = (LSP2) is.readObject();
                    System.out.println("LSP received = "+lsp);
                    //store the lsp and forward it
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

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (InterruptedException e) {
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
                        System.out.println("Received Data: " +lsp.portNo + " " + receivedLSP.get(i).lsp.portNo +" : " );
                        if( receivedLSP.get(i).lsp.portNo == lsp.portNo && receivedLSP.get(i).lsp.sequenceNo < lsp.sequenceNo)
                        {
                            flag=1;
                            System.out.println("I am here1");
                            receivedLSP.set(i,new LSPRecord(lsp,0));
                            break;
                        }
                        else if( receivedLSP.get(i).lsp.portNo == lsp.portNo)
                        {
                            flag = 1;
                            System.out.println("I am here");
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
                //System.out.println("Received 1");
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

    public static void main(String[] args) throws IOException {
        RouterD routerD = new RouterD();

        while (routerD.receivedLSP.size()!=routerD.NUM_NODES)
        {

            //receive
            int numToReceive = (int) routerD.NumToReceive(0);
            System.out.println("Received: "+numToReceive);
            routerD.Receive(numToReceive);

            //prepare the LSPs to be sent
            ArrayList<LSPRecord> listToSend = new ArrayList<>();
            for(int i=0;i<routerD.receivedLSP.size();i++)
            {
                if(routerD.receivedLSP.get(i).sent == 0)
                {
                    LSPRecord temp = routerD.receivedLSP.get(i);
                    listToSend.add(temp);
                    temp.sent=1;
                    routerD.receivedLSP.set(i,temp);
                }
            }

            if(listToSend.size() == 0)
            {
                System.out.println("Nothing to send");
            }
            //send LSPs
            routerD.SendNumLSP(listToSend.size());
            for (int i=0;i<listToSend.size();i++)  //for each LSP
            {
                for(int j=0;j<routerD.neighbours.size();j++)  //for each neighbour
                {
                    routerD.Send(new Address(routerD.neighbours.get(j).neighbourAddr,routerD.neighbours.get(j).neighbourPort),listToSend.get(i).lsp);
                }
            }

            listToSend.clear();
        }

        System.out.println("The received LSPs are: ");
        for (int i=0;i<routerD.receivedLSP.size();i++)
        {
            System.out.println(routerD.receivedLSP.get(i));
        }


    }

    private int NumToReceive(int neighbourIndex) throws IOException {
        byte[] buffer=new byte[1024];
        DatagramPacket dp= new DatagramPacket(buffer,buffer.length);
        socket.receive(dp);
        String str=new String(dp.getData(),0,dp.getLength());



        //acknowledge
        buffer="a".getBytes();
        DatagramPacket dp2=new DatagramPacket(buffer, buffer.length,neighbours.get(neighbourIndex).neighbourAddr,neighbours.get(neighbourIndex).neighbourPort);
        this.socket.send(dp2);

        return Integer.parseInt(str.trim());
    }

    private void SendNumLSP(final int size) throws IOException {
        //System.out.println("in SendNumLSP");
        if(size>0)
        {
            //System.out.println("size: "+size);
            for(int i=0;i<neighbours.size();i++)
            {
                byte[] buffer = Integer.toString(size).getBytes();
                DatagramPacket dp=new DatagramPacket(buffer, buffer.length,neighbours.get(i).neighbourAddr,neighbours.get(i).neighbourPort);
                this.socket.send(dp);

                //wait for acknowledgement
                DatagramPacket dp2= new DatagramPacket(buffer,buffer.length);
                this.socket.receive(dp);
                String ack=new String(dp.getData(),0,dp.getLength());
                System.out.println(ack);
            }

        }
    }
}
