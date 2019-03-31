import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;

public class LSP2 implements Serializable
{
    public int portNo;
    InetAddress ipAddr;
    public int sequenceNo;
    public int ttl;
    public ArrayList<Neighbour> neighbours;

    LSP2(final InetAddress ipAddr,final int portNo,final int sequenceNo,final int ttl, final ArrayList<Neighbour>neighbours)   //neighbours string is like: IP1-Port1-Cost1;IP2-Port2-Cost2;
    {
        this.ipAddr = ipAddr;
        this.portNo = portNo;
        this.sequenceNo = sequenceNo;
        this.ttl = ttl;
        this.neighbours = neighbours;
    }

    @Override
    public String toString() {
        return ipAddr+":"+portNo+":"+sequenceNo+":"+ttl+":"+neighbours;
    }
}
