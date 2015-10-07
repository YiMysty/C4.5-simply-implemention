# C4.5-simply-implemention
Simply implement of C4.5 By Java

you can try use `cd` to get in to the file folder.
And then `java -jar xxx.jar` to run the corresponding program.

TrainWithoutPrune.jar
Build the model without prune.

TrainWithPrune.jar
Build the model with prune.

Validate.jar
get the accuracy based on current tree model.

Test.jar
get the prediction of the btest.csv and ouput as output_btest.csv

Also, you can modify the parameter in myConfiguration.properties

Properties in myConfiguration.properties.
#####ACCURACY   
This the condition to terminate the tree, if current information entropy is smaller than this value, then terminate.

#####DISCRETE_THRESHOLD 
This is variable to judge the value is discrete or not. The program will build a map, and if the category of the attrubute
					  value which is smaller than this threshold is discrete otherwise it is continuous.

#####TRAINFILE   
training file name.

#####VALIDATEFILE 
validate filename

#####TestFILE  
test filename.

#####ModelName  
the model name

#####TRAININGSIZE  
this will work in prune tree. generate the tree with  Trainsize * DataSize data

#####learnigsize  
determine to use how many data to get the tree model.

#####isPruendModel 
determine to use the prune model or not. it will only useful if the current pruning model is generated.

currently the whole model is built and you can use this as you want.

If you want to print the tree, please import the folder into eclipse.
And you can print the tree as following steps.

1. find the Train.java.
2. uncommented TreeTools.printTree(node); and run it, you will see a tree.
3. also if you want to see dnf just uncommented TreeTools.printDNF(node);
