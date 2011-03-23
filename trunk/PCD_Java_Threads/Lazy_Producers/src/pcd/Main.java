package pcd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import pcd.javathreads.ConcurrentStack;
import pcd.javathreads.Consumer;
import pcd.javathreads.Producer;

public class Main
{
	
	public static void main(String[] args)
	{
		InputStreamReader input = new InputStreamReader(System.in); 
	    BufferedReader breader = new BufferedReader(input);
	    
	    System.out.println("Insert number of producers, consumers, stack size limit and stack minimum to produce (one per line)");
	    String str;
	    int prods = 1;
	    int cons = 1;
	    int maxSize = 20;
	    int minToProd = 5;
		try
		{
			str = breader.readLine();
			prods = Integer.valueOf(str).intValue();
			str = breader.readLine();
			cons = Integer.valueOf(str).intValue();
			str = breader.readLine();
			maxSize = Integer.valueOf(str).intValue();
			str = breader.readLine();
			minToProd = Integer.valueOf(str).intValue();
		}
		catch (IOException e1) {}
	    
		ConcurrentStack<Integer> theStack = new ConcurrentStack<Integer>(maxSize, minToProd);
		
		ArrayList<Producer> producers = new ArrayList<Producer>();
		for (int i = 0; i < prods; i++)
			producers.add(new Producer(theStack));
		
		ArrayList<Consumer> consumers = new ArrayList<Consumer>();
		for (int i = 0; i < cons; i++)
			consumers.add(new Consumer(theStack));
		
		System.out.println("Insert 1 to start, 2 to stop, or 0 to exit.");
		
	    boolean started = false;
	    try
	    {
			str = breader.readLine();
			int option = Integer.valueOf(str).intValue();
			while (option != 0)
			{
				if (option == 1 && !started)
				{
					for (int i = 0; i < prods; i++)
						producers.get(i).start();
					for (int i = 0; i < cons; i++)
						consumers.get(i).start();
					started = true;
				}
				else if (option == 2 && started)
				{
					for (int i = 0; i < prods; i++)
						producers.get(i).finish();
					for (int i = 0; i < cons; i++)
						consumers.get(i).finish();
				}

				str = breader.readLine();
				option = Integer.valueOf(str).intValue();
			}
			for (int i = 0; i < prods; i++)
				producers.get(i).finish();
			for (int i = 0; i < cons; i++)
				consumers.get(i).finish();
		}
	    catch (IOException e) {}
	}

}
