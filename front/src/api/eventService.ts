import axiosInstance from './axios'
import {news} from './newsService'
import {keyword} from './keywordService'

interface event {
  eventId: string;
  event: string;
  popularity: number;
}

export interface Comment {
  commentId: number;
  comment: string;
  source: string;
}

const eventService = {
  async getEvent(num: number): Promise<event[]> {
    try {
      const response = await axiosInstance.get<event[]>(`/event/getEvent`, {
        params: { limitNum: num }
      });
      return response.data;
    } catch (error) {
      console.error('Failed to fetch events:', error);
      throw error;
    }
  },

  async getEventById(eventId: string): Promise<event> {
    try {
      const response = await axiosInstance.get<event>(`/event/getEventById`, {
        params: { eventId: eventId }
      });
      return response.data;
    } catch (error) {
      console.error('Failed to fetch event by id:', error);
      throw error;
    }
  },

  async getRelatedNews(eventId: string, num: number): Promise<news[]> {
    try {
      const response = await axiosInstance.get<news[]>(`/event/getRelatedNews`, {
        params: { eventId: eventId, limitNum: num }
      });
      return response.data;
    } catch (error) {
      console.error('Failed to fetch related news:', error);
      throw error;
    }
  },
  async getEventSource(eventId: string): Promise<[string, string, string]> {
    try {
      const response = await axiosInstance.get<[string, string, string]>(`/event/getEventSource`, {
        params: { eventId: eventId }
      });
      return response.data;
    } catch (error) {
      console.error('Failed to fetch event source:', error);
      throw error;
    }
  },

  async getEventEchartsData(eventId: string): Promise<any> {
    try {
      const response = await axiosInstance.get<any>(`/event/getEventEchartsData`, {
        params: { eventId: eventId }
      });
      return response.data;
    } catch (error) {
      console.error('Failed to fetch event Echarts data:', error);
      throw error;
    }
  },

  async getEventComment(eventId: string, num: number): Promise<Comment[]> {
    try {
      const response = await axiosInstance.get<Comment[]>(`/event/getEventComments`, {
        params: { eventId: eventId, limitNum: num }
      });
      return response.data;
    } catch (error) {
      console.error('Failed to fetch event comments:', error);
      throw error;
    }
  },

  async searchEvent(keyword: string, num: number): Promise<event[]> {
    try {
      const response = await axiosInstance.get<event[]>(`/event/searchEvent`, {
        params: { keyword: keyword, limitNum: num }
      });
      return response.data;
    } catch (error) {
      console.error('Failed to search events:', error);
      throw error;
    }
  },

  async getRelatedKeywords(eventId: string, num: number): Promise<keyword[]> {
    try {
      const response = await axiosInstance.get<keyword[]>(`/event/getRelatedKeywords`, {
        params: { eventId: eventId, limitNum: num } 
      });
      return response.data;
    } catch (error) {
      console.error('Failed to fetch related keywords:', error);
      throw error;
    }
  },

  async getHotEventBySource(source: string, num: number): Promise<event[]> {
    try {
      const response = await axiosInstance.get<event[]>(`/event/getHotEventBySource`, {
        params: { source: source, limitNum: num } 
      });
      return response.data;
    } catch (error) {
      console.error('Failed to fetch hot events by source:', error);
      throw error;
    }
  },

  async getEventTimeline(eventId: string): Promise<any> {
    try {
      const response = await axiosInstance.get<any>(`/event/getTimeLine`, {
        params: { eventId: eventId }  
      });
      return response.data;
    } catch (error) {
      console.error('Failed to fetch event timeline:', error);
      throw error;
    }
  }
}

export type { event };
export default eventService;
