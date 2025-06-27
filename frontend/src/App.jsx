import { Outlet, useLocation } from 'react-router-dom';
import Footer from './components/Footer';
import Header from './components/Header';
import ScrollToTop from './components/ScorllToTop';


export default function App() {
  const location = useLocation();
  const isHome = location.pathname === '/';

  return (
    <div className="min-h-screen bg-white">
      <ScrollToTop />
      <Header />
      <main className={isHome ? "pt-[0px]" : "pt-[100px]"}>
        <Outlet />
      </main>
      <Footer />
    </div>
  );
}
