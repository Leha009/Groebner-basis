package eltech.DM2020.BigNum;

import java.util.*;
import java.math.*;
import java.io.*;

/**
* Класс, который содержит интерфейс
* @version 1
* @author 
*/
public class Colloquium
{
	private static Basis base = new Basis();
	private static Example examples = new Example();
	private static Scanner in = new Scanner(System.in);
	private static int mode;
	
	public static void start()
	{
		System.out.println("Для ознакомления с программой введите \"?\" или \"help\"");
		Interface();
		
		/*ArrayList<Integer> dj = new ArrayList<Integer>();
		dj.add(2);
		dj.add(1);
		dj.add(0);
		BigPolinom test = new BigPolinom(3, "x1^3+x1x2", dj);
		BigPolinom test2 = new BigPolinom(3, "x1+x2", dj);
		System.out.println(test.mod(test2));
		//System.out.println(test2+"\n");
		//System.out.println(test2.multiply(test3));
		//System.out.println(test.sPolynom(test2));
		//System.out.println(test.mod(test2));*/
	}
	
	private static void Interface()
	{
		String cmd;
		boolean EXIT = false;
		int i, formed = 0, inputed = 0;
		while(!EXIT)
		{
			System.out.print("> ");
			cmd = in.nextLine();
			try
			{
				switch(cmd.toLowerCase())
				{
					case "q":{}
					case "quit":{}
					case "exit":
					{
						EXIT = true;
						break;
					}
					case "in":{}
					case "input":
					{
						input();
						formed = 0;
						inputed = 1;
						break;
					}
					case "out":{}
					case "output":
					{
						if(inputed != 0)
							outputOriginal();
						else
							System.out.println("Вы не вводили полиномы. Введите input(или in), чтобы ввести их.");
						break;
					}
					case "basis":
					{
						if(inputed != 0)
						{
							if(formed == 0)
								formBasis();
							else
								System.out.println("Базис уже составлен! Введите outb, чтобы посмотреть его");
							formed = 1;
						}
						else
							System.out.println("Вы не вводили полиномы. Введите input(или in), чтобы ввести их.");
						break;
					}
					case "outputbasis":{}
					case "outbasis":{}
					case "outb":
					{
						if(formed == 1)
							outputBasis();
						else
							if(inputed != 0)
								System.out.println("Вы еще не сформировали базис. Введите basis, чтобы сформировать его.");
							else
								System.out.println("Вы не вводили полиномы. Введите input(или in), чтобы ввести их.");
						break;
					}
					case "?":{}
					case "help":
					{
						help();
						break;
					}
					case "cls":{}
					case "clear":
					{
						System.out.println( String.join( "", Collections.nCopies(160, "\n") ) );
						break;
					}
					case "dec":{}
					case "decision":
					{
						if(inputed != 0 && formed != 0)
							dicision();
						else
							System.out.println("Необходимо ввести полиномы и/или сформировать базис для них.");
						break;
					}
					case "reduce":
					{
						if(formed != 0)
							reduceInput();
						else
							if(inputed != 0)
								System.out.println("Вы еще не сформировали базис. Введите basis, чтобы сформировать его.");
							else
								System.out.println("Вы не вводили полиномы. Введите input(или in), чтобы ввести их.");
						break;
					}
					case "cord":{}
					case "changeorder":
					{
						if(inputed != 0)
						{
							changeOrder();
							formed = 0;
						}
						else
							System.out.println("Вы не вводили полиномы. Введите input(или in), чтобы ввести их.");
						break;
					}
					case "spoly":{}
					case "spolynom":
					{
						SPolynom();
						break;
					}
					case "inputbasis":{}
					case "inbasis":
					{
						inputBasis();
						formed = 1;
						break;
					}
					case "generate":{}
					case "gen":
					{
						generateExample();
						inputed = 1;
						formed = 0;
						break;
					}
					default:
					{
						System.out.println("Данной команды не существует. Посмотрите команды в help.");
					}
				}
			}
			catch(Throwable t)
			{
				System.out.println("Упс... Произошла неизвестная ошибка!");
				//System.out.println(t);
			}
		}
	}
	
