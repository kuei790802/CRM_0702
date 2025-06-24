import { useState } from 'react';
import { FaEye, FaEyeSlash, FaFacebook, FaLine } from 'react-icons/fa';
import { useNavigate } from 'react-router-dom';

const SignFlow = () => {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    email: '',
    agreePromo: false,
    agreeTerms: false,
    username: '',
    password: '',
    gender: '',
    birthYear: '',
    birthMonth: '',
    birthDay: '',
  });

  const [showPassword, setShowPassword] = useState(false);
  const togglePasswordVisibility = () => setShowPassword(!showPassword);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setForm((prev) => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value,
    }));
  };

  const isFormValid =
    form.email && form.username && form.password && form.agreeTerms;

  const handleSubmit = () => {
    const birthDate = `${form.birthYear}-${form.birthMonth}-${form.birthDay}`;
    const payload = { ...form, birthDate };
    console.log('註冊資料：', payload);
    navigate('/SignSuccess');
  };

  return (
    <div className="max-w-md mx-auto p-8 bg-white shadow-md rounded-lg">
      <h2 className="text-3xl font-bold mb-2">立即註冊會員</h2>
      <p className="text-sm text-gray-600 mb-6">請填寫以下資訊以建立帳號</p>

      {/* Email */}
      <input
        type="email"
        name="email"
        placeholder="電郵"
        value={form.email}
        onChange={handleChange}
        className="w-full border-b border-gray-300 py-2 mb-6 focus:outline-none"
      />

      {/* Username */}
      <input
        type="text"
        name="username"
        placeholder="用戶名"
        value={form.username}
        onChange={handleChange}
        className="w-full border-b border-gray-300 py-2 mb-6 focus:outline-none"
      />

      {/* Password */}
      <div className="relative mb-6">
        <input
          type={showPassword ? 'text' : 'password'}
          name="password"
          placeholder="密碼"
          value={form.password}
          onChange={handleChange}
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
        name="gender"
        value={form.gender}
        onChange={handleChange}
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
          name="birthYear"
          value={form.birthYear}
          onChange={handleChange}
          className="w-1/3 border-b border-gray-300 py-2 focus:outline-none"
        >
          <option value="">年</option>
          {Array.from({ length: 100 }, (_, i) => 2025 - i).map((y) => (
            <option key={y} value={y}>
              {y}
            </option>
          ))}
        </select>
        <select
          name="birthMonth"
          value={form.birthMonth}
          onChange={handleChange}
          className="w-1/3 border-b border-gray-300 py-2 focus:outline-none"
        >
          <option value="">月</option>
          {Array.from({ length: 12 }, (_, i) => i + 1).map((m) => (
            <option key={m} value={m}>
              {m}
            </option>
          ))}
        </select>
        <select
          name="birthDay"
          value={form.birthDay}
          onChange={handleChange}
          className="w-1/3 border-b border-gray-300 py-2 focus:outline-none"
        >
          <option value="">日</option>
          {Array.from({ length: 31 }, (_, i) => i + 1).map((d) => (
            <option key={d} value={d}>
              {d}
            </option>
          ))}
        </select>
      </div>

      {/* 同意條款 */}
      <div className="flex items-start mb-6">
        <input
          type="checkbox"
          name="agreeTerms"
          checked={form.agreeTerms}
          onChange={handleChange}
          className="mt-1 mr-2"
        />
        <span>
          我同意網站
          <a href="#" className="text-blue-600 underline ml-1">
            服務條款及隱私權政策
          </a>
        </span>
      </div>


      {/* Submit */}
      <button
        onClick={handleSubmit}
        disabled={!isFormValid}
        className={`w-full font-bold py-2 rounded ${
          isFormValid
            ? 'bg-orange-500 text-white hover:bg-orange-600'
            : 'bg-gray-200 text-gray-400 cursor-not-allowed'
        }`}
      >
        立即加入！
      </button>
    </div>
  );
};

export default SignFlow;
