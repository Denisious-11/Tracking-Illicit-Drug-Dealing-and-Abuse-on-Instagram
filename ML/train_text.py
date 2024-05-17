#importing necessary libraries
import pandas as pd
import re
import nltk
import pickle
from nltk.corpus import stopwords
from sklearn.metrics import accuracy_score
import time
from nltk.stem.porter import PorterStemmer
import numpy as np
from sklearn.metrics import confusion_matrix
import seaborn as sns
import matplotlib.pyplot as plt
nltk.download('stopwords')


#reading dataset
df = pd.read_csv("Text_Dataset/Final_Dataset.csv")
print("DATA LOADED\n")

def print_star():
    print('*'*50, '\n')

print(df.head(10))

print_star()

#selecting required columns
df=df[["text","Label"]]
print("selecting required columns\n")
print(df.head(10))
print_star()

#Dropping null columns
df=df.dropna( axis=0)
print(df.head(10))
print_star()

# Seperating data and labels
data=df["text"]
labels=df["Label"]

print(labels.value_counts())

print("DATAS\n")
print(data.head(10))
print(labels.head(10))
print_star()

print("Preprocessing Started")


port_stem = PorterStemmer()

def stemming(content):
    review = re.sub('[^a-zA-Z]',' ',content)
    review = review.lower()
    review = review.split()
    review = [port_stem.stem(word) for word in review if not word in stopwords.words('english')]
    review = ' '.join(review)
    return review


#text preprocessing
def cleantext(text):
  x=str(text).lower().replace('\\','').replace('_','')
  tag=' '.join(re.sub("(@[A-Za-z0-9]+)|([^0-9A-Za-z \t])|(\w+:\/\/\S+)"," ",x).split())
  spcl=tag.replace('[^\w\s]','')
  return spcl

print("Preprocessing Completed")
print_star()


data=data.apply(lambda x:cleantext(x))
data= data.apply(stemming)
print(data.head(10))

from sklearn.feature_extraction.text import TfidfVectorizer

# Create feature vectors
vectorizer = TfidfVectorizer(max_features=8000)
data= vectorizer.fit_transform(data)
# print("Before balancing-->",data.shape)
print("data",data)


pickle.dump(vectorizer,open("Project_Saved_Models/svmvec.pickle",'wb'))


from sklearn.model_selection import train_test_split
from imblearn.over_sampling import SMOTE

sm_ote= SMOTE(random_state = 2)
X_train_res, y_train_res = sm_ote.fit_resample(data, labels)
# print("After balancing-->",X_train_res.shape)
print(type(labels))

X_train, X_test, y_train, y_test = train_test_split(X_train_res, y_train_res, test_size = 0.2, random_state=2)
print(X_train.shape)
print(X_test.shape)
print(y_train.shape)
print(y_test.shape)


# "Support vector classifier" 
from sklearn.svm import SVC  
classifier = SVC(kernel='linear', random_state=0)  

#training
classifier.fit(X_train,y_train)

#saving the trained model
pickle.dump(classifier,open("Project_Saved_Models/svmmodel.pickle",'wb'))

#prediction using the test dataset
y=classifier.predict(X_test)

#finding accuracy of the trained model
score=accuracy_score(y_test, y)
print("svm accuracy-->",score)

