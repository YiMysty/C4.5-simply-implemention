package dataloader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import tools.Const;
public class ConfigReader {
	public static float getAccuracy() throws Exception{
		Properties p =new Properties();
		FileInputStream inputStream =new FileInputStream(new File(Const.CONFIGURATION));
		p.load(inputStream);
		float f= Float.parseFloat((String)p.get("ACCURACY"));
		inputStream.close();
		return f;
	}
	public static float getPruningSize() throws Exception{
		Properties p =new Properties();
		FileInputStream inputStream =new FileInputStream(new File(Const.CONFIGURATION));
		p.load(inputStream);
		return Float.parseFloat((String)p.get("TRAININGSIZE"));
	}
	public static float getLearningSize() throws Exception{
		Properties p =new Properties();
		FileInputStream inputStream =new FileInputStream(new File(Const.CONFIGURATION));
		p.load(inputStream);
		return Float.parseFloat((String)p.get("learnigsize"));
	}
	public static boolean isPrune() throws Exception{
		Properties p =new Properties();
		FileInputStream inputStream =new FileInputStream(new File(Const.CONFIGURATION));
		p.load(inputStream);
		return ((String)p.get("isPruendModel")).equals("0")?false:true;
	}
	public static int getDiscreteThreshold() throws Exception{
		Properties p =new Properties();
		FileInputStream inputStream =new FileInputStream(new File(Const.CONFIGURATION));
		p.load(inputStream);
		return Integer.parseInt((String) p.get("DISCRETE_THRESHOLD"));
	}
	public static String getTrainFile() throws Exception{
		Properties p =new Properties();
		FileInputStream inputStream =new FileInputStream(new File(Const.CONFIGURATION));
		p.load(inputStream);
		return(String)p.get("TRAINFILE");
	}
	public static String getValidateFile() throws Exception{
		Properties p =new Properties();
		FileInputStream inputStream =new FileInputStream(new File(Const.CONFIGURATION));
		p.load(inputStream);
		return(String)p.get("VALIDATEFILE");
	}
	public static String getTestFile() throws Exception{
		Properties p =new Properties();
		FileInputStream inputStream =new FileInputStream(new File(Const.CONFIGURATION));
		p.load(inputStream);
		return(String)p.get("TestFILE");
	}
	public static String getModelFile() throws Exception{
		Properties p =new Properties();
		FileInputStream inputStream =new FileInputStream(new File(Const.CONFIGURATION));
		p.load(inputStream);
		return(String)p.get("ModelName");
	}	
	
	public static Properties getProperties() {
		Properties p = new Properties();
		try {
			p.load(new FileInputStream(new File(Const.CONFIGURATION)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p;
	}
}
