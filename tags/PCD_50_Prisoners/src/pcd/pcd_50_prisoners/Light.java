package pcd.pcd_50_prisoners;

import java.util.Random;

public class Light
{
	
	private boolean on;
	
	public Light()
	{
		on = (new Random()).nextBoolean();
	}
	
	public void turnOn()
	{
		on = true;
	}
	
	public void turnOff()
	{
		on = false;
	}
	
	public boolean isOn()
	{
		return on;
	}

}
