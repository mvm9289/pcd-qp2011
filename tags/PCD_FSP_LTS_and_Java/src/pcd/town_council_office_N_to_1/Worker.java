package pcd.town_council_office_N_to_1;

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
		
		int turn = myPanel.turn(myID);
		while (turn != -1)
		{
			myOffice.check_turn(myID, turn);
			myOffice.service(myID, turn);
			myPanel.inc(myID);
			
			try
			{
				Thread.sleep((long)random.nextInt(1000));
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			
			turn = myPanel.turn(myID);
		}
	}
	
}
