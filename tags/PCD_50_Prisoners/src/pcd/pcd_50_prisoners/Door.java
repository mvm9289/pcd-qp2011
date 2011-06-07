package pcd.pcd_50_prisoners;

public class Door
{
	
	private boolean inside;
	
	public Door()
	{
		inside = false;
	}
	
	public void in()
	{
		inside = true;
	}
	
	public void out()
	{
		inside = false;
	}
	
	public boolean isFree()
	{
		return !inside;
	}

}
