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
	private static Scanner in = new Scanner(System.in);
	private static int mode;
	
	public static void start()
	{
		System.out.println("Для ознакомления с программой введите \"?\" или \"help\"");
		Interface();
		
		/*ArrayList<Integer> dj = new ArrayList<Integer>();
		dj.add(1);
		dj.add(0);
		dj.add(2);
		BigPolinom test = new BigPolinom(3, "-1944/49*x2^10 + 9720/49*x2^9 - 18630/49*x2^8 + 16200/49*x2^7 - 5940/49*x2^6 + 1944/49*x2^5 - 2385/49*x2^4 + 990/49*x2^3 - 663/98*x2^2 + 753/98*x2 - 559/392-1944/49*x2^10", dj);
		BigPolinom test2 = new BigPolinom(3, "x1^3*x2^2 + 1*x1^2", dj);
		BigMonom test3 = new BigMonom(3, "x1x2", dj);
		System.out.println(test.getHighMonom().getHighPower());
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
							System.out.println("Вы не вводили уравнения. Введите input(или in), чтобы ввести их.");
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
							System.out.println("Вы не вводили уравнения. Введите input(или in), чтобы ввести их.");
						break;
					}
					case "outputbasis":{}
					case "outb":
					{
						if(formed == 1)
							outputBasis();
						else
							if(inputed != 0)
								System.out.println("Вы еще не сформировали базис. Введите basis, чтобы сформировать его.");
							else
								System.out.println("Вы не вводили уравнения. Введите input(или in), чтобы ввести их.");
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
							System.out.println("Необходимо ввести уравнения и/или сформировать базис для них.");
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
				//System.out.println("Упс... Произошла неизвестная ошибка!");
				System.out.println(t);
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
		int amount = 0;
		int curMode = 0;
		System.out.println("Введите количество неизвестных и режим сортировки через пробел");
		try
		{
			do
			{
				System.out.print("> ");
				amount = num.nextInt();
			} while(amount < 1);
			System.out.println("Ввод многочленов имеет следующий вид: каждая переменная должна содержать индекс после себя, например, 45x1 + 7/5x2^7");
			base.clearAll();
			base.setMaxPower(amount);
			//base.setMode(curMode);
			if(amount < 4)
			{
				Scanner num2 = new Scanner(System.in);
				System.out.println("\nВведите любое число, отличное от нуля, чтобы использовать следующий вид переменных: x y z");
				System.out.print("> ");
				mode = num2.nextInt();
			}
			else
				mode = 0;
			System.out.println("\nПострочно вводите уравнения. Чтобы прекратить ввод, нажмите Enter");
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
			base.settingMode();
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
}