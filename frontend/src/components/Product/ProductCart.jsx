import { useRef, useState } from "react";
import useCartStore from "../../stores/cartStore";

function ProductCart({ product }) {
  const containerRef = useRef(null);
  const imgRef = useRef(null);
  const [quantity, setQuantity] = useState(1);

  // 取出 store 方法（不再使用 addItem）
  const { addItemToServer, fetchCartFromServer, openCart } = useCartStore();

  if (!product) return null;

  const { id, name, price, image, descriptionList } = product;

  const handleAddToCart = async () => {
  try {
    console.log("🧪 加入購物車參數", { id, quantity });

    await addItemToServer(id, quantity);
    await fetchCartFromServer();
    openCart();
    console.log("✅ 已新增並打開購物車");
  } catch (error) {
    console.error("❌ 加入購物車失敗", error);
    console.log("🔍 錯誤詳細資訊", error?.response?.data || error.message);
  }
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

  return (
    <div className="max-w-7xl mx-auto p-6 grid grid-cols-1 md:grid-cols-2 gap-8">
      <div
        ref={containerRef}
        className="relative overflow-hidden rounded-lg border w-full h-[400px] cursor-zoom-in"
        onMouseMove={handleMouseMove}
        onMouseEnter={handleMouseEnter}
        onMouseLeave={handleMouseLeave}
      >
        <img
          ref={imgRef}
          src={image}
          alt={name}
          className="absolute top-0 left-0 w-full h-full object-cover transition-transform duration-300 ease-in-out"
        />
      </div>

      <div className="space-y-6">
        <h1 className="text-2xl md:text-3xl font-semibold">{name}</h1>

        <ul className="list-inside text-gray-700 space-y-1">
          {descriptionList?.map((desc, idx) => (
            <li key={idx}>✅ {desc}</li>
          ))}
        </ul>

        <div className="text-2xl font-bold text-gray-800">NT${price}</div>

        <div className="flex items-center gap-4">
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
