package delaunay2;

public class Edge {
	Pnt p1,p2;
	Pnt midpnt;
	public Edge()
	{
		//no initialization;
	}
	public Edge(Pnt a, Pnt b)
	{
		p1=a;
		p2=b;
		midpnt = p1.midP(p2);
	}
	
	public Pnt getP1(Edge e)
	{
		return this.p1;
	}
	public Pnt getP2()
	{
		return this.p2;
	}
	public void setP1(Pnt p)
	{
		this.p1=p;
	}
	public void setP2(Pnt p)
	{
		this.p2=p;
	}
	public boolean colinear(Edge e1,Edge e2)
	{
		return false;
	}
	  
	
}
