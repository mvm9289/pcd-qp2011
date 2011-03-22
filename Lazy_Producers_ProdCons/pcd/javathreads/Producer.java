package pcd.javathreads;

import java.util.Random;

public class Producer implements Runnable
{
	private final static int MAX_ELEMS = 20;
	
	private ConcurrentStack<Integer> stack;
	private boolean finished;
	Thread myThread;
	
	public Producer(ConcurrentStack<Integer> theStack)
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
		while (!finished)
		{
			Random random = new Random();
			Integer elem = new Integer(random.nextInt(MAX_ELEMS));
			stack.push(elem);
		}
	}

}
