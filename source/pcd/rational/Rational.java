package pcd.rational;

public class Rational
{
	
	public int numerator;
	public int denominator;
	
	public Rational(int num, int den)
	{
		numerator = num;
		denominator = den;
	}
	
	private int mcd(int a, int b)
	{
		if (b == 0) return a;
		
		return mcd(b, a%b);
	}
	
	public void simplify()
	{
		int aux = mcd(numerator, denominator);
		numerator /= aux;
		denominator /= aux;
	}
	
	public Rational add(Rational r)
	{
		Rational aux = new Rational(numerator*r.denominator + r.numerator*denominator, denominator*r.denominator);
		aux.simplify();
		
		return aux;
	}
	
	public Rational sub(Rational r)
	{
		Rational aux = new Rational(numerator*r.denominator - r.numerator*denominator, denominator*r.denominator);
		aux.simplify();
		
		return aux;
	}
	
	public Rational mul(Rational r)
	{
		Rational aux = new Rational(numerator*r.numerator, denominator*r.denominator);
		aux.simplify();
		
		return aux;
	}
	
	public Rational div(Rational r)
	{
		Rational aux = new Rational(numerator*r.denominator, denominator*r.numerator);
		aux.simplify();
		
		return aux;
	}
	
	public double floatValue()
	{
		double a = (double)numerator;
		double b = (double)denominator;
		
		return a/b;
	}
}
