package eltech.DM2020.BigNum;

import java.util.*;

/**
* Класс, который позволяет манипулировать с мономами с рациональными коэффициентами
* 
* @version 0.01
*/
public class BigMonom
{
	private BigQ coef;												//коэффициент
	private ArrayList<Integer> powers = new ArrayList<Integer>();	//степени x_i
	
	private BigMonom() {}
	
	/**
	* Конструктор, с помощью которого можно инициализировать моном
	* Если строка src пустая или null, то бросит исключение
	*
	* -27/7 | x | 1^3 | x | 2
	*
	* -27/7x1^3x2
	*
	* -57/7
	*
	* @param String src - представление полинома в виде строки
	*
	* @version 1
	* @author 
	*/
	public BigMonom(int amount, String src) throws IllegalArgumentException
	{
		int i, power, index;
		String[] str;
		for(i = 0; i < amount; i++)
			powers.add(null);
		if(src.indexOf("x") == -1)
		{
			if(src.trim().equals("") || src.trim().equals("-"))
				src += "1";
			coef = new BigQ(src);
		}
		else
		{
			str = src.split("x");
			if(str[0].trim().equals("") || str[0].trim().equals("-"))
				str[0] += "1";
			coef = new BigQ(str[0]);
			for(i = 1; i < str.length; i++)
			{
				if(str[i].indexOf("^") != -1)
				{
					index = Integer.parseInt( str[i].substring(0,str[i].indexOf("^")) );
					if (index > amount)
						throw new IllegalArgumentException("Встречен индекс, превышающий кол-во неизвестных\n");
					power = Integer.parseInt( str[i].substring(str[i].indexOf("^")+1, str[i].length()) );
					powers.set(index-1, power);
				}
				else
				{
					index = Integer.parseInt( str[i] );
					if (index > amount)
						throw new IllegalArgumentException("Встречен индекс, превышающий кол-во неизвестных\n");
					powers.set(index-1, 1);
				}
			}
		}
		
		for(i = 0; i < powers.size(); i++)
			if(powers.get(i) == null)
				powers.set(i, 0);
	}
	
	/**
	* Вывод монома в виде строки
	* Выводиться должно так:
	* (+-)27x1^3
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
		buffS += coef.toString();
		for(i = 0; i < powers.size(); i++)
			if(powers.get(i) != 0) 
				if(powers.get(i) != 1) 
					buffS += "*x"+(i+1)+"^"+powers.get(i);
				else
					buffS += "*x"+(i+1);
		return buffS;
	}
	
	/**
	* Проверка монома на нуль
	*
    * @return 1, если моном - нуль, иначе 0
	*
	* @version 1
	* @author
	*/
	public boolean isZero()
	{
		return this.coef.isZero() ? true : false;
	}
	
	/**
    * Клонирование объекта
	*
    * @return копию BigMonom
    *
    * @version 1
    * @author
    */
	@Override
	public BigMonom clone()
	{
		BigMonom result = new BigMonom();
		result.coef = this.coef.clone();
		result.powers = new ArrayList<Integer>(this.powers);
		return result;
	}
	
	/**
	* Сравнивание мономов
	*
    * @return 1 - сравниваемый моном имеет большую степень, 0 - мономы одной степени, -1 - сравниваемый моном имеет степень меньше
	*
	* @version 1
	* @author
	*/
	public int compareTo(BigMonom other)
	{
		int i;
		if(this.isZero())
			if(other.isZero())
				return 0;
			else
				return -1;
		else if(other.isZero())
			return 1;
		for(i = 0; i < this.powers.size(); i++)
			if(this.powers.get(i) > other.powers.get(i))
				return 1;
			else if(this.powers.get(i) < other.powers.get(i))
				return -1;
		return 0;
	}
	
	private boolean isMoreThan(BigMonom other)
    {
		return this.compareTo(other) > 0 ? true : false;
    }
	
	private boolean isMoreOrEquals(BigMonom other)
    {
		return this.compareTo(other) >= 0 ? true : false;
    }
	
	private boolean isLessThan(BigMonom other)
    {
		return this.compareTo(other) < 0 ? true : false;
    }
	
	private boolean isLessOrEquals(BigMonom other)
    {
		return this.compareTo(other) <= 0 ? true : false;
    }
	
	private boolean isEquals(BigMonom other)
    {
		return this.compareTo(other) == 0 ? true : false;
    }
	
