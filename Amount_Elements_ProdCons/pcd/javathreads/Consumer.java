package pcd.javathreads;

import java.util.Random;


public class Consumer implements Runnable
{
	
	private ConcurrentStack<Integer> stack;
	private boolean finished;
	Thread myThread;
	int maxConsumeElems;
	Random random;
	
	public Consumer(ConcurrentStack<Integer> theStack, int consumeElems)
	{
		stack = theStack;
		finished = false;
		myThread = new Thread(this);
		maxConsumeElems = consumeElems;
		random = new Random();
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
			int consume = random.nextInt(maxConsumeElems);
			stack.pop(consume);
		}
	}

}
