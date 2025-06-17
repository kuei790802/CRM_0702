import { useState } from 'react';
import { FaEye, FaEyeSlash } from 'react-icons/fa';
import { Link } from "react-router-dom";

const CustDetail = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [gender, setGender] = useState('');
  const [birthYear, setBirthYear] = useState('');
  const [birthMonth, setBirthMonth] = useState('');
  const [birthDay, setBirthDay] = useState('');

  const togglePasswordVisibility = () => setShowPassword(!showPassword);

  const handleSubmit = () => {
    console.log({
      username,
      password,
      gender,
      birthDate: `${birthYear}-${birthMonth}-${birthDay}`,
    });
  };

  return (
    <div className="max-w-md mx-auto p-8 bg-white shadow-md rounded-lg">
      <h2 className="text-3xl font-bold mb-2">還差一步</h2>
      <p className="text-sm text-gray-600 mb-6">請輸入你的個人資料，以完成註冊流程！</p>

      {/* Username */}
      <input
        type="text"
        placeholder="用戶名"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
        className="w-full border-b border-gray-300 py-2 mb-6 focus:outline-none"
      />

      {/* Password */}
      <div className="relative mb-6">
        <input
          type={showPassword ? 'text' : 'password'}
          placeholder="密碼"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          className="w-full border-b border-gray-300 py-2 pr-10 focus:outline-none"
        />
        <button
          type="button"
          onClick={togglePasswordVisibility}
          className="absolute right-2 top-1/2 -translate-y-1/2 text-gray-500"
        >
          {showPassword ? <FaEyeSlash /> : <FaEye />}
        </button>
      </div>

      {/* Gender */}
      <select
        value={gender}
        onChange={(e) => setGender(e.target.value)}
        className="w-full border-b border-gray-300 py-2 mb-6 focus:outline-none text-gray-700"
      >
        <option value="">性別 (選填)</option>
        <option value="male">男</option>
        <option value="female">女</option>
        <option value="other">其他</option>
      </select>

      {/* Birthdate */}
      <div className="flex justify-between gap-2 mb-6">
        <select
          value={birthYear}
          onChange={(e) => setBirthYear(e.target.value)}
          className="w-1/3 border-b border-gray-300 py-2 focus:outline-none"
        >
          <option value="">年</option>
          {Array.from({ length: 100 }, (_, i) => 2025 - i).map((y) => (
            <option key={y} value={y}>{y}</option>
          ))}
        </select>
        <select
          value={birthMonth}
          onChange={(e) => setBirthMonth(e.target.value)}
          className="w-1/3 border-b border-gray-300 py-2 focus:outline-none"
        >
          <option value="">月</option>
          {Array.from({ length: 12 }, (_, i) => i + 1).map((m) => (
            <option key={m} value={m}>{m}</option>
          ))}
        </select>
        <select
          value={birthDay}
          onChange={(e) => setBirthDay(e.target.value)}
          className="w-1/3 border-b border-gray-300 py-2 focus:outline-none"
        >
          <option value="">日</option>
          {Array.from({ length: 31 }, (_, i) => i + 1).map((d) => (
            <option key={d} value={d}>{d}</option>
          ))}
        </select>
      </div>

      {/* Social Subscribe */}
      <p className="text-sm text-gray-600 mb-2">
        使用社群平台訂閱優惠消息及服務推廣相關資訊
      </p>
      <div className="flex gap-4 mb-8">
        <button className="border border-green-500 text-green-600 px-4 py-2 rounded hover:bg-green-100">
          訂閱 LINE
        </button>
        <button className="border border-blue-500 text-blue-600 px-4 py-2 rounded hover:bg-blue-100">
          訂閱 Facebook
        </button>
      </div>

      {/* Submit */}
      <Link to="/SignSuccess">
      <button
        onClick={handleSubmit}
        className="w-full bg-orange-500 text-white font-bold py-2 rounded hover:bg-orange-600"
      >
        立即加入！
      </button>
    </Link>
    
        {/* Back Button */}
    <Link to="/Sign">
      <button
        className="w-full mt-4 text-blue-500 text-sm underline hover:text-blue-700"
        onClick={() => window.history.back()}
      >
        返回上一步
      </button>
    </Link>
    </div>
  );
};

export default CustDetail;
