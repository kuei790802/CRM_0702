import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import CategoryButton from "../components/Store/CategoryButton";
import ProductCard from "../components/Store/ProductCardCard";
import axios from "../api/axiosInstance";

function Store() {
  const [storeData, setStoreData] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState("全部商品");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const res = await axios.get("/products");
        setStoreData(res.data);
      } catch (err) {
        console.error("抓取商品失敗", err);
        setError("無法載入商品資料");
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();
  }, []);

  const allProducts = storeData.flatMap(section => section.products);
  const isAll = selectedCategory === "全部商品";
  const selectedSection = isAll
    ? allProducts
    : storeData.find(section => section.category === selectedCategory)?.products || [];

  const handleSelect = (category) => {
    setSelectedCategory(category);
  };

  return (
    <div className="">
      {/* 固定分類列區塊 */}
      <div className="sticky top-[48px] z-40 bg-white shadow-md w-full">
        <div className="flex flex-wrap items-end gap-4 justify-center pt-3 px-4 sm:px-6 md:px-8">
          <CategoryButton
            label="全部商品"
            onClick={() => handleSelect("全部商品")}
            active={isAll}
          />
          {storeData.map((section, index) => (
            <CategoryButton
              key={index}
              label={section.category}
              onClick={() => handleSelect(section.category)}
              active={selectedCategory === section.category}
            />
          ))}
        </div>
      </div>

      {/* 商品列表 */}
      <div className="w-full p-10">
        {loading ? (
          <p>載入中...</p>
        ) : error ? (
          <p className="text-red-500">{error}</p>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6 max-w-[1100px] mx-auto px-2">
            {selectedSection.map((product, index) => (
              <Link to="/Product" key={index} className="w-full">
                <ProductCard {...product} />
              </Link>
            ))}
          </div>
        )}
      </div>

    </div>
  );
}

export default Store;
