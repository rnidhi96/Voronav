package delaunay2;
import java.util.ArrayList;
import java.awt.Polygon;

public class Cell extends Pnt {

	ArrayList<Pnt>sites;
	String order;
	Pnt centroid;
	
	Pnt boundary[] = new Pnt[3];
	ArrayList<VoroEdge> elist;
	public Cell()
	{
		sites=new ArrayList<Pnt>();
		order="";
		elist=new ArrayList<VoroEdge>();
		boundary[0]=new Pnt(-1,-1);
		boundary[1]=new Pnt(-1,-1);
		boundary[2]=new Pnt(-1,-1);
	}
	public Cell(String o)
	{
		sites=new ArrayList<Pnt>();
		order=o;
		elist=new ArrayList<VoroEdge>();
		boundary[0]=new Pnt(-1,-1);
		boundary[1]=new Pnt(-1,-1);
		boundary[2]=new Pnt(-1,-1);
	}
	public Cell(String o,ArrayList<Pnt> old_sites)
	{
		sites=old_sites;
		order=o;
		elist=new ArrayList<VoroEdge>();
		boundary[0]=new Pnt(-1,-1);
		boundary[1]=new Pnt(-1,-1);
		boundary[2]=new Pnt(-1,-1);
	}
	public void setOrder(String o)
	{
		this.order=o;
	}
	public String concat_Order(String o, Pnt P)
	{
		
		this.sites.add(P);
		return (this.order+o);
	}
	public void update_edge(VoroEdge item)
	{
		this.elist.add(item);		
	}
	public void update_site(Pnt item)
	{
		this.sites.add(item);		
	}
	public ArrayList<VoroEdge> get_edges()
	{
		return this.elist;
	}
	public ArrayList<Pnt> get_sites()
	{
		return this.sites;
	}
	public void remove_edge(VoroEdge item)
	{
		this.elist.remove(item);		
	}
	public void set_centroid(Pnt c)
	{
		this.centroid=c;
	}
	
		
}