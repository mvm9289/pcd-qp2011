package pcd.javathreads;

import java.util.Stack;

@SuppressWarnings("serial")
public class ConcurrentStack<E> extends Stack<E>
{

	private int maxSize;
	
	public ConcurrentStack(int size)
	{
		super();
		maxSize = size;
	}
	
	public synchronized E push(E item)
	{
		while (size() >= maxSize)
		{
			try
			{
				wait();
			}
			catch (InterruptedException e) {}
		}
		
		E res;
		synchronized(this)
		{
			res = super.push(item);

			System.out.println("Produce element " + res);
			print();
			System.out.println();
			System.out.println("Stack size: " + size());
			System.out.println();
		}
		notifyAll();
		
		return res;
	}
	
	public synchronized E pop()
	{
		while (empty())
		{
			try
			{
				wait();
			}
			catch (InterruptedException e) {}
		}

		E res;
		synchronized(this)
		{
			res = super.pop();

			System.out.println("Consume element " + res);
			print();
			System.out.println();
			System.out.println("Stack size: " + size());
			System.out.println();
		}
		notifyAll();
		
		return res;
	}
	
	public void print()
	{
		synchronized(this)
		{
			System.out.println("Stack content: ");
			for (int i = 0; i < size(); i++) System.out.print(get(i) + " ");
		}
	}
}
