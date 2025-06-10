// src/components/Login.jsx
import { useState } from "react";
import { FaEye, FaEyeSlash, FaFacebook } from "react-icons/fa";
import { SiLine } from "react-icons/si";
import { Link } from "react-router-dom";

function Login() {
  const [showPassword, setShowPassword] = useState(false);

  return (
    <div className="max-w-md mx-auto bg-white p-8 rounded shadow-md">
      <h2 className="text-2xl font-bold mb-6">登入</h2>

      <div className="mb-4">
        <input
          type="text"
          placeholder="電郵或手機號碼"
          className="w-full border-b border-gray-300 py-2 focus:outline-none"
        />
      </div>

      <div className="mb-2 relative">
        <input
          type={showPassword ? "text" : "password"}
          placeholder="密碼"
          className="w-full border-b border-gray-300 py-2 focus:outline-none"
        />
        <div
          className="absolute right-2 top-2 cursor-pointer"
          onClick={() => setShowPassword(!showPassword)}
        >
          {showPassword ? <FaEye /> : <FaEyeSlash />}
        </div>
      </div>

      <div className="mb-4 text-sm text-blue-600 cursor-pointer">忘記密碼？</div>
      <Link to="/User" >
      <button className="w-full bg-orange-500 text-white py-2 rounded font-bold hover:bg-orange-600">
        開始購物吧！
      </button>
      </Link>

      <div className="text-center mt-6 text-sm text-gray-600">或使用社群帳號登入</div>

      <div className="flex justify-center space-x-6 mt-4">
        <SiLine className="text-3xl text-green-500 cursor-pointer" />
        <FaFacebook className="text-3xl text-blue-600 cursor-pointer" />
      </div>

      <div className="text-center mt-10">
        <p className="text-lg font-bold">還不是會員？</p>
        <button className="mt-2 px-6 py-2 border border-orange-500 text-orange-500 font-semibold rounded hover:bg-orange-50">
          註冊會員
        </button>
      </div>
    </div>
  );
}

export default Login;