	private static void help()
	{
		String line;
		try (BufferedReader inFile = new BufferedReader(new InputStreamReader( new FileInputStream("ReadMe.txt"), "UTF-8")))
		{
			while ( (line = inFile.readLine()) != null )
				System.out.println(line);
		}
		catch(Throwable t)
		{
			System.out.println(t);
		}
	}
	
	private static void input()
	{
		Scanner num = new Scanner(System.in);
		String buffS;
		ArrayList<Integer> order = new ArrayList<Integer>();
		int amount = 0;
		int k = 0;
		int i;
		System.out.println("Введите количество неизвестных");
		try
		{
			do
			{
				System.out.print("> ");
				amount = num.nextInt();
			} while(amount < 1);
			System.out.println("Введите построчно порядок неизвестных");
			while(order.size() != amount)
			{
				Scanner orderIn = new Scanner(System.in);
				k = orderIn.nextInt();
				if(k > 0 && k <= amount)
				{
					if(order.indexOf(k) == -1)
						order.add(k);
					else
					{
						System.out.println("Данный индекс уже был использован");
						System.out.println("Текущий порядок: " + order);
					}
				}
				else
					System.out.println("Такого индекса быть не может. Максимум " + amount);
			}
			for(i = 0; i < order.size(); i++)
				order.set(i, order.get(i)-1);
			System.out.println("Ввод многочленов имеет следующий вид: каждая переменная должна содержать индекс после себя, например, 45x1 + 7/5x2^7");
			base.clearAll();
			base.setMaxVars(amount);
			base.setMode(order);
			if(amount < 4)
			{
				Scanner num2 = new Scanner(System.in);
				System.out.println("\nВведите любое число, отличное от нуля, чтобы использовать следующий вид переменных: x y z");
				System.out.print("> ");
				mode = num2.nextInt();
			}
			else
				mode = 0;
			System.out.println("\nПострочно вводите полиномы. Чтобы прекратить ввод, нажмите Enter");
			do
			{
				buffS = in.nextLine();
				if(!buffS.equals(""))
				{
					if(mode != 0)
					{
						buffS = buffS.replace("x", "x1");
						buffS = buffS.replace("y", "x2");
						buffS = buffS.replace("z", "x3");
					}
					else
					{
						buffS = buffS.replace("y", "x2");
						buffS = buffS.replace("z", "x3");
					}
					base.addBase(buffS);
				}
			} while(!buffS.equals(""));
			//base.settingMode();
		}
		catch(Throwable t)
		{
			System.out.println("Ошибка ввода, попробуйте еще раз.");
			//System.out.println(t);
		}
	}
	
	private static void outputOriginal()
	{
			base.outputBase(mode);
	}
	
	private static void outputBasis()
	{
			base.output(mode);
	}
	
	private static void formBasis()
	{
		base.clearDec();
		System.out.println("Базис формируется...");
		base.doActions();
		System.out.println("Формирование базиса прошло успешно! Для вывода введите outbasis(или outb)");
	}
	
	private static void dicision()
	{
		base.outputDecision(mode);
	}
	
	private static void reduceInput()
	{
		String buffS;
		int maxvars = base.getMaxVars();
		System.out.println("Напомним, всего неизвестных " + maxvars);
		try
		{
			if(maxvars < 4)
			{
				Scanner num2 = new Scanner(System.in);
				System.out.println("\nВведите любое число, отличное от нуля, чтобы использовать следующий вид переменных: x y z");
				System.out.print("> ");
				mode = num2.nextInt();
			}
			else
				mode = 0;
			System.out.println("\nВведите полином");
			buffS = in.nextLine();
			if(!buffS.equals(""))
			{
				if(mode != 0)
				{
					buffS = buffS.replace("x", "x1");
					buffS = buffS.replace("y", "x2");
					buffS = buffS.replace("z", "x3");
				}
				else
				{
					buffS = buffS.replace("y", "x2");
					buffS = buffS.replace("z", "x3");
				}
			}
			BigPolinom reduced = new BigPolinom(maxvars, buffS, base.getMode());
			buffS = reduced.reduce2(base.getBasis(), 0, true, true).toString();
			if(mode != 0)
			{
				buffS = buffS.replace("x1", "x");
				buffS = buffS.replace("x2", "y");
				buffS = buffS.replace("x3", "z");
			}
			System.out.println("Результат редукции: " + buffS);
		}
		catch(Throwable t)
		{
			System.out.println("Ошибка ввода, попробуйте еще раз.");	
			//System.out.println(t);
		}
	}
	
