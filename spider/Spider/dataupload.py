import time
import pymysql
import qwen
from datetime import datetime

def db_connect_check():
    connTest = None  # 初始化为 None
    try:
        connTest = pymysql.connect(
            host='localhost',
            port=3306,
            user='root',
            password='123456',
            database='globalwindow',
            charset='utf8mb4'
        )
        print('数据库连接成功')
    except Exception as e:
        print('数据库连接失败:', e)
    finally:
        connTest.close()

#@Insert("insert into event values (#(eventId),#{keyword},#{event},#{popularity},#{link}, #{source}, #{time})")
def insert_event(event, popularity, text, link, source):
    conn = pymysql.connect(
        host='localhost',
        port=3306,
        user='root',
        password='123456',
        database='globalwindow',
        charset='utf8mb4')
    cursor = conn.cursor()
    try:
        keyword = qwen.getKeyWords(text)
        time.sleep(1)
        eventId = "Id:" + keyword
        current_time = datetime.now()
        formatted_time = current_time.strftime('%Y-%m-%d %H:%M:%S')
        query = """
                INSERT INTO event (eventId, keyword, event, popularity, link, source, time)
                VALUES (%s, %s, %s, %s, %s, %s, %s)
                ON DUPLICATE KEY UPDATE
                keyword = VALUES(keyword),
                event = VALUES(event),
                popularity = VALUES(popularity),
                link = VALUES(link),
                source = VALUES(source),
                time = VALUES(time)
                """
        cursor.execute(query, (eventId, keyword, event, popularity,link,source,formatted_time))
        conn.commit()
        print("event添加执行成功")
    except Exception as e:
        print("event添加执行错误:", e)
        conn.rollback()
    finally:
        cursor.close()
        conn.close()
    return eventId

# @Insert("insert into news(news,type,popularity,link,picturelink,source,attitude,time,emotion,eventId) values (#{news},#{type},#{popularity},#{link},#{picturelink},#{source},#{attitude},#{time},#{emotion},#{eventId})")
def insert_news(news, type, popularity, link, source, eventId, picturelink=''):
    conn = pymysql.connect(
        host='localhost',
        port=3306,
        user='root',
        password='123456',
        database='globalwindow',
        charset='utf8mb4')
    cursor = conn.cursor()
    try:
        emotion = qwen.getEmotion(news)
        time.sleep(1)
        attitude = qwen.getAttitude(news)
        current_time = datetime.now()
        formatted_time = current_time.strftime('%Y-%m-%d %H:%M:%S')
        cursor.execute("insert into news(news,type,popularity,link,picturelink,source,attitude,time,emotion,eventId) values (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)", (news, type, popularity, link, picturelink, source, attitude, formatted_time, emotion, eventId))
        conn.commit()
        print("news添加执行成功")
    except Exception as e:
        print("news添加执行错误:", e)
        conn.rollback()
    finally:
        cursor.close()
        conn.close()

#@Insert("insert into comment values (#{comment},#{attitude},#{time},#{source},#{newsId},#{emotion},#{likeNum})")
def insert_comment(comment, source, newsId, likeNum):
    conn = pymysql.connect(
        host='localhost',
        port=3306,
        user='root',
        password='123456',
        database='globalwindow',
        charset='utf8mb4')
    cursor = conn.cursor()
    try:
        # 获取当前时间化为数据库适用的datetime格式
        current_time = datetime.now()
        formatted_time = current_time.strftime('%Y-%m-%d %H:%M:%S')
        emotion = qwen.getEmotion(comment)
        time.sleep(1)
        attitude = qwen.getAttitude(comment)
        cursor.execute("insert into comment(comment, attitude, time, source, newsId, emotion, likeNum) values (%s,%s,%s,%s,%s,%s,%s)", (comment, attitude,formatted_time, source, newsId, emotion, likeNum))
        conn.commit()
        print("comment添加执行成功")
    except Exception as e:
        print("comment添加执行错误:", e)
        conn.rollback()
    finally:
        cursor.close()
        conn.close()

def insert_Xuexi(news, type, link, source, picturelink=''):
    conn = pymysql.connect(
        host='localhost',
        port=3306,
        user='root',
        password='123456',
        database='globalwindow',
        charset='utf8mb4')
    cursor = conn.cursor()
    try:
        attitude = 0
        emotion = 'N/A'
        eventId = ''
        current_time = datetime.now()
        formatted_time = current_time.strftime('%Y-%m-%d %H:%M:%S')
        popularity = 0
        cursor.execute("insert into news(news,type,popularity,link,picturelink,source,attitude,time,emotion,eventId) values (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)", (news, type, popularity, link, picturelink, source, attitude, formatted_time, emotion, eventId))
        conn.commit()
        print("执行成功")
    except Exception as e:
        print("执行错误:", e)
        conn.rollback()
    finally:
        cursor.close()
        conn.close()

def get_news_id_by_link(news_link):
    conn = pymysql.connect(
        host='localhost',
        port=3306,
        user='root',
        password='123456',
        database='globalwindow',
        charset='utf8mb4')
    cursor = conn.cursor()
    try:
        cursor.execute("SELECT newsId FROM news WHERE link = %s", (news_link,))
        result = cursor.fetchone()

        if result:
            return result[0]  # 返回找到的新闻ID
        else:
            print("未找到与链接匹配的新闻")
            return None

    except Exception as e:
        print("查询新闻ID错误:", e)
        return None
    finally:
        cursor.close()
        conn.close()
