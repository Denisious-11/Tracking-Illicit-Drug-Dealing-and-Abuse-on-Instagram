#import necessary libraries
import pandas as pd
from sklearn.model_selection import train_test_split
from imblearn.over_sampling import SMOTE
from collections import Counter
from sklearn.svm import SVC
from sklearn.metrics import accuracy_score
import joblib
import re
import numpy as np
from sklearn.feature_extraction.text import TfidfVectorizer
import pickle

from nltk.corpus import stopwords
stop = stopwords.words('english')

#load dataset
data=pd.read_csv("Text_Dataset/Final_Dataset.csv")
print(data.head())
print(data.columns)

#removing columns
data.drop(data.columns[[0]], axis=1, inplace=True)
print(data.columns)
print(data)

print(data.isnull().values.any())

# #preprocessing
data['text']=data['text'].fillna("")
print(data.isnull().values.any())
data['text'] = data['text'].str.lower()
data['text'] = data['text'].str.replace(r"(?:\@|https?\://)\S+", "", regex=True)
data['text'] = data['text'].str.replace(r"#(\w+)", "", regex=True)
data['text'] = data['text'].str.replace('\d+', '')
data['text'] = data['text'].str.replace(r'[^\w\s]|_', '', regex=True)
data['text'] = data['text'].str.replace(r'\r\n', '', regex=True)
data['text'] = data['text'].apply(lambda x: [item for item in x.split() if item not in stop])
data['text']=[" ".join(review) for review in data['text'].values]

print(data)

# Seperating data and labels
x=data['text']
y=data["Label"]
print(x)


# # Create feature vectors
vectorizer = TfidfVectorizer(max_features=8000)

data= vectorizer.fit_transform(x)
pickle.dump(vectorizer,open("vectorizer.pickle",'wb'))
print(data)

x_train, x_test, y_train, y_test = train_test_split(data,y, test_size = 0.2, random_state=2)
print(x_train.shape)
print(y_train.shape)
print(x_test.shape)
print(y_test.shape)
print(x_train)
print(y_train)


#Data balancing using SMOTE
counter = Counter(y_train)
print("__________________BEFORE::::::", counter)

smt = SMOTE()

x_train_sm, y_train_sm = smt.fit_resample(x_train, y_train)

counter = Counter(y_train_sm)
print("___________________AFTER:::::::", counter)


print("x_train_sm shape:", x_train_sm.shape)
print("y_train_sm shape:", y_train_sm.shape)

#SVM model
svclassifier = SVC(kernel='linear')
svclassifier.fit(x_train_sm, y_train_sm)

prediction = svclassifier.predict(x_test)


# calculate accuracy =(TP+TN)/total
acc = accuracy_score(y_test, prediction)
print(f"The accuracy score for SVM is :{round(acc,3)*100}%")

# model saving
filename = "Project_Saved_Models/text_model.sav"
joblib.dump(svclassifier, filename)