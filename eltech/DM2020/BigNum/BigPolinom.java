package eltech.DM2020.BigNum;

import java.util.*;
import java.util.concurrent.TimeUnit;	//для отсчитывания секунд

/**
* Класс, который позволяет манипулировать с полиномами с рациональными коэффициентами
* @version 0.01
*/
public class BigPolinom
{
	private ArrayList<BigMonom> factors = new ArrayList<BigMonom>();
	private int mode;															//режим сортировки: 0 - лексический, 1 - обратный лексический

	private BigPolinom() {}

	/**
	* Конструктор, с помощью которого можно инициализировать полином
	* Если строка пустая или null, то бросит исключение
	* Может принять строку такого вида:
	* 27x_1^3 + 7x2 + 3
	*
	* @param String src - представление полинома в виде строки
	*
	* @version 1
	* @author 
	*/
	public BigPolinom(int amount, String src, int curMode)
	{
		mode = curMode;
		int i;
		String[] str;
		if(src == null)
			throw new IllegalArgumentException("Неверный аргумент: строка не может быть не инициализированной\n");
		if(src.equals(""))
			throw new IllegalArgumentException("Неверный аргумент: строка не может быть пустой\n");
		src = src.trim();
		src = src.replace("*", "");
		src = src.replace(")", "");
		src = src.replace("(", "");
		src = src.replace("-", "+-");
		src = src.replace("++", "+");
		src = src.replace("/+", "/");
		src = src.replace("_", "");
		if(src.indexOf("+") == 0)
			src = src.substring(1,src.length());
		str = src.split("[+]");
		for(i = 0; i < str.length; i++)
		{
			factors.add(new BigMonom(amount, str[i].trim(), mode));
		}
		this.sort();
	}
	
	public BigPolinom(int amount, String src)		//для констант
	{	
		int i;
		String[] str;
		if(src == null)
			throw new IllegalArgumentException("Неверный аргумент: строка не может быть не инициализированной\n");
		if(src.equals(""))
			throw new IllegalArgumentException("Неверный аргумент: строка не может быть пустой\n");
		src = src.trim();
		src = src.replace("*", "");
		src = src.replace(")", "");
		src = src.replace("(", "");
		src = src.replace("-", "+-");
		src = src.replace("++", "+");
		src = src.replace("/+", "/");
		src = src.replace("_", "");
		if(src.indexOf("+") == 0)
			src = src.substring(1,src.length());
		str = src.split("[+]");
		for(i = 0; i < str.length; i++)
		{
			factors.add(new BigMonom(amount, str[i].trim(),mode));
		}
	}

	/**
	* Вывод полинома в виде строки
	* Выводиться должно так:
	* 27x1^3 + 7x2 + 3
	*
    * @return String - представление полинома в виде строки
	*
	* @version 1
	* @author
	*/
	@Override
	public String toString()
	{
		int i;
		String buffS = "";
		for(i = 0; i < factors.size(); i++)
			if(!factors.get(i).isZero()) buffS += factors.get(i).toString()+" + ";
		buffS = buffS.replace("+ -", "- ");
		return buffS.equals("") ? "0" : buffS.substring(0, buffS.length() - 3);
	}

	/**
	* Проверяет является ли многочлен нулём
	*
    * @return boolean - true, если числитель многочлен имеет степень 0 и коэффициент при нулевой степени равен нулю; иначе false
	*
	* @version 1
	* @author
	*/
	public boolean isZero()
	{
		if(this.factors.size() == 0)
			return true;
        return this.factors.get(0).isZero();
	}

    /**
    * Сравнение двух полиномов
    *
    * @param BigPolinom other - второй полином для сравнения с исходным
    * @return int - 0 если степени полиномов равны, -1 если степень исходного полинома меньше other, иначе 1
    *
    * @version 1
    * @author
    */
    public int compareTo(BigPolinom other)
    {
		if(this.isZero() || other.isZero())
			return this.factors.size() >= other.factors.size() ? (this.factors.size() == other.factors.size() ?  0 : 1 ) : -1 ;
		/*if(mode != 0)
			return this.factors.get(0).compareTo2( other.factors.get(0) );*/
		return this.factors.get(0).compareTo( other.factors.get(0) );
    }

