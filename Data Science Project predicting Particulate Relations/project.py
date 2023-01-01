from tkinter import N
from turtle import shape
import matplotlib.pyplot as plt
from sklearn import metrics, tree
from sklearn.multioutput import RegressorChain
from sklearn.neural_network import MLPRegressor
from sklearn.linear_model import LinearRegression
from sklearn.ensemble import BaggingRegressor, RandomForestRegressor
from sklearn.model_selection import GridSearchCV, train_test_split
from sklearn.inspection import  PartialDependenceDisplay, permutation_importance
import pandas as pd
import shap
import math
from sklearn.pipeline import make_pipeline

from sklearn.preprocessing import StandardScaler

def permutation(model):
    r = permutation_importance(model,x_test,y_test,n_repeats=30,random_state=0)

    for i in r.importances_mean.argsort()[::-1]:
        if r.importances_mean[i] - 2 * r.importances_std[i] > 0:
            print(f"{feature_list[i]:<10}"
            f"{r.importances_mean[i]:.3f}"
            f" +/- {r.importances_std[i]:.3f}")
    i = 0

def gridSearchD():
    param_dict = {'ccp_alpha':[0,.1,.01,.001],'max_depth': range(1,15),'criterion':['friedman_mse']}
    grid = GridSearchCV(tree.DecisionTreeRegressor(),param_grid=param_dict,cv=5,verbose=1)
    grid.fit(x_train,y_train)
    y_pred = grid.predict(x_test)
    print("--- Decision Tree ---\n")

    print("MAE for Decision Tree:",metrics.mean_absolute_error(y_test,y_pred))
    print("MSE for Decision Tree:",metrics.mean_squared_error(y_test,y_pred))
    print("RMSE for Decision Tree:",math.sqrt(metrics.mean_squared_error(y_test,y_pred)))

    print("Accuracy:",grid.score(x_test,y_test))

    print("Best Estimator -- Decision Tree:",grid.best_estimator_)
    print("Best Parameters -- Decision Tree:",grid.best_params_)
    print("\n\n")

    return grid.best_estimator_

def gridSearchB():
    bc_params = {
          'n_estimators': [10,50,100],
          'n_jobs':[-1]
        }
    grid = GridSearchCV(BaggingRegressor(tree.DecisionTreeRegressor()), bc_params, cv=5, verbose=1)   
    grid.fit(x_train,y_train.values.ravel())
    y_pred = grid.predict(x_test)
    print("--- Bagging Data ---\n")
    print("MAE for Bagging:",metrics.mean_absolute_error(y_test,y_pred))
    print("MSE for Bagging:",metrics.mean_squared_error(y_test,y_pred))
    print("RMSE for Bagging:",math.sqrt(metrics.mean_squared_error(y_test,y_pred)))

    print("Accuracy:",grid.score(x_test,y_test))

    print("Best Estimator -- Bagging:",grid.best_estimator_)
    print("Best Parameters -- Bagging:",grid.best_params_)
    print("\n\n")

    return grid.best_estimator_


def forest():
    forest = RandomForestRegressor()
    forest.fit(x_train,y_train.values.ravel())
    y_pred = forest.predict(x_test)
    print("--- Forest Data ---\n") 
    print("MAE for Forest:",metrics.mean_absolute_error(y_test,y_pred))
    print("MSE for Forest:",metrics.mean_squared_error(y_test,y_pred))
    print("RMSE for Forest:",math.sqrt(metrics.mean_squared_error(y_test,y_pred)))

    print("Accuracy:",forest.score(x_test,y_test))
    print("\n\n")

    return forest


def lRegression():
    linear = LinearRegression().fit(x_train,y_train)
    y_pred = linear.predict(x_test)
    print("-- Linear Regression --\n")

    print("MAE for linear:",metrics.mean_absolute_error(y_test,y_pred))
    print("MSE for linear:",metrics.mean_squared_error(y_test,y_pred))
    print("RMSE for linear:",math.sqrt(metrics.mean_squared_error(y_test,y_pred)))

    print("Accuracy:",linear.score(x_test,y_test))

    return linear

def mlpGenerate():
    regressor = MLPRegressor(max_iter=50000,alpha=.005)
    regressor.fit(x_train,y_train.values.ravel())
    y_pred = regressor.predict(x_test)

    print("--- MLP Data ---\n")
    print("MAE for MLP:",metrics.mean_absolute_error(y_test,y_pred))
    print("MSE for MLP:",metrics.mean_squared_error(y_test,y_pred))
    print("RMSE for MLP:",math.sqrt(metrics.mean_squared_error(y_test,y_pred)))

    print("Accuracy:",regressor.score(x_test,y_test))

    return regressor

def pdp(model):
    PartialDependenceDisplay.from_estimator(model,x_train,features=feature_list)
    plt.show()

def shapely(model):
    explainer = shap.Explainer(model,x_train,feature_names=feature_list)
    shap_values = explainer(x_test)
    shap.plots.beeswarm(shap_values)

def shapelyT(model):
    explainer = shap.TreeExplainer(model,feature_names=feature_list)
    shap_values = explainer(x_test)
    shap.plots.beeswarm(shap_values)

def shapelyN(model):
    model = make_pipeline(
        StandardScaler(),
        model
        )
    model.fit(x_train.values,y_train.values.ravel())
    explainer = shap.KernelExplainer(model.predict,x_train)
    shap_values = explainer.shap_values(x_test,nsamples=100)
    shap.summary_plot(shap_values,x_test,feature_names=feature_list)
    

   
dataImport = pd.read_csv("airQualityData.csv", delimiter=',')
dataImport = dataImport.drop(dataImport.columns[[0]],axis=1)
dataImport = dataImport[dataImport["cityname"].str.contains("city",case=True)]
dataImport["cityname"] = dataImport["cityname"].str.replace("city","")
dataImport["cityname"] = pd.to_numeric(dataImport["cityname"])
dataImport = dataImport.sample(1000)

feature_list = ["SO2","PM10","CO","O3_8","NO2","cityname"]



x = dataImport[["SO2","PM10","CO","O3_8","NO2","cityname"]]
y = dataImport[["PM25"]]

x_train,x_test,y_train,y_test = train_test_split(x,y,test_size=.20)

regressor = lRegression()
permutation(regressor)
bagging = gridSearchB()
permutation(bagging)
random = forest()
permutation(random)
mlp = mlpGenerate()
permutation(mlp)


