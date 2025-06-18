import Navbar from "./Header/Navbar";
import TopCarouselBar from "./Header/TopCarouselBar";

function Header() {
  return (
    <>
      {/* 這段會隨頁面滾動，不固定 */}
      <div className="h-10 pt-[50px]">
        <TopCarouselBar />
      </div>

      {/* 這段固定在畫面最上方 */}
      <div className="fixed top-0 left-0 w-full z-50">
        <Navbar />
      </div>
    </>
  );
}

export default Header;
