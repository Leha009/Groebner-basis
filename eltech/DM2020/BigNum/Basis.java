package eltech.DM2020.BigNum;

import java.util.*;
import java.util.concurrent.TimeUnit;	//для отсчитывания секунд

public class Basis
{
	public ArrayList<BigPolinom> polynoms = new ArrayList<BigPolinom>();		//Базис Грёбнера
	private ArrayList<BigPolinom> basePolynoms = new ArrayList<BigPolinom>();	//Исходные уравнения
	private ArrayList<String> linked = new ArrayList<String>();	//Записываем номера тех многочленов, с которыми уже строили S полином
	private int maxpower;														//Кол-во неизвестных
	private BigPolinom decision;
	private int mode;															//режим сортировки: 0 - лексический, 1 - обратный лексический
	private boolean changed;													//Была ли смена сортировки
	private long time;															//Потраченное время
	private long start;															//Время начала
	private static final int LIMIT = 60;										//Предел рассчета
	
	public void clearAll()
	{
		while(basePolynoms.size() > 0)
			basePolynoms.remove(0);
		while(polynoms.size() > 0)
			polynoms.remove(0);
	}
	
	private void clearBasis()
	{
		int i;
		while(this.polynoms.size() > 0)
			this.polynoms.remove(0);
		for(i = 0; i < this.basePolynoms.size(); i++)
		{
			this.basePolynoms.get(i).setMode(mode);
			this.basePolynoms.get(i).sort();
			this.polynoms.add(this.basePolynoms.get(i).clone());
		}
	}
	
	public void addBasis(String newPolynom)
	{
		polynoms.add( new BigPolinom(maxpower, newPolynom, mode) );
		basePolynoms.add( new BigPolinom(maxpower, newPolynom, mode) );
	}
	
	public void setMaxPower(int power)
	{
		maxpower = power;
	}
	
	public void setMode(int curMode)
	{
		mode = curMode;
	}
	
