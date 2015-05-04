package main;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;

import dataloader.ConfigReader;
import dataloader.EntryBuilder;
import dataloader.SchemaBuilder;
import tools.TreeTools;
import entity.DataEntry;
import entity.Schema;
import entity.TreeNode;
public class Train {
	public static void main(String args[]) throws Exception {
		TreeNode node = BuildModel();
		//TreeTools.printTree(node);
		//TreeTools.printDNF(node);
		ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(ConfigReader.getModelFile()));
		os.writeObject(node);
		os.close();
	}
	public static TreeNode BuildModel() {
		try {
			System.out.println("=====Building Model===========");
			SchemaBuilder sbuilder = new SchemaBuilder();
			EntryBuilder ebuilder = new EntryBuilder();
			String FileName = ConfigReader.getTrainFile();
			Schema schema = sbuilder.BuildSchema(FileName);
			HashSet<Integer> set = new HashSet<Integer>();
			for (int i = 0; i < schema.length() - 1; i++)
				set.add(i);
			ArrayList<DataEntry> e = ebuilder.buildEntry(FileName, schema,true);
			TreeNode root = TreeTools.BuildTree(schema, e, set);
			System.out.println("=====finish building...");
			System.out.println("Training Data size:"+e.size());
			System.out.println("Training Model Accuracy:"+Validate.doValid(e, root, schema)*100+"%");
			return root;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
	}
	public static TreeNode BuildModel(ArrayList<DataEntry> data,Schema schema) throws Exception{
		System.out.println("=====Building Model===========");
		HashSet<Integer> set = new HashSet<Integer>();
		for (int i = 0; i < schema.length() - 1; i++)
			set.add(i);
		TreeNode root = TreeTools.BuildTree(schema, data, set);
		System.out.println("=====finish building...");
		return root;
	}
}
