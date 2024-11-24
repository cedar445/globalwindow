import requests
import datetime

#热搜基类
class HotSearch:
    def __init__(self, platform, url, headers):
        self.platform = platform
        self.url = url
        self.headers = headers

    def get_hot_searches(self):
        raise NotImplementedError("Subclasses should implement this method")

    def save_to_file(self, hot_searches_list):
        filename = f'{self.platform}_list.js'
        with open(filename, 'w', encoding='utf-8') as f:
            f.write(str(hot_searches_list))

'''
list形式
    'type': type, 热搜类型（属于哪个平台）
    'name': name, 热搜标题
    'rank': rank, 排名
    'startTime': startTime, 用于传递数据的参数
    'concretetime': formatted_time, 上榜时间（时间戳已转化）
    'lastCount': lastCount 当前热度

'''
class WeiboHotSearch(HotSearch):
    def __init__(self, headers):
        super().__init__('weibo', 'https://trends.zhiweidata.com/hotSearchTrend/search/longTimeInListSearch?type=weibo&sortType=realTime', headers)

    def get_hot_searches(self):
        hot_searches_list = []
        response = requests.get(url=self.url, headers=self.headers)
        js_data = response.json()
        for data in js_data['data']:
            if not data['recommend']:
                type = data['type']
                rank = data['rank']
                name = data['name']
                startTime = data['startTime']
                lastCount = data['lastCount']
                dt_object = datetime.datetime.fromtimestamp(startTime / 1000)
                formatted_time = dt_object.strftime("%Y-%m-%d %H:%M:%S")
                hot_searches_list.append({
                    'type': type,
                    'name': name,
                    'rank': rank,
                    'startTime': startTime,
                    'concretetime': formatted_time,
                    'lastCount': lastCount
                })
        return hot_searches_list

class LittleRedBookHotSearch(HotSearch):
    def __init__(self, headers):
        super().__init__('little-red-book', 'https://trends.zhiweidata.com/hotSearchTrend/search/longTimeInListSearch?type=little-red-book&sortType=realTime', headers)

    def get_hot_searches(self):
        hot_searches_list = []
        response = requests.get(url=self.url, headers=self.headers)
        js_data = response.json()
        for data in js_data['data']:
            type = data['type']
            name = data['name']
            rank = data['rank']
            startTime = data['startTime']
            lastCount = data['lastCount']
            dt_object = datetime.datetime.fromtimestamp(startTime / 1000)
            formatted_time = dt_object.strftime("%Y-%m-%d %H:%M:%S")
            hot_searches_list.append({
                'type': type,
                'name': name,
                'rank': rank,
                'startTime': startTime,
                'concretetime': formatted_time,
                'lastCount': lastCount
            })
        return hot_searches_list

class DouyinHotSearch(HotSearch):
    def __init__(self, headers):
        super().__init__('douyin', 'https://trends.zhiweidata.com/hotSearchTrend/search/longTimeInListSearch?type=douyin&sortType=realTime', headers)

    def get_hot_searches(self):
        hot_searches_list = []
        response = requests.get(url=self.url, headers=self.headers)
        js_data = response.json()
        for data in js_data['data']:
            type = data['type']
            name = data['name']
            rank = data['rank']
            startTime = data['startTime']
            lastCount = data['lastCount']
            dt_object = datetime.datetime.fromtimestamp(startTime / 1000)
            formatted_time = dt_object.strftime("%Y-%m-%d %H:%M:%S")
            hot_searches_list.append({
                'type': type,
                'name': name,
                'rank': rank,
                'startTime': startTime,
                'concretetime': formatted_time,
                'lastCount': lastCount
            })
        return hot_searches_list

class ZhihuHotSearch(HotSearch):
    def __init__(self, headers):
        super().__init__('zhihu', 'https://trends.zhiweidata.com/hotSearchTrend/search/longTimeInListSearch?type=zhihu&sortType=realTime', headers)

    def get_hot_searches(self):
        hot_searches_list = []
        response = requests.get(url=self.url, headers=self.headers)
        js_data = response.json()
        for data in js_data['data']:
            type = data['type']
            name = data['name']
            rank = data['rank']
            startTime = data['startTime']
            lastCount = data['lastCount']
            dt_object = datetime.datetime.fromtimestamp(startTime / 1000)
            formatted_time = dt_object.strftime("%Y-%m-%d %H:%M:%S")
            hot_searches_list.append({
                'type': type,
                'name': name,
                'rank': rank,
                'startTime': startTime,
                'concretetime': formatted_time,
                'lastCount': lastCount
            })
        return hot_searches_list

class BaiduHotSearch(HotSearch):
    def __init__(self, headers):
        super().__init__('baidu', 'https://trends.zhiweidata.com/hotSearchTrend/search/longTimeInListSearch?type=baidu&sortType=realTime', headers)

    def get_hot_searches(self):
        hot_searches_list = []
        response = requests.get(url=self.url, headers=self.headers)
        js_data = response.json()
        for data in js_data['data']:
            type = data['type']
            name = data['name']
            rank = data['rank']
            startTime = data['startTime']
            lastCount = data['lastCount']
            dt_object = datetime.datetime.fromtimestamp(startTime / 1000)
            formatted_time = dt_object.strftime("%Y-%m-%d %H:%M:%S")
            hot_searches_list.append({
                'type': type,
                'name': name,
                'rank': rank,
                'startTime': startTime,
                'concretetime': formatted_time,
                'lastCount': lastCount
            })
        return hot_searches_list


