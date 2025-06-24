import { create } from 'zustand';
import axiosInstance from '../api/axiosFrontend';

const useUserStore = create((set) => {
  const storedToken = localStorage.getItem('token');
  const storedUser = localStorage.getItem('user');

  if (storedToken) {
    axiosInstance.defaults.headers.common['Authorization'] = `Bearer ${storedToken}`;
  }

  return {
    user: storedUser ? JSON.parse(storedUser) : null,
    token: storedToken || null,
    isAuthenticated: !!storedToken,

    login: async (credentials) => {
      try {
        const response = await axiosInstance.post('/auth/login', credentials);
        const { token, user } = response.data;


        // 儲存 token 和 user 到 localStorage
        localStorage.setItem('token', token);
        localStorage.setItem('user', JSON.stringify(user));

        // 設定 axios header
        axiosInstance.defaults.headers.common['Authorization'] = `Bearer ${token}`;

        set({ user, token, isAuthenticated: true });
      } catch (error) {
        console.error('Login failed:', error);
        throw error;
      }
    },

    logout: () => {
      // 清除 axios header
      delete axiosInstance.defaults.headers.common['Authorization'];

      // 清除 localStorage
      localStorage.removeItem('token');
      localStorage.removeItem('user');

      set({ user: null, token: null, isAuthenticated: false });
    },

    fetchProfile: async () => {
      try {
        const response = await axiosInstance.get('/auth/profile');
        set({ user: response.data });
        localStorage.setItem('user', JSON.stringify(response.data));
      } catch (error) {
        console.error('Fetch profile failed:', error);
      }
    },
  };
});

export default useUserStore;
