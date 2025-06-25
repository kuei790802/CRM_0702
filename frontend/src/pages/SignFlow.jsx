import { useState } from 'react';
import { FaEye, FaEyeSlash } from 'react-icons/fa';
import { useNavigate } from 'react-router-dom';
import axios from '../api/axiosFrontend';

const SignFlow = () => {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    account: '',
    email: '',
    address: '',
    agreePromo: false,
    username: '',
    password: '',
    birthYear: '',
    birthMonth: '',
    birthDay: '',
  });

  const [showPassword, setShowPassword] = useState(false);
  const [hasSubmitted, setHasSubmitted] = useState(false);

  const togglePasswordVisibility = () => setShowPassword(!showPassword);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setForm((prev) => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value,
    }));
  };

  const isBirthComplete =
    form.birthYear && form.birthMonth && form.birthDay;

  const isFormValid =
    form.account &&
    form.email &&
    form.username &&
    form.password &&
    isBirthComplete;

  const handleSubmit = async () => {
    setHasSubmitted(true);
    if (!isFormValid) return;

    const birthDate = `${form.birthYear}-${String(form.birthMonth).padStart(
      2,
      '0'
    )}-${String(form.birthDay).padStart(2, '0')}`;

    const payload = {
      account: form.account,
      password: form.password,
      email: form.email,
      customerName: form.username,
      address: form.address,
      birthday: birthDate,
    };

    try {
      const response = await axios.post('/customer/register', payload);
      console.log('註冊成功:', response.data);
      navigate('/SignSuccess');
    } catch (error) {
      console.error('註冊失敗:', error.response?.data || error.message);
      alert('註冊失敗，請確認輸入資料是否正確');
    }
  };

  const inputStyle = (field) =>
    `w-full py-3 px-4 rounded-md border transition focus:outline-none focus:ring-2 focus:ring-orange-400 ${
      hasSubmitted && !form[field]
        ? 'border-red-500 bg-red-50'
        : 'border-gray-300'
    }`;

  return (
    <div className="max-w-md mx-auto p-6 bg-white shadow-md rounded-lg">
      <h2 className="text-3xl font-bold mb-2">立即註冊會員</h2>
      <p className="text-sm text-gray-600 mb-4">請填寫以下資訊以建立帳號</p>

      {/* 帳號 */}
      <div className="mb-4">
        <input
          type="text"
          name="account"
          placeholder="帳號"
          value={form.account}
          onChange={handleChange}
          className={inputStyle('account')}
        />
        {hasSubmitted && !form.account && (
          <p className="text-red-500 text-sm mt-1 ml-1">⚠️ 請輸入你的帳號</p>
        )}
      </div>

      {/* Email */}
      <div className="mb-4">
        <input
          type="email"
          name="email"
          placeholder="電子郵件"
          value={form.email}
          onChange={handleChange}
          className={inputStyle('email')}
        />
        {hasSubmitted && !form.email && (
          <p className="text-red-500 text-sm mt-1 ml-1">⚠️ 請輸入你的電子郵件</p>
        )}
      </div>

      {/* 地址 */}
      <div className="mb-4">
        <input
          type="text"
          name="address"
          placeholder="地址（選填）"
          value={form.address}
          onChange={handleChange}
          className="w-full py-3 px-4 rounded-md border border-gray-300 focus:outline-none focus:ring-2 focus:ring-orange-400"
        />
      </div>

      {/* 用戶名 */}
      <div className="mb-4">
        <input
          type="text"
          name="username"
          placeholder="用戶名"
          value={form.username}
          onChange={handleChange}
          className={inputStyle('username')}
        />
        {hasSubmitted && !form.username && (
          <p className="text-red-500 text-sm mt-1 ml-1">⚠️ 請輸入你的用戶名</p>
        )}
      </div>

      {/* 密碼 */}
      <div className="relative mb-4">
        <input
          type={showPassword ? 'text' : 'password'}
          name="password"
          placeholder="密碼"
          value={form.password}
          onChange={handleChange}
          className={`${inputStyle('password')} pr-10`}
        />
        <button
          type="button"
          onClick={togglePasswordVisibility}
          className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-500"
        >
          {showPassword ? <FaEyeSlash /> : <FaEye />}
        </button>
        {hasSubmitted && !form.password && (
          <p className="text-red-500 text-sm mt-1 ml-1">⚠️ 請輸入你的密碼</p>
        )}
      </div>

      {/* 出生日 */}
      <div className="mb-4">
        <label className="block text-gray-700 font-medium mb-2">出生日期</label>
        <div className="flex justify-between gap-2">
          <select
            name="birthYear"
            value={form.birthYear}
            onChange={handleChange}
            className="w-1/3 border rounded-md py-2 px-2 focus:outline-none border-gray-300"
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
            className="w-1/3 border rounded-md py-2 px-2 focus:outline-none border-gray-300"
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
            className="w-1/3 border rounded-md py-2 px-2 focus:outline-none border-gray-300"
          >
            <option value="">日</option>
            {Array.from({ length: 31 }, (_, i) => i + 1).map((d) => (
              <option key={d} value={d}>
                {d}
              </option>
            ))}
          </select>
        </div>
        {hasSubmitted && !isBirthComplete && (
          <p className="text-red-500 text-sm mt-1 ml-1">⚠️ 請選擇完整出生日期</p>
        )}
      </div>

      {/* 提交按鈕 */}
      <button
        onClick={handleSubmit}
        disabled={!isFormValid}
        className={`w-full font-bold py-3 rounded-md transition ${
          isFormValid
            ? 'bg-orange-500 text-white hover:bg-orange-600'
            : 'bg-gray-200 text-gray-400 cursor-not-allowed'
        }`}
      >
        立即加入
      </button>
    </div>
  );
};

export default SignFlow;
