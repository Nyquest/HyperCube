import java.io.*;
import java.util.*;
public class HyperCube {
	//static String INPUT_FILE = "03.cube";
	static String INPUT_FILE = "15.cube";
	
	static String OUTPUT_FILE = "00.res";
	static Scanner scanner = null;
	static PrintWriter writer = null;
	public static void main(String[] args) {
		Cube c = new Cube();		
		try {//считывание исходных данных
			scanner = new Scanner(new BufferedReader(new FileReader(INPUT_FILE)));
			for(int i=0;i<Cube.X;i++){				
				for(int j=0;j<Cube.Y;j++){					
					c.a[i][j] = scanner.next().toUpperCase();
				}
			}
		} 
		catch (IOException exc) {			
			System.out.println("Can't open input file");
			return;
		}
		finally{
			if(scanner != null){
				scanner.close();
			}
		}
		pr("Исходный куб");
		c.showCube();
		
		//Алфавит вращений
		T[] abc = new T[]{T.R,T.R_,T.L,T.L_,T.F,T.F_,T.B,T.B_,T.U,T.U_,T.D,T.D_};//Допустимые вращения
		//Комбинации для желтого креста
		T[] comb1 = new T[]{T.F,T.U,T.R,T.U_,T.R_,T.F_};//"уголок"
		T[] comb2 = new T[]{T.F,T.R,T.U,T.R_,T.U_,T.F_};//"палка"				
		//Комбинации для желтой шапки
		T[] comb3 = new T[]{T.R,T.R,T.D,T.R_,T.U,T.U,T.R,T.D_,T.R_,T.U,T.U,T.R_};//"глаза"
		T[] comb4 = new T[]{T.R_,T.F_,T.L,T.F,T.R,T.F_,T.L_,T.F};//"уши"
		T[] comb5 = new T[]{T.R_,T.F_,T.L_,T.F,T.R,T.F_,T.L,T.F};//"кривые уши"
		T[] comb6 = new T[]{T.R,T.U,T.R_,T.U,T.R,T.U,T.U,T.R_};//"L"
		T[] comb7 = new T[]{T.R,T.U,T.U,T.R_,T.U_,T.R,T.U_,T.R_};//"приставка"
		T[] comb8 = new T[]{T.R,T.U,T.U,T.R,T.R,T.U_,T.R,T.R,T.U_,T.R,T.R,T.U,T.U,T.R};//"вертолет"
		T[] comb9 = new T[]{T.F, T.R,T.U,T.R_,T.U_, T.R,T.U,T.R_,T.U_, T.R,T.U,T.R_,T.U_, T.F_};//без названия
		
		//Комбинации для расстановки угловых кубиков на последнем слое
		T[] comb10 = new T[]{T.R,T.U,T.U,T.R_,T.U_,T.R,T.U,T.U,T.L_,T.U,T.R_,T.U_,T.L};//боковой
		T[] comb11 = new T[]{T.L_,T.U,T.R,T.U_,T.L,T.U,T.L_,T.U,T.R_,T.U_,T.L,T.U,T.U,T.R,T.U,T.U,T.R_};//диагональный
		
		//Комбинации для расстановки боковых кубиков на последнем слое
		T[] comb12 = new T[]{T.R,T.R,T.U,T.R,T.U,T.R_,T.U_,T.R_,T.U_,T.R_,T.U,T.R_};//Смещение боковых кубиков по треугольнику (по часовой)
		T[] comb13 = new T[]{T.R,T.U_,T.R,T.U,T.R,T.U,T.R,T.U_,T.R_,T.U_,T.R,T.R};//смещение боковых кубиков по треугольнику (против часовой)
		T[] comb14 = new T[]{T.R,T.B_,T.R_,T.B,T.F,T.R_,T.B_,T.F,T.R_,T.B,T.R,T.F,T.F,T.U};//"двойная диагональ"
		T[] comb15 = new T[]{T.R,T.R,T.L,T.L,T.D,T.R,T.R,T.L,T.L,T.U,T.U,T.R,T.R,T.L,T.L,T.D,T.R,T.R,T.L,T.L};//меняем местами четыре боковых элемента крест-накрест
		
		//Комбинации для "правильного" белого креста
		T[] comb16 = new T[]{T.R,T.D,T.R_,T.D_,T.R};//меняем два смежных элемента креста
		T[] comb17 = new T[]{T.R_,T.L,T.D,T.D,T.R,T.L_};//меняем два противоположных элемента креста
		//Комбинации для белой шапки
		T[] comb18 = new T[]{T.R,T.U,T.R_};//белый цвет расположен справа
		T[] comb19 = new T[]{T.F_,T.U_,T.F};//белый цвет расположен лицом к нам
		T[] comb20 = new T[]{T.R,T.F,T.R,T.R,T.F_,T.R_};//белый находится сверху
		
		//Комбинации для второго слоя
		T[] comb21 = new T[]{T.U, T.R,T.U,T.R_,T.U_, T.F_,T.U_,T.F};//элемент расположен слева от своего места
		T[] comb22 = new T[]{T.U_, T.F_,T.U_,T.F,T.U, T.R,T.U,T.R_};//элемент расположен справа от своего места
		
		boolean complete = false;
		while(true){
			//Проверка на завершенность сборки			
			if(c.checkComplete()){				
				System.out.println("Кубик собран!");
				complete = true;
				break;
			}
			if(c.checkAngularOnPlaces()){
				for(int t=0;t<=3;t++){					
					boolean find = false;
					T[][] combs = {comb12,comb13,comb14,comb15};
					for(T[] comb:combs){
						comb = Cube.transform(comb, t);
						Cube tmpCube = new Cube(c);
						tmpCube.combo(comb);
						if(tmpCube.checkComplete()){
							pr(Cube.getText(comb));							
							c.commitCombo(comb);
							find = true;
							break;
						}
					}
					if(find)break;
				}
			}else if(c.checkYellowHat()){
				T[][] combs = {comb10,comb11};
				//Постепенно проворачиваем верхную грань
				for(int shift=0;shift<=3;shift++){
					//pr("Вращаем желтую верхушку " + shift);
					boolean find = false;
					if(shift!=0){
						T[] comb = new T[]{T.U};
						
						pr(Cube.getText(comb));
						c.commitCombo(comb);						
					}
					for(int t=0;t<=3;t++){
						for(T[] comb:combs){
							comb = Cube.transform(comb, t);
							Cube tmpCube = new Cube(c);
							tmpCube.combo(comb);
							if(tmpCube.checkAngularOnPlaces()){
								pr("К желтой шапке");
								pr(Cube.getText(comb));
								c.commitCombo(comb);
								find = true;
								break;
							}
						}
						if(find)break;
					}
					if(find)break;
				}
			}else if(c.checkYellowCross()){
				pr("checkYellowCross()");
				for(int t=0;t<=3;t++){
					boolean find = false;
					T[][] combs = {comb3,comb4,comb5,comb6,comb7,comb8,comb9};
					for(T[]comb:combs){
						comb = Cube.transform(comb, t);
						Cube tmpCube = new Cube(c);
						tmpCube.combo(comb);
						if(tmpCube.checkYellowHat()){
							pr(Cube.getText(comb));
							c.commitCombo(comb);
							find = true;
							break;
						}
					}
					if(find)break;
				}
			}else if(c.checkTwoLayersComplete()){
				pr("checkTwoLayersComplete()");
				boolean find = false;
				for(int t=0;t<=3;t++){
					T[][] combs = {comb1,comb2};
					for(T[]comb:combs){
						comb = Cube.transform(comb, t);
						Cube tmpCube = new Cube(c);
						tmpCube.combo(comb);
						if(tmpCube.checkYellowCross()){
							pr(Cube.getText(comb));
							c.commitCombo(comb);
							find = true;
							break;							
						}
					}
					if(find)break;
				}
				if(!find){
					pr(Cube.getText(comb2));
					c.commitCombo(comb2);
					for(int t=0;t<=3;t++){
						T[][]combs = {comb1,comb2};
						for(T[]comb:combs){
							comb = Cube.transform(comb, t);
							Cube tmpCube = new Cube(c);
							tmpCube.combo(comb);
							if(tmpCube.checkYellowCross()){
								pr(Cube.getText(comb));
								c.commitCombo(comb);
								find = true;
								break;
							}
						}
						if(find)break;
					}
				}
			}else if(c.checkFirstLayerComplete()){
				pr("checkFirstLayerComplete()");				
				//pr("Оценка верхушки = " + c.getRateUpperLateral());
				//pr("Оценка центра = " + c.getRateCentralAngular());
				int rate = c.getRateCentralAngular();
				while(rate != 4){
					if(c.getRateUpperLateral() > 0){
						//переносим кубик с верхнего слоя на центральный
						T[][]combs1 = {new T[]{},new T[]{T.U},new T[]{T.U_},new T[]{T.U,T.U}};
						T[][]combs2 = {comb21,comb22};
						three:{
							for(T[] cmb1:combs1){
								for(T[] cmb2:combs2){
									for(int t=0;t<=3;t++){
										T[] comb=Result.series(cmb1, Cube.transform(cmb2, t));
										Cube tmpCube = new Cube(c);
										tmpCube.combo(comb);
										int curRate = tmpCube.getRateCentralAngular();
										if(curRate > rate){
											pr(Cube.getText(comb));
											c.commitCombo(comb);
											rate = curRate;
											break three;
										}
									}
								}
							}
						}
					}else{
						//выводим некорректный кубик из центрального слоя в верхний						
						for(int t=0;t<=3;t++){
							T[] comb = Cube.transform(comb21, t);
							Cube tmpCube = new Cube(c);
							tmpCube.combo(comb);
							if(tmpCube.getRateUpperLateral()>0 && tmpCube.getRateCentralAngular()==rate){
								pr(Cube.getText(comb));
								c.commitCombo(comb);
								break;
							}
						}
					}					
				}
			}else if(c.checkWhiteCross()){
				pr("checkWhiteCross()");				
				int rate = c.getRateLowerAngular();
				while(rate != 4){					
					if(c.getRateUpperAngular() > 0){												
						//переносим угловой кубик из верхнего слоя в нижний
						T[][]combs1 = {new T[]{},new T[]{T.U},new T[]{T.U_},new T[]{T.U,T.U}};
						T[][]combs2 = {comb18,comb19,comb20};
						three:{
							for(T[] cmb1:combs1){
								for(T[] cmb2:combs2){
									for(int t=0;t<=3;t++){
										T[] comb=Result.series(cmb1, Cube.transform(cmb2, t));
										Cube tmpCube = new Cube(c);
										tmpCube.combo(comb);
										int curRate = tmpCube.getRateLowerAngular();
										if(curRate > rate){
											pr(Cube.getText(comb));
											c.commitCombo(comb);
											rate = curRate;
											break three;
										}
									}
								}
							}
						}
					}else{						
						//выводим некорректный кубик из нижнего слоя в верхний						
						for(int t=0;t<=3;t++){
							T[] comb = Cube.transform(comb18, t);
							Cube tmpCube = new Cube(c);
							tmpCube.combo(comb);
							if(tmpCube.getRateUpperAngular()>0 && tmpCube.getRateLowerAngular()==rate){
								pr(Cube.getText(comb));
								c.commitCombo(comb);
								break;
							}
						}						
					}
				}
			}else if(c.checkSimpleCross()){
				pr("checkSimpleCross()");				
				T[][]combs1 = {new T[]{},new T[]{T.D},new T[]{T.D_},new T[]{T.D,T.D}};
				T[][]combs2 = {comb16,comb17};
				three:{
					for(T[] cmb1:combs1){					
						for(T[] cmb2:combs2){
							for(int t=0;t<=3;t++){
								T[] comb = Result.series(cmb1, Cube.transform(cmb2, t));								
								Cube tmpCube = new Cube(c);
								tmpCube.combo(comb);
								if(tmpCube.checkWhiteCross()){
									pr(Cube.getText(comb));
									c.commitCombo(comb);
									break three;
								}
							}
						}
					}
				}				
			}else{//строим простой крест
				pr("This is random cube");				
				//Начальная оценка, которую будем улучшать
				int rate = c.getRateSimpleCross();
				int initRate = rate;
				for(int i=0;i<4-initRate;i++){
					pr("cycle " + i);
					boolean find = false;//Найдено ли улучшение состояния
					Cube tmpCube;
					T[] comb;
					int curRate;
					//улучшение позиции за один ход
					for(T t1:abc){			
						tmpCube = new Cube(c);
						comb = new T[]{t1};
						tmpCube.combo(comb);
						curRate = tmpCube.getRateSimpleCross();						
						if(curRate > rate){//улучшение							
							pr("1: " + Cube.getText(comb));
							c.commitCombo(comb);
							rate = curRate;
							find = true;
							break;
						}
					}
					if(!find){					
						//улучшение позиции за два хода
						two:{
							for(T t1:abc){							
								for(T t2:abc){
									tmpCube = new Cube(c);
									comb = new T[]{t1,t2};
									tmpCube.combo(comb);
									curRate = tmpCube.getRateSimpleCross();
									if(curRate > rate){//улучшение
										pr("2: "+ Cube.getText(comb));
										c.commitCombo(comb);
										rate = curRate;
										find = true;
										break two;
									}
								}						
							}
						}
					}
					if(!find){					
						//улучшение позиции за три хода
						three:{
							for(T t1:abc){
								for(T t2:abc){
									for(T t3:abc){
										tmpCube = new Cube(c);
										comb = new T[]{t1,t2,t3};
										tmpCube.combo(comb);
										curRate = tmpCube.getRateSimpleCross();
										if(curRate > rate){//улучшение
											pr("3: " + Cube.getText(comb));
											c.commitCombo(comb);
											rate = curRate;
											find = true;
											break three;
										}
									}								
								}							
							}
						}
					}					
				}
			}
			
			
			break;//Заглушка
		}
		if(complete){
			System.out.println("\tКубик полностью собран");
		}else{
			System.out.println("КУБИК НЕ СОБРАН)))))");
		}
		
		/*
		try {//Вывод решения задачи
			writer = new PrintWriter(new FileWriter(OUTPUT_FILE));
			for(int i=0;i<Cube.X;i++){
				for(int j=0;j<Cube.Y;j++){
					writer.write(c.a[i][j] + ",");
				}
				writer.write("\n");
			}
		}
		catch (IOException e) {
			System.out.println("Can't open output file");
			return;
		}
		finally{
			if(writer != null){
				writer.close();
			}
		}
		*/
		pr("ФИНАЛЬНАЯ КОМБИНАЦИЯ:");		
		pr(Cube.getText(Result.finalComb).trim());
		pr("EOF");
	}
	static void pr(String message){
		System.out.println(message);
	}
	static void pr_(String message){
		System.out.print(message);
	}	
}
class Result{
	static T[] finalComb = {};
	//Объединение алгоритмов в общую серию
	static void addCobmo(T[] combo){
		int len1 = finalComb.length;
		int len2 = combo.length;		
		T[] ret = new T[len1 + len2];
		int i = 0;
		for(;i<len1;i++){
			ret[i] = finalComb[i];
		}
		for(int j=0;j<len2;j++){
			ret[i+j] = combo[j];
		}
		/*
		for(int k=0;k<len1+len2;k++){
			Cube.pr(k + " " + ret[k].toString());
		}
		*/
		finalComb = ret;
	}
	static T[] series(T[] t1, T[] t2){		
		int len1 = t1.length;
		int len2 = t2.length;
		//Cube.pr("series " + len1 + " " + len2);
		T[] ret = new T[len1+len2];
		int i = 0;
		for(;i<len1;i++){
			ret[i] = t1[i];
			//pr(i + "i " + t1[i]);
		}
		for(int j=0;j<len2;j++){
			ret[i + j] = t2[j];
			//pr((i+j) + "j " + t2[j]);
		}
		return ret;
	}
}
class Cube{
	// 1 - W - белая сторона	
	// 2 - O - оранжевая сторона	
	// 3 - B - синяя сторона	
	// 4 - R - красная сторона	
	// 5 - G - зеленая сторона	
	// 6 - Y - желтая сторона
	static final int X=6;//Кол-во сторон
	static final int Y=9;//Кол-во кубиков на одной стороне
	//static T[] finalComb = new T[0];	
	
