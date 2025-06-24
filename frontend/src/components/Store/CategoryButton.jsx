// components/Store/CategoryButton.jsx
function CategoryButton({ label, active, onClick }) {
  return (
    <button
      onClick={onClick}
      className={`relative px-3 pb-2 text-sm font-medium transition-colors ${active ? 'text-black after:absolute after:bottom-0 after:left-0 after:w-full after:h-[5px] after:bg-gray-500' : 'text-black'
        }`}
    >
      {label}
    </button>
  );
}

export default CategoryButton;
