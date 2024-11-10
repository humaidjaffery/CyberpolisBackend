import numpy as np
import math
import matplotlib.pyplot as plt
import copy
import pandas as pd


def get_xy(dataframe, Y_label, X_labels=None):
  dataframe = copy.deepcopy(dataframe)
  if X_labels is None:
    X = dataframe[[c for c in dataframe.columns if c!=Y_label]].values
  else:
    if len(X_labels) ==1:
      X = dataframe[X_labels[0]].values.reshape(-1, 1)
    else:
      X = dataframe[X_labels].values


  Y = dataframe[Y_label].values.reshape(-1, 1)
  data = np.hstack((X,Y))
  X = X.reshape(-1)
  Y = Y.reshape(-1)


  return data, X, Y


def load_data_NumberVines():
  df = pd.read_excel("Real estate valuation data set.xlsx").drop(["X1 transaction date","X3 distance to the nearest MRT station","X5 latitude","X6 longitude"],axis=1)
  df_NumberAttacks = df.drop(["X2 house age"], axis=1)
  return get_xy(df_NumberAttacks, "Y house price of unit area", X_labels=["X4 number of convenience stores"])


def compute_cost_test():
    results = []
    
    # test cases
    test_cases = [
        {
            "name": "Case 1",
            "x": np.array([5, 10, 15, 20]),
            "y": np.array([15, 25, 30, 35]),
            "initial_w": 2,
            "initial_b": 3,
            "expected_cost": 10.125
        },
        {
            "name": "Case 2",
            "x": np.array([5, 10, 15, 20]),
            "y": np.array([15, 25, 30, 35]),
            "initial_w": 2,
            "initial_b": 1,
            "expected_cost": 8.625
        },
        {
            "name": "Case 3",
            "x": np.array([1.5, 3, 4.5, 6, 7.5]),
            "y": np.array([9, 12, 15, 18, 21]),
            "initial_w": 1,
            "initial_b": 0,
            "expected_cost": 57.375
        },
        {
            "name": "Case 4",
            "x": np.array([1.5, 3, 4.5, 6, 7.5]),
            "y": np.array([9, 12, 15, 18, 21]),
            "initial_w": 1,
            "initial_b": 1,
            "expected_cost": 47.375
        },
        {
            "name": "Case 5",
            "x": np.array([1.5, 3, 4.5, 6, 7.5]),
            "y": np.array([7, 10, 13, 15, 19]),
            "initial_w": 1,
            "initial_b": 0,
            "expected_cost": 36.475
        }
    ]
    
    # test cases
    for case in test_cases:
        try:
            cost = compute_cost(case["x"], case["y"], case["initial_w"], case["initial_b"])
            assert np.isclose(cost, case["expected_cost"]), f"{case['name']}: Cost should be close to {case['expected_cost']} but got {cost}"
            results.append({
                "name": case["name"],
                "input": {
                    "x": case["x"].tolist(),
                    "y": case["y"].tolist(),
                    "initial_w": case["initial_w"],
                    "initial_b": case["initial_b"]
                },
                "expected_output": case["expected_cost"],
                "stdout": f"Cost computed as {cost}",
                "stderr": "",
                "passed": True
            })
        except AssertionError as e:
            results.append({
                "name": case["name"],
                "input": {
                    "x": case["x"].tolist(),
                    "y": case["y"].tolist(),
                    "initial_w": case["initial_w"],
                    "initial_b": case["initial_b"]
                },
                "expected_output": case["expected_cost"],
                "stdout": str(e),
                "stderr": "",
                "passed": False
            })
        except Exception as e:
            results.append({
                "name": case["name"],
                "input": {
                    "x": case["x"].tolist(),
                    "y": case["y"].tolist(),
                    "initial_w": case["initial_w"],
                    "initial_b": case["initial_b"]
                },
                "expected_output": case["expected_cost"],
                "stdout": "",
                "stderr": f"Unexpected error: {str(e)}",
                "passed": False
            })
    
    return results

def compute_cost(x,y,w,b):
  """
  Computes cost function for linear regression algorithm.


  Arguments:
    x: array with shape (m,) Features/input into the model
    y: array with shape (m,) Target for the model's predictions
    w, b: parameters of the model that are used to make predictions


  Returns:
    total_cost: the cost of the model when using w, b parameters to predict y with the inputs of x


  """
  #Number of training elements
  m = x.shape[0]


  #Initiate the total_cost as 0, must be incremented and returned correctly
  total_cost = 0


  ### Begin code here ###
  for i in range(m):
    prediction = (w * x[i]) + b
    cost = (prediction - y[i]) ** 2
    total_cost += cost


  total_cost = total_cost / (2 * m)


  """
  ANSWER


  for i in range(m):
    prediction = (w * x[i]) + b
    cost = (prediction - y[i]) ** 2
    total_cost += cost


  total_cost = total_cost / (2 * m)
  """

  ### End code here ###
  return total_cost

   
if __name__ == '__main__':
   print({
      "results": compute_cost_test()
    })
   


   