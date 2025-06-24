import Navbar from "./Header/Navbar";
import TopCarouselBar from "./Header/TopCarouselBar";
import { useLocation } from "react-router-dom";

function Header() {

  const location = useLocation();
  const isStorePage = location.pathname === "/store"; // 判斷當前頁面是不是 /store

  return (
    <>
      {/* 只有非 /store 頁面才顯示這個 bar */}
      {!isStorePage && (
        <div className="h-10 pt-[50px]">
          <TopCarouselBar />
        </div>
      )}

      {/* 這段固定在畫面最上方 */}
      <div className="fixed top-0 left-0 w-full z-50">
        <Navbar />
      </div>


    </>
  );
}

export default Header;
