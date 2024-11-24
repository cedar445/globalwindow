

import axiosInstance from "./axios";

interface news {
    newsId:number;
    news:string;
    type:string;
    popularity:number;
    link:string;
    picturelink:string;
    source:string;
    attitude:number;
    time:string;
    emotion:string;
    eventId:string
}

const newsService = {
    async getNewsByType(num: number, type: string): Promise<news[]> {
        try {
            const response = await axiosInstance.get<news[]>(`/news/getByType`, {
                params: { type: type, limitNum: num } 
            });
            return response.data;
        } catch (error) {
            console.error('Failed to fetch news:', error);
            throw error;
        }
    },
    
    async getNewsBySource(num: number, source: string): Promise<news[]> {
        try {
            const response = await axiosInstance.get<news[]>(`/news/getBySource`, {
                params: { source: source, limitNum: num } 
            });
            return response.data;
        } catch (error) {
            console.error('Failed to fetch news:', error);
            throw error;
        }
    },

    async getNewsByPopularity(num: number): Promise<news[]> {
        try {
            const response = await axiosInstance.get<news[]>(`/news/getByPopularity`, {
                params: { limitNum: num }   
            });
            return response.data;
        } catch (error) {
            console.error('Failed to fetch news:', error);
            throw error;
        }
    },

    async searchNews(query: string, num: number): Promise<news[]> {
        try {
            const response = await axiosInstance.get<news[]>(`/news/searchNews`, {
                params: { query: query, limitNum: num }
            });
            return response.data;
        } catch (error) {
            console.error('Failed to search news:', error);
            throw error;
        }
    },

    async getNewsByX(num: number): Promise<news[]> {
        try {
            const response = await axiosInstance.get<news[]>(`/news/getNewsByX`, {
                params: { limitNum: num } 
            });
            return response.data;
        } catch (error) {
            console.error('Failed to fetch news:', error);
            throw error;
        }
    },

    async getNewsImages(number: number): Promise<string[]> {
        try {
            const response = await axiosInstance.get<string[]>(`/news/getNewsImages`, {
                params: { limitNum: number }
            });
            return response.data;
        } catch (error) {
            console.error('Failed to fetch news images:', error);
            throw error;
        }
    },

    async getNewsById(newsId: number): Promise<news> {
        try {
            const response = await axiosInstance.get<news>(`/news/getNewsById`, {
                params: { newsId: newsId }
            });
            return response.data;
        } catch (error) {
            console.error('Failed to fetch news by id:', error);
            throw error;
        }
    },

    async getNewsRelatedNews(newsId: number, num: number): Promise<news[]> {

        try {
            const response = await axiosInstance.get<news[]>(`/news/getNewsRelatedNews`, {
                params: { newsId: newsId, limitNum: num }
            });
            return response.data;
        } catch (error) {
            console.error('Failed to fetch related news:', error);
            throw error;
        }
    },

    async getNewsComments(newsId: number, num: number): Promise<news[]> {

        try {
            const response = await axiosInstance.get<news[]>(`/news/getNewsComments`, {
                params: { newsId: newsId, limitNum: num }
            });
            return response.data;
        } catch (error) {
            console.error('Failed to fetch comments:', error);
            throw error;
        }
    },

    async getNewsKeywords(newsId: number): Promise<string[]> {

        try {
            const response = await axiosInstance.get<string[]>(`/news/getNewsKeywords`, {
                params: { newsId: newsId }
            });
            return response.data;
        } catch (error) {
            console.error('Failed to fetch keywords:', error);
            throw error;
        }
    },

    async getNewsEmotion(newsId: number): Promise<string[]> {

        try {
            const response = await axiosInstance.get<string[]>(`/news/getNewsEmotion`, {
                params: { newsId: newsId }
            });
            return response.data;
        } catch (error) {
            console.error('Failed to fetch emotions:', error);
            throw error;
        }
    },

    async getNewsAttitude(newsId: number): Promise<number> {

        try {
            const response = await axiosInstance.get<number>(`/news/getNewsAttitude`, {
                params: { newsId: newsId }
            });
            return response.data;
        } catch (error) {
            console.error('Failed to fetch atitude:', error);
            throw error;
        }
    },

    async getNewsEchartsData(newsId: number): Promise<any> {

        try {
            const response = await axiosInstance.get<any>(`/news/getNewsEchartsData`, {
                params: { newsId: newsId }
            });
            return response.data;
        } catch (error) {
            console.error('Failed to fetch echarts data:', error);
            throw error;
        }
    }

}

export default newsService;
export type { news };