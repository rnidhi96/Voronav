# Voronav
Indoor Positioning with BLE Beacons using Voronoi Mapping

ALGORITHM FOR VORONOI
Order 1 Voronoi: 
1.	In addition to given sites add a super triangle with 3 sites covering the entire area.
Sites: S={(x1, y1,),(x2, y2)…(xn, yn)} + Super
n= number of sites.
2.	Create Delaunay Triangulations of all sites using Bowyer Watson algorithm.
T= {t1, t2…tx}
ti=(P1,P2,P3)
x= number of triangles
3.	Traverse Triangles: ti. 
4.	If Circumcenter: Ci = outside triangle ti go to step 7
5.	If P1 & P2 lie inside area boundary: Construct Voronoi linej(Midpt(P1,P2), Ci)
6.	If Ci = outside boundary: linej = linej(Midpt(P1,P2), Intersection(linej, boundary)). Step 9
7.	If P1, P2 ! largest side: got to step 5.
8.	Repeat steps 4-7 for P2,P3 and P1,P3
9.	Repeat steps 2-8 for remaining triangles
Constructed lines form Voronoi diagram
Order K Voronoi:
Given order k-1 (Vk-1), construct order k diagram ( Vk)
Orderk-1(i): Order of nearness to sites in ascending order.
Regionk-1(i) : Set of edges for Polygon(i) of Vk-1 mapping to Orderk-1(i)

1.	Traverse Regionk-1 of k-1 Voronoi. Get Orderk-1(i)
2.	Delete k-1 sites in Orderk-1(i)
3.	Construct Order 1 voronoi for remaining sites(k-n): Vtemp
4.	Superimpose Vtemp with Vk-1 within Regionk-1(i)
5.	Repeat steps 1-4 for all Regionk-1

Constructed lines form Ordered, Order K Voronoi diagram
