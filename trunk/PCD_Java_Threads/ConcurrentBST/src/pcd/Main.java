package pcd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import pcd.bst.BST;
import pcd.bst.Comparator;
import pcd.bst.Printer;
import pcd.javathreads.Producer;

public class Main
{
	
	public static void main(String[] args)
	{
		InputStreamReader input = new InputStreamReader(System.in); 
	    BufferedReader breader = new BufferedReader(input);
	    
	    System.out.println("Insert number of producers and number of elements to insert for each thread");
	    String str;
	    int prods = 1;
	    int elems = 5;
		try
		{
			str = breader.readLine();
			prods = Integer.valueOf(str).intValue();
			str = breader.readLine();
			elems = Integer.valueOf(str).intValue();
		}
		catch (IOException e1) {}
	    
		BST<Integer> theBST = new BST<Integer>();
		Comparator<Integer> comparator = new Comparator<Integer>()
		{
			public boolean compare(Integer a, Integer b)
			{
				return a.intValue() > b.intValue();
			}
		};
		Printer<Integer> printer = new Printer<Integer>()
		{
			public void print(Integer t)
			{
				System.out.print(t + " ");
			}
		};
		
		ArrayList<Producer> producers = new ArrayList<Producer>();
		for (int i = 0; i < prods; i++)
			producers.add(new Producer(elems, theBST, comparator, printer));
		
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
					started = true;
				}
				else if (option == 2 && started)
				{
					for (int i = 0; i < prods; i++)
						producers.get(i).finish();
				}

				str = breader.readLine();
				option = Integer.valueOf(str).intValue();
			}
			for (int i = 0; i < prods; i++)
				producers.get(i).finish();
		}
	    catch (IOException e) {}
	}

}
