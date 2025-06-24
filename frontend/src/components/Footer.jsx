import { FaFacebookF, FaInstagram, FaLine } from "react-icons/fa";
import { Link, useNavigate } from "react-router-dom";
import useBackUserStore from "../stores/useBackUserStore"; 

function Footer() {
  const navigate = useNavigate();
  const { isBackAuthenticated, backUser } = useBackUserStore();

  // ✅ 點擊「後台入口」後的跳轉邏輯
  const handleAdminRedirect = () => {
    if (isBackAuthenticated && backUser?.role) {
      const role = backUser.role;
      if (role === "admin") navigate("/users");
      else if (role === "editor") navigate("/cms");
      else if (role === "manager") navigate("/erp");
      else navigate("/");
    } else {
      navigate("/backlogin"); // 尚未登入則導向登入頁
    }
  };

  return (
    <footer className=" text-gray-800 py-10">
      <div className="max-w-7xl mx-auto px-4 grid grid-cols-1 md:grid-cols-3 gap-12">
        {/* 聯絡我們 */}
        <div className="flex flex-col items-center text-center">
          <h2 className="text-lg font-bold bg-logo-tan inline-block px-4 py-1 rounded-full mb-4">
            聯絡我們
          </h2>
          <p className="font-semibold">心邦有限公司</p>
          <p>統編 55646980</p>
          <p>電話 02-23456617</p>
          <p>cs@vegsavage.com</p>
          <div className="flex space-x-4 mt-4 text-xl">
            <a href="#"><FaFacebookF /></a>
            <a href="#"><FaInstagram /></a>
            <a href="#"><FaLine /></a>
          </div>
        </div>

        {/* 公司・店面位置 */}
        <div className="flex flex-col items-center text-center">
          <h2 className="text-lg font-bold bg-logo-tan inline-block px-4 py-1 rounded-full mb-4">
            公司・店面位置
          </h2>
          <p className="font-semibold">良野豆乳冰品Vegan Savage</p>
          <p className="text-sm">(心邦有限公司)</p>
          <p>大新里斗中路103號</p>
          <p>週一至週五 09:00-18:00</p>
          <p>（國定假日休息）</p>
        </div>

        {/* 顧客服務 + 後台入口 */}
        <div className="flex flex-col items-center text-center">
          <h2 className="text-lg font-bold bg-logo-tan inline-block px-4 py-1 rounded-full mb-4">
            顧客服務
          </h2>
          <ul className="space-y-1">
            <li><a href="#" className="hover:underline">常見問題</a></li>
            <li><a href="#" className="hover:underline">運送服務方式</a></li>
            <li><a href="#" className="hover:underline">付款服務方式</a></li>
            <li><a href="#" className="hover:underline">退換貨政策</a></li>
            <li>
              <Link to="/backlogin" className="hover:underline">條款與細則</Link>
            </li>
            <li>
              <button onClick={handleAdminRedirect} className="text-blue-600 hover:underline text-sm">
                後台入口
              </button>
            </li>
          </ul>
        </div>
      </div>
    </footer>
  );
}

export default Footer;
