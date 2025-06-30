import { create } from 'zustand';
import axiosInstance from '../api/axiosBackend';

const useBackUserStore = create((set) => {
  const storedToken = localStorage.getItem('back_token');
  const storedUser = localStorage.getItem('back_user');

  if (storedToken) {
    axiosInstance.defaults.headers.common['Authorization'] = `Bearer ${storedToken}`;
  }

  return {
    backUser: storedUser ? JSON.parse(storedUser) : null,
    backToken: storedToken || null,
    isBackAuthenticated: !!storedToken,

    loginBackUser: async (credentials) => {
      try {
        const res = await axiosInstance.post('/user/auth/login', credentials);
        const { token, user } = res.data;

        localStorage.setItem('back_token', token);
        localStorage.setItem('back_user', JSON.stringify(user));
        axiosInstance.defaults.headers.common['Authorization'] = `Bearer ${token}`;

        set({ backUser: user, backToken: token, isBackAuthenticated: true });
        return user; // ✅ 回傳 user，供 login 後導向使用
      } catch (error) {
        console.error('後台登入失敗:', error);
        throw error;
      }
    },

    logoutBackUser: () => {
      delete axiosInstance.defaults.headers.common['Authorization'];
      localStorage.removeItem('back_token');
      localStorage.removeItem('back_user');
      set({ backUser: null, backToken: null, isBackAuthenticated: false });
    },

    fetchBackProfile: async () => {
      try {
        const res = await axiosInstance.get('/backauth/profile');
        set({ backUser: res.data });
        localStorage.setItem('back_user', JSON.stringify(res.data));
      } catch (err) {
        console.error('後台使用者資訊取得失敗', err);
      }
    },
  };
});

export default useBackUserStore;
