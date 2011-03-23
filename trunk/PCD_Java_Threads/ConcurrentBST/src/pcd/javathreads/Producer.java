package pcd.javathreads;

import java.util.Random;

import pcd.bst.BST;
import pcd.bst.Comparator;
import pcd.bst.Printer;

public class Producer implements Runnable
{
	private final static int MAX_ELEMS = 100;
	
	private int elements;
	private int elementsInserted;
	private BST<Integer> bst;
	private Comparator<Integer> comp;
	private Printer<Integer> printer;
	private boolean finished;
	Thread myThread;
	
	public Producer(int elems, BST<Integer> theBST, Comparator<Integer> comparator, Printer<Integer> print)
	{
		elements = elems;
		elementsInserted = 0;
		bst = theBST;
		comp = comparator;
		printer = print;
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
		while (!finished && elementsInserted < elements)
		{
			Random random = new Random();
			Integer elem = new Integer(random.nextInt(MAX_ELEMS));
			bst.insert(elem, comp, printer, true);
			elementsInserted++;
		}
	}

}