	private static void changeOrder()
	{
		String buffS;
		ArrayList<Integer> order = new ArrayList<Integer>();
		ArrayList<BigPolinom> polys = base.getBase();
		int maxvars = base.getMaxVars();
		int k = 0;
		int i;
		System.out.println("Напомним, всего неизвестных " + maxvars);
		try
		{
			System.out.println("Введите построчно порядок неизвестных");
			while(order.size() != maxvars)
			{
				Scanner orderIn = new Scanner(System.in);
				k = orderIn.nextInt();
				if(k > 0 && k <= maxvars)
				{
					if(order.indexOf(k) == -1)
						order.add(k);
					else
					{
						System.out.println("Данный индекс уже был использован");
						System.out.println("Текущий порядок: " + order);
					}
				}
				else
					System.out.println("Такого индекса быть не может. Максимум " + maxvars);
			}
			for(i = 0; i < order.size(); i++)
				order.set(i, order.get(i)-1);
			base.setMode(order);
			for(i = 0; i < polys.size(); i++)
			{
				polys.get(i).setMode(order);
			}
		}
		catch(Throwable t)
		{
			System.out.println("Ошибка ввода, попробуйте еще раз.");
			//System.out.println(t);
		}
	}
	
	private static void inputBasis()
	{
		Scanner num = new Scanner(System.in);
		String buffS;
		ArrayList<Integer> order = new ArrayList<Integer>();
		int amount = 0;
		int k = 0;
		int i;
		System.out.println("Введите количество неизвестных");
		try
		{
			do
			{
				System.out.print("> ");
				amount = num.nextInt();
			} while(amount < 1);
			System.out.println("Введите построчно порядок неизвестных");
			while(order.size() != amount)
			{
				Scanner orderIn = new Scanner(System.in);
				k = orderIn.nextInt();
				if(k > 0 && k <= amount)
				{
					if(order.indexOf(k) == -1)
						order.add(k);
					else
					{
						System.out.println("Данный индекс уже был использован");
						System.out.println("Текущий порядок: " + order);
					}
				}
				else
					System.out.println("Такого индекса быть не может. Максимум " + amount);
			}
			for(i = 0; i < order.size(); i++)
				order.set(i, order.get(i)-1);
			System.out.println("Ввод многочленов имеет следующий вид: каждая переменная должна содержать индекс после себя, например, 45x1 + 7/5x2^7");
			base.clearAll();
			base.setMaxVars(amount);
			base.setMode(order);
			if(amount < 4)
			{
				Scanner num2 = new Scanner(System.in);
				System.out.println("\nВведите любое число, отличное от нуля, чтобы использовать следующий вид переменных: x y z");
				System.out.print("> ");
				mode = num2.nextInt();
			}
			else
				mode = 0;
			System.out.println("\nПострочно вводите полиномы. Чтобы прекратить ввод, нажмите Enter");
			do
			{
				buffS = in.nextLine();
				if(!buffS.equals(""))
				{
					if(mode != 0)
					{
						buffS = buffS.replace("x", "x1");
						buffS = buffS.replace("y", "x2");
						buffS = buffS.replace("z", "x3");
					}
					else
					{
						buffS = buffS.replace("y", "x2");
						buffS = buffS.replace("z", "x3");
					}
					base.addBasis(buffS);
				}
			} while(!buffS.equals(""));
			//base.settingMode();
		}
		catch(Throwable t)
		{
			System.out.println("Ошибка ввода, попробуйте еще раз.");
			//System.out.println(t);
		}
	}
	
