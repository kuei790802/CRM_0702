import { useState } from "react";
import { Link } from "react-router-dom";
import CategoryButton from "../components/Store/CategoryButton";
import ProductCard from "../components/Store/ProductCardCard";
import product1 from "../assets/product1.png";
import product2 from "../assets/product2.png";
import product3 from "../assets/product3.png";

const storeData = [
  {
    category: "贈禮盒裝水果冰棒",
    products: [
      {
        imageSrc: product1,
        title: "小小沁甜禮 mini版水果冰棒多口味24入分享盒",
        price: "NT$840"
      },
      {
        imageSrc: product2,
        title: "大目釋迦冰棒8入盒裝｜春選果香精裝禮盒",
        price: "NT$640"
      },
      {
        imageSrc: product3,
        title: "香瓜冰棒8入盒裝｜春選果香精裝禮盒",
        price: "NT$640"
      }
    ]
  },
  {
    category: "單一口味冰棒多人組",
    products: [
      {
        imageSrc: product2, // 或者你可以新增其他圖像資源
        title: "大目釋迦冰棒 20 入家庭號",
        price: "NT$1,200"
      }
    ]
  }
];

function Store() {
  const [selectedCategory, setSelectedCategory] = useState("全部商品");

  // 將所有產品平展化
  const allProducts = storeData.flatMap(section => section.products);

  const handleSelect = (category) => {
    setSelectedCategory(category);
  };

  const isAll = selectedCategory === "全部商品";
  const selectedSection = isAll ? allProducts : storeData.find(section => section.category === selectedCategory)?.products || [];

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
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
          {selectedSection.map((product, index) => (
            <Link to="/Product" key={index}>
            <ProductCard key={index} {...product} />
            </Link>
          ))}
        </div>
      </div>
    </div>
  );
}

export default Store;