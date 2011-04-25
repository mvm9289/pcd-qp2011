package pcd.town_council_office_N_to_M_2services;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Office
{

	private static final int NONE = -1;
	private static final int WAITING = 1;
	private static final int CHECKED = 2;
	private static final int REQUESTED1 = 3;
	private static final int SERVED1 = 4;
	private static final int REQUESTED2 = 5;
	private static final int SERVED2 = 6;
	
	private Panel panel;
	private Tickets tickets;
	private int[] ticketState;
	private Worker[] workers;
	private Client[] clients;
	
	public Office(int NTickets, int NClients, int MWorkers)
	{
		panel = new Panel(NTickets);
		
		tickets = new Tickets(NTickets);
		
		ticketState = new int[NTickets];
		for (int i = 0; i < NTickets; i++)
			ticketState[i] = NONE;
		
		workers = new Worker[MWorkers];
		for (int i = 0; i < MWorkers; i++)
			workers[i] = new Worker(i, panel, this);
		
		clients = new Client[NClients];
		for (int i = 0; i < NClients; i++)
			clients[i] = new Client(i, tickets, this);
	}
	
	public synchronized void wait_turn(int clientID, int turn)
	{
		System.out.println("CLIENT[" + clientID + "]->wait_turn[" + turn + "]");
		
		ticketState[turn] = WAITING;
		notifyAll();
		
		while (ticketState[turn] != CHECKED)
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
	}
	
	public synchronized void check_turn(int workerID, int turn)
	{
		while (ticketState[turn] != WAITING)
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

		System.out.println("WORKER[" + workerID + "]->check_turn[" + turn + "]");
		
		ticketState[turn] = CHECKED;
		notifyAll();
	}
	
	public synchronized void request1(int clientID, int turn)
	{
		System.out.println("CLIENT[" + clientID + "]->request1[" + turn + "]");
		
		ticketState[turn] = REQUESTED1;
		notifyAll();
		
		while (ticketState[turn] != SERVED1)
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
		
		ticketState[turn] = NONE;
	}
	
	public synchronized void request2(int clientID, int turn)
	{
		System.out.println("CLIENT[" + clientID + "]->request2[" + turn + "]");
		
		ticketState[turn] = REQUESTED2;
		notifyAll();
		
		while (ticketState[turn] != SERVED2)
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
		
		ticketState[turn] = NONE;
	}
	
	public synchronized void service(int workerID, int turn)
	{
		while (ticketState[turn] != REQUESTED1 && ticketState[turn] != REQUESTED2)
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

		if (ticketState[turn] == REQUESTED1)
		{
			System.out.println("WORKER[" + workerID + "]->service1[" + turn + "]");
			ticketState[turn] = SERVED1;
		}
		
		if (ticketState[turn] == REQUESTED2)
		{
			System.out.println("WORKER[" + workerID + "]->service2[" + turn + "]");
			ticketState[turn] = SERVED2;
		}
		
		notifyAll();
	}

	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
        Pattern delimiters = Pattern.compile(System.getProperty("line.separator")+"|\\s");
        sc.useDelimiter(delimiters);

        System.out.println("Town Council Office (N to M with 2 services)\n--------------------------------------------\n");
        System.out.println("Insert number of tickets, number of clients and number of workers:");
        int nTickets = sc.nextInt();
        int nClients = sc.nextInt();
        int nWorkers = sc.nextInt();
        
        new Office(nTickets, nClients, nWorkers);
	}

}
