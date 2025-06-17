import React from 'react';

function StoreCredits({ storeCredits = 50 }) {
  const records = [
    {
      date: '2025/05/28 1:07am',
      item: '新加入會員購物金',
      amount: 50,
      expireDate: '2025/07/27',
    },
    // 可擴充其他紀錄
  ];

  return (
    <div className="text-sm space-y-6">
      {/* 標題區 */}
      <div className="flex items-center gap-2 text-gray-700 font-bold text-base">
        <span>🪙</span>
        <span>購物金紀錄</span>
      </div>

      {/* 現有購物金卡片 */}
      <div className="bg-gray-100 rounded-lg p-6 text-center">
        <p className="text-gray-500 mb-2">現有購物金</p>
        <p className="text-3xl font-bold text-gray-800">{storeCredits}</p>
      </div>

      {/* 資料表格 */}
      <div className="overflow-x-auto">
        <table className="w-full table-auto text-left border-t border-gray-300">
          <thead className="text-gray-600">
            <tr className="border-b border-gray-200">
              <th className="py-2 pr-4 font-semibold">日期</th>
              <th className="py-2 pr-4 font-semibold">購物金項目</th>
              <th className="py-2 pr-4 font-semibold">購物金款項</th>
              <th className="py-2 pr-4 font-semibold">到期日</th>
              <th className="py-2 font-semibold">購物金餘額</th>
            </tr>
          </thead>
          <tbody>
            {records.map((record, index) => (
              <tr key={index} className="border-b border-gray-100">
                <td className="py-2 pr-4 text-gray-700">{record.date}</td>
                <td className="py-2 pr-4 text-gray-700">{record.item}</td>
                <td className="py-2 pr-4 text-green-600 font-medium">+{record.amount}</td>
                <td className="py-2 pr-4 text-gray-700">{record.expireDate}</td>
                <td className="py-2 text-gray-800">{record.amount}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default StoreCredits;