	String[][]a = new String[X][Y];
	
	//Конструктор по умолчанию
	Cube(){
		
	}
	//Конструктор
	Cube(Cube source){
		for(int i=0;i<X;i++){
			for(int j=0;j<Y;j++){
				a[i][j] = source.a[i][j];
			}
		}
	}
	//Показать кубик
	void showCube(){
		String text="";
		for(int i=0;i<X;i++){
			for(int j=0;j<Y;j++){
				text += a[i][j] + " ";
			}
			text += "\n";
		}
		System.out.println(text);
	}
	//Проверка на завершенность сборки
	boolean checkComplete(){
		//System.out.print("checkComplete()");
		boolean check = true;
		for(int i=0;i<X;i++){
			for(int j=0;j<Y;j++){				
				if( a[i][j].equals(a[i][4]) == false ){
					check = false;
				}
			}
		}
		//System.out.println(" := " + check);
		return check;
	}
	//Проверка - боковые по местам (последний шаг)
	//boolean checkLateralOnPlaces(){
	//	boolean check = false;
	//	return check;
	//}
	
	//Проверка - угловые по местам (предпоследний шаг)
	boolean checkAngularOnPlaces(){
		//pr_("checkAngularOnPlaces()");
		boolean check = true;
		if(!checkYellowHat()){
			check = false;
		}else{
			for(int i=0;i<=3;i++){
				for(int j=0;j<=2;j+=2){
					if(a[i][j].equals(a[i][4]) == false){
						check = false;
					}
				}
			}
		}
		
		//pr(" := " + check);
		return check;		
	}
	//Проверка - желтая "шапка" на последней стороне
	boolean checkYellowHat(){
		//pr_("checkYellowHat()");
		boolean check = true;
		if(!checkTwoLayersComplete()){
			check = false;
		}else{
			for(int i=0;i<Y;i++){
				if(a[5][i].equals(a[5][4]) == false){
					check = false;
				}
			}
		}
		//pr(" := " + check);
		return check;
	}
	//Проверка - крест на последней стороне
	boolean checkYellowCross(){
		//pr_("checkYellowCross()");
		boolean check = true;		
		if(!checkTwoLayersComplete()){
			check = false;
		}else{
			for(int i=0;i<Y;i++){
				if(i % 2 == 1){					
					if(a[5][i].equals(a[5][4]) == false){
						check = false;
					}
				}
			}
		}
		//pr(" := " + check);
		return check;
	}
	//Проверка - завершена сборка двух слоев
	boolean checkTwoLayersComplete(){
		//pr_("checkTwoLayersComplete()");
		boolean check = true;
		if(!checkWhiteHat()){
			check = false;
		}else{
			for(int i=0;i<=3;i++){
				for(int j=3;j<Y;j++){
					if(a[i][j].equals(a[i][4]) == false){
						check = false;
					}
				}
			}
		}
		//pr(" := " + check);
		return check;
	}
	//Проверка - завершен первый слой
	boolean checkFirstLayerComplete(){
		//pr_("checkFirstLayerComplete()");
		boolean check = true;
		if(!checkWhiteHat()){
			check = false;
		}else{
			for(int i=0;i<=3;i++){
				for(int j=6;j<Y;j++){
					if(a[i][j].equals(a[i][4]) == false){
						check = false;
					}
				}
			}
		}
		//pr(" := " + check);
		return check;
	}
	//Проверка - белая "шапка" на начальной стороне
	boolean checkWhiteHat(){
		//pr_("checkWhiteHat()");
		boolean check = true;
		for(int i=0;i<Y;i++){
			if(a[4][i].equals(a[4][4]) == false){
				check = false;
			}
		}
		//pr(" := " + check);
		return check;
	}
	//Проверка - белый крест на начальной стороне
	boolean checkSimpleCross(){
		//pr_("checkSimpleCross()");
		boolean check = true;
		for(int i=0;i<Y;i++){
			if(i % 2 == 1){
				if(a[4][i].equals(a[4][4]) == false){
					check=false;
				}
			}
		}
		//pr(" := " + check);
		return check;
	}
	//Проверка - "правильный" белый креста на начальной стороне
	boolean checkWhiteCross(){
		boolean check = true;
		if(!checkSimpleCross()){
			check = false;
		}else{
			for(int i=0;i<=3;i++){
				if(a[i][7].equals(a[i][4]) == false){
					check = false;
					break;
				}
			}
		}
		return check;
	}
	
