import java.net.InetAddress;

public class Address
{
    InetAddress ipAddr;
    int port;

    Address(final InetAddress ipAddr,final int port)
    {
        this.ipAddr = ipAddr;
        this.port = port;
    }
}
