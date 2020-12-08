# -*- coding: utf-8 -*-
"""
Created on Sat Nov 28 00:41:06 2020

@author: guaaak
"""

import requests

base_url = "http://localhost:5000/notification"
# message = {"item_id": 1, "user_id": 1}
# x = requests.post(base_url+"/watchlist", json = message)
# print(x)
# message = {"item_id": 0, "user_id": 1, "seller_id": 1, "bid_price": 100}
# x = requests.post(base_url+"/seller_alert", json = message)
# print(x.json())
# message = {"item_id": 0, "user_id_list": [1], "highest_bid_user_id": 1, "highest_bid_price": 100}
# x = requests.post(base_url+"/bid_update", json = message)
# print(x.json())
# message = {"item_id": 0, "user_id_list": [1], "remain_time": 300}
# x = requests.post(base_url+"/time_alert", json = message)
# print(x.json())
# print(x.headers)

x = requests.post(base_url+"/receive_email")
print(x.json())
message = {"email_to":"kanggua@gmail.com", "subject":"123", "text":"123"}
x = requests.post(base_url+"/reply_email", json = message)
print(x.json())