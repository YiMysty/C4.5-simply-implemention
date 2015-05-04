package entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import tools.Tool;
@SuppressWarnings("rawtypes")
public class Target {
	HashMap<Object,Integer> map;
	int DataSize = 0;
	public Target(){
		map = new HashMap<Object,Integer>();
		
	}
	public Target addToMap(Object o){
		if(map.containsKey(o)){
			map.put(o,map.get(o)+1);
		}else{
			map.put(o, 1);
		}
		DataSize++;
		return this;
	}
	public Target removeOne(Object o){
		if(map.containsKey(o)){
			DataSize--;
			map.put(o, map.get(o)-1);
		}
		return this;
	}
	public double calculateGain(){
		double info = 0;
		double sum = 0;
		ArrayList<Double> buffer = new ArrayList<Double>();
		Iterator<Entry<Object, Integer>> it = map.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry entry = (Map.Entry) it.next();
			sum +=Double.parseDouble(entry.getValue()+"");
			buffer.add(Double.parseDouble(entry.getValue()+""));
		}
		for(Double d:buffer){
			info +=(d/sum)*Tool.log2(d/sum);
		}
		return 0-info;
	}
	public int getDataSize(){
		return this.DataSize;
	}
	public Object getTheMostOne(){
		int ammount = -1;
		Object returnVal = null;
		Iterator<Entry<Object, Integer>> it = map.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry entry = (Map.Entry) it.next();
			if((int)entry.getValue()>ammount){
				ammount = (int)entry.getValue();
				returnVal = entry.getKey();
			}
		}
		return returnVal;
	}
}
