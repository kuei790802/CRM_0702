// components/ProductCard.jsx
import { Link } from "react-router-dom";
import img from "../../assets/product1.png"; // 假設有一個預設圖片
function ProductCard({ product }) {
  return (
    <Link to={`/store`} className="w-full max-w-[240px] text-center group">
      <div className="relative">
        <img src={img} alt={product.title} className="w-full rounded-lg shadow-md" />
        
        {/* NEW 或 Tag 標籤 */}
        {product.tag && (
          <div className="absolute top-0 left-0 bg-red-500 text-white text-xs px-2 py-1 rounded-br">
            {product.tag}
          </div>
        )}
        
        
      </div>
      
      <p className="mt-2 text-sm text-gray-800">{product.title}</p>
      <p className="text-lg font-semibold text-gray-900">{product.price}</p>
    </Link>
  );
}

export default ProductCard;
