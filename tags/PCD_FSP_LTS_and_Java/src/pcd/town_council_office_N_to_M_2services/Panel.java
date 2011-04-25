package pcd.town_council_office_N_to_M_2services;

public class Panel
{
	
	private int N;
	private int nextTurn;
	
	public Panel(int n)
	{
		N = n;
		nextTurn = 0;
	}
	
	public synchronized int atomicTurn(int workerID)
	{
		int result = nextTurn;
		if (nextTurn >= N) result = -1;
		nextTurn++;

		System.out.println("WORKER[" + workerID + "]->turn[" + result + "]->inc");
		
		return result;
	}

}
