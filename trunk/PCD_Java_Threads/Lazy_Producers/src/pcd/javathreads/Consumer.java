package pcd.javathreads;


public class Consumer implements Runnable
{
	
	private ConcurrentStack<Integer> stack;
	private boolean finished;
	Thread myThread;
	
	public Consumer(ConcurrentStack<Integer> theStack)
	{
		stack = theStack;
		finished = false;
		myThread = new Thread(this);
	}
	
	public void start()
	{
		myThread.start();
	}
	
	public void finish()
	{
		finished = true;
	}

	public void run()
	{
		while (!finished) stack.pop();
	}

}
