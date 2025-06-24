import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import CategoryButton from "../components/Store/CategoryButton";
import ProductCard from "../components/Store/ProductCardCard";
import axios from "../api/axiosFrontend";

function Store() {
  const [storeData, setStoreData] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState("全部商品");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const res = await axios.get("/cmsproducts");
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
    <div className="flex flex-col md:flex-row gap-8 p-4">
      {/* 左側分類 */}
      <div className="w-full md:w-1/4 space-y-3">
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

      {/* 右側產品 */}
      <div className="w-full md:w-3/4">
        {loading ? (
          <p>載入中...</p>
        ) : error ? (
          <p className="text-red-500">{error}</p>
        ) : (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
            {selectedSection.map((product, index) => (
              <Link to="/Product" key={index}>
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
