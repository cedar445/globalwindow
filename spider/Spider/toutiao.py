import schedule
from selenium import webdriver
from selenium.webdriver.edge.service import Service
from selenium.webdriver.edge.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from bs4 import BeautifulSoup
import os
import re
import requests
import dataupload
from fake_useragent import UserAgent
import time
import urllib3

urllib3.disable_warnings(urllib3.exceptions.InsecureRequestWarning)


ua = UserAgent()
random_user_agent = ua.random

headers = {
    'User-Agent': random_user_agent
}

hot_search_url = 'https://www.toutiao.com/hot-event/hot-board/?origin=toutiao_pc&_signature=_02B4Z6wo00f01RVR9eQAAIDAygoGq7lXxCkVdfFAACPD3SZRleE74tkzDv4ac2R08F8ia2aX8GZCQqTiKvvRAGpfYoIAeouRsdESUJ1XBng8cBiqELK5QhH9sZWOOW8bHySbvhOT-wd.k-Tp0c](https://www.toutiao.com/hot-event/hot-board/?origin=toutiao_pc&_signature=_02B4Z6wo00f01RVR9eQAAIDAygoGq7lXxCkVdfFAACPD3SZRleE74tkzDv4ac2R08F8ia2aX8GZCQqTiKvvRAGpfYoIAeouRsdESUJ1XBng8cBiqELK5QhH9sZWOOW8bHySbvhOT-wd.k-Tp0c)'

def get_hot_searches():
    response = requests.get(hot_search_url, headers=headers,verify=True)
    try:
        result = response.json()
    except requests.exceptions.JSONDecodeError:
        print("获取热搜列表时解析JSON出错")
        return []

    hot_searches = []
    for tag in result['data']:
        text = tag['Title']
        link = tag['Url']
        popularity = tag['HotValue']

        hot_searches.append({'热搜': text,'热度': popularity, 'link': link})
    return hot_searches

# Edge选项
edge_options = Options()
edge_options.add_argument("--headless")
edge_options.add_argument("--disable-gpu")
edge_options.add_argument("--window-size=1920x1080")
edge_options.add_argument('--ignore-ssl-errors=yes')
edge_options.add_argument('--ignore-certificate-errors')

current_directory = os.path.dirname(os.path.abspath(__file__))
driver_path = os.path.join(current_directory, 'msedgedriver.exe')

service = Service(driver_path)

driver = webdriver.Edge(service=service, options=edge_options)

def get_data(url):
    try:
        driver.get(url = url)

        WebDriverWait(driver, 10).until(
            EC.presence_of_element_located((By.CSS_SELECTOR, 'div.feed-card-wrapper'))
        )
        html = driver.page_source
        soup = BeautifulSoup(html, 'html.parser')

        # 找到所有包含文章和视频的div
        articles_list = soup.select('div.feed-card-wrapper.feed-card-article-wrapper')
        videos_list = soup.select('div.feed-card-wrapper.feed-card-short-video-wrapper')
        news_list = []
        if articles_list:
            for item in articles_list:
                # 文章
                title = item.select_one('a.title')
                title_text = title.get_text(strip=True) if title else 'No title'
                link = title['href'] if title else 'No link'
                img_tag = item.select_one('div.feed-card-cover img')
                piclink = img_tag['src'] if img_tag else 'No image'

                comment = item.select_one('div.feed-card-footer-comment-cmp a[aria-label]')
                comment_count = comment.get_text(strip=True) if comment else 'No comments'
                if comment:
                    comment_count = re.search(r'\d+', str(comment)).group()
                else:
                    comment_count = 0

                news_list.append({'text': title_text, 'link': link, 'comment_count': comment_count, 'piclink': piclink})
        if videos_list:
            for item in videos_list:
                # 视频
                title = item.select_one('a.title')
                title_text = title.get_text(strip=True) if title else 'No title'
                link = title['href'] if title else 'No link'
                img_tag = item.select_one('div.feed-card-cover img')
                piclink = img_tag['src'] if img_tag else 'No image'

                comment = item.select_one('div.feed-card-footer-comment-cmp a[aria-label]')
                comment_count = comment.get_text(strip=True) if comment else 'No comments'
                if comment:
                    comment_count = re.search(r'\d+', str(comment)).group()
                else:
                    comment_count = 0

            news_list.append({'text': title_text, 'link': link, 'comment_count': comment_count, 'piclink': piclink})

        comments = soup.select('div.ttp-comment-item')
        comments_list = []
        for comment in comments:
            comment_text = comment.select_one('p.content').get_text(strip=True)

            like_tag = comment.select_one('div.ttp-comment-like span')
            like_count = int(re.search(r'\d+', str(like_tag)).group()) if re.search(r'\d+', str(like_tag)) else 0

            comments_list.append({'comment_text': comment_text, 'like_count': like_count})
    finally:
        return news_list, comments_list


def toutiao_job():
    hot_searches = get_hot_searches()

    for elem in hot_searches:
        eventId = dataupload.insert_event(elem['热搜'], elem['热度'], elem['热搜'], elem['link'], '今日头条')
        link = elem['link']
        if link.startswith('https://www.toutiao.com/trending/'):
            news_list, comments_list = get_data(link)
            for news in news_list:
                popularity = int(news['comment_count']) * 3
                dataupload.insert_news(news['text'], '综合', popularity, news['link'],'今日头条', eventId, news['piclink'])
                comment_link = news_list[0]['link']
                for comment in comments_list:
                    if comment['like_count'] >= 20:
                        dataupload.insert_comment(comment['comment_text'], '今日头条', dataupload.get_news_id_by_link(comment_link), comment['like_count'])
    driver.quit()


toutiao_job()