import { createBrowserRouter } from "react-router-dom";
import App from "./App.jsx";
import Home from "./pages/Home";
import Store from "./pages/Store";
import About from "./pages/About";
import News from "./pages/News";
import Contact from "./pages/Contact";
import Login from "./pages/Login";
import FunnyError from "./components/FunnyError";
import Product from "./pages/Product.jsx";
import NewsDetail from "./pages/NewsDetail.jsx";
import User from "./pages/User.jsx";
import Sign from "./pages/Sign.jsx";
import CustDetail from "./pages/CustDetail.jsx";
import SignSuccess from "./pages/SignSuccess.jsx";
import Cart from "./pages/Cart.jsx";

import BaseLayout from "./layout/BaseLayout";
import cmsConfig from "./configs/cmsConfig";
import erpConfig from "./configs/erpConfig";
import b2cConfig from "./configs/b2cConfig";
import crmConfig from "./configs/crmConfig";
import usersConfig from "./configs/usersConfig.jsx";
import appListConfig from "./configs/appListConfig";
import useUserStore from "./stores/userStore";
import "antd/dist/reset.css";
//CRM相關頁面
import CRMCustomer from "./backpages/crm/CRMCustomer.jsx";
import CRMDashboard from "./backpages/crm/CRMDashboard.jsx";
import SalesFunnelBoard from "./backpages/crm/SalesFunnelBoard.jsx";
import CRMCalender from "./backpages/crm/CRMCalender.jsx";

const user = useUserStore.getState().user;
const role = user?.role || "guest";
const getFilteredAppList = (role) =>
  appListConfig.filter((item) => item.roles.includes(role));
const filteredAppList = getFilteredAppList(role);

const router = createBrowserRouter([
  {
    path: "/",
    element: <App />, // 前台 layout
    children: [
      { index: true, element: <Home /> },
      { path: "store", element: <Store /> },
      { path: "about", element: <About /> },
      { path: "news", element: <News /> },
      { path: "contact", element: <Contact /> },
      { path: "login", element: <Login /> },
      { path: "sign", element: <Sign /> },
      { path: "custdetail", element: <CustDetail /> },
      { path: "signsuccess", element: <SignSuccess /> },
      { path: "product", element: <Product /> },
      { path: "cart", element: <Cart /> },
      { path: "funnyerror", element: <FunnyError /> },
      { path: "user", element: <User /> },
      { path: "news/:id", element: <NewsDetail /> },
    ],
  },
  {
    path: "/b2c/*",
    element: ["admin", "editor"].includes(role) ? (
      <BaseLayout menuConfig={b2cConfig} appListConfig={filteredAppList} />
    ) : (
      <FunnyError />
    ),
  },
  {
    path: "/cms/*",
    element: ["admin", "editor"].includes(role) ? (
      <BaseLayout menuConfig={cmsConfig} appListConfig={filteredAppList} />
    ) : (
      <FunnyError />
    ),
  },
  {
    path: "/crm/*",
    element:
      role === "admin" ? (
        <BaseLayout menuConfig={crmConfig} appListConfig={filteredAppList} />
      ) : (
        <FunnyError />
      ),
      children: [
      {
        path: "dashboard", 
        element: <CRMDashboard />,
      },
      {
        path: "salesfunnel", 
        element: <SalesFunnelBoard />,
      },
      {
        path: "customer", 
        element: <CRMCustomer />,
      },
      {
        path: "calender", 
        element: <CRMCalender />,
      }
    ],
  },
  {
    path: "/erp/*",
    element: ["admin", "manager"].includes(role) ? (
      <BaseLayout menuConfig={erpConfig} appListConfig={filteredAppList} />
    ) : (
      <FunnyError />
    ),
  },
  {
    path: "/users/*",
    element:
      role === "admin" ? (
        <BaseLayout menuConfig={usersConfig} appListConfig={filteredAppList} />
      ) : (
        <FunnyError />
      ),
  },
]);

export default router;
