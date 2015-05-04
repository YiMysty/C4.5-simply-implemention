package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import tools.Tool;
import dataloader.ConfigReader;
import dataloader.EntryBuilder;
import dataloader.SchemaBuilder;
import entity.DataEntry;
import entity.Schema;
import entity.TreeNode;

public class Validate {
	public static void main(String args[]) throws Exception {
		String FileName = ConfigReader.getValidateFile();
		TreeNode node = null;
		if(!new File((ConfigReader.isPrune()?"Prune_":"")+ConfigReader.getModelFile()).exists()){
			node = Train.BuildModel();
		}else{
			ObjectInputStream reader = new ObjectInputStream(new FileInputStream((ConfigReader.isPrune()?"Prune_":"")+ConfigReader.getModelFile()));
			node = (TreeNode)reader.readObject();
			reader.close();
		}
		System.out.println("Load model finished!...");
		//TreeTools.printTree(node);
		SchemaBuilder sbuilder = new SchemaBuilder();
		EntryBuilder ebuilder = new EntryBuilder();
		Schema schema = sbuilder.BuildSchema(FileName);
		ArrayList<DataEntry> e = ebuilder.buildEntry(FileName, schema,false);
		System.out.println("DataLoad Finished");
		System.out.println("Accuracy:"+doValid(e, node, schema)*100+"%");
	}
	public static float doValid(List <DataEntry> data, TreeNode root,
			Schema schema) {
		long totalNum = 0;
		long correct  = 0;
		for (DataEntry d : data) {
			// examine each data
			String supposedValue = d.getData((d.length() - 1));
			if(supposedValue.equals("?"))
				continue;
			TreeNode node = root;
			totalNum+=1;
			while (!node.isLeaf()) {
				node = node.findChild(d.getData(node.getIndex()),
						schema.isDiscrete(node.getIndex()));
			}
			String finalResult = node.getValue();
			if(Tool.equal(supposedValue, finalResult)){
				correct++;
			}
		}
		return (float)correct/(float)totalNum;
	}
}
