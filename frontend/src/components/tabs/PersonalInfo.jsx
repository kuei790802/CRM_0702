import React, { useState } from "react";

function PersonalInfo() {
  const initialForm = {
    name: "lin",
    email: "abz666666a@gmail.com",
    phone: "0912345678",
    countryCode: "+886",
    gender: "男",
    birthday: "1996-04-23",
    contactPhone: "0912345678",
  };

  const [form, setForm] = useState(initialForm);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleCancel = () => {
    setForm(initialForm);
  };

  const handleSave = () => {
    console.log("儲存資料：", form);
    alert("已儲存變更");
  };

  return (
    <div className="grid md:grid-cols-2 gap-6 text-sm text-gray-800">
      {/* 左：會員資料 */}
      <div className="bg-white rounded-2xl p-6 shadow border border-gray-100">
        <h2 className="text-lg font-semibold mb-4">會員資料</h2>

        {/* 姓名 */}
        <div className="mb-4">
          <label className="block mb-1 text-gray-600 font-medium">姓名</label>
          <input
            name="name"
            value={form.name}
            onChange={handleChange}
            className="w-full border border-gray-200 rounded-xl px-4 py-2 focus:ring-logo-tan focus:outline-none"
          />
        </div>

        {/* 電郵 */}
        <div className="mb-4">
          <label className="block mb-1 text-gray-600 font-medium">電郵</label>
          <input
            type="email"
            name="email"
            value={form.email}
            onChange={handleChange}
            className="w-full border border-gray-200 rounded-xl px-4 py-2 focus:ring-logo-tan focus:outline-none"
          />
        </div>

        {/* 手機 */}
        <div className="mb-4">
          <label className="block mb-1 text-gray-600 font-medium">
            手機號碼
          </label>
          <div className="flex gap-2">
            <select
              name="countryCode"
              value={form.countryCode}
              onChange={handleChange}
              className="border border-gray-200 rounded-xl px-3 py-2 focus:ring-logo-tan focus:outline-none"
            >
              <option value="+886">TW +886</option>
              <option value="+81">JP +81</option>
              <option value="+1">US +1</option>
            </select>
            <input
              type="tel"
              name="phone"
              value={form.phone}
              onChange={handleChange}
              className="flex-1 border border-gray-200 rounded-xl px-4 py-2 focus:ring-logo-tan focus:outline-none"
            />
          </div>
        </div>

        {/* 性別 */}
        <div className="mb-4">
          <label className="block mb-1 text-gray-600 font-medium">
            性別（選填）
          </label>
          <select
            name="gender"
            value={form.gender}
            onChange={handleChange}
            className="w-full border border-gray-200 rounded-xl px-4 py-2 focus:ring-logo-tan focus:outline-none"
          >
            <option value="">請選擇</option>
            <option value="男">男</option>
            <option value="女">女</option>
          </select>
        </div>

        {/* 生日 */}
        <div className="mb-4">
          <label className="block mb-1 text-gray-600 font-medium">
            生日日期
          </label>
          <input
            type="date"
            name="birthday"
            value={form.birthday}
            onChange={handleChange}
            className="w-full border border-gray-200 rounded-xl px-4 py-2 focus:ring-logo-tan focus:outline-none"
          />
        </div>

        {/* 密碼 */}
        <div className="mb-4">
          <label className="block mb-1 text-gray-600 font-medium">密碼</label>
          <a href="#" className="text-blue-600 hover:underline">
            設定新的密碼
          </a>
        </div>

        {/* 刪除帳號 */}
        <div className="mt-6">
          <button
            onClick={() => {
              const confirmed =
                window.confirm("確定要刪除帳號嗎？此動作將無法復原！");
              if (confirmed) {
                // 實際上這裡應該呼叫 API 或執行 logout + 刪除
                console.log("帳號已刪除");
                alert("帳號已刪除");
              }
            }}
            className="text-red-500 border border-red-200 hover:bg-red-50 px-5 py-2 rounded-xl transition"
          >
            刪除帳號
          </button>
        </div>
      </div>

      {/* 右：送貨與付款資料 */}
      <div className="bg-white rounded-2xl p-6 shadow border border-gray-100">
        <h2 className="text-lg font-semibold mb-4">送貨與付款資料</h2>

        {/* 聯絡電話 */}
        <div className="mb-4">
          <label className="block mb-1 text-gray-600 font-medium">
            聯絡電話（選填）
          </label>
          <div className="flex gap-2">
            <select
              name="countryCode"
              value={form.countryCode}
              onChange={handleChange}
              className="border border-gray-200 rounded-xl px-3 py-2 focus:ring-logo-tan focus:outline-none"
            >
              <option value="+886">TW +886</option>
              <option value="+81">JP +81</option>
              <option value="+1">US +1</option>
            </select>
            <input
              type="tel"
              name="contactPhone"
              value={form.contactPhone}
              onChange={handleChange}
              className="flex-1 border border-gray-200 rounded-xl px-4 py-2 focus:ring-logo-tan focus:outline-none"
            />
          </div>
        </div>

        {/* 地址 */}
        <div className="mb-4 space-y-5">
          <label className="block mb-1 text-gray-600 font-medium">
            儲存的地址（選填）
          </label>

          {/* 收件人 */}
          <div>
            <label className="block mb-1 text-gray-600 font-medium">
              收件人
            </label>
            <input
              name="recipient"
              className="w-full border border-gray-200 rounded-xl px-4 py-2 focus:ring-logo-tan focus:outline-none"
              placeholder="請輸入收件人姓名"
            />
          </div>

          {/* 電話 */}
          <div>
            <label className="block mb-1 text-gray-600 font-medium">
              收件人電話號碼
            </label>
            <div className="flex gap-2">
              <select className="border border-gray-200 rounded-xl px-3 py-2 focus:outline-none">
                <option value="+886">TW +886</option>
                <option value="+81">JP +81</option>
                <option value="+1">US +1</option>
              </select>
              <input
                type="tel"
                className="flex-1 border border-gray-200 rounded-xl px-4 py-2 focus:ring-logo-tan focus:outline-none"
                placeholder="0912 345 678"
              />
            </div>
          </div>

          {/* 國家 */}
          <div>
            <label className="block mb-1 text-gray-600 font-medium">
              送貨地點
            </label>
            <select className="w-full border border-gray-200 rounded-xl px-4 py-2 focus:ring-logo-tan focus:outline-none">
              <option value="台灣">台灣</option>
              <option value="日本">日本</option>
              <option value="美國">美國</option>
            </select>
          </div>

          {/* 城市 */}
          <div>
            <label className="block mb-1 text-gray-600 font-medium">
              城市 / 縣
            </label>
            <select className="w-full border border-gray-200 rounded-xl px-4 py-2 focus:ring-logo-tan focus:outline-none">
              <option value="">請選擇</option>
              <option value="台北市">台北市</option>
              <option value="新北市">新北市</option>
              <option value="高雄市">高雄市</option>
            </select>
          </div>

          {/* 地區 */}
          <div>
            <label className="block mb-1 text-gray-600 font-medium">地區</label>
            <select className="w-full border border-gray-200 rounded-xl px-4 py-2 focus:ring-logo-tan focus:outline-none">
              <option value="">請選擇</option>
              <option value="信義區">信義區</option>
              <option value="中山區">中山區</option>
              <option value="三重區">三重區</option>
            </select>
          </div>

          {/* 地址 */}
          <div>
            <label className="block mb-1 text-gray-600 font-medium">地址</label>
            <input
              className="w-full border border-gray-200 rounded-xl px-4 py-2 focus:ring-logo-tan focus:outline-none"
              placeholder="請輸入詳細地址"
            />
          </div>
        </div>
      </div>

      {/* 按鈕 */}
      <div className="col-span-2 flex justify-end gap-4 mt-4">
        <button
          onClick={handleCancel}
          className="px-6 py-2 border border-gray-300 rounded-xl text-gray-700 hover:bg-gray-100 transition"
        >
          取消
        </button>
        <button
          onClick={handleSave}
          className="px-6 py-2 bg-logo-tan text-white rounded-xl hover:opacity-90 transition"
        >
          儲存變更
        </button>
      </div>
    </div>
  );
}

export default PersonalInfo;
