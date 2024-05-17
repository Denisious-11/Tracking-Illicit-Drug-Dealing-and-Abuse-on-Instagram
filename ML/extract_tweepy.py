from os import access
import sys
import tweepy
import datetime
import pandas as pd

def scrape(words, numtweet):
    numtweet=int(numtweet)

##    consumer_key="Z1IF1ldDaB399uI0KVyoKXt1H"
##    consumer_secret="nyAgAvQZnj8EGmimT14Uu6v50KFCKzWxXwtZ2KbrkWJEGksVWk"
##    access_token="1416988800367022083-XWXzFC4MaC8KowcbsEr3EeF2NkEq1z"
##    access_secret="3imQZH0k9yjjCWpucIa5vNBJ3RMRJ6GoC0ibDbDiaSUhh"

    consumer_key = "0JtXccfaanGsJRuBEbPcV4iBA"
    consumer_secret = "pWUzIOFkc0ikNrrHN2tOhfifRWqYYPRVEcRZmlYvZJWF6XMBLD"  
    access_token = "1416988800367022083-CJMRB5NwhaqwQp84lVVbksIG8RwzHc"
    access_secret = "bO5xg513leRxb4LkJ9OnJ8Blye4VBqzGWmwtVNn2GjMAn"

    auth = tweepy.OAuthHandler(consumer_key, consumer_secret)

    auth.set_access_token(access_token, access_secret)


    api = tweepy.API(auth)
      
    db = pd.DataFrame(columns=['text'])

  
   
    tweets = tweepy.Cursor(api.search, q=words, lang="en",tweet_mode='extended').items(numtweet)
     

    list_tweets = [tweet for tweet in tweets]
      
   
    i = 1  
      
   
    for tweet in list_tweets:
          
        
        try:
            text = tweet.retweeted_status.full_text

        except AttributeError:
            text = tweet.full_text

        ith_tweet = [text]####extra added
        db.loc[len(db)] = ith_tweet
          
       
        i = i+1


    filename = 'scrapped.csv'
    # filename1='collected_tweets/%s/%s-%s-%s.csv'%(f,words,numtweet,date_since.replace('/','-'))
      
    db.to_csv(filename)
    # db.to_csv(filename1)
  
  
if __name__ == '__main__':
##    consumer_key="Z1IF1ldDaB399uI0KVyoKXt1H"
##    consumer_secret="nyAgAvQZnj8EGmimT14Uu6v50KFCKzWxXwtZ2KbrkWJEGksVWk"
##    access_token="1416988800367022083-XWXzFC4MaC8KowcbsEr3EeF2NkEq1z"
##    access_secret="3imQZH0k9yjjCWpucIa5vNBJ3RMRJ6GoC0ibDbDiaSUhh"

    consumer_key = "0JtXccfaanGsJRuBEbPcV4iBA"
    consumer_secret = "pWUzIOFkc0ikNrrHN2tOhfifRWqYYPRVEcRZmlYvZJWF6XMBLD"  
    access_token = "1416988800367022083-CJMRB5NwhaqwQp84lVVbksIG8RwzHc"
    access_secret = "bO5xg513leRxb4LkJ9OnJ8Blye4VBqzGWmwtVNn2GjMAn"

    auth = tweepy.OAuthHandler(consumer_key, consumer_secret)

    auth.set_access_token(access_token, access_secret)


    api = tweepy.API(auth)
    print("Enter query")
    words = input()

    numtweet = 150  
    scrape(words, numtweet)
    print('Scraping has completed!')
