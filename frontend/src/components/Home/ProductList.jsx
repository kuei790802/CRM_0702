// components/ProductList.jsx
import ProductCard from "./ProductCard.jsx";



function ProductList({ products }) {
  return (
    <section className="py-10 bg-[#f9f6f1] text-center">
      <h2 className="text-2xl font-semibold mb-8">輕包裝水果冰棒</h2>
      <div className="flex flex-wrap justify-center gap-6">
        {products.map((product) => (
          <ProductCard key={product.id} product={product} />
        ))}
      </div>

      <div className="mt-10">
        <button className="bg-orange-500 hover:bg-orange-600 text-white font-semibold py-2 px-6 rounded">
          查看更多
        </button>
      </div>
    </section>
  );
}

export default ProductList;
