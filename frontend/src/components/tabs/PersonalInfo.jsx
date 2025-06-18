import React, { useState } from 'react';

function PersonalInfo() {
  const initialData = {
    name: 'Brad',
    email: 'Brad@gmail.com',
    phone: '0912345678',
    countryCode: '+886',
    gender: '',
    birthday: '2025-04-23',
  };

  const [formData, setFormData] = useState(initialData);
  const [emailVerified, setEmailVerified] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSendVerification = () => {
    alert(`驗證信已發送至 ${formData.email}`);
    setEmailVerified(true);
  };

  const handleCancel = () => {
    setFormData(initialData);
    setEmailVerified(false);
  };

  const handleSave = () => {
    console.log('儲存會員資料：', formData);
    alert('變更已儲存！');
  };

  return (
    <div className="space-y-6 text-sm">
      {/* 姓名 */}
      <div>
        <label className="block mb-1 font-medium text-gray-700">姓名</label>
        <input
          type="text"
          name="name"
          value={formData.name}
          onChange={handleChange}
          className="w-full border border-gray-300 rounded px-3 py-2"
        />
      </div>

      {/* 電郵 + 驗證狀態 */}
      <div>
        <label className="block mb-1 font-medium text-gray-700">電郵</label>
        <div className="flex items-center space-x-3">
          <input
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            className="flex-1 border border-gray-300 rounded px-3 py-2"
          />
          {!emailVerified && (
            <div className="text-red-500 text-sm flex items-center gap-1">
              <span>⚠️ 尚未驗證</span>
            </div>
          )}
        </div>
        <button
          onClick={handleSendVerification}
          className="mt-2 px-4 py-1 bg-green-500 text-white rounded hover:bg-green-600"
        >
          發送驗證信
        </button>
      </div>

      {/* 手機號碼 */}
      <div>
        <label className="block mb-1 font-medium text-gray-700">手機號碼</label>
        <div className="flex gap-2">
          <select
            name="countryCode"
            value={formData.countryCode}
            onChange={handleChange}
            className="border border-gray-300 rounded px-2"
          >
            <option value="+886">TW +886</option>
            <option value="+81">JP +81</option>
            <option value="+1">US +1</option>
          </select>
          <input
            type="tel"
            name="phone"
            value={formData.phone}
            onChange={handleChange}
            className="flex-1 border border-gray-300 rounded px-3 py-2"
          />
        </div>
      </div>

      {/* 性別 */}
      <div>
        <label className="block mb-1 font-medium text-gray-700">性別（選填）</label>
        <select
          name="gender"
          value={formData.gender}
          onChange={handleChange}
          className="w-full border border-gray-300 rounded px-3 py-2"
        >
          <option value="">請選擇</option>
          <option value="male">男</option>
          <option value="female">女</option>
        </select>
      </div>

      {/* 生日 */}
      <div>
        <label className="block mb-1 font-medium text-gray-700">生日日期</label>
        <input
          type="date"
          name="birthday"
          value={formData.birthday}
          onChange={handleChange}
          className="border border-gray-300 rounded px-3 py-2"
        />
      </div>

      {/* 密碼設定連結 */}
      <div>
        <label className="block mb-1 font-medium text-gray-700">密碼</label>
        <a href="#" className="text-blue-600 hover:underline">
          設定新的密碼
        </a>
      </div>

      {/* 按鈕區 */}
      <div className="flex justify-end gap-3 mt-8">
        <button
          onClick={handleCancel}
          className="px-4 py-2 border border-gray-300 rounded text-gray-700 hover:bg-gray-100"
        >
          取消
        </button>
        <button
          onClick={handleSave}
          className="px-4 py-2 bg-orange-500 text-white rounded hover:bg-orange-600"
        >
          儲存變更
        </button>
      </div>
    </div>
  );
}

export default PersonalInfo;
