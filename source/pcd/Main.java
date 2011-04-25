package pcd;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import pcd.bst.BST;
import pcd.bst.Comparator;
import pcd.bst.Printer;
import pcd.rational.Rational;

public class Main
{

	public static void main(String[] args)
	{
		/* Create input objects */
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader buffer = new BufferedReader(input);
		
		/* Get number of elements in the sequence */
		int n = 0;
		try
		{
			n = Integer.parseInt(buffer.readLine());
			if (n < 0) throw new Exception();
		}
		catch (Exception e)
		{
			System.out.println("Bad argument: BST size must be an unsigned integer (one per line).");
			System.exit(0);
		}
		
		/* Construct BST and all additional objects required */
		BST<Rational> ratbst = new BST<Rational>();
		Comparator<Rational> comp = new Comparator<Rational>() {
			public boolean compare(Rational a, Rational b)
			{
				return a.floatValue() > b.floatValue();
			}
		};
		Printer<Rational> printer = new Printer<Rational>() {
			public void print(Rational t)
			{
				System.out.print(t.numerator + "/" + t.denominator + " ");
			}
		};
		
		/* Read sequence of rationals and insert in BST */
		for (int i = 0; i < n; i++)
		{
			try
			{
				String aux = buffer.readLine();
				int num = Integer.parseInt(aux.substring(0, aux.indexOf('/')));
				int den = Integer.parseInt(aux.substring(aux.indexOf('/') + 1, aux.length()));
				ratbst.insert(new Rational(num, den), comp);
			}
			catch (Exception e)
			{
				System.out.println("Bad argument: BST element must be an rational like \"x/y\" (one per line).");
				System.exit(0);
			}
		}
		
		/* Print inorder traverse of BST */
		ratbst.inord_traverse(printer);
	}

}
