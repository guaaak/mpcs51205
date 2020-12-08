# -*- coding: utf-8 -*-
"""
Created on Fri Nov 27 13:04:44 2020

@author: guaaak
"""

from flask import Flask
from flask import jsonify
from flask import request
from flask import make_response
from flask_cors import CORS
import notification_client
import email_send
app = Flask(__name__)
cors = CORS(app, resources={r"*": {"origins": "http://localhost:3000"}})
NC = notification_client.NotificationClient()
ES = email_send.EmailSending()

@app.route('/notification/watchlist', methods=["POST"])
def send_watchlist_notification():
    """
    post request {"item_id": 1, "user_id": 1}
    
    returns {"success": True}

    """
    try:
        content_json = request.get_json()
        print(content_json)
        item_id = content_json["item_id"]
        user_id = ["user_id"]
        notification_type = "watchlistAlert"
        NC.send_alert(item_id, user_id, notification_type)
        return jsonify({"success": True})
    except Exception as e:
        print(e)
        return jsonify({"success": False})
    
@app.route('/notification/seller_alert', methods=["POST"])
def send_seller_alert():
    """
    post request {"item_id": 1, "user_id": 1}
    
    returns {"success": True}

    """
    try:
        content_json = request.get_json()
        item_id = content_json["item_id"]
        user_id = content_json["user_id"]
        seller_id = content_json["seller_id"]
        bid_price = content_json["bid_price"]
        notification_type = "sellerAlert"
        NC.send_seller_alert(item_id, user_id, seller_id, bid_price, notification_type)
        return jsonify({"success": True})
    except Exception as e:
        print(e)
        return jsonify({"success": False})
    

@app.route('/notification/bid_update', methods=["POST"])
def send_bid_update_notification():
    """
    post request {"item_id": 1, "user_id_list": [1, 2, 3, 4]}
    
    returns {"success": True}

    """
    try:
        content_json = request.get_json()
        item_id = content_json["item_id"]
        user_id_list = content_json["user_id_list"]
        highest_bid_user = content_json["highest_bid_user_id"]
        highest_bid_price = content_json["highest_bid_price"]
        notification_type = "bidUpdateAlert"
        NC.send_bid_alert(item_id, user_id_list, highest_bid_user, highest_bid_price, notification_type)
        return jsonify({"success": True})
    except Exception as e:
        print(e)
        return jsonify({"success": False})

@app.route('/notification/time_alert', methods=["POST"])
def send_time_alert():
    """
    post request {"item_id": 1, "user_id_list": [1, 2, 3, 4], "remain_time": "1 day"}
    
    returns {"success": True}

    """
    try:
        content_json = request.get_json()
        item_id = content_json["item_id"]
        user_id_list = content_json["user_id_list"]
        remain_time = content_json["remain_time"]
        notification_type = "timeRemainAlert"
        NC.send_time_alert(item_id, user_id_list, remain_time, notification_type)
        return jsonify({"success": True})
    except Exception as e:
        print(e)
        return jsonify({"success": False})

@app.route('/notification/receive_email', methods=["POST"])
def receive_email():
    try:
        email_list = ES.receive_email()
        return jsonify({"success": True, "email_list": email_list})
    except Exception as e:
        print(e)
        return jsonify({"success": False, "email_list": []})
    
@app.route('/notification/reply_email', methods=["POST"])
def reply_email():
    try:
        content_json = request.get_json()
        email_to = content_json["email_to"]
        subject = content_json["subject"]
        text = content_json["text"]
        ES.reply_email(email_to, subject, text)
        return jsonify({"success": True})
    except Exception as e:
        print(e)
        return jsonify({"success": False})
    
    

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)