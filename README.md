# PLConfiguration

This repository is organized in three directories: Models, Results and SearchStrategies
1. Models.
This directory have one folder per experiment under the next structure:
1.1 Experiment 1: Contains the feature models generated by Betty generator separated in six directories, each one for the number of features configured and each directoy contains the combinations of number of models (1,2,3,4 and 5) for every FM.
1.1.1 properties_exp_1: Contains the results files in .properties extension of the choco implementation over the 1.1 item files.
1.2 Experiment 2: Contains a set of three directories which contains the variation of CMC density (10, 15 and 20 percent) for the 40 features model.
1.2.1 properties_exp_2: Contains the results files in .properties extension of the choco implementation over the 1.2 item files.
1.3 Experiment 3: Contains a folder for every real model (1. DRUPAL, 3. E-shopping, 4. Web Portal and 5. DecisionAl).
Each directory contains de FM original file or afm2coco for the SPLOT original models and the transformations executed to this.
1.3.1 properties_exp_3: Contains the results files in .properties extension of the choco implementation over the 1.3 item files.

2.Results.
This directory have one folder per experiment and on every directoy is contained the csv files result of the processing of brute data, the R file to generate the graphs and the pdf file with the graphs generates for every experiment.

3. SearchStrategies.
This directory contains the java project for executing transformations, from .FM and SPLOT files to .properties and the execution over choco for statistics generation. The directory PLConfiguration/SearchStrategies/CoCoStandalone/src/coco/testing/ contains the java classes that executes the choco implementation and specifically the MainTransformedDynamic.java class contains the implementation of heuristics.
This package depends on PLConfiguration/SearchStrategies/CoCoStandalone/src/pkg/ classes.
