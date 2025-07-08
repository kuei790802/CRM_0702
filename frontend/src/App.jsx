import { useEffect } from "react";
import { Outlet, useLocation } from "react-router-dom";
import Footer from "./components/Footer";
import Header from "./components/Header";
import ScrollToTop from "./components/ScorllToTop";
import BackToTop from "./components/YuYu/BackToTop";
import { ProConfigProvider } from "@ant-design/pro-components";
import { StyleProvider } from "@ant-design/cssinjs";
import { App as AntApp } from "antd";

const Router = () => {
  const location = useLocation();
  const isHome = location.pathname === "/";

  useEffect(() => {
    const frontendPaths = ["/", "/about", "/product", "/store"];
    const isFrontend = frontendPaths.some((path) =>
      location.pathname.startsWith(path)
    );

    if (isFrontend) {
      document.body.classList.add("cursor-frontend");
    } else {
      document.body.classList.remove("cursor-frontend");
    }
  }, [location.pathname]);

  return (
    <div className="min-h-screen bg-white">
      <ScrollToTop />
      <Header />
      <main className={isHome ? "pt-[0px]" : "pt-[100px]"}>
        <Outlet />
      </main>
      <BackToTop />
      <Footer />
    </div>
  );
};

const App = () => {
  return (
    <ProConfigProvider>
      <StyleProvider hashPriority="high">
        <AntApp>
          <Router />
        </AntApp>
      </StyleProvider>
    </ProConfigProvider>
  );
};

export default App;
