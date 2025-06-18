import { useState, useEffect } from "react";
import { Link, useLocation } from "react-router-dom";
import useCartStore from "../../stores/cartStore";
import CartModal from "./CartModel";
import { FaUserCircle, FaShoppingCart, FaBars, FaTimes } from "react-icons/fa";
import logo from "../../../public/images/logo04.png";

const Navbar = () => {
  const location = useLocation();
  const [isTop, setIsTop] = useState(true);
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);

  // 偵測滾動
  useEffect(() => {
    const handleScroll = () => {
      setIsTop(window.scrollY < 100);
    };
    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  // 購物車狀態
  const { toggleCart, getTotalQuantity } = useCartStore();
  const cartItemCount = getTotalQuantity();

  // 控制背景禁止滾動
  // useEffect(() => {
  //   document.body.style.overflow = isMobileMenuOpen ? "hidden" : "unset";
  // }, [isMobileMenuOpen]);

  const isHomeAtTop = location.pathname === "/" && isTop;

  const toggleMobileMenu = () => setIsMobileMenuOpen((prev) => !prev);
  const closeMobileMenu = () => setIsMobileMenuOpen(false);

  return (
    <>
      {/* 導覽列本體 */}
      <nav
        className={` transition duration-300 px-6 py-1 backdrop-blur-sm ${isHomeAtTop
            ? "bg-gray-100 text-logo-tan shadow-xs"
            : "bg-gray-100/80 text-logo-tan shadow-md"
          }`}
      >
        <div className="container mx-auto flex justify-between items-center">
          {/* 左側 logo */}
          <Link to="/" className="flex items-center space-x-2">
            <img src={logo} alt="Logo" className="h-10 w-auto object-contain" />
          </Link>

          {/* 桌面導覽連結 */}
          <div className="hidden md:flex items-center space-x-6 text-black">
            <div className="hidden md:flex items-center space-x-6 text-black absolute left-1/2 transform -translate-x-1/2">
              <Link to="/store" className="hover:text-gray-800">
                精選商店
              </Link>
              <Link to="/about" className="hover:text-gray-800">
                關於良野
              </Link>
              <Link to="/news" className="hover:text-gray-800">
                最新消息
              </Link>
              <Link to="/contact" className="hover:text-gray-800">
                聯絡我們
              </Link>
            </div>
            <Link to="/login" className="text-xl hover:text-gray-800">
              <FaUserCircle />
            </Link>
            <button onClick={toggleCart}>
              <FaShoppingCart className="text-xl hover:text-gray-800 cursor-pointer" />
              {cartItemCount > 0 && (
                <span className="absolute -top-2 -right-2 bg-red-500 text-white text-xs rounded-full h-5 w-5 flex items-center justify-center font-medium">
                  {cartItemCount > 99 ? '99+' : cartItemCount}
                </span>
              )}
            </button>
          </div>

          {/* 手機版按鈕 */}
          <div className="md:hidden">
            <button onClick={toggleMobileMenu}>
              <FaBars className="text-2xl" />
            </button>
          </div>
        </div>
      </nav>

      {/* 手機版選單 (側邊滑出 + 遮罩) */}
      <div
        className={`fixed inset-0 z-[45] transition-all duration-300 md:hidden ${isMobileMenuOpen ? "visible opacity-100" : "invisible opacity-0"
          }`}
      >
        {/* 背景遮罩 */}
        <div
          className={`absolute inset-0 bg-black transition-opacity duration-300 ${isMobileMenuOpen ? "bg-opacity-40" : "bg-opacity-0"
            }`}
          onClick={closeMobileMenu}
        ></div>

        {/* 側邊滑出選單 */}
        <div
          className={` z-[46] absolute top-0 left-0 h-full w-72 bg-logo-blue text-logo-tan transform transition-transform duration-300 ease-in-out ${isMobileMenuOpen ? "translate-x-0" : "-translate-x-full"
            }`}
        >
          <div className="flex justify-between items-center px-4 py-4 border-b border-logo-lightBlue">
            <span className="text-lg font-bold">良野選單</span>
            <button onClick={closeMobileMenu}>
              <FaTimes className="text-2xl" />
            </button>
          </div>

          <div className="px-4 py-4 space-y-4">
            <Link
              to="/store"
              onClick={closeMobileMenu}
              className="block hover:text-logo-lightBlue"
            >
              精選商店
            </Link>
            <Link
              to="/about"
              onClick={closeMobileMenu}
              className="block hover:text-logo-lightBlue"
            >
              關於良野
            </Link>
            <Link
              to="/news"
              onClick={closeMobileMenu}
              className="block hover:text-logo-lightBlue"
            >
              最新消息
            </Link>
            <Link
              to="/contact"
              onClick={closeMobileMenu}
              className="block hover:text-logo-lightBlue"
            >
              聯絡我們
            </Link>
            <Link
              to="/login"
              onClick={closeMobileMenu}
              className="flex hover:text-logo-lightBlue items-center"
            >
              <FaUserCircle className="mr-2" /> 會員登入
            </Link>
            <button
              onClick={() => {
                closeMobileMenu();
                toggleCart();
              }}
              className="flex hover:text-logo-lightBlue  items-center"
            >
              <FaShoppingCart className="mr-2" /> 購物車
              {cartItemCount > 0 && (
                <span className="ml-2 bg-logo-lightBlue text-white text-xs px-2 py-1 rounded-full">
                  {cartItemCount}
                </span>
              )}
            </button>
          </div>
        </div>
      </div>
      {/* 購物車模態框 */}
      <CartModal />
    </>
  );
};

export default Navbar;
