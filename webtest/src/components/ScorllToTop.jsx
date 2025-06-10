import { useEffect } from 'react';
import { useLocation } from 'react-router-dom';

const ScrollToTop = () => {
  const { pathname } = useLocation();

  useEffect(() => {
    window.scrollTo(0, 0); // 捲動到頁面頂部
  }, [pathname]);

  return null; // 此元件不需要 render 任何畫面
};

export default ScrollToTop;
