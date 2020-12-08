FROM python:3.8.3

WORKDIR /usr/src/app


RUN pip install --upgrade pip
COPY ./requirements.txt /usr/src/app/requirements.txt
RUN pip install -r requirements.txt
COPY . /usr/src/app/

ADD . /app
WORKDIR /app/ 
RUN pip3 install -r requirements.txt