	private boolean isMoreThan(BigPolinom other)	//this имеет степень старше other
    {
		return this.compareTo(other) > 0 ? true : false;
    }
	
	private boolean isMoreOrEquals(BigPolinom other)	//this имеет степень старше или равной other
    {
		return this.compareTo(other) >= 0 ? true : false;
    }
	
	private boolean isLessThan(BigPolinom other)	//this имеет степень меньше other
    {
		return this.compareTo(other) < 0 ? true : false;
    }
	
	private boolean isLessOrEquals(BigPolinom other)	//this имеет степень меньше или равной other
    {
		return this.compareTo(other) <= 0 ? true : false;
    }
	
	private boolean isEquals(BigPolinom other)	//this имеет степень равной other
    {
		return this.compareTo(other) == 0 ? true : false;
    }
	
	public boolean equals2(BigPolinom other)	//this имеет степень равной other
    {
		if(this.toString().equals(other.toString()))
			return true;
		return false;
    }
	
	/**
    * Клонирование объекта
	*
    * @return копию BigPolinom
    *
    * @version 1
    * @author
    */
	@Override
	public BigPolinom clone()
	{
		int i,n;
		BigPolinom result = new BigPolinom();
		n = this.factors.size();
		for(i = 0; i < n; i++)
			result.factors.add(this.factors.get(i).clone());
		result.mode = this.mode;
		return result;
	}

	/**
    * Сложение полиномов
	*
	* @param BigPolinom other - второй полином, который прибавляется
	* @param int mode - режим сортировки: 1 - обратный лексический, 0 - лексический
	*
    * @return BigPolinom result - результат сложения
    *
    * @version 1
    * @author 
    */
	public BigPolinom add(BigPolinom other)
	{
		int i,n,index;
		BigPolinom result = this.clone();
		BigPolinom buffOther = other.clone();
		BigQ resultCoef;	//коэффициент в результирующем мономе
		BigQ otherCoef;		//коэффициент монома в other
		//if(buffOther.factors.size() > result.factors.size())
			n = buffOther.factors.size();
		//else
		//	n = result.factors.size();
		for(i = 0; i < n; i++)
		{
			index = this.monomIndex( buffOther.factors.get(i) );		//Смотрим, есть ли уже моном из other в this
			if(index != -1)												//Если есть, то складываем коэффициенты
			{
				resultCoef = result.factors.get(index).getCoef();
				otherCoef = buffOther.factors.get(i).getCoef();
				result.factors.get(index).setCoef( resultCoef.add(otherCoef).reduce() );
			}
			else														//Иначе просто добавляем моном в полином
				result.factors.add( buffOther.factors.get(i) );
		}
		result.sort();
        return result;
	}

	/**
    * Вычитания полиномов
	*
	* @param BigPolinom other - второй полином, который вычитания
	*
    * @return BigPolinom result - результат вычитания
    *
    * @version 1
    * @author 
    */
	public BigPolinom subtract(BigPolinom other)
	{
        int i,n,index;
		BigPolinom result = this.clone();
		BigPolinom buffOther = other.clone();
		BigQ resultCoef;
		BigQ otherCoef;
		BigQ minusOne = new BigQ("-1/1");
		//if(buffOther.factors.size() > result.factors.size())
			n = buffOther.factors.size();
		//else
		//	n = result.factors.size();
		for(i = 0; i < n; i++)
		{
			index = this.monomIndex( buffOther.factors.get(i) );
			if(index != -1)
			{
				resultCoef = result.factors.get(index).getCoef();
				otherCoef = buffOther.factors.get(i).getCoef();
				result.factors.get(index).setCoef( resultCoef.subtract(otherCoef) );
			}
			else
			{
				buffOther.factors.get(i).setCoef( buffOther.factors.get(i).getCoef().multiply(minusOne));
				result.factors.add( buffOther.factors.get(i) );
			}
		}
		result.sort();
        return result;
	}