class ToutiaoHotSearch(HotSearch):
    def __init__(self, headers):
        super().__init__('toutiao', 'https://trends.zhiweidata.com/hotSearchTrend/search/longTimeInListSearch?type=toutiao&sortType=realTime', headers)

    def get_hot_searches(self):
        hot_searches_list = []
        response = requests.get(url=self.url, headers=self.headers)
        js_data = response.json()
        for data in js_data['data']:
            type = data['type']
            name = data['name']
            rank = data['rank']
            startTime = data['startTime']
            dt_object = datetime.datetime.fromtimestamp(startTime / 1000)
            formatted_time = dt_object.strftime("%Y-%m-%d %H:%M:%S")
            hot_searches_list.append({
                'type': type,
                'name': name,
                'rank': rank,
                'startTime': startTime,
                'concretetime': formatted_time,
            })
        return hot_searches_list


class BiliBiliHotSearch(HotSearch):
    def __init__(self, headers):
        super().__init__('bilibili', 'https://trends.zhiweidata.com/hotSearchTrend/search/longTimeInListSearch?type=bilibili&sortType=realTime', headers)

    def get_hot_searches(self):
        hot_searches_list = []
        response = requests.get(url=self.url, headers=self.headers)
        js_data = response.json()
        for data in js_data['data']:
            type = data['type']
            name = data['name']
            rank = data['rank']
            startTime = data['startTime']
            dt_object = datetime.datetime.fromtimestamp(startTime / 1000)
            formatted_time = dt_object.strftime("%Y-%m-%d %H:%M:%S")
            hot_searches_list.append({
                'type': type,
                'name': name,
                'rank': rank,
                'startTime': startTime,
                'concretetime': formatted_time

            })
        return hot_searches_list

#Token存在时效性，采集时需要手动*登录*获取更改，不然有些热搜只能爬取十条（有token则为30条）
headers = {
    'Token': 'eyJhbGciOiJIUzI1NiJ9.eyJ1aWQiOiJ0ZXN0dWlkIiwic3ViIjoie1wiY3JlYXRlX0F0XCI6MTcyMDQ5MTMxMTEyNixcImN1c3RvbVwiOmZhbHNlLFwiZW50ZXJwcmlzZVwiOmZhbHNlLFwiZmVuc2lcIjowLFwiZmlyc3RMb2dpblwiOjAsXCJndWFuemh1XCI6MCxcImlkXCI6XCI2NjhjOWQyZjFjNTY2NjAwMDE2NTljYTNcIixcImluc2lkZVwiOmZhbHNlLFwibG9jYXRpb25cIjpcIuacquefpeWcsOWMulwiLFwibG9naW5CeUxhc3RUaW1lXCI6MTcyMDUxMTkwNTIwMyxcInBhc3N3b3JkXCI6XCJEMERDQkYwRDEyQTZCMUU3RkJGQTJDRTU4NDhGM0VGRlwiLFwicGhvbmVcIjpcIjE1MDYxMTM4MjMwXCIsXCJyZUxpbWl0XCI6NSxcInNleFdCXCI6XCLmnKrnn6XmgKfliKtcIixcInN0YXJcIjowLFwidGFnc1wiOlwi5rS76LeDXCIsXCJ1c2VQcm9kdWN0XCI6W1widHJlbmRzXCJdLFwidXNlclR5cGVcIjpcInRyZW5kXCIsXCJ1c2VyX2ltZ1wiOlwi5pyq55-l5aS05YOP5Zyw5Z2AXCIsXCJ1c2VybmFtZVwiOlwi5omL5py655So5oi3MTUwNjExMzgyMzBcIixcInZ0eXBlXCI6LTMsXCJ3ZWlkb3VcIjowfSIsInVzZXJfbmFtZSI6Impqd3QiLCJuaWNrX25hbWUiOiJlZnRva2VuIiwiaXNzIjoiemhpd2VpZGF0YSIsImV4cCI6MTcyMDU4MDMyMCwiaWF0IjoxNzIwNTczMTIwLCJqdGkiOiJDMDY1MzEyRDdGQTY2MTI0QTQ2RTUzNUM3QjhCQkZBRCJ9.jf0rgAvMYJESHbOZAQ6-Tffr875OyBXVdP9piOkJhsc',
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36'
}


weibo_hot_search = WeiboHotSearch(headers)
weibo_list = weibo_hot_search.get_hot_searches()
weibo_hot_search.save_to_file(weibo_list)

little_red_book_hot_search = LittleRedBookHotSearch(headers)
little_red_book_list = little_red_book_hot_search.get_hot_searches()
little_red_book_hot_search.save_to_file(little_red_book_list)


douyin_hot_search = DouyinHotSearch(headers)
douyin_list = douyin_hot_search.get_hot_searches()
douyin_hot_search.save_to_file(douyin_list)

zhihu_hot_search = ZhihuHotSearch(headers)
zhihu_list = zhihu_hot_search.get_hot_searches()
zhihu_hot_search.save_to_file(zhihu_list)

baidu_hot_search = BaiduHotSearch(headers)
baidu_list = baidu_hot_search.get_hot_searches()
baidu_hot_search.save_to_file(baidu_list)

toutiao_hot_search = ToutiaoHotSearch(headers)
toutiao_list = toutiao_hot_search.get_hot_searches()
toutiao_hot_search.save_to_file(toutiao_list)

bilibili_hot_search = BiliBiliHotSearch(headers)
bilibili_list = bilibili_hot_search.get_hot_searches()
bilibili_hot_search.save_to_file(bilibili_list)