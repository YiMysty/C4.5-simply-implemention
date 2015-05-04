package tools;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Tool {
	public static double log2(double val){
		return Math.log(val)/Math.log(2);
	}
	public static boolean DoubleEqual(double a,double b){
		double result = Math.abs(a-b);
		return result<=0.00001;
	}
	public static double div(String val1,String val2){
		BigDecimal value1 = new BigDecimal(val1);
		BigDecimal value2 = new BigDecimal(val2);
		return value1.divide(value2,10,RoundingMode.CEILING).doubleValue();
	}
	public static int comp(String val1,String val2){
		BigDecimal value1 = new BigDecimal(val1);
		BigDecimal value2 = new BigDecimal(val2);
		if(value1.subtract(value2).doubleValue()>0){
			return 1;
		}else if(value1.subtract(value2).doubleValue()<0){
			return -1;
		}
		return 0;
	}
	public static double add(String val1,String val2){
		BigDecimal value1 = new BigDecimal(val1);
		BigDecimal value2 = new BigDecimal(val2);
		return value1.add(value2).doubleValue();
	}
	public static boolean cmp(String val1,String val2){
		BigDecimal value1 = new BigDecimal(val1);
		BigDecimal value2 = new BigDecimal(val2);
		if(value1.subtract(value2).doubleValue()<=0)
			return true;
		return false;
	}
	public static boolean equal(String val1,String val2){
		BigDecimal value1 = new BigDecimal(val1);
		BigDecimal value2 = new BigDecimal(val2);
		return Math.abs(value1.subtract(value2).doubleValue())<=Const.EQUAL;
	}
}
