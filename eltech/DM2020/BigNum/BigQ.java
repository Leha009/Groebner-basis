package eltech.DM2020.BigNum;

import java.util.*;

/**
* Класс, который позволяет манипулировать с большими рациональными числами
* @version 0.01
* @author Аюпов Ренат, Сычев Александр
*/
public class BigQ
{
	private BigZ p; // Числитель
	private BigZ q; // Знаменатель
	
	private BigQ(){}
	
	/**
	* Конструктор, с помощью которого можно ввести большое рациональное число
	* Если p и q  не инициализированны, то бросит исключение
	*
	* @param BigZ p, q - целые числа: p - числитель, q - знаменатель
	*
	* @version 1.1
	* @author Аюпов Ренат
	*/
	public BigQ(BigZ p, BigZ q) throws IllegalArgumentException, ArithmeticException
	{
		if(p == null || q == null)
			throw new IllegalArgumentException("Неверный аргумент: числа должны быть инициализированны\n");
		this.p = p.clone();
		this.q = q.clone();
		if( q.isZero() )
			throw new ArithmeticException("В знаменателе не может быть нуля\n");
		this.reduceSelf();
	}
		
	/**
	* Конструктор, с помощью которого можно ввести большое рациональное число
	* Если строка src пустая или null, то бросит исключение
	* Например: из этого "-2521/-2632" сделает это "2521/2632"
	* "2521/-2632" ------> "-2521/2632"
	* "(-2521/2632)" ------> "-2521/2632"
	* "2521/2632" ------> "2521/2632"
	* "(-2521/1)" ------> "-2521"
	* "-2521/-1" ------> "2521", "2521/-1" ------> "-2521"
	* "2521/1" ------> "2521"
	* "2521/0" или "2521/-0" ------> исключение
	*
	* @param String src - строка, представляющая большое рациональное число. Её вид должен быть такой: "[числитель]/[знаменатель]". Например: -2357982579/-5617929
	*
	* @version 1.1
	* @author Сычев Александр
	*/
	public BigQ(String src) throws IllegalArgumentException, ArithmeticException
	{
		int SlashIndex;
		if(src == null)
			throw new IllegalArgumentException("Неверный аргумент: строка не может быть не инициализированной\n");
		if(src.equals(""))
			throw new IllegalArgumentException("Неверный аргумент: строка не может быть пустой\n");
		src = src.trim();
		src = src.replace(")", "");
		src = src.replace("(", "");
		SlashIndex = src.indexOf("/");
		if (SlashIndex == -1)
		{
			this.p = new BigZ(src);
			this.q = new BigZ("1");
		}
		else
		{
			this.p = new BigZ( src.substring(0, SlashIndex) );
			this.q = new BigZ( src.substring(SlashIndex+1, src.length()) );
		}
		if( q.isZero() )
			throw new ArithmeticException("В знаменателе не может быть нуля\n");
		this.reduceSelf();
	}
	
	/**
	* Вывод большого рационального числа в виде строки
	* Например: из этого "-2521/-2632" сделает это "2521/2632"
	* "2521/-2632" ------> "-2521/2632"
	* "-2521/2632" ------> "-2521/2632"
	* "2521/2632" ------> "2521/2632"
	* "-2521/1" ------> "-2521"
	* "-2521/-1" ------> "2521", "2521/-1" ------> "-2521"
	* "2521/1" ------> "2521"
	* "2521/0" или "2521/-0" ------> исключение
	*
    * @return String - представление числа в виде строки
	*
	* @version 1.2
	* @author Сычев Александр
	*/
	@Override
	public String toString()
	{
		if(this.isZero())
			return "0";
		else
			return ( this.checkPositive() ? "" : "-") + this.p.abs().toString() + ( q.abs().equals(new BigZ("1")) ? "" : ("/" + q.abs().toString()) );
	}
	
	/**
	* Клонирование объекта
	*
    * @return BigQ - копия
	*
	* @version 1
	* @author Сычев Александр
	*/
	@Override
	public BigQ clone()
	{
		BigQ result = new BigQ();
		result.p = this.p.clone();
		result.q = this.q.clone();
		return result;
	}
	
	/**
	* Проверка знака большого рациональное числа
	*
    * @return boolean - знак рационального числа
	*
	* @version 1
	* @author
	*/
	public boolean checkPositive()
	{
		return !(p.checkPositive() ^ q.checkPositive());
	}
	
	/**
	* Проверяет является ли числитель нулём
	*
    * @return boolean - true, если числитель 0; false, если числитель не нуль
	*
	* @version 1
	* @author Афанасьева Антонина
	*/
	public boolean isZero()
	{
		return this.p.isZero();
	}
	
	/**
	* Сравнение BigQ, согласно спецификации Java
	*
    * @return эквивалентность
	*
	* @version 1
	* @author
	*/
	@Override
    public boolean equals(Object otherObj) 
	{
		if (otherObj == this) return true; 
		if (otherObj == null) return false;
		if( this.getClass() != otherObj.getClass() ) return false;
		BigQ other = (BigQ)otherObj;
		return this.p.equals(other.p) && this.q.equals(other.q);
    }
	
    /**
    * Сравнение двух больших рациональных чисел.
    *
    * @param BigQ other - второе число для сравнения с исходным
    * @return int - 0 если равны, -1 если меньше other, 1 если больше other
    *
    * @version 1
    * @author
    */
    public int compareTo(BigQ other)
    {
		BigQ buff = this.subtract(other);
		if(buff.isZero())
			return 0;
		else if (buff.checkPositive())
			return 1;
		else 
			return -1;
    }
	
