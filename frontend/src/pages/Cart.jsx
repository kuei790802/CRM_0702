import { useState } from "react";

function Cart() {
  const [quantity, setQuantity] = useState(1);
  const price = 910;
  const discount = 50;
  const shipping = 160;

  const handleDecrease = () => {
    if (quantity > 1) setQuantity(quantity - 1);
  };

  const handleIncrease = () => {
    setQuantity(quantity + 1);
  };

  const subtotal = price * quantity;
  const total = subtotal - discount + shipping;

  return (
    <div className="max-w-5xl mx-auto p-4 space-y-8">
      {/* 進度條 */}
      <div className="flex justify-center space-x-16 text-center">
        <div className="flex flex-col items-center">
          <div className="w-8 h-8 rounded-full bg-green-600 text-white">1</div>
          <p>購物車</p>
        </div>
        <div className="flex flex-col items-center text-gray-400">
          <div className="w-8 h-8 rounded-full bg-gray-300">2</div>
          <p>填寫資料</p>
        </div>
        <div className="flex flex-col items-center text-gray-400">
          <div className="w-8 h-8 rounded-full bg-gray-300">3</div>
          <p>訂單確認</p>
        </div>
      </div>

      {/* 購物車項目 */}
      <div className="border rounded-md p-4">
        <h2 className="text-lg font-bold mb-4">購物車（{quantity} 件）</h2>
        <div className="flex items-center justify-between border-b pb-4">
          <div className="flex items-center space-x-4">
            <img
              src="/img/product.jpg" // 你可以替換為真實圖片
              alt="大目釋迦冰棒"
              className="w-20 h-20 object-cover"
            />
            <div>
              <p className="font-medium">大目釋迦冰棒12入組 | 台東大目釋迦</p>
              <p className="text-sm text-gray-500">NT${price}</p>
            </div>
          </div>
          <div className="flex items-center space-x-2">
            <button onClick={handleDecrease} className="px-2 py-1 border">−</button>
            <span>{quantity}</span>
            <button onClick={handleIncrease} className="px-2 py-1 border">＋</button>
          </div>
          <p className="font-bold">NT${price * quantity}</p>
        </div>
      </div>

      {/* 訂單資訊 */}
      <div className="grid md:grid-cols-2 gap-6">
        <div>
          <h3 className="font-semibold mb-2">選擇送貨及付款方式</h3>
          <div className="space-y-4">
            <div>
              <label className="block mb-1 text-sm">送貨地點</label>
              <select className="w-full border p-2">
                <option>台灣</option>
              </select>
            </div>
            <div>
              <label className="block mb-1 text-sm">送貨方式</label>
              <select className="w-full border p-2">
                <option>黑貓・冷凍</option>
              </select>
            </div>
            <div>
              <label className="block mb-1 text-sm">付款方式</label>
              <select className="w-full border p-2">
                <option>LINE Pay</option>
              </select>
            </div>
          </div>
        </div>

        <div className="border p-4 rounded-md">
          <h3 className="font-semibold mb-4">訂單資訊</h3>
          <div className="space-y-2 text-sm">
            <div className="flex justify-between">
              <span>小計</span>
              <span>NT${subtotal}</span>
            </div>
            <div className="flex justify-between text-green-600">
              <span>折抵購物金</span>
              <span>-NT${discount}</span>
            </div>
            <div className="flex justify-between">
              <span>運費</span>
              <span>NT${shipping}</span>
            </div>
            <div className="border-t mt-2 pt-2 flex justify-between font-bold text-lg">
              <span>合計</span>
              <span>NT${total}</span>
            </div>
          </div>
          <button className="mt-4 w-full bg-green-600 text-white py-2 rounded hover:bg-green-700 transition">
            前往結帳
          </button>
        </div>
      </div>
    </div>
  );
}

export default Cart;
