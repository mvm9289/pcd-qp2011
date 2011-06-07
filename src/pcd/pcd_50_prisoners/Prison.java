package pcd.pcd_50_prisoners;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Prison
{
	
	private Alice alice;
	private Prisoner[] prisoners;
	private Room room;
	private int[] globalPrisoners;
	
	public Prison(int NPrisoners)
	{
		globalPrisoners = new int[1];
		globalPrisoners[0] = 0;
		room = new Room();
		alice = new Alice(room, this, NPrisoners, globalPrisoners);
		prisoners = new Prisoner[NPrisoners - 1];
		for (int i = 0; i < NPrisoners - 1; i++)
			prisoners[i] = new Prisoner(room, i, globalPrisoners);
	}
	
	public void freeAll()
	{
		for (int i = 0; i < prisoners.length; i++)
			prisoners[i].free();
	}
	
	public void printResults()
	{
		while(!alice.isFree());
		
		for (int i = 0; i < prisoners.length; i++)
			while(!prisoners[i].isFree());
		
		System.out.println();
		System.out.println("Prisoners who have entered the room: " + globalPrisoners[0]);
	}

	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
        Pattern delimiters = Pattern.compile(System.getProperty("line.separator")+"|\\s");
        sc.useDelimiter(delimiters);
        
        System.out.println("50 Prisoners with initial light state not known\n-----------------------------------------------\n");
        System.out.println("Insert number of prisoners:");
        int nPrisoners = sc.nextInt();

		System.out.println();
        (new Prison(nPrisoners)).printResults();
	}

}
