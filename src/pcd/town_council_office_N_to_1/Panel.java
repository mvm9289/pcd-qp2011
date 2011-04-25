package pcd.town_council_office_N_to_1;

public class Panel
{
	
	private int N;
	private int nextTurn;
	
	public Panel(int n)
	{
		N = n;
		nextTurn = 0;
	}
	
	public synchronized int turn(int workerID)
	{
		System.out.println("WORKER[" + workerID + "]->turn[" + nextTurn + "]");
		
		return nextTurn;
	}
	
	public synchronized void inc(int workerID)
	{
		System.out.println("WORKER[" + workerID + "]->inc");
		
		nextTurn++;
		if (nextTurn >= N) nextTurn = -1;
	}

}
