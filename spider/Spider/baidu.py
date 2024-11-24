from selenium import webdriver
from selenium.webdriver.edge.service import Service
from selenium.webdriver.edge.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from bs4 import BeautifulSoup
import os
import dataupload
from fake_useragent import UserAgent
import urllib3


urllib3.disable_warnings(urllib3.exceptions.InsecureRequestWarning)


ua = UserAgent()
random_user_agent = ua.random

headers = {
    'User-Agent': random_user_agent
}

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


def baidu_job():
    url = 'https://top.baidu.com/board?tab=realtime'
    try:
        driver.get(url=url)

        WebDriverWait(driver, 15)
        html = driver.page_source
        soup = BeautifulSoup(html, 'html.parser')

        category_elements = driver.find_elements(By.CSS_SELECTOR, 'div.category-wrap_iQLoo.horizontal_1eKyQ')

        for category_element in category_elements:
            popularity_element = category_element.find_element(By.CSS_SELECTOR, 'div.hot-index_1Bl1a')
            popularity = popularity_element.text.strip()

            title_element = category_element.find_element(By.CSS_SELECTOR, 'div.c-single-text-ellipsis')
            title_content = title_element.text.strip()

            text_element = category_element.find_element(By.CSS_SELECTOR, 'div.hot-desc_1m_jR.large_nSuFU ')
            text_content = text_element.text.strip()

            link_element = category_element.find_element(By.CSS_SELECTOR, 'a.img-wrapper_29V76')
            link_url = link_element.get_attribute('href')

            dataupload.insert_event(title_content, popularity, title_content + text_content, link_url, '百度')
    except Exception as e:
        print(f"出现错误: {e}")
    finally:
        driver.quit()