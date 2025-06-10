import descriptionImg from "../../assets/product1.png"; // 替換為你的圖片路徑

function ProductDescription() {
  return (
    <div className="max-w-4xl mx-auto py-12 px-4">
      <h2 className="text-center text-lg font-semibold text-gray-700">
        商品描述
      </h2>
      <div className="w-12 h-1 bg-orange-500 mx-auto my-2 rounded"></div>

      <img
        src={descriptionImg}
        alt="檸檬煉乳冰棒描述圖"
        className="w-full mt-6 rounded-lg shadow-md"
      />
    </div>
  );
}

export default ProductDescription;
