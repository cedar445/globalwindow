import axiosInstance from "./axios";
import { news } from "./newsService";

interface keyword {
    keyword: string;
    frequency: number;
}

const keywordService = {

    async getKeywordsFre(keyword: string): Promise<number> {
        try {
          const response = await axiosInstance.get<number>(`/keyword/getKeywordFre`, {
            params: { keyword: keyword }
          });
          return response.data;
        } catch (error) {
          console.error('Failed to get keyword frequency:', error);
          throw error;
        }
      },

    async searchKeywords(keyword: string, num: number): Promise<keyword[]> {
        try {
          const response = await axiosInstance.get<keyword[]>(`/keyword/searchKeywords`, {
            params: { keyword: keyword, limitNum: num }
          });
          return response.data;
        } catch (error) {
          console.error('Failed to search keywords:', error);
          throw error;
        }
      },

      async getHotKeywords(num: number): Promise<keyword[]> {
        try {
          const response = await axiosInstance.get<keyword[]>(`/keyword/getHotKeywords`, {
            params: { limitNum: num }
          });
          return response.data;
        } catch (error) {
          console.error('Failed to fetch hot keywords:', error);
          throw error;
        }
      },

      async getKeywordNews(keyword: string, num: number): Promise<news[]> {
        try {
          const response = await axiosInstance.get<news[]>(`/keyword/getKeywordNews`, {
            params: { keyword: keyword, limitNum: num }
          });
          return response.data;
        } catch (error) {
          console.error('Failed to fetch related news:', error);
          throw error;
        }
      },

      async getKeywordSource(keyword: string): Promise<[string, string, string]> {
        try {
          const response = await axiosInstance.get<[string, string, string]>(`/keyword/getKeywordSource`, {
            params: { keyword: keyword }
          });
          return response.data;
        } catch (error) {
          console.error('Failed to fetch event source:', error);
          throw error;
        }
      },

      async getKeywordEchartsData(keyword: string): Promise<any> {
        try {
          const response = await axiosInstance.get<any>(`/keyword/getKeywordEchartsData`, {
            params: { keyword: keyword }
          });
          return response.data;
        } catch (error) {
          console.error('Failed to fetch event Echarts data:', error);
          throw error;
        }
      },

}
export type {keyword};
export default keywordService;