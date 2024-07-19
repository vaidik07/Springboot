{
 "cells": [
  {
   "cell_type": "code",
   "execution_count": 2,
   "id": "6974260d-16bf-44d9-b0d7-495be3f6c6e7",
   "metadata": {},
   "outputs": [
    {
     "name": "stdout",
     "output_type": "stream",
     "text": [
      " * Serving Flask app '__main__'\n",
      " * Debug mode: on\n"
     ]
    },
    {
     "name": "stderr",
     "output_type": "stream",
     "text": [
      "\u001b[31m\u001b[1mWARNING: This is a development server. Do not use it in a production deployment. Use a production WSGI server instead.\u001b[0m\n",
      " * Running on http://127.0.0.1:5002\n",
      "\u001b[33mPress CTRL+C to quit\u001b[0m\n",
      " * Restarting with stat\n",
      "Traceback (most recent call last):\n",
      "  File \"<frozen runpy>\", line 198, in _run_module_as_main\n",
      "  File \"<frozen runpy>\", line 88, in _run_code\n",
      "  File \"/opt/homebrew/Cellar/jupyterlab/4.2.1/libexec/lib/python3.12/site-packages/ipykernel_launcher.py\", line 18, in <module>\n",
      "    app.launch_new_instance()\n",
      "  File \"/opt/homebrew/Cellar/jupyterlab/4.2.1/libexec/lib/python3.12/site-packages/traitlets/config/application.py\", line 1074, in launch_instance\n",
      "    app.initialize(argv)\n",
      "  File \"/opt/homebrew/Cellar/jupyterlab/4.2.1/libexec/lib/python3.12/site-packages/traitlets/config/application.py\", line 118, in inner\n",
      "    return method(app, *args, **kwargs)\n",
      "           ^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n",
      "  File \"/opt/homebrew/Cellar/jupyterlab/4.2.1/libexec/lib/python3.12/site-packages/ipykernel/kernelapp.py\", line 692, in initialize\n",
      "    self.init_sockets()\n",
      "  File \"/opt/homebrew/Cellar/jupyterlab/4.2.1/libexec/lib/python3.12/site-packages/ipykernel/kernelapp.py\", line 331, in init_sockets\n",
      "    self.shell_port = self._bind_socket(self.shell_socket, self.shell_port)\n",
      "                      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n",
      "  File \"/opt/homebrew/Cellar/jupyterlab/4.2.1/libexec/lib/python3.12/site-packages/ipykernel/kernelapp.py\", line 253, in _bind_socket\n",
      "    return self._try_bind_socket(s, port)\n",
      "           ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n",
      "  File \"/opt/homebrew/Cellar/jupyterlab/4.2.1/libexec/lib/python3.12/site-packages/ipykernel/kernelapp.py\", line 229, in _try_bind_socket\n",
      "    s.bind(\"tcp://%s:%i\" % (self.ip, port))\n",
      "  File \"/opt/homebrew/Cellar/jupyterlab/4.2.1/libexec/lib/python3.12/site-packages/zmq/sugar/socket.py\", line 311, in bind\n",
      "    super().bind(addr)\n",
      "  File \"_zmq.py\", line 898, in zmq.backend.cython._zmq.Socket.bind\n",
      "  File \"_zmq.py\", line 160, in zmq.backend.cython._zmq._check_rc\n",
      "zmq.error.ZMQError: Address already in use (addr='tcp://127.0.0.1:56617')\n"
     ]
    },
    {
     "ename": "SystemExit",
     "evalue": "1",
     "output_type": "error",
     "traceback": [
      "An exception has occurred, use %tb to see the full traceback.\n",
      "\u001b[0;31mSystemExit\u001b[0m\u001b[0;31m:\u001b[0m 1\n"
     ]
    },
    {
     "name": "stderr",
     "output_type": "stream",
     "text": [
      "/opt/homebrew/Cellar/jupyterlab/4.2.1/libexec/lib/python3.12/site-packages/IPython/core/interactiveshell.py:3585: UserWarning: To exit: use 'exit', 'quit', or Ctrl-D.\n",
      "  warn(\"To exit: use 'exit', 'quit', or Ctrl-D.\", stacklevel=1)\n"
     ]
    }
   ],
   "source": [
    "import pandas as pd\n",
    "import pymongo\n",
    "from sklearn.feature_extraction.text import TfidfVectorizer\n",
    "from sklearn.metrics.pairwise import cosine_similarity\n",
    "from flask import Flask, jsonify\n",
    "\n",
    "app = Flask(__name__)\n",
    "\n",
    "# Connect to MongoDB\n",
    "client = pymongo.MongoClient(\"mongodb://localhost:27017/\")\n",
    "db = client[\"Ecom\"]\n",
    "\n",
    "# Retrieve data from MongoDB\n",
    "items_cursor = db[\"items\"].find()\n",
    "items = pd.DataFrame(list(items_cursor))\n",
    "\n",
    "# Convert ObjectId to string and drop unnecessary columns\n",
    "items['_id'] = items['_id'].astype(str)\n",
    "if '_class' in items.columns:\n",
    "    items.drop(columns=['_class'], inplace=True)\n",
    "\n",
    "# Combine item features into a single string\n",
    "items['features'] = items['name'] + \" \" + items['quantity'].astype(str) + \" \" + items['price'].astype(str)\n",
    "\n",
    "# Use TF-IDF to vectorize item features\n",
    "vectorizer = TfidfVectorizer(stop_words='english')\n",
    "item_features = vectorizer.fit_transform(items['features'])\n",
    "\n",
    "# Calculate cosine similarity scores\n",
    "similarity_scores = cosine_similarity(item_features)\n",
    "\n",
    "def recommend_items(item_id, similarity_scores, items, num_recommendations=5):\n",
    "    try:\n",
    "        # Get index of the item\n",
    "        item_idx = items.index[items['_id'] == item_id].tolist()[0]\n",
    "        \n",
    "        # Get similarity scores for the target item\n",
    "        item_scores = similarity_scores[item_idx]\n",
    "        \n",
    "        # Exclude the target item from recommendations\n",
    "        recommendations = pd.Series(item_scores, index=items['_id'])\n",
    "        recommendations = recommendations.drop(item_id, errors='ignore')\n",
    "        \n",
    "        # Sort and return top N recommendations\n",
    "        recommendations = recommendations.sort_values(ascending=False).head(num_recommendations)\n",
    "        return items.loc[items['_id'].isin(recommendations.index)]\n",
    "    except Exception as e:\n",
    "        return str(e)\n",
    "\n",
    "@app.route('/recommend/<item_id>', methods=['GET'])\n",
    "def recommend(item_id):\n",
    "    recommendations = recommend_items(item_id, similarity_scores, items)\n",
    "    return recommendations.to_json(orient=\"records\")\n",
    "\n",
    "if __name__ == '__main__':\n",
    "    app.run(debug=True, port=5002)\n"
   ]
  },
  {
   "cell_type": "code",
   "execution_count": 3,
   "id": "9dd5e69e-a5aa-4ac3-a352-313e989b8897",
   "metadata": {},
   "outputs": [
    {
     "name": "stdout",
     "output_type": "stream",
     "text": [
      " * Serving Flask app '__main__'\n",
      " * Debug mode: on\n"
     ]
    },
    {
     "name": "stderr",
     "output_type": "stream",
     "text": [
      "\u001b[31m\u001b[1mWARNING: This is a development server. Do not use it in a production deployment. Use a production WSGI server instead.\u001b[0m\n",
      " * Running on http://127.0.0.1:5003\n",
      "\u001b[33mPress CTRL+C to quit\u001b[0m\n",
      " * Restarting with stat\n",
      "Traceback (most recent call last):\n",
      "  File \"<frozen runpy>\", line 198, in _run_module_as_main\n",
      "  File \"<frozen runpy>\", line 88, in _run_code\n",
      "  File \"/opt/homebrew/Cellar/jupyterlab/4.2.1/libexec/lib/python3.12/site-packages/ipykernel_launcher.py\", line 18, in <module>\n",
      "    app.launch_new_instance()\n",
      "  File \"/opt/homebrew/Cellar/jupyterlab/4.2.1/libexec/lib/python3.12/site-packages/traitlets/config/application.py\", line 1074, in launch_instance\n",
      "    app.initialize(argv)\n",
      "  File \"/opt/homebrew/Cellar/jupyterlab/4.2.1/libexec/lib/python3.12/site-packages/traitlets/config/application.py\", line 118, in inner\n",
      "    return method(app, *args, **kwargs)\n",
      "           ^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n",
      "  File \"/opt/homebrew/Cellar/jupyterlab/4.2.1/libexec/lib/python3.12/site-packages/ipykernel/kernelapp.py\", line 692, in initialize\n",
      "    self.init_sockets()\n",
      "  File \"/opt/homebrew/Cellar/jupyterlab/4.2.1/libexec/lib/python3.12/site-packages/ipykernel/kernelapp.py\", line 331, in init_sockets\n",
      "    self.shell_port = self._bind_socket(self.shell_socket, self.shell_port)\n",
      "                      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n",
      "  File \"/opt/homebrew/Cellar/jupyterlab/4.2.1/libexec/lib/python3.12/site-packages/ipykernel/kernelapp.py\", line 253, in _bind_socket\n",
      "    return self._try_bind_socket(s, port)\n",
      "           ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n",
      "  File \"/opt/homebrew/Cellar/jupyterlab/4.2.1/libexec/lib/python3.12/site-packages/ipykernel/kernelapp.py\", line 229, in _try_bind_socket\n",
      "    s.bind(\"tcp://%s:%i\" % (self.ip, port))\n",
      "  File \"/opt/homebrew/Cellar/jupyterlab/4.2.1/libexec/lib/python3.12/site-packages/zmq/sugar/socket.py\", line 311, in bind\n",
      "    super().bind(addr)\n",
      "  File \"_zmq.py\", line 898, in zmq.backend.cython._zmq.Socket.bind\n",
      "  File \"_zmq.py\", line 160, in zmq.backend.cython._zmq._check_rc\n",
      "zmq.error.ZMQError: Address already in use (addr='tcp://127.0.0.1:56617')\n"
     ]
    },
    {
     "ename": "SystemExit",
     "evalue": "1",
     "output_type": "error",
     "traceback": [
      "An exception has occurred, use %tb to see the full traceback.\n",
      "\u001b[0;31mSystemExit\u001b[0m\u001b[0;31m:\u001b[0m 1\n"
     ]
    },
    {
     "name": "stderr",
     "output_type": "stream",
     "text": [
      "/opt/homebrew/Cellar/jupyterlab/4.2.1/libexec/lib/python3.12/site-packages/IPython/core/interactiveshell.py:3585: UserWarning: To exit: use 'exit', 'quit', or Ctrl-D.\n",
      "  warn(\"To exit: use 'exit', 'quit', or Ctrl-D.\", stacklevel=1)\n"
     ]
    }
   ],
   "source": [
    "import pandas as pd\n",
    "import pymongo\n",
    "from sklearn.feature_extraction.text import TfidfVectorizer\n",
    "from sklearn.metrics.pairwise import cosine_similarity\n",
    "from flask import Flask, jsonify\n",
    "\n",
    "app = Flask(__name__)\n",
    "\n",
    "# Connect to MongoDB\n",
    "client = pymongo.MongoClient(\"mongodb://localhost:27017/\")\n",
    "db = client[\"Ecom\"]\n",
    "\n",
    "# Retrieve data from MongoDB\n",
    "items_cursor = db[\"items\"].find()\n",
    "items = pd.DataFrame(list(items_cursor))\n",
    "\n",
    "# Convert ObjectId to string and drop unnecessary columns\n",
    "items['_id'] = items['_id'].astype(str)\n",
    "if '_class' in items.columns:\n",
    "    items.drop(columns=['_class'], inplace=True)\n",
    "\n",
    "# Combine item features into a single string\n",
    "items['features'] = items['name'] + \" \" + items['quantity'].astype(str) + \" \" + items['price'].astype(str)\n",
    "\n",
    "# Use TF-IDF to vectorize item features\n",
    "vectorizer = TfidfVectorizer(stop_words='english')\n",
    "item_features = vectorizer.fit_transform(items['features'])\n",
    "\n",
    "# Calculate cosine similarity scores\n",
    "similarity_scores = cosine_similarity(item_features)\n",
    "\n",
    "def recommend_items(item_id, similarity_scores, items, num_recommendations=5):\n",
    "    try:\n",
    "        # Get index of the item\n",
    "        item_idx = items.index[items['_id'] == item_id].tolist()[0]\n",
    "        \n",
    "        # Get similarity scores for the target item\n",
    "        item_scores = similarity_scores[item_idx]\n",
    "        \n",
    "        # Exclude the target item from recommendations\n",
    "        recommendations = pd.Series(item_scores, index=items['_id'])\n",
    "        recommendations = recommendations.drop(item_id, errors='ignore')\n",
    "        \n",
    "        # Sort and return top N recommendations\n",
    "        recommendations = recommendations.sort_values(ascending=False).head(num_recommendations)\n",
    "        return items.loc[items['_id'].isin(recommendations.index)]\n",
    "    except Exception as e:\n",
    "        return str(e)\n",
    "\n",
    "@app.route('/recommend/<item_id>', methods=['GET'])\n",
    "def recommend(item_id):\n",
    "    recommendations = recommend_items(item_id, similarity_scores, items)\n",
    "    return recommendations.to_json(orient=\"records\")\n",
    "\n",
    "if __name__ == '__main__':\n",
    "    app.run(debug=True, port=5003)  # Changed port to 5003\n"
   ]
  },
  {
   "cell_type": "code",
   "execution_count": null,
   "id": "c0d131b8-4499-4224-83da-af5bf8680260",
   "metadata": {},
   "outputs": [],
   "source": []
  }
 ],
 "metadata": {
  "kernelspec": {
   "display_name": "Python 3 (ipykernel)",
   "language": "python",
   "name": "python3"
  },
  "language_info": {
   "codemirror_mode": {
    "name": "ipython",
    "version": 3
   },
   "file_extension": ".py",
   "mimetype": "text/x-python",
   "name": "python",
   "nbconvert_exporter": "python",
   "pygments_lexer": "ipython3",
   "version": "3.12.3"
  }
 },
 "nbformat": 4,
 "nbformat_minor": 5
}
