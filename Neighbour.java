import java.io.Serializable;
import java.net.InetAddress;

public class Neighbour implements Serializable
{
    InetAddress neighbourAddr;
    int neighbourPort;
    int neighbourCost;

    Neighbour(final InetAddress neighbourAddr,final int neighbourPort,final int neighbourCost)
    {
        this.neighbourAddr = neighbourAddr;
        this.neighbourPort = neighbourPort;
        this.neighbourCost = neighbourCost;
    }

    @Override
    public String toString() {
        return neighbourAddr.toString() + ":" + Integer.toString(neighbourPort) + Integer.toString(neighbourCost);
    }
}
