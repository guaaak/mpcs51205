# -*- coding: utf-8 -*-
"""
Created on Fri Nov 27 15:30:39 2020

@author: guaaak
"""
import pika
import json

class NotificationClient:
    
    def __init__(self):
        self.__host = "localhost"
        self.__queue_name = "notification"
        self.__routing_key = "notification"
        
    def create_conn(self):
        return pika.BlockingConnection(pika.ConnectionParameters(host=self.__host))
    
    def send_alert(self, item_id, user_id, notification_type):
        conn = None
        try:
            conn = pika.BlockingConnection(pika.ConnectionParameters(host=self.__host))
            channel = conn.channel()
            channel.queue_declare(queue=self.__queue_name, durable=True)
            message = {"notification_type": notification_type, 
                       "item_id": item_id,
                       "user_id": user_id}
            print(message["notification_type"])
            json_message = json.dumps(message)
            channel.basic_publish(exchange='', routing_key=self.__routing_key, body=json_message)
            print("[x] Sent watchlist alert notification!")
        except Exception as e:
            print(e)
        finally:
            if conn:
                conn.close()
        return
    
    def send_seller_alert(self, item_id, user_id, seller_id, bid_price, notification_type):
        conn = None
        try:
            conn = pika.BlockingConnection(pika.ConnectionParameters(host=self.__host))
            channel = conn.channel()
            channel.queue_declare(queue=self.__queue_name, durable=True)
            message = {"notification_type": notification_type, 
                       "item_id": item_id,
                       "user_id": user_id,
                       "seller_id": seller_id,
                       "bid_price": bid_price}
            print(message["notification_type"])
            json_message = json.dumps(message)
            channel.basic_publish(exchange='', routing_key=self.__routing_key, body=json_message)
            print("[x] Sent seller alert notification!")
        except Exception as e:
            print(e)
        finally:
            if conn:
                conn.close()
        return
    
    def send_bid_alert(self, item_id, user_id_list, highest_bid_user, highest_bid_price, notification_type):
        conn = None
        try:
            conn = pika.BlockingConnection(pika.ConnectionParameters(host=self.__host))
            channel = conn.channel()
            channel.queue_declare(queue=self.__queue_name, durable=True)
            message = {"notification_type": notification_type, 
                       "item_id": item_id,
                       "user_id_list": user_id_list,
                       "highest_bid_user": highest_bid_user,
                       "highest_bid_price": highest_bid_price}
            print(message["notification_type"])
            json_message = json.dumps(message)
            channel.basic_publish(exchange='', routing_key=self.__routing_key, body=json_message)
            print("[x] Sent bid update alert notification!")
        except Exception as e:
            print(e)
        finally:
            if conn:
                conn.close()
        return
    
    def send_time_alert(self, item_id, user_id_list, remain_time, notification_type):
        conn = None
        try:
            conn = pika.BlockingConnection(pika.ConnectionParameters(host=self.__host))
            channel = conn.channel()
            channel.queue_declare(queue=self.__queue_name, durable=True)
            message = {"notification_type": notification_type, 
                       "item_id": item_id,
                       "user_id_list": user_id_list,
                       "remain_time": remain_time}
            print(message["notification_type"])
            json_message = json.dumps(message)
            channel.basic_publish(exchange='', routing_key=self.__routing_key, body=json_message)
            print("[x] Sent time alert notification!")
        except Exception as e:
            print(e)
        finally:
            if conn:
                conn.close()
        return
    
if __name__ == "__main__":
    NC = NotificationClient()
    NC.send_alert(1, 2, "watchlistAlert")
    NC.send_alert(1, 2, "sellerAlert")
    NC.send_bid_alert(1, [1, 2], "bidUpdateAlert")
    NC.send_time_alert(1, [1, 2], "1 day", "timeRemainAlert")
        