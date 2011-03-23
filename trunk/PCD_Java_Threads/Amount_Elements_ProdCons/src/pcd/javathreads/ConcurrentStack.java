package pcd.javathreads;

import java.util.ArrayList;
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
	
	public synchronized void push(ArrayList<E> items)
	{
		while (size() + items.size() >= maxSize)
		{
			try
			{
				wait();
			}
			catch (InterruptedException e) {}
		}
		
		E res;
		for (int i = 0; i < items.size(); i++)
		{
			res = super.push(items.get(i));
			System.out.println("Produce element " + res);
		}
		print();
		System.out.println();
		System.out.println("Stack size: " + size());
		System.out.println();
		notifyAll();
	}
	
	public synchronized ArrayList<E> pop(int items)
	{
		while (size() < items)
		{
			try
			{
				wait();
			}
			catch (InterruptedException e) {}
		}

		ArrayList<E> res = new ArrayList<E>();
		for (int i = 0; i < items; i++)
		{
			res.add(super.pop());
			System.out.println("Consume element " + res.get(i));
		}
		print();
		System.out.println();
		System.out.println("Stack size: " + size());
		System.out.println();
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
