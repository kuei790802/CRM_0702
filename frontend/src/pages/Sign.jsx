import { useState } from 'react';
import { FaFacebook, FaLine } from 'react-icons/fa';
import { Link } from "react-router-dom";

const Sign = () => {
  const [email, setEmail] = useState('');
  const [agreePromo, setAgreePromo] = useState(false);
  const [agreeTerms, setAgreeTerms] = useState(false);

  const isFormValid = email && agreeTerms;

  const handleSubmit = () => {
    if (isFormValid) {
      console.log('Proceeding to next step with email:', email);
    }
  };

  return (
    <div className="max-w-md mx-auto p-8 bg-white shadow-md rounded-lg">
      <h2 className="text-3xl font-bold mb-6">註冊會員</h2>

      <input
        type="email"
        placeholder="電郵"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        className="w-full border-b border-gray-300 py-2 mb-6 focus:outline-none"
      />

      <div className="flex items-start mb-4">
        <input
          type="checkbox"
          checked={agreePromo}
          onChange={() => setAgreePromo(!agreePromo)}
          className="mt-1 mr-2"
        />
        <span>
          我願意接收良野的最新優惠消息及服務推廣相關資訊
        </span>
      </div>

      <div className="flex items-start mb-6">
        <input
          type="checkbox"
          checked={agreeTerms}
          onChange={() => setAgreeTerms(!agreeTerms)}
          className="mt-1 mr-2"
        />
        <span>
          我同意網站
          <a href="#" className="text-blue-600 underline ml-1">
            服務條款及隱私權政策
          </a>
        </span>
      </div>
      <Link to="/CustDetail">
      <button
        onClick={handleSubmit}
        disabled={!isFormValid}
        className={`w-full py-2 font-bold rounded ${
          isFormValid
            ? 'bg-orange-500 text-white hover:bg-orange-600'
            : 'bg-gray-200 text-gray-400 cursor-not-allowed'
        }`}
      >
        下一步
      </button>
      </Link>
      <div className="text-center my-6 text-sm text-gray-600">
        或使用社群帳戶註冊
      </div>

      <div className="flex justify-center gap-6 mb-10">
        <button className="text-green-500 text-3xl">
          <FaLine />
        </button>
        <button className="text-blue-600 text-3xl">
          <FaFacebook />
        </button>
      </div>

      <div className="text-center mt-8 border-t pt-6">
        <p className="text-lg font-semibold mb-2">已經有帳號？</p>
        <p className="text-sm mb-4">立即登入享有更多優惠！</p>
        <Link to="/Login">
        <button className="w-full border border-orange-500 text-orange-500 py-2 rounded hover:bg-orange-100">
          登入
        </button>
        </Link>
      </div>
    </div>
  );
};

export default Sign;
