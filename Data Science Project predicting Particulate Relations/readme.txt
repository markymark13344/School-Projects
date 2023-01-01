The following document is presented to reproduce the results given within the other text files of this folder:

*Note -- All changes in code must be placed after line 135 (line containing test,train,split) as variables created prior will be required
for other methods

For non-graphical output methods, sample size of 40,000 was used
For graphical output methods, sample size of 10,000 was used except for 
MLP which was set to 1,000 (sample size of 10,000 was estimated for hours)

Step 1)
To obtain on linear regression and its permutation feature importance, create a variable that will hold the method call lRegression().
The lRegression() method will create a linear regression model and output the Accuracy, MSE, RMSE, and MAE of the model. Next use the variable as a parameter
for the method permutation(model) where the receiving variable for lRegression is the variable. This will output the permutation feature importance for this model

ex. test = lRegression()
    permutation(test)

To change the number of features, alter the feature_list and x variable. For corresponding results, make sure the features requested are listed in the 
same order. 

ex. feature_list = ["SO2","PM10","CO","O3_8","NO2","cityname"]

    x = dataImport[["SO2","PM10","CO","O3_8","NO2","cityname"]]

Step 2) 
For decision tree, random forest tree, bagging tree, and Multi-layer Perception regressor do the same steps with a defined variable receiving the 
respective method calls (gridSearchD(),forest(),gridSearchB(),mlpGenerate()) and the output will be the same as the linear regression

Step 3) 
To generate a partial dependence plot, you can use the pdp(model) method which will take the requested model as a parameter. 

ex. pdp(test)

After a little bit of waiting, a plot showing the particial dependence plots will appear. The default will show the average partial depedence of each feature. 
If you wish to change this to show individual partial depedences, change PartialDependenceDisplay.from_estimator(model,x_train,feature_names=feature_list) to
PartialDependenceDisplay.from_estimator(model,x_train,feature_names=feature_list, kind = individual). If you wish to show both, kind = both.

step 4) 
To generate a graph containing shap values, models must be entered into three different methods depending on their type.
To generate a graph for linear, insert the model into shapley()
To generate a graph for tree-based models, insert the model into shapelyT() **Note that Bagging is not supported for shap** 
To generate a graph for the MLP Neural Model, use shapleyN()


