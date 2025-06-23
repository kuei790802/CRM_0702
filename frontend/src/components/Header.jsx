import Navbar from "./Header/Navbar";
import TopCarouselBar from "./Header/TopCarouselBar";

function Header() {
  return (
    <div className="fixed top-0 left-0 w-full z-50">
      {/* <div className="h-10 bg-logo-blue ">
        <TopCarouselBar />
      </div> */}
      <Navbar />
    </div>
  );
}

export default Header;
