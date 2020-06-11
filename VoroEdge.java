package delaunay2;
import java.util.ArrayList;
public class VoroEdge  {
	Edge voredge;
	ArrayList<Cell >cells;
	public VoroEdge()
	{
		cells = new ArrayList<Cell>();
		voredge = null;
	}
	public VoroEdge(Edge item)
	{
		cells = new ArrayList<Cell>();
		voredge=item;
	}
	public void addCell(Cell item)
	{
		this.cells.add(item);
	}
	public ArrayList<Cell> getCell()
	{
		return this.cells;
	}

	
}
