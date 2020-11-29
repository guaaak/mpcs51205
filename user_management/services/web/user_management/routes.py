from flask import request, render_template, make_response, jsonify
from datetime import datetime as dt
from flask import current_app as app
from .models import db, User, Watchlist
from sqlalchemy import and_
import requests
from flask_cors import cross_origin

#for test
@app.route('/')
@cross_origin(origin='*',headers=['Content-Type'])
def hello():
    resp = make_response(jsonify(
        id = '5',
        name = 'container'
    ))
    # resp.headers['Access-Control-Allow-Origin'] = 'http://localhost:3000'
    # resp.headers['Access-Control-Allow-Methods'] = 'GET'
    # resp.headers['Access-Control-Allow-Headers'] = 'Content-Type'
    return resp
    # return jsonify(
    #     id = '5',
    #     name = 'container'
    # )

def is_user_admin_or_suspend(uid, op):
    result = False
    if op == 'isAdmin':
        r = db.session.query(User.admin).filter(User.uid == uid).first()
        if r.admin:
            result = True
    elif op == 'isSuspend':
        r = db.session.query(User.suspend).filter(User.uid == uid).first()
        if r.suspend:
            result = True
    return result

@app.route('/isAdmin/', methods=['GET'])
def is_user_admin():
    result = False
    uid = int(request.args.get('uid'))
    if uid:
        result = is_user_admin_or_suspend(uid, 'isAdmin')
    
    # return jsonify(
    #     is_admin = result
    # ), 200
    resp = make_response(jsonify(
        is_admin = result
    ))
    resp.headers['Access-Control-Allow-Origin'] = 'http://localhost:3000'
    resp.headers['Access-Control-Allow-Methods'] = 'GET'
    resp.headers['Access-Control-Allow-Headers'] = 'Content-Type'
    return resp

@app.route('/isSuspend/', methods=['GET'])
def is_user_suspend():
    result = False
    uid = int(request.args.get('uid'))
    if uid:
        result = is_user_admin_or_suspend(uid, 'isSuspend')
    # return jsonify(
    #     is_suspend = result
    # ), 200
    resp = make_response(jsonify(
        is_suspend = result
    ))
    resp.headers['Access-Control-Allow-Origin'] = 'http://localhost:3000'
    resp.headers['Access-Control-Allow-Methods'] = 'GET'
    resp.headers['Access-Control-Allow-Headers'] = 'Content-Type'
    return resp

@app.route('/userCreate/', methods=['POST', 'GET'])
def user_create():
    username = request.json['username']
    email = request.json['email']
    password = request.json['password']
    bio = request.json['user_bio']

    content = jsonify(
            status = 'fail',
            reason = 'user already existed'
        )
    
    if username and email:
        existing_user = User.query.filter(
            User.username == username or User.email == email
        ).first()
        if existing_user:
            # return make_response(
            #     f'{username} ({email}) already created!'
            # )
            resp = make_response(content)
            resp.headers['Access-Control-Allow-Origin'] = 'http://localhost:3000'
            resp.headers['Access-Control-Allow-Methods'] = 'POST'
            resp.headers['Access-Control-Allow-Headers'] = 'Content-Type'
            return resp
        new_user = User(
            username=username,
            email=email,
            password=password,
            credibility=5.0,
            created=dt.now(),
            bio=bio,
            admin=False,
            suspend=False,
        )
        db.session.add(new_user)  # Adds new User record to database
        db.session.commit()
    content = jsonify(
        status = 'success'
    )
    resp = make_response(content)
    resp.headers['Access-Control-Allow-Origin'] = 'http://localhost:3000'
    resp.headers['Access-Control-Allow-Methods'] = 'POST'
    resp.headers['Access-Control-Allow-Headers'] = 'Content-Type'
    return resp

@app.route('/userDelete/', methods=['POST', 'GET'])
def user_delete():
    uid = int(request.args.get('uid'))
    content = jsonify(
            status = 'fail'
        )
    if uid:
        existing_user = db.session.query(User).get(uid)
        if existing_user:
            db.session.delete(existing_user)
            db.session.commit()
            # return make_response('Delete successfully!')
            #return True
            content = jsonify(
                status = 'success'
            )
    resp = make_response(content)
    resp.headers['Access-Control-Allow-Origin'] = 'http://localhost:3000'
    resp.headers['Access-Control-Allow-Methods'] = 'POST'
    resp.headers['Access-Control-Allow-Headers'] = 'Content-Type'
    return resp
    

@app.route('/userSuspend/', methods=['POST', 'GET'])
def user_suspend():
    uid = int(request.args.get('uid'))
    content = jsonify(
            status = 'fail'
        )
    if uid:
        existing_user = db.session.query(User).get(uid)
        if existing_user:
            existing_user.suspend = True
            db.session.commit()
            # return make_response('Suspend successfully')
            #return True
            content =  jsonify(
                status = 'success'
            )
    resp = make_response(content)
    resp.headers['Access-Control-Allow-Origin'] = 'http://localhost:3000'
    resp.headers['Access-Control-Allow-Methods'] = 'GET'
    resp.headers['Access-Control-Allow-Headers'] = 'Content-Type'
    return resp

