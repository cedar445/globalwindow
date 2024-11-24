import requests
import mysql.connector
import pymysql
import dataupload
import urllib3

urllib3.disable_warnings(urllib3.exceptions.InsecureRequestWarning)
dataupload.db_connect_check()

headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3'
}

url = 'https://www.xuexi.cn/lgdata/index.json?_st=28679455&js_v=1706580237911'

def xuexi_job():
    response = requests.get(url = url, headers=headers,verify=False)
    try:
        result = response.json()
    except requests.exceptions.JSONDecodeError:
        print("获取学习强国时解析JSON出错")
        return []
    xuexi = []
    for tag in result['pageData']['important-news-text-list']:
        text = tag['title']['text']
        link = tag['title']['link']
        xuexi.append({'text':text,'link': link})
    for elem in xuexi:
        dataupload.insert_Xuexi(elem['text'],"重要新闻",elem['link'],"学习强国")
