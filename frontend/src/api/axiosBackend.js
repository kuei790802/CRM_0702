import axios from 'axios';

const axiosBackend = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

export default axiosBackend;
