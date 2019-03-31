import java.util.Comparator;

class ForSort implements Comparator<Neighbour>
{


    @Override
    public int compare(Neighbour o1, Neighbour o2)
    {
        if(o1.neighbourCost>o2.neighbourCost)
        {
            return 1;
        }

        return 0;
    }
}