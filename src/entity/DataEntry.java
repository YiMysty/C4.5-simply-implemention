package entity;

import java.util.ArrayList;
public class DataEntry {
	ArrayList<String> Data = new ArrayList<String>();
	public void insertEntry(String o){
		Data.add(o);
	}
	public String getData(int order){
		return Data.get(order);
	}
	public void setData(int order,String value){
		Data.set(order, value);
	}
	public int length(){
		return Data.size();
	}
	public String toDataString(){
		String returnMessage = "(";
		for(String s:Data){
			returnMessage +=" "+s+" ";
		}
		returnMessage +=")";
		return returnMessage;
	}
	public String toString(){
		String returnMessage = "";
		if(Data.size()==0)
			return returnMessage;
		returnMessage = Data.get(0);
		for(int i=1;i<Data.size();i++){
			returnMessage +=","+Data.get(i);
		}
		return returnMessage;
	}
}
