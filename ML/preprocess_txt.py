import pandas as pd  
aa = pd.read_csv("Scrapped/Drug/Drug.csv")  
aa["Label"] = "1"  

#saving the preprocessed dataset
aa.to_csv("Scrapped/Drug/preprocessed_dataset.csv",index=False)

bb = pd.read_csv("Scrapped/Non-Drug/Non_Drug.csv", error_bad_lines=False)  
bb["Label"] = "0"  

#saving the preprocessed dataset
bb.to_csv("Scrapped/Non-Drug/preprocessed_dataset.csv",index=False)