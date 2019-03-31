import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;

public class LSP implements Serializable
{
    public String sender;   //IP-Port
    //public int portNo;
    //InetAddress ipAddr;
    public int sequenceNo;
    public int ttl;
    public String neighbours;
    //public

    LSP(final String sender,final int sequenceNo,final int ttl, String neighbours)   //neighbours string is like: IP1-Port1-Cost1;IP2-Port2-Cost2;
    {
        this.sender = sender;
        this.sequenceNo = sequenceNo;
        this.ttl = ttl;
        this.neighbours = neighbours;
    }

    @Override
    public String toString() {
        return sender+":"+sequenceNo+":"+ttl+":"+neighbours;
    }
}
