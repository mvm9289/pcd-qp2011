package pcd.bst;


public class BST<T>
{
	private T value;
	private boolean empty;
	private BST<T> left;
	private BST<T> right;
	
	public BST()
	{
		create_empty();
	}
	
	public void create_empty()
	{
		empty = true;
		left = null;
		right = null;
	}
	
	public boolean is_empty()
	{
		return empty;
	}
	
	public synchronized void insert(T x, Comparator<T> comp, Printer<T> printer, boolean first)
	{
		if (empty)
		{
			value = x;
			empty = false;
			left = new BST<T>();
			right = new BST<T>();
		}
		else if (comp.compare(value, x))
			left.insert(x, comp, printer, false);
		else
			right.insert(x, comp, printer, false);
		
		if (first)
		{
			System.out.print("Insert element ");
			printer.print(x);
			System.out.println();
			inord_traverse(printer);
			System.out.println();
			System.out.println();
		}
	}
	
	private synchronized void inord_traverse(Printer<T> printer)
	{
		if (empty) return;
		left.inord_traverse(printer);
		printer.print(value);
		right.inord_traverse(printer);
	}
}
