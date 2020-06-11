package delaunay2;

import java.util.Comparator;


class CustomComparator implements Comparator<VoroEdge>
{    

	@Override
	public int compare(VoroEdge ob1,VoroEdge ob2)
	{
		if(ob1.voredge.p1.coord(0)-ob2.voredge.p1.coord(0)>0.00000001)
				return 1;
		else if (ob2.voredge.p1.coord(0)-ob1.voredge.p1.coord(0)>0.00000001)
			return -1;
		else 
		{
			if(ob1.voredge.p1.coord(1)-ob2.voredge.p1.coord(1)>0.00000001)
				return 1;
			else if (ob2.voredge.p1.coord(1)-ob1.voredge.p1.coord(1)>0.00000001)
				return -1;
		}
		return 0;
	}


}
class CustomComparator2 implements Comparator<Pnt>
{

	@Override
	public int compare(Pnt p1, Pnt p2) {
		if(p2.coord(0)-p1.coord(0)>0.00000001)
			return -1;
		else if(p1.coord(0)-p2.coord(0)>0.00000001)
			return 1;
		else 
		{
			if(p2.coord(1)-p1.coord(1)>0.00000001)
				return -1;
			else if(p1.coord(1)-p2.coord(1)>0.00000001)
				return 1;
			else{				
				return 0;}
		}
	}
	
}