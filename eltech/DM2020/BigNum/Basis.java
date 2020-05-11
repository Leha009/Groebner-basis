package eltech.DM2020.BigNum;

import java.util.*;

public class Basis
{
	public ArrayList<BigPolinom> polynoms = new ArrayList<BigPolinom>();
	private ArrayList<BigPolinom> basePolynoms = new ArrayList<BigPolinom>();
	private ArrayList<String> linked = new ArrayList<String>();	//Записываем те многочлены, с которыми уже строили S полином
	private int maxpower;
	
	public void addBasis(String newPolynom)
	{
		polynoms.add( new BigPolinom(maxpower, newPolynom) );
		basePolynoms.add( new BigPolinom(maxpower, newPolynom) );
	}
	
	public void setMaxPower(int power)
	{
		maxpower = power;
	}
	
	public void doActions()
	{
		Buhberger();
		removeDivided();
		output();
	}
	
	public void output()
	{
		int i;
		String buffS;
		BigPolinom buff;
		for(i = 0; i < this.polynoms.size(); i++)
		{
			buff = this.polynoms.get(i);
			buff.divideByHighCoef();
			this.polynoms.set(i, buff);
		}
		System.out.println("Размер базиса: " + this.polynoms.size());
		for(i = 0; i < this.polynoms.size(); i++)
		{
			buffS = this.polynoms.get(i).toString();
			if(maxpower < 4)
			{
				buffS = buffS.replace("x1", "x");
				buffS = buffS.replace("x2", "y");
				buffS = buffS.replace("x3", "z");
			}
			System.out.println(buffS + "\n");
		}
	}
	
	private void Buhberger()
	{
		newLinkList();
		boolean f = true;
		sPolynom2();
		if(polynoms.size() == basePolynoms.size())
			sPolynom();
		while(f)
		{
			//removeDivided();
			f = simple3();
			if(!f)
				f = sPolynom2();
			else
			{
				//while(simple3());
				sPolynom2();
			}
			removeEquals();//equals
			System.out.println(f);
		}
		removeEquals();
	}
	
	private void removeDivided()
	{
		int i,j;
		for(i = 0; i < this.polynoms.size(); i++)
		{
			for(j = 0; j < this.polynoms.size(); j++)
			{
				if(i != j)
					if(this.polynoms.get(i).getHighMonom().isDivided(this.polynoms.get(j).getHighMonom()))
					{
					//if(this.polynoms.get(i).equals2(this.polynoms.get(j)))
						this.polynoms.remove(i);
						linked.remove(i);
						i = 0;
						j = 0;
					}
			}
		}
	}
	
	private void removeEquals()
	{
		int i,j;
		for(i = 0; i < this.polynoms.size(); i++)
		{
			for(j = 0; j < this.polynoms.size(); j++)
			{
				if(i != j)
					if(this.polynoms.get(i).equals2(this.polynoms.get(j)))
					{
						this.polynoms.remove(i);
						i = 0;
						j = 0;
					}
			}
		}
	}
	
	/*private void simple()
	{
		int i;
		for(i = 0; i < this.polynoms.size(); i++)				//Упрощаем базисы
		{
			this.polynoms.set(i, this.polynoms.get(i).reduce2(this.polynoms));
			//System.out.println("TEST " + this.polynoms.get(i).isZero() + " i = " + i);
			if(this.polynoms.get(i).isZero())
			{
				this.polynoms.remove(i);
				i--;
			}
		}
	}
	
	private boolean simple2()
	{
		BigPolinom buff;
		int i = 0;
		boolean res = false;
		System.out.println("Размер: " + this.polynoms.size());
		for(i = 0; i < this.polynoms.size(); i++)				//Упрощаем базисы
		{
			buff = this.polynoms.get(i);
			this.polynoms.set(i, this.polynoms.get(i).reduce2(this.polynoms));
			//System.out.println("TEST " + this.polynoms.get(i).isZero() + " i = " + i);
			if(this.polynoms.get(i).isZero())
			{
				this.polynoms.remove(i);
				i--;
			}
			else if(!this.polynoms.get(i).equals2(buff))
				res = true;
			System.out.println(i + "/" + (this.polynoms.size()-1));
		}
		copyBasis();
		return res;
	}*/
	
	private boolean simple3()
	{
		boolean f = false;
		BigPolinom buff;
		int i = 0;
		do				//Упрощаем базисы
		{
			buff = this.polynoms.get(i).reduce22(this.polynoms);	//reduce2
			if(!(buff.equals2(this.polynoms.get(i))))
			{
				if(buff.isZero())
					this.polynoms.remove(i);
				else
					this.polynoms.set(i, buff);
				i--;
				if(!f && !buff.isZero())
					f = true;
			}
			i++;
			System.out.println(i + "/" + this.polynoms.size());
		} while(i < this.polynoms.size());
		/*for(i = 0; i < this.polynoms.size(); i++)
		{
			buff = this.polynoms.get(i);
			buff.divideByHighCoef();
			this.polynoms.set(i, buff);
		}*/
		//copyBasis();
		newLinkList();
		return f;
	}
	