	static void pr(String message){
		System.out.println(message);
	}
	static void pr_(String message){
		System.out.print(message);
	}
	//Вращение вправо (по часовой)
	void rotateRight(){
		//pr("rotateRight()");
		//Сдвиг кольца
		String swap1 = a[5][0];
		String swap2 = a[5][1];
		String swap3 = a[5][2];
		
		a[5][0] = a[1][2];
		a[5][1] = a[1][5];
		a[5][2] = a[1][8];
		
		a[1][2] = a[4][8];
		a[1][5] = a[4][7];
		a[1][8] = a[4][6];
		
		a[4][8] = a[3][6];
		a[4][7] = a[3][3];
		a[4][6] = a[3][0];
		
		a[3][6] = swap1;
		a[3][3] = swap2;
		a[3][0] = swap3;
		
		//Сдвиг квадрата
		swap1 = a[2][2];
		swap2 = a[2][1];
		
		a[2][2] = a[2][0];
		a[2][1] = a[2][3];
		
		a[2][0] = a[2][6];
		a[2][3] = a[2][7];
		
		a[2][6] = a[2][8];
		a[2][7] = a[2][5];
		
		a[2][8] = swap1;
		a[2][5] = swap2;
	}
	//Вращение вправо (против часовой)
	void rotateRightInverse(){
		//pr("rotateRightInverse()");
		rotateRight();
		rotateRight();
		rotateRight();
	}
	//Вращение влево (по часовой)
	void rotateLeft(){
		//pr("rotateLeft()");
		//Сдвиг кольца
		String swap1 = a[1][0];
		String swap2 = a[1][3];
		String swap3 = a[1][6];
		
		a[1][0] = a[5][6];
		a[1][3] = a[5][7];
		a[1][6] = a[5][8];
		
		a[5][6] = a[3][8];
		a[5][7] = a[3][5];
		a[5][8] = a[3][2];
		
		a[3][8] = a[4][2];
		a[3][5] = a[4][1];
		a[3][2] = a[4][0];
		
		a[4][2] = swap1;
		a[4][1] = swap2;
		a[4][0] = swap3;
		
		//Сдвиг квадрата
		swap1 = a[0][2];
		swap2 = a[0][1];
		
		a[0][2] = a[0][0];
		a[0][1] = a[0][3];
		
		a[0][0] = a[0][6];
		a[0][3] = a[0][7];
		
		a[0][6] = a[0][8];
		a[0][7] = a[0][5];
		
		a[0][8] = swap1;
		a[0][5] = swap2;
	}
	//Вращение влево (против часовой)
	void rotateLeftInverse(){
		//pr("rotateLeftInverse()");
		rotateLeft();
		rotateLeft();
		rotateLeft();
	}
	//Вращение фронтальной части (по часовой)
	void rotateFront(){
		//pr("rotateFront()");
		//Сдвиг кольца
		String swap1 = a[5][8];
		String swap2 = a[5][5];
		String swap3 = a[5][2];
		
		a[5][8] = a[0][8];
		a[5][5] = a[0][5];
		a[5][2] = a[0][2];
		
		a[0][8] = a[4][8];
		a[0][5] = a[4][5];
		a[0][2] = a[4][2];
		
		a[4][8] = a[2][0];
		a[4][5] = a[2][3];
		a[4][2] = a[2][6];
		
		a[2][0] = swap1;
		a[2][3] = swap2;
		a[2][6] = swap3;
		
		//Сдвиг квадрата
		swap1 = a[1][2];
		swap2 = a[1][1];
		
		a[1][2] = a[1][0];
		a[1][1] = a[1][3];
		
		a[1][0] = a[1][6];
		a[1][3] = a[1][7];
		
		a[1][6] = a[1][8];
		a[1][7] = a[1][5];
		
		a[1][8] = swap1;
		a[1][5] = swap2;		
	}
	//Вращение фронтальной части (против часовой)
	void rotateFrontInverse(){
		//pr("rotateFrontInverse()");
		rotateFront();
		rotateFront();
		rotateFront();
	}
	//Вращение задней части (по часовой)
	void rotateBack(){
		//pr("rotateBack()");
		//Сдвиг кольца
		String swap1 = a[5][6];
		String swap2 = a[5][3];
		String swap3 = a[5][0];
		
		a[5][6] = a[2][2];
		a[5][3] = a[2][5];
		a[5][0] = a[2][8];
		
		a[2][2] = a[4][6];
		a[2][5] = a[4][3];
		a[2][8] = a[4][0];
		
		a[4][6] = a[0][6];
		a[4][3] = a[0][3];
		a[4][0] = a[0][0];
		
		a[0][6] = swap1;
		a[0][3] = swap2;
		a[0][0] = swap3;
		
		//Сдвиг квадрата
		swap1 = a[3][2];
		swap2 = a[3][1];
		
		a[3][2] = a[3][0];
		a[3][1] = a[3][3];
		
		a[3][0] = a[3][6];
		a[3][3] = a[3][7];
		
		a[3][6] = a[3][8];
		a[3][7] = a[3][5];
		
		a[3][8] = swap1;
		a[3][5] = swap2;		
	}
	//Вращение задней части (против часовой)
	void rotateBackInverse(){
		//pr("rotateBackInverse()");
		rotateBack();
		rotateBack();
		rotateBack();	
	}
	//Вращение верхней части (по часовой)
	void rotateUpper(){
		//pr("rotateUpper()");
		//Сдвиг кольца
		String swap1 = a[1][0];
		String swap2 = a[1][1];
		String swap3 = a[1][2];
		
		a[1][0] = a[2][0];
		a[1][1] = a[2][1];
		a[1][2] = a[2][2];
		
		a[2][0] = a[3][0];
		a[2][1] = a[3][1];
		a[2][2] = a[3][2];
		
		a[3][0] = a[0][0];
		a[3][1] = a[0][1];
		a[3][2] = a[0][2];
		
		a[0][0] = swap1;
		a[0][1] = swap2;
		a[0][2] = swap3;
		
		//Сдвиг квадрата
		swap1 = a[5][0];
		swap2 = a[5][3];
		
		a[5][0] = a[5][6];
		a[5][3] = a[5][7];
		
		a[5][6] = a[5][8];
		a[5][7] = a[5][5];
		
		a[5][8] = a[5][2];
		a[5][5] = a[5][1];
		
		a[5][2] = swap1;
		a[5][1] = swap2;		
		
	}
	//Вращение верхней части (против часовой)
	void rotateUpperInverse(){
		//pr("rotateUpperInverse()");
		rotateUpper();
		rotateUpper();
		rotateUpper();
	}
	//Вращение нижней части (по часовой)
	void rotateLower(){
		//pr("rotateLower()");
		//Сдвиг кольца
		String swap1 = a[1][6];
		String swap2 = a[1][7];
		String swap3 = a[1][8];
		
		a[1][6] = a[0][6];
		a[1][7] = a[0][7];
		a[1][8] = a[0][8];
		
		a[0][6] = a[3][6];
		a[0][7] = a[3][7];
		a[0][8] = a[3][8];
		
		a[3][6] = a[2][6];
		a[3][7] = a[2][7];
		a[3][8] = a[2][8];
		
		a[2][6] = swap1;
		a[2][7] = swap2;
		a[2][8] = swap3;
		
		//Сдвиг квадрата
		swap1 = a[4][8];
		swap2 = a[4][5];
		
		a[4][8] = a[4][2];
		a[4][5] = a[4][1];
		
		a[4][2] = a[4][0];
		a[4][1] = a[4][3];
		
		a[4][0] = a[4][6];
		a[4][3] = a[4][7];
		
		a[4][6] = swap1;
		a[4][7] = swap2;		

	}
	//Вращение нижней части (против часовой)
	void rotateLowerInverse(){
		//pr("rotateLowerInverse()");
		rotateLower();
		rotateLower();
		rotateLower();
	}
	//Применение комбинаций вращений к кубу
	void combo(T[] arr){
		int len = arr.length;		
		for(int i=0;i<len;i++){			
			switch(arr[i]){
			case R:rotateRight();break;
			case R_:rotateRightInverse();break;
			case L:rotateLeft();break;
			case L_:rotateLeftInverse();break;
			case F:rotateFront();break;
			case F_:rotateFrontInverse();break;
			case B:rotateBack();break;
			case B_:rotateBackInverse();break;
			case U:rotateUpper();break;
			case U_:rotateUpperInverse();break;
			case D:rotateLower();break;
			case D_:rotateLowerInverse();break;
			default:
				pr("Unknown rotate");
			}			
		}
	}
	//Вывести алгоритм в текстовом виде
	static String getText(T[] arr){
		String ret = "";
		int len = arr.length;		
		for(int i=0;i<len;i++){			
			switch(arr[i]){
			case R:ret+=" П";break;
			case R_:ret+=" П'";break;
			case L:ret+=" Л";break;
			case L_:ret+=" Л'";break;
			case F:ret+=" Ф";break;
			case F_:ret+=" Ф'";break;
			case B:ret+=" З";break;
			case B_:ret+=" З'";break;
			case U:ret+=" В";break;
			case U_:ret+=" В'";break;
			case D:ret+=" Н";break;
			case D_:ret+=" Н'";break;			
			}			
		}
		return ret;
	}
	//Трансформация алгоритма
	static T[] transform(T[] arr,int transVal){
		//pr("transform " + transVal);
		int len = arr.length;
		T[] ret = new T[len];
		for(int i=0;i<len;i++){			
			T t = arr[i];
			for(int j=0;j<transVal;j++){
				switch(t){
				case R:t=T.F;break;
				case R_:t=T.F_;break;
				case L:t=T.B;break;
				case L_:t=T.B_;break;
				case F:t=T.L;break;
				case F_:t=T.L_;break;
				case B:t=T.R;break;
				case B_:t=T.R_;break;							
				}
			}
			ret[i] = t;
		}		
		return ret;
	}
	
