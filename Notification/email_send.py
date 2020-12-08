# -*- coding: utf-8 -*-
"""
Created on Fri Nov 27 17:24:49 2020

@author: guaaak
"""
import smtplib
import ssl
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
from imap_tools import MailBox 
import requests


class EmailSending:
    
    def __init__(self):
        self.__port = 465
        self.__stmp_server = "smtp.gmail.com"
        self.__imap_server = "imap.gmail.com"
        self.__password = "MKzf@961103"
        self.__email = "projectnotificationtest@gmail.com"
    
    def login(self):
        context = ssl.create_default_context()

        with smtplib.SMTP_SSL("smtp.gmail.com", self.__port, context=context) as server:
            server.login("projectnotificationtest@gmail.com", self.__password)
            # TODO: Send email here
            print('ok')
        
    def get_user_info(self, user_id):
        url = "http://localhost:23333/getUserInfo/?uid="
        r = requests.get(url+str(user_id))
        print(r.json())
        email = r.json()["email"]
        username = r.json()["username"]
        # email = "kanggua@gmail.com"
        # username = "guaaak"
        return username, email
    
    def get_item_into(self, item_id):
        url = "http://localhost:8080/auction/item/"
        r = requests.get(url+str(item_id))
        print(r.json())
        item_name = r.json()["name"]
        print()
        # item_name = "soap"
        return item_name
    
    def get_email_content(self, email_type):
        if email_type == "watchlistAlert":
            text  = "Dear, user {}!\n"+\
                    "\tYour item {} on your watchlist matchs you sepcifications!\n"+\
                    "\tPlease pay attention.\n"+\
                    "Best Regard!"
        elif email_type == "sellerAlert":
            text  = "Dear, seller {}!\n"+\
                    "\tYour item {} on your watchlist matchs you sepcifications!\n"+\
                    "\tPlease pay attention.\n"+\
                    "Best Regard!"
        else:
            text  = "Dear, user {}!\n"+\
                    "\tYour item {} has been bid by someone else!\n"+\
                    "\tPlease pay attention.\n"+\
                    "Best Regards!"
        return text
    
    def send_watchlist_notification(self, item_id, user_id):
        context = ssl.create_default_context()
        with smtplib.SMTP_SSL(self.__stmp_server, self.__port, context=context) as server:
            server.login(self.__email, self.__password)
            sender_email = self.__email
            receiver_name, receiver_email = self.get_user_info(user_id)
            item_name = self.get_item_into(item_id)
            message = MIMEMultipart("alternative")
            message["Subject"] = "Watchlist Notification"
            message["From"] = sender_email
            message["To"] = receiver_email
            text  = "Dear, user {}!\n".format(receiver_name)+\
                    "\tYour item {} on your watchlist matchs you sepcifications!\n".format(item_name)+\
                    "\tPlease pay attention.\n"+\
                    "Best Regard!"
            part1 = MIMEText(text, "plain")
            message.attach(part1)
            print("sending Email")
            server.sendmail(sender_email, receiver_email, message.as_string())
        return
    
    def send_seller_notification(self, item_id, seller_id, user_id, bid_price):
        context = ssl.create_default_context()
        with smtplib.SMTP_SSL(self.__stmp_server, self.__port, context=context) as server:
            server.login(self.__email, self.__password)
            sender_email = self.__email
            receiver_name, receiver_email = self.get_user_info(seller_id)
            buyer_name, _ = self.get_user_info(user_id)
            item_name = self.get_item_into(item_id)
            message = MIMEMultipart("alternative")
            message["Subject"] = "Seller Notification"
            message["From"] = sender_email
            message["To"] = receiver_email
            text  = "Dear, seller {}!\n".format(receiver_name)+\
                    "\tYour item {} has been bid by {} with price {}!\n".format(item_name, buyer_name, bid_price)+\
                    "\tPlease pay attention.\n"+\
                    "Best Regard!"
            part1 = MIMEText(text, "plain")
            message.attach(part1)
            print("sending Email")
            server.sendmail(sender_email, receiver_email, message.as_string())
        return
    
    def send_bidupdate_notification(self, item_id, user_id, highest_bid_user, highest_bid_price):
        context = ssl.create_default_context()
        with smtplib.SMTP_SSL(self.__stmp_server, self.__port, context=context) as server:
            server.login(self.__email, self.__password)
            sender_email = self.__email
            receiver_name, receiver_email = self.get_user_info(user_id)
            highest_bid_username, _ = self.get_user_info(highest_bid_user)
            item_name = self.get_item_into(item_id)
            message = MIMEMultipart("alternative")
            message["Subject"] = "Bid Update Notification"
            message["From"] = sender_email
            message["To"] = receiver_email
            text  = "Dear, user {}!\n".format(receiver_name)+\
                    "\tYour item {} has been bid by {} with higher price {}!\n".format(item_name, highest_bid_username, highest_bid_price)+\
                    "\tPlease pay attention.\n"+\
                    "Best Regard!"
            part1 = MIMEText(text, "plain")
            message.attach(part1)
            print("sending Email")
            server.sendmail(sender_email, receiver_email, message.as_string())
        return
    
    def send_time_notification(self, item_id, user_id, time_remain):
        context = ssl.create_default_context()
        with smtplib.SMTP_SSL(self.__stmp_server, self.__port, context=context) as server:
            server.login(self.__email, self.__password)
            sender_email = self.__email
            receiver_name, receiver_email = self.get_user_info(user_id)
            item_name = self.get_item_into(item_id)
            message = MIMEMultipart("alternative")
            message["Subject"] = "timeRemainAlert"
            message["From"] = sender_email
            message["To"] = receiver_email
            text  = "Dear, user {}!\n".format(receiver_name)+\
                    "\tThe auction of item {} will colse in {} seconds!\n".format(item_name, time_remain)+\
                    "\tPlease pay attention.\n"+\
                    "Best Regards!"
            part1 = MIMEText(text, "plain")
            message.attach(part1)
            print("sending Email")
            server.sendmail(sender_email, receiver_email, message.as_string())
        return
             
    def receive_email(self):
        email_list = []
        with MailBox(self.__imap_server).login(self.__email, self.__password, 'INBOX') as mailbox:
            emails = mailbox.fetch()
            for email in emails:
                email_obj = {"from": email.from_,
                             "subject": email.subject,
                             "text": email.text}
                email_list.append(email_obj)
                mailbox.delete(email.uid)
        return email_list
    
    def reply_email(self, email_to, subject, text):
        context = ssl.create_default_context()
        with smtplib.SMTP_SSL(self.__stmp_server, self.__port, context=context) as server:
            server.login(self.__email, self.__password)
            sender_email = self.__email
            message = MIMEMultipart("alternative")
            message["Subject"] = subject
            message["From"] = sender_email
            message["To"] = email_to
            text  = text
            part1 = MIMEText(text, "plain")
            message.attach(part1)
            print("sending Email")
            server.sendmail(sender_email, email_to, message.as_string())
        return

if __name__ == "__main__":
    ES = EmailSending()
    # ES.send_watchlist_notification(1, 1)
    # ES.send_seller_notification(1, 1, 2, 100)
    # ES.send_bidupdate_notification(1, 1, 3, 400)
    # ES.send_time_notification(1, 1, 300)
    # print(ES.receive_email())
    ES.reply_email("kanggua@gmail.com", "1231232", "asdfaewv")
    # message = {
    #             "username": "username",
    #         	"email": "username@email.com",
    #         	"password": "password",
    #         	"user_bio": "user bio"
    #         }
    # x = requests.post("http://localhost:23333/userCreate/", json = message)
    # print(x.json())
    # message = {
    #                "ratings": 2.4,
    #                "quantitiy": 1,
    #                "description": "item qq",
    #                "name": "ver2 item",
    #                "shippingCosts": 198,
    #                "isFlagged": "false",
    #                "categoryId": "yesok"
    #             }
    # #x = requests.post("http://localhost:8080/auction/item/create", json = message)
    # #print(x.json())
    # print(ES.get_user_info(1))
    # print(ES.get_item_into(0))