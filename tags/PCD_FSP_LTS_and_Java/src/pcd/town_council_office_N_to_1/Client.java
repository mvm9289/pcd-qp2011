package pcd.town_council_office_N_to_1;

import java.util.Random;

public class Client implements Runnable
{

	private int myID;
	private Thread myThread;
	private Tickets myTickets;
	private Office myOffice;
	
	public Client(int id, Tickets tickets, Office office)
	{
		myID = id;
		myThread = new Thread(this);
		myTickets = tickets;
		myOffice = office;
		myThread.start();
	}
	
	@Override
	public void run()
	{
		Random random = new Random();
		
		int turn = myTickets.ticket(myID);
		while (turn != -1)
		{
			myOffice.wait_turn(myID, turn);
			myOffice.request(myID, turn);
			
			try
			{
				Thread.sleep((long)random.nextInt(1000));
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			
			turn = myTickets.ticket(myID);
		}
	}

}
