import schedule
import time
import requests
from bs4 import BeautifulSoup
import dataupload
import urllib3

# 禁用 SSL 验证警告
urllib3.disable_warnings(urllib3.exceptions.InsecureRequestWarning)

dataupload.db_connect_check()

headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3'
}

def get_hot_searches():
    hot_search_url = 'https://weibo.com/ajax/side/hotSearch'
    response = requests.get(hot_search_url, headers=headers)
    try:
        result = response.json()
    except requests.exceptions.JSONDecodeError:
        print("获取热搜列表时解析JSON出错")
        return []

    hot_searches = []
    for tag in result['data']['realtime']:
        hot_search = tag.get('word')
        link = f'https://m.s.weibo.com/ajax_topic/detail?q=%23{hot_search}%23&show_rank_info=1'
        hot_searches.append({'热搜': tag.get('word'), '类型': tag.get('category', '综合'), 'link': link ,'热度': tag.get('num')})
    return hot_searches

def get_hot_search_details(hot_search):
    detail_url = f'https://m.s.weibo.com/ajax_topic/detail?q=%23{hot_search}%23&show_rank_info=1'
    response = requests.get(detail_url, headers=headers)
    try:
        result = response.json()
    except requests.exceptions.JSONDecodeError:
        print(f"获取热搜详情 '{hot_search}' 时解析JSON出错")
        return {'热搜': str(hot_search), '阅读量': 'N/A', '讨论量': 'N/A', '互动量': 'N/A', '原创量': 'N/A'}

    if 'data' in result and 'baseData' in result['data']:
        base_data = result['data']['baseData']
        if 'sum_all' in base_data:
            sum_all = base_data['sum_all']
            hot_search_details = {
                '热搜': str(hot_search),
                '阅读量': str(sum_all['r']['val']) + sum_all['r']['unit'],
                '讨论量': str(sum_all['m']['val']) + sum_all['m']['unit'],
                '互动量': str(sum_all['interact']['val']) + sum_all['interact']['unit'],
                '原创量': str(sum_all['ori_m']['val']) + sum_all['ori_m']['unit'],
            }
            return hot_search_details
    return {'热搜': str(hot_search), '阅读量': 'N/A', '讨论量': 'N/A', '互动量': 'N/A', '原创量': 'N/A'}

def get_post(search):
    search_url = f'https://m.weibo.cn/api/container/getIndex?containerid=100103type%3D60%26q%3D{search}&title=%E7%83%AD%E9%97%A8-{search}&cardid=weibo_page&extparam=title%3D%E7%83%AD%E9%97%A8%26mid%3D%26q%3D%{search}&luicode=10000011&lfid=100103type%3D1%26t%3D10%26q%3D%{search}'
    response = requests.get(url=search_url, headers=headers, timeout=10)
    try:
        result = response.json()
    except requests.exceptions.JSONDecodeError:
        print(f"获取热搜相关话题帖子 '{search}' 时解析JSON出错")
        return []
    post_list = []
    if 'data' in result and 'cards' in result['data']:
        for mblog in result['data']['cards']:
            mblog_data = mblog.get('mblog', {})
            post_list.append({
                    'id': mblog_data.get('id'),
                    'mid': mblog_data.get('mid'),
                    'text': clean_html(mblog_data.get('text')),
                    'created_at': mblog_data.get('created_at'),
                    'attitudes_count': mblog_data.get('attitudes_count'),
                    'comments_count': mblog_data.get('comments_count'),
                    'reposts_count': mblog_data.get('reposts_count')
                })
    return post_list


def get_comment(post_id):
    url = f'https://m.weibo.cn/comments/hotflow?id={post_id}&mid={post_id}&max_id_type=0'
    response = requests.get(url=url, headers=headers)
    try:
        result = response.json()
    except requests.exceptions.JSONDecodeError:
        print(f"获取微博评论 '{post_id}' 时解析JSON出错")
        return []

    concrete_post_list = []

    comments_data = result.get('data', {}).get('data', [])

    for elem in comments_data:
        text = clean_html(elem['text'])
        like_count = elem['like_count']
        time = elem['created_at']
        if elem['text']:
            concrete_post_list.append({'评论时间': time, '评论文本': text, '点赞数': like_count})
    return concrete_post_list


def clean_html(html_content):
    if html_content is None:
        return ""
    try:
        soup = BeautifulSoup(html_content, 'html.parser')
        cleaned_text = soup.get_text(separator=' ', strip=True)
        return cleaned_text
    except Exception as e:
        print(f"Error cleaning HTML: {e}")
        return ""

def weibo_job():
    hot_searches = get_hot_searches()
    for hot_search in hot_searches:
        hot_search_details = get_hot_search_details(hot_search['热搜'])
        eventId = dataupload.insert_event(hot_search_details['热搜'],hot_search['热度'],hot_search_details['热搜'],hot_search['link'],'微博')
        hot_search_posts = get_post(hot_search['热搜'])
        for post in hot_search_posts:
            if post['id']:
                id = post['id']
                type = hot_search['类型']
                popularity = int(post['attitudes_count'])+int(post['comments_count'])+int(post['reposts_count'])
                link = f'https://m.weibo.cn/detail/{id}'
                dataupload.insert_news(post['text'], type, popularity, link, '微博', eventId)
                news_id = dataupload.get_news_id_by_link(link)
                hot_search_comments = get_comment(post['id'])
                count = 0
                for comment in hot_search_comments:
                    if comment['点赞数']>=50:
                        count += 1
                        dataupload.insert_comment(comment['评论文本'], '微博', news_id, comment['点赞数'])
                        if count >= 3:
                            count= 0
                            break