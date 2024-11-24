import requests
import dataupload

url = 'https://api.bilibili.com/x/web-interface/wbi/search/square?limit=10&platform=web&w_rid=9e148bc78dc7f49d622decc07fdd8a43&wts=1720165135'

headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3'
}
def job():
    response = requests.get(url, headers=headers)
    try:
        result = response.json()
    except requests.exceptions.JSONDecodeError:
        print("获取热搜列表时解析JSON出错")
        return []

    for tag in result['data']['trending']['list']:
        event = tag.get('show_name')
        popularity = 0
        text = tag.get('show_name')
        link = 'https://search.bilibili.com/all?{event}&from_source=webtop_search&spm_id_from=333.1007&search_source=4'
        dataupload.insert_event(event,popularity,text,link,'B站')
job()