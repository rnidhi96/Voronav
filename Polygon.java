package delaunay2;
import java.util.ArrayList;

import java.util.Scanner;

 

public class Polygon

{

    public ArrayList<Pnt> Polygon(ArrayList<Pnt> Pnts)

    {

        ArrayList<Pnt> convexHull = new ArrayList<Pnt>();

        if (Pnts.size() < 3)

            return (ArrayList) Pnts.clone();

 

        int minPnt = -1, maxPnt = -1;

        double minX = Integer.MAX_VALUE;

        double maxX = Integer.MIN_VALUE;

        for (int i = 0; i < Pnts.size(); i++)// find pnts with min and max x coordinate

        {

            if (Pnts.get(i).coord(0) < minX)

            {

                minX = Pnts.get(i).coord(0);

                minPnt = i;

            }

            if (Pnts.get(i).coord(0) > maxX)

            {

                maxX = Pnts.get(i).coord(0);

                maxPnt = i;

            }

        }

        Pnt A = Pnts.get(minPnt);

        Pnt B = Pnts.get(maxPnt);

        convexHull.add(A);

        convexHull.add(B);

        Pnts.remove(A);

        Pnts.remove(B);

 

        ArrayList<Pnt> leftSet = new ArrayList<Pnt>();

        ArrayList<Pnt> rightSet = new ArrayList<Pnt>();

 

        for (int i = 0; i < Pnts.size(); i++)

        {

            Pnt p = Pnts.get(i);

            if (PntLocation(A, B, p) == -1)

                leftSet.add(p);

            else if (PntLocation(A, B, p) == 1)

                rightSet.add(p);

        }

        hullSet(A, B, rightSet, convexHull);

        hullSet(B, A, leftSet, convexHull);

 

        return convexHull;

    }

 

    public double distance(Pnt A, Pnt B, Pnt C)

    {

        double ABx = B.coord(0) - A.coord(0);

        double ABy = B.coord(1) - A.coord(1);

        double num = ABx * (A.coord(1) - C.coord(1)) - ABy * (A.coord(0) - C.coord(0));

        if (num < 0)

            num = -num;

        return num;

    }

 

    public void hullSet(Pnt A, Pnt B, ArrayList<Pnt> set,

            ArrayList<Pnt> hull)

    {

        int insertPosition = hull.indexOf(B);

        if (set.size() == 0)

            return;

        if (set.size() == 1)

        {

            Pnt p = set.get(0);

            set.remove(p);

            hull.add(insertPosition, p);

            return;

        }

        double dist = Integer.MIN_VALUE;

        int furthestPnt = -1;

        for (int i = 0; i < set.size(); i++)

        {

            Pnt p = set.get(i);

            double distance = distance(A, B, p);

            if (distance > dist)

            {

                dist = distance;

                furthestPnt = i;

            }

        }

        Pnt P = set.get(furthestPnt);

        set.remove(furthestPnt);

        hull.add(insertPosition, P);

 

        // Determine who's to the left of AP

        ArrayList<Pnt> leftSetAP = new ArrayList<Pnt>();

        for (int i = 0; i < set.size(); i++)

        {

            Pnt M = set.get(i);

            if (PntLocation(A, P, M) == 1)

            {

                leftSetAP.add(M);

            }

        }

 

        // Determine who's to the left of PB

        ArrayList<Pnt> leftSetPB = new ArrayList<Pnt>();

        for (int i = 0; i < set.size(); i++)

        {

            Pnt M = set.get(i);

            if (PntLocation(P, B, M) == 1)

            {

                leftSetPB.add(M);

            }

        }

        hullSet(A, P, leftSetAP, hull);

        hullSet(P, B, leftSetPB, hull);

 

    }

 

    public int PntLocation(Pnt A, Pnt B, Pnt P)

    {

        double cp1 = (B.coord(0) - A.coord(0)) * (P.coord(1) - A.coord(1)) - (B.coord(1) - A.coord(1)) * (P.coord(0) - A.coord(0));

        if (cp1 > 0)

            return 1;

        else if (cp1 == 0)

            return 0;

        else

            return -1;

    }
    public static boolean onSegment(Pnt p, Pnt q, Pnt r)

    {

        if (q.coord(0) <= Math.max(p.coord(0), r.coord(0)) && q.coord(0) >= Math.min(p.coord(0), r.coord(0))

                && q.coord(1) <= Math.max(p.coord(1), r.coord(1)) && q.coord(1) >= Math.min(p.coord(1), r.coord(1)))

            return true;

        return false;

    }

 

