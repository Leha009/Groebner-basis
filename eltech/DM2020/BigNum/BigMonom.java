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
	private ArrayList<Integer> powers = new ArrayList<Integer>();	//степени
	private ArrayList<Integer> mode = new ArrayList<Integer>();		//режим сортировки
	
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
	public BigMonom(int amount, String src, ArrayList<Integer> curMode) throws IllegalArgumentException
	{
		mode = curMode;
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
		int i;
		BigMonom result = new BigMonom();
		result.coef = this.coef.clone();
		for(i = 0; i < this.powers.size(); i++)
			result.powers.add(this.powers.get(i));
		result.mode = this.mode;
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
	/*public int compareTo(BigMonom other)
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
	}*/
	
	public int compareTo(BigMonom other)
	{
		//смотрим, чтобы сравниваемый одночлен не был нулем
		if(this.isZero())
			//если одночлен, с которым сравниваем
			//тоже нуль, то они равны
			//иначе нет
			if(other.isZero())
				return 0;
			else
				return -1;
		//если сравниваемый - не нуль, а второй - нуль
		//то, очевидно, сравниваемый больше
		else if(other.isZero())
			return 1;
		int i;
		//ниже смотрим степени при неизвестных, начиная со старших
		//в зависимости от того, в каком одночлене степень отличается
		//узнаем какой и больше
		for(i = 0; i < mode.size(); i++)
			if(this.powers.get(mode.get(i)) > other.powers.get(mode.get(i)))
				return 1;
			else if(this.powers.get(mode.get(i)) < other.powers.get(mode.get(i)))
				return -1;
		//если степени все равны, то и одночлены равны
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
		BigMonom buffOther = other.clone();
		//Перемножаем коэффициенты
		result.coef = result.coef.multiply(buffOther.coef);
		//Увеличиваем степени неизвестных или добавляем то, чего нет
		//Например, xy * x^z = x^2yz
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
		//result - на данный момент, одночлен, который домножаем
		BigMonom result = this.clone();
		//Коэффициент монома = частное от коэффициента other на коэффициент result
		result.setCoef( other.getCoef().divide(result.getCoef()) );
		for(i = 0; i < result.powers.size(); i++)
			//Если в result какая-то степень меньше степени в other,
			//то запишем разницу степеней other-result
			if(result.powers.get(i) <= other.powers.get(i))		
				result.powers.set(i, other.powers.get(i) - result.powers.get(i));
			//Степень в result > other, поэтому домнажать не надо будет => степень 0
			else
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
	
	/**
    * НОД
	*
	* @param BigMonom other - второй моном
	*
    * @return BigMonom result - НОД
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
		BigMonom result = new BigMonom(this.powers.size(), "1", this.mode);
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
	
	/**
    * НОК
	*
	* @param BigMonom other - второй моном
	*
    * @return BigMonom result - НОК мономов
    *
    * @version 1
    * @author 
    */
	public BigMonom lcm(BigMonom other)
	{
		int i;
		BigMonom buffThis = this.clone();
		BigMonom buffOther = other.clone();
		BigMonom result = new BigMonom(this.powers.size(), "1", this.mode);
		for(i = 0; i < this.powers.size(); i++)
			result.powers.set(i, Math.max(this.powers.get(i), other.powers.get(i)));
		//result.setCoef(buffThis.getCoef().lcm(buffOther.getCoef()));
		return result;
	}
	
	/**
    * Конвертация в BigPolinom
	*
    * @return BigPolinom result - НОД
    *
    * @version 1
    * @author 
    */
	public BigPolinom toBigPolinom()
	{
		BigPolinom result = new BigPolinom(this.powers.size(), "1");
		result.getFactors().add(this.clone());
		result.getFactors().remove(0);
		return result;
	}
	
	/**
    * Проверка, что моном - константа
	*
    * @return true - моном является константой, иначе false
    *
    * @version 1
    * @author 
    */
	public boolean isConst()
	{
		int i;
		for(i = 0; i < this.powers.size(); i++)
			if(this.powers.get(i) != 0)
				return false;
		return true;
	}
	
	/**
    * Получение первой ненулевой степени
	*
    * @return int i - индекс переменной с ненулевой степенью
    *
    * @version 1
    * @author 
    */
	public int getHighPower()
	{
		int i;
		for(i = 0; i < this.powers.size(); i++)
		{
			if(this.powers.get(i) > 0)
				return this.powers.get(i);
		}
		return i;
	}
	
	public int cmpTo(BigMonom other)
	{
		int i,s1,s2;
		for(i = 0, s1 = 0, s2 = 0; i < this.powers.size(); i++)
		{
			s1 += this.powers.get(i);
			s2 += other.powers.get(i);
		}
		return s1 >= s2 ? 1 : s1 == s2 ? 0 : -1;
	}
	
	/**
    * Проверка, что в мономе только одна неизвестная
	*
    * @return true - в мономе одна переменная, иначе - false
    *
    * @version 1
    * @author 
    */
	public int clearPower()
	{
		int i,power = -1;
		{
			for(i = 0; i < this.powers.size(); i++)
			{
				if(this.powers.get(i) > 0)
				{
					if(power == -1)
						power = i;
					else
						return -1;
				}
			}
		}
		return power;
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
	
	public void setMode(ArrayList<Integer> curMode)
	{
		mode = curMode;
	}
	
	public ArrayList<Integer> getMode()
	{
		return mode;
	}
}