	/**
    * Умножение полиномов
	*
	* @param BigPolinom other - второй полином, на который умножается исходный
	*
    * @return BigPolinom result - результат умножения
    *
    * @version 1
    * @author 
    */
	public BigPolinom multiply(BigPolinom other)
	{
		if(this.isZero())
			return new BigPolinom(other.factors.get(0).getPowers().size(), "1");
		else if(other.isZero())
			return new BigPolinom(this.factors.get(0).getPowers().size(), "1");
        int i,j,index;
		String buffS = "0";
        BigPolinom result = new BigPolinom();
		BigPolinom buffThis = new BigPolinom(this.factors.get(0).getPowers().size(), "1");
        BigPolinom buffOther = new BigPolinom(other.factors.get(0).getPowers().size(), "1");
		BigQ resultCoef, otherCoef;
        for(i = 0; i < this.factors.size(); i++)
            for(j = 0; j < other.factors.size(); j++)
			{
				buffThis.factors.set(0, this.factors.get(i) );
				buffOther.factors.set(0, other.factors.get(j) );
				buffThis.factors.set(0, buffThis.factors.get(0).multiply(buffOther.factors.get(0)) );
				index = result.monomIndex( buffThis.factors.get(0) );
				if(index != -1)
				{
					resultCoef = result.factors.get(index).getCoef();
					otherCoef = buffThis.factors.get(0).getCoef();
					result.factors.get(index).setCoef( resultCoef.add(otherCoef) );
				}
				else
				{
					result.factors.add( buffThis.factors.get(0) );
				}
			}
		result.sort();
        return result;
	}
	
	/**
    * Умножение полинома на моном
	*
	* @param BigPolinom other - моном, на который умножается полином
	*
    * @return BigPolinom result - результат умножения
    *
    * @version 1
    * @author 
    */
	/*public BigPolinom multiply2(BigMonom other)	//Умножение на моном старое
	{
        int i,j,index;
		String buffS = "0";
        BigPolinom result = new BigPolinom();
		BigPolinom buffThis = new BigPolinom(this.factors.get(0).getPowers().size(), "1");
        BigMonom buffOther = other.clone();
		BigQ resultCoef, otherCoef;
        for(i = 0; i < this.factors.size(); i++)
		{
			buffThis.factors.set(0, this.factors.get(i) );
			buffThis.factors.set(0, buffThis.factors.get(0).multiply(buffOther) );
			index = result.monomIndex( buffThis.factors.get(0) );
			if(index != -1)
			{
				resultCoef = result.factors.get(index).getCoef();
				otherCoef = buffThis.factors.get(0).getCoef();
				result.factors.get(index).setCoef( resultCoef.add(otherCoef).reduce() );
			}
			else
			{
				result.factors.add( buffThis.factors.get(0) );
			}
		}
		result.sort2();
        return result;
	}*/
	
	public BigPolinom multiply(BigMonom other)	//Умножение на моном
	{
        int i,j,index;
		String buffS = "0";
        BigPolinom result = this.clone();
        for(i = 0; i < this.factors.size(); i++)
			result.factors.set(i, this.factors.get(i).multiply(other));
        return result;
	}
	
	/**
    * Умножение полинома на -1
	*
    * @return BigPolinom result - результат умножения
    *
    * @version 1
    * @author 
    */
	public BigPolinom multiplyByMinusOne()
	{
        BigPolinom result = this.clone();
		BigQ minusOne = new BigQ("-1/1");
		int i,j;
		for(i = 0; i < result.factors.size(); i++)
			result.factors.get(i).multiplyByMinusOne();
        return result;
	}
	
	/**
    * Деление полиномов
	*
	* @param BigPolinom other - делитель
	*
    * @return частное и остаток
    *
    * @version 1
    * @author 
    */
	public Case divideUniversal(BigPolinom other) throws IllegalArgumentException
	{
		if(other.isZero())
			throw new IllegalArgumentException("Делить на 0 нельзя\n");
		BigPolinom result = new BigPolinom();
		BigPolinom buffThis = this.clone();
		BigPolinom buffOther = other.clone();
		BigMonom multiplier;
		BigPolinom zero = new BigPolinom(this.factors.get(0).getPowers().size(), "0");
		if(buffThis.isLessThan(buffOther))
			return new Case(zero, buffThis);
		
		if(!this.isDivided(other))
			return new Case(zero, buffThis);
		while(buffThis.isMoreOrEquals(buffOther))
		{
			multiplier = buffOther.getHighMonom().getMultiplier(buffThis.getHighMonom());		//Здесь получаем моном, на который нужно умножить старший член buffOther, чтобы получить старший член buffThis
			result.factors.add(multiplier);														//В частное добавляем этот моном
			buffThis = buffThis.subtract( buffOther.multiply(multiplier) );					//Вычитаем из buffThis полином buffOther, умноженный на multiplier
			/*if(!buffThis.isZero())	//убрать?
				buffThis.divideByHighCoef();*/
		}
		//result.sort2();
		return new Case(result, buffThis);		//Частное, остаток
	}
	
