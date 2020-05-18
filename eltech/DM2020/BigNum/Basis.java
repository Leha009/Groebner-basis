package eltech.DM2020.BigNum;

import java.util.*;

public class Basis
{
	public ArrayList<BigPolinom> polynoms = new ArrayList<BigPolinom>();		//Базис Грёбнера
	private ArrayList<BigPolinom> basePolynoms = new ArrayList<BigPolinom>();	//Исходные уравнения
	private ArrayList<String> linked = new ArrayList<String>();	//Записываем номера тех многочленов, с которыми уже строили S полином
	
	
	
	private int maxpower;														//Кол-во неизвестных
	private BigPolinom decision;
	
	public void clearBasis()
	{
		while(basePolynoms.size() > 0)
			basePolynoms.remove(0);
		while(polynoms.size() > 0)
			polynoms.remove(0);
	}
	
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
		//removeDivided();
		//output();
	}
	
	public void outputBase(int type)
	{
		Scanner in = new Scanner(System.in);
		int i;
		String buffS;
		if(this.basePolynoms.size() != 0)
		{
			System.out.println("Количество уравнений: " + this.basePolynoms.size());
			for(i = 0; i < this.basePolynoms.size(); i++)
			{
				buffS = this.basePolynoms.get(i).toString();
				if(type != 0)
				{
					buffS = buffS.replace("x1", "x");
					buffS = buffS.replace("x2", "y");
					buffS = buffS.replace("x3", "z");
				}
				System.out.println(buffS + "\n");
			}
		}
		else
			System.out.println("Вы не вводили уравнения. Введите input(или in), чтобы ввести их.");
	}
	
	public void output(int type)
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
			if(type != 0)
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
		//sPolynom2();
		if(polynoms.size() < 3)// basePolynoms.size())
			sPolynom();
		/*while(f)
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
			//System.out.println(f);
		}*/
		//while(simple3());
		while(f)
		{
			while(sPolynom2());
			//sPolynom2();
			while(simple3());
			f = sPolynom2();
		}
		removeEquals();
	}
	
	private void removeDivided()
	{
		int i,j;
		for(i = 0; i < this.polynoms.size(); i++)
		{
			for(j = i+1; j < this.polynoms.size(); j++)
			{
				if(this.polynoms.get(i).getHighMonom().isDivided(this.polynoms.get(j).getHighMonom()))
				{
					this.polynoms.remove(i);
					linked.remove(i);
					i = 0;
					j--;
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

	private boolean simple3()
	{
		boolean f = false;
		BigPolinom buff;
		int i = 0;
		do				//Упрощаем базисы
		{
			buff = this.polynoms.get(i).reduce2(this.polynoms);	//reduce2
			if(!(buff.equals2(this.polynoms.get(i))))
			{
				if(buff.isZero())
					this.polynoms.remove(i);
				else
				{
					this.polynoms.set(i, buff);
				}
				i--;
				if(!f && !buff.isZero())
					f = true;
			}
			i++;
			System.out.println(i + "/" + this.polynoms.size());
		} while(i < this.polynoms.size());
		newLinkList();
		/*for(i = 0; i < polynoms.size(); i++)
			this.polynoms.get(i).divideByHighCoef();*/
		return f;
	}
	
	private void sPolynom()
	{
		Integer i,j;
		for(i = 0; i < this.basePolynoms.size(); i++)
			for(j = i+1; j < this.basePolynoms.size(); j++)
			{
				if(!this.isLinked(i,j))
				{
					this.basePolynoms.get(i).sPolynom( this.basePolynoms.get(j) ).reduce(this.polynoms);
					linked.add("");
				}
			}
	}
	
	private boolean sPolynom2()
	{
		boolean f = false, temp;
		Integer i,j,k;
		BigPolinom buff;
		System.out.println("size : " + this.polynoms.size());
		for(i = 0; i < this.polynoms.size(); i++)
			for(j = 0; j < this.polynoms.size(); j++)
			//for(j = i+1; j < this.polynoms.size(); j++)
			//for(j = this.polynoms.size()-1; j > 0; j--)
			{
				temp = false;
				//if(i != j && !this.isLinked(i,j) && triangle(i,j) && !this.polynoms.get(i).getHighMonom().gcd(this.polynoms.get(j).getHighMonom()).isConst())
				if(i != j && !this.isLinked(i,j) && !this.polynoms.get(i).getHighMonom().gcd(this.polynoms.get(j).getHighMonom()).isConst())
				{
					//buff = this.polynoms.get(i).sPolynom2( this.polynoms.get(j) );
					buff = this.polynoms.get(i).sPolynom( this.polynoms.get(j) );
					if(!buff.isZero() && !isReducedToZero(buff))
						temp = buff.reduce(this.polynoms);
					if(!this.isLinked(i,j))
						addLink(i,j);
					if(temp)
					{
						//if(i != 0) i--;
						//j = this.polynoms.size();
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
	
	private boolean isReducedToZero(BigPolinom ths)
	{
		boolean f;
		int i,j;
		BigMonom buff = ths.getHighMonom();
		for(i = 0; i < this.polynoms.size(); i++)
			for(j = i+1; j < this.polynoms.size(); j++)
				if(i != j)
					if(polynoms.get(i).getHighMonom().lcm(polynoms.get(j).getHighMonom()).isDivided(buff) && isLinked(i,j))
						return true;
		return false;
	}
	
	private boolean triangle(int ths, int other)
	{
		int i;
		BigMonom buff = this.polynoms.get(ths).getHighMonom().lcm(this.polynoms.get(other).getHighMonom());
		for(i = 0; i < this.polynoms.size(); i++)
		{
			if(i != ths && i != other && this.polynoms.get(i).getHighMonom().isDivided(buff))
				if(isLinked(i, ths) && isLinked(i, other))
				{
					System.out.println("YES");
					addLink(ths,other);
					return false;
				}
		}
		return true;
	}
	
	private void newLinkList()
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
		BigPolinom end = new BigPolinom(this.polynoms.get(0).getFactors().get(0).getPowers().size(), "0");
		for(int i=0; i < this.basePolynoms.size(); i++)
		{
			buf = this.basePolynoms.get(i);
			for(int j=0; j < this.polynoms.size(); j++)
			{
				if (!buf.divide(this.polynoms.get(j)).isZero())
				{
					if(buf.mod(this.polynoms.get(j)).isZero())
					{
						break;
					}
					buf=buf.mod(this.polynoms.get(j));
				}
			}
			if(buf.onlyOne()) {
				end = end.add(buf);
			}
			decision = end.clone();
		}
	}
	
	public void outputDecision()
	{
		if(decision == null)
			decision();
		System.out.println(decision);
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