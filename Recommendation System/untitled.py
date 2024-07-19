import pandas as pd
import pymongo
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
from flask import Flask, jsonify

app = Flask(__name__)

# Connect to MongoDB
client = pymongo.MongoClient("mongodb://localhost:27017/")
db = client["Ecom"]

# Retrieve data from MongoDB
items_cursor = db["items"].find()
items = pd.DataFrame(list(items_cursor))

# Convert ObjectId to string and drop unnecessary columns
items['_id'] = items['_id'].astype(str)
if '_class' in items.columns:
    items.drop(columns=['_class'], inplace=True)

# Combine item features into a single string
items['features'] = items['name'] + " " + items['quantity'].astype(str) + " " + items['price'].astype(str)

# Use TF-IDF to vectorize item features
vectorizer = TfidfVectorizer(stop_words='english')
item_features = vectorizer.fit_transform(items['features'])

# Calculate cosine similarity scores
similarity_scores = cosine_similarity(item_features)

def recommend_items(item_id, similarity_scores, items, num_recommendations=5):
    try:
        # Get index of the item
        item_idx = items.index[items['_id'] == item_id].tolist()[0]
        
        # Get similarity scores for the target item
        item_scores = similarity_scores[item_idx]
        
        # Exclude the target item from recommendations
        recommendations = pd.Series(item_scores, index=items['_id'])
        recommendations = recommendations.drop(item_id, errors='ignore')
        
        # Sort and return top N recommendations
        recommendations = recommendations.sort_values(ascending=False).head(num_recommendations)
        return items.loc[items['_id'].isin(recommendations.index)]
    except Exception as e:
        return str(e)

@app.route('/recommend/<item_id>', methods=['GET'])
def recommend(item_id):
    recommendations = recommend_items(item_id, similarity_scores, items)
    return recommendations.to_json(orient="records")

if __name__ == '__main__':
    app.run(debug=True, port=5003)  # Ensure this port is not in use
