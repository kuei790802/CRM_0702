import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "../api/axiosFrontend";
import ProductCart from "../components/Product/ProductCart";
import ProductDescription from "../components/Product/ProductDescription";

function Product() {
  const { id } = useParams(); // 抓取網址上的 id
  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchProduct = async () => {
      try {
        const res = await axios.get("/cmsproducts");
        const allProducts = res.data.flatMap(section => section.products);
        const foundProduct = allProducts.find(p => p.id === Number(id));
        if (foundProduct) {
          setProduct(foundProduct);
        } else {
          setError("找不到該商品");
        }
      } catch (err) {
        console.error("取得商品失敗", err);
        setError("無法載入商品資料");
      } finally {
        setLoading(false);
      }
    };

    fetchProduct();
  }, [id]);

  if (loading) return <p className="p-10">載入中...</p>;
  if (error) return <p className="p-10 text-red-500">{error}</p>;

  return (
    <>
      <ProductCart product={product} />
    </>
  );
}

export default Product;
