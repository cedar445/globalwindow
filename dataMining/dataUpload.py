import schedule
import time
import pymysql
import qwen

def db_connect_check():
    connTest = None  # 初始化为 None
    try:
        connTest = pymysql.connect(
            host='localhost',
            port=3306,
            user='root',
            password='meorin',
            database='globalwindow',
            charset='utf8mb4'
        )
        print('数据库连接成功')
    except Exception as e:
        print('数据库连接失败:', e)
    finally:
        connTest.close()

#@Insert("insert into comment values (#{comment},#{attitude},#{time},#{source},#{newsId},#{emotion},#{likeNum})")
def insert_comment(comment, attitude, time, source, newsId, emotion, likeNum):
    conn = pymysql.connect(
        host='localhost',
        port=3306,
        user='root', 
        password='meorin', 
        database='globalwindow', 
        charset='utf8mb4')
    cursor = conn.cursor()
    try:
        cursor.execute("insert into comment values (%s,%s,%s,%s,%s,%s,%s)", (comment, attitude, time, source, newsId, emotion, likeNum))
        conn.commit()
        print("执行成功")
    except Exception as e:
        print("执行错误:", e)
        conn.rollback()
    finally:
        cursor.close()
        conn.close()

#@Insert("insert into event values (#(eventId),#{keyword},#{event},#{popularity})")
def insert_event(event, popularity, text):
    conn = pymysql.connect(
        host='localhost',
        port=3306,
        user='root',
        password='meorin',
        database='globalwindow',
        charset='utf8mb4')
    cursor = conn.cursor()
    try:
        keyword = qwen.getKeyWords(text)
        eventId = "Id:" + keyword
        cursor.execute("insert into event values (%s,%s,%s,%s)", (eventId, keyword, event, popularity))
        conn.commit()
        print("执行成功")
    except Exception as e:
        print("执行错误:", e)
        conn.rollback()
    finally:
        cursor.close()
        conn.close()

# @Insert("insert into news(news,type,popularity,link,picturelink,source,attitude,time,emotion,eventId) values (#{news},#{type},#{popularity},#{link},#{picturelink},#{source},#{attitude},#{time},#{emotion},#{eventId})")
def insert_news(news, type, popularity, link, source, time, eventId, attitude = '', picturelink=''):
    conn = pymysql.connect(
        host='localhost',
        port=3306,
        user='root',
        password='meorin',
        database='globalwindow',
        charset='utf8mb4')
    cursor = conn.cursor()
    try:
        emotion = qwen.getEmotion(news)
        cursor.execute("insert into news(news,type,popularity,link,picturelink,source,attitude,time,emotion,eventId) values (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)", (news, type, popularity, link, picturelink, source, attitude, time, emotion, eventId))
        conn.commit()
        print("执行成功")
    except Exception as e:
        print("执行错误:", e)
        conn.rollback()
    finally:
        cursor.close()
        conn.close()


# 每天的特定时间执行
#schedule.every().day.at("10:30").do(db_task)

# 持续运行
# while True:
#     schedule.run_pending()
#     time.sleep(1)

# a = "宝马中国将退出价格战"
# b = 1
# c = "【#宝马中国将退出价格战#，维护品牌价值】宝马中国决定退出持续近一年的 “降价保份额”。据车 Fans 创始人孙少军称，因价格战导致门店亏损严重，宝马将从 7 月起，通过减少销售量来稳定价格，缓解门店的经营压力。目前宝马暂未作出回应。今年初，永达汽车在年报里提到其代理经销的保时捷、宝马主动调减销售计划。上半年，包括宝马在内的豪华品牌积极参与打折，尤其是电动车。宝马目前折价最高的车型之一宝马 i3，裸车价已相较 35.39 万元的指导价砍半，上险后的落地价已从去年的 25 万左右降到 20 万元以内。宝马 5 系燃油车的起售价降至约 31 万元，相当于七折优惠。去年宝马降价的策略保住份额。全年折扣率为 17.66%，高于行业平均水平。去年宝马在华交付 82.5 万辆，同比增长 4%，放缓的增长率已显示出守住份额的艰难。而以价换量的代价早已显现，宝马去年净利润同比下滑超三成，汽车业务的息税前利润率为 9.8%，低于市场预期。降价没有换销量显著增长。宝马中国上半年累计卖出 37.6 万辆车，同比下滑 4%。同期宝马全球销量同比增长。同样大幅降价的奔驰，上半年在华销量同比下滑近 6%。奔驰 E 级、奔驰 C 级的起售价均较指导价低约 10 万元。（晚点财经）"
# insert_event(a, b, c)