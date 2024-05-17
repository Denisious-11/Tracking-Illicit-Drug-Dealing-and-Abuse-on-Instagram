#importing necessary libraries
import pickle
from nltk.stem.porter import PorterStemmer
import re
import nltk
from nltk.corpus import stopwords
from sklearn.feature_extraction.text import TfidfVectorizer

#Loading trained model and vectorizer
model=pickle.load( open( "Project_Saved_Models/svmmodel.pickle", "rb" ) )
tfidf=pickle.load( open( "Project_Saved_Models/svmvec.pickle", "rb" ) )

#preprocessing
port_stem = PorterStemmer()

def stemming(content):
    review = re.sub('[^a-zA-Z]',' ',content)
    review = review.lower()
    review = review.split()
    review = [port_stem.stem(word) for word in review if not word in stopwords.words('english')]
    review = ' '.join(review)
    return review

def cleantext(text):
  x=str(text).lower().replace('\\','').replace('_','')
  tag=' '.join(re.sub("(@[A-Za-z0-9]+)|([^0-9A-Za-z \t])|(\w+:\/\/\S+)"," ",x).split())
  spcl=tag.replace('[^\w\s]','')
  return spcl



#prediction 
def prediction(val):
    x=cleantext(val)
    print("clean text==>",x)
    x=stemming(x)
    print("stem text==>",x)
    vec=tfidf.transform([x])
    pred=model.predict(vec)
    print(pred)
    pred=pred[0]
    print(pred)
    if(pred==1):
        return "Drug"
    else:
        return "Non Drug"
    

text=input("Enter the Text :\n")
output=prediction(text)
print("\nRESULT\n")
print(output)