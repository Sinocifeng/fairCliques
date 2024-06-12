## FairCliques :  fairness-aware maximal clique

### 1. Introduction

- You need to introduce a `jxl` jar package to assist in the processing of `.xsl` files during the process. During the running of my program, `jxl-2.6.jar` is used.
- The file name of the data source file can be configured in the `DataPara.java` file under `src/staticData/`. Just modify the `fileName` parameter to the name of your data source file. 

### 2. Process raw data

- **First**, You need to run the `main` method in the ` src\DataPreprocessing.java ` file to process the original data. In this process.

- the method `getAttrValue()` that generates node attributes in the code can be modified according to the attribute assignment method you want. I only wrote randomly generated code here.

### 3. Run

**Then**, you can run the main method in `src/Entrance.java`, which will eventually produce an `.xsl` file. Our results are all displayed in this `.xsl` file.

### 4. About choosing edges with probability `p`

- Related processing methods, please see `src/ChooseEdge.java`
- If you want to modify the probability `p` of the sampling edge, please modify the parameter `probability`.

**Note**: We use stream extensively in the code for programming. Please do not use versions lower than JDK8.

### 5. Update：

As requested, we updated the code for assigning node attributes during data preprocessing in June 2021. Updates:
- file1: path-`/src/prepare/impl/GenAttrValues.java`
  > We added this file, which contains the logic code for node attribute assignment
- file1: path-`/src/prepare/impl/PrepareDataImpl.java`
  > We updated the function of **getAttrValue(int i)** (line 75-86), which calls the logic codes in the ***GenAttrValues.java*** file to assign attribute values ​​to nodes.