    public static int orientation(Pnt p, Pnt q, Pnt r)

    {

        double val = (q.coord(1) - p.coord(1)) * (r.coord(0) - q.coord(0)) - (q.coord(0) - p.coord(0)) * (r.coord(1) - q.coord(1));

 

        if (val == 0)

            return 0;

        return (val > 0) ? 1 : 2;

    }

 

    public static boolean doIntersect(Pnt p1, Pnt q1, Pnt p2, Pnt q2)

    {

 

        int o1 = orientation(p1, q1, p2);

        int o2 = orientation(p1, q1, q2);

        int o3 = orientation(p2, q2, p1);

        int o4 = orientation(p2, q2, q1);

 

        if (o1 != o2 && o3 != o4)

            return true;

 

        if (o1 == 0 && onSegment(p1, p2, q1))

            return true;

 

        if (o2 == 0 && onSegment(p1, q2, q1))

            return true;

 

        if (o3 == 0 && onSegment(p2, p1, q2))

            return true;

 

        if (o4 == 0 && onSegment(p2, q1, q2))

            return true;

 

        return false;

    }

 

    public boolean isInside(ArrayList<Pnt> polygon, int n, Pnt p)

    {

        int INF = 10000;

        if (n < 3)

            return false;

 

        Pnt extreme = new Pnt(INF, p.coord(1));

 

        int count = 0, i = 0;

        do

        {

            int next = (i + 1) % n;

            if (doIntersect(polygon.get(i), polygon.get(next), p, extreme))

            {

                if (orientation(polygon.get(i), p, polygon.get(next)) == 0)

                    return onSegment(polygon.get(i), p, polygon.get(next));

 

                count++;

            }

            i = next;

        } while (i != 0);

 

        return (count & 1) == 1 ? true : false;

    }

 
    public double SignedPolygonArea(ArrayList<Pnt>polygon,int N)
    {
    	
    	int i,j;
    	double area = 0;

    	for (i=0;i<N;i++) {
    		j = (i + 1) % N;
    		area += polygon.get(i).coord(0) * polygon.get(j).coord(1);
    		area -= polygon.get(i).coord(1) * polygon.get(j).coord(0);
    	}
    	area /= 2.0;

       return(area);
    	//return(area < 0 ? -area : area); for unsigned
    }
     
     
    /* CENTROID */

    public Pnt PolygonCentroid(ArrayList<Pnt>polygon,int N)
    {
    	double cx=0,cy=0;
    	double A=SignedPolygonArea(polygon,N);
    	Pnt res;
    	int i,j;

    	double factor=0;
    	for (i=0;i<N;i++) {
    		j = (i + 1) % N;
    		factor=(polygon.get(i).coord(0)*polygon.get(j).coord(1)-polygon.get(j).coord(0)*polygon.get(i).coord(1));
    		cx+=(polygon.get(i).coord(0)+polygon.get(j).coord(0))*factor;
    		cy+=(polygon.get(i).coord(1)+polygon.get(j).coord(1))*factor;
    	}
    	A*=6.0f;
    	factor=1/A;
    	cx*=factor;
    	cy*=factor;
    	res=new Pnt(cx,cy);
    	
    	return res;
    }


    public static void main(String args[])

    {

        System.out.println("Quick Hull Test");

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the number of Pnts");

        int N = sc.nextInt();

 

        ArrayList<Pnt> Pnts = new ArrayList<Pnt>();

        System.out.println("Enter the coordinates of each Pnts: <x> <y>");

        for (int i = 0; i < N; i++)

        {

            int x = sc.nextInt();

            int y = sc.nextInt();

            Pnt e = new Pnt(x, y);

            Pnts.add(i, e);

        }

 

        Polygon qh = new Polygon();

        ArrayList<Pnt> p = qh.Polygon(Pnts);

        System.out

                .println("The Pnts in the Convex hull using Quick Hull are: ");

        for (int i = 0; i < p.size(); i++)

            System.out.println("(" + p.get(i).coord(0) + ", " + p.get(i).coord(1) + ")");
        Polygon obj= new Polygon();
        for (int i = 0; i < N; i++)

        {
        	System.out.println("Enter the coordinates of point to be checked: <x> <y>");

            int x1 = sc.nextInt();

            int y1 = sc.nextInt();

            Pnt e = new Pnt(x1, y1);

            System.out.println(obj.isInside(p, N, e));

        }

        

        sc.close();

    }

}