	/**
    * Частное от деления полиномов
	*
	* @param BigPolinom other - делитель
	*
    * @return result - частное
    *
    * @version 1
    * @author 
    */
	public BigPolinom divide(BigPolinom other)
	{
		return this.divideUniversal(other).getFirst();
	}
	
	/**
    * Остаток от деления полиномов
	*
	* @param BigPolinom other - делитель
	*
    * @return buffThis - частное
    *
    * @version 1
    * @author 
    */
	public BigPolinom mod(BigPolinom other)
	{
		return this.divideUniversal(other).getSecond();
	}
	
	/**
    * нод(this;other)
    *
    * @param BigPolinom other - второй полнином для нахождения нод
    * @return BigPolinom result - нод(this;other)
    *
    * @version 1
    * @author 
    */
    public BigPolinom gcd(BigPolinom other)
    {
		BigPolinom buffThis = this.clone();
        BigPolinom buffOther = other.clone();
		BigPolinom result = buffOther;
		BigPolinom one = new BigPolinom(this.factors.get(0).getPowers().size(), "1", mode);
		if(buffOther.isMoreThan(buffThis))
		{
			result = buffOther;
			buffOther = buffThis;
			buffThis = result;
			result = buffOther;
		}
		if(!buffThis.isDivided(buffOther))
			return one;
		while(!buffThis.mod(buffOther).isZero())
        {
            result = buffThis.mod(buffOther);
			buffThis = buffOther;
			buffOther = result;
			//System.out.println(buffThis + " : " + buffOther + " !! " + result);
        }
		//result.divideByHighCoef();
		if(result.isZero())
			result = one;
		return result;
    }
	
	public BigPolinom lcm(BigPolinom other)
    {
		return this.multiply(other).divide(this.gcd(other));
    }
	
	/**
    * Класс, который необходим для метода divideUniversal
	*
    * @version 1
    * @author 
    */
	private class Case
	{
		private BigPolinom first;
		private BigPolinom second;

		public Case(BigPolinom first, BigPolinom second)
		{
			this.first = first;
			this.second = second;
		}

		public BigPolinom getFirst()
		{
			return first;
		}

		public BigPolinom getSecond()
		{
			return second;
		}
	}

	/**
    * Получение индекса монома, если тот имеется в полиноме
	*
	* @param BigMonom other - моном, который мы ищем
	*
    * @return index - номер монома в полиноме, если не найден, то возращает -1
    *
    * @version 1
    * @author 
    */
	public int monomIndex(BigMonom other)
	{
		int index;
		if(this.isZero() || other.isZero())
			return -1;
		for(index = 0; index < this.factors.size(); index++)
			if(this.factors.get(index).getPowers().equals(other.getPowers()))
				return index;
		return -1;
	}
	
	public int monomIndexDivided(BigMonom other)	//Используется для делимости
	{
		int index;
		for(index = 0; index < this.factors.size(); index++)	//индекс монома, который сравниваем с other
		{
			if(this.factors.get(index).isDivided(other))
				return index;
		}
		return -1;
	}
	
	/**
    * Сортировка полинома
    *
    * @version 1
    * @author 
    */
	public void sort()
	{
		int i;
		BigPolinom buffThis = this.clone();
		BigPolinom result = new BigPolinom();
		BigMonom buffMonom;
		while(buffThis.factors.size() > 0)
		{
			buffMonom = buffThis.factors.get(0);
			for(i = 1; i < buffThis.factors.size(); i++)
			{
				if(buffMonom.compareTo( buffThis.factors.get(i) ) < 0)
					buffMonom = buffThis.factors.get(i);
			}
			buffMonom.setCoef( buffMonom.getCoef().reduce() );
			if(!buffMonom.isZero())
				result.factors.add(buffMonom);
			buffThis.factors.remove(buffThis.factors.indexOf(buffMonom));
		}
		this.factors = result.factors;
	}
	
