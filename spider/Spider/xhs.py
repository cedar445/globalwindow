import requests
import dataupload

url = 'https://trends.zhiweidata.com/hotSearchTrend/search/longTimeInListSearch?type=little-red-book&sortType=realTime'

headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3'
}


def xhs_job():
    response = requests.get(url=url, headers=headers)
    js_data = response.json()
    for data in js_data['data']:
        event = data['name']
        popularity = data['lastCount']
        link = f'https://www.xiaohongshu.com/search_result?keyword={event}'
        dataupload.insert_event(event,popularity,event,link,'小红书')
xhs_job()