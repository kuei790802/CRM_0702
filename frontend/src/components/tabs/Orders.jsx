import React from 'react';

function Orders() {
  const orders = [
    {
      id: '20240306031349447',
      date: '2024-03-06',
      total: 'NT$12,496',
      status: '已完成',
      completeDate: '2024-03-09',
    },
    {
      id: '20230814040003025',
      date: '2023-08-14',
      total: 'NT$3,060',
      status: '已完成',
      completeDate: '2023-08-27',
    },
  ];

  return (
    <div className="text-sm space-y-6">
      <h2 className="text-base font-bold text-gray-800">訂單紀錄</h2>

      {/* 表格 */}
      <div className="overflow-x-auto">
        <table className="w-full table-auto border-t border-gray-300 text-left">
          <thead className="text-gray-600 border-b border-gray-200">
            <tr>
              <th className="py-2 px-4 font-semibold">訂單號碼</th>
              <th className="py-2 px-4 font-semibold">訂單日期</th>
              <th className="py-2 px-4 font-semibold">合計</th>
              <th className="py-2 px-4 font-semibold">訂單狀態</th>
              <th className="py-2 px-4 font-semibold">操作</th>
            </tr>
          </thead>
          <tbody>
            {orders.map((order, index) => (
              <tr key={index} className="border-b border-gray-100">
                <td className="py-3 px-4 text-gray-800">{order.id}</td>
                <td className="py-3 px-4 text-gray-700">{order.date}</td>
                <td className="py-3 px-4 text-gray-700">{order.total}</td>
                <td className="py-3 px-4 text-gray-700">
                  {order.status}
                  <br />
                  <span className="text-xs text-gray-500">{order.completeDate}</span>
                </td>
                <td className="py-3 px-4">
                  <button className="bg-gray-500 text-white px-4 py-1 rounded hover:bg-gray-600">
                    查閱
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* 備註 */}
      <p className="text-xs text-gray-500 text-right mt-2">僅顯示 2 年內訂單</p>
    </div>
  );
}

export default Orders;
