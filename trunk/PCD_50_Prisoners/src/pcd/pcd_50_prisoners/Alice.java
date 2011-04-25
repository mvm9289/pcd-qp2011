package pcd.pcd_50_prisoners;

import java.util.Random;

public class Alice implements Runnable
{
	
	private Thread myThread;
	private Room myRoom;
	private int totalPrisoners;
	private int counter;
	private Random myRandom;
	private int[] myGlobalPrisoners;
	private Prison myPrison;
	
	public Alice(Room room, Prison prison, int prisoners, int[] globalPrisoners)
	{
		myPrison = prison;
		myGlobalPrisoners = globalPrisoners;
		myGlobalPrisoners[0]++;
		myRandom = new Random();
		counter = 2;
		totalPrisoners = prisoners;
		myRoom = room;
		myThread = new Thread(this);
		myThread.start();
	}
	
	public boolean isFree()
	{
		return !myThread.isAlive();
	}

	@Override
	public void run()
	{
		while (counter < 2*totalPrisoners)
		{
			myRoom.in();
			System.out.print("ALICE[" + counter + "] -> room.in");
			if (!myRoom.isOn())
			{
				myRoom.turnOn();
				System.out.print(" -> room.turnOn");
				myRoom.turnOff();
				System.out.print(" -> room.turnOff");
			}
			else if (counter < 2*totalPrisoners - 1)
			{
				myRoom.turnOff();
				System.out.print(" -> room.turnOff");
				System.out.print(" -> display[" + String.valueOf((counter - 1)/2 + 1) + "]");
				counter++;
			}
			else
			{
				System.out.print(" -> freeAll[" + String.valueOf((counter - 1)/2 + 1) + "]");
				myPrison.freeAll();
				counter++;
			}
			System.out.println(" -> room.out");
			myRoom.out();
			
			try
			{
				Thread.sleep((long)myRandom.nextInt(1000));
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

}
