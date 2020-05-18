package eltech.DM2020.BigNum;

import java.util.*;
import java.math.*;
import java.io.*;

/**
* Класс, который содержит интерфейс
* @version 0.01
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
		
		/*BigPolinom test = new BigPolinom(3, "-379184395505189595953/2007878116973328684*x2*x3^4 - 116148539251300772714/501969529243332171*x2*x3^3 - 271120228478818833391/2007878116973328684*x2*x3^2 - 29*x2*x3 - 159665/104411*x3^17 - 56124369097423/6410174269548*x3^16 - 20022098482313825789/2007878116973328684*x3^15 + 3347249/104411*x3^14 + 85531629101083/3205087134774*x3^13 + 38683887067578168928/501969529243332171*x3^12 + 15734783117727584653/669292705657776228*x3^11 - 63148301252300456741/334646352828888114*x3^10 - 166993230441965617421/669292705657776228*x3^9 - 69299120396104279147/334646352828888114*x3^8 + 51391695579123955487/669292705657776228*x3^7 + 463332964317368474473/1003939058486664342*x3^6 + 85474025902389390929/167323176414444057*x3^5 + 174434264177173975469/669292705657776228*x3^4 + 10462338910884304341/111548784276296038*x3^3 + 6292227882697869467/143419865498094906*x3^2");
		System.out.println(test);
		test.divideByHighCoef();
		System.out.println(test);*/
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
							formBasis();
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
				System.out.println("Упс... Произошла неизвестная ошибка!");
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
		System.out.print("Введите количество неизвестных: ");
		try
		{
			do
			{
				amount = num.nextInt();
			} while(amount < 1);
			System.out.println("Ввод многочленов имеет следующий вид: каждая переменная должна содержать индекс после себя, например, 45x1 + 7/5x2^7");
			base.clearBasis();
			base.setMaxPower(amount);
			if(amount < 4)
			{
				Scanner num2 = new Scanner(System.in);
				System.out.println("\nВведите любое число, отличное от нуля, чтобы использовать следующий вид переменных: x y z");
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
		}
		catch(Throwable t)
		{
			System.out.println("Ошибка ввода, попробуйте еще раз.");
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
		System.out.println("Базис формируется...");
		base.doActions();
		System.out.println("Формирование базиса прошло успешно! Для вывода введите outbasis(или outb)");
	}
	
	private static void dicision()
	{
			base.outputDecision();
	}
}