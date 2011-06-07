package pcd.pcd_50_prisoners;

public class Room
{
	
	private Door door;
	private Light light;
	
	public Room()
	{
		door = new Door();
		light = new Light();
	}
	
	public synchronized void in()
	{
		while (!door.isFree())
		{
			try
			{
				wait();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		
		door.in();
	}
	
	public synchronized void out()
	{
		door.out();
		notify();
	}
	
	public void turnOn()
	{
		light.turnOn();
	}
	
	public void turnOff()
	{
		light.turnOff();
	}
	
	public boolean isOn()
	{
		return light.isOn();
	}

}