	private static void SPolynom()
	{
		Scanner num = new Scanner(System.in);
		String buffS;
		BigPolinom first = new BigPolinom(1, "0");
		BigPolinom second = new BigPolinom(1, "0");
		ArrayList<Integer> order = new ArrayList<Integer>();
		int amount = 0;
		int k = 0;
		int i;
		System.out.println("Введите количество неизвестных");
		try
		{
			do
			{
				System.out.print("> ");
				amount = num.nextInt();
			} while(amount < 1);
			System.out.println("Введите построчно порядок неизвестных");
			while(order.size() != amount)
			{
				Scanner orderIn = new Scanner(System.in);
				k = orderIn.nextInt();
				if(k > 0 && k <= amount)
				{
					if(order.indexOf(k) == -1)
						order.add(k);
					else
					{
						System.out.println("Данный индекс уже был использован");
						System.out.println("Текущий порядок: " + order);
					}
				}
				else
					System.out.println("Такого индекса быть не может. Максимум " + amount);
			}
			for(i = 0; i < order.size(); i++)
				order.set(i, order.get(i)-1);
			System.out.println("Ввод многочленов имеет следующий вид: каждая переменная должна содержать индекс после себя, например, 45x1 + 7/5x2^7");
			if(amount < 4)
			{
				Scanner num2 = new Scanner(System.in);
				System.out.println("\nВведите любое число, отличное от нуля, чтобы использовать следующий вид переменных: x y z");
				System.out.print("> ");
				mode = num2.nextInt();
			}
			else
				mode = 0;
			System.out.println("\nПострочно вводите полиномы.");
			i = 0;
			do
			{
				Scanner input = new Scanner(System.in);
				buffS = input.nextLine();
				if(!buffS.equals(""))
				{
					if(mode != 0)
					{
						buffS = buffS.replace("x", "x1");
						buffS = buffS.replace("y", "x2");
						buffS = buffS.replace("z", "x3");
					}
					else
					{
						buffS = buffS.replace("y", "x2");
						buffS = buffS.replace("z", "x3");
					}
					if(i == 0)
						first = new BigPolinom(amount, buffS, order);
					else
						second = new BigPolinom(amount, buffS, order);
					i++;
				}
			} while(!buffS.equals("") && i < 2);
			buffS = first.sPolynom(second).toString();
			if(mode != 0)
			{
				buffS = buffS.replace("x1", "x");
				buffS = buffS.replace("x2", "y");
				buffS = buffS.replace("x3", "z");
			}
			System.out.println("S-полином: " + buffS);
		}
		catch(Throwable t)
		{
			System.out.println("Ошибка ввода, попробуйте еще раз.");
			//System.out.println(t);
		}
	}
	
	private static void generateExample()
	{
		Scanner num = new Scanner(System.in);
		String buffS;
		ArrayList<BigPolinom> polys = examples.getExample();
		ArrayList<Integer> order = polys.get(0).getMode();
		int amount, i;
		amount = polys.get(0).getFactors().get(0).getPowers().size();
		base.clearAll();
		base.setMaxVars(amount);
		base.setMode(order);
		try
		{
			if(amount < 4)
			{
				Scanner num2 = new Scanner(System.in);
				System.out.println("\nВведите любое число, отличное от нуля, чтобы использовать следующий вид переменных: x y z");
				System.out.print("> ");
				mode = num2.nextInt();
			}
			else
				mode = 0;
			for(i = 0; i < polys.size(); i++)
			{
				buffS = polys.get(i).toString();
				if(!buffS.equals(""))
					base.addBase(buffS);
			}
			System.out.println("Предлагаемый порядок: ");
			for(i = 0; i < order.size(); i++)
				System.out.print("x" + (order.get(i)+1) + " ");
			System.out.println("\nВведите out, чтобы увидеть текущую систему. Чтобы сформировать базис введите basis");
		}
		catch(Throwable t)
		{
			System.out.println("Ошибка ввода, попробуйте еще раз.");
			//System.out.println(t);
		}
	}
}