	//Применить комбинацию к кубу
	void commitCombo(T[] arr){
		//pr("commitCombo()");
		combo(arr);		
		Result.addCobmo(arr);
	}
	//Вернуть оценку позиции верхних боковых кубиков
	int getRateUpperLateral(){
		//pr("getRateUpperLateral()");
		int ret = 0;
		//Фронтальный кубик
		if(!a[5][5].equals(a[5][4]) && !a[1][1].equals(a[5][4])){
			//pr("FrontRate");
			ret++;
		}
		//Правый кубик
		if(!a[5][1].equals(a[5][4]) && !a[2][1].equals(a[5][4])){
			//pr("RightRate");
			ret++;
		}
		//Задний кубик
		if(!a[5][3].equals(a[5][4]) && !a[3][1].equals(a[5][4])){
			//pr("BackRate");
			ret++;
		}
		//Левый кубик
		if(!a[5][7].equals(a[5][4]) && !a[0][1].equals(a[5][4])){
			//pr("LeftRate");
			ret++;
		}
		
		return ret;
	}
	//Вернуть оценку позиции центральных угловых кубиков
	int getRateCentralAngular(){
		//pr("getRateCentralAngular()");
		int ret = 0;
		//На пересечении фронтальной и правой стороны
		if(a[1][5].equals(a[1][4]) && a[2][3].equals(a[2][4])){
			//pr("Front-Right Rate");
			ret++;
		}
		//На пересечении правой и задней стороны
		if(a[2][5].equals(a[2][4]) && a[3][3].equals(a[3][4])){
			//pr("Right-Back Rate");
			ret++;
		}
		//На пересечении задней и левой стороны
		if(a[3][5].equals(a[3][4]) && a[0][3].equals(a[0][4])){
			//pr("Back-Left Rate");
			ret++;
		}
		//На пересечении левой и фронтальной стороны
		if(a[0][5].equals(a[0][4]) && a[1][3].equals(a[1][4])){
			//pr("Left-Front Rate");
			ret++;
		}
		return ret;
	}
	//Вернуть оценку позиции верхних угловых кубиков
	int getRateUpperAngular(){
		//pr("getRateUpperAngular()");
		int ret = 0;
		//На пересечении фронтальной и правой стороны
		if(a[1][2].equals(a[4][4]) || a[2][0].equals(a[4][4]) || a[5][2].equals(a[4][4])){
			//pr("Front-Right Rate");
			ret++;
		}
		//На пересечении правой и задней стороны
		if(a[2][2].equals(a[4][4]) || a[3][0].equals(a[4][4]) || a[5][0].equals(a[4][4])){
			//pr("Right-Back Rate");
			ret++;
		}
		//На пересечении задней и левой стороны
		if(a[3][2].equals(a[4][4]) || a[0][0].equals(a[4][4]) || a[5][6].equals(a[4][4])){
			//pr("Back-Left Rate");
			ret++;
		}
		//На пересечении левой и фронтальной стороны
		if(a[0][2].equals(a[4][4]) || a[1][0].equals(a[4][4]) || a[5][8].equals(a[4][4])){
			//pr("Left-Front Rate");
			ret++;
		}
		return ret;
	}
	//Вернуть оценку позиции нижних угловых кубиков
	int getRateLowerAngular(){
		//pr("getRateLowerAngular()");
		int ret = 0;
		//На пересечении фронтальной и правой стороны
		if(a[1][8].equals(a[1][4]) && a[2][6].equals(a[2][4])){
			//pr("Front-Right Rate");
			ret++;
		}
		//На пересечении правой и задней стороны
		if(a[2][8].equals(a[2][4]) && a[3][6].equals(a[3][4])){
			//pr("Right-Back Rate");
			ret++;
		}
		//На пересечении задней и левой стороны
		if(a[3][8].equals(a[3][4]) && a[0][6].equals(a[0][4])){
			//pr("Back-Left Rate");
			ret++;
		}		
		//На пересечении левой и фронтальной стороны
		if(a[0][8].equals(a[0][4]) && a[1][6].equals(a[1][4])){
			//pr("Left-Front Rate");
			ret++;
		}
		return ret;
	}
	//Вернуть оценку простого белого креста на нижней (начальной) стороне
	int getRateSimpleCross(){
		//pr("getRateSimpleCross()");
		int ret = 0;
		if(a[4][5].equals(a[4][4]))ret++;
		if(a[4][7].equals(a[4][4]))ret++;
		if(a[4][3].equals(a[4][4]))ret++;
		if(a[4][1].equals(a[4][4]))ret++;
		return ret;
	}
}
enum T{R,R_,L,L_,F,F_,B,B_,U,U_,D,D_};
