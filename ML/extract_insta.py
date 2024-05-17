import requests

url = "https://instagram-captions.p.rapidapi.com/captions/search"

querystring = {"word":"drugs"}

headers = {
    "X-RapidAPI-Key": "873cefe22cmshb25c0eb6ecf45a7p1ea5bdjsn8fb8b70ac4eb",
    "X-RapidAPI-Host": "instagram-captions.p.rapidapi.com"
}

response = requests.request("GET", url, headers=headers, params=querystring)

print(response.text)