	/**
    * Умножение мономов
	*
	* @param BigMonom other - второй моном, на который умножается исходный
	*
    * @return BigMonom result - результат вычитания
    *
    * @version 1
    * @author 
    */
	public BigMonom multiply(BigMonom other)
	{
        int i;
        BigMonom result = this.clone();
		BigMonom buffOther;
		buffOther = other.clone();
		result.coef = result.coef.multiply(buffOther.coef);
		for(i = 0; i < result.powers.size(); i++)
			result.powers.set(i, result.powers.get(i) + buffOther.powers.get(i) );
        return result;
	}
	
	/**
    * Умножение монома на -1
	*
    * @return BigMonom result - результат умножения
    *
    * @version 1
    * @author 
    */
	public void multiplyByMinusOne()
	{
        BigMonom result = this.clone();
		BigQ minusOne = new BigQ("-1/1");
		coef = coef.multiply(minusOne);
	}
	
	/**
    * Получение монома, на который необходимо умножить this, чтобы получить other
	*
	* Пример: есть мономы 5x1^2x2^3 и x2. x2 мы домножим на 5x1^2x2^2
	*
	* @param BigMonom other - второй моном
	*
    * @return BigMonom result - моном, на который умножаем
    *
    * @version 1
    * @author 
    */
	public BigMonom getMultiplier(BigMonom other)
	{
		int i;
		BigMonom result = this.clone();
		result.setCoef( other.getCoef().divide(result.getCoef()) );		//Коэффициент монома = частное от коэффициента other на коэффициент result
		for(i = 0; i < result.powers.size(); i++)
			if(result.powers.get(i) <= other.powers.get(i))		//Если в result какая-то степень меньше степени в other, то запишем разницу степеней other-result
				result.powers.set(i, other.powers.get(i) - result.powers.get(i));
			else	//Степень в result >= other, поэтому домнажать не надо будет => степень 0
				result.powers.set(i, 0);
		return result;
	}
	
	/**
    * Проверка на делимость мономов
	*
    * @return true - делятся, иначе false
    *
    * @version 1
    * @author 
    */
	public boolean isDivided(BigMonom other)
	{
		int i, f = 0;
		for(i = 0; i < other.powers.size(); i++)
		{
			if(other.powers.get(i) <= this.powers.get(i))
				f++;
		}
		if(f != other.powers.size())
			return false;
		return true;
	}
	
	//if(other.powers.get(i) > this.powers.get(i) || (this.powers.get(i) == 0 && other.powers.get(i) != 0) || (other.powers.get(i) == 0 && this.powers.get(i) != 0))

	/**
    * НОД
	*
    * @return result - НОД
    *
    * @version 1
    * @author 
    */
	public BigMonom gcd(BigMonom other)
	{
		int i;
		BigMonom buffThis = this.clone();
        BigMonom buffOther = other.clone();
		BigMonom buff;
		BigMonom result = new BigMonom(this.powers.size(), "1");
		if(buffThis.isLessThan(buffOther))
		{
			buff = buffOther;
			buffOther = buffThis;
			buffThis = buff;
		}
		for(i = 0; i < buffThis.powers.size(); i++)
		{
			if(buffOther.powers.get(i) <= buffThis.powers.get(i))
				result.powers.set(i, Math.min(buffThis.powers.get(i), buffOther.powers.get(i)));
		}
		return result;
	}
	
	public BigMonom lcm(BigMonom other)
	{
		int i;
		BigMonom buffThis = this.clone();
		BigMonom buffOther = other.clone();
		BigMonom result = new BigMonom(this.powers.size(), "1");
		for(i = 0; i < this.powers.size(); i++)
			result.powers.set(i, Math.max(this.powers.get(i), other.powers.get(i)));
		return result;
	}
	
	public BigPolinom toBigPolinom()
	{
		BigPolinom result = new BigPolinom(this.powers.size(), "1");
		result.getFactors().add(this.clone());
		result.getFactors().remove(0);
		return result;
	}
	
	public boolean isConst()
	{
		int i;
		for(i = 0; i < this.powers.size(); i++)
			if(this.powers.get(i) != 0)
				return false;
		return true;
	}
	
	public int getHighPower()
	{
		int i;
		for(i = 0; i < this.powers.size(); i++)
		{
			if(this.powers.get(i) != 0)
				return i;
		}
		return i;
	}
	
	public ArrayList<Integer> getPowers()
	{
		return powers;
	}
	
	public void setPowers(ArrayList<Integer> newPowers)
	{
		powers = newPowers;
	}
	
	public BigQ getCoef()
	{
		return coef;
	}
	
	public void setCoef(BigQ num)
	{
		coef = num;
	}
}