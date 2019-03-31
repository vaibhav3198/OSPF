import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class RoutingTable
{

    public class MyClass
    {
        int port;
        int cost;
        int nextPort;

        MyClass(int port,int cost,int nextPort)
        {
            this.port = port;
            this.cost = cost;
            this.nextPort = nextPort;
        }
    }

    void CreateRoutingTable(ArrayList<LSPRecord> receivedLSP,int port)
    {
//        ArrayList<MyClass2> foo = null;
//        for(int i=0;i<receivedLSP.size();i++)
//        {
//            Collection<MyClass> funk = null;
//            for(int j=0;j<receivedLSP.get(i).lsp.neighbours.size();j++)
//            {
//                funk.add(new MyClass(receivedLSP.get(i).lsp.neighbours.get(j).neighbourPort,
//                        receivedLSP.get(i).lsp.neighbours.get(j).neighbourCost,
//                        receivedLSP.get(i).lsp.neighbours.get(j).neighbourPort));
//            }
//            foo.add(new MyClass2(receivedLSP.get(i).lsp.neighbours.get(j).neighbourPort,funk));
//        }
//
//        Collection<MyClass> confirmedList = null;
//        Collection<MyClass> tentativeList = null;
//
//        for(int i=0;i<foo.size();i++)
//        {
//            if(foo.get(i).senderPort == port)
//            {
//                confirmedList.add();
//            }
//        }
//

        ArrayList<Neighbour> myList = new ArrayList<>();
        int [][] adjMAt = new int [receivedLSP.size()][receivedLSP.size()];
        int currPort = port;
        int [] done = new int[receivedLSP.size()];
        int pos=0;
        for (int i=0;i<receivedLSP.size();i++)
        {
            done[i]=0;
            if(receivedLSP.get(i).lsp.portNo == port)
            {
                pos = i;
            }
        }
        myList = receivedLSP.get(pos).lsp.neighbours;
        Collections.sort(myList,new ForSort());
        ArrayList<MyClass> confirmed = new ArrayList<>();
        ArrayList<MyClass> tentative = new ArrayList<>();
        confirmed.add(new MyClass(port,0,port));
        for(int i=0;i<myList.size();i++)
        {
            tentative.add(new MyClass(myList.get(i).neighbourPort,myList.get(i).neighbourCost,port));
        }
        for(int i=0;i<tentative.size();i++)
        {
            MyClass cont = tentative.remove(0);
            int flag=0;
            for (int j=0;j<confirmed.size(); j++)
            {
                if(confirmed.get(j).port == cont.port)
                {
                    flag=1;
                    break;
                }
            }
            if(flag==1)
            {
                continue;
            }
            confirmed.add(cont);
            for (int j=0;j<receivedLSP.size();j++)
            {
                if(receivedLSP.get(j).lsp.portNo == cont.port)
                {
                    pos = j;
                }
            }
            for(int j=0;j<receivedLSP.get(pos).lsp.neighbours.size();j++)
            {
                tentative.add(new MyClass(myList.get(i).neighbourPort,myList.get(i).neighbourCost,port));
            }
        }
    }

}
