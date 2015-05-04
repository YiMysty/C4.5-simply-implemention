package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import dataloader.ConfigReader;
import dataloader.EntryBuilder;
import dataloader.SchemaBuilder;
import entity.DataEntry;
import entity.Schema;
import entity.TreeNode;

public class Test{
	public static void main(String args[]) throws Exception{
		String FileName = ConfigReader.getTestFile();
		TreeNode node = null;
		if(!new File((ConfigReader.isPrune()?"Prune_":"")+ConfigReader.getModelFile()).exists()){
			node = Train.BuildModel();
		}else{
			ObjectInputStream reader = new ObjectInputStream(new FileInputStream((ConfigReader.isPrune()?"Prune_":"")+ConfigReader.getModelFile()));
			node = (TreeNode)reader.readObject();
			reader.close();
		}
		System.out.println("Loading Model Finished");
		SchemaBuilder sbuilder = new SchemaBuilder();
		Schema schema = sbuilder.BuildSchema(FileName);
		EntryBuilder ebuilder = new EntryBuilder();
		ArrayList<DataEntry> data = ebuilder.loadEntry(FileName, schema);
		System.out.println("FillData");
		System.out.println("filling size:"+data.size());
		fillData(data,node,schema);
		BufferedWriter writer = new BufferedWriter(new FileWriter("output_"+FileName));
		String output = schema.toString()+"\n";
		for(DataEntry d:data){
			output+=d.toString()+"\n";
		}
		writer.write(output);
		writer.close();
		
	}
	public static void fillData(List<DataEntry> data, TreeNode root,
			Schema schema) {
		for (DataEntry d : data) {
			TreeNode node = root;
			while (!node.isLeaf()) {
				node = node.findChild(d.getData(node.getIndex()),
						schema.isDiscrete(node.getIndex()));
			}
			String finalResult = node.getValue();
			d.setData(d.length()-1, finalResult);
		}
	}
}
