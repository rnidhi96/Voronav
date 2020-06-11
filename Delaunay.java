package delaunay2;
import java.io.*;

import java.util.*;
import java.awt.geom.Line2D;
//import java.awt.geom.Point2D;
public class Delaunay {
	static Edge boundary[];
	static Pnt corners[];
	static ArrayList<Pnt> beacons;
	static ArrayList<Cell> Vorocells=new ArrayList<Cell>();;
	static ArrayList<Cell> Tempcells;
	static Triangle initialTriangle;
	static Triangulation original_dt;
	ArrayList<VoroEdge> bad_lines;
	static int Site_check[];
	static double lowx=1000000,lowy=1000000;
	double xlimit=2000,ylimit=1000;
	static int order=1;
	int debug=0;
	int debug2;
	int debug3;
	int cnt=0;
	public static void main(String args[])throws IOException
	{
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		int count=0;
		double x ,y;
		beacons= new ArrayList<Pnt>();
		voropass vobj= new voropass();
		//beacons=
		for(Pnt site_itr:vobj.read())
		{
		//System.out.println("Enter Beacon location:");
		//System.out.print("X Coordinate:");
		x=site_itr.coord(0);	
		//System.out.print("Y Coordinate:");
		y=site_itr.coord(1);
		String s = String.valueOf(++count);
		Cell site= new Cell(s);
		Pnt p = new Pnt(site,x,y);
		if(x<lowx)
		lowx=x;
		if(x<lowy)
		lowy=y;
		site.update_site(p);
		beacons.add(p);		
		/*System.out.println("Do you wish to enter more points?(y/n)");
		String check = stdin.readLine();
		if(check.equals("n"))
			break;
		else
			continue;*/
		
		}
		Site_check=new int[count+1];
		original_dt= new Triangulation(initialTriangle);
		int number_of_bound=4;
		int n=number_of_bound;
		
		boundary = new Edge[n];
		corners= new Pnt[n];
		Pnt origin = new Pnt(0, 0);
		corners[0] = origin;
		corners[1] = new Pnt(2000,0);
		corners[2] = new Pnt(2000,1000);
		corners[3] = new Pnt(0,1000);
		boundary[0] = new Edge(origin,corners[1]);
		boundary[1] = new Edge(corners[1],corners[2]);
		boundary[2] = new Edge(corners[2],corners[3]);
		boundary[3] = new Edge(corners[3],origin);
		double initialSize =10000;
		initialTriangle = new Triangle(
                new Pnt(-initialSize, -initialSize),
                new Pnt( initialSize, -initialSize),
                new Pnt(           0,  initialSize));
		
		Delaunay obj1 = new Delaunay();
		obj1.Voronoi(beacons);
		order++;		
		int floor_id=1;
		while(order<=3)
		{
			obj1.higher_order();
			System.out.println("Order complete"+order);
			for(Cell cell_itr:Vorocells)
			{
				for(VoroEdge j:cell_itr.get_edges())
					System.out.println("Final edges: "+j.voredge.p1+"\t"+j.voredge.p2);
				System.out.println("Centroid of Cell" + cell_itr.order+" :"+cell_itr.centroid);
			}
			
			order++;
		}
		
		vobj.update(Vorocells,floor_id);
	}
	void Voronoi(ArrayList<Pnt> beacon_list)
	{
		int debug4=0;
		if(beacon_list.size()==1)
		{
			for(int i=0;i<4;i++)
			{
				VoroEdge vtemp = new VoroEdge(boundary[i]);
				beacon_list.get(0).voro_cell.update_edge(vtemp);
				vtemp.addCell(beacon_list.get(0).voro_cell);				
			}
			return;
		}
		
		Triangulation dt= new Triangulation(initialTriangle);
		Iterator<Pnt> itr = beacon_list.iterator();		
		while(itr.hasNext())
		{
			dt.delaunayPlace((Pnt) itr.next());
			//if(order==1)
				//original_dt.delaunayPlace((Pnt) itr.next());
		}
		if(order==1)
			original_dt=dt;
		Delaunay obj = new Delaunay();
		bad_lines = new ArrayList<VoroEdge>();
		for(Triangle itr2 : dt)
		{
			Pnt[] vertices = itr2.toArray(new Pnt[0]);
			Pnt circumcentre = itr2.getCircumcenter();
			
			System.out.println("Triangle: "+ vertices[0]+ "\t" + vertices[1]+ "\t" + vertices[2]);
			System.out.println("Circumcenter: "+ circumcentre);
			for(int i=0;i<2;i++)
			{	for(int j=i+1;j<=2;j++)
					{
						//Edge edge= new Edge(vertices[i], vertices[j]);
						Pnt p1=vertices[i];
						Pnt p2 = vertices[j];
						if(circumcentre.isInside(vertices))//circumcenter outside triangle
						{
							if(p1.boundary(corners)&&p2.boundary(corners))
							{
								
								Edge temp = new Edge(p1.midP(p2),circumcentre);
								if(!circumcentre.boundary(corners))
								{									
									Pnt inter = obj.intersectBound(temp, boundary);
									if(inter !=null)//create boundary edge
									{	
										temp.setP2(inter);											
											obj.boundary_store(vertices, inter, i);
											obj.boundary_store(vertices, inter, j);																					
										
									}
								}
								//update maps								
									VoroEdge vtemp = new VoroEdge(temp);
									if(p1.boundary(corners))									
									{
										vtemp.addCell(vertices[i].voro_cell);																	
										vertices[i].voro_cell.update_edge(vtemp);
									}
									if(p2.boundary(corners))
									{
										vtemp.addCell(vertices[j].voro_cell);									
										vertices[j].voro_cell.update_edge(vtemp);
									}
								
								
							}
							
						}
						else//circumcentre is outside triangle
						{
							Pnt opp=circumcentre.isOutside(vertices);
							if(opp.equals(p1)||opp.equals(p2))//not largest facet-circumcentre not on this side
							{
								if(p1.boundary(corners)&&p2.boundary(corners))
								{
								Edge temp = new Edge(p1.midP(p2),circumcentre);
								if(!circumcentre.boundary(corners))
								{								
									Pnt inter = obj.intersectBound(temp, boundary);
									if(inter !=null)//create boundary edge
									{	
										temp.setP2(inter);
										
											obj.boundary_store(vertices, inter, i);
											obj.boundary_store(vertices, inter, j);
											
										
									}
								}
								//update maps
								
									VoroEdge vtemp = new VoroEdge(temp);
									if(p1.boundary(corners))									
									{
										vtemp.addCell(vertices[i].voro_cell);																	
										vertices[i].voro_cell.update_edge(vtemp);
									}
									if(p2.boundary(corners))
									{
										vtemp.addCell(vertices[j].voro_cell);									
										vertices[j].voro_cell.update_edge(vtemp);
									}
								
								}
							}
							else// bad lines
							{
								//store bad lines
								if(p1.boundary(corners)&&p2.boundary(corners))
								{
									//System.out.println("badline pts: "+p1+"\t"+p2);
								Edge temp = new Edge(p1.midP(p2),circumcentre);
								VoroEdge vtemp = new VoroEdge(temp);
								bad_lines.add(vtemp);
								vtemp.addCell(vertices[i].voro_cell);
								vtemp.addCell(vertices[j].voro_cell);
								}
							}
						}
					}
			}
			
			
		}		
		//compare bad lines and truncate unnecessary edges
		VoroEdge bline;
		for(int itr3=0;itr3<bad_lines.size();itr3++)
		{
			bline = bad_lines.get(itr3);			
			Cell itr_cell=bline.getCell().get(0);//
			Cell itr_cell2=bline.getCell().get(1);
				for(int itr2=0;itr2<itr_cell.get_edges().size();itr2++)
				{
					VoroEdge itr_edge=itr_cell.get_edges().get(itr2);
					Pnt bpoint = bline.voredge.p1;
					Pnt gpoint = itr_edge.voredge.p1;
					if(bpoint.isequal(gpoint))//one end point meets
					{
						if(bline.voredge.p2.boundary(corners))
							itr_edge.voredge.setP1(bline.voredge.p2);//truncate part of gline which overlaps with blline
						else//special case. Remove extra boundary intersection and remove line
						{
							for(int k=0;k<3;k++)
							{
								if(itr_cell.boundary[k].isequal(itr_edge.voredge.p2))//incorrect boundary intersection
									{boundary_alter(itr_cell,k);
									System.out.println("found wrong inter");									
									}
								if(itr_cell2.boundary[k].isequal(itr_edge.voredge.p2))//incorrect boundary intersection
								{boundary_alter(itr_cell2,k);
								System.out.println("found wrong inter");								
								}
							}
							itr_cell.remove_edge(itr_edge);
							itr_cell2.remove_edge(itr_edge);
						}
							break;
					}
				}
			
		}
		for(Pnt cell_itr:beacon_list)
		{
		
			Collections.sort(cell_itr.voro_cell.elist,new CustomComparator());
		}		
		//Traverse cells to merge edges and if order==1 detects boundary edges of cells		
		Arrays.fill(Site_check, 0);			
		for(Pnt cell_itr:beacon_list)
		{
			
			debug4++;
			VoroEdge edge_itr;
			VoroEdge edge_itr2;
			for(int e =0; e<cell_itr.voro_cell.get_edges().size()-1;e++)//traverse list of edges for each cell point
			{
				edge_itr=cell_itr.voro_cell.get_edges().get(e);
				edge_itr2=cell_itr.voro_cell.get_edges().get(e+1);
				if(edge_itr.voredge.p1.isequal(edge_itr2.voredge.p1))					
				{
					Pnt p1=edge_itr.voredge.p1;
					Pnt p2=edge_itr.voredge.p2;
					Pnt p3=edge_itr2.voredge.p2;
					if(p1.collinear(p1, p2, p3))//points are collinear
					{
					edge_itr.voredge.setP1(edge_itr2.voredge.p2);//merge edge 2 with edge1
					edge_itr.getCell().get(0).remove_edge(edge_itr2);//remove the edge2 from its belonging cells
					edge_itr.getCell().get(1).remove_edge(edge_itr2);
					}
										
				}
			}		
			
			boundary_edge(cell_itr.voro_cell);
			if(order==1)
			{
				Vorocells.add(cell_itr.voro_cell);								
				
			}
			
		}
		/*for(Pnt cell_itr:beacon_list)
		{
			for(VoroEdge j:cell_itr.voro_cell.get_edges())
				System.out.println("Final edges: "+j.voredge.p1+"\t"+j.voredge.p2);
			System.out.println();
		}*/
	}
	void higher_order()//Computer Order i where i<=k
	{
		ArrayList<Cell> next_order = new ArrayList<Cell>();
		debug2=0;
		int index;
		Delaunay obj1= new Delaunay();
		for(Cell cell_itr:Vorocells)//Traversing cells formed in order i-1
		{
			debug3=0;
			ArrayList<Pnt> adj_pnts = new ArrayList<Pnt>();
			lowx=100000;
			lowy=100000;
			for(Pnt site_itr:cell_itr.get_sites())
			{
				index=Integer.parseInt(site_itr.voro_cell.order);
				Site_check[index]=1;				
				
			}
			ArrayList<Pnt> adj_cell= new ArrayList<Pnt>();
			for(int i=0;i<cell_itr.get_sites().size();i++)
			{
				Pnt site_itr=cell_itr.get_sites().get(i);
				if(debug==1&&debug2==18&&i==1)
					System.out.println();
				//find adj cells of each major beacon site
				for(VoroEdge edge_itr:site_itr.voro_cell.get_edges())
				{
					for(Cell adj_itr:edge_itr.getCell())
					{
						Pnt major_site=adj_itr.get_sites().get(0);//get major site of adj cell
						 index=Integer.parseInt(major_site.voro_cell.order);
						 if(Site_check[index]==0)
						 {
							 //Create temporary beacon site and add point to list
							 Cell site= new Cell(major_site.voro_cell.order);
							 Pnt temp = new Pnt(site,major_site.coord(0),major_site.coord(1));
							 adj_pnts.add(temp);
							 site.update_site(temp);
							 if(major_site.coord(0)<lowx)
									lowx=major_site.coord(0);
									if(major_site.coord(1)<lowy)
									lowy=major_site.coord(1);
							adj_cell.add(major_site);
							Site_check[index]=1;
						 }
							 
					}
				}
				
			}
			
			Collections.sort(adj_pnts,new CustomComparator2());
			Collections.sort(adj_cell,new CustomComparator2());
			for(int t=0;t<adj_pnts.size()-1;t++)
			{
				Pnt p1=adj_pnts.get(t);
				Pnt p2=adj_pnts.get(t+1);
				Pnt p3=adj_pnts.get(t);
				Pnt p4=adj_pnts.get(t+1);
				if(p1.isequal(p2))
				{
					adj_pnts.remove(t);
					t--;
				}
				if(p3.isequal(p4))
				{
					adj_cell.remove(t);
					t--;
				}
			}
			if(debug==0&&debug2==5)
				System.out.println();
			
			System.out.println("calling order 1");
			obj1.Voronoi(adj_pnts);
			/*for(Pnt cell_itr2:adj_pnts)
			{
				for(VoroEdge j:cell_itr2.voro_cell.get_edges())
					System.out.println("Final edges: "+j.voredge.p1+"\t"+j.voredge.p2);
				System.out.println();
			}*/
			/*start merging new formed adj cells with current cell
			 cell_itr = major cell
			 Mpoly=list of major cell sites in order or polygon formation
			 temp_cell= cell iterator for new temporary order 1 to be merged with major cell
			*/
			
			ArrayList<Pnt> MPnts= new ArrayList<Pnt>();
			ArrayList<Pnt> MPoly;
			for(VoroEdge Medge_itr:cell_itr.get_edges())//form a polygon using major cell points
			{
				MPnts.add(Medge_itr.voredge.p1);
				MPnts.add(Medge_itr.voredge.p2);
				
			}
			
			Polygon obj= new Polygon();
			Pnt objp= new Pnt(1,1);
			MPoly=obj.Polygon(MPnts);
			
			//Traverse adj_cell list
			
			for(int counter=0;counter<adj_pnts.size();counter++)
			{
				Pnt j=adj_pnts.get(counter);
				ArrayList<Pnt> NPnts= new ArrayList<Pnt>();
				ArrayList<Pnt> NPoly;
				
				Cell temp_cell = j.voro_cell;
				ArrayList<Pnt> Temp_pnts = new ArrayList<Pnt>();
				for(VoroEdge Nedge_itr:temp_cell.get_edges())//form a polygon using j cell of new voronoi
				{
					NPnts.add(Nedge_itr.voredge.p1);
					NPnts.add(Nedge_itr.voredge.p2);
					
				}
				NPoly=obj.Polygon(NPnts);
				int polcheck=0;
				ArrayList<Pnt> TPoly;
				for(Pnt mpnt_itr:MPoly)
				{
					if(obj.isInside(NPoly, NPoly.size(), mpnt_itr))//check if any main cell points fall inside new polygon
					{
						Temp_pnts.add(mpnt_itr);
						polcheck++;
					}
				}
				if(polcheck==MPoly.size())//all main cell pointsfall inside new voronoi cell
				{
					//main cell itself is the new cell
					TPoly = obj.Polygon(MPoly);
				}
				else
				{
				for(VoroEdge temp_edges:j.voro_cell.get_edges())					
				{
					
					Pnt inter1=null,inter2=null;
					if(obj.isInside(MPoly, MPoly.size(), temp_edges.voredge.p1)&&obj.isInside(MPoly, MPoly.size(), temp_edges.voredge.p2))
					{
						Temp_pnts.add(temp_edges.voredge.p1);					
						Temp_pnts.add(temp_edges.voredge.p2);
					}
					else
					{
						if(obj.isInside(MPoly, MPoly.size(), temp_edges.voredge.p1))						
							Temp_pnts.add(temp_edges.voredge.p1);
						else if(obj.isInside(MPoly, MPoly.size(), temp_edges.voredge.p2))
							Temp_pnts.add(temp_edges.voredge.p2);							
					
					for(VoroEdge Medge_itr:cell_itr.get_edges())//traverse main cell edges
					{
						
						Pnt inter=objp.intersect_Pnt(Medge_itr.voredge.p1, Medge_itr.voredge.p2, temp_edges.voredge.p1, temp_edges.voredge.p2);
						if(inter!=null)
						{
							int check=0;
							for(int i=0;i<4;i++)
							{
								if(i%2==0)
								{
								if(Medge_itr.voredge.p1.coord(1)==boundary[i].p1.coord(1))
								{	if(Medge_itr.voredge.p1.coord(1)==Medge_itr.voredge.p2.coord(1))
										check=1;
								}
								}
								else
								{
								if(Medge_itr.voredge.p1.coord(0)==boundary[i].p1.coord(0))
								{
									if(Medge_itr.voredge.p1.coord(0)==boundary[i].p1.coord(0))
										check=1;
								}
								}
												
							}							
							if(check==0)
							Temp_pnts.add(inter);
						}
							
					}
					
					}
										
				}
				Collections.sort(Temp_pnts,new CustomComparator2());
				if(debug==0&&debug2==0&&debug3==2)
					System.out.println();
				for(int t=0;t<Temp_pnts.size()-1;t++)
				{
					Pnt p1=Temp_pnts.get(t);
					Pnt p2=Temp_pnts.get(t+1);
					double diff1=p1.coord(0)-p2.coord(0);
					double diff2=p1.coord(1)-p2.coord(1);
					if(Math.abs(diff1)<0.00000001&&Math.abs(diff2)<0.00000001)
					{
						if(p1.coord(0)==corners[2].coord(0)||p1.coord(0)==0||p1.coord(1)==corners[2].coord(1)||p1.coord(1)==0)//chose the accurate boundary value
							Temp_pnts.remove(t+1);
						else
						{
						Temp_pnts.remove(t);
						t--;
						}
					}
				}
				
				
				TPoly = obj.Polygon(Temp_pnts);
				}
				if(TPoly.size()!=0)
				{
				String new_order=cell_itr.order;				
				Cell new_cell = new Cell(new_order);
				for(Pnt site_itr:cell_itr.get_sites())
					new_cell.update_site(site_itr);
				Pnt k=adj_cell.get(counter);
				new_cell.order=new_cell.concat_Order(j.voro_cell.order, k.voro_cell.get_sites().get(0));//Create new cell of old order+order of new merged cell. Add merged cell site to new cell sites
				int v;
				for(v=0;v<TPoly.size()-1;v++)
				{
					
					Pnt point1=TPoly.get(v);
					Pnt point2=TPoly.get(v+1);					
					Edge new_edge= new Edge(point1,point2);
					VoroEdge vedge = new VoroEdge(new_edge);
					vedge.addCell(new_cell);
					new_cell.update_edge(vedge);
				}
				if(debug==0&&debug2==4)
					System.out.println();
				Pnt point1=TPoly.get(0);//first point
				Pnt point2=TPoly.get(v);//last point
				
				Edge new_edge= new Edge(point1,point2);
				VoroEdge vedge = new VoroEdge(new_edge);
				vedge.addCell(new_cell);
				new_cell.update_edge(vedge);
				next_order.add(new_cell);
				if(order==3)
				{
					Pnt cent=obj.PolygonCentroid(TPoly, TPoly.size());
					new_cell.set_centroid(cent);
					
				}
				}
				debug3++;
			}
			debug2++;
		}
		Vorocells= next_order;
		debug++;
		
		
	}
	void boundary_store(Pnt vertices[],Pnt inter, int i)
	{
		Pnt pbound= vertices[i].voro_cell.boundary[0];
		Pnt pbound2= vertices[i].voro_cell.boundary[1];
		Pnt pbound3= vertices[i].voro_cell.boundary[2];
		//Detect edges of cells belonging to boundary
		if(pbound.coord(0)==-1)//first boundary point		
			vertices[i].voro_cell.boundary[0]=inter;		
		else if(pbound2.coord(0)==-1)//second boundary point		
			vertices[i].voro_cell.boundary[1]=inter;
		else if(pbound3.coord(0)==-1)//second boundary point		
			vertices[i].voro_cell.boundary[2]=inter;
		
	}
	void boundary_alter(Cell cell,int k)
	{
		if(k!=2)
		{
			while(k<2)
			{
				cell.boundary[k]=cell.boundary[k+1];
				k++;
			}
		}
	}
	void boundary_edge(Cell cell)
	{
		Pnt pbound= cell.boundary[0];
		Pnt pbound2= cell.boundary[1];
		Pnt pcheck=new Pnt(1179.904255319149,1000.0);
		Pnt pcheck2= new Pnt(0.0,710.8293413173653);
		System.out.println("Boundary addition "+pbound+" "+pbound2);
		double x1,y1,x2,y2;
		x1 = pbound.coord(0);
		y1 = pbound.coord(1);
		x2 = pbound2.coord(0);
		y2 = pbound2.coord(1);		
		int check;
		VoroEdge vtemp;
		VoroEdge vtemp2;
		VoroEdge vtemp3;
		VoroEdge vtemp4;
		if(x1==-1)
			return;
		
		//for()				
			//System.out.println("2nd boundary pnt:"+inter);
			Edge temp1;
			Edge temp2=null;
			Edge temp3=null;
			Edge temp4=null;
				//System.out.println(vertices[i].voro_cell.boundary[0]);
				if((x1==x2)||(y1==y2))//belong to the same boundary line
				{
					temp1 = new Edge(pbound,pbound2);
					check=1;
				}
				else 
				{	if((int)y2-y1==(int)corners[2].coord(1))//opp pnts on x axis
					{
						if (cell.sites.get(0).coord(0)==lowx)
						{
							temp1=new Edge(corners[0],cell.boundary[0]);
							temp2=new Edge(corners[3],cell.boundary[1]);
							temp3=new Edge(corners[0],corners[3]);
						}
						else
						{
							temp1=new Edge(corners[1],cell.boundary[0]);
							temp2=new Edge(corners[2],cell.boundary[1]);
							temp3=new Edge(corners[1],corners[2]);
						}
						
						check=3;
					}
					else if((int)y1-y2==(int)corners[2].coord(1))//opp pnts on x axis
					{
						if (cell.sites.get(0).coord(0)==lowx)
						{
						temp1=new Edge(corners[0],cell.boundary[1]);
						temp2=new Edge(corners[3],cell.boundary[0]);
						temp3=new Edge(corners[0],corners[3]);
						}
						else
						{
						temp1=new Edge(corners[1],cell.boundary[1]);
						temp2=new Edge(corners[2],cell.boundary[0]);
						temp3=new Edge(corners[1],corners[2]);
						}
						check=3;
					}
				
					else if((int)x2-x1==(int)corners[2].coord(0))
					{
					
						if (cell.sites.get(0).coord(1)==lowy)
						{
							temp1=new Edge(corners[0],cell.boundary[0]);
							temp2=new Edge(corners[1],cell.boundary[1]);
							temp3=new Edge(corners[0],corners[1]);
						}
						else
						{
							temp1=new Edge(corners[3],cell.boundary[0]);
							temp2=new Edge(corners[2],cell.boundary[1]);
							temp3=new Edge(corners[2],corners[3]);
						}
						check=3;
					}
					else if((int)x1-x2==(int)corners[2].coord(0))
					{
						if (cell.sites.get(0).coord(1)==lowy)
						{
							temp1=new Edge(corners[0],cell.boundary[1]);
							temp2=new Edge(corners[1],cell.boundary[0]);
							temp3=new Edge(corners[0],corners[1]);
						}
						else
						{
							temp1=new Edge(corners[3],cell.boundary[1]);
							temp2=new Edge(corners[2],cell.boundary[0]);
							temp3=new Edge(corners[2],corners[3]);
						}
					
						check=3;
					}
					
					else
					{//1 corner or 3 corners
						check=2;
						cnt++;
						Polygon obj=new Polygon();
					Pnt corner,corner1,corner2,corner3;
					ArrayList<Pnt> poly1=new ArrayList<Pnt>();
					ArrayList<Pnt>poly2=new ArrayList<Pnt>();
					
					if(pbound.coord(0)==0||pbound.coord(0)==2000)//pbound is on x=0 or x=xlimit													
					{		
						
						corner = new Pnt(pbound.coord(0),pbound2.coord(1));
						corner1 = new Pnt(pbound.coord(0),ylimit-pbound2.coord(1));
						corner2 = new Pnt(xlimit-pbound.coord(0),ylimit-pbound2.coord(1));
						corner3 = new Pnt(xlimit-pbound.coord(0),pbound2.coord(1));
						
					}
					else//pbound is on y=0 or y=ylimit													
					{corner = new Pnt(pbound2.coord(0),pbound.coord(1));
					corner1 = new Pnt(xlimit-pbound2.coord(0),pbound.coord(1));
					corner2 = new Pnt(xlimit-pbound2.coord(0),ylimit-pbound.coord(1));
					corner3 = new Pnt(pbound2.coord(0),ylimit-pbound.coord(1));
					}
					for(VoroEdge edge_itr:cell.get_edges())//form a polygon using major cell points
					{
						poly1.add(edge_itr.voredge.p1);
						poly1.add(edge_itr.voredge.p2);
						
					}						
					poly1.add(corner);
					poly1=obj.Polygon(poly1);
				
				
					if(obj.isInside(poly1, poly1.size(), cell.get_sites().get(0)))
					{
						System.out.println("1 corner: "+ pbound+" "+ pbound2 );
					temp1 = new Edge(pbound,corner);
					temp2 = new Edge(pbound2,corner);
					}
					else
					{
						temp1 = new Edge(pbound,corner1);
						temp2 = new Edge(corner1,corner2);
						temp3 = new Edge(corner2,corner3);
						temp4 = new Edge(corner3,pbound2);
						check=4;
					}
					}
				
				}
				if(check==4)
				{
					vtemp4 = new VoroEdge(temp4);
					vtemp4.addCell(cell);
					cell.update_edge(vtemp4);
				}
				if(check>=3)
				{
					vtemp3 = new VoroEdge(temp3);
					vtemp3.addCell(cell);
					cell.update_edge(vtemp3);
				}
				if(check>1)
				{
				vtemp2 = new VoroEdge(temp2);
				vtemp2.addCell(cell);
				cell.update_edge(vtemp2);
				}
				vtemp = new VoroEdge(temp1);
				vtemp.addCell(cell);
				cell.update_edge(vtemp);
																																					
	
	}
	
	Pnt intersectBound(Edge line, Edge[] boundary)
	{
		Pnt inter;
		Pnt Pobj = new Pnt();
		
		for(int i=0;i<4;i++)
		{
			inter = Pobj.Intersection(line, boundary[i],i); 
			//inter = Pobj.intersect_Pnt(line.p1,line.p2, boundary[i].p1,boundary[i].p2);
			if(inter!=null)
			{
				System.out.println(inter);
				return inter;
			}
			
		}
		return null;
	}
	

}
