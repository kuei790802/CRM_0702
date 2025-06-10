// components/Store/ProductCard.jsx
function ProductCard({ imageSrc, title, price }) {
  return (
    <div className="bg-white rounded-lg shadow-md overflow-hidden max-w-xs">
      <img src={imageSrc} alt={title} className="w-full h-48 object-cover" />
      <div className="p-4">
        <h2 className="text-lg font-semibold text-gray-800">{title}</h2>
        <p className="text-lg text-green-600 font-bold mt-2">{price}</p>
      </div>
    </div>
  );
}

export default ProductCard;
