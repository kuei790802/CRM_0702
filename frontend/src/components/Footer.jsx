import { FaFacebookF, FaInstagram, FaLine } from 'react-icons/fa';
import { IoIosArrowForward } from "react-icons/io";

function Footer() {
  return (
    <footer className="bg-gray-100 text-gray-800 py-10">
      <div className="max-w-7xl mx-auto px-4">
        {/* 上方 logo + hr */}
        <hr className="border-t border-gray-300 my-4" />
        <div className="flex items-center space-x-1 text-gray-700 hover:text-black mb-6">
          <img src="/images/logo03.png" alt="Logo" className="h-7 w-auto" />
          <IoIosArrowForward className="text-sm" />
          <span className="text-sm font-medium">哈根良野</span>
        </div>
        {/* 三欄內容 */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-12">
          {/* 聯絡我們 */}
          <div className="flex flex-col text-left">
            <p className="font-semibold">聯絡我們</p>
            <div className='text-sm space-y-1'>
            <div className="font-semibold underline">哈根良野有限公司</div>
              <div>統編 55646980</div>
              <div>電話 02-23456617</div>
              <div>cs@vegsavage.com</div>
            </div>
            <div className="flex space-x-4 mt-4 text-xl">
              <a href="#"><FaFacebookF /></a>
              <a href="#"><FaInstagram /></a>
              <a href="#"><FaLine /></a>
            </div>
          </div>

          {/* 公司・店面位置 */}
          <div className="flex flex-col text-left">
            <p className="font-semibold">公司位置</p>
            <div className='text-sm space-y-1'>
            <div className="font-semibold underline">良野豆乳冰品</div>
            <div>大新里斗中路103號</div>
            <div>週一至週五 09:00-18:00</div>
            <div>(國定假日休息)</div>
            </div>
          </div>

          {/* 顧客服務 */}
          <div className="flex flex-col text-left">
            <p className="font-semibold">顧客服務</p>
            <ul className="text-sm space-y-1">
              <li><a href="#" className="hover:underline">常見問題</a></li>
              <li><a href="#" className="hover:underline">運送服務方式</a></li>
              <li><a href="#" className="hover:underline">付款服務方式</a></li>
              <li><a href="#" className="hover:underline">退換貨政策</a></li>
              <li><a href="#" className="hover:underline">條款與細則</a></li>
            </ul>
          </div>
        </div>
         <hr className="border-t border-gray-300 my-4" />
         <div className='text-center text-xs text-gray-700'>2025 © 哈根良野有限公司</div>
      </div>
    </footer>
  );
}

export default Footer;
