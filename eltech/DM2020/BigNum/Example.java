package eltech.DM2020.BigNum;

import java.util.*;

public class Example
{
	private ArrayList<BigPolinom> polynoms0 = new ArrayList<BigPolinom>();		//Заготовленная система 1
	private ArrayList<BigPolinom> polynoms1 = new ArrayList<BigPolinom>();		//Заготовленная система 2
	private ArrayList<BigPolinom> polynoms2 = new ArrayList<BigPolinom>();		//Заготовленная система 3
	private ArrayList<BigPolinom> polynoms3 = new ArrayList<BigPolinom>();		//Заготовленная система 4
	private ArrayList<BigPolinom> polynoms4 = new ArrayList<BigPolinom>();		//Заготовленная система 5
	private ArrayList<BigPolinom> polynoms5 = new ArrayList<BigPolinom>();		//Заготовленная система 6
	private ArrayList<BigPolinom> polynoms6 = new ArrayList<BigPolinom>();		//Заготовленная система 7
	private static final Random random = new Random();
	
	public Example()
	{	
		ArrayList<Integer> dj = new ArrayList<Integer>();
		dj.add(0);
		dj.add(1);
		dj.add(2);
		
		polynoms0.add(new BigPolinom(2, "x2^2-1" ,dj)); //y^2-1
		polynoms0.add(new BigPolinom(2, "x1x2-1" ,dj)); //xy-1
		
		polynoms1.add(new BigPolinom(3, "x1x2-x3^2-x3" ,dj)); //xy-z^2-z
		polynoms1.add(new BigPolinom(3, "x1^2+x1-x2x3" ,dj)); //x^2+x-yz
		polynoms1.add(new BigPolinom(3, "x1x3-x2^2-x2" ,dj)); //xz-y^2-y
	
		polynoms2.add(new BigPolinom(3, "x1^3x2x3-x1x2+x1-x3" ,dj)); 	//x^3yz-xy+x-z
		polynoms2.add(new BigPolinom(3, "x1x2-x2^2+x3" ,dj));			//xy-y^2+z
		polynoms2.add(new BigPolinom(3, "x2^3-x3" ,dj));				//y^3-z
		
		polynoms3.add(new BigPolinom(2, "-6x1^2+6x1-6x2^2+6x2-2" ,dj));  //-6x^2+6x-6y^2+6y-2
		polynoms3.add(new BigPolinom(2, "4x1^3-6x1^2-4x2^3+6x2^2" ,dj)); //4x^3-6x^2-4y^3+6y^2
		
		polynoms4.add(new BigPolinom(3, "x1x2-x3^2-x3" ,dj));	//xy-z^2-z
		polynoms4.add(new BigPolinom(3, "x1^2-x1-x2x3" ,dj)); //x^2-x-yz
		polynoms4.add(new BigPolinom(3, "x1x3-x2^2-x2" ,dj)); //xz-y^2-y
		
		polynoms5.add(new BigPolinom(3, "x1^8x2x3-6x1-x3" ,dj));	//x^8yz-6x-z
		polynoms5.add(new BigPolinom(3, "x1x2-5x2^2+7x3" ,dj));	//xy-5y^2+7z
		polynoms5.add(new BigPolinom(3, "7x2^2-3x3" ,dj));		//7y^2-3z
	}
	
	public ArrayList<BigPolinom> getExample()
	{
		int i = random.nextInt(6);
		switch(i)
		{
			case 0:
				return polynoms0;
			case 1:
				return polynoms1;
			case 2:
				return polynoms2;
			case 3:
				return polynoms3;
			case 4:
				return polynoms4;
			case 5:
				return polynoms5;
			case 6:
				return polynoms6;
		}
		return polynoms0;
	}
}