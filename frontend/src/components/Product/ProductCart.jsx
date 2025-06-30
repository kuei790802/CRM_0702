import { useRef, useState } from "react";
import mainImage from "../../assets/product1.png";
import useCartStore from "../../stores/cartStore";

function ProductCart() {
  const containerRef = useRef(null);
  const imgRef = useRef(null);
  const [quantity, setQuantity] = useState(1);
  const addItem = useCartStore((state) => state.addItem);

  const handleAddToCart = () => {
    const product = {
      id: "1",
      name: "2025全新口味｜檸檬煉乳冰棒6入組",
      price: 300,
      image: mainImage,
    };

    addItem(product, quantity);
  };

  const handleMouseMove = (e) => {
    const container = containerRef.current;
    const img = imgRef.current;
    if (!container || !img) return;

    const rect = container.getBoundingClientRect();
    const x = ((e.clientX - rect.left) / rect.width) * 100;
    const y = ((e.clientY - rect.top) / rect.height) * 100;

    img.style.transformOrigin = `${x}% ${y}%`;
  };

  const handleMouseEnter = () => {
    imgRef.current.style.transform = "scale(1.4)";
  };

  const handleMouseLeave = () => {
    imgRef.current.style.transform = "scale(1)";
  };

  const handleDecrease = () => {
    setQuantity((prev) => Math.max(1, prev - 1));
  };

  const handleIncrease = () => {
    setQuantity((prev) => Math.min(99, prev + 1));
  };

  return (
    <div className="max-w-7xl mx-auto p-6 grid grid-cols-1 md:grid-cols-2 gap-8">
      {/* 圖片區 */}
      <div
        ref={containerRef}
        className="relative overflow-hidden rounded-lg border w-full h-[400px] cursor-zoom-in"
        onMouseMove={handleMouseMove}
        onMouseEnter={handleMouseEnter}
        onMouseLeave={handleMouseLeave}
      >
        <img
          ref={imgRef}
          src={mainImage}
          alt="主圖"
          className="absolute top-0 left-0 w-full h-full object-cover transition-transform duration-300 ease-in-out"
        />
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

        <div className="text-2xl font-bold text-gray-800">NT$300</div>

        <div className="flex items-center gap-4">
          {/* 數量選擇器 */}
          <div className="flex border rounded overflow-hidden text-gray-800">
            <button
              onClick={() => setQuantity(Math.max(1, quantity - 1))}
              className="px-3 py-1 bg-gray-100 hover:bg-gray-200"
            >
              -
            </button>
            <div className="px-4 py-1 bg-white border-x text-center w-12 select-none">
              {quantity}
            </div>
            <button
              onClick={() => setQuantity(Math.min(99, quantity + 1))}
              className="px-3 py-1 bg-gray-100 hover:bg-gray-200"
            >
              +
            </button>
          </div>

          {/* 加入購物車按鈕 */}
          <button
            onClick={handleAddToCart}
            className="bg-yellow-300 hover:bg-yellow-400 text-black font-bold px-6 py-2 rounded"
          >
            加入購物車
          </button>
        </div>
      </div>
    </div>
  );
}

export default ProductCart;
