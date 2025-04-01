# app.py
from flask import Flask, request, jsonify
from flask_pymongo import PyMongo
import jwt
import datetime
from werkzeug.security import generate_password_hash, check_password_hash
from bson.objectid import ObjectId
from config import Config

app = Flask(__name__)
app.config.from_object(Config)

# MongoDB kapcsolat inicializálása
mongo = PyMongo(app)

# JWT token generálása
def generate_token(user_id):
    expiration_time = datetime.datetime.utcnow() + datetime.timedelta(hours=1)
    token = jwt.encode({"user_id": str(user_id), "exp": expiration_time}, app.config["SECRET_KEY"], algorithm="HS256")
    return token

# Regisztráció
@app.route('/register', methods=['POST'])
def register():
    data = request.get_json()
    username = data.get("username")
    password = data.get("password")

    if not username or not password:
        return jsonify({"message": "Username and password are required"}), 400

    hashed_password = generate_password_hash(password)
    user = {
        "username": username,
        "password": hashed_password
    }

    if mongo.db.users.find_one({"username": username}):
        return jsonify({"message": "User already exists"}), 400

    mongo.db.users.insert_one(user)
    return jsonify({"message": "User registered successfully"}), 201

# Bejelentkezés
@app.route('/login', methods=['POST'])
def login():
    data = request.get_json()
    username = data.get("username")
    password = data.get("password")

    if not username or not password:
        return jsonify({"message": "Username and password are required"}), 400

    user = mongo.db.users.find_one({"username": username})

    if not user or not check_password_hash(user['password'], password):
        return jsonify({"message": "Invalid credentials"}), 401

    token = generate_token(user['_id'])
    print(f"Generated token: {token}")
    return jsonify({"message": "Login successful", "token": token}), 200

# Blog bejegyezés létrehozása
@app.route('/post', methods=['POST'])
def create_post():
    token = request.headers.get('Authorization')

    if not token or not token.startswith("Bearer "):
        return jsonify({"message": "Token is missing or invalid"}), 401
    
    token = token.split(" ")[1]

    try:
        decoded_token = jwt.decode(token, app.config["SECRET_KEY"], algorithms=["HS256"])
        print(f"Decoded token: {decoded_token}")
        user_id = decoded_token["user_id"]
    except jwt.ExpiredSignatureError:
        return jsonify({"message": "Token has expired"}), 401
    except jwt.InvalidTokenError:
        return jsonify({"message": "Invalid token"}), 401

    data = request.get_json()
    title = data.get("title")
    content = data.get("content")

    if not title or not content:
        return jsonify({"message": "Title and content are required"}), 400

    post = {
        "user_id": ObjectId(user_id),
        "title": title,
        "content": content,
        "created_at": datetime.datetime.utcnow()
    }

    mongo.db.posts.insert_one(post)
    return jsonify({"message": "Post created successfully"}), 201

# Összes blog bejegyzés lekérdezése
@app.route('/posts', methods=['GET'])
def get_posts():
    token = request.headers.get('Authorization')

    if not token or not token.startswith("Bearer "):
        return jsonify({"message": "Token is missing or invalid"}), 401
    
    token = token.split(" ")[1]

    try:
        decoded_token = jwt.decode(token, app.config["SECRET_KEY"], algorithms=["HS256"])
        user_id = decoded_token["user_id"]
    except jwt.ExpiredSignatureError:
        return jsonify({"message": "Token has expired"}), 401
    except jwt.InvalidTokenError:
        return jsonify({"message": "Invalid token"}), 401

    posts = mongo.db.posts.find({"user_id": ObjectId(user_id)})
    result = []
    for post in posts:
        post_data = {
            "_id": str(post["_id"]),
            "title": post["title"],
            "content": post["content"],
            "created_at": post["created_at"]
        }
        result.append(post_data)

    return jsonify(result), 200


# Másik ember blogbejegyzéseinek lekérdezése
@app.route('/posts/<user_name>', methods=['GET'])
def get_user_posts(user_name):
    token = request.headers.get('Authorization')

    if not token or not token.startswith("Bearer "):
        return jsonify({"message": "Token is missing or invalid"}), 401
    
    token = token.split(" ")[1]

    try:
        jwt.decode(token, app.config["SECRET_KEY"], algorithms=["HS256"])
    except jwt.ExpiredSignatureError:
        return jsonify({"message": "Token has expired"}), 401
    except jwt.InvalidTokenError:
        return jsonify({"message": "Invalid token"}), 401
    
    user = mongo.db.users.find_one({"username": user_name})
    if not user:
        return jsonify({"message": "User not found"}), 404

    posts = mongo.db.posts.find({"user_id": ObjectId(user["_id"])})
    if not posts:
        return jsonify({"message": "No posts found for this user"}), 404
    result = []
    for post in posts:
        post_data = {
            "_id": str(post["_id"]),
            "title": post["title"],
            "content": post["content"],
            "created_at": post["created_at"]
        }
        result.append(post_data)

    return jsonify(result), 200

# Blog bejegyzés törlése
@app.route('/post/<post_id>', methods=['DELETE'])
def delete_post(post_id):
    token = request.headers.get('Authorization')

    if not token or not token.startswith("Bearer "):
        return jsonify({"message": "Token is missing or invalid"}), 401
    
    token = token.split(" ")[1]

    try:
        decoded_token = jwt.decode(token, app.config["SECRET_KEY"], algorithms=["HS256"])
        user_id = decoded_token["user_id"]
    except jwt.ExpiredSignatureError:
        return jsonify({"message": "Token has expired"}), 401
    except jwt.InvalidTokenError:
        return jsonify({"message": "Invalid token"}), 401

    post = mongo.db.posts.find_one({"_id": ObjectId(post_id), "user_id": ObjectId(user_id)})
    if not post:
        return jsonify({"message": "Post not found"}), 404

    mongo.db.posts.delete_one({"_id": ObjectId(post_id)})
    return jsonify({"message": "Post deleted successfully"}), 200

if __name__ == '__main__':
    app.run(debug=True)
