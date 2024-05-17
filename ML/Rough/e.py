from apify_client import ApifyClient

# Initialize the ApifyClient with your API token
client = ApifyClient("apify_api_sCiPeCBqAVaHu67lf2GG6qv5rumb383yemqh")

# Prepare the actor input
run_input = {
    "hashtags": ["webscraping"],
    "resultsLimit": 20,
}

# Run the actor and wait for it to finish
run = client.actor("zuzka/instagram-hashtag-scraper").call(run_input=run_input)

# Fetch and print actor results from the run's dataset (if there are any)
for item in client.dataset(run["defaultDatasetId"]).iterate_items():
    print(item)