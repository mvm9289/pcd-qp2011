package pcd.javathreads;

import java.util.ArrayList;
import java.util.Random;

public class Producer implements Runnable
{
	private final static int MAX_ELEMS = 20;
	
	private ConcurrentStack<Integer> stack;
	private boolean finished;
	Thread myThread;
	int maxProduceElems;
	Random random;
	
	public Producer(ConcurrentStack<Integer> theStack, int produceElems)
	{
		stack = theStack;
		finished = false;
		myThread = new Thread(this);
		maxProduceElems = produceElems;
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
			int produce = random.nextInt(maxProduceElems);
			ArrayList<Integer> elems = new ArrayList<Integer>();
			for (int i = 0; i < produce; i++)
				elems.add(new Integer(random.nextInt(MAX_ELEMS)));
			stack.push(elems);
		}
	}

}
