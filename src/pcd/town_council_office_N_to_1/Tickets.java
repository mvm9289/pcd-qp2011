package pcd.town_council_office_N_to_1;


public class Tickets
{
	
	private int N;
	private int nextTicket;
	
	public Tickets(int n)
	{
		N = n;
		nextTicket = 0;
	}
	
	public synchronized int ticket(int clientID)
	{
		int result = nextTicket;
		if (result >= N) result = -1;
		nextTicket++;
		
		System.out.println("CLIENT[" + clientID + "]->ticket[" + result + "]");
		
		return result;
	}
	
}
