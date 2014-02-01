from flask import Flask, render_template, request, g, flash, redirect, url_for
from forms import SubReddit, Comments
import json
import requests
import random
import sqlite3


DATABASE = '/home/droidBehavior/mysite/data.db'
app = Flask(__name__)
app.config.from_object(__name__)
app.secret_key='jacob'
CSRF_ENABLED=True


def connect_db():
    return sqlite3.connect(app.config['DATABASE'])

@app.before_request
def before_request():
    g.db = connect_db()
    g.db.execute('PRAGMA foreign_keys = ON;')
    g.db.text_factory = str


@app.teardown_request
def teardown_request(exception):
    g.db.commit()
    g.db.close()

@app.route('/index',methods=['GET','POST'])
@app.route('/',methods=['GET','POST'])
def index():
    form = SubReddit()
    formB = Comments()
    rand = random.randint(0,99)
    sr = g.db.execute('select sr from subreddit')
    srr = sr.fetchone()[0]
    r = requests.get('http://www.reddit.com/r/{subreddit}.json?limit=1000'.format(subreddit=srr))
    j = json.loads(r.content)
    pic = j['data']['children'][rand]['data']['url']
    title = j['data']['children'][rand]['data']['title']
    if pic.lower().startswith('http://imgur') and not '/a/'in pic and not '/gallery/' in pic:
                pic = pic.replace('http://imgur', 'http://i.imgur')
                pic = pic+'.jpg'
                print 'adding imgur+jpg1'
                print pic
    if not 'imgur' in pic:
        print 'not imgur'
        flash('not imgur')
        print pic
    if form.validate_on_submit():
        srr = form.subreddit.data
        r = requests.get('http://www.reddit.com/r/{subreddit}.json?limit=1000'.format(subreddit=srr))
        try:
            print 'trying'
            print form.subreddit.data
            j = json.loads(r.content)
            print j['data']['children'][0]['data']['url']
        except:
            print 'invalid json'
            flash('invalid subreddit')
            return redirect(url_for('index'))
        if j['data']['children'][rand]['data']['url']:
            print 'adding to db'
            g.db.execute("UPDATE subreddit SET sr=(?) where id=1", [form.subreddit.data])
            sr = g.db.execute('select sr from subreddit')
            srr = sr.fetchone()[0]
            pic = j['data']['children'][rand]['data']['url']
            if pic.lower().startswith('http://imgur') and not '/a/'in pic and not '/gallery/' in pic:
                pic = pic.replace('http://imgur', 'http://i.imgur')
                pic = pic+'.jpg'
                print 'adding imgur+jpg2'
                print pic
                return render_template('index.html',form=form,formB=formB,srr=srr,pic=pic,title=title)
            else:
                print 'not an imgur2'
                return render_template('index.html',form=form,formB=formB,srr=srr,pic=pic,title=title)
        else:
            print 'not valid json'
            return render_template('index.html',form=form,formB=formB,srr=srr,pic=pic)
    else:
        pass
    print 'clean submit'
    return render_template('index.html',form=form,formB=formB,pic=pic,title=title,srr=srr)

@app.route('/comments',methods=['GET','POST'])
def comments():
    form = SubReddit()
    formB = Comments()
    cur = g.db.cursor()
    exist = None
    if formB.validate_on_submit():
        print 'valid submit comment'
        username = request.form['user']
        user = requests.get('http://www.reddit.com/user/{username}/comments.json?limit=1000'.format(username=username))
        j = json.loads(user.content)
        try:
            print j['error']
            print 'user does not exist'
            flash('user not exist')
            exist = 'No'
            return render_template('comments.html',form=form,formB=formB,exist=exist)
        except:
            pass
        id = cur.execute('select id from user where username=(?)', [username])
        userid = id.fetchone()
        try:
            print j['data']['children'][0]['data']
            print ' user has comments'
            exist = 'Yes'
        except:
            print 'user no comments'
            exist = 'No'
        if userid:
            print 'user exists'
            a = g.db.execute('select title,comments from comments where user_id=(?)', [userid[0]])
            a = a.fetchall()
            return render_template('comments.html',form=form,a=a,formB=formB,exist=exist)
        else:
            j = json.loads(user.content)
            print 'adding user'
            cur.execute('insert into user(username) values(?)', [username])
            lastrow=cur.lastrowid
            id = cur.execute('select id from user where username=(?)', [username])
            userid = id.fetchone()
            for i in j['data']['children']:
                cur.execute('insert into comments(title,comments,user_id) values(?,?,?)', [i['data']['link_title'],
                                                                                        i['data']['body'],lastrow])

            a = g.db.execute('select title,comments from comments where user_id=(?)', [userid[0]])
            a = a.fetchall()
            return render_template('comments.html',form=form,formB=formB,a=a,exist=exist)
    else:
        print 'not valid comment submit'
        return render_template('comments.html',form=form,formB=formB)

    return render_template('comments.html',form=form,formB=formB)




if __name__ == '__main__':
    app.run(debug=True)