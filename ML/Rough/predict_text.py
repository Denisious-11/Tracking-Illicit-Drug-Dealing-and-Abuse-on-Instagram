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

loaded_model = joblib.load('Project_Saved_Models/text_model.sav')
tfidf=pickle.load(open( "vectorizer.pickle", "rb" ))

text=input("Enter the Text :\n")

text=text.lower()
print(text)
text=re.sub(r"(?:\@|https?\://)\S+", "",text)
print(text)
text=re.sub(r"#(\w+)", "",text)
print(text)
text=re.sub('\d+', '',text)
print(text)
text=re.sub(r'[^\w\s]|_', '',text)
print(text)
text=re.sub(r'\r\n', '',text)
print(text)
text=[item for item in text.split() if item not in stop]
print(text)

vec=tfidf.transform(text)
pred=loaded_model.predict(vec)
print(pred)

if(1 in pred):
    print("Drug")
else:
    print("No Drug")