	public void doActions()
	{
		changed = false;
		clearBasis();
		Buhberger();
		removeDivided();
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
		start = System.nanoTime();
		time = 0;
		int i;
		newLinkList();
		boolean f = false;
		if(polynoms.size() < 3)// basePolynoms.size())
			sPolynom();
		if(!changed)
		{
			do
			{
				while(sPolynom2());
				//output(1);
				while(simple3());
				//output(1);
				f = sPolynom2();
				time = TimeUnit.SECONDS.convert(System.nanoTime()-start, TimeUnit.NANOSECONDS);
				if(time > LIMIT)
					f = false;
			} while(f);
			if(time > LIMIT)
			{
				System.out.println("Изменен режим сортировки, базис примет более приятный вид после просчета");
				mode = mode^1;
				clearBasis();
				changed = true;
				Buhberger();
			}
		}
		else
		{
			do
			{
				while(sPolynom2());
				while(simple3());
				f = sPolynom2();
			} while(f);
			//removeEquals();
		}
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
					//System.out.println("This " + this.polynoms.get(i) + "\nOther " + this.polynoms.get(j));
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
		//System.out.println("simpe");
		do				//Упрощаем базисы
		{
			if(!changed)
			{
				time = TimeUnit.SECONDS.convert(System.nanoTime()-start, TimeUnit.NANOSECONDS);
				if(time > LIMIT)
					return false;
			}
			buff = this.polynoms.get(i).reduce2(this.polynoms, start, changed);	//reduce2
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
			//System.out.println(i + "/" + this.polynoms.size());
		} while(i < this.polynoms.size());
		newLinkList();
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
					this.basePolynoms.get(i).sPolynom( this.basePolynoms.get(j) ).reduce(this.polynoms, start, changed);
					linked.add("");
				}
			}
	}
	
	private boolean sPolynom2()
	{
		boolean f = false, temp;
		Integer i,j,k;
		BigPolinom buff;
		//System.out.println("size : " + this.polynoms.size());
		for(i = 0; i < this.polynoms.size(); i++)
			for(j = 0; j < this.polynoms.size(); j++)
			{
				if(!changed)
				{
					time = TimeUnit.SECONDS.convert(System.nanoTime()-start, TimeUnit.NANOSECONDS);
					if(time > LIMIT)
						return false;
				}
				temp = false;
				//if(i != j && !this.isLinked(i,j) && !this.polynoms.get(i).getHighMonom().gcd(this.polynoms.get(j).getHighMonom()).isConst() && triangle(i,j))		Критерии не работают!! Только хуже!
				//if(i != j && !this.isLinked(i,j) && !this.polynoms.get(i).getHighMonom().gcd(this.polynoms.get(j).getHighMonom()).isConst())
				if(i != j && !this.isLinked(i,j))
				{
					//buff = this.polynoms.get(i).sPolynom2( this.polynoms.get(j) );
					buff = this.polynoms.get(i).sPolynom( this.polynoms.get(j) );
					if(!buff.isZero() && !isReducedToZero(buff))
						temp = buff.reduce(this.polynoms, start, changed);
					if(!this.isLinked(i,j))
						addLink(i,j);
					if(temp)
						linked.add("");
					if(!f)
						f = temp;
					//System.out.println("SPoly: " + i + " : " + j + " size:" + this.polynoms.size());
				}
			}
		//System.out.println("POLY " + f);
		return f;
	}
	
	private boolean isLinked(Integer ths, Integer other)		//Проверка на связку
	{
		String buffS = "," + other.toString() + "$";
		String buffLink = linked.get(ths);
		if(buffLink.indexOf(buffS) == -1)
			return false;
		return true;
	}
	
	private void addLink(Integer ths, Integer other)
	{
		String buffS = "," + other.toString() + "$";
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
					//System.out.println("YES");
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
		int i = 0;
		BigPolinom buf;
		BigPolinom end = new BigPolinom(this.polynoms.get(0).getFactors().get(0).getPowers().size(), "0");
		BigPolinom tmp = new BigPolinom(this.polynoms.get(0).getFactors().get(0).getPowers().size(), "0");
		BigPolinom reset = new BigPolinom(this.polynoms.get(0).getFactors().get(0).getPowers().size(), "0");
		int k = 1;
		//System.out.println(this.basePolynoms.size() + ":"+ i);
		do {
			//System.out.println(this.basePolynoms.size() + ":"+ i);
			k = 1;
			buf = this.basePolynoms.get(i);
			//System.out.println("Само уравнение");
			//System.out.println(buf);
			while (k == 1) {
				//System.out.println("Пока");
				for (int j = 0; j < this.polynoms.size(); j++) {
					//System.out.println("Вошли в цикл");
					if (!buf.divide(this.polynoms.get(j)).isZero()) {
						//System.out.println("Делится");
						if (buf.mod(this.polynoms.get(j)).isZero()) {
							//System.out.println("Нулевой остаток");
						}
						else {
							buf = buf.mod(this.polynoms.get(j));
							//System.out.println("Поделили");
							j = 0;
						}
					}
					//System.out.println("Внутри цикла");
					//System.out.println(buf);
				}
				//System.out.println("Что должны добавить");
				//System.out.println(buf);
				if (buf.onlyOne()) {
					k = 0;
					//System.out.println("Содержит только одну переменную");
					tmp = end.add(buf.getHighMonom().toBigPolinom());
					if(tmp.onlyOne()) {
						end = end.add(buf);
					}
					else{
						end = reset;
						k = 0;
					}
				}
				else {
					if(buf.getHighMonom().toBigPolinom().onlyOne()) {
						tmp = end.add(buf.getHighMonom().toBigPolinom());
						if(tmp.onlyOne()) {
							end = end.add(buf.getHighMonom().toBigPolinom());
							//System.out.println("Добавили только старший член");
							//System.out.println(end);
							buf = buf.add(buf.getHighMonom().toBigPolinom().multiplyByMinusOne());
							//System.out.println("Отняли старший член");
							//System.out.println(buf);
						}
						else{
							end = reset;
							k = 0;
						}
						tmp = reset;
					}
					else{
						end = reset;
						k = 0;
					}
				}
				//System.out.println("Конец");
				//System.out.println(end);
			}
			i++;
		}while((end.toString() == "0") && (i < this.basePolynoms.size()));
		decision = end.clone();
	}
	
	public void outputDecision(int type)
	{
		String buffS;
		if(decision == null)
			decision();
		buffS = decision.toString();
		if(type != 0)
		{
			buffS = buffS.replace("x1", "x");
			buffS = buffS.replace("x2", "y");
			buffS = buffS.replace("x3", "z");
		}
		System.out.println(buffS + "\n");
	}
	
	public void clearDec()
	{
		decision = null;
	}
	
	private boolean clearPowers()
	{
		int i, power;
		power = this.polynoms.get(0).getHighMonom().getPowers().size();
		ArrayList<Integer> powers = new ArrayList<Integer>();
		for(i = 0; i < power; i++)
			powers.add(0);
		for(i = 0; i < this.polynoms.size(); i++)
		{
			power = this.polynoms.get(i).getHighMonom().clearPower();
			if(power != -1)
				powers.set(power, 1);
		}
		for(i = 0; i < powers.size(); i++)
		{
			if(powers.get(i) == 0)
				return false;
		}
		return true;
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