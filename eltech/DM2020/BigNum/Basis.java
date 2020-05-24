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
	private ArrayList<Integer> mode;											//режим сортировки
	private boolean changed;													//Была ли смена сортировки
	private long time;															//Потраченное время
	private long start;															//Время начала
	private static final int LIMIT = 60;										//Предел рассчета
	
	/**
    * Очистка базиса и исходной системы
    *
    * @version 1
    * @author 
    */
	public void clearAll()
	{
		while(basePolynoms.size() > 0)
			basePolynoms.remove(0);
		while(polynoms.size() > 0)
			polynoms.remove(0);
	}
	
	/**
    * Очистка базиса
    *
    * @version 1
    * @author 
    */
	private void clearBasis()
	{
		int i;
		while(this.polynoms.size() > 0)
			this.polynoms.remove(0);
		for(i = 0; i < this.basePolynoms.size(); i++)
		{
			this.basePolynoms.get(i).sort();
			this.polynoms.add(this.basePolynoms.get(i).clone());
		}
	}
	
	/**
    * Добавление полинома в исходную систему полиномов
	*
	* @param String newPolynom - Новый полином для добавления
    *
    * @version 1
    * @author 
    */
	public void addBasis(String newPolynom)
	{
		//polynoms.add( new BigPolinom(maxpower, newPolynom) );
		basePolynoms.add( new BigPolinom(maxpower, newPolynom) );
	}
	
	/**
    * Установление максимального количества неизвестных в системе
    *
	* @param int power - кол-во неизвестных
	*
    * @version 1
    * @author 
    */
	public void setMaxPower(int power)
	{
		maxpower = power;
	}
	
	/**
    * Запуск алгоритма Бухбергера
    *
    * @version 1
    * @author 
    */
	public void doActions()
	{
		int i;
		//settingMode();
		changed = false;
		clearBasis();
		//changeMode();
		Buhberger();
		//removeDivided();
	}
	
	/**
    * Вывод исходной системы
	*
	* @param int type - режим отображения неизвестных. Если один, то выводит x,y,z, иначе x1,x2,x3
    *
    * @version 1
    * @author 
    */
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
	
	/**
    * Вывод базиса
	*
	* @param int type - режим отображения неизвестных. Если один, то выводит x,y,z, иначе x1,x2,x3
    *
    * @version 1
    * @author 
    */
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
	
	/**
    * Алгоритм Бухбергера
    *
    * @version 1
    * @author 
    */
	private void Buhberger()
	{
		start = System.nanoTime();
		time = 0;
		int i;
		newLinkList();
		boolean f = false;
		//if(polynoms.size() < 3)// basePolynoms.size())
			//sPolynom();
		do
		{
			while(sPolynom2());
			//output(0);
			//removeDivided();
			while(simple3());
			f = sPolynom3();
			//output(0);
			time = TimeUnit.SECONDS.convert(System.nanoTime()-start, TimeUnit.NANOSECONDS);
			if(time > LIMIT)
				f = false;
		} while(f);
		if(time > LIMIT)
		{
			System.out.println("Изменен режим сортировки, базис примет более приятный вид после просчета");
			changeMode();
			changed = false;
			Buhberger();
		}
	}
	
	/**
    * Удаление из базиса делящихся по старшим членам полиномов
    *
    * @version 1
    * @author 
    */
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
	
	/**
    * Удаление из базиса одинаковых полиномов
    *
    * @version 1
    * @author 
    */
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
	
	/**
    * Упрощение базиса через редукцию
    *
	* @return true - если базис был упрощен, иначе false
	*
    * @version 1
    * @author 
    */
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
	
	/**
    * Построение S-полиномов для пар с зацеплением
    *
	* @return true - была построен минимум один S-полином, который был добавлен в базис, иначе - false
	*
    * @version 1
    * @author 
    */
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
				if(i != j && !this.isLinked(i,j) && !this.polynoms.get(i).getHighMonom().gcd(this.polynoms.get(j).getHighMonom()).isConst() && triangle(i,j))		//Критерии не работают!! Только хуже!
				//if(i != j && !this.isLinked(i,j) && !this.polynoms.get(i).getHighMonom().gcd(this.polynoms.get(j).getHighMonom()).isConst())
				//if(i != j && !this.isLinked(i,j))
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
	
	/**
    * Построение S-полиномов без зацепления
    *
	* @return true - была построен минимум один S-полином, который был добавлен в базис, иначе - false
	*
    * @version 1
    * @author 
    */
	private boolean sPolynom3()
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
	
	/**
    * Проверка на то, были ли построены S-полиномы для пары полиномов
	*
	* @param Integer ths - номер первого полинома в системе
	* @param Integer other - номер второго полинома в системе
    *
	* @return true - S-полином был построен, иначе - false
	*
    * @version 1
    * @author 
    */
	private boolean isLinked(Integer ths, Integer other)		//Проверка на связку
	{
		String buffS = "," + other.toString() + "$";
		String buffLink = linked.get(ths);
		if(buffLink.indexOf(buffS) == -1)
			return false;
		return true;
	}
	
	/**
    * Добавление связки между двумя полиномами(то, что с ними были построен S-полином)
	*
	* @param Integer ths - номер первого полинома в системе
	* @param Integer other - номер второго полинома в системе
	*
    * @version 1
    * @author 
    */
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
	
	/**
    * Проверка на то, редуцируется ли S-полином к нулю
	*
	* @param Integer ths - номер первого полинома в системе
	* @param Integer other - номер второго полинома в системе
    *
	* @return true - S-полином редукцируется к нулю, иначе - false
	*
    * @version 1
    * @author 
    */
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
	
	/**
    * Правило треугольников
	*
	* @param int ths - номер первого полинома в системе
	* @param int other - номер второго полинома в системе
    *
	* @return true - S-полином можно редукцировать, иначе - false
	*
    * @version 1
    * @author 
    */
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
	
	/**
    * Создание пустого списка связок между полиномами
	*
    * @version 1
    * @author 
    */
	private void newLinkList()
	{
		int i;
		while(linked.size() > 0)
			linked.remove(0);
		for(i = 0; i < polynoms.size(); i++)
			linked.add("");
	}
	
	/**
    * Сведение исходной системы уравнений к уравнению с одной неизвестной
	*
    * @version 1
    * @author 
    */
	public void decision()
	{
		int i = 0;
		BigPolinom buf;
		BigPolinom end = new BigPolinom(this.polynoms.get(0).getFactors().get(0).getPowers().size(), "0");
		BigPolinom tmp = new BigPolinom(this.polynoms.get(0).getFactors().get(0).getPowers().size(), "0");
		BigPolinom reset = new BigPolinom(this.polynoms.get(0).getFactors().get(0).getPowers().size(), "0");
		BigPolinom dec = new BigPolinom(this.polynoms.get(0).getFactors().get(0).getPowers().size(), "0");

		boolean simple = true;
		int k = 1;
		//System.out.println(this.basePolynoms.size() + ":"+ i);
		if(clearPowers()) {
			do {
				end = reset;
				//System.out.println(this.basePolynoms.size() + ":" + i);
				k = 1;
				buf = this.basePolynoms.get(i);
				//System.out.println("Само уравнение");
				//System.out.println(buf);

				//System.out.println("Пока");
				for (int j = 0; j < this.polynoms.size(); j++) {
					//System.out.println("Вошли в цикл");
					if (!buf.divide(this.polynoms.get(j)).isZero()) {
						//System.out.println("Делится");
						if (buf.mod(this.polynoms.get(j)).isZero()) {
							//System.out.println("Нулевой остаток");
						} else {
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
					//System.out.println("Содержит только одну переменную");
					end = end.add(buf);
				}
				else {
					//System.out.println("Содержит несколько");
					if (buf.add(buf.getHighMonom().toBigPolinom().multiplyByMinusOne()).toString() == "0") {
						//System.out.println("Моном");
						end = end.add(buf);
					}
					else {
						end = reset;
					}
				}
				//System.out.println("Конец");
				//System.out.println(end);
				if (!end.isZero() && simple) {
					dec = end.clone();
					simple = false;
				}
				if (end.toString() != "0") {
					if (!dec.getHighMonom().toBigPolinom().divide(end.getHighMonom().toBigPolinom()).isZero()) {
						dec = end.clone();
					}
				}

				i++;
			} while ((i < this.basePolynoms.size()));
			decision = dec.clone();
		}
		else{
			System.out.println("Уравнение имеет бесконечное количество решений.");
		}
	}
	
	/**
    * Вывод уравнения, полученного в decision
	*
	* @param int type - режим отображения неизвестных. Если один, то выводит x,y,z, иначе x1,x2,x3
    *
    * @version 1
    * @author 
    */
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
		System.out.print("Количество решений: ");
		System.out.println(decision.getHighMonom().getHighPower());
	}
	
	/**
    * Удаление старого решения системы
	*
    * @version 1
    * @author 
    */
	public void clearDec()
	{
		decision = null;
	}
	
	
	/**
    * Проверка на то, что в системе встречаются чистые степени неизвестных
	*
	* @return true - встречены чистые степени ВСЕХ неизвестных, иначе - false
    *
    * @version 1
    * @author 
    */
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
	
	/**
    * Установка упорядочевания по следующему методу:
	* Старшинство строится по возрастанию максимальной степени неизвестной в системе
	* На примере полинома:
	* x^3+y^7+z
	* Тут по возрастанию степени порядок будет такой: z,x,y
	* Упорядоченный полином:
	* z+x^3+y^7
    *
    * @version 1
    * @author 
    */
	public void settingMode()
	{
		int i, j;
		ArrayList<Integer> toMaxPowers = new ArrayList<Integer>();
		ArrayList<Integer> buffMaxPowers = new ArrayList<Integer>();
		i = this.basePolynoms.get(0).getFactors().get(0).getPowers().size();
		while(toMaxPowers.size() != i)
			toMaxPowers.add(0);
		for(i = 0; i < this.basePolynoms.size(); i++)
		{
			buffMaxPowers = this.basePolynoms.get(i).getMaxPowers();
			for(j = 0; j < toMaxPowers.size(); j++)
				if(buffMaxPowers.get(j) > toMaxPowers.get(j))
					toMaxPowers.set(j, buffMaxPowers.get(j));
		}
		for(i = 0; i < this.basePolynoms.size(); i++)
			this.basePolynoms.get(i).setMode(toMaxPowers);
		buffMaxPowers = this.basePolynoms.get(0).getMode();
		this.mode = buffMaxPowers;
		System.out.print("Упорядочевание:");
		for(i = 0; i < buffMaxPowers.size(); i++)
			System.out.print(" x" + (buffMaxPowers.get(i)+1));
		System.out.println("");
	}
	
	/**
    * Установка нового упорядочевания по следующему методу:
	* Вторая(на момент смены) неизвестная становится по старшенству последней
    *
    * @version 1
    * @author 
    */
	private void changeMode()
	{
		int i, buff = -1;
		for(i = 1; i < this.mode.size()-1; i++)
		{
			if(i == 1)
				buff = this.mode.get(i);
			this.mode.set(i, this.mode.get(i+1));
		}
		this.mode.set(i, buff);
		for(i = 0; i < this.basePolynoms.size(); i++)
			this.basePolynoms.get(i).setMode(this.mode);
		clearBasis();
		System.out.print("Упорядочевание:");
		for(i = 0; i < this.mode.size(); i++)
			System.out.print(" x" + (this.mode.get(i)+1));
		System.out.println("");
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