	/**
    * Проверка на делимость полиномов
	*
    * @return true - делятся, иначе false
    *
    * @version 1
    * @author 
    */
	public boolean isDivided(BigPolinom other)
	{
		/*int i,j;
		int monoms = this.factors.size();	//Получаем кол-во мономов
		for(i = 0; i < other.factors.size(); i++)	//Прогоняем мономы из other
			if(this.monomIndexDivided(other.factors.get(i)) != -1)	//если в this встретился моном, то вычитаем его из monoms
				monoms--;
		return monoms == 0 ? true : false;*/
		return this.getHighMonom().isDivided(other.getHighMonom());
	}
	
	/**
    * Получение старшего монома
	*
    * @return старший моном
    *
    * @version 1
    * @author 
    */
	public BigMonom getHighMonom()
	{
		return this.factors.get(0).clone();
	}
	
	/**
    * Получение S полинома
	*
	* @param BigPolinom other - второй полином в паре
	*
    * @return BigPolinom result - s-полином
    *
    * @version 1
    * @author 
    */
	public BigPolinom sPolynom(BigPolinom other)	//Быстрее нижнего?
	{
		BigPolinom result = new BigPolinom();
		BigPolinom buffThis = this.clone();
		BigPolinom buffOther = other.clone();
		BigMonom multiplier;
		//multiplier = buffThis.getHighMonom().getMultiplier(buffOther.getHighMonom());
		multiplier = buffThis.factors.get(0).getMultiplier(buffOther.factors.get(0));
		buffThis = buffThis.multiply(multiplier);
		//multiplier = buffOther.getHighMonom().getMultiplier(buffThis.getHighMonom());
		multiplier = buffOther.factors.get(0).getMultiplier(buffThis.factors.get(0));
		buffOther = buffOther.multiply(multiplier);
		result = buffThis.subtract(buffOther);
		return result;
	}
	
	public BigPolinom sPolynom2(BigPolinom other)
	{
		BigPolinom result;
		BigPolinom buffThis = this.clone();
		BigPolinom buffOther = other.clone();
		BigPolinom LCM = this.getHighMonom().lcm(other.getHighMonom()).toBigPolinom();
		buffThis = buffThis.multiply(LCM).divide(this.getHighMonom().toBigPolinom());
		buffOther = buffOther.multiply(LCM).divide(other.getHighMonom().toBigPolinom());
		result = buffThis.subtract(buffOther);
		return result;
	}
	
	public BigPolinom reduce22(ArrayList<BigPolinom> basis, long startTime, boolean changed)	//основано на делителе
	{
		int i = 0, f = 0;
		BigPolinom buffThis = this.clone();
		BigPolinom result = new BigPolinom(buffThis.factors.get(0).getPowers().size(), "0", mode);
		BigPolinom divider;
		do
		{
			if(!changed)
			{
				long time = TimeUnit.SECONDS.convert(System.nanoTime()-startTime, TimeUnit.NANOSECONDS);
				if(time > 60)
					return buffThis;
			}
			for(i = 0, f = 0; i < basis.size() && f == 0; i++)
			{
				if(!buffThis.equals2(basis.get(i)) && !basis.get(i).isZero())
					if(buffThis.getHighMonom().isDivided( basis.get(i).getHighMonom() ))
						f = 1;
			}
			i--;
			if(f == 0)
			{
				/*buffThis.factors.get(0).setCoef( buffThis.factors.get(0).getCoef().reduce() );
				result.factors.add(buffThis.factors.get(0));
				buffThis.factors.remove(0);*/
				return buffThis;
			}
			else
			{
				divider = buffThis.getHighMonom().toBigPolinom().divide( basis.get(i).getHighMonom().toBigPolinom());
				buffThis = buffThis.subtract(basis.get(i).multiply(divider));
				buffThis.sort();
				/*if(!buffThis.isZero())
					buffThis.divideByHighCoef();*/
				/*if(!buffThis.isZero())	//убрать?
					buffThis.simpleMod();*/
				//System.out.print("T");
				//System.out.println("\nbuffThis:" + buffThis);
			}
		} while(!buffThis.isZero());
		result.sort();
		/*if(!result.isZero())
			result.divideByHighCoef();*/
		//System.out.println("\nRES:" + result);
		return result;
	}
	
