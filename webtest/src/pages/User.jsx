import React from 'react';

function User() {
  return (
    <div className="min-h-screen bg-[#f8f5f1] text-gray-800 px-6 py-4">
      {/* Header */}
      <div className="flex justify-between items-center mb-6 text-sm">
        <div>
          你好, <span className="font-bold">User</span>
        </div>
        <button className="text-blue-600 hover:underline">登出</button>
      </div>

      {/* Tabs */}
      <div className="flex border-b border-gray-300 text-center text-sm font-medium">
        {['個人資訊', '商店購物金', '優惠券', '訊息', '訂單',].map((tab, index) => (
          <div
            key={index}
            className={`px-6 py-3 cursor-pointer ${
              index === 0 ? 'bg-white border-t border-l border-r border-gray-300 rounded-t-md font-bold' : ''
            }`}
          >
            {tab}
          </div>
        ))}
      </div>

      {/* Content Placeholder */}
      <div className="bg-white min-h-[400px] p-6 border border-t-0 border-gray-300 rounded-b-md">
        {/* 這裡之後會放每個分頁的實際內容 */}
      </div>

      {/* Action Buttons */}
      <div className="flex justify-end gap-3 mt-6">
        <button className="px-4 py-2 border rounded text-gray-700 hover:bg-gray-100">取消</button>
        <button className="px-4 py-2 bg-orange-500 text-white rounded hover:bg-orange-600">儲存變更</button>
      </div>
    </div>
  );
}

export default User;
