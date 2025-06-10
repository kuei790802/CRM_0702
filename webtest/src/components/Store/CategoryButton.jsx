// components/Store/CategoryButton.jsx
import { FaIceCream } from "react-icons/fa";

function CategoryButton({ label, onClick, active }) {
  return (
    <button
      onClick={onClick}
      className={`flex items-center gap-2 px-4 py-2 rounded-lg shadow text-gray-800 w-full
        ${active ? "bg-yellow-300 font-bold" : "bg-white hover:bg-yellow-100"}`}
    >
      <FaIceCream className="text-orange-500" />
      {label}
    </button>
  );
}

export default CategoryButton;
