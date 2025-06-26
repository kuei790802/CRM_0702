import axios from 'axios';

const axiosFrontend = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

export default axiosFrontend;
