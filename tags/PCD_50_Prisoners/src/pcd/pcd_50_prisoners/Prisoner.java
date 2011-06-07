package pcd.pcd_50_prisoners;

import java.util.Random;

public class Prisoner implements Runnable
{

	private final static int NONE = 0;
	private final static int AT_LEAST_ONE = 1;
	private final static int AL_LEAST_TWO = 2;
	
	private Thread myThread;
	private Room myRoom;
	private int state;
	private Random myRandom;
	private int id;
	private boolean imFree;
	private int[] myGlobalPrisoners;
	
	public Prisoner(Room room, int prisoner, int[] globalPrisoners)
	{
		myGlobalPrisoners = globalPrisoners;
		imFree = false;
		id = prisoner;
		myRandom = new Random();
		state = NONE;
		myRoom = room;
		myThread = new Thread(this);
		myThread.start();
	}
	
	public void free()
	{
		imFree = true;
	}
	
	public boolean isFree()
	{
		return imFree && !myThread.isAlive();
	}

	@Override
	public void run()
	{
		while (!imFree)
		{
			myRoom.in();
			System.out.print("PRISONER[" + id + "] -> room.in");
			switch (state)
			{
				case NONE:
				case AT_LEAST_ONE:
					if (myRoom.isOn())
					{
						myRoom.turnOff();
						System.out.print(" -> room.turnOff");
						myRoom.turnOn();
						System.out.print(" -> room.turnOn");
					}
					else
					{
						myRoom.turnOn();
						System.out.print(" -> room.turnOn");
						if (state == NONE)
						{
							myGlobalPrisoners[0]++;
							state = AT_LEAST_ONE;
						}
						else state = AL_LEAST_TWO;
					}
					break;
				case AL_LEAST_TWO:
					if (myRoom.isOn())
					{
						myRoom.turnOff();
						System.out.print(" -> room.turnOff");
						myRoom.turnOn();
						System.out.print(" -> room.turnOn");
					}
					else
					{
						myRoom.turnOn();
						System.out.print(" -> room.turnOn");
						myRoom.turnOff();
						System.out.print(" -> room.turnOff");
					}
					break;
			}
			System.out.println(" -> room.out");
			myRoom.out();
			
			try
			{
				Thread.sleep((long)myRandom.nextInt(2000));
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

}
