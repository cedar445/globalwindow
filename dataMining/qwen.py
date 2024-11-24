import requests
import os

def getKeyWords(text):
    api_key = os.getenv("DASHSCOPE_API_KEY")
    url = 'https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation'
    headers = {'Content-Type': 'application/json',
            'Authorization':f'Bearer {api_key}'}
    body = {
        'model': 'qwen-turbo',
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

    response = requests.post(url, headers=headers, json=body)
    content = response.json()['output']['choices'][0]['message']['content']
    return content

def getEmotion(text):
    api_key = os.getenv("DASHSCOPE_API_KEY")
    url = 'https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation'
    headers = {'Content-Type': 'application/json',
            'Authorization':f'Bearer {api_key}'}
    body = {
        'model': 'qwen-turbo',
        "input": {
            "messages": [
                {
                    "role": "system",
                    "content": "你的任务是对一个新闻进行情感分析。给出的结果类似于{悲伤，坚定，愤怒，恐惧，快乐，平静等等}。"
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

    response = requests.post(url, headers=headers, json=body)
    content = response.json()['output']['choices'][0]['message']['content']
    return content