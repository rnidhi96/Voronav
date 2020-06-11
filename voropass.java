package delaunay2;
import java.util.*;
import java.sql.*;
import java.io.*;
public class voropass {
	
	public void update(ArrayList<Cell> Vorocells, int floor_id)
	{
	
	 String JDBC_DRIVER ="com.mysql	.jdbc.Driver";
	String DB_URL = "jdbc:mysql://localhost:3306/voronav";

	//  Database credentials
	 String USER = "root";
	String PASS = "nidhi123";
	 BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
 
			
	//public static void main(String[] args) {
	Connection conn = null;
	Statement stmt = null;
	String ans;
	try{
		//STEP 2: Register JDBC driver
	    Class.forName("com.mysql.jdbc.Driver");

	    //STEP 3: Open a connection
	    System.out.println("Connecting to database...");
	    conn = DriverManager.getConnection(DB_URL,USER,PASS);

	    //STEP 4: Execute a query
	    System.out.println("Creating statement...");
	    stmt = conn.createStatement();
	    int map_id=1;
	    String order;
	    double c_x,c_y;	    
	    for(Cell cell_itr:Vorocells)
		{
	    	order=cell_itr.order;
	    	c_x=cell_itr.centroid.coord(0);
	    	c_y= cell_itr.centroid.coord(1);
	    	int r1;
	    	if(!Double.isNaN(c_x))
			 r1=stmt.executeUpdate("INSERT INTO mapping(orders,centroid_x,centroid_y,floor_id) " + "VALUES ("+order+","+c_x+","+c_y+","+ floor_id+")");
			 //map_id++;
		}
		 
		  //ResultSet rs1=stmt.executeQuery("select*from simple");
		  System.out.println("done");
		  /*while(rs1.next())
		  {
		  int oid=rs1.getInt("officeid");
		  //String state=rs.getString("office.state");
		  String region=rs1.getString("State");
		  int target=rs1.getInt("target");
		  System.out.println(" "+oid+"\t"+region+"\t"+target);
		  }*/
	  

//System.out.println("Do you wish to continue?(Y/n)");
//ans=br.readLine();
//System.out.print(ans);


}//try block
	catch(SQLException se){
	    //Handle errors for JDBC
	    se.printStackTrace();
	 }catch(Exception e){
	    //Handle errors for Class.forName
	    e.printStackTrace();
	 }finally{
	    //finally block used to close resources
	    try{
	       if(stmt!=null)
	          stmt.close();
	    }catch(SQLException se2){
	    }// nothing we can do
	    try{
	       if(conn!=null)
	          conn.close();
	    }catch(SQLException se){
	       se.printStackTrace();
	    }//end finally try catch
	 }//end finally 
	 System.out.println("Goodbye!");
	}//update
	public ArrayList<Pnt> read()
	{
	
	 String JDBC_DRIVER ="com.mysql	.jdbc.Driver";
	String DB_URL = "jdbc:mysql://localhost:3306/voronav";

	//  Database credentials
	 String USER = "root";
	String PASS = "nidhi123";
	 BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
 
			
	//public static void main(String[] args) {
	Connection conn = null;
	Statement stmt = null;
	String ans;
	ArrayList<Pnt> beacons=new ArrayList<Pnt>();
	try{
		//STEP 2: Register JDBC driver
	    Class.forName("com.mysql.jdbc.Driver");

	    //STEP 3: Open a connection
	    System.out.println("Connecting to database...");
	    conn = DriverManager.getConnection(DB_URL,USER,PASS);

	    //STEP 4: Execute a query
	    System.out.println("Creating statement...");
	    stmt = conn.createStatement();
	    int map_id=1;	    
	    double b_x,b_y;	    
	    ResultSet rs2= stmt.executeQuery("select*from beacon");
	    
	  	while(rs2.next())
	  		{
	  		int floor=rs2.getInt("floor_id");
   //   	String state=rs.getString("office.state");
	  		b_x=rs2.getDouble("beacon_x");
	  		b_y=rs2.getDouble("beacon_y");	  		
	  		Pnt p= new Pnt(b_x,b_y);
	  		beacons.add(p);
	  		}
	  	Collections.sort(beacons,new CustomComparator2());
	  	System.out.println("done");
	  	//return beacons;
	    
		  
}//try block
	catch(SQLException se){
	    //Handle errors for JDBC
	    se.printStackTrace();
	 }catch(Exception e){
	    //Handle errors for Class.forName
	    e.printStackTrace();
	 }finally{
	    //finally block used to close resources
	    try{
	       if(stmt!=null)
	          stmt.close();
	    }catch(SQLException se2){
	    }// nothing we can do
	    try{
	       if(conn!=null)
	          conn.close();
	    }catch(SQLException se){
	       se.printStackTrace();
	    }//end finally try catch
	 }//end finally 
	 System.out.println("Goodbye!");
	 return beacons;
	}//update

}//class body

