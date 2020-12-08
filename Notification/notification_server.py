# -*- coding: utf-8 -*-
"""
Created on Fri Nov 27 15:30:28 2020

@author: guaaak
"""
import pika
import json
import sys
import os
import email_send

class NotificationServer:
    
    def __init__(self):
        self.__host = "localhost"
        self.__queue_name = "notification"
        self.__routing_key = "notification"
        
    
            
    def receive_message(self):
        connection = pika.BlockingConnection(pika.ConnectionParameters(host='localhost'))
        channel = connection.channel()
    
        channel.queue_declare(queue=self.__queue_name, durable=True)
        def callback(ch, method, properties, body):
            message = json.loads(body)
            ES = email_send.EmailSending()
            notification_type = message["notification_type"]
            print("[x] Send notification with notification type {}.".format(notification_type))
            item_id = message["item_id"]
            if notification_type == "timeRemainAlert":
                user_id_list = message["user_id_list"]
                remain_time = message["remain_time"]
                for user_id in user_id_list:
                    ES.send_time_notification(item_id, user_id, remain_time)
            elif notification_type == "bidUpdateAlert":
                user_id_list = message["user_id_list"]
                highest_bid_user = message["highest_bid_user"]
                highest_bid_price = message["highest_bid_price"]
                for user_id in user_id_list:
                    ES.send_bidupdate_notification(item_id, user_id, highest_bid_user, highest_bid_price)
            elif notification_type == "watchlistAlert":
                user_id = message["user_id"]
                ES.send_watchlist_notification(item_id, user_id)
            else:
                user_id = message["user_id"]
                seller_id = message["seller_id"]
                bid_price = message["bid_price"]
                ES.send_seller_notification(item_id, seller_id, user_id, bid_price)
        channel.basic_consume(queue=self.__queue_name, on_message_callback=callback, auto_ack=True)
    
        print(' [*] Waiting for messages. To exit press CTRL+C')
        channel.start_consuming()

if __name__ == "__main__":
    NS = NotificationServer()
    try:
        NS.receive_message()
    except KeyboardInterrupt:
        print('Interrupted')
        try:
            sys.exit(0)
        except SystemExit:
            os._exit(0)
        