import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import useUserStore from '../stores/userStore';
import PersonalInfo from '../components/tabs/PersonalInfo';
import StoreCredits from '../components/tabs/StoreCredits';
import Coupons from '../components/tabs/Coupons';
import Messages from '../components/tabs/Messages';
import Orders from '../components/tabs/Orders';

const tabs = ['個人資訊', '商店購物金', '優惠券', '訊息', '訂單'];

const tabComponents = [
  <PersonalInfo />,
  <StoreCredits />,
  <Coupons />,
  <Messages />,
  <Orders />,
];

function User() {
  const [activeTab, setActiveTab] = useState(0);
  const navigate = useNavigate();
  const user = useUserStore((state) => state.user);
  const logout = useUserStore((state) => state.logout);

  const handleAdminRedirect = () => {
    const role = user?.role;
    if (role === 'admin') {
      navigate('/users');
    } else if (role === 'editor') {
      navigate('/cms');
    } else if (role === 'manager') {
      navigate('/erp');
    }
  };

  return (
    <div className="min-h-screen bg-[#f8f5f1] text-gray-800 px-6 py-4">
      {/* Header */}
      <div className="flex justify-between items-center mb-6 text-sm">
        <div>
          你好, <span className="font-bold">{user?.name || '訪客'}</span>
        </div>
        {['admin', 'editor', 'manager'].includes(user?.role) && (
            <button
              className="text-green-600 hover:underline"
              onClick={handleAdminRedirect}
            >
              進入後台系統
            </button>
          )}
        <button
          className="text-blue-600 hover:underline"
          onClick={() => {
            logout();
            navigate('/login');
          }}
        >
          登出
        </button>
      </div>

      {/* Tabs */}
      <div className="overflow-x-auto">
        <div className="grid grid-cols-5 min-w-[500px] border-b border-gray-300 text-center text-sm font-medium">
          {tabs.map((tab, index) => (
            <div
              key={index}
              onClick={() => setActiveTab(index)}
              className={`py-3 cursor-pointer transition-all ${
                activeTab === index
                  ? 'bg-white border-t border-l border-r border-gray-300 rounded-t-md font-bold'
                  : 'bg-[#f8f5f1] hover:bg-white'
              }`}
            >
              {tab}
            </div>
          ))}
        </div>
      </div>

      {/* Content */}
      <div className="bg-white min-h-[400px] p-6 border border-t-0 border-gray-300 rounded-b-md">
        {tabComponents[activeTab]}
      </div>
    </div>
  );
}

export default User;