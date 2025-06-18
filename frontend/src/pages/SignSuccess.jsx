import { FaCheckCircle } from 'react-icons/fa';
import { useNavigate } from 'react-router-dom';

const SignSuccess = () => {
  const navigate = useNavigate();

  return (
    <div className="max-w-md mx-auto p-8 bg-white shadow-md rounded-lg text-center">
      <FaCheckCircle className="text-green-500 text-6xl mx-auto mb-4" />
      <h2 className="text-3xl font-bold text-gray-800 mb-2">註冊成功！</h2>
      <p className="text-gray-600 mb-6">歡迎加入，我們已完成你的會員註冊。</p>

      <button
        onClick={() => navigate('/login')}
        className="w-full bg-orange-500 text-white py-2 font-semibold rounded hover:bg-orange-600 mb-4"
      >
        立即登入
      </button>

      <button
        onClick={() => navigate('/')}
        className="w-full text-blue-600 underline text-sm hover:text-blue-800"
      >
        返回首頁
      </button>
    </div>
  );
};

export default SignSuccess;
