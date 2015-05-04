package dataloader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import tools.Const;
import entity.DataEntry;
import entity.Schema;
public class EntryBuilder {
	//this is used to judge the field is discrete or not..
	public ArrayList<DataEntry>buildEntry(String FileName,Schema schema,boolean isTrain) throws Exception{
		ArrayList<DataEntry> result = new ArrayList<DataEntry>();
		BufferedReader reader = new BufferedReader(new FileReader(FileName));
		//we assume only the integer can be discrete.. 
		List<HashSet<String>> set = new ArrayList<HashSet<String>>();
		for(int i=0;i<schema.length();i++){
			set.add(new HashSet<String>());
		}
		//skip the first line.
		reader.readLine();
		String s = "";
		while((s=reader.readLine())!=null){
			DataEntry entry = new DataEntry();
			String []record = s.split(",");
			if(record[record.length-1].equals("?")&&isTrain){
				//those data do not help
				continue;
			}
			for(int i = 0;i<record.length;i++){
				entry.insertEntry(record[i]);
				set.get(i).add(record[i]);
			}
			result.add(entry);
		}
		for(int i=0;i<schema.length();i++){
			if(set.get(i).size()<ConfigReader.getDiscreteThreshold()){
				schema.setDataType(i, Const.DISCRETE);
			}else{
				schema.setDataType(i, Const.CONTINUOUS);
			}
		}
		reader.close();
		if(isTrain){
			result = new ArrayList<DataEntry>(result.subList(0, (int)(result.size()*ConfigReader.getLearningSize())));
		}
		return result;
	}
	//this part is for test data
	public ArrayList<DataEntry>loadEntry(String FileName,Schema schema) throws Exception{
		ArrayList<DataEntry> result = new ArrayList<DataEntry>();
		BufferedReader reader = new BufferedReader(new FileReader(FileName));
		//we assume only the integer can be discrete.. 
		List<HashSet<String>> set = new ArrayList<HashSet<String>>();
		for(int i=0;i<schema.length();i++){
			set.add(new HashSet<String>());
		}
		//skip the first line.
		reader.readLine();
		String s = "";
		while((s=reader.readLine())!=null){
			DataEntry entry = new DataEntry();
			String []record = s.split(",");
			for(int i = 0;i<record.length;i++){
				entry.insertEntry(record[i]);
				set.get(i).add(record[i]);
			}
			result.add(entry);
		}
		for(int i=0;i<schema.length();i++){
			if(set.get(i).size()<ConfigReader.getDiscreteThreshold()){
				schema.setDataType(i, Const.DISCRETE);
			}else{
				schema.setDataType(i, Const.CONTINUOUS);
			}
		}
		reader.close();
		return result;
	}
}