    /**
    * Абсолютное значение рационального числа
    *
    * @return BigQ - абсолютное значение рационального числа
    *
    * @version 2
    * @author Влад Балаганский
    */
    public BigQ abs()
    {
		return new BigQ( this.p.abs(), this.q.abs() );
	}
	
	/**
    * Возведение в степень с помощью алгоритма бинарного возведения в степень
    *
    * @param BigN other - число, в степень которого возводится исходное
	*
    * @return BigQ result - результат возведения исходного в степень other
    *
    * @version 1
    * @author
    */
	public BigQ pow(BigN other) 
	{
        BigN TWO = new BigN("2");
        BigQ result = new BigQ("1");
        BigQ base = this.clone();
        BigN b = other.clone();
        while( !b.isZero() )
        {
            if( b.isEven() == false)
                result = result.multiply(base);
            base = base.multiply(base);
            b = b.divide(TWO);
        }
        return result;
    }
	
	/**
    * Конвертация в BigN
	* Если BigQ отрицательное или знаменатель не равен единице, то бросает исключение
    *
    * @return BigN result - целое число
    *
    * @version 1
    * @author
    */
    public BigN toBigN() throws ArithmeticException
    {
		if(this.isZero())
			return new BigN("0");
		if(this.checkPositive() == false)
			throw new ArithmeticException("Нельзя перевести отрицательное число в натуральное + {0}\n");
		if( !this.q.abs().equals( new BigZ("1") ) )
			throw new ArithmeticException("Нельзя перевести рациональное число со знаменателем не равным 1 в натуральное число + {0}\n");
		return this.p.abs().toBigN();
    }
	
	/**
    * Конвертация в BigZ
	* Если у BigQ знаменатель не равен единице, то бросает исключение
    *
    * @return BigZ result - целое число
    *
    * @version 1
    * @author
    */
    public BigZ toBigZ() throws ArithmeticException
    {
		if(this.isZero())
			return new BigZ("0");
		if( !this.q.abs().equals( new BigZ("1") ) )
			throw new ArithmeticException("Нельзя перевести рациональное число со знаменателем не равным 1 в целое\n");
		BigZ result = this.p.clone();
		if( this.checkPositive() )
			return result.abs();
		else
			return result.abs().multiplyByMinusOne();
    }

	/**
	 * Умножение BigQ
	 *
	 * @param BigQ other - число на которое умножаем
	 * @return BigQ result - результат умножения
	 * @version 2
	 *
	 * @author Маймаева Анастасия, Хамитов Абулкаир
	 *
	 */
	public BigQ multiply(BigQ other)
	{
		BigQ result = new BigQ();
		result.p = this.p.multiply(other.p);
		result.q = this.q.multiply(other.q);
		return result.reduce();
	}

	/**
	* Сокращение дробей
	*
	* @return BigQ - новую сокращенную дробь
	*
	* @version 1
	* @author Хамитов Абулкаир
	*/
	public BigQ reduce()
	{
		BigQ result = this.clone();
		BigZ gcd = new BigZ( result.p.abs().toBigN().gcd( result.q.abs().toBigN() ) );
		result.p = result.p.divide(gcd);
		result.q = result.q.divide(gcd);
		return result;
	}
	
	/**
	* Сокращение дробей
	*
	* @version 1
	* @author Сычев Александр
	*/
	private void reduceSelf()
	{
		BigZ gcd = new BigZ( p.abs().toBigN().gcd( q.abs().toBigN() ) );
		p = p.divide(gcd);
		q = q.divide(gcd);
	}

	/**
	* Сложение больших рациональных чисел
	*
	* @return BigQ - результат суммы двух дробей
	*
	* @version 1
	* @author Хамитов Абулкаир
	*/
	public BigQ add(BigQ other)
	{
		BigQ result = new BigQ();
		result.q = this.q.multiply(other.q);
		result.p = ( this.p.multiply(other.q) ).add( other.p.multiply(this.q) );
		return result.reduce();
	}


	/**
	* Вычитание больших рациональных чисел
	*
	* @return BigQ - результат вычитание двух дробей
	*
	* @version 1
	* @author Хамитов Абулкаир
	*/
	public BigQ subtract(BigQ other)
	{
		BigQ result = new BigQ();
		result.q = this.q.multiply(other.q);
		result.p = ( this.p.multiply(other.q) ).subtract(other.p.multiply(this.q));
		result.reduce();
		return result;
	}

	/**
	* Деление больших рациональных чисел
	* Если числитель второго числа == 0, то бросит исключение
	*
	* @return BigQ - результат деления двух дробей
	*
	* @version 2
	* @author Хамитов Абулкаир
	*/
	public BigQ divide(BigQ other) throws ArithmeticException
	{
		BigQ result = new BigQ();
		if( other.isZero() )
			throw new ArithmeticException("Нельзя делить на 0\n");

		result.q = this.q.multiply(other.p);
		result.p = this.p.multiply(other.q);
		result.reduce();
		return result;
	}
	
	/**
	* Остаток от деления на простое число BigQ
	*
	* @return BigQ - остаток
	*
	* @version 
	* @author 
	*/
	public BigQ mod(BigQ other) throws ArithmeticException
	{
		BigQ result = this.clone();
		BigQ buffOther = other.clone();
		if( other.isZero() )
			throw new ArithmeticException("Нельзя делить на 0\n");
		
		buffOther.q = result.q.clone();
		buffOther.p = buffOther.p.multiply(result.q);
		
		while(result.compareTo(buffOther) > 0)
			result = result.subtract(buffOther);
		result.reduce();
		return result;
	}
	
	public BigZ getP()
	{
		return p;
	}
	
	public BigZ getQ()
	{
		return q;
	}
	
	public void setP(BigZ other)
	{
		this.p = other;
	}
	
	public void setQ(BigZ other)
	{
		this.q = other;
	}
}
















