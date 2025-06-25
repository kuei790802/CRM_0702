import { useState } from "react";
import mainImage from "../../assets/product1.png";
import img2 from "../../assets/product2.png";
import img3 from "../../assets/product3.png";

function ProductCart() {
  const [selectedImg, setSelectedImg] = useState(mainImage);

  const images = [mainImage, img2, img3];

  return (
    <div className="max-w-7xl mx-auto p-6 grid grid-cols-1 md:grid-cols-2 gap-8">
      {/* 圖片區 */}
      <div>
        <img
          src={selectedImg}
          alt="主圖"
          className="w-full rounded-lg border"
        />
        <div className="flex gap-4 mt-4">
          {images.map((img, idx) => (
            <img
              key={idx}
              src={img}
              alt={`圖${idx + 1}`}
              className={`w-20 h-20 object-cover border rounded cursor-pointer ${
                selectedImg === img ? "ring-2 ring-gray-500" : ""
              }`}
              onClick={() => setSelectedImg(img)}
            />
          ))}
        </div>
      </div>

      {/* 商品內容區 */}
      <div className="space-y-6">
        <h1 className="text-2xl md:text-3xl font-semibold">
          2025全新口味｜檸檬煉乳冰棒6入組
        </h1>
        <ul className="list-inside text-gray-700 space-y-1">
          <li>✅ 特選無皮油檸檬，清爽果酸香氣細緻純淨</li>
          <li>✅ 天然手作煉乳，口感濃郁</li>
          <li>✅ 全手工製作，封存真實果味</li>
          <li>✅ 完美搭配，為夏季添上一抹清涼</li>
          <li>✅ 自營工廠HACCP/ISO22000認證</li>
        </ul>

        <div className="bg-gray-50 p-4 rounded-lg text-sm">
          <p>📦 全店滿3000贈送手提不織布袋</p>
          <p>🚛 單筆滿2000免運（限母親節期間）</p>
          <p>💝 全站任選滿2000折200元（可累計）</p>
        </div>

        <div className="text-2xl font-bold text-gray-800">NT$300</div>

        <div className="flex items-center gap-4">
          <button className="bg-gray-700 hover:bg-gray-600 text-white px-6 py-2 rounded-lg">
            加入購物車
          </button>
          <button className="bg-gray-800 hover:bg-gray-900 text-white px-6 py-2 rounded-lg">
            立即購買
          </button>
        </div>

        {/* 分享
        <div className="flex gap-3 mt-6 items-center">
          <span className="text-gray-500">分享到:</span>
          <img src="/icons/line.svg" alt="Line" className="w-6 h-6" />
          <img src="/icons/facebook.svg" alt="Facebook" className="w-6 h-6" />
          <img src="/icons/whatsapp.svg" alt="WhatsApp" className="w-6 h-6" />
          <img src="/icons/link.svg" alt="Copy Link" className="w-6 h-6" />
        </div> */}
      </div>
    </div>
  );
}

export default ProductCart;
