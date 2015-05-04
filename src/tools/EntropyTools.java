package tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import dataloader.ConfigReader;
import entity.DataEntry;
import entity.Schema;
import entity.SplitResult;

public class EntropyTools {
	public static SplitResult getSplitResult(int order, Schema schema,
			List<DataEntry> record) {
		double Entropy = getEntropy(record);
		SplitResult result = new SplitResult();
		if (!schema.isDiscrete(order)) {
			// this means the data is continuous
			// remove those attributes is missing
			List<DataEntry> data = filterData(order, record);
			Entropy = getEntropy(data);
			Collections.sort(data, new Comparator<DataEntry>() {
				public int compare(DataEntry o1, DataEntry o2) {
					return Tool.comp(o1.getData(order), o2.getData(order));
				}
			});
			// the naive way just split at the middle. Actually it can be
			// optimized by target value changed.
			
			ArrayList<DataEntry> left = new ArrayList<DataEntry>();
			ArrayList<DataEntry> right = new ArrayList<DataEntry>(data);
			HashMap<String,Long> leftMap = new HashMap<String,Long>();
			HashMap<String,Long> rightMap = new HashMap<String,Long>(getTargetValueMap(data));

			double maxGainRatio = 0;
			for (int i = 0; i < data.size() - 1; i++) {
				left.add(data.get(i));
				String Key = data.get(i).getData(schema.length()-1);
				Long val = leftMap.get(Key);
				leftMap.put(Key, val==null?1L:val+1);
				right.remove(0);
				val = rightMap.get(Key);
				rightMap.put(Key, val-1);
				if (!left.get(left.size() - 1).getData(order)
						.equals(right.get(0).getData(order))) {
					double AttributeVal = Tool.add(data.get(i).getData(order),
							data.get(i + 1).getData(order)) / 2;
					double leftEntropy = getEntropy(leftMap,left.size());
					double rightEntropy = getEntropy(rightMap,right.size());
					double AttributeInfo = Tool.div(left.size() + "",
							data.size() + "")
							* leftEntropy
							+ Tool.div(right.size() + "", data.size() + "")
							* rightEntropy;
					double splitratio = getSplitRation2((double)left.size()/(double)data.size());
					double GainRatio = (Entropy-AttributeInfo)/splitratio;
					//System.out.println("F:"+GainRatio+" E:"+Entropy+" "+schema.getName(order)+","+AttributeVal+",V:"+AttributeInfo+",S:"+splitratio+",###==L:"+left.size()+","+leftEntropy+"==R:"+right.size()+","+rightEntropy);
					if(GainRatio>maxGainRatio){
						maxGainRatio = GainRatio;
						result = new SplitResult();
						result.setAttributeName(schema.getName(order));
						result.setInfoGain(Entropy-AttributeInfo);
						result.setInfoGainRatio(GainRatio);
						result.addDescribe("<="+AttributeVal);
						result.addDescribe(">"+AttributeVal);
						result.addData(new ArrayList<DataEntry>(left));
						result.addData(new ArrayList<DataEntry>(right));
					}
				}
			}
			//At this case means the attribute value is all the same
			if(maxGainRatio==0){
				result.setAttributeName(schema.getName(order));
				result.setInfoGain(0);
				result.setInfoGainRatio(0);
				result.addDescribe("="+(data.size()==0?"null":data.get(0).getData(order)));
				result.addData(new ArrayList<DataEntry>(data));
			}
		} else {
			assert (schema.getType(order) == Const.DISCRETE);
			HashMap<String, List<DataEntry>> map = null;
			try{
				map = DataToMap(order, record);
			}catch(Exception e){
				System.err.println(order);
				for(DataEntry dd:record){
					System.err.println(dd.toString());
				}
				result.setInfoGainRatio(0);
				return result;
			}
			//System.out.println(Entropy);
			double attributeInfo = getAttributesInfo(map, record.size());
			//System.out.println(attributeInfo);
			double splitRatio = getSplitRatio(map, record.size());
			result.setAttributeName(schema.getName(order));
			result.setInfoGain(Entropy - attributeInfo);
			result.setInfoGainRatio((Entropy - attributeInfo) / splitRatio);
			Iterator<Entry<String, List<DataEntry>>> it = map.entrySet()
					.iterator();
			while (it.hasNext()) {
				Entry<String, List<DataEntry>> e = it.next();
				result.addDescribe("=" + e.getKey());
				result.addData(e.getValue());
			}
		}
		return result;
	}
	public static double getEntropy(Map<String,Long> targetMap,long totalNum){
		assert totalNum>0;
		double Entropy = 0;
		Iterator<Entry<String, Long>> it = targetMap.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, Long> e = it.next();
			if(e.getValue()==0)
				continue;
			double p = (double)e.getValue()/(double)totalNum;
			Entropy += p*Tool.log2(p)*(-1);
		}
		return Entropy;
	}
	private static double getEntropy(List<DataEntry> record) {
		HashMap<String, Long> map = new HashMap<String, Long>();
		int totalNum = 0;
		for (DataEntry e : record) {
			Long val = map.get(e.getData(e.length() - 1));
			map.put(e.getData(e.length() - 1), val == null ? 1L : val + 1);
			totalNum += 1;
		}
		double Entropy = 0;
		Iterator<Entry<String, Long>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Long> e = it.next();
			double p = Tool.div(e.getValue() + "", totalNum + "");
			Entropy += p * Tool.log2(p) * (-1);
		}
		return Entropy;
	}
	public static HashMap<String,Long> getTargetValueMap(List<DataEntry> data){
		HashMap<String, Long> map = new HashMap<String, Long>();
		for (DataEntry e : data) {
			Long val = map.get(e.getData(e.length() - 1));
			map.put(e.getData(e.length() - 1), val == null ? 1L : val + 1);
		}
		return map;
	}

	private static HashMap<String, List<DataEntry>> DataToMap(int order,
			List<DataEntry> record) {
		HashMap<String, List<DataEntry>> map = new HashMap<String, List<DataEntry>>();
		List<DataEntry> missingAttributes = new ArrayList<DataEntry>();
		long max = Long.MIN_VALUE;
		String contents = "";
		for (DataEntry e : record) {
			if (e.getData(order).equals("?")) {
				missingAttributes.add(e);
				continue;
			}
			List<DataEntry> val = map.get(e.getData(order));
			if (val == null) {
				val = new ArrayList<DataEntry>();
			}
			val.add(e);
			map.put(e.getData(order), val);
		}
		Iterator<Entry<String, List<DataEntry>>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, List<DataEntry>> e = it.next();
			if (e.getValue().size() > max) {
				contents = e.getKey();
				max = e.getValue().size();
			}
		}
		List<DataEntry> val = map.get(contents);
		val.addAll(missingAttributes);
		map.put(contents, val);
		return map;
	}

	private static double getAttributesInfo(Map<String, List<DataEntry>> map,
			long totalNum) {
		double info = 0;
		Iterator<Entry<String, List<DataEntry>>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, List<DataEntry>> e = it.next();
			info += Tool.div(e.getValue().size() + "", totalNum + "")
					* getEntropy(e.getValue());
		}
		return info;
	}

	private static double getSplitRatio(Map<String, List<DataEntry>> map,
			long totalNum) {
		double ratio = 0;
		Iterator<Entry<String, List<DataEntry>>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, List<DataEntry>> e = it.next();
			double p = Tool.div(e.getValue().size() + "", totalNum + "");
			ratio += p * Tool.log2(p) * (-1);
		}
		return ratio;
	}

	private static double getSplitRation2(double p) {
		//System.out.println(p+"==========");
		double ratio = 0;
		ratio = p * Tool.log2(p) + (1 - p) * Tool.log2(1 - p);
		return 0 - ratio;
	}
	private static List<DataEntry> filterData(int order, List<DataEntry> data) {
		List<DataEntry> result = new ArrayList<DataEntry>();
		for (DataEntry e : data) {
			if (!e.getData(order).equals("?"))
				result.add(e);
		}
		return result;
	}
	public static boolean isPure(List<DataEntry> data) throws Exception{
		HashMap<String,Long> map = new HashMap<String,Long>();
		long totalNum = 0;
		for(DataEntry d:data){
			String key = d.getData(d.length()-1);
			Long val = map.get(key);
			map.put(key, val==null?1L:val+1);
			totalNum+=1;
		}
		if(map.size()==1)
			return true;
		double entropy = getEntropy(map,totalNum);
		if(entropy<ConfigReader.getAccuracy())
			return true;
		return false;
	}
	public static String getMostTargetValue(List<DataEntry> data){
		if(data.size()==0)
			return "None";
		HashMap<String,Long> map = new HashMap<String,Long>();
		long max = Long.MIN_VALUE;
		String result = "";
		for(DataEntry d:data){
			String key = d.getData(d.length()-1);
			Long val = map.get(key);
			map.put(key, val==null?1L:val+1);
			if(map.get(key)>max){
				max = map.get(key);
				result =key;
			}
		}
		return result;
	}
}
