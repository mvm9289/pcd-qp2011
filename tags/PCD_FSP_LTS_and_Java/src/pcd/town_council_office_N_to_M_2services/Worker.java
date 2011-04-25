package pcd.town_council_office_N_to_M_2services;

import java.util.Random;

public class Worker implements Runnable
{

	private int myID;
	private Thread myThread;
	private Panel myPanel;
	private Office myOffice;
	
	public Worker(int id, Panel panel, Office office)
	{
		myID = id;
		myThread = new Thread(this);
		myPanel = panel;
		myOffice = office;
		myThread.start();
	}
	
	@Override
	public void run()
	{
		Random random = new Random();
		
		int turn = myPanel.atomicTurn(myID);
		while (turn != -1)
		{
			myOffice.check_turn(myID, turn);
			myOffice.service(myID, turn);
			
			try
			{
				Thread.sleep((long)random.nextInt(1000));
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			
			turn = myPanel.atomicTurn(myID);
		}
	}
	
}
