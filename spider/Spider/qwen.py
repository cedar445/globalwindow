import requests
import os
from requests.adapters import HTTPAdapter, Retry


def getKeyWords(text):
    api_key = os.getenv("DASHSCOPE_API_KEY")
    url = 'https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation'
    headers = {'Content-Type': 'application/json',
            'Authorization':f'Bearer {api_key}'}
    body = {
        'model': 'qwen1.5-0.5b-chat',
        "input": {
            "messages": [
                {
                    "role": "system",
                    "content": "你的任务是对新闻或者事件生成至多5个关键词。每个关键词之间用逗号分隔。"
                },
                {
                    "role": "user",
                    "content": text
                }
            ]
        },
        "parameters": {
            "result_format": "message"
        }
    }
    session = requests.Session()
    retry = Retry(total=20, backoff_factor=0.1, status_forcelist=[500, 502, 503, 504, 443])
    adapter = HTTPAdapter(max_retries=retry)
    session.mount('http://', adapter)
    session.mount('https://', adapter)
    try:
        response = session.post(url, headers=headers, json=body)
        response.raise_for_status()
        content = response.json()['output']['choices'][0]['message']['content']
        return content
    except requests.exceptions.RequestException as e:
        print(f"keyword请求错误: {e}")
        return "请求失败"

def getEmotion(text):
    api_key = os.getenv("DASHSCOPE_API_KEY")
    url = 'https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation'
    headers = {'Content-Type': 'application/json',
            'Authorization':f'Bearer {api_key}'}
    body = {
        'model': 'qwen1.5-0.5b-chat',
        "input": {
            "messages": [
                {
                    "role": "system",
                    "content": "你的任务是对一个新闻进行情感分析。给出的结果类似于{悲伤，坚定，愤怒，恐惧，快乐，平静等等}。如果难以分析，则给出结果为:未知"
                },
                {
                    "role": "user",
                    "content": text
                }
            ]
        },
        "parameters": {
            "result_format": "message"
        }
    }
    session = requests.Session()
    retry = Retry(total=10, backoff_factor=0.1, status_forcelist=[500, 502, 503, 504, 443])
    adapter = HTTPAdapter(max_retries=retry)
    session.mount('http://', adapter)
    session.mount('https://', adapter)

    try:
        response = session.post(url, headers=headers, json=body, verify=False)
        response.raise_for_status()
        content = response.json()['output']['choices'][0]['message']['content']
        return content
    except requests.exceptions.RequestException as e:
        print(f"emotion请求错误: {e}")
        return '未知'  # 默认返回值

def getAttitude(text):
    api_key = os.getenv("DASHSCOPE_API_KEY")
    url = 'https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation'
    headers = {'Content-Type': 'application/json',
            'Authorization':f'Bearer {api_key}'}
    body = {
        'model': 'qwen1.5-0.5b-chat',
        "input": {
            "messages": [
                {
                    "role": "system",
                    "content": "你的任务是对一个评论进行情感分析。给出的结果类似于{支持1，反对-1，中立0等等}。只用输出数字{0，1，2}。"
                },
                {
                    "role": "user",
                    "content": text
                }
            ]
        },
        "parameters": {
            "result_format": "message"
        }
    }
    session = requests.Session()
    retry = Retry(total=10, backoff_factor=0.1, status_forcelist=[500, 502, 503, 504, 443])
    adapter = HTTPAdapter(max_retries=retry)
    session.mount('http://', adapter)
    session.mount('https://', adapter)

    try:
        response = session.post(url, headers=headers, json=body, verify=False)
        response.raise_for_status()
        content = response.json()['output']['choices'][0]['message']['content']
        return content
    except requests.exceptions.RequestException as e:
        print(f"请求错误：{e}")
        content = 0
        return  content

