import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import CategoryButton from "../components/Store/CategoryButton";
import ProductCard from "../components/Store/ProductCardCard";
import axios from "../api/axiosFrontend";
import ProductList from "../components/Home/ProductList";

import Img1 from '../assets/popsicle/010626.jpg';
import Img2 from '../assets/popsicle/020626.jpg';
import Img3 from '../assets/popsicle/030626.jpg';
import Img4 from '../assets/popsicle/040626.jpg';
import Img5 from '../assets/popsicle/050626.jpg';
import Img6 from '../assets/popsicle/060626.jpg';
import Img7 from '../assets/popsicle/070626.jpg';
import Img8 from '../assets/popsicle/080626.jpg';
import Img9 from '../assets/popsicle/090626.jpg';
import Img10 from '../assets/popsicle/100626.jpg';

const lightPackProducts = [
  {
    id: "062601",
    imageSrc: Img1,
    title: "良野頂級熟韻(巧克力)豆乳雪糕",
    price: "NT$300000",
    tag: "NEW"
  },
  {
    id: "062602",
    imageSrc: Img2,
    title: "良野特級日式(抹茶)豆乳雪糕",
    price: "NT$910",
    tag: "含果粒全新概念冰棒"
  },
  {
    id: "062603",
    imageSrc: Img3,
    title: "良野莓好生活(草莓)豆乳雪糕",
    price: "NT$910",

  },
  {
    id: "062604",
    imageSrc: Img4,
    title: "良野濃醇(花生)豆乳雪糕",
    price: "NT$910",

  },
  {
    id: "062605",
    imageSrc: Img5,
    title: "良野超越經典(香草)豆乳雪糕",
    price: "NT$",

  },
  {
    id: "062606",
    imageSrc: Img6,
    title: "良野大花之吻(玫瑰)豆乳雪糕",
    price: "NT$",

  },
  {
    id: "062607",
    imageSrc: Img7,
    title: "良野法式(焦糖佐脆餅)豆乳雪糕",
    price: "NT$",

  },
  {
    id: "062608",
    imageSrc: Img8,
    title: "良野濃(黑芝麻)豆乳雪糕",
    price: "NT$",

  },
  {
    id: "062609",
    imageSrc: Img9,
    title: "良野藍色狂想曲(藍莓)豆乳雪糕",
    price: "NT$",

  },
  {
    id: "062610",
    imageSrc: Img10,
    title: "良野轉轉(OREO)豆乳雪糕",
    price: "NT$",

  }
];

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
    <div className="">
      {/* 固定分類列區塊 */}
      <div className="top-[48px] z-40 bg-white shadow-md w-full">
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

      <ProductList products={lightPackProducts} />
      
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