@app.route('/getUserInfo/', methods=['GET'])
def get_user_info():
    uid = int(request.args.get('uid'))
    content = jsonify(
        status = 'fail',
        reason = 'uid not right or missed'
    )
    if uid:
        user_info = db.session.query(User).get(uid)
        if user_info:
            content =  jsonify(
                status = 'success',
                username = user_info.username,
                email = user_info.email
            ), 200
    resp = make_response(content)
    resp.headers['Access-Control-Allow-Origin'] = 'http://localhost:3000'
    resp.headers['Access-Control-Allow-Methods'] = 'GET'
    resp.headers['Access-Control-Allow-Headers'] = 'Content-Type'
    return resp

@app.route('/login/', methods=['GET', 'POST'])
def user_login():
    email = request.json['email']
    password = request.json['password']
    # email = 'test4@test.com'
    # password = 'test'
    content = jsonify(
            status = 'fail',
            reason = 'email or password missed'
        )
    if email and password:
        user_info = db.session.query(User.password, User.uid, User.admin).filter(User.email == email).first()
        if user_info and user_info.password == password:
            content = jsonify(
                status = 'success',
                user_id = user_info.uid,
                admin = user_info.admin
            )
    resp = make_response(content)
    resp.headers['Access-Control-Allow-Origin'] = 'http://localhost:3000'
    resp.headers['Access-Control-Allow-Methods'] = 'GET'
    resp.headers['Access-Control-Allow-Headers'] = 'Content-Type'
    return resp

@app.route('/fetchUserIdentity/', methods=['GET'])
def fetch_user_identity():
    uid = int(request.args.get('uid'))
    content = jsonify(
        status = 'fail',
        info = 'uid wrong'
    )
    if uid:
        user_info = db.session.query(User.username, User.email, User.suspend).filter(User.uid == uid).first()
        if user_info:
            content = jsonify(
                status = 'success',
                username = user_info.username,
                email = user_info.email,
                suspend = user_info.suspend
            )
    resp = make_response(content)
    resp.headers['Access-Control-Allow-Origin'] = 'http://localhost:3000'
    resp.headers['Access-Control-Allow-Methods'] = 'GET'
    resp.headers['Access-Control-Allow-Headers'] = 'Content-Type'
    return resp


@app.route('/addItemToWatchlist/', methods=['GET', 'POST'])
def add_item_to_watchlist():
    uid = int(request.args.get('uid'))
    item_id = int(request.args.get('item_id'))
    criteria = float(request.args.get('criteria'))
    content = jsonify(
                status = 'fail'
            )
    if uid is not None and item_id is not None:
        existing_record = Watchlist.query.filter(
            and_(Watchlist.uid == uid,Watchlist.itemid == item_id)
        ).first()
        if existing_record:
            resp = make_response(content)
            resp.headers['Access-Control-Allow-Origin'] = 'http://localhost:3000'
            resp.headers['Access-Control-Allow-Methods'] = 'GET'
            resp.headers['Access-Control-Allow-Headers'] = 'Content-Type'
            return resp
        url = 'http://itemservice:8080/auction/item/' + str(item_id)
        r = requests.get(url)
        name = r.json()['name']
        new_record = Watchlist(
            uid = uid,
            itemid = item_id,
            itemname = name,
            created = dt.now(),
            criteria = criteria
        )
        db.session.add(new_record)
        db.session.commit()
        content =  jsonify(
            status = 'success'
        )
    resp = make_response(content)
    resp.headers['Access-Control-Allow-Origin'] = 'http://localhost:3000'
    resp.headers['Access-Control-Allow-Methods'] = 'GET'
    resp.headers['Access-Control-Allow-Headers'] = 'Content-Type'
    return resp


@app.route('/getItemsInWatchlist/', methods=['GET'])
def get_items_in_watchlist():
    uid = int(request.args.get('uid'))
    records = []
    if uid:
        all_records = db.session.query(Watchlist.itemid, Watchlist.itemname, Watchlist.criteria).filter(Watchlist.uid == uid).all()
        for record in all_records:
            print(record)
            item = {
                'item_name': record.itemname,
                'item_id': record.itemid,
                'criteria': record.criteria
            }
            records.append(item)
        resp = make_response(jsonify(
            uid = uid,
            records = records
        ))
        resp.headers['Access-Control-Allow-Origin'] = 'http://localhost:3000'
        resp.headers['Access-Control-Allow-Methods'] = 'GET'
        resp.headers['Access-Control-Allow-Headers'] = 'Content-Type'
        return resp


@app.route('/meetCriteria/')
def users_meet_criteria():
    item_id = int(request.args.get('item_id'))
    price = float(request.args.get('price'))
    content = jsonify(
        status = 'fail'
    )
    if item_id and price:
        infos = []
        all_users = db.session.query(Watchlist.uid, Watchlist.criteria).filter(and_(Watchlist.itemid == item_id, Watchlist.criteria >= price)).all()
        for user in all_users:
            print(user)
            user_info = db.session.query(User).get(user.uid)
            print(user_info)
            if user_info:
                info = {
                    'uid': user_info.uid,
                    'username': user_info.username,
                    'email': user_info.email,
                    'criteria': user.criteria
                }
                infos.append(info)
        content = jsonify(
            all_users = infos,
            status = 'success'
        )
    resp = make_response(content)
    resp.headers['Access-Control-Allow-Origin'] = 'http://localhost:3000'
    resp.headers['Access-Control-Allow-Methods'] = 'GET'
    resp.headers['Access-Control-Allow-Headers'] = 'Content-Type'
    return resp

    