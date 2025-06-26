import React, { useState } from 'react';

function Coupons() {
  const [couponCode, setCouponCode] = useState('');
  const [coupons, setCoupons] = useState([
    {
      code: 'WELCOME100',
      discount: '100元折扣',
      status: '可使用',
    },
    {
      code: 'SUMMER50',
      discount: '50元折扣',
      status: '已使用',
    },
  ]);

  const handleAddCoupon = () => {
    if (!couponCode.trim()) return;

    // 模擬新增邏輯，可改成呼叫後端 API
    setCoupons((prev) => [
      {
        code: couponCode.trim(),
        discount: '新加入優惠券',
        status: '可使用',
      },
      ...prev,
    ]);
    setCouponCode('');
  };

  return (
    <div className="space-y-6 text-sm">
      <h2 className="text-base font-bold text-gray-800">我的優惠券</h2>

      {/* 輸入區域 */}
      <div className="flex items-center gap-2">
        <input
          type="text"
          placeholder="請輸入優惠碼"
          value={couponCode}
          onChange={(e) => setCouponCode(e.target.value)}
          className="border border-gray-300 rounded px-3 py-2 w-full max-w-xs"
        />
        <button
          onClick={handleAddCoupon}
          className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600"
        >
          新增
        </button>
      </div>

      {/* 清單區域 */}
      <div className="border-t border-gray-200 pt-4">
        {coupons.length === 0 ? (
          <p className="text-gray-500">尚無可用優惠券</p>
        ) : (
          <ul className="space-y-3">
            {coupons.map((coupon, index) => (
              <li
                key={index}
                className="flex justify-between items-center p-4 border border-gray-200 rounded bg-white"
              >
                <div>
                  <div className="font-semibold text-gray-800">{coupon.code}</div>
                  <div className="text-gray-600">{coupon.discount}</div>
                </div>
                <span
                  className={`text-sm font-medium ${
                    coupon.status === '可使用'
                      ? 'text-green-600'
                      : 'text-gray-400 line-through'
                  }`}
                >
                  {coupon.status}
                </span>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
}

export default Coupons;