	private void sPolynom()
	{
		Integer i,j;
		for(i = 0; i < this.basePolynoms.size(); i++)
			for(j = i+1; j < this.basePolynoms.size(); j++)
			{
				//System.out.println(this.isLinked(i,j));
				if(!this.isLinked(i,j))
				{
					this.basePolynoms.get(i).sPolynom( this.basePolynoms.get(j) ).reduce(this.polynoms);
					linked.add("");
				}
			}
		//copyBasis();
	}
	
	private boolean sPolynom2()
	{
		boolean f = false, temp;
		Integer i,j,k;
		BigPolinom buff;
		System.out.println("size : " + this.polynoms.size());
		for(i = 0; i < this.polynoms.size(); i++)
			for(j = 0; j < this.polynoms.size(); j++)
			{
				temp = false;
				if(i != j && !this.polynoms.get(i).getHighMonom().gcd(this.polynoms.get(j).getHighMonom()).isConst() && !this.isLinked(i,j))
				{
					buff = this.polynoms.get(i).sPolynom2( this.polynoms.get(j) );
					if(!buff.isZero() && !isReducedToZero(buff))
						temp = buff.reduce(this.polynoms);
					if(!this.isLinked(i,j))
						addLink(i,j);
					if(temp)
					{
						linked.add("");
					}
					if(!f)
						f = temp;
					System.out.println("SPoly: " + i + " : " + j + " size:" + this.polynoms.size());
				}
			}
		/*for(i = 0; i < this.polynoms.size(); i++)
		{
			buff = this.polynoms.get(i);
			buff.divideByHighCoef();
			this.polynoms.set(i, buff);
		}*/
		//newLinkList();
		return f;
	}
	
	private boolean isLinked(Integer ths, Integer other)		//Проверка на связку
	{
		String buffS = "," + other.toString();
		String buffLink = linked.get(ths);
		//System.out.println(buffS + " : " + buffLink);
		if(buffLink.indexOf(buffS) == -1)
			return false;
		return true;
	}
	
	private void addLink(Integer ths, Integer other)
	{
		String buffS = "," + other.toString();
		String buffLink = linked.get(ths);
		if(buffLink.equals(""))
			buffLink = buffS;
		else
			buffLink += "," + buffS;
		linked.set(ths, buffLink);
	}
	
	public boolean isReducedToZero(BigPolinom ths)
	{
		boolean f;
		int i,j;
		BigMonom buff = ths.getHighMonom();
		for(i = 0; i < this.polynoms.size(); i++)
			for(j = 0; j < this.polynoms.size(); j++)
				if(i != j)
					if(polynoms.get(i).getHighMonom().lcm(polynoms.get(j).getHighMonom()).isDivided(buff) && isLinked(i,j))
						return true;
		return false;
	}
	
	public void newLinkList()
	{
		int i;
		while(linked.size() > 0)
			linked.remove(0);
		for(i = 0; i < polynoms.size(); i++)
			linked.add("");
	}
	
	public void decision()
	{
		BigPolinom buf;
		BigPolinom end = new BigPolinom(this.polynoms.get(0).getFactors().get(0).getPowers().size(), "1");
		for(int i=0; i < this.polynoms.size(); i++)
		{
			buf = this.basePolynoms.get(i);
			System.out.print("\nИзначально ");
			System.out.println(buf);
			for(int j=0; j < this.polynoms.size(); j++)
			{
				if (!buf.divide(this.polynoms.get(j)).isZero())
				{
					System.out.print("\nС помощью чего сокращаем: ");
					System.out.println(this.polynoms.get(j));
					if(buf.mod(this.polynoms.get(j)).isZero())
					{
						break;
					}
					buf=buf.mod(this.polynoms.get(j));
					System.out.print("\nОстаток ");
					System.out.println(buf);
				}
			}
			System.out.print("\nЧто должны добавить: ");
			System.out.println(buf);
			end = end.add(buf);
			System.out.print("\nКонец ");
			System.out.println(end);
		}
	}
	
	private void copyBasis()
	{
		int i;
		while(polynoms.size() > basePolynoms.size())
			basePolynoms.add(new BigPolinom(maxpower, "0"));
		while(basePolynoms.size() > polynoms.size())
			basePolynoms.remove(basePolynoms.size()-1);
		for(i = 0; i < polynoms.size(); i++)
			basePolynoms.set(i, polynoms.get(i).clone());
	}
}


/*
	3
	x1x2-x3^2-x3
	x1^2-x1-x2x3
	x1x3-x2^2-x2
	
	xy-z^2-z
	x^2-x-yz
	xz-y^2-y

	3
	x1x2-x3^2-x3
	x1^2+x1-x2x3
	x1x3-x2^2-x2
	
	xy-z^2-z
	x^2+x-yz
	xz-y^2-y
	
	2
	x2^2-1
	x1x2-1
	
	y^2-1
	xy-1

	3
	x1^3x2x3-x1x2+x1-x3
	x1x2-x2^2+x3
	x2^3-x3
	
	x^3yz-xy+x-z
	xy-y^2+z
	y^3-z

	2
	-6x1^2+6x1-6x2^2+6x2-2
	4x1^3-6x1^2-4x2^3+6x2^2
	
	-6x^2+6x-6y^2+6y-2
	4x^3-6x^2-4y^3+6y^2

*/