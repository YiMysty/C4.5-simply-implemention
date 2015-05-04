package entity;
import java.util.ArrayList;

import tools.Const;
public class Schema {
	ArrayList<String> attributesName;
	ArrayList<Character> type;
	ArrayList<Character> DataType;
	public Schema(){
		attributesName = new ArrayList<String>();
		type = new ArrayList<Character>();
		//To determine is discrete or continuous...
		DataType = new ArrayList<Character>();
	}
	public void addAttributesName(String name){
		attributesName.add(name);
	}
	public void addType(Character t){
		type.add(t);
	}
	public void addDataType(Character T){
		DataType.add(T);
	}
	public void setDataType(int index,Character T){
		DataType.set(index, T);
	}
	public char getType(int order){
		return type.get(order);
	}
	public String getName(int order){
		return attributesName.get(order);
	}
	public boolean isDiscrete(int index){
		return DataType.get(index)==Const.DISCRETE;
	}
	public int length(){
		return attributesName.size();
	}
	public void showSchema(){
		System.out.println("===================");
		for(int i=0;i<attributesName.size();i++){
			System.out.println(i+" Attributes: "+attributesName.get(i)+" Type: "+type.get(i)+" IsDiscrete:"+isDiscrete(i));
		}
		System.out.println("====================");
	}
	public String toString(){
		if(attributesName.size()==0)
			return "";
		String value = attributesName.get(0);
		for(int i=1;i<attributesName.size();i++){
			value +=","+attributesName.get(i);
		}
		return value;
	}
}