	/**
    * Рекуция
	*
	* @param BigPolinom other - второй полином в паре
	*
    * @return BigPolinom result - s-полином
    *
    * @version 1
    * @author 
    */
	public BigPolinom reduce2(ArrayList<BigPolinom> basis, long startTime, boolean changed)
	{
		int i, f;
		BigPolinom buffThis = this.clone();
		BigPolinom result = new BigPolinom(buffThis.factors.get(0).getPowers().size(), "0", mode);
		BigMonom multiplier;
		BigPolinom buffOther;						//Полином, старший член которого делится на старший член buffThis
		do
		{
			if(!changed)
			{
				long time = TimeUnit.SECONDS.convert(System.nanoTime()-startTime, TimeUnit.NANOSECONDS);
				if(time > 60)
					return buffThis;
			}
			for(i = 0, f = 0; i < basis.size() && f == 0; i++)
			{
				if(!buffThis.equals2(basis.get(i)))
					if(buffThis.factors.get(0).isDivided( basis.get(i).factors.get(0) ))
						f = 1;
			}
			i--;
			if(f == 0)
			{
				result.factors.add(buffThis.factors.get(0));
				buffThis.factors.remove(0);
			}
			else
			{
				buffOther = basis.get(i).clone();
				multiplier = buffOther.factors.get(0).getMultiplier(buffThis.factors.get(0));
				buffOther = buffOther.multiply(multiplier);
				buffThis = buffThis.subtract(buffOther);
				buffThis.sort();
				/*if(!buffThis.isZero())
					buffThis.gcdAndLcm();*/
			}
			//System.out.print("T");
			//System.out.println("\nbuffThis:" + buffThis);
		} while(!buffThis.isZero());
		result.sort();
		//result.gcdRational();
		//System.out.println("\nRES:" + result);
		return result;
	}
	
	public boolean reduce(ArrayList<BigPolinom> basis, long startTime, boolean changed)
	{
		int i,f = 0;
		if(this.isZero())
			return false;
		BigPolinom reduced;
		reduced = this.reduce2(basis, startTime, changed);	//reduce2 вернуть
		//System.out.println(reduced);
		if(!reduced.isZero())
			for(i = 0; i < basis.size() && f == 0; i++)
				if(basis.get(i).equals2(reduced))
					f++;
		if(f == 0 && !reduced.isZero())
		{
			//reduced.simpleMod();	//убрать?
			basis.add(reduced);
		}
		else
			return false;
		return true;
	}
	
	public void divideByHighCoef()
	{
		int i;
		BigQ highCoef = this.factors.get(0).getCoef();
		for (i = 0; i < factors.size(); i++)
		{
			this.factors.get(i).setCoef(this.factors.get(i).getCoef().divide(highCoef));
		}
		//this.simpleMod();	//убрать?
	}
	
	public boolean onlyOne()
	{
		if(this.getHighMonom().isConst())
			return true;
		int i,j,required = -1;
		for(i = 0; i < this.getHighMonom().getPowers().size(); i++)
		{
			if(this.getHighMonom().getPowers().get(i) > 0)
				if(required == -1)
					required = i;
				else
					return false;
		}
		for(i = 1; i < this.factors.size(); i++)
		{
			for(j = 0; j < this.factors.get(i).getPowers().size(); j++)
				if(this.factors.get(i).getPowers().get(j) > 0 && j != required)
					return false;
		}
		return true;
	}
	
	public ArrayList<BigMonom> getFactors()
	{
		return factors;
	}
	
	public void setMode(int curMode)
	{
		mode = curMode;
		int i;
		for(i = 0; i < this.factors.size(); i++)
			this.factors.get(i).setMode(mode);
	}
}
