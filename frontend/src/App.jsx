import { Outlet, useLocation } from 'react-router-dom';
import Footer from './components/Footer';
import Header from './components/Header';
import ScrollToTop from './components/ScorllToTop';
import BackToTop from "./components/tabs/BackToTop";
import ModelViewer from './components/tabs/ModelViewer';


export default function App() {
  const location = useLocation();
  const isHome = location.pathname === '/';

  return (
    <div className="min-h-screen bg-white">
      <ScrollToTop />
      <Header />
     
      <main className={isHome ? "pt-[35px]" : "pt-[35px]"}>
        <Outlet />
      </main> 
      <ModelViewer />
      <BackToTop />
      <Footer />
    </div>
